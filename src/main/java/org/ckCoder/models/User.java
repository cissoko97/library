package org.ckCoder.models;

import java.io.Serializable;
import java.util.Set;

public class User extends AbstractEntity implements Serializable {

    private String email;
    private String password;
    private Boolean isLocked;
    private Person person;
    private Set<Book> books;
    private Set<Critique> critiques;
    private Set<Profil> profils;
    private Set<Connexion> connexionSet;

    public User() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getLocked() {
        return isLocked;
    }

    public void setLocked(Boolean locked) {
        isLocked = locked;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public Set<Critique> getCritiques() {
        return critiques;
    }

    public void setCritiques(Set<Critique> critiques) {
        this.critiques = critiques;
    }

    public Set<Profil> getProfils() {
        return profils;
    }

    public void setProfils(Set<Profil> profils) {
        this.profils = profils;
    }

    public Set<Connexion> getConnexionSet() {
        return connexionSet;
    }

    public void setConnexionSet(Set<Connexion> connexionSet) {
        this.connexionSet = connexionSet;
    }
}
