package org.ckCoder.controller.partial;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.ckCoder.utils.PathFile;

public class FormLogin {
    @FXML
    public Button submitBTN;
    @FXML
    public Text title_text;
    @FXML
    public Label password_label;
    @FXML
    public TextField passwordTextField;
    @FXML
    public TextField usernameTextField;
    @FXML
    public Label username_label;

    @FXML
    public void onLogin(ActionEvent actionEvent) {
        //PathFile.loadFXML()
    }
}
