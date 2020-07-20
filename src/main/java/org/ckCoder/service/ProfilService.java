package org.ckCoder.service;

import org.ckCoder.models.Profil;
import org.ckCoder.service.contract.IService;
import org.ckCoder.service.contract.Service;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ProfilService extends Service implements IService<Profil, Long> {
    @Override
    public Profil create(Profil profil) throws SQLException {
        try {
            CallableStatement statement = connection.prepareCall("CALL  save_or_update_profils(?,?,?,?)");
            statement.setInt(1, (int) profil.getId());
            statement.setString(2, profil.getLabel());
            statement.setString(3, profil.getDescription());
            statement.setInt(4, 1);

            boolean status = statement.execute();

            if (status) {
                ResultSet set = statement.getResultSet();
                while (set.next()) {
                    profil = getProfilFromResulSet(set);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return profil;
    }

    @Override
    public Profil update(Profil profil) {

        try {
            CallableStatement statement = connection.prepareCall("CALL  save_or_update_profils(?,?,?,?)");
            statement.setInt(1, (int) profil.getId());
            statement.setString(2, profil.getLabel());
            statement.setString(3, profil.getDescription());
            statement.setInt(4, 0);

            boolean status = statement.execute();

            if (status) {
                ResultSet set = statement.getResultSet();
                while (set.next()) {
                    profil = getProfilFromResulSet(set);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return profil;
    }

    @Override
    public boolean delete(Long aLong) {
        return false;
    }

    @Override
    public Set<Profil> findAll(Profil profil) {

        Set<Profil> profils = null;
        try {
            CallableStatement statement = connection.prepareCall("call fetch_profils(?)");
            statement.setInt(1, (int) profil.getId());
            boolean status = statement.execute();

            if (status) {
                profils = new HashSet<>();
                ResultSet set = statement.getResultSet();
                while (set.next()) {
                    profils.add(getProfilFromResulSet(set));
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return profils;
    }

    @Override
    public Profil findById(Long aLong) {
        return null;
    }

    private Profil getProfilFromResulSet(ResultSet set) throws SQLException {
        Profil profil = new Profil();

        profil.setId(set.getInt("id"));
        profil.setLabel(set.getString("label"));
        profil.setDescription(set.getString("description"));

        return profil;
    }
}
