package org.ckCoder.service;

import org.ckCoder.database.Connexion;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VersionService {
    public double checkVersion() throws SQLException {
        CallableStatement stm = Connexion.getConnection().prepareCall("call check_version()");
        double verion =0.0;
        if (stm.execute()) {
            ResultSet res = stm.getResultSet();
            if(res.next())
                verion = res.getDouble(1);
        }
        return verion;
    }
}
