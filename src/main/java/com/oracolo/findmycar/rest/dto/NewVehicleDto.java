package com.oracolo.findmycar.rest.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewVehicleDto {
    @NotNull
    public String owner;
    @NotNull
    public String bleHardware;

    public String bleHumanName;
    @NotNull
    public String vehicleName;

    @NotNull
    public Boolean isFavorite;




}
