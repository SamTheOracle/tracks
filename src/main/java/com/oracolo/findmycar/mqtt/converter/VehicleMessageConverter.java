package com.oracolo.findmycar.mqtt.converter;

import javax.enterprise.context.ApplicationScoped;

import com.oracolo.findmycar.mqtt.enums.PersistenceAction;
import com.oracolo.findmycar.mqtt.messages.VehicleMessage;
import com.oracolo.findmycar.entities.Vehicle;

@ApplicationScoped
public class VehicleMessageConverter {

	public VehicleMessage from(Vehicle vehicle, PersistenceAction persistenceAction){
		VehicleMessage vehicleMessage = new VehicleMessage();
		vehicleMessage.vehicleId = vehicle.getId();
		vehicleMessage.vehicleName = vehicle.getVehicleName();
		vehicleMessage.owner = vehicle.getOwner();
		vehicleMessage.action = persistenceAction;
		return vehicleMessage;
	}
}
