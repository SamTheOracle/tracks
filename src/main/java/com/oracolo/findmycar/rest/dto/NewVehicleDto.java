package com.oracolo.findmycar.rest.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.runtime.annotations.RegisterForReflection;

@JsonInclude(JsonInclude.Include.NON_NULL)
@RegisterForReflection
public class NewVehicleDto {
    public String owner;
    @NotNull
    public String bleHardware;

    public String bleHumanName;
    @NotNull
    public String vehicleName;

    @NotNull
    public Boolean isFavorite;




}
