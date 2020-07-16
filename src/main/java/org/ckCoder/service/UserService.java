package org.ckCoder.service;

import org.ckCoder.models.Person;
import org.ckCoder.models.Profil;
import org.ckCoder.models.User;
import org.ckCoder.service.contract.IService;
import org.ckCoder.service.contract.Service;
import org.ckCoder.utils.HashWordUtils;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class UserService extends Service implements IService<User, Long> {

    public User create(User user, Profil profil) {
        User r_user = null;
        try {
            // HashPasword
            String password = HashWordUtils.hashWord(user.getPassword());
            CallableStatement statement = this.connection.prepareCall("CALL save_user(?,?,?,?,?)");
            statement.setString(1, user.getPerson().getName());
            statement.setString(2, user.getPerson().getSurname());
            statement.setString(3, user.getEmail());
            statement.setString(4, password);
            statement.setInt(4, (int) profil.getId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return r_user;
    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        User r_user = null;
        try {
            // HashPasword
            String password = HashWordUtils.hashWord(user.getPassword());
            CallableStatement statement = this.connection.prepareCall("CALL update_profil_user(?,?,?)");
            statement.setInt(1, (int) user.getId());
            statement.setString(2, user.getEmail());
            statement.setString(3, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return r_user;
    }

    @Override
    public boolean delete(Long aLong) {
        return false;
    }

    @Override
    public Set<User> findAll(User user) {

        Set<User> users = null;
        try {
            CallableStatement statement = this.connection.prepareCall("CALL get_all_user()");
            boolean status = statement.execute();

            if (status) {
                ResultSet set = statement.getResultSet();
                users = new HashSet<>();
                while (set.next()) {
                    users.add(this.getUserFromResultset(set));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }

    @Override
    public User findById(Long aLong) {
        return null;
    }

    public User findByEmailAndPassword(String email, String password) {

        User r_user = null;
        try {
            // Hash password
            String hashPasssword = HashWordUtils.hashWord(password);
            CallableStatement statement = this.connection.prepareCall("CALL get_user_credential(?,?)");
            statement.setString(1, email);
            statement.setString(2, hashPasssword);

            boolean status = statement.execute();
            if (status) {
                ResultSet set = statement.getResultSet();
                while (set.next()) {
                    if (r_user == null) {
                        r_user = getUserFromResultset(set);
                    }

                    Profil profil = new Profil();
                    profil.setId(set.getInt("p2.id"));
                    profil.setLabel(set.getString("label"));
                    profil.setDescription(set.getString("description"));
                    System.out.println(profil);
                    r_user.getProfils().add(profil);
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return r_user;
    }

    public User findByUsername(String name) {
        return null;
    }

    public boolean addProfilToUser(Long userId, Long profilId) {
        return false;
    }

    public boolean removeProfilToUser(Long userId, Long profilId) {
        return false;
    }

    public boolean changePassword(Long userId, String password) {
        return false;
    }

    public boolean setStatus(User user) {
        boolean result = false;
        try {
            CallableStatement statement = connection.prepareCall("CALL lock_or_unlock_user(?,?)");
            statement.setInt(1, (int) user.getId());
            statement.setBoolean(2, !user.getLocked());

            result = statement.execute();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    private User getUserFromResultset(ResultSet set) throws SQLException {
        User user = new User();
        Person person = new Person();
        person.setId(set.getInt("id"));
        person.setName(set.getString("name"));
        person.setSurname(set.getString("surname"));

        user.setId(set.getInt("U_id"));
        user.setEmail(set.getString("U_email"));
        user.setPerson(person);

        return user;
    }
}
