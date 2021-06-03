package com.oracolo.findmycar.rest.dto;

public class MetadataDto {
  private String versionId, lastModified;
  private boolean deleted = false;


  public String getVersionId() {
    return versionId;
  }

  public MetadataDto setVersionId(String versionId) {
    this.versionId = versionId;
    return this;
  }

  public String getLastModified() {
    return lastModified;
  }

  public MetadataDto setLastModified(String lastModified) {
    this.lastModified = lastModified;
    return this;
  }

  public boolean getDeleted() {
    return deleted;
  }

  public MetadataDto setDeleted(boolean deleted) {
    this.deleted = deleted;
    return this;
  }
}
