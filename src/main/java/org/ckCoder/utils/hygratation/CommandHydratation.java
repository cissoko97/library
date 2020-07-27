package org.ckCoder.utils.hygratation;

import org.ckCoder.models.Command;
import org.ckCoder.models.Person;
import org.ckCoder.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommandHydratation {

    public static Command commandHelperHydratation(ResultSet res) throws SQLException {
        Command command = new Command();
        Person person = new Person();
        User user = new User();
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
}
