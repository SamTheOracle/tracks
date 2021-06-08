package com.oracolo.findmycar.dao;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import com.oracolo.findmycar.entities.Position;

@ApplicationScoped
public class PositionDao extends BaseDao<Position> {

	public Optional<Position> getLastPosition(Integer vehicleId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Position> PositionCriteriaQuery = cb.createQuery(Position.class);
		Root<Position> root = PositionCriteriaQuery.from(Position.class);
		Order descendingOrder = cb.desc(root.get("timeStamp"));
		PositionCriteriaQuery.where(cb.equal(root.get("vehicle"), vehicleId)).orderBy(descendingOrder);
		TypedQuery<Position> query = em.createQuery(PositionCriteriaQuery);
		query.setMaxResults(1);
		return query.getResultStream().findFirst();
	}

}
