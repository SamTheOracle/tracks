package com.oracolo.findmycar.mqtt.messages;

import org.eclipse.microprofile.reactive.messaging.Message;

import com.oracolo.findmycar.mqtt.enums.PersistenceAction;

public class VehicleMessage{

	public Integer vehicleId;
	public String owner;
	public String vehicleName;
	public PersistenceAction action;

	@Override
	public String toString() {
		return "VehicleMessage{" + "vehicleId=" + vehicleId + ", owner='" + owner + '\'' + ", vehicleName='" + vehicleName + '\''
				+ ", action=" + action + '}';
	}
}
