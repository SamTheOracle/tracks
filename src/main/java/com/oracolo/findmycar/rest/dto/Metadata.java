package com.oracolo.findmycar.rest.dto;

public class Metadata {
  private String versionId, lastModified;
  private boolean deleted = false;


  public String getVersionId() {
    return versionId;
  }

  public Metadata setVersionId(String versionId) {
    this.versionId = versionId;
    return this;
  }

  public String getLastModified() {
    return lastModified;
  }

  public Metadata setLastModified(String lastModified) {
    this.lastModified = lastModified;
    return this;
  }

  public boolean getDeleted() {
    return deleted;
  }

  public Metadata setDeleted(boolean deleted) {
    this.deleted = deleted;
    return this;
  }
}
