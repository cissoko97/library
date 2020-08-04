package org.ckCoder.utils;

public enum NotificationType {
    SUCCES("SUCCES"),
    ERROR("ERROR"),
    INFO("INFO");

    private String name = "";

    //Constructeur
    NotificationType(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
