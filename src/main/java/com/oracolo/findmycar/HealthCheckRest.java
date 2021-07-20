package com.oracolo.findmycar;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.http.HttpServerRequest;

@Path("/ping")
public class HealthCheckRest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Context
	HttpServerRequest request;

	@GET
	public String ping(){
		logger.debug("Received ping from {}",request.host());
		return "pong";
	}


}
