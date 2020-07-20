package org.ckCoder.utils.hygratation;

import org.ckCoder.models.Author;
import org.ckCoder.models.Book;
import org.ckCoder.models.Category;
import org.ckCoder.models.Critique;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookHydratation {

    public static Book book_critique_category_favory_authorHydrate(ResultSet res) throws SQLException, IOException {
        Book book = new Book();
        Critique critique;
        Category category;
        Author author;
        while (res.next()) {
            critique = new Critique();
            category = new Category();
            author = new Author();
            /*book.setId(res.getLong(1));
            book.setTitle(res.getString(2));
            book.setDescription(res.getString(3));
            book.setFileName(res.getString(4));
            book.setNbVue(res.getInt(6));
            book.setEditionYear(res.getInt(7));
            book.setValeurCritique(res.getInt(8));
            book.setValeurNominal(res.getInt(9));
            book.setPrice(res.getDouble(10));
            book.setType(res.getString(11));
            book.setImgName(res.getString(12));
            book.setCreatedAt(res.getTimestamp(13).toLocalDateTime());
            book.setUpdatedAt(res.getTimestamp(14).toLocalDateTime());
            book.setAvailability(res.getBoolean(15));
            book.setBookBinary(res.getBytes(16));
            book.setImgBinary(res.getBytes(17));*/
            book = hydrateBookHelper(res);

            author.setId(res.getLong(18));
            author.setBibliography(res.getString(19));

            category.setId(res.getLong(20));
            category.setFlag(res.getString(21));
            category.setDescription(res.getString(22));

            critique.setId(res.getLong(23));
            critique.setNote(res.getInt(24));
            critique.setComment(res.getString(25));
            critique.setCreatedAt(res.getTimestamp(26).toLocalDateTime());

            book.setCategory(category);
            book.getAuthors().add(author);
        }

        return book;
    }

    public static Book getBook(ResultSet res) throws SQLException {
        Book book = new Book();
        while (res.next()) {
            book = hydrateBookHelper(res);
        }
        return book;
    }

    public static Book hydrateBookHelper(ResultSet res) throws SQLException {
        Book book = new Book();
        book.setId(res.getLong("id"));
        book.setTitle(res.getString("title"));
        book.setDescription(res.getString("description"));
        book.setFileName(res.getString("file_name"));
        book.setNbVue(res.getInt("nb_vue"));
        book.setAvailability(res.getBoolean("availability"));
        book.setEditionYear(res.getInt("edition_year"));
        book.setValeurNominal(res.getInt("valeur_nominal"));
        book.setValeurCritique(res.getInt("valeur_critique"));
        book.setPrice(res.getDouble("price"));
        book.setType(res.getString("type"));
        book.setImgName(res.getString("img_name"));
        book.setCreatedAt(res.getTimestamp("created_at").toLocalDateTime());
        book.setUpdatedAt(res.getTimestamp("updated_at").toLocalDateTime());
        book.setBookBinary(res.getBytes("fileByte"));
        book.setImgBinary(res.getBytes("imgByte"));
        return book;
    }
}
