package org.ckCoder.service;

import org.apache.commons.io.FileUtils;
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
        CallableStatement stm;
        if (book.getId() == 0) {
            stm = Connexion.getConnection().
                    prepareCall("call save_book(?,?,?,?,?,?,?,?,?,?,?,?)");
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
          //  stm.setBytes(11, IOUtils.toByteArray(stream));
            stm.setBytes(11, FileUtils.readFileToByteArray(book.getBookfile()));
            stm.setBytes(12, IOUtils.toByteArray(streamImg));
        } else {
            stm = Connexion.getConnection().
                    prepareCall("call update_book(?,?,?,?,?,?,?,?)");
            stm.setString(1, book.getTitle());
            stm.setString(2, book.getDescription());
            stm.setInt(3, (int) book.getCategory().getId());
            stm.setInt(4, book.getEditionYear());
            stm.setInt(5, book.getValeurNominal());
            if(book.getPrice() == null)
                book.setPrice(0.0);
            stm.setDouble(6, book.getPrice());
            stm.setString(7, book.getType());
            stm.setLong(8, book.getId());

        }


        if (stm.execute()) {
            ResultSet res = stm.getResultSet();
            book = BookHydratation.getBook(res);
        }
        // stm.setString(3, );
        return book;
    }



    @Override
    public Book update(Book book) throws SQLException {

        // stm.setString(3, );
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
                books.add(BookHydratation.hydrateBookHelperNoFileBite(res));
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

    public Book findAllBookAndtherElement(Long id) throws SQLException, IOException {
        CallableStatement stm = Connexion.getConnection().prepareCall("call select_book_author_categorie_critique(?)");
        stm.setLong(1,id);
        Book book;
        if (stm.execute()) {
            book = BookHydratation.book_critique_category_favory_authorHydrate(stm.getResultSet());
        } else
            book = new Book();
        return book;
    }


    public Set<Book> findBookByCategory(Long idCategory) throws SQLException {
        CallableStatement stm = Connexion.getConnection().prepareCall("call find_product_by_category(?)");
        stm.setLong(1, idCategory);

        Set<Book> books = new HashSet<>();

        if (stm.execute()) {
            ResultSet res = stm.getResultSet();
            while (res.next()) {
                books.add(BookHydratation.hydrateBookHelperNoFileBite(res));
            }
        }

        return books;
    }

    public boolean incrementNumberOfView(Long bookId, Long idUser) throws SQLException {
        CallableStatement stm = Connexion.getConnection().prepareCall("call increment_number_of_views(?,?)");
        stm.setLong(1, bookId);
        stm.setLong(2, idUser);
        System.out.println("book id = " + bookId + " user id = " + idUser);
        return stm.execute();
    }




}
