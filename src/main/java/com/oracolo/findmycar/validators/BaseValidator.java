package com.oracolo.findmycar.validators;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.ForbiddenException;

@ApplicationScoped
public class BaseValidator<T> {

	@Inject
	Validator validator;

	public void validate(T object){
		Set<ConstraintViolation<T>> violations = validator.validate(object);
		if(!violations.isEmpty()){
			String message = violations.stream().map(cv->"Error property "+cv.getPropertyPath()+": "+cv.getMessage()).collect(Collectors.joining("\n"));
			throw new ForbiddenException(message);
		}
	}

}
