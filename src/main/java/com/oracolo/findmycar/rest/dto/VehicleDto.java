package com.oracolo.findmycar.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;



@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleDto {
    private String owner, name;
    private Integer id;
    private MetadataDto metadata;
    @JsonProperty(value = "isFavorite")
    private Boolean isFavorite;
    private String bleHardware;
    private String bleHumanName;

    public MetadataDto getMetadata() {
        return metadata;
    }

    public VehicleDto setMetadata(MetadataDto metadata) {
        this.metadata = metadata;
        return this;
    }


    public String getBleHardware() {
        return bleHardware;
    }

    public VehicleDto setBleHardware(String bleHardware) {
        this.bleHardware = bleHardware;
        return this;
    }

    public String getBleHumanName() {
        return bleHumanName;
    }

    public VehicleDto setBleHumanName(String bleHumanName) {
        this.bleHumanName = bleHumanName;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public VehicleDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public VehicleDto setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public String getName() {
        return name;
    }

    public VehicleDto setName(String name) {
        this.name = name;
        return this;
    }

    @JsonProperty(value = "isFavorite")
    public Boolean isFavorite() {
        return isFavorite == null ? Boolean.FALSE : isFavorite;
    }

    @JsonProperty(value = "isFavorite")
    public VehicleDto setIsFavorite(Boolean isFavorite) {
        if (isFavorite == null) {
            this.isFavorite = Boolean.FALSE;
            return this;
        }
        this.isFavorite = isFavorite;
        return this;
    }


    @Override
    public String toString() {
        return "VehicleDto{" +
                "owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", metadata=" + metadata +
                ", isFavorite=" + isFavorite +
                ", bleHardware='" + bleHardware + '\'' +
                ", bleHumanName='" + bleHumanName + '\'' +
                '}';
    }


}
