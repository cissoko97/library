package org.ckCoder.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connexion {
    private static Connection connection = null;

    private Connexion() {

    }

    public static Connection getConnection() {
        if (connection == null) {
            makeConnection();
        }
        return connection;
    }

    private static void makeConnection() {

        InputStream inputStream;
        inputStream = Connexion.class.getResourceAsStream("/properties/database.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            String username = properties.getProperty("user");
            String password = properties.getProperty("password");
            String port = properties.getProperty("port");
            String host = properties.getProperty("host");
            String database_name = properties.getProperty("database_name");
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database_name + "?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC";

            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, username, password);

            System.out.println("Bonjour connecion avec la base de donnée avec succes!!");

        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }
}
