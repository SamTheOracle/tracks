package com.oracolo.findmycar.rest.dto;

import javax.validation.constraints.NotNull;

public class PositionDto {
	@NotNull
	public String latitude;
	@NotNull
	public String longitude;
	@NotNull
	public String timestamp;
	@NotNull
	public String timezone;
	@NotNull
	public String userId;
	@NotNull
	public Long chatId;
}
