package com.oracolo.findmycar.service;

import com.oracolo.findmycar.rest.dto.NewVehicleDto;
import com.oracolo.findmycar.rest.dto.UpdateVehicleDto;

class BaseVehicleTest {

	protected static NewVehicleDto createNewVehicleDto(String owner, String bleHardware, String bleHumanName, boolean isFavorite,
			String vehicleName) {
		NewVehicleDto newVehicleDto = new NewVehicleDto();
		newVehicleDto.vehicleName = vehicleName;
		newVehicleDto.bleHardware = bleHardware;
		newVehicleDto.isFavorite = isFavorite;
		newVehicleDto.owner = owner;
		newVehicleDto.bleHumanName = bleHumanName;
		return newVehicleDto;
	}

	protected static NewVehicleDto createNewVehicleDto(String owner, String bleHardware, boolean isFavorite, String vehicleName) {
		NewVehicleDto newVehicleDto = new NewVehicleDto();
		newVehicleDto.vehicleName = vehicleName;
		newVehicleDto.bleHardware = bleHardware;
		newVehicleDto.isFavorite = isFavorite;
		newVehicleDto.owner = owner;
		return newVehicleDto;
	}
	protected static UpdateVehicleDto createUpdateVehicleDto(Integer id, String vehicleName, boolean isFavorite){
		UpdateVehicleDto updateVehicleDto = new UpdateVehicleDto();
		updateVehicleDto.id = id;
		updateVehicleDto.vehicleName = vehicleName;
		updateVehicleDto.isFavorite = isFavorite;
		return updateVehicleDto;
	}
}
