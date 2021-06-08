package service;

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
		vehicleAssociationDao.insert(vehicleAssociation);
	}

	public List<VehicleAssociation> getFavoriteSelectionsByVehicleId(Integer vehicleId){
		return new ArrayList<>();
	}

	public List<VehicleAssociation> getVehicleAssociationByOwner(String owner) {
		return vehicleAssociationDao.getVehicleAssociationsByOwnerId(owner);
	}

	@Transactional
	public void setAllOwnerVehicleAssociationsAsNotFavorite(String owner) {
		vehicleAssociationDao.setAllVehicleAssociationsFavoriteToFalse(owner);
	}

	public Optional<VehicleAssociation> getVehicleAssociationByOwnerAndVehicleId(String owner, Integer id) {
		return vehicleAssociationDao.getVehicleAssociationsByOwnerIdAndVehicleId(owner,id);
	}

	@Transactional
	public void updateVehicleAssociation(VehicleAssociation vehicleAssociationEntity) {
		vehicleAssociationDao.update(vehicleAssociationEntity);
	}

	@Transactional
	public void setAllOwnerVehicleAssociationsAsNotFavoriteExceptVehicleId(String owner, Integer vehicleIdToExclude) {
		vehicleAssociationDao.setAllVehicleAssociationsFavoriteToFalse(owner,vehicleIdToExclude);
	}

	@Transactional
	public void deleteAssociation(VehicleAssociation association) {
		vehicleAssociationDao.delete(association);
	}
}
