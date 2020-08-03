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
import org.ckCoder.models.Langage;
import org.ckCoder.models.LanguageEmun;
import org.ckCoder.models.Profil;
import org.ckCoder.models.User;
import org.ckCoder.utils.SelectedLanguage;
import org.ckCoder.utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class IndexController implements Initializable {
    @FXML
    public HBox hbooxMenu;
    @FXML
    public Button logoutBTN;
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
    public ComboBox<Langage> langue_combobox;
    @FXML
    public ComboBox profil_combobox;

    private PrimaryScene primaryScene = new PrimaryScene();

    Properties properties = SelectedLanguage.getInstace(LanguageEmun.AUCUN);
    String flag = "BOOK";

    public IndexController() throws IOException {
    }


    public void initialize(URL location, ResourceBundle resources) {
        initialiseMenu();
        manageMenu();
        initComboxSelect();

       // langue_combobox.setVisible(false);
        profil_combobox.setVisible(false);

        logoutBTN.setOnAction(event -> {
            manager.setUser(null);
            manager.getBookSet().clear();
            Stage stage = (Stage) ((Control) event.getSource()).getScene().getWindow();
            primaryScene.constructPrimaryStage(stage);
            stage.setHeight(500);
            stage.setWidth(500);
        });
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
        langue_combobox.getItems().addAll(new Langage(properties.getProperty("EN_ITEMS_LANGUAGE_INDEX"), LanguageEmun.en),
                new Langage(properties.getProperty("FR_ITEMS_LANGUAGE_INDEX"), LanguageEmun.fr));
        langue_combobox.setPromptText(properties.getProperty("LANGUAGE_MENU_COMBOX_INDEX"));
        prefenre_combobox.getItems().addAll("Caddy","favoriy");
        book_btn.setOnAction(event -> {
            try {
                loadFxmlFile("BOOK", "/view/book/book_principal_stage");
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        langue_combobox.valueProperty().addListener((observableValue, langage, t1) -> {
            try {
                SelectedLanguage.writeToFile(t1.getValue());
                SelectedLanguage.getInstace(t1.getValue());
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
