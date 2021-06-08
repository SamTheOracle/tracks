package com.oracolo.findmycar.rest.converter;

import java.time.ZoneId;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import com.oracolo.findmycar.dao.PositionDao;
import com.oracolo.findmycar.entities.Position;
import com.oracolo.findmycar.rest.dto.PositionDto;

import service.VehicleService;

@ApplicationScoped
public class PositionConverter {

	@Inject
	VehicleService vehicleService;

	public Position from(PositionDto positionDto,Integer vehicleId){
		Position position = new Position();
		position.setChatId(positionDto.chatId);
		position.setLatitude(positionDto.latitude);
		position.setLongitude(positionDto.longitude);
		position.setUserId(positionDto.userId);
		position.setTimezone(positionDto.timezone);
		position.setTimeStamp(positionDto.timestamp);
		position.setVehicle(vehicleService.getVehicleById(vehicleId).orElseThrow(()->new NotFoundException("Vehicle with id "+vehicleId+" does not exist")));
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
