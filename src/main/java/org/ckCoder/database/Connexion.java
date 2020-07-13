package org.ckCoder.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {
    private static Connection con = null;

    static {
        String url = "jdbc:mysql://localhost:3306/librery?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC";
        String username = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url,username,password);
            System.out.println("la connexion est établit");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return con;
    }
}
