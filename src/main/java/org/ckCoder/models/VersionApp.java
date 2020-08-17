package org.ckCoder.models;

public class VersionApp extends AbstractEntity {
    private Double numVerson;
    private String description;
    private String url;

    public VersionApp() {

    }

    public Double getNumVerson() {
        return numVerson;
    }

    public void setNumVerson(Double numVerson) {
        this.numVerson = numVerson;
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
