package org.ckCoder.service;

import org.ckCoder.models.Book;
import org.ckCoder.service.contract.Service;

import java.util.Set;

public class BookService implements Service<Book, Long> {

    @Override
    public Book create(Book book) {
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
