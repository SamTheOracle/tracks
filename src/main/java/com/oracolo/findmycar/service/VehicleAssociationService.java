package com.oracolo.findmycar.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import com.oracolo.findmycar.dao.VehicleAssociationDao;
import com.oracolo.findmycar.entities.VehicleAssociation;

@ApplicationScoped
public class VehicleAssociationService {

	@Inject
	VehicleAssociationDao vehicleAssociationDao;

	@Transactional
	public void insertAssociation(VehicleAssociation vehicleAssociation){
		if(vehicleAssociation.getFavorite()){
			setAllUserVehicleAssociationsAsNotFavorite(vehicleAssociation.getUserId());
		}
		vehicleAssociationDao.insert(vehicleAssociation);
	}

	public List<VehicleAssociation> getFavoriteSelectionsByVehicleId(Integer vehicleId){
		return new ArrayList<>();
	}

	public List<VehicleAssociation> getVehicleAssociationByOwner(String owner) {
		return vehicleAssociationDao.getVehicleAssociationsByOwnerId(owner);
	}

	@Transactional
	public void setAllUserVehicleAssociationsAsNotFavorite(String user) {
		vehicleAssociationDao.setAllVehicleAssociationsFavoriteToFalse(user);
	}

	public Optional<VehicleAssociation> getVehicleAssociationByUserAndVehicleId(String owner, Integer id) {
		return vehicleAssociationDao.getVehicleAssociationsByUserIdAndVehicleId(owner,id);
	}

	@Transactional
	public void updateVehicleAssociation(VehicleAssociation vehicleAssociationEntity) {
		vehicleAssociationDao.update(vehicleAssociationEntity);
	}

	@Transactional
	public void setAllUserVehicleAssociationsAsNotFavoriteExceptVehicleId(String user, Integer vehicleIdToExclude) {
		vehicleAssociationDao.setAllVehicleAssociationsFavoriteToFalse(user,vehicleIdToExclude);
	}

	@Transactional
	public void deleteAssociation(VehicleAssociation association) {
		vehicleAssociationDao.delete(association);
	}
}
