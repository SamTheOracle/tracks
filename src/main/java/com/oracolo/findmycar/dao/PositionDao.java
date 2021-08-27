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
import com.oracolo.findmycar.entities.Position_;
import com.oracolo.findmycar.entities.Vehicle;

@ApplicationScoped
public class PositionDao extends BaseDao<Position> {

	public Optional<Position> getLastPosition(Vehicle vehicle) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Position> positionCriteriaQuery = cb.createQuery(Position.class);
		Root<Position> root = positionCriteriaQuery.from(Position.class);
		Order descendingOrder = cb.desc(root.get(Position_.timeStamp));
		positionCriteriaQuery.where(cb.equal(root.get(Position_.vehicle), vehicle)).orderBy(descendingOrder);
		TypedQuery<Position> query = em.createQuery(positionCriteriaQuery);
		query.setMaxResults(1);
		return query.getResultStream().findFirst();
	}

	public List<Position> getAllPositions(String owner, Vehicle vehicle) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Position> criteriaQuery = cb.createQuery(Position.class);
		Root<Position> root = criteriaQuery.from(Position.class);
		Predicate userPredicate = cb.equal(root.get(Position_.userId),owner);
		Predicate vehicleIdPredicate = cb.equal(root.get(Position_.vehicle),vehicle);
		TypedQuery<Position> typedQuery = em.createQuery(criteriaQuery.where(cb.and(vehicleIdPredicate,userPredicate)));
		return typedQuery.getResultList();
	}
}
