package com.oracolo.findmycar.rest.converter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import com.oracolo.findmycar.entities.Vehicle;
import com.oracolo.findmycar.rest.dto.NewVehicleDto;
import com.oracolo.findmycar.rest.dto.VehicleDto;

@ApplicationScoped
public class VehicleConverter {

	public Vehicle from(NewVehicleDto vehicleDto){
		Vehicle vehicle = new Vehicle();
		vehicle.setVehicleName(vehicleDto.vehicleName);
		vehicle.setBleHardwareMac(vehicleDto.bleHardware);
		vehicle.setOwner(vehicleDto.owner);

		return vehicle;
	}

	public VehicleDto toVehicleDto(Vehicle vehicle) {
		VehicleDto vehicleDto = new VehicleDto();
		vehicleDto.setOwner(vehicle.getOwner());
		return vehicleDto;
	}
	public List<VehicleDto> toVehicleDto(List<Vehicle> vehicles){
		return vehicles.stream().map(this::toVehicleDto).collect(Collectors.toUnmodifiableList());
	}
}
