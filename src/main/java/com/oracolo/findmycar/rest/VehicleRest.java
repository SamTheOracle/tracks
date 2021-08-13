package com.oracolo.findmycar.rest;

import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
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

import org.keycloak.KeycloakPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracolo.findmycar.BaseRest;
import com.oracolo.findmycar.Role;
import com.oracolo.findmycar.rest.converter.PositionConverter;
import com.oracolo.findmycar.rest.converter.VehicleAssociationConverter;
import com.oracolo.findmycar.rest.converter.VehicleConverter;
import com.oracolo.findmycar.rest.dto.NewVehicleDto;
import com.oracolo.findmycar.rest.dto.PositionDto;
import com.oracolo.findmycar.rest.dto.UpdateVehicleDto;
import com.oracolo.findmycar.rest.dto.VehicleAssociationDto;
import com.oracolo.findmycar.rest.dto.VehicleDto;
import com.oracolo.findmycar.service.PositionService;
import com.oracolo.findmycar.service.VehicleAssociationService;
import com.oracolo.findmycar.service.VehicleService;
import com.oracolo.findmycar.validators.NewVehicleValidator;
import com.oracolo.findmycar.validators.PositionValidator;
import com.oracolo.findmycar.validators.QueryVehicleValidator;
import com.oracolo.findmycar.validators.UpdateVehicleValidator;
import com.oracolo.findmycar.validators.VehicleAssociationValidator;

import io.quarkus.security.identity.SecurityIdentity;

@RequestScoped
@Path("tracks/vehicles")
public class VehicleRest extends BaseRest {
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

	@Inject
	VehicleAssociationValidator vehicleAssociationValidator;

	@Inject
	VehicleAssociationConverter vehicleAssociationConverter;


	@POST
	@RolesAllowed({ Role.USER,Role.OWNER })
	@Consumes(MediaType.APPLICATION_JSON)
	public void createNewVehicle(@NotNull NewVehicleDto vehicleDto) {
		newVehicleValidator.validate(vehicleDto);
		String loggedUserId = getLoggedUserId();
		vehicleService.createVehicle(vehicleConverter.from(vehicleDto,loggedUserId), vehicleDto.isFavorite);
	}

	@GET
	@Path("{vehicleId}")
	@RolesAllowed({ Role.USER,Role.OWNER })
	@Produces(MediaType.APPLICATION_JSON)
	public VehicleDto getVehicles(@PathParam("vehicleId") Integer vehicleId) {
		return vehicleConverter.toVehicleDto(
				vehicleService.getVehicleAssociationByVehicleId(vehicleId).orElseThrow(NotFoundException::new));
	}

	@GET
	@RolesAllowed({ Role.USER,Role.OWNER })
	@Produces(MediaType.APPLICATION_JSON)
	public List<VehicleDto> getVehicles() {
		String loggedUserId = getLoggedUserId();
		return vehicleConverter.toVehicleDto(vehicleAssociationService.getVehicleAssociationByOwner(loggedUserId));
	}

	@PUT
	@Path("{vehicleId}")
	@RolesAllowed(Role.OWNER)
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateVehicle(@NotNull UpdateVehicleDto updateVehicleDto, @PathParam("vehicleId") Integer vehicleId) {

		updateVehicleValidator.validate(updateVehicleDto);

		updateVehicleValidator.validateUpdateForGivenPathId(vehicleId, updateVehicleDto);

		vehicleService.updateVehicle(vehicleId, vehicleConverter.from(updateVehicleDto));
	}

	@DELETE
	@RolesAllowed(Role.OWNER)
	@Path("{vehicleId}")
	public void deleteVehicle(@PathParam("vehicleId") Integer vehicleId, @QueryParam("owner") String owner) {
		queryVehicleValidator.validateQueryParameters(owner);

		vehicleService.deleteVehicle(owner, vehicleId);

	}

	@POST
	@Path("{vehicleId}/positions")
	@RolesAllowed({Role.USER,Role.OWNER})
	@Consumes(value = MediaType.APPLICATION_JSON)
	public void createPosition(PositionDto positionDto, @PathParam("vehicleId") Integer vehicleId) {
		positionValidator.validate(positionDto);
		positionService.createNewPosition(positionConverter.from(positionDto, vehicleId));
	}

	@GET
	@Path("{vehicleId}/positions/last")
	@RolesAllowed({Role.USER,Role.OWNER})
	@Produces(value = MediaType.APPLICATION_JSON)
	public PositionDto getLastPositionForVehicle(@PathParam("vehicleId") Integer vehicleId) {
		return positionConverter.to(positionService.getLastPositionByVehicleId(vehicleId));
	}

	@POST
	@Path("{vehicleId}/associations")
	@RolesAllowed(Role.OWNER)
	public void createNewAssociation(@PathParam("vehicleId") Integer vehicleId, @NotNull VehicleAssociationDto vehicleAssociationDto) {
		vehicleAssociationValidator.validate(vehicleAssociationDto);
		vehicleAssociationService.insertAssociation(vehicleAssociationConverter.from(vehicleAssociationDto,
				vehicleService.getVehicleById(vehicleId).orElseThrow(
						() -> new BadRequestException("Creating association for non existing vehicle"))));
	}
}
