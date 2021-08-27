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

import com.oracolo.findmycar.entities.Metadata_;
import com.oracolo.findmycar.entities.Vehicle;
import com.oracolo.findmycar.entities.VehicleAssociation;
import com.oracolo.findmycar.entities.VehicleAssociation_;

@ApplicationScoped
public class VehicleAssociationDao extends BaseDao<VehicleAssociation> {

	public List<VehicleAssociation> getVehicleAssociationsByOwnerId(String ownerId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<VehicleAssociation> vehicleCriteriaQuery = cb.createQuery(VehicleAssociation.class);
		Root<VehicleAssociation> root = vehicleCriteriaQuery.from(VehicleAssociation.class);
		vehicleCriteriaQuery.where(cb.equal(root.get(VehicleAssociation_.userId), ownerId));
		return em.createQuery(vehicleCriteriaQuery).getResultList();
	}

	public void setAllVehicleAssociationsFavoriteToFalse(String userId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<VehicleAssociation> vehicleAssociationCriteriaUpdate = cb.createCriteriaUpdate(VehicleAssociation.class);
		Root<VehicleAssociation> root = vehicleAssociationCriteriaUpdate.from(VehicleAssociation.class);
		vehicleAssociationCriteriaUpdate.set(root.get(VehicleAssociation_.isFavorite), false).set(root.get(VehicleAssociation_.metadata).get(
				Metadata_.lastUpdate),
				LocalDateTime.now()).where(cb.equal(root.get(VehicleAssociation_.userId), userId));

		em.createQuery(vehicleAssociationCriteriaUpdate).executeUpdate();

	}

	public Optional<VehicleAssociation> getVehicleAssociationsByUserIdAndVehicleId(String owner, Vehicle vehicle) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<VehicleAssociation> vehicleCriteriaQuery = cb.createQuery(VehicleAssociation.class);
		Root<VehicleAssociation> root = vehicleCriteriaQuery.from(VehicleAssociation.class);
		Predicate ownerPredicate = cb.equal(root.get(VehicleAssociation_.userId), owner);
		Predicate vehicleIdPredicate = cb.equal(root.get(VehicleAssociation_.vehicle), vehicle);
		vehicleCriteriaQuery.where(cb.and(ownerPredicate, vehicleIdPredicate));
		return em.createQuery(vehicleCriteriaQuery).getResultStream().findFirst();
	}

	public List<VehicleAssociation> getVehicleAssociationsById(Vehicle vehicle) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<VehicleAssociation> vehicleCriteriaQuery = cb.createQuery(VehicleAssociation.class);
		Root<VehicleAssociation> root = vehicleCriteriaQuery.from(VehicleAssociation.class);
		Predicate vehicleIdPredicate = cb.equal(root.get(VehicleAssociation_.vehicle), vehicle);
		vehicleCriteriaQuery.where(vehicleIdPredicate);
		return em.createQuery(vehicleCriteriaQuery).getResultList();
	}

}
