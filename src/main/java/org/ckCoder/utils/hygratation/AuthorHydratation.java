package org.ckCoder.utils.hygratation;

import org.ckCoder.models.Author;
import org.ckCoder.models.Book;
import org.ckCoder.models.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class AuthorHydratation {
    public static Set<Author> getAuthors(ResultSet res) throws SQLException {
        Set<Author> authors = new HashSet<>();
        while (res.next()) {
            Author author = new Author();
            Person person = new Person();
            author.setId(res.getLong(1));
            author.setBibliography(res.getString("bibliography"));
            author.setCreatedAt(res.getTimestamp(4).toLocalDateTime());
            author.setUpdatedAt(res.getTimestamp(5).toLocalDateTime());
            person.setId(6);
            person.setName(res.getString("name"));
            person.setSurname(res.getString("surname"));
            person.setCreatedAt(res.getTimestamp(9).toLocalDateTime());
            person.setUpdatedAt(res.getTimestamp(10).toLocalDateTime());

            author.setPerson(person);

            authors.add(author);
        }

        return authors;
    }

}
