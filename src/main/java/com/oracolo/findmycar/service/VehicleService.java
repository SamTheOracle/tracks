package com.oracolo.findmycar.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracolo.findmycar.dao.VehicleDao;
import com.oracolo.findmycar.entities.Vehicle;

@ApplicationScoped
public class VehicleService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Inject
	VehicleDao vehicleDao;

	@PostConstruct
	void init(){
		logger.info("Vehicle service started");
	}

	@Transactional
	public void createVehicle(Vehicle vehicle) {
		Optional<Vehicle> vehicleOptional = vehicleDao.getVehicleByBleHardware(vehicle.getBleHardwareMac());
		if (vehicleOptional.isPresent())
			throw new BadRequestException("Vehicle with same bleHardware is already present");

		vehicleDao.insert(vehicle);
	}

	public List<Vehicle> findVehiclesByOwnerId(String ownerId) {
		return vehicleDao.getVehiclesByOwnerId(ownerId);
	}

	public Optional<Vehicle> getVehicleById(Integer vehicleId) {
		throw new NotFoundException("Vehicle with id "+vehicleId+"not found");
	}
	public List<Vehicle> getVehiclesByOwnerId(String owner){
		return vehicleDao.getVehiclesByOwnerId(owner);
	}
}
