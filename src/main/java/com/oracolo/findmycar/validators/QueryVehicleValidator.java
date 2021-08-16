package com.oracolo.findmycar.validators;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.BadRequestException;

import com.oracolo.findmycar.rest.dto.NewVehicleDto;

@ApplicationScoped
public class QueryVehicleValidator extends BaseValidator<Integer> {

}
