package org.ckCoder.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class IndexController implements Initializable {

    @FXML
    public Pane principal_pane;
    @FXML
    public Button book_btn;
    @FXML
    public Button order_btn;
    @FXML
    public Button user_btn;
    @FXML
    public ComboBox<String> prefenre_combobox;
    @FXML
    public ComboBox<String> langue_combobox;
    @FXML
    public ComboBox profil_combobox;


    public void initialize(URL location, ResourceBundle resources) {
        prefenre_combobox.getItems().addAll("Caddy", "favorie");
        langue_combobox.getItems().addAll("Français", "Anglais");
        book_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                principal_pane.getChildren().removeAll();
                principal_pane.getChildren().clear();
                try {
                    principal_pane.getChildren().add(loadFXML("/view/book/book_principal_stage"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        order_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                principal_pane.getChildren().removeAll();
                principal_pane.getChildren().clear();
                try {
                    principal_pane.getChildren().add(loadFXML("/view/order/oder_view"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        user_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                principal_pane.getChildren().clear();
                principal_pane.getChildren().clear();
                try {
                    principal_pane.getChildren().add(loadFXML("/view/user/user_view"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(IndexController.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }


}
