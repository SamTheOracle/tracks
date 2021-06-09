package com.oracolo.findmycar.mqtt.messages;

import com.oracolo.findmycar.mqtt.enums.PersistenceAction;

public class PositionMessage {
	public Long chatId;
	public String timeStamp;
	public String timezone;
	public String userId;
	public String longitude;
	public String latitude;
	public PersistenceAction action;

	@Override
	public String toString() {
		return "PositionMessage{" + "chatId=" + chatId + ", timeStamp='" + timeStamp + '\'' + ", timezone='" + timezone + '\''
				+ ", userId='" + userId + '\'' + ", longitude='" + longitude + '\'' + ", latitude='" + latitude + '\'' + ", action="
				+ action + '}';
	}
}
