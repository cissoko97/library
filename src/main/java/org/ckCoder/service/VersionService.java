package org.ckCoder.service;

import org.ckCoder.database.Connexion;
import org.ckCoder.models.VersionApp;
import org.ckCoder.utils.hygratation.VersionHydratation;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VersionService {
    public VersionApp checkVersion() throws SQLException {
        CallableStatement stm = Connexion.getConnection().prepareCall("call check_version()");
        VersionApp version = null;

        if (stm.execute()) {
            ResultSet res = stm.getResultSet();
            if (res.next()) {
                version = VersionHydratation.versionHelperHydratation(res);
            }
        }
        return version;
    }
}
