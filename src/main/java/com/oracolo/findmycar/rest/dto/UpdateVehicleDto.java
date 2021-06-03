package com.oracolo.findmycar.rest.dto;

import javax.validation.constraints.NotNull;

public class UpdateVehicleDto {

	@NotNull
	public Integer id;
	public String vehicleName;
	public Boolean isFavorite;
}
