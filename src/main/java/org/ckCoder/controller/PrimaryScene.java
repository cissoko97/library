package org.ckCoder.controller;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.ckCoder.MainApp;
import org.ckCoder.models.User;
import org.ckCoder.service.UserService;
/*import org.ckCoder.utils.NotificationType;
import org.ckCoder.utils.NotificationUtil;*/
import org.ckCoder.utils.SelectedLanguage;
import org.ckCoder.utils.SessionManager;
import org.ckCoder.utils.Verification;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PrimaryScene {
    private final UserService userService = new UserService();
    private SessionManager manager = SessionManager.getInstance();
    private Properties properties = SelectedLanguage.getInstace();

    public PrimaryScene() throws IOException {
    }

    public void constructPrimaryStage(Stage stage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPrefWidth(500);
        grid.setPrefHeight(500);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text(properties.getProperty("AUTHENTICATION_PAGE_WELCOME"));
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label(properties.getProperty("AUTHENTICATION_PAGE_USERNAME"));
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label(properties.getProperty("AUTHENTICATITION_PAGE_PASSWORD"));
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button btn = new Button(properties.getProperty("AUTHENTICATION_PAGE_BTN"));
        HBox hbBtn = new HBox(10);

        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        Scene scene = new Scene(grid, 500, 500);

        pwBox.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                if (validationForm(userTextField, pwBox, grid)) {
                    valideCredentiel(userTextField, pwBox, stage);
                }
            }
        });

        btn.setOnAction(event -> {
            //primaryStage.setScene(scene1);
            //NotificationUtil.showNotiication(String.valueOf(NotificationType.ERROR), "bonjour", "vide");
            if (validationForm(userTextField, pwBox, grid)) {
                //loadUser(userTextField,pwBox,userService,primaryStage,manager);

                valideCredentiel(userTextField, pwBox, stage);
            }
        });
        stage.setTitle(properties.getProperty("INDEX_TITLE_DIALOG"));
        stage.setWidth(500);
        stage.setHeight(500);
        stage.setScene(scene);
        stage.show();
    }

    private static boolean validationForm(TextField userTextField, PasswordField pwField, GridPane pane) {
        try {
            Properties properties = SelectedLanguage.getInstace();
            pane.getChildren().forEach(Verification::remouveDangerClass);
            if (userTextField.getText().equals("") || pwField.getText().equals("")) {
                Verification.dangerField(userTextField);
                Verification.dangerField(pwField);
                Verification.alertMessage(properties.getProperty("AUTHENTICATION_FIELD_EMPTY"), Alert.AlertType.ERROR);
                return false;
            } else {
                if (!Verification.emailVerificatio(userTextField.getText())) {
                    Verification.dangerField(pwField);
                    Verification.alertMessage(properties.getProperty("AUTHENTICATION_VALID_ADRESS"), Alert.AlertType.ERROR);
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
                primaryStage.setHeight(height);
                primaryStage.setWidth(width);
                primaryStage.setScene(scene1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            List<String> message = new ArrayList<>();
            message.add(properties.getProperty("AUTHENTICATION_BAD_CREDENTICIAL"));
            Verification.alertMessage(message, Alert.AlertType.ERROR);
        }
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
