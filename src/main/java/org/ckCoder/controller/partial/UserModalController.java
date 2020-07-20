package org.ckCoder.controller.partial;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.ckCoder.models.User;

import java.net.URL;
import java.util.ResourceBundle;

public class UserModalController implements Initializable {

    public TextField usernameTextField;
    private User user;

    @Override

    public void initialize(URL location, ResourceBundle resources) {
        initField();
    }

    public void setUser(User user) {
        System.out.println("Set User " + user);
        this.user = user;
    }

    public void onSave(ActionEvent actionEvent) {
    }

    private void initField() {
        System.out.println("Valeur de user dans le modal " + user.toString());
        if (user != null) {
            usernameTextField.setText(user.getPerson().getSurname());
        } else {
            usernameTextField.setText("User is null");
        }
    }
}
