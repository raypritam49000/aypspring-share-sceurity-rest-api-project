package com.share.security.rest.api.dto.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditableBaseDTO extends BaseDTO {
    protected Date creationDate;
    protected Date modifiedDate;
    protected String createdBy;
    protected String modifiedBy;
    protected boolean deleted;
}