package org.ckCoder.controller.user;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.ckCoder.controller.IndexController;
import org.ckCoder.controller.utils.BottonComponent;
import org.ckCoder.controller.utils.ControlBtn;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserControler implements Initializable {
    private Text id_userText;
    private Text idAuthorText;
    private Text idAccountText;
    private Text biographieText;
    private Text dateUpdateAccount;
    private Text dateCreatedText;
    private Text isLockText;
    private Text usernameText;
    private Text dateLastUpdatText;
    private Text dateCreateAccountText;
    private Text surnameText;
    private Text nameUserText;
    private TextFlow biographieTextFlow;

    /*@FXML
    private HBox rootFor_btnControler;*/
    @FXML
    private ControlBtn controlBtnController;

    @FXML
    private BottonComponent bottonComponentController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controlBtnController.getAdd_btn().setOnAction(event -> {
            Stage stage = new Stage();
            Scene scene = null;
            try {
                scene = new Scene(IndexController.loadFXML("/view/partial/form_save_book"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setScene(scene);
            stage.setTitle("Save user Form");
            //do this for get current stage
            stage.initOwner(((Control) event.getSource()).getScene().getWindow());
            // or do this
            //stage.initOwner(this.btn.getScene().getWindows());
            stage.show();
        });
    }
}
