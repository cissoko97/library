package org.ckCoder.utils;

import org.ckCoder.models.User;

public class SessionManager {

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User user = null;

    private static SessionManager INSTANCE = null;

    private SessionManager() {

    }

    public static synchronized SessionManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SessionManager();
        }
        return INSTANCE;
    }
}
