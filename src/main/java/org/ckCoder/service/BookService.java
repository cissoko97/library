package org.ckCoder.service;

import org.ckCoder.database.Connexion;
import org.ckCoder.models.Book;
import org.ckCoder.service.contract.IService;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Set;

public class BookService implements IService<Book, Long> {

    @Override
    public Book create(Book book) throws SQLException {
        CallableStatement stm = Connexion.getConnection().
                prepareCall("call save_book(?,?,?,?,?,?,?,?,?,?)");

        stm.setString(1, book.getTitle());
        stm.setString(2, book.getDescription());
        
        return null;
    }

    @Override
    public Book update(Book book) {
        return null;
    }

    @Override
    public boolean delete(Long aLong) {
        return false;
    }

    @Override
    public Set<Book> findAll(Book book) {
        return null;
    }

    @Override
    public Book findById(Long aLong) {
        return null;
    }

}
