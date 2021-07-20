package com.oracolo.findmycar.validators;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ForbiddenException;

import com.oracolo.findmycar.rest.dto.UpdateVehicleDto;
import com.oracolo.findmycar.rest.dto.VehicleAssociationDto;

@ApplicationScoped
public class VehicleAssociationValidator extends BaseValidator<VehicleAssociationDto> {

}
