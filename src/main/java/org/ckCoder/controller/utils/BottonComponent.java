package org.ckCoder.controller.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class BottonComponent {
    @FXML
    public Label title_labe;

    @FXML
    public Label decription_label;

    @FXML
    public TextField titleTextFied;

    @FXML
    public TextArea descriptionTextArray;

    @FXML
    public Button submitOrUpdude_btn;

    @FXML
    public TableView tableView;

    @FXML
    public GridPane formArray;
    @FXML
    public Button reset_btn;

    public void onSaveOrUpdate(ActionEvent actionEvent) {

    }
}
