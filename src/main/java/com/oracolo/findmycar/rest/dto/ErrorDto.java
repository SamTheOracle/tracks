package com.oracolo.findmycar.rest.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ErrorDto {
	public String error;
	public String message;
}
