package com.oracolo.findmycar.exceptions;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper extends BaseExceptionMapper implements ExceptionMapper<BadRequestException> {
	@Override
	public Response toResponse(BadRequestException exception) {
		return createResponse(Response.Status.BAD_REQUEST.getStatusCode(),BadRequestException.class.getSimpleName(),exception.getMessage());
	}
}
