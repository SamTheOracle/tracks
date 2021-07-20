package com.oracolo.findmycar.exceptions;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ForbiddenExceptionMapper extends BaseExceptionMapper implements ExceptionMapper<ForbiddenException> {
	@Override
	public Response toResponse(ForbiddenException exception) {
		return createResponse(Response.Status.FORBIDDEN.getStatusCode(),ForbiddenException.class.getSimpleName(),exception.getMessage());
	}
}
