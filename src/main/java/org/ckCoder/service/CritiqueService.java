package org.ckCoder.service;

import org.ckCoder.models.Critique;
import org.ckCoder.service.contract.IService;
import org.ckCoder.service.contract.Service;
import org.ckCoder.utils.hygratation.BookHydratation;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class CritiqueService extends Service implements IService<Critique, Long> {
    @Override
    public Critique create(Critique critique) {

        try {
            CallableStatement statement = connection.prepareCall("CALL save_critique(?,?,?,?)");

            statement.setInt(1, critique.getNote());
            statement.setString(2, critique.getComment());
            statement.setInt(3, (int) critique.getBook().getId());
            statement.setInt(4, (int) critique.getUser().getId());

            statement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return critique;
    }

    @Override
    public Critique update(Critique critique) {
        return null;
    }

    @Override
    public boolean delete(Long aLong) {
        return false;
    }

    @Override
    public Set<Critique> findAll(Critique critique) {

        return null;
    }

    public Set<Critique> findAllCritiqueByIdBook(long id_book) {
        Set<Critique> critiques = new HashSet<>();
        try {
            CallableStatement statement = connection.prepareCall("CALL get_critique(?)");
            statement.setInt(1, (int) id_book);
            boolean status = statement.execute();
            if (status) {
                ResultSet resultSet = statement.getResultSet();

                while (resultSet.next()) {
                    critiques.add(BookHydratation.critique_userHydrateHelper(resultSet));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return critiques;
    }

    @Override
    public Critique findById(Long aLong) {
        return null;
    }


    private Critique getCritique(ResultSet set) {
        return null;
    }
}
