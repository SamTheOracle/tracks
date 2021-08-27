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
import com.oracolo.findmycar.entities.Vehicle;
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

	public List<VehicleAssociation> getVehicleAssociationsById(Vehicle vehicle) {
		return vehicleAssociationDao.getVehicleAssociationsById(vehicle);
	}

	public List<VehicleAssociation> getVehicleAssociationByOwner(String owner) {
		return vehicleAssociationDao.getVehicleAssociationsByOwnerId(owner);
	}

	@Transactional
	public void setAllUserVehicleAssociationsAsNotFavorite(String user) {
		vehicleAssociationDao.setAllVehicleAssociationsFavoriteToFalse(user);
	}

	public Optional<VehicleAssociation> getVehicleAssociationByUserAndVehicleId(String owner, Vehicle vehicle) {
		return vehicleAssociationDao.getVehicleAssociationsByUserIdAndVehicleId(owner, vehicle);
	}

	@Transactional
	public void updateIsFavorite(Vehicle vehicle, String user, Boolean isFavorite) {
		Optional<VehicleAssociation> associationOptional = vehicleAssociationDao.getVehicleAssociationsByUserIdAndVehicleId(user,vehicle);
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
