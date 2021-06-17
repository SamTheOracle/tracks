package com.oracolo.findmycar.rest.converter;

import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.oracolo.findmycar.entities.Vehicle;
import com.oracolo.findmycar.entities.VehicleAssociation;
import com.oracolo.findmycar.rest.dto.VehicleAssociationDto;
import com.oracolo.findmycar.service.VehicleService;

@ApplicationScoped
public class VehicleAssociationConverter {


	public VehicleAssociation from(VehicleAssociationDto vehicleAssociationDto, Vehicle vehicle){
		VehicleAssociation vehicleAssociation = new VehicleAssociation();
		vehicleAssociation.setVehicle(vehicle);
		vehicleAssociation.setFavorite(Objects.requireNonNullElse(vehicleAssociationDto.isFavorite,false));
		vehicleAssociation.setUserId(vehicleAssociationDto.userId);
		return vehicleAssociation;
	}
}
