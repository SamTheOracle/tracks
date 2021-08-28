package com.oracolo.findmycar.rest.converter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.ForbiddenException;

import com.oracolo.findmycar.entities.Position;
import com.oracolo.findmycar.entities.Vehicle;
import com.oracolo.findmycar.entities.VehicleAssociation;
import com.oracolo.findmycar.rest.dto.PositionDto;
import com.oracolo.findmycar.service.VehicleAssociationService;
import com.oracolo.findmycar.service.VehicleService;

@ApplicationScoped
public class PositionConverter {

	@Inject
	VehicleAssociationService vehicleAssociationService;

	public Position from(PositionDto positionDto, Vehicle vehicle, String loggedUserId) {
		Position position = new Position();
		position.setChatId(positionDto.chatId);
		position.setLatitude(positionDto.latitude);
		position.setLongitude(positionDto.longitude);
		position.setUserId(loggedUserId);
		position.setTimezone(positionDto.timezone);
		position.setTimeStamp(positionDto.timestamp);
		position.setVehicle(vehicleAssociationService.getVehicleAssociationByUserAndVehicle(loggedUserId, vehicle).map(
				VehicleAssociation::getVehicle).orElseThrow(
				() -> new ForbiddenException("User with id " + loggedUserId + " has no association on vehicle " + vehicle)));
		return position;
	}

	public PositionDto to(Position position) {
		PositionDto positionDto = new PositionDto();
		positionDto.timestamp = position.getTimeStamp();
		positionDto.userId = position.getUserId();
		positionDto.longitude = position.getLongitude();
		positionDto.latitude = position.getLatitude();
		positionDto.timezone = position.getTimezone();
		positionDto.chatId = position.getChatId();
		return positionDto;
	}
}
