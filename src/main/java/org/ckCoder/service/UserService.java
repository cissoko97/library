package org.ckCoder.service;

import org.ckCoder.models.User;
import org.ckCoder.service.contract.Service;

import java.util.Set;

public class UserService implements Service<User, Long> {
    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public boolean delete(Long integer) {
        return false;
    }

    @Override
    public Set<User> findAll(User user) {
        return null;
    }

    @Override
    public User findById(Long integer) {
        return null;
    }

    public User findByEmailAndPassword(String email, String password) {
        return null;
    }

    public User findByUsername(String name) {
        return null;
    }

    public boolean addProfilToUser(Long userId, Long profilId) {
        return false;
    }

    public boolean removeProfilToUser(Long userId, Long profilId) {
        return false;
    }

    public boolean changePassword(Long userId, String password) {
        return false;
    }

    public boolean setStatus(User user) {
        return false;
    }
}
