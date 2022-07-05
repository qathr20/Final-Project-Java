package com.qathrin.bus.payload.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class LowerUpperValueRequest {
	@ApiModelProperty(hidden = true)
	private Long id;

	private Integer lowerValue;

	private Integer upperValue;
}
