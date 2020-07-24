package org.ckCoder.utils.hygratation;

import org.ckCoder.models.*;

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
            critique = critiqueHydrateHelper(res);
            category = getCategoryHelper(res);
            author = getAuthorHelper(res);
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

            critique.setBook(book);
            book.setCategory(category);
            book.getCritiques().add(critique);
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
        book.setId(res.getLong("book_id"));
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
        book.setCreatedAt(res.getTimestamp("book_created_at").toLocalDateTime());
        book.setUpdatedAt(res.getTimestamp("book_updated_at").toLocalDateTime());
        book.setBookBinary(res.getBytes("fileByte"));
        book.setImgBinary(res.getBytes("imgByte"));
        return book;
    }

    public static Book hydrateBookHelperNoFileBite(ResultSet res) throws SQLException {
        Book book = new Book();
        book.setId(res.getLong("book_id"));
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
        book.setCreatedAt(res.getTimestamp("book_created_at").toLocalDateTime());
        book.setUpdatedAt(res.getTimestamp("book_updated_at").toLocalDateTime());
        book.setImgBinary(res.getBytes("imgByte"));
        return book;
    }

    public static Critique critiqueHydrateHelper(ResultSet res) throws SQLException {
        Critique critique = new Critique();
        if(res.getTimestamp("critique_created_at") != null)
            critique.setCreatedAt(res.getTimestamp("critique_created_at").toLocalDateTime());
        critique.setNote(res.getInt("critique_note"));
        critique.setComment(res.getString("critique_comment"));
        critique.setId(res.getLong("critique_id"));
        return critique;
    }

    public static Critique critique_userHydrateHelper(ResultSet resultSet) throws SQLException {
        Critique critique = critiqueHydrateHelper(resultSet);
        User user = new User();
        user.setEmail(resultSet.getString("user_email"));
        critique.setUser(user);
        return critique;
    }

    public static Author getAuthorHelper(ResultSet resultSet) throws SQLException {
        Author author = new Author();
        Person person =new Person();
        author.setId(resultSet.getLong("author_id"));
        author.setBibliography(resultSet.getString("authoer_bibliography"));
        /*if(resultSet.getTimestamp("author_update_at") != null)
            author.setUpdatedAt(resultSet.getTimestamp("author_update_at").toLocalDateTime());*/
        /*if(resultSet.getTimestamp("author_created_at") != null)
            author.setCreatedAt(resultSet.getTimestamp("author_created_at").toLocalDateTime());*/
        person.setId(resultSet.getLong("person_id"));
        person.setSurname(resultSet.getString("person_surname"));
        person.setName(resultSet.getString("person_name"));

        /*if(resultSet.getTimestamp("person_update_at") != null)
            person.setUpdatedAt(resultSet.getTimestamp("person_update_at").toLocalDateTime());
        if(resultSet.getTimestamp("person_created_at") != null)
            person.setCreatedAt(resultSet.getTimestamp("person_created_at").toLocalDateTime());*/

        author.setPerson(person);
        return author;
    }

    public static Category getCategoryHelper(ResultSet res) throws SQLException {
        Category category = new Category();
        category.setId(res.getLong("category_id"));
        category.setDescription(res.getString("category_description"));
        category.setFlag(res.getString("category_flag"));
        return category;
    }


}
