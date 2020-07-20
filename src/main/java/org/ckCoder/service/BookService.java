package org.ckCoder.service;

import org.apache.commons.io.IOUtils;
import org.ckCoder.utils.hygratation.BookHydratation;
import org.ckCoder.database.Connexion;
import org.ckCoder.models.Book;
import org.ckCoder.service.contract.IService;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookService implements IService<Book, Long> {

    @Override
    public Book create(Book book) throws SQLException, IOException {
        CallableStatement stm = Connexion.getConnection().
                prepareCall("call save_book(?,?,?,?,?,?,?,?,?,?,?,?)");

        System.out.println(book.getId());
        FileInputStream stream = new FileInputStream(book.getBookfile());
        FileInputStream streamImg = new FileInputStream(book.getImgfile());
        stm.setString(1, book.getTitle());
        stm.setString(2, book.getDescription());
        stm.setString(3, book.getBookfile().getName());
        stm.setInt(4, (int) book.getCategory().getId());
        stm.setInt(5, book.getEditionYear());
        stm.setInt(6, book.getValeurNominal());
        if(book.getPrice() == null)
            book.setPrice(0.0);
        stm.setDouble(7, book.getPrice());
        stm.setString(8, book.getType());
        stm.setString(9, book.getImgfile().getName());
        stm.setLong(10, book.getId());
        stm.setBytes(11, IOUtils.toByteArray(stream));
        stm.setBytes(12, IOUtils.toByteArray(streamImg));


        if (stm.execute()) {
            ResultSet res = stm.getResultSet();
            book = BookHydratation.getBook(res);
        }
        // stm.setString(3, );
        return book;
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
    public Set<Book> findAll(Book book) throws SQLException {
        CallableStatement stm = Connexion.getConnection().prepareCall("call find_all_book()");
        Set<Book> books = new HashSet<>();
        if (stm.execute()) {
            ResultSet res = stm.getResultSet();
            while (res.next()) {
                books.add(BookHydratation.hydrateBookHelper(res));
            }
        }
        return books;
    }

    @Override
    public Book findById(Long aLong) {
        return null;
    }

    public void createAndAffectAuthor_Categorie_AtBook(long idBook, Set<Long> id_author) throws SQLException {
        CallableStatement stm;

        for (Long id : id_author) {
            stm = Connexion.getConnection().prepareCall("call add_authorAtBook(?, ?)");
            stm.setLong(1, idBook);
            stm.setLong(2, id);
            stm.execute();
            stm.getResultSet();
        }
    }


}
