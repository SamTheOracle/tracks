package com.oracolo.findmycar.validators;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ForbiddenException;

import com.oracolo.findmycar.rest.dto.PositionDto;
import com.oracolo.findmycar.rest.dto.UpdateVehicleDto;

@ApplicationScoped
public class PositionValidator extends BaseValidator<PositionDto> {

	@Override
	public void validate(PositionDto object) {
		super.validate(object);
		String timeZone = object.timezone;
		try{
			ZoneId zoneId = ZoneId.of(timeZone);
			ZonedDateTime dtoDate = ZonedDateTime.parse(object.timestamp, DateTimeFormatter.ISO_ZONED_DATE_TIME);
			ZoneId dtoDateZoneId = dtoDate.getZone();
			if(!zoneId.getId().equals(dtoDateZoneId.getId())){
				throw new IllegalArgumentException("Error: timestamp formatted with zoneId "+dtoDateZoneId.getId()+", but timezone is "+zoneId.getId());
			}
		}catch (Exception e){
			throw new ForbiddenException(e.getMessage());
		}

	}
}
