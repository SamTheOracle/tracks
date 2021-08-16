package com.oracolo.findmycar.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;

import com.oracolo.findmycar.dao.PositionDao;
import com.oracolo.findmycar.entities.Position;
import com.oracolo.findmycar.mqtt.converter.PositionMessageConverter;
import com.oracolo.findmycar.mqtt.enums.PersistenceAction;

@ApplicationScoped
public class PositionService {
	@Inject
	VehicleAssociationService vehicleAssociationService;

	@Inject
	PositionDao positionDao;

	@Inject
	MqttClientService mqttClientService;

	@Inject
	PositionMessageConverter positionMessageConverter;

	@Transactional
	public void createNewPosition(Position position) {
		positionDao.insert(position);
		mqttClientService.sendPositionMessage(positionMessageConverter.from(position, PersistenceAction.CREATE));
	}

	public Position getLastPositionByVehicleId(Integer vehicleId) {
		return positionDao.getLastPosition(vehicleId).orElseThrow(
				() -> new NotFoundException("No position found for vehicle with id " + vehicleId));
	}

	public void deleteAllPositions(String owner, Integer vehicleId) {
		List<Position> positions = positionDao.getAllPositions(owner, vehicleId);
		if (!positions.isEmpty())
			positionDao.delete(positions);
	}
}
