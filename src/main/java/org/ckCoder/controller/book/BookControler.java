package org.ckCoder.controller.book;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.ckCoder.controller.IndexController;
import org.ckCoder.controller.utils.ControlBtn;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BookControler implements Initializable {
    @FXML
    public Text idBook;
    @FXML
    public Text updateDate;
    @FXML
    public Text creatrionDate;
    @FXML
    public Text typeBook;
    @FXML
    public Text pricebook;
    @FXML
    public Text critiqueValueBook;
    @FXML
    public Text nominalValueBook;
    @FXML
    public Text editionYearBook;
    @FXML
    public Text availabilityBook;
    @FXML
    public Text nbre_vue;
    @FXML
    public Text titleBook;
    @FXML
    public TextFlow descriptionField;
    @FXML
    public VBox imageBox;
    @FXML
    public Text originalNameText;


    @FXML
    public ControlBtn btn_controlController;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_controlController.getAdd_btn().setOnAction(event -> {
            Stage stage = new Stage();
            Scene scene = null;
            try {
                scene = new Scene(IndexController.loadFXML("/view/partial/form_save_book"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setScene(scene);
            stage.setTitle("ADD BOOK FORM");
            //do this for get current stage
            stage.initOwner(((Control) event.getSource()).getScene().getWindow());
            // or do this
            //stage.initOwner(this.btn.getScene().getWindows());
            stage.show();
        });
    }
}
