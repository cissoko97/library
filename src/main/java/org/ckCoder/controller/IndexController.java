package org.ckCoder.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import org.ckCoder.models.User;
import org.ckCoder.service.UserService;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class IndexController implements Initializable {
    UserService userService = null;

    public void initialize(URL location, ResourceBundle resources) {
        userService = new UserService();
    }

    public void Hello(ActionEvent actionEvent) {
    }
}
