package com.oracolo.findmycar.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "vehicles")
public class Vehicle implements MetadataEnable, Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "vehicle_name")
	private String vehicleName;

	@Column(name = "owner")
	private String owner;

	@Column(name = "ble_hardware_mac")
	private String bleHardwareMac;

	@Version
	private int version;

	private Metadata metadata;


	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getBleHardwareMac() {
		return bleHardwareMac;
	}

	public void setBleHardwareMac(String bleHardwareMac) {
		this.bleHardwareMac = bleHardwareMac;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Vehicle))
			return false;
		Vehicle vehicle = (Vehicle) o;
		return Objects.equals(id, vehicle.id) && Objects.equals(vehicleName, vehicle.vehicleName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, vehicleName);
	}

	@Override
	public String toString() {
		return "Vehicle{" + "id=" + id + ", vehicleName='" + vehicleName + '\'' + ", owner='" + owner + '\'' + ", bleHardwareMac='"
				+ bleHardwareMac + '\'' + '}';
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
