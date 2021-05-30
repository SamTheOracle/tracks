package com.oracolo.findmycar.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.oracolo.findmycar.dao.VehicleAssociationDao;
import com.oracolo.findmycar.entities.VehicleAssociation;

@ApplicationScoped
public class VehicleAssociationService {

	@Inject
	VehicleAssociationDao vehicleAssociationDao;

	public void insertAssociation(VehicleAssociation vehicleAssociation){
		vehicleAssociationDao.insert(vehicleAssociation);
	}

	public List<VehicleAssociation> getFavoriteSelectionsByVehicleId(Integer vehicleId){
		return new ArrayList<>();
	}

	public List<VehicleAssociation> getVehicleAssociationByOwner(String owner) {
		return vehicleAssociationDao.getVehicleAssociationsByOwnerId(owner);
	}

	public void setVehicleAssociationAsFalse(String owner) {
		vehicleAssociationDao.setVehicleAssociationFavoriteToFalse(owner);
	}
}
