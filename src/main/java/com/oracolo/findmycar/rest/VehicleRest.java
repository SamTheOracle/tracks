package com.oracolo.findmycar.rest;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.oracolo.findmycar.rest.converter.VehicleConverter;
import com.oracolo.findmycar.rest.dto.NewVehicleDto;
import com.oracolo.findmycar.rest.dto.VehicleDto;
import com.oracolo.findmycar.service.VehicleService;

@Path("tracks/vehicles")
public class VehicleRest {

	@Inject
	VehicleService vehicleService;

	@Inject
	VehicleConverter vehicleConverter;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void createNewVehicle(@Valid @NotNull NewVehicleDto vehicleDto){
		vehicleService.createVehicle(vehicleConverter.from(vehicleDto),vehicleDto.isFavorite);
	}

	@GET
	@Path("/{vehicleid}")
	@Produces(MediaType.APPLICATION_JSON)
	public VehicleDto getVehicles(@PathParam("vehicleid") Integer vehicleId) {
		return vehicleConverter.toVehicleDto(vehicleService.getVehicleById(vehicleId).orElseThrow(NotFoundException::new));
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<VehicleDto> getVehicles(@QueryParam("owner") String owner){
		return vehicleConverter.toVehicleDto(vehicleService.getVehiclesByOwnerId(owner));
	}


}
