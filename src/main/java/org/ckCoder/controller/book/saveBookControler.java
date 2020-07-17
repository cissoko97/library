package org.ckCoder.controller.book;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import org.ckCoder.utils.Verification;
import sun.font.TextLabel;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class saveBookControler implements Initializable {

    @FXML
    private GridPane formContener;
    @FXML
    private TextField valeurNominal_textField;
    @FXML
    private ComboBox<String> type_textField;
    @FXML
    private TextField price_textField;
    @FXML
    private TextField title_textField;
    @FXML
    private Label title_label;
    @FXML
    private Label anneeEdition_label;
    @FXML
    private Label valeurNominal_label;
    @FXML
    private Label type_label;
    @FXML
    private Label price_label;
    @FXML
    private Label fileName_label;
    @FXML
    private Label imgName_label;
    @FXML
    private Label description_label;
    @FXML
    private TextArea descriptiopn_text_array;
    @FXML
    private TextField anneeEditionTextField;
    @FXML
    private Button fileName_btn;
    @FXML
    private Button uploadImgName_btn;
    @FXML
    private Label category_label;
    @FXML
    private ComboBox category_textField;


    private  File selectedFile;
    private File selectedImg;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        type_textField.getItems().addAll("public", "privé");
        type_textField.setValue("public");
    }

    @FXML
    public void onUpLoadFieldName(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file nbook");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Text Files", "*.pdf"
        ));
         selectedFile = fileChooser.showOpenDialog(((Control) actionEvent.getSource()).getScene().getWindow());
        System.out.println(selectedFile.getName());
    }

    @FXML
    public void onUploadImgName(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image book");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Imge Files", "*.png", "*.jpng", "*.jpg"
        ));
        selectedImg = fileChooser.showOpenDialog(((Control) actionEvent.getSource()).getScene().getWindow());
    }

    @FXML
    public void onSave(ActionEvent actionEvent) {
        boolean isGood = true;
//        formContener.getChildren().forEach(c ->{
//            c.getStyleClass().removeAll();
//        });

        System.out.println("click effectué");

        /*category_textField.getStyleClass().removeAll();
        title_textField.getStyleClass().removeAll();
        anneeEditionTextField.getStyleClass().removeAll();
        valeurNominal_textField.getStyleClass().removeAll();
        type_textField.getStyleClass().removeAll();
        price_textField.getStyleClass().removeAll();
        fileName_btn.getStyleClass().removeAll();
        uploadImgName_btn.getStyleClass().removeAll();

        title_label.getStyleClass().removeAll();
        category_label.getStyleClass().removeAll();
        anneeEdition_label.getStyleClass().removeAll();
        valeurNominal_label.getStyleClass().removeAll();
        type_label.getStyleClass().removeAll();
        price_label.getStyleClass().removeAll();
        fileName_label.getStyleClass().removeAll();
        imgName_label.getStyleClass().removeAll();*/

        if(category_textField.getValue() == null){
           isGood = false;
           Verification.dangerField(category_textField, category_label);
        }

        if(title_textField.getText() == null){
            Verification.dangerField(title_textField, title_label);
            isGood = false;
        }

        if(anneeEditionTextField.getText() == null){
            Verification.dangerField(anneeEditionTextField, title_label);

            if (!Verification.numeric(anneeEditionTextField.getText()) ||
                    anneeEditionTextField.getText().length() != 4) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error in form");
                alert.setContentText("the value of field year is not correctly. Please enter tone numeric value and " +
                        "length of value is 4 characters");
                alert.setResizable(false);
                alert.showAndWait();
            }
            isGood = false;
        }

        if(valeurNominal_textField.getText() == null){
            Verification.dangerField(valeurNominal_textField, valeurNominal_label);
            isGood = false;
        }
        if(type_textField.getValue() == null){
            Verification.dangerField(type_textField, type_label);
            isGood = false;
        }
        if(price_textField.getText() == null){
            Verification.dangerField(price_textField, price_label);
            isGood = false;
        }
        if(selectedFile == null){
            Verification.dangerField(fileName_btn, fileName_label);
            isGood = false;
        }
        if(selectedImg == null){
            Verification.dangerField(uploadImgName_btn, imgName_label);
            isGood = false;
        }
        if(descriptiopn_text_array.getParagraphs() == null){
            Verification.dangerField(descriptiopn_text_array, description_label);
            isGood = false;
        }

    }

}
