package org.ckCoder.utils.hygratation;

import org.ckCoder.models.VersionApp;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VersionHydratation {

    public static VersionApp versionHelperHydratation(ResultSet res) throws SQLException {

        VersionApp version = new VersionApp();

        version.setId(res.getLong("id"));
        version.setNumVersion(res.getDouble("version"));
        version.setUrl(res.getString("url"));
        version.setDescription(res.getString("description"));

        version.setDeployAt(res.getTimestamp("deploy_at").toLocalDateTime());
        version.setCreatedAt(res.getTimestamp("created_at").toLocalDateTime());
        version.setUpdatedAt(res.getTimestamp("updated_at").toLocalDateTime());

        return version;
    }

}
