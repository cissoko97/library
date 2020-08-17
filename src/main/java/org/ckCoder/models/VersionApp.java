package org.ckCoder.models;

public class VersionApp extends AbstractEntity {
    private Double numVersion;
    private String description;
    private String url;

    public VersionApp() {

    }

    public Double getNumVerson() {
        return numVersion;
    }

    public void setNumVerson(Double numVerson) {
        this.numVersion = numVerson;
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
