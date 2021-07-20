package com.oracolo.findmycar.entities;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "positions")
public class Position implements MetadataEnable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "latitude")
	private String latitude;

	@Column(name = "longitude")
	private String longitude;

	@Column(name = "chatid")
	private Long chatId;

	@Column(name = "userid")
	private String userId;

	@Column(name = "timezone")
	private String timezone;

	@Column(name = "user_timestamp")
	private String timeStamp;


	@ManyToOne
	private Vehicle vehicle;

	@Version
	private int version;

	private Metadata metadata;

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Long getChatId() {
		return chatId;
	}

	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}



	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Position))
			return false;
		Position position = (Position) o;
		return Objects.equals(id, position.id) && Objects.equals(latitude, position.latitude) && Objects.equals(longitude,
				position.longitude) && Objects.equals(chatId, position.chatId) && Objects.equals(userId, position.userId) && Objects.equals(
				timezone, position.timezone) && Objects.equals(vehicle, position.vehicle);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Position{" + "id=" + id + ", latitude='" + latitude + '\'' + ", longitude='" + longitude + '\'' + ", chatId=" + chatId
				+ ", userId='" + userId + '\'' + ", timezone='" + timezone + '\'' + ", timeStamp='" + timeStamp + '\'' + ", vehicle="
				+ vehicle + ", version=" + version + ", metadata=" + metadata + '}';
	}

	@Override
	public Metadata getMetadata() {
		return metadata;
	}

	@Override
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
}
