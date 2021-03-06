package org.ckCoder.utils;

import org.ckCoder.models.Book;
import org.ckCoder.models.User;

import java.util.HashSet;
import java.util.Set;

public class SessionManager {

    private Set<Book> bookSet = new HashSet<>();
    private static SessionManager INSTANCE = null;
    private User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Set<Book> getBookSet() {
        return bookSet;
    }

    public void setBookSet(Set<Book> bookSet) {
        this.bookSet = bookSet;
    }


    private SessionManager() {

    }

    public static synchronized SessionManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SessionManager();
        }
        return INSTANCE;
    }
}
