package com.oracolo.findmycar.clients;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.oracolo.findmycar.clients.dto.RecordDto;

@RegisterRestClient
public interface RecordPublisherRestClient {

	@POST
	@Path("/services")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response postRecord(RecordDto recordDto);

}
