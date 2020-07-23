package org.ckCoder.controller.partial;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.ckCoder.models.User;

import java.net.URL;
import java.util.ResourceBundle;

public class UserModalController implements Initializable {

    Logger logger = Logger.getLogger(this.getClass());
    private User user;
    private Stage dialogStage;
    private boolean okClicked = false;
    private boolean flag = false;

    @FXML
    private ComboBox<?> personneCombobox;

    @FXML
    private TextField personEmail;

    @FXML
    private PasswordField personPassword;

    @FXML
    private Button save_btn;

    @FXML
    private Button update_btn;

    @FXML
    private TextField userName;

    @FXML
    private TextField userSurname;

    @FXML
    private TextField userEmail;

    @FXML
    private TextField userPassword;

    @FXML
    public Tab emptyUser;

    @FXML
    public Tab fromPerson;

    @FXML
    public TabPane modalUserTab;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initButton();
    }

    public void setUser(User user) {
        this.user = user;
        initField();
    }

    public void onSave(ActionEvent actionEvent) {


    }

    private void initField() {
        fromPerson.setDisable(true);
        modalUserTab.getSelectionModel().select(emptyUser);
        if (user != null) {
            userSurname.setText(user.getPerson().getSurname());
            userEmail.setText(user.getEmail());
            userName.setText(user.getPerson().getName());
        }else{
            userEmail.setEditable(true);
        }
    }

    private boolean validateField() {
        return false;
    }

    private void initButton() {
        update_btn.setOnAction(event -> {
            this.hydrateUserWithNewData();
            okClicked = true;
            this.dialogStage.close();
        });

        save_btn.setOnAction(event -> {
            this.hydrateUserWithNewData();
            okClicked = true;
            this.dialogStage.close();
        });
    }

    private void hydrateUserWithNewData() {
        //TODO:: make field validation before update user information
        // check if user are null and set new User instance()
        if (user == null) {
            flag = true;
            user = new User();
        }
        user.getPerson().setName(userName.getText());
        user.getPerson().setSurname(userSurname.getText());
        user.setEmail(userEmail.getText());
        user.setPassword(userPassword.getText());
    }

    public void setDialogStage(Stage stage) {
        dialogStage = stage;
    }

    public boolean getIsOkClicked() {
        return okClicked;
    }

    public boolean getFlag() {
        return flag;
    }

    public User getUser() {
        return user;
    }
}
