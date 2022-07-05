package com.qathrin.bus.payload.request;

import javax.validation.constraints.NotBlank;

import com.qathrin.bus.model.User;
import io.swagger.annotations.ApiModelProperty;

public class AgencyRequest {
	@ApiModelProperty(hidden = true)
	private Long id;

	private String code;

	private String name;

	private String details;

	private Long owner;

	private User user;

	public AgencyRequest() {
	}

	public AgencyRequest(Long id, @NotBlank String code, @NotBlank String name, @NotBlank String details,
			@NotBlank Long owner) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.details = details;
		this.owner = owner;
	}

	public AgencyRequest(Long id, @NotBlank String code, @NotBlank String name, @NotBlank String details,
						 @NotBlank User user) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.details = details;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOwner() {
		return owner;
	}

	public void setOwner(Long owner) {
		this.owner = owner;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
