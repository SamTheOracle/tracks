package com.oracolo.findmycar;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import io.vertx.core.http.HttpServerRequest;

@Path("/ping")
public class HealthCheckRest {
	private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

	@Context
	HttpServerRequest request;

	@GET
	public String ping(){
		logger.info("Received ping from "+request.absoluteURI());
		return "pong";
	}


}
