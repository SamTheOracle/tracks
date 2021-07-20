package com.oracolo.findmycar.exceptions;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.oracolo.findmycar.rest.dto.ErrorDto;

public class BaseExceptionMapper {

	protected static Response createResponse(int statusCode,String error,String message){
		ErrorDto errorDto = new ErrorDto();
		errorDto.error = error;
		errorDto.message = message;
		return Response.status(statusCode).entity(errorDto).type(MediaType.APPLICATION_JSON).build();
	}

}
