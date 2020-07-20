package org.ckCoder.controller.partial;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;
import org.ckCoder.models.User;

import java.net.URL;
import java.util.ResourceBundle;

public class UserModalController implements Initializable {

    Logger logger = Logger.getLogger(this.getClass());
    public TextField usernameTextField;
    private User user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setUser(User user) {
        this.user = user;
        initField();
    }

    public void onSave(ActionEvent actionEvent) {
    }

    private void initField() {
        if (user != null) {
            usernameTextField.setText(user.getPerson().getSurname());
        } else {
            usernameTextField.setText("");
        }
    }
}
