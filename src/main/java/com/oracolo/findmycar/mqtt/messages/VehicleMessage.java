package com.oracolo.findmycar.mqtt.messages;

import com.oracolo.findmycar.mqtt.enums.PersistenceAction;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class VehicleMessage{

	public Integer vehicleId;
	public String owner;
	public String vehicleName;
	public PersistenceAction action;
	public Boolean isFavorite;
	public String userId;

	@Override
	public String toString() {
		return "VehicleMessage{" + "vehicleId=" + vehicleId + ", owner='" + owner + '\'' + ", vehicleName='" + vehicleName + '\''
				+ ", action=" + action + ", isFavorite=" + isFavorite + ", userId='" + userId + '\'' + '}';
	}
}
