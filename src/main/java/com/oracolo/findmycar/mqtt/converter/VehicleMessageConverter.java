package com.oracolo.findmycar.mqtt.converter;

import javax.enterprise.context.ApplicationScoped;

import com.oracolo.findmycar.entities.VehicleAssociation;
import com.oracolo.findmycar.mqtt.enums.PersistenceAction;
import com.oracolo.findmycar.mqtt.messages.VehicleMessage;
import com.oracolo.findmycar.entities.Vehicle;

@ApplicationScoped
public class VehicleMessageConverter {

	public VehicleMessage from(VehicleAssociation vehicleAssociation, PersistenceAction persistenceAction){
		Vehicle vehicle = vehicleAssociation.getVehicle();
		VehicleMessage vehicleMessage = new VehicleMessage();
		vehicleMessage.vehicleId = vehicle.getId();
		vehicleMessage.vehicleName = vehicle.getVehicleName();
		vehicleMessage.owner = vehicle.getOwner();
		vehicleMessage.action = persistenceAction;
		vehicleMessage.isFavorite = vehicleAssociation.getFavorite();
		vehicleMessage.userId = vehicleAssociation.getUserId();
		return vehicleMessage;
	}
}
