package com.share.security.rest.api.security.jsonwebtoken;

import com.share.security.rest.api.enumeration.CustomerLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenDetailsDTO {
    private String userId;
    private String username;
    private String email;
    private String customerId;
    private CustomerLevel customerLevel;
    private String branchId;
    private String logIp;
    private String tenant;
    private List<String> roleNames;
    private List<String> grantedAuthorities;
    private Date expirationDate;
    private Date lastPasswordChangeDate;
    private String userZoneId;

    public static class Builder {
        private String userId;
        private String username;
        private String email;
        private String customerId;
        private CustomerLevel customerLevel;
        private String branchId;
        private String logIp;
        private String tenant;
        private List<String> roleNames;
        private List<String> grantedAuthorities;
        private Date expirationDate;
        private Date lastPasswordChangeDate;
        private String userZoneId;

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder withCustomerLevel(CustomerLevel customerLevel) {
            this.customerLevel = customerLevel;
            return this;
        }

        public Builder withBranchId(String branchId) {
            this.branchId = branchId;
            return this;
        }

        public Builder withLogIp(String logIp) {
            this.logIp = logIp;
            return this;
        }

        public Builder withTenant(String tenant) {
            this.tenant = tenant;
            return this;
        }

        public Builder withRoleNames(List<String> roleNames) {
            this.roleNames = roleNames;
            return this;
        }

        public Builder withGrantedAuthorities(List<String> grantedAuthorities) {
            this.grantedAuthorities = grantedAuthorities;
            return this;
        }

        public Builder withExpirationDate(Date expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public Builder withLastPasswordChangeDate(Date lastPasswordChangeDate) {
            this.lastPasswordChangeDate = lastPasswordChangeDate;
            return this;
        }

        public Builder withUserZoneId(String userZoneId) {
            this.userZoneId = userZoneId;
            return this;
        }

        public AuthTokenDetailsDTO build() {
            AuthTokenDetailsDTO dto = new AuthTokenDetailsDTO();
            dto.userId = this.userId;
            dto.username = this.username;
            dto.email = this.email;
            dto.customerId = this.customerId;
            dto.customerLevel = this.customerLevel;
            dto.branchId = this.branchId;
            dto.logIp = this.logIp;
            dto.tenant = this.tenant;
            dto.roleNames = this.roleNames;
            dto.grantedAuthorities = this.grantedAuthorities;
            dto.expirationDate = this.expirationDate;
            dto.lastPasswordChangeDate = this.lastPasswordChangeDate;
            dto.userZoneId = this.userZoneId;
            return dto;
        }
    }
}