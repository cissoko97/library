package org.ckCoder;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.ckCoder.database.Connexion;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class MainApp extends Application {
    private static Scene scene;
    public static void main(String[] args) {
        launch(args);
    }
    private boolean lockConnection = false;
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
        btn.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                try {
                    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                    int width = gd.getDisplayMode().getWidth();
                    int height = gd.getDisplayMode().getHeight();
                    Scene scene = new Scene(loadFXML("/view/index"), width, height);
                    scene.getStylesheets().add(this.getClass().getResource("/css/stylesheet.css").toExternalForm());
                    primaryStage.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        primaryStage.setScene(scene);

        if (!lockConnection)
            primaryStage.show();
//        Connexion.getInstance();
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
    public void init() {
        try {
            if(Connexion.getConnection().isClosed()){
                lockConnection = true;
            }
        } catch (SQLException throwables) {
            System.out.println("la connexion est fermée");
        }
    }
}