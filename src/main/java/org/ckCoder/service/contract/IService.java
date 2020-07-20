package org.ckCoder.service.contract;

import org.ckCoder.database.Connexion;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

public interface IService<T, I> {

    T create(T t) throws SQLException, IOException;

    T update(T t);

    boolean delete(I i);

    Set<T> findAll(T t) throws SQLException;

    T findById(I i);
}
