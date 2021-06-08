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

import com.oracolo.findmycar.rest.converter.PositionConverter;
import com.oracolo.findmycar.rest.converter.VehicleConverter;
import com.oracolo.findmycar.rest.dto.NewVehicleDto;
import com.oracolo.findmycar.rest.dto.PositionDto;
import com.oracolo.findmycar.rest.dto.UpdateVehicleDto;
import com.oracolo.findmycar.rest.dto.VehicleDto;
import com.oracolo.findmycar.validators.NewVehicleValidator;
import com.oracolo.findmycar.validators.PositionValidator;
import com.oracolo.findmycar.validators.QueryVehicleValidator;
import com.oracolo.findmycar.validators.UpdateVehicleValidator;

import service.PositionService;
import service.VehicleAssociationService;
import service.VehicleService;

@Path("tracks/vehicles")
public class VehicleRest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Inject
	VehicleService vehicleService;

	@Inject
	VehicleAssociationService vehicleAssociationService;

	@Inject
	VehicleConverter vehicleConverter;

	@Inject
	UpdateVehicleValidator updateVehicleValidator;

	@Inject
	NewVehicleValidator newVehicleValidator;

	@Inject
	QueryVehicleValidator queryVehicleValidator;

	@Inject
	PositionService positionService;

	@Inject
	PositionValidator positionValidator;

	@Inject
	PositionConverter positionConverter;

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
		return vehicleConverter.toVehicleDto(
				vehicleService.getVehicleAssociationByVehicleId(vehicleId).orElseThrow(NotFoundException::new));
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<VehicleDto> getVehicles(@QueryParam("owner") String owner) {
		queryVehicleValidator.validateQueryParameters(owner);
		return vehicleConverter.toVehicleDto(vehicleAssociationService.getVehicleAssociationByOwner(owner));
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

	@POST
	@Path("{vehicleId}/positions")
	@Consumes(value = MediaType.APPLICATION_JSON)
	public void createPosition(PositionDto positionDto, @PathParam("vehicleId") Integer vehicleId) {
		positionValidator.validate(positionDto);
		positionService.createNewPosition(positionConverter.from(positionDto, vehicleId));
	}

	@GET
	@Path("{vehicleId}/positions/last")
	@Produces(value = MediaType.APPLICATION_JSON)
	public PositionDto getLastPositionForVehicle(@PathParam("vehicleId") Integer vehicleId) {
		return positionConverter.to(positionService.getLastPositionByVehicleId(vehicleId));
	}
}
