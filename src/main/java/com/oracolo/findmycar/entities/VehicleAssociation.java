package com.oracolo.findmycar.entities;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "vehicle_associations")
public class VehicleAssociation implements MetadataEnable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "userid")
	private String userId;

	@Column(name = "is_favorite")
	private Boolean isFavorite;

	@ManyToOne(cascade = CascadeType.ALL)
	private Vehicle vehicle;

	@Version
	private int version;

	private Metadata metadata;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Boolean getFavorite() {
		return isFavorite;
	}

	public void setFavorite(Boolean favorite) {
		isFavorite = favorite;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	@Override
	public String toString() {
		return "VehicleAssociation{" + "id=" + id + ", userId='" + userId + '\'' + ", isFavorite=" + isFavorite + ", vehicle=" + vehicle
				+ ", version=" + version + ", metadata=" + metadata + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof VehicleAssociation))
			return false;
		VehicleAssociation that = (VehicleAssociation) o;
		return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(isFavorite, that.isFavorite)
				&& Objects.equals(vehicle, that.vehicle);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, userId, isFavorite, vehicle);
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

