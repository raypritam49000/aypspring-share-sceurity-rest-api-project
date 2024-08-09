package com.share.security.rest.api.dto;

import com.share.security.rest.api.dto.base.TenantAuditableBaseDTO;
import com.share.security.rest.api.enumeration.CustomerLevel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends TenantAuditableBaseDTO {
        @NotNull
        @NotEmpty(message = "Required")
        private String username;
        @NotNull
        @NotEmpty(message = "Required")
        @Email
        private String email;
        private String password;
        @NotNull
        @NotEmpty(message = "Required")
        private List<String> roles;
        private boolean enabled;
        private boolean accountNonExpired;
        private boolean credentialsNonExpired;
        private boolean accountNonLocked;
        private int failedAttempt;
        public Date lockTime;
        public Date lastLoginDate;
        private Date lastPasswordChangeDate;
        private boolean inTraining;
        private CustomerLevel customerLevel;
}