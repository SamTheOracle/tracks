package com.oracolo.findmycar.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.oracolo.findmycar.entities.Vehicle;
import com.oracolo.findmycar.entities.VehicleAssociation;

@ApplicationScoped
public class VehicleAssociationDao extends BaseDao<VehicleAssociation>{


	public List<VehicleAssociation> getVehicleAssociationsByOwnerId(String ownerId){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<VehicleAssociation> vehicleCriteriaQuery = cb.createQuery(VehicleAssociation.class);
		Root<VehicleAssociation> root = vehicleCriteriaQuery.from(VehicleAssociation.class);
		vehicleCriteriaQuery.where(cb.equal(root.get("userId"),ownerId));
		return em.createQuery(vehicleCriteriaQuery).getResultList();
	}

	public void setAllVehicleAssociationsFavoriteToFalse(String userId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<VehicleAssociation> vehicleAssociationCriteriaUpdate = cb.createCriteriaUpdate(VehicleAssociation.class);
		Root<VehicleAssociation> root = vehicleAssociationCriteriaUpdate.from(VehicleAssociation.class);
		vehicleAssociationCriteriaUpdate.set(root.get("isFavorite"),false)
				.set(root.get("metadata").get("lastUpdate"), LocalDateTime.now())
		.where(cb.equal(root.get("userId"),userId));

		em.createQuery(vehicleAssociationCriteriaUpdate).executeUpdate();

	}
	public void setAllVehicleAssociationsFavoriteToFalse(String userId,Integer vehicleToExclude) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<VehicleAssociation> vehicleAssociationCriteriaUpdate = cb.createCriteriaUpdate(VehicleAssociation.class);
		Root<VehicleAssociation> root = vehicleAssociationCriteriaUpdate.from(VehicleAssociation.class);
		Predicate exclusion = cb.notEqual(root.get("vehicle").get("id"),vehicleToExclude);
		Predicate inclusion = cb.equal(root.get("userId"),userId);
		vehicleAssociationCriteriaUpdate.set(root.get("isFavorite"),false)
				.set(root.get("metadata").get("lastUpdate"), LocalDateTime.now())
				.where(cb.and(exclusion,inclusion));

		em.createQuery(vehicleAssociationCriteriaUpdate).executeUpdate();

	}

	public Optional<VehicleAssociation> getVehicleAssociationsByOwnerIdAndVehicleId(String owner, Integer id) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<VehicleAssociation> vehicleCriteriaQuery = cb.createQuery(VehicleAssociation.class);
		Root<VehicleAssociation> root = vehicleCriteriaQuery.from(VehicleAssociation.class);
		Predicate ownerPredicate = cb.equal(root.get("userId"),owner);
		Predicate vehicleIdPredicate = cb.equal(root.get("id"),id);
		vehicleCriteriaQuery.where(cb.and(ownerPredicate,vehicleIdPredicate));
		return em.createQuery(vehicleCriteriaQuery).getResultStream().findFirst();
	}
}
