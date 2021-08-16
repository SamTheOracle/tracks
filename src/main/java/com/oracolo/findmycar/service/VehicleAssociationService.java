package com.oracolo.findmycar.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.swing.text.html.Option;
import javax.transaction.Transactional;

import com.oracolo.findmycar.dao.VehicleAssociationDao;
import com.oracolo.findmycar.entities.VehicleAssociation;

@ApplicationScoped
public class VehicleAssociationService {

	@Inject
	VehicleAssociationDao vehicleAssociationDao;

	@Transactional
	public void insertAssociation(VehicleAssociation vehicleAssociation) {
		if (vehicleAssociation.getFavorite()) {
			setAllUserVehicleAssociationsAsNotFavorite(vehicleAssociation.getUserId());
		}
		vehicleAssociationDao.insert(vehicleAssociation);
	}

	public List<VehicleAssociation> getVehicleAssociationsById(Integer vehicleId) {
		return vehicleAssociationDao.getVehicleAssociationsById(vehicleId);
	}

	public List<VehicleAssociation> getVehicleAssociationByOwner(String owner) {
		return vehicleAssociationDao.getVehicleAssociationsByOwnerId(owner);
	}

	@Transactional
	public void setAllUserVehicleAssociationsAsNotFavorite(String user) {
		vehicleAssociationDao.setAllVehicleAssociationsFavoriteToFalse(user);
	}

	public Optional<VehicleAssociation> getVehicleAssociationByUserAndVehicleId(String owner, Integer id) {
		return vehicleAssociationDao.getVehicleAssociationsByUserIdAndVehicleId(owner, id);
	}

	@Transactional
	public void updateVehicleAssociation(VehicleAssociation vehicleAssociationEntity) {
		vehicleAssociationDao.update(vehicleAssociationEntity);
	}

	@Transactional
	public void setAllUserVehicleAssociationsAsNotFavoriteExceptVehicleId(String user, Integer vehicleIdToExclude) {
		vehicleAssociationDao.setAllVehicleAssociationsFavoriteToFalse(user, vehicleIdToExclude);
	}

	@Transactional
	public void deleteAssociation(VehicleAssociation association) {
		vehicleAssociationDao.delete(association);
	}

	@Transactional
	public void updateIsFavorite(Integer vehicleId, String user, Boolean isFavorite) {
		Optional<VehicleAssociation> associationOptional = vehicleAssociationDao.getVehicleAssociationsByUserIdAndVehicleId(user,vehicleId);
		associationOptional.ifPresent(association->association.setFavorite(isFavorite));
		if(isFavorite){
			Optional<VehicleAssociation> vehicleAssociationToUpdate = vehicleAssociationDao.getVehicleAssociationsByOwnerId(user).stream().filter(
					VehicleAssociation::getFavorite).findAny();
			vehicleAssociationToUpdate.ifPresent(vehicleAssociation -> vehicleAssociation.setFavorite(false));
		}
	}

	public void deleteAssociations(List<VehicleAssociation> associations) {
		vehicleAssociationDao.delete(associations);
	}
}
