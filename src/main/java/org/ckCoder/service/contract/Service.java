package org.ckCoder.service.contract;

import org.ckCoder.database.Connexion;

import java.sql.Connection;

public abstract class Service {

    public Connection connection = Connexion.getConnection();

}
