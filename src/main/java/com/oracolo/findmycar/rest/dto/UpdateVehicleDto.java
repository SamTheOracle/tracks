package com.oracolo.findmycar.rest.dto;

import javax.validation.constraints.NotNull;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class UpdateVehicleDto {

	@NotNull
	public Integer id;
	public String vehicleName;
	public Boolean isFavorite;
}
