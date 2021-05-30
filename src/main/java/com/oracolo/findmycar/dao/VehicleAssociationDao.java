package com.oracolo.findmycar.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
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

	public void setVehicleAssociationFavoriteToFalse(String owner) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<VehicleAssociation> vehicleAssociationCriteriaUpdate = cb.createCriteriaUpdate(VehicleAssociation.class);
		Root<VehicleAssociation> root = vehicleAssociationCriteriaUpdate.from(VehicleAssociation.class);
		vehicleAssociationCriteriaUpdate.set(root.get("isFavorite"),false)
		.where(cb.equal(root.get("userId"),owner));

		em.createQuery(vehicleAssociationCriteriaUpdate).executeUpdate();

	}
}
