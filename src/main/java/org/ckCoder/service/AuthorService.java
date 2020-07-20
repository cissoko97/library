package org.ckCoder.service;

import org.ckCoder.database.Connexion;
import org.ckCoder.models.Author;
import org.ckCoder.service.contract.IService;
import org.ckCoder.utils.hygratation.AuthorHydratation;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class AuthorService implements IService<Author, Long> {
    @Override
    public Author create(Author author) throws SQLException, IOException {
        return null;
    }

    @Override
    public Author update(Author author) {
        return null;
    }

    @Override
    public boolean delete(Long aLong) {
        return false;
    }

    @Override
    public Set<Author> findAll(Author author) throws SQLException {
        CallableStatement stm = Connexion.getConnection().prepareCall("call find_all_author");
        Set<Author> authors = new HashSet<>();
        if (stm.execute()) {
            ResultSet res = stm.getResultSet();
            authors = AuthorHydratation.getAuthors(res);
        }
        return authors;
    }

    @Override
    public Author findById(Long aLong) {
        return null;
    }
}
