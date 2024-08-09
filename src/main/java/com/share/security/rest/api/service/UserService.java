package com.share.security.rest.api.service;

import com.share.security.rest.api.dto.AuthTokenDTO;
import com.share.security.rest.api.dto.RoleDTO;
import com.share.security.rest.api.dto.UserDTO;

import java.util.List;

public interface UserService {
    AuthTokenDTO authenticateUser(String emailOrUsername, String password, String lockIp, String userZoneId);

    UserDTO createUser(String auth, UserDTO userDTO);

    void deleteUser(String auth, String id);

    UserDTO updateUser(String auth, String id, UserDTO userDTO);

    UserDTO findUserById(String auth, String id);

    UserDTO findUserByUsername(String auth, String username);

    UserDTO findUserByEmailInternal(String email);

    UserDTO findUserByEmail(String auth, String email);

    List<RoleDTO> findAllRolesForUser(String auth, String id);

    UserDTO findByUsernameOrEmailAndDeletedFalse(String auth, String username, String email);

    AuthTokenDTO generateRefreshToken(String refreshToken);

}
