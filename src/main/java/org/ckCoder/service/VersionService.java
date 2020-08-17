package org.ckCoder.service;

import org.ckCoder.database.Connexion;
import org.ckCoder.models.VersionApp;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VersionService {
    public VersionApp checkVersion() throws SQLException {
        CallableStatement stm = Connexion.getConnection().prepareCall("call check_version()");
        VersionApp versionApp = new VersionApp();
       /* double verion =0.0;
        if (stm.execute()) {
            ResultSet res = stm.getResultSet();
            if (res.next()) {
                verion = res.getDouble(1);
            }
        }*/
        return versionApp;
    }
}
