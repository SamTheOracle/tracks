package com.oracolo.findmycar.rest;

import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracolo.findmycar.rest.converter.VehicleConverter;
import com.oracolo.findmycar.rest.dto.NewVehicleDto;
import com.oracolo.findmycar.rest.dto.UpdateVehicleDto;
import com.oracolo.findmycar.rest.dto.VehicleDto;
import com.oracolo.findmycar.service.VehicleService;
import com.oracolo.findmycar.validators.NewVehicleValidator;
import com.oracolo.findmycar.validators.QueryVehicleValidator;
import com.oracolo.findmycar.validators.UpdateVehicleValidator;

@Path("tracks/vehicles")
public class VehicleRest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Inject
	VehicleService vehicleService;

	@Inject
	VehicleConverter vehicleConverter;

	@Inject
	UpdateVehicleValidator updateVehicleValidator;

	@Inject
	NewVehicleValidator newVehicleValidator;

	@Inject
	QueryVehicleValidator queryVehicleValidator;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void createNewVehicle(@NotNull NewVehicleDto vehicleDto) {
		newVehicleValidator.validate(vehicleDto);

		vehicleService.createVehicle(vehicleConverter.from(vehicleDto), vehicleDto.isFavorite);
	}

	@GET
	@Path("{vehicleId}")
	@Produces(MediaType.APPLICATION_JSON)
	public VehicleDto getVehicles(@PathParam("vehicleId") Integer vehicleId) {
		return vehicleConverter.toVehicleDto(vehicleService.getVehicleById(vehicleId).orElseThrow(NotFoundException::new));
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<VehicleDto> getVehicles(@QueryParam("owner") String owner) {
		queryVehicleValidator.validateQueryParameters(owner);
		return vehicleConverter.toVehicleDto(vehicleService.getVehiclesByOwnerId(owner));
	}

	@PUT
	@Path("{vehicleId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateVehicle(@NotNull UpdateVehicleDto updateVehicleDto, @PathParam("vehicleId") Integer vehicleId) {

		updateVehicleValidator.validate(updateVehicleDto);

		updateVehicleValidator.validateUpdateForGivenPathId(vehicleId, updateVehicleDto);

		vehicleService.updateVehicle(vehicleId, vehicleConverter.from(updateVehicleDto));
	}

	@DELETE
	@Path("{vehicleId}")
	public void deleteVehicle(@PathParam("vehicleId") Integer vehicleId, @QueryParam("owner") String owner) {
		queryVehicleValidator.validateQueryParameters(owner);

		vehicleService.deleteVehicle(owner, vehicleId);

	}

}
