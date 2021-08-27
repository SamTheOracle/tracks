package com.oracolo.findmycar.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracolo.findmycar.dao.VehicleDao;
import com.oracolo.findmycar.entities.Vehicle;
import com.oracolo.findmycar.entities.VehicleAssociation;
import com.oracolo.findmycar.mqtt.converter.VehicleMessageConverter;
import com.oracolo.findmycar.mqtt.enums.PersistenceAction;

@ApplicationScoped
public class VehicleService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Inject
	VehicleDao vehicleDao;

	@Inject
	VehicleAssociationService vehicleAssociationService;

	@Inject
	VehicleMessageConverter vehicleMessageConverter;

	@Inject
	PositionService positionService;

	@Inject
	MqttClientService mqttClientService;

	@PostConstruct
	void init() {
		logger.info("Vehicle service started");
	}

	@Transactional
	public void createVehicle(Vehicle vehicle, boolean isFavorite) {
		Optional<Vehicle> vehicleOptional = vehicleDao.getVehicleByBleHardware(vehicle.getBleHardwareMac());
		if (vehicleOptional.isPresent())
			throw new ForbiddenException("Cannot create a new vehicle with same ble hardware " + vehicle.getBleHardwareMac());

		vehicleDao.insert(vehicle);

		VehicleAssociation vehicleAssociation = new VehicleAssociation();
		vehicleAssociation.setFavorite(isFavorite);
		vehicleAssociation.setVehicle(vehicle);
		vehicleAssociation.setUserId(vehicle.getOwner());
		vehicleAssociationService.insertAssociation(vehicleAssociation);
		mqttClientService.sendVehicleMessage(vehicleMessageConverter.from(vehicleAssociation, PersistenceAction.CREATE));
	}

	@Transactional
	public void updateVehicle(Integer vehicleId, String newVehicleName, Boolean isFavorite, String loggedUserId) {
		Optional<Vehicle> vehicleOptional = vehicleDao.getVehicleById(vehicleId);
		if (vehicleOptional.isEmpty()) {
			throw new ForbiddenException("Vehicle with id " + vehicleId + " does not exist ");
		}
		Vehicle vehicleEntity = vehicleOptional.get();
		if (!vehicleEntity.getOwner().equals(loggedUserId)) {
			throw new ForbiddenException("Vehicle is not owned by the user");
		}
		if (newVehicleName != null) {
			vehicleEntity.setVehicleName(newVehicleName);
		}
		if (isFavorite != null) {
			vehicleAssociationService.updateIsFavorite(vehicleEntity, loggedUserId, isFavorite);
		}
		vehicleDao.update(vehicleEntity);
		mqttClientService.sendVehicleMessage(vehicleMessageConverter.from(
				vehicleAssociationService.getVehicleAssociationByUserAndVehicleId(loggedUserId, vehicleEntity).orElseThrow(
						() -> new NotFoundException("Association not found")), PersistenceAction.UPDATE));
	}

	public Optional<VehicleAssociation> getVehicleAssociationByVehicleId(Integer vehicleId) {
		Optional<Vehicle> vehicleOptional = vehicleDao.getVehicleById(vehicleId);
		if (vehicleOptional.isEmpty()) {
			throw new NotFoundException("Vehicle not found for id " + vehicleId);
		}
		Vehicle vehicle = vehicleOptional.get();
		return vehicleAssociationService.getVehicleAssociationByUserAndVehicleId(vehicle.getOwner(), vehicle);
	}

	/**
	 * Only owner can remove vehicle
	 */
	@Transactional
	public void deleteVehicle(String user, Vehicle vehicle) {

		List<VehicleAssociation> associations = vehicleAssociationService.getVehicleAssociationsById(vehicle);
		if (associations.isEmpty()) {
			throw new ForbiddenException("There is no association with vehicle " + vehicle + " for owner " + user);
		}
		vehicleAssociationService.deleteAssociations(associations);

		logger.debug("Deleting positions that match userId {} and vehicleId {}", user, vehicle);
		positionService.deleteAllPositions(user, vehicle);
		vehicleDao.delete(vehicle);
		mqttClientService.sendVehicleMessage(vehicleMessageConverter.from(associations.get(0), PersistenceAction.DELETE));

	}

	public Optional<Vehicle> getVehicleById(Integer vehicleId) {
		return vehicleDao.getVehicleById(vehicleId);
	}
}
