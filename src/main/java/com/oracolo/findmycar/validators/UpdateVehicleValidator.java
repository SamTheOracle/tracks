package com.oracolo.findmycar.validators;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ForbiddenException;

import com.oracolo.findmycar.rest.dto.UpdateVehicleDto;

@ApplicationScoped
public class UpdateVehicleValidator extends BaseValidator<UpdateVehicleDto> {

	public void validateUpdateForGivenPathId(Integer pathId, UpdateVehicleDto updateVehicleDto){
		if(!pathId.equals(updateVehicleDto.id)){
			throw new ForbiddenException("Path parameter "+pathId+" is not equal to dto vehicle id "+updateVehicleDto.id);
		}
	}

}
