package org.ckCoder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.ckCoder.controller.PrimaryScene;
import org.ckCoder.database.Connexion;
import org.ckCoder.models.User;
import org.ckCoder.service.UserService;
import org.ckCoder.service.VersionService;
import org.ckCoder.utils.SessionManager;
import org.ckCoder.utils.UtilForArray;
import org.ckCoder.utils.Verification;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class MainApp extends Application {

   // Logger logger = Logger.getLogger(getClass());

    private PrimaryScene primaryScene = new PrimaryScene();

    private boolean isConnect = false;
    private VersionService versionService = new VersionService();
    private static Stage window;

    public MainApp() throws IOException {
    }

    public static void main(String[] args) {
        PropertyConfigurator.configure(MainApp.class.getResource("/properties/log4j.properties"));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        InputStream inputStream = getClass().getResourceAsStream("/properties/config.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        if (UtilForArray.isIntegr(properties.getProperty("version")) && versionService.checkVersion() > Double.parseDouble(properties.getProperty("version"))) {

            Optional<ButtonType> optional = Verification.alertMessage(properties.getProperty("MESSAGE_DIALOG_UPDATE_APP_TITLE"),
                    properties.getProperty("MESSAGE_DIALOG_UPDATE_APP_CONTENT"), Alert.AlertType.CONFIRMATION).showAndWait();
            if(optional.get() == ButtonType.OK) {
                window = primaryStage;
                window.setResizable(false);
                window.centerOnScreen();

            }


        }
        primaryScene.constructPrimaryStage(primaryStage);
    }

    @Override
    public void init() throws Exception {
        if (!Connexion.getConnection().isClosed())
            this.isConnect = true;
    }

    public static void restartApplication(String xr3PlayerUpdater) {
    }

}
