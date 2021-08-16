package com.oracolo.findmycar.dao;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.oracolo.findmycar.entities.Position;

@ApplicationScoped
public class PositionDao extends BaseDao<Position> {

	public Optional<Position> getLastPosition(Integer vehicleId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Position> positionCriteriaQuery = cb.createQuery(Position.class);
		Root<Position> root = positionCriteriaQuery.from(Position.class);
		Order descendingOrder = cb.desc(root.get("timeStamp"));
		positionCriteriaQuery.where(cb.equal(root.get("vehicle"), vehicleId)).orderBy(descendingOrder);
		TypedQuery<Position> query = em.createQuery(positionCriteriaQuery);
		query.setMaxResults(1);
		return query.getResultStream().findFirst();
	}

	public void deleteAllPositionsByOwnerAndVehicleId(String userId, Integer vehicleId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<Position> criteriaDelete = cb.createCriteriaDelete(Position.class);
		Root<Position> root = criteriaDelete.from(Position.class);
		Predicate userPredicate = cb.equal(root.get("userId"),userId);
		Predicate vehicleIdPredicate = cb.equal(root.get("vehicle"),vehicleId);
		criteriaDelete.where(cb.and(vehicleIdPredicate,userPredicate));
		em.createQuery(criteriaDelete).executeUpdate();
	}

	public List<Position> getAllPositions(String owner, Integer vehicleId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Position> criteriaQuery = cb.createQuery(Position.class);
		Root<Position> root = criteriaQuery.from(Position.class);
		Predicate userPredicate = cb.equal(root.get("userId"),owner);
		Predicate vehicleIdPredicate = cb.equal(root.get("vehicle"),vehicleId);
		TypedQuery<Position> typedQuery = em.createQuery(criteriaQuery.where(cb.and(vehicleIdPredicate,userPredicate)));
		return typedQuery.getResultList();
	}
}
