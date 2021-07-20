package com.oracolo.findmycar.exceptions;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ServerErrorExceptionMapper extends BaseExceptionMapper implements ExceptionMapper<ServerErrorException> {
	@Override
	public Response toResponse(ServerErrorException exception) {
		return createResponse(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),ServerErrorException.class.getSimpleName(),exception.getMessage());
	}
}
