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
import org.ckCoder.database.Connexion;
import org.ckCoder.models.User;
import org.ckCoder.service.UserService;
import org.ckCoder.utils.SessionManager;
import org.ckCoder.utils.Verification;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainApp extends Application {

    Logger logger = Logger.getLogger(getClass());

    private static Scene scene;
    private boolean isConnect = false;
    UserService userService = new UserService();
    SessionManager manager = SessionManager.getInstance();

    public static void main(String[] args) {
        PropertyConfigurator.configure(MainApp.class.getResource("/properties/log4j.properties"));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        primaryStage.setTitle("Gestion librerie");
        /*scene = new Scene(loadFXML("/view/index"));
        //FXMLLoader loader = new FXMLLoader(this.getClass().getResource(".fxml"));
        //Parent parent = loader.load();
        primaryStage.setScene(scene);*/
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPrefWidth(500);
        grid.setPrefHeight(500);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);

        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        Scene scene = new Scene(grid, 500, 500);

        pwBox.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                if (validationForm(userTextField, pwBox, grid)) {
                    valideCredentiel(userTextField, pwBox, primaryStage);
                }
            }
        });

        btn.setOnAction(event -> {
            //primaryStage.setScene(scene1);
            if (validationForm(userTextField, pwBox, grid)) {
                //loadUser(userTextField,pwBox,userService,primaryStage,manager);

                valideCredentiel(userTextField, pwBox, primaryStage);
            }
        });

        primaryStage.setScene(scene);
        if (isConnect)
            primaryStage.show();
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public void setScene(String pathUrl, Stage stage) throws IOException {
        Scene scene = new Scene(loadFXML(pathUrl));
        stage.setScene(scene);
    }

    @Override
    public void init() throws Exception {
        if (!Connexion.getConnection().isClosed())
            this.isConnect = true;
    }

    private boolean validationForm(TextField userTextField, PasswordField pwField, GridPane pane) {
        pane.getChildren().forEach(Verification::remouveDangerClass);
        List<String> list = new ArrayList<>();
        if (userTextField.getText().equals("") || pwField.getText().equals("")) {
            list.add("please fill in all field");
            Verification.dangerField(userTextField);
            Verification.dangerField(pwField);
            Verification.alertMessage(list, Alert.AlertType.ERROR);
            return false;
        } else {
            if (!Verification.emailVerificatio(userTextField.getText())) {
                list.add("this adresse is not valid pleace verify");
                Verification.dangerField(pwField);
                Verification.alertMessage(list, Alert.AlertType.ERROR);
                return false;
            }
            return true;
        }
    }

    private void valideCredentiel(TextField userTextField, PasswordField pwBox, Stage primaryStage) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        String userEmail = userTextField.getText();

        String userPassword = pwBox.getText();
        User user = userService.findByEmailAndPassword(userEmail, userPassword);
        if (user != null && user.getEmail() != null) {

            if (user.getLocked()) {
                Verification.alertMessage("Unauthorized", Alert.AlertType.WARNING);
                return;
            }
            manager.setUser(user);
            try {
                Scene scene1 = new Scene(loadFXML("/view/index"), width, height);
                scene1.getStylesheets().addAll("/css/stylesheet.css", "/css/buttonStyle.css");
                primaryStage.setScene(scene1);
                primaryStage.setScene(scene1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            List<String> message = new ArrayList<>();
            message.add("bad credentiel");
            Verification.alertMessage(message, Alert.AlertType.ERROR);
        }
    }

}
