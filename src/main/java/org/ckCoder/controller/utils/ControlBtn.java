package org.ckCoder.controller.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.ckCoder.controller.IndexController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControlBtn implements Initializable {
    @FXML
    public HBox gridPaneRoot;
    @FXML
    private Button add_btn;
    @FXML
    private Button delete_btn;
    @FXML
    private Button update_btn3;
    @FXML
    private Button load_btn;
    @FXML
    private Button lockUser_btn;
    @FXML
    private Button addFavory_btn;
    @FXML
    private Button addCaddyBtn;


    @FXML
    public void onSave(ActionEvent actionEvent) throws IOException {

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public Button getAdd_btn() {
        return add_btn;
    }

    public void setAdd_btn(Button add_btn) {
        this.add_btn = add_btn;
    }

    public Button getDelete_btn() {
        return delete_btn;
    }

    public void setDelete_btn(Button delete_btn) {
        this.delete_btn = delete_btn;
    }

    public Button getUpdate_btn3() {
        return update_btn3;
    }

    public void setUpdate_btn3(Button update_btn3) {
        this.update_btn3 = update_btn3;
    }

    public Button getLoad_btn() {
        return load_btn;
    }

    public HBox getGridPaneRoot() {
        return gridPaneRoot;
    }

    public void setGridPaneRoot(HBox gridPaneRoot) {
        this.gridPaneRoot = gridPaneRoot;
    }

    public void setLoad_btn(Button load_btn) {
        this.load_btn = load_btn;
    }

    public Button getLockUser_btn() {
        return lockUser_btn;
    }

    public void setLockUser_btn(Button lockUser_btn) {
        this.lockUser_btn = lockUser_btn;
    }

    public Button getAddFavory_btn() {
        return addFavory_btn;
    }

    public void setAddFavory_btn(Button addFavory_btn) {
        this.addFavory_btn = addFavory_btn;
    }

    public Button getAddCaddyBtn() {
        return addCaddyBtn;
    }

    public void setAddCaddyBtn(Button addCaddyBtn) {
        this.addCaddyBtn = addCaddyBtn;
    }
}
