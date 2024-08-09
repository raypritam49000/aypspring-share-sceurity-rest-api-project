package com.share.security.rest.api;

import com.share.security.rest.api.entity.Permission;
import com.share.security.rest.api.entity.Role;
import com.share.security.rest.api.entity.User;
import com.share.security.rest.api.enumeration.CustomerLevel;
import com.share.security.rest.api.repository.PermissionRepository;
import com.share.security.rest.api.repository.RoleRepository;
import com.share.security.rest.api.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SpringShareSecurityRestApiProjectApplication {
    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringShareSecurityRestApiProjectApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return (args) -> {
            Set<Permission> permissions = Set.<Permission>of(
                    new Permission("USR-C", "Create User"),
                    new Permission("USR-R", "Read User"),
                    new Permission("USR-U", "Update User"),
                    new Permission("USR-D", "Delete User"),
                    new Permission("USR-V", "View User"),
                    new Permission("ROL-C", "Create Role"),
                    new Permission("ROL-R", "Read Role"),
                    new Permission("ROL-U", "Update Role"),
                    new Permission("ROL-D", "Delete Role"),
                    new Permission("ROL-V", "View Role"),
                    new Permission("PERM-C", "Create Permission"),
                    new Permission("PERM-R", "Read Permission"),
                    new Permission("PERM-U", "Update Permission"),
                    new Permission("PERM-D", "Delete Permission"),
                    new Permission("PERM-V", "View Permission")
            );

            List<Role> roles = List.of(new Role("ADMIN", permissions, 1));

           // User userEntity = userEntityRepository.save(new User("admin", "pritam.ray@apy10.com", "hashed_password", BCrypt.hashpw("admin", BCrypt.gensalt()), roles, true, true, true, true, CustomerLevel.SYS));
            //System.out.println(userEntity);
        };
    }

}
