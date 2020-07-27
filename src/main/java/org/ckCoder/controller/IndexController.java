package org.ckCoder.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.ckCoder.MainApp;
import org.ckCoder.controller.book.CategoryAndBookController;
import org.ckCoder.models.Profil;
import org.ckCoder.models.User;
import org.ckCoder.utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class IndexController implements Initializable {
    @FXML
    public HBox hbooxMenu;
    SessionManager manager = SessionManager.getInstance();
    Logger logger = Logger.getLogger(this.getClass());
    @FXML
    public BorderPane principal_pane;

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

    String flag = "BOOK";

    public void initialize(URL location, ResourceBundle resources) {
        initialiseMenu();
        manageMenu();
        initComboxSelect();
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(IndexController.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }


    private void manageMenu() {
        User user = manager.getUser();
        Profil profilAdmin =
                user
                        .getProfils()
                        .stream()
                        .filter(profil -> profil.getLabel().equalsIgnoreCase("admin")).findFirst().orElse(null);
        if (profilAdmin == null) {
            hbooxMenu.getChildren().removeAll(order_btn, user_btn);
        }
    }

    private void initialiseMenu() {
        prefenre_combobox.getItems().addAll("Caddy", "fa");
        langue_combobox.getItems().addAll("Français", "Anglais");
        book_btn.setOnAction(event -> {

            try {
                loadFxmlFile("BOOK", "/view/book/book_principal_stage");
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        order_btn.setOnAction(event -> {
            try {
                loadFxmlFile("COMMAND", "/view/order/oder_view");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        user_btn.setOnAction(event -> {
            try {
                loadFxmlFile("USER", "/view/user/user_view");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadFxmlFile(String flag, String fileName) throws IOException {
        if (!this.flag.equalsIgnoreCase(flag)) {
            this.flag = flag;
            principal_pane.setCenter(loadFXML(fileName));
        }
    }

    private void initComboxSelect() {
        prefenre_combobox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equalsIgnoreCase("caddy")) {

                try {
                    Stage stage = new Stage();
                    Scene scene;
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(MainApp.class.getResource("/view/panier/caddy_view.fxml"));
                    CategoryAndBookController panierController = new CategoryAndBookController();
//                    panierController.
                    BorderPane page = loader.load();
                    stage.setResizable(false);
                    stage.setMinWidth(600);
                    stage.setMinHeight(400);
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initOwner(prefenre_combobox.getScene().getWindow());

                    scene = new Scene(page);
                    stage.setScene(scene);
                    stage.setTitle("Command View");
                    stage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //do this for get current stage

            }
        });
    }
}
