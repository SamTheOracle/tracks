package com.oracolo.findmycar.dao;

import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.oracolo.findmycar.entities.Metadata;
import com.oracolo.findmycar.entities.MetadataEnable;

@ApplicationScoped
public class BaseDao<T> {

	@Inject
	EntityManager em;

	public void insert(T entity){
		if(entity instanceof MetadataEnable){
			MetadataEnable metadataEnable = (MetadataEnable) entity;
			Metadata metadata = new Metadata();
			metadata.setInsertDate(LocalDateTime.now());
			metadata.setDeleted(false);
			metadataEnable.setMetadata(metadata);
		}
		em.persist(entity);
	}

	public void update(T entity){
		if(entity instanceof MetadataEnable){
			Metadata metadata = ((MetadataEnable) entity).getMetadata();
			metadata.setLastUpdate(LocalDateTime.now());
		}
		em.merge(entity);
	}

}
