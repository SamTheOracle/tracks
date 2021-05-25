package com.oracolo.findmycar.dao;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.oracolo.findmycar.entities.Vehicle;

@ApplicationScoped
public class VehicleDao extends BaseDao<Vehicle>{

	public List<Vehicle> getVehiclesByOwnerId(String ownerId){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Vehicle> vehicleCriteriaQuery = cb.createQuery(Vehicle.class);
		Root<Vehicle> root = vehicleCriteriaQuery.from(Vehicle.class);
		vehicleCriteriaQuery.where(cb.equal(root.get("owner"),ownerId));
		return em.createQuery(vehicleCriteriaQuery).getResultList();
	}

	public Optional<Vehicle> getVehicleByBleHardware(String bleHardwareMac) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Vehicle> vehicleCriteriaQuery = cb.createQuery(Vehicle.class);
		Root<Vehicle> root = vehicleCriteriaQuery.from(Vehicle.class);
		vehicleCriteriaQuery.where(cb.equal(root.get("bleHardwareMac"),bleHardwareMac));
		TypedQuery<Vehicle> query = em.createQuery(vehicleCriteriaQuery);
		query.setMaxResults(1);
		return query.getResultStream().findFirst();
	}
}
