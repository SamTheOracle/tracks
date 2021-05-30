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

@ApplicationScoped
public class VehicleService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Inject
	VehicleDao vehicleDao;

	@Inject
	VehicleAssociationService vehicleAssociationService;

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
			vehicleAssociationService.setVehicleAssociationAsFalse(vehicle.getOwner());
			vehicleAssociation.setFavorite(true);
		} else {
			vehicleAssociation.setFavorite(false);
		}
		vehicleAssociation.setVehicle(vehicle);
		vehicleAssociation.setUserId(vehicle.getOwner());
		vehicleAssociationService.insertAssociation(vehicleAssociation);


	}

	public Optional<VehicleAssociation> getVehicleById(Integer vehicleId) {
		throw new NotFoundException("Vehicle with id " + vehicleId + "not found");
	}

	public List<VehicleAssociation> getVehiclesByOwnerId(String owner) {

		return vehicleAssociationService.getVehicleAssociationByOwner(owner);
	}

}
