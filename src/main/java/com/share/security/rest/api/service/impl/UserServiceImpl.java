package com.share.security.rest.api.service.impl;

import com.share.security.rest.api.dto.AuthTokenDTO;
import com.share.security.rest.api.dto.PermissionDTO;
import com.share.security.rest.api.dto.RoleDTO;
import com.share.security.rest.api.dto.UserDTO;
import com.share.security.rest.api.entity.Permission;
import com.share.security.rest.api.entity.Role;
import com.share.security.rest.api.entity.User;
import com.share.security.rest.api.exception.ResourceConflictException;
import com.share.security.rest.api.exception.ResourceNotFoundException;
import com.share.security.rest.api.mappers.PermissionsMapper;
import com.share.security.rest.api.mappers.RolesMapper;
import com.share.security.rest.api.mappers.UserEntityMapper;
import com.share.security.rest.api.repository.RoleRepository;
import com.share.security.rest.api.repository.UserEntityRepository;
import com.share.security.rest.api.security.jsonwebtoken.AuthTokenDetailsDTO;
import com.share.security.rest.api.security.jsonwebtoken.JsonWebTokenUtility;
import com.share.security.rest.api.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserEntityRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public AuthTokenDTO authenticateUser(String emailOrUsername, String password, String lockIp, String userZoneId) {
        User user = userRepository.findByUsernameOrEmailAndDeletedFalse(emailOrUsername, emailOrUsername).orElseThrow(() -> new ResourceNotFoundException("User not found with given username : " + emailOrUsername));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid Credentials");
        }

        var permissions = findAllPermissionsForUser(user.getId());
        var roles = findAllRolesForUserInternal(user.getId());

        List<String> permissionAbbreviations = permissions.stream().map(PermissionDTO::getAbbr).distinct().collect(toList());
        List<String> roleNames = roles.stream().map(RoleDTO::getName).distinct().collect(toList());

        AuthTokenDetailsDTO authTokenDetails = new AuthTokenDetailsDTO();
        authTokenDetails.setUserId(user.getId());
        authTokenDetails.setUsername(user.getUsername());
        authTokenDetails.setEmail(user.getEmail());
        authTokenDetails.setLogIp(lockIp);
        authTokenDetails.setCustomerLevel(user.getCustomerLevel());
        authTokenDetails.setTenant(user.getTenant());
        authTokenDetails.setRoleNames(roleNames);
        authTokenDetails.setGrantedAuthorities(permissionAbbreviations);
        authTokenDetails.setExpirationDate(buildExpirationDate());
        authTokenDetails.setLastPasswordChangeDate(user.getLastPasswordChangeDate());
        authTokenDetails.setUserZoneId(userZoneId);

        // Generate access token
        String jwt = JsonWebTokenUtility.createJsonWebToken(authTokenDetails);

        // Generate refresh token
        AuthTokenDetailsDTO refreshTokenDetails = new AuthTokenDetailsDTO();
        refreshTokenDetails.setUserId(user.getId());
        refreshTokenDetails.setUsername(user.getUsername());
        refreshTokenDetails.setEmail(user.getEmail());
        refreshTokenDetails.setLogIp(lockIp);
        refreshTokenDetails.setCustomerLevel(user.getCustomerLevel());
        refreshTokenDetails.setTenant(user.getTenant());
        refreshTokenDetails.setRoleNames(roleNames);
        refreshTokenDetails.setGrantedAuthorities(permissionAbbreviations);
        refreshTokenDetails.setLastPasswordChangeDate(user.getLastPasswordChangeDate());
        refreshTokenDetails.setUserZoneId(userZoneId);
        refreshTokenDetails.setExpirationDate(buildRefreshTokenExpirationDate());
        String refreshToken = JsonWebTokenUtility.createJsonWebToken(refreshTokenDetails);

        // Prepare the AuthTokenDTO
        AuthTokenDTO authToken = new AuthTokenDTO();
        authToken.setToken(jwt);            // Access token
        authToken.setRefreshToken(refreshToken);  // Refresh toke

        return authToken;
    }

    @Override
    public UserDTO createUser(String auth, UserDTO userDTO) {
        var authTokenDetails = JsonWebTokenUtility.parseAndValidate(auth);

        userDTO.setUsername(StringUtils.trimToNull(userDTO.getUsername()));
        userDTO.setEmail(StringUtils.trimToNull(userDTO.getEmail()));

        User existingUser = userRepository.findByUsernameAndDeletedFalse(userDTO.getUsername());

        if (Objects.nonNull(existingUser)) {
            throw new ResourceConflictException("User already register with given username : " + userDTO.getUsername());
        }

        List<Role> roles = roleRepository.findAllByNameIn(userDTO.getRoles());

        if (StringUtils.isNotEmpty(userDTO.getPassword())) {
            userDTO.setCredentialsNonExpired(true);
            userDTO.setLastPasswordChangeDate(new Date());
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        User user = new User(userDTO.getUsername(), userDTO.getEmail(), userDTO.getTenant(), userDTO.getPassword(), roles, userDTO.isEnabled(), true, true, true, userDTO.getCustomerLevel());

        assert authTokenDetails != null;
        user.setCreatedBy(authTokenDetails.getUsername());
        user.setCreationDate(new Date());

        return UserEntityMapper.INSTANCE.toDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(String auth, String id) {
        var authTokenDetails = JsonWebTokenUtility.parseAndValidate(auth);
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with given id: " + id));
        user.setDeleted(true);
        assert authTokenDetails != null;
        user.setModifiedBy(authTokenDetails.getUsername());
        userRepository.save(user);
    }

    @Override
    public UserDTO updateUser(String auth, String id, UserDTO userDTO) {
        var authTokenDetails = JsonWebTokenUtility.parseAndValidate(auth);
        assert authTokenDetails != null;

        String trimmedUsername = StringUtils.trimToNull(userDTO.getUsername());
        String trimmedEmail = StringUtils.trimToNull(userDTO.getEmail());

        userDTO.setUsername(trimmedUsername);
        userDTO.setEmail(trimmedEmail);

        var user = userRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("User not found with given id: " + id));

        if (!user.getUsername().equalsIgnoreCase(trimmedUsername) || !user.getEmail().equalsIgnoreCase(trimmedEmail)) {
            userRepository.findByUsernameOrEmailAndDeletedFalse(trimmedUsername, trimmedEmail).ifPresent(matchingUser -> {
                throw new ResourceConflictException("User already register with given username : " + matchingUser.getUsername());
            });
        }

        updateUserDetails(user, userDTO, authTokenDetails.getUsername());
        return UserEntityMapper.INSTANCE.toDto(userRepository.save(user));
    }

    @Override
    public UserDTO findUserById(String auth, String id) {
        return UserEntityMapper.INSTANCE.toDto(userRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("User not found with given id: " + id)));
    }

    @Override
    public UserDTO findUserByUsername(String auth, String username) {
        User user = userRepository.findByUsernameAndDeletedFalse(username);
        if (ObjectUtils.isEmpty(user))
            throw new ResourceNotFoundException("User not found with given username: " + username);
        return UserEntityMapper.INSTANCE.toDto(user);
    }

    @Override
    public UserDTO findUserByEmailInternal(String email) {
        return UserEntityMapper.INSTANCE.toDto(userRepository.findByEmailAndDeletedFalse(email).orElseThrow(() -> new ResourceNotFoundException("User not found with given email: " + email)));
    }

    @Override
    public UserDTO findUserByEmail(String auth, String email) {
        return UserEntityMapper.INSTANCE.toDto(userRepository.findByEmailAndDeletedFalse(email).orElseThrow(() -> new ResourceNotFoundException("User not found with given email: " + email)));
    }

    @Override
    public List<RoleDTO> findAllRolesForUser(String auth, String id) {
        var user = userRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("User not found with given id: " + id));
        return RolesMapper.INSTANCE.toDtoList(user.getRoles());
    }

    @Override
    public UserDTO findByUsernameOrEmailAndDeletedFalse(String auth, String username, String email) {
        User user = userRepository.findByUsernameOrEmailAndDeletedFalse(username, email).orElseThrow(() -> new ResourceNotFoundException("User not found with given username : " + username));
        return UserEntityMapper.INSTANCE.toDto(user);
    }

    @Override
    public AuthTokenDTO generateRefreshToken(String refreshToken) {
        // Validate the refresh token
        AuthTokenDetailsDTO refreshTokenDetails = JsonWebTokenUtility.parseAndValidate(refreshToken);

        if (Objects.isNull(refreshTokenDetails)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        User user = userRepository.findById(refreshTokenDetails.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found with ID : " + refreshTokenDetails.getUserId()));

        // Generate new access token
        AuthTokenDetailsDTO authTokenDetails = new AuthTokenDetailsDTO();
        authTokenDetails.setUserId(user.getId());
        authTokenDetails.setUsername(user.getUsername());
        authTokenDetails.setEmail(user.getEmail());
        authTokenDetails.setLogIp(refreshTokenDetails.getLogIp());
        authTokenDetails.setCustomerLevel(user.getCustomerLevel());
        authTokenDetails.setTenant(user.getTenant());
        authTokenDetails.setRoleNames(refreshTokenDetails.getRoleNames());
        authTokenDetails.setGrantedAuthorities(refreshTokenDetails.getGrantedAuthorities());
        authTokenDetails.setExpirationDate(buildExpirationDate());// Shorter expiration for access token
        authTokenDetails.setLastPasswordChangeDate(user.getLastPasswordChangeDate());
        authTokenDetails.setUserZoneId(refreshTokenDetails.getUserZoneId());
        String newJwt = JsonWebTokenUtility.createJsonWebToken(authTokenDetails);

        // Prepare the AuthTokenDTO
        AuthTokenDTO authToken = new AuthTokenDTO();
        authToken.setToken(newJwt);            // New Access token
        authToken.setRefreshToken(refreshToken);  // Return the same refresh token

        return authToken;
    }

    private void updateUserDetails(User user, UserDTO userDTO, String modifiedBy) {
        user.setEmail(userDTO.getEmail());
        user.setEnabled(userDTO.isEnabled());
        user.setAccountNonLocked(userDTO.isAccountNonLocked());
        user.setAccountNonExpired(userDTO.isAccountNonExpired());
        user.setCredentialsNonExpired(userDTO.isCredentialsNonExpired());
        user.setInTraining(userDTO.isInTraining());
        user.setModifiedBy(modifiedBy);
        user.setModifiedDate(new Date());
        user.setRoles(roleRepository.findAllByNameIn(userDTO.getRoles()));
    }

    private List<PermissionDTO> findAllPermissionsForUser(String id) {
        User user = userRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("User not found with given id: " + id));
        List<Permission> permissions = new ArrayList<>();
        for (Role role : user.getRoles()) {
            permissions.addAll(role.getPermissions());
        }
        return PermissionsMapper.INSTANCE.toDtoList(permissions);
    }

    private List<RoleDTO> findAllRolesForUserInternal(String id) {
        User user = userRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("User not found with given id: " + id));
        List<Role> roles = user.getRoles();
        return RolesMapper.INSTANCE.toDtoList(roles);
    }

    private Date buildExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 12);
        return calendar.getTime();
    }

    private Date buildRefreshTokenExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 30); // Set the refresh token to expire in 30 days (or any other period)
        return calendar.getTime();
    }
}
