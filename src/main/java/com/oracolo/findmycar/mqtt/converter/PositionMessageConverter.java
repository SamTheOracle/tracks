package com.oracolo.findmycar.mqtt.converter;

import javax.enterprise.context.ApplicationScoped;

import com.oracolo.findmycar.entities.Position;
import com.oracolo.findmycar.entities.Vehicle;
import com.oracolo.findmycar.mqtt.enums.PersistenceAction;
import com.oracolo.findmycar.mqtt.messages.PositionMessage;
import com.oracolo.findmycar.mqtt.messages.VehicleMessage;

@ApplicationScoped
public class PositionMessageConverter {

	public PositionMessage from(Position position, PersistenceAction persistenceAction){
		PositionMessage positionMessage = new PositionMessage();
		positionMessage.action = persistenceAction;
		positionMessage.chatId = position.getChatId();
		positionMessage.latitude = position.getLatitude();
		positionMessage.longitude = position.getLongitude();
		positionMessage.timezone = position.getTimezone();
		positionMessage.timeStamp = position.getTimeStamp();
		positionMessage.userId = position.getUserId();
		Vehicle vehicle = position.getVehicle();
		positionMessage.vehicleId = vehicle.getId();
		positionMessage.vehicleName = vehicle.getVehicleName();
		return positionMessage;
	}
}
