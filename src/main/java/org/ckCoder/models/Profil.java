package org.ckCoder.models;

public class Profil extends AbstractEntity {

    private String label;
    private String description;

    public Profil() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Profil{" +
                "id='" + this.getId()  +'\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
