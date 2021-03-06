package com.oracolo.findmycar.rest.converter;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import com.oracolo.findmycar.entities.Vehicle;
import com.oracolo.findmycar.entities.VehicleAssociation;
import com.oracolo.findmycar.rest.dto.NewVehicleDto;
import com.oracolo.findmycar.rest.dto.UpdateVehicleDto;
import com.oracolo.findmycar.rest.dto.VehicleDto;

@ApplicationScoped
public class VehicleConverter {

	public Vehicle from(NewVehicleDto vehicleDto,String loggedUserId){
		Vehicle vehicle = new Vehicle();
		vehicle.setVehicleName(vehicleDto.vehicleName);
		vehicle.setBleHardwareMac(vehicleDto.bleHardware);
		vehicle.setOwner(loggedUserId);

		return vehicle;
	}
	public VehicleAssociation from(UpdateVehicleDto vehicleDto,String loggedUserId){
		Vehicle vehicle = new Vehicle();
		vehicle.setVehicleName(vehicleDto.vehicleName);
		vehicle.setId(vehicleDto.id);
		VehicleAssociation vehicleAssociation = new VehicleAssociation();
		vehicleAssociation.setVehicle(vehicle);
		vehicleAssociation.setFavorite(vehicleDto.isFavorite);
		return vehicleAssociation;
	}

	public VehicleDto to(VehicleAssociation vehicleAssociation) {
		VehicleDto vehicleDto = new VehicleDto();
		Vehicle vehicle = vehicleAssociation.getVehicle();
		vehicleDto.setOwner(vehicle.getOwner());
		vehicleDto.setId(vehicle.getId());
		vehicleDto.setBleHardware(vehicle.getBleHardwareMac());
		vehicleDto.setName(vehicle.getVehicleName());
		vehicleDto.setIsFavorite(vehicleAssociation.getFavorite());
		return vehicleDto;
	}
	public List<VehicleDto> to(List<VehicleAssociation> vehicleAssociations){
		return vehicleAssociations.stream().map(this::to).collect(Collectors.toUnmodifiableList());
	}
}
