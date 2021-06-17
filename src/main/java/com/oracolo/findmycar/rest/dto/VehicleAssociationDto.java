package com.oracolo.findmycar.rest.dto;

import javax.validation.constraints.NotNull;

public class VehicleAssociationDto {
	@NotNull
	public String userId;
	@NotNull
	public Integer vehicleId;

	public Boolean isFavorite;
}
