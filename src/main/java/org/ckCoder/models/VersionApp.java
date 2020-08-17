package org.ckCoder.models;

import java.time.LocalDateTime;

public class VersionApp extends AbstractEntity {
    private Double numVersion;
    private String description;
    private String url;
    private LocalDateTime deployAt;

    public VersionApp() {

    }

    public LocalDateTime getDeployAt() {
        return deployAt;
    }

    public void setDeployAt(LocalDateTime deployAt) {
        this.deployAt = deployAt;
    }

    public Double getNumVersion() {
        return numVersion;
    }

    public void setNumVersion(Double numVersion) {
        this.numVersion = numVersion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
