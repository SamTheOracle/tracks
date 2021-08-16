package com.oracolo.findmycar.rest.dto;

import javax.validation.constraints.NotNull;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class PositionDto {
	@NotNull
	public String latitude;
	@NotNull
	public String longitude;
	@NotNull
	public String timestamp;
	@NotNull
	public String timezone;
	public String userId;
	@NotNull
	public Long chatId;
}
