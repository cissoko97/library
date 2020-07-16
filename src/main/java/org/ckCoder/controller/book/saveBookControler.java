package org.ckCoder.controller.book;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class saveBookControler implements Initializable {

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
        boolean isGood = false;
        if(category_textField.getValue() == null){
           category_textField.getStyleClass().add("form-input-error");
           category_label.getStyleClass().add("text-danger");
        }

        if(title_textField.getText() == null){
            title_textField.getStyleClass().add("form-input-error");
            title_label.getStyleClass().add("text-danger");
        }

        if(anneeEdition_label.getText() == null){
            anneeEditionTextField.getStyleClass().add("form-input-error");
            anneeEdition_label.getStyleClass().add("text-danger");
        }

        if(valeurNominal_textField.getText() == null){
            valeurNominal_textField.getStyleClass().add("form-input-error");
            valeurNominal_textField.getStyleClass().add("text-danger");
        }
        if(type_textField.getValue() == null){
            type_textField.getStyleClass().add("form-input-error");
            type_label.getStyleClass().add("text-danger");
        }
        if(price_textField.getText() == null){
            price_textField.getStyleClass().add("form-input-error");
            price_label.getStyleClass().add("text-danger");
        }
        if(selectedFile == null){
            fileName_btn.getStyleClass().add("form-input-error");
            fileName_label.getStyleClass().add("text-danger");
        }
        if(selectedImg == null){
            uploadImgName_btn.getStyleClass().add("form-input-error");
            imgName_label.getStyleClass().add("text-danger");
        }
        if(descriptiopn_text_array.getParagraphs() == null){
            descriptiopn_text_array.getStyleClass().add("form-input-error");
            description_label.getStyleClass().add("text-danger");
        }

    }


}
