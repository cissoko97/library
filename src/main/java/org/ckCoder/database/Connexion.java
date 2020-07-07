package org.ckCoder.database;

public class Connexion {

    private Connexion connexion;

    private Connexion() {

    }

    public Connexion getInstance() {
        return this.connexion;
    }
}