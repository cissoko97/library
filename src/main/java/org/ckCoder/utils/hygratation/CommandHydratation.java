package org.ckCoder.utils.hygratation;

import org.ckCoder.models.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommandHydratation {

    public static Command commandHelperHydratation(ResultSet res) throws SQLException {
        Command command = new Command();
        Person person = new Person();
        User user = new User();
        user.setEmail(res.getString("user_email"));
        user.setId(res.getLong("user_id"));
        person.setName(res.getString("person_name"));
        person.setSurname(res.getString("person_surname"));

        command.setAccepted(res.getBoolean("command_accept"));
        command.setId(res.getInt("command_id"));
        command.setTotalPrice(res.getDouble("command_total_price"));
        command.setCreatedAt(res.getTimestamp("command_created_at").toLocalDateTime());
        command.setUpdatedAt(res.getTimestamp("command_update_at").toLocalDateTime());

        user.setPerson(person);
        command.setUser(user);
        return command;
    }

    public static Line lineHelperHydratation(ResultSet res) throws SQLException {
        Line line = new Line();
        Book book = new Book();

        book.setPrice(res.getDouble("book_price"));
        book.setCreatedAt(res.getTimestamp("book_created_at").toLocalDateTime());
        book.setUpdatedAt(res.getTimestamp("book_update_at").toLocalDateTime());
        book.setTitle(res.getString("book_title"));
        book.setBookBinary(res.getBytes("book_file_bytes"));

        line.setBook(book);
        return line;
    }
}
