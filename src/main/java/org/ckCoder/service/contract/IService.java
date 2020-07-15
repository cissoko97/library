package org.ckCoder.service.contract;

import org.ckCoder.database.Connexion;

import java.sql.Connection;
import java.util.Set;

public interface IService<T, I> {

    T create(T t);

    T update(T t);

    boolean delete(I i);

    Set<T> findAll(T t);

    T findById(I i);
}
