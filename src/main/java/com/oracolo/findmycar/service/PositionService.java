package com.oracolo.findmycar.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;

import com.oracolo.findmycar.dao.PositionDao;
import com.oracolo.findmycar.entities.Position;
import com.oracolo.findmycar.mqtt.PositionPublisher;
import com.oracolo.findmycar.mqtt.converter.PositionMessageConverter;
import com.oracolo.findmycar.mqtt.enums.PersistenceAction;

@ApplicationScoped
public class PositionService {
	@Inject
	VehicleAssociationService vehicleAssociationService;

	@Inject
	PositionDao positionDao;

	@Inject
	PositionPublisher positionPublisher;

	@Inject
	PositionMessageConverter positionMessageConverter;

	@Transactional
	public void createNewPosition(Position position) {
		positionDao.insert(position);
		positionPublisher.sendPosition(positionMessageConverter.from(position, PersistenceAction.CREATE));
	}

	public Position getLastPositionByVehicleId(Integer vehicleId) {
		return positionDao.getLastPosition(vehicleId).orElseThrow(
				() -> new NotFoundException("No position found for vehicle with id " + vehicleId));
	}
}
