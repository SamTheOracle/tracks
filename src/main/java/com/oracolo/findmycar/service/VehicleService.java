package com.oracolo.findmycar.service;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracolo.findmycar.mqtt.VehiclePublisher;
import com.oracolo.findmycar.mqtt.converter.VehicleMessageConverter;
import com.oracolo.findmycar.mqtt.enums.PersistenceAction;
import com.oracolo.findmycar.dao.VehicleDao;
import com.oracolo.findmycar.entities.Vehicle;
import com.oracolo.findmycar.entities.VehicleAssociation;

@ApplicationScoped
public class VehicleService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Inject
	VehicleDao vehicleDao;

	@Inject
	VehicleAssociationService vehicleAssociationService;

	@Inject
	VehiclePublisher vehiclePublisher;

	@Inject
	VehicleMessageConverter vehicleMessageConverter;

	@Inject
	ManagedExecutor executor;

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
		if (isFavorite) {
			vehicleAssociationService.setAllUserVehicleAssociationsAsNotFavorite(vehicle.getOwner());
			vehicleAssociation.setFavorite(true);
		} else {
			vehicleAssociation.setFavorite(false);
		}
		vehicleAssociation.setVehicle(vehicle);
		vehicleAssociation.setUserId(vehicle.getOwner());
		vehicleAssociationService.insertAssociation(vehicleAssociation);
		vehiclePublisher.sendMessage(vehicleMessageConverter.from(vehicleAssociation, PersistenceAction.CREATE));
	}

	@Transactional
	public void updateVehicle(Integer vehicleId, VehicleAssociation vehicleAssociation) {
		Optional<Vehicle> vehicleOptional = vehicleDao.getVehicleById(vehicleId);
		if (vehicleOptional.isEmpty()) {
			throw new ForbiddenException("Vehicle with id " + vehicleId + " does not exist ");
		}
		Vehicle vehicleEntity = vehicleOptional.get();

		String vehicleName = vehicleAssociation.getVehicle().getVehicleName();
		if (vehicleName != null) {
			vehicleEntity.setVehicleName(vehicleAssociation.getVehicle().getVehicleName());
		}
		String owner = vehicleAssociation.getVehicle().getOwner();
		if (owner != null) {
			//must check if there is an association for the new owner;
			vehicleEntity.setOwner(owner);
		}
		VehicleAssociation vehicleAssociationEntity = vehicleAssociationService.getVehicleAssociationByUserAndVehicleId(
				vehicleEntity.getOwner(), vehicleEntity.getId()).orElseThrow(() -> new ForbiddenException(
				"There are no vehicle association for given vehicle id " + vehicleId + " and owner " + vehicleEntity.getOwner()));
		Boolean isFavorite = vehicleAssociation.getFavorite();
		if (isFavorite != null && isFavorite) {
			vehicleAssociationEntity.setFavorite(true);
			vehicleAssociationService.updateVehicleAssociation(vehicleAssociationEntity);
			vehicleAssociationService.setAllUserVehicleAssociationsAsNotFavoriteExceptVehicleId(vehicleEntity.getOwner(), vehicleId);

		}
		if (isFavorite != null && !isFavorite) {
			vehicleAssociationService.setAllUserVehicleAssociationsAsNotFavorite(vehicleEntity.getOwner());
		}
		vehicleDao.update(vehicleEntity);
		vehiclePublisher.sendMessage(vehicleMessageConverter.from(vehicleAssociationEntity, PersistenceAction.UPDATE));
	}

	public Optional<VehicleAssociation> getVehicleAssociationByVehicleId(Integer vehicleId) {
		Optional<Vehicle> vehicleOptional = vehicleDao.getVehicleById(vehicleId);
		if (vehicleOptional.isEmpty()) {
			throw new NotFoundException("Vehicle not found for id " + vehicleId);
		}
		Vehicle vehicle = vehicleOptional.get();
		return vehicleAssociationService.getVehicleAssociationByUserAndVehicleId(vehicle.getOwner(), vehicleId);
	}

	@Transactional
	public void deleteVehicle(String owner, Integer vehicleId) {
		Optional<VehicleAssociation> vehicleAssociationOptional = vehicleAssociationService.getVehicleAssociationByUserAndVehicleId(owner,
				vehicleId);
		if (vehicleAssociationOptional.isEmpty()) {
			throw new ForbiddenException("There is no association with vehicle " + vehicleId + " for owner " + owner);
		}
		VehicleAssociation association = vehicleAssociationOptional.get();
		logger.debug("Deleting vehicle association {}", association);
		vehicleAssociationService.deleteAssociation(association);
		Vehicle vehicle = association.getVehicle();
		logger.debug("Deleting vehicle {}", vehicle);
		vehicleDao.delete(association.getVehicle());
		vehiclePublisher.sendMessage(vehicleMessageConverter.from(association, PersistenceAction.DELETE));

	}

	public Optional<Vehicle> getVehicleById(Integer vehicleId) {
		return vehicleDao.getVehicleById(vehicleId);
	}
}
