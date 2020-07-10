package org.ckCoder.models;

public class Critique extends AbstractEntity {

    private int note;
    private String comment;
    private User user;
    private Book book;

    public Critique() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
