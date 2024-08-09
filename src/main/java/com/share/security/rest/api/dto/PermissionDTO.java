package com.share.security.rest.api.dto;

import com.share.security.rest.api.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDTO extends BaseDTO {
	private String abbr;
	private String name;
}