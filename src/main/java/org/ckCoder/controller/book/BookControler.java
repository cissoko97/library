package org.ckCoder.controller.book;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
