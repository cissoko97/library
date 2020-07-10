package org.ckCoder.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connexion {

    private static Connection connection = null;
    private static Properties properties;

    private Connexion() {

    }

    public static Connection getInstance() throws IOException, SQLException {
        if (connection == null) {
            getDataBaseConfig();
        }
        return connection;
    }

    private static void getDataBaseConfig() throws IOException, SQLException {
        properties = new Properties();
        properties.load(Connexion.class.getResourceAsStream("/properties/database.properties"));
        String host = properties.getProperty("host");
        String password = properties.getProperty("password");
        String user = properties.getProperty("user");
        String port = properties.getProperty("port");
        Connection connection = DriverManager.getConnection("");
    }
}
