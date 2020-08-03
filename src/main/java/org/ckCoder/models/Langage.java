package org.ckCoder.models;

public class Langage {
    private String label;
    private LanguageEmun value;

    public Langage(String label, LanguageEmun value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public LanguageEmun getValue() {
        return value;
    }

    @Override
    public String toString() {
        return label;
    }
}

