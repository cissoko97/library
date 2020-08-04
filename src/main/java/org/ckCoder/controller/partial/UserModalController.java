package org.ckCoder.controller.partial;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.ckCoder.models.User;
import org.ckCoder.utils.SelectedLanguage;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class UserModalController implements Initializable {

    @FXML
    private Text title_user_form;
    @FXML
    private Label label_user_name;
    @FXML
    private Label label_user_surname;
    @FXML
    private Label label_u_email;
    @FXML
    private Label label_u_password;
    @FXML
    private Label label_u_password_conf;

    Properties properties = SelectedLanguage.getInstace();
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
    private PasswordField userPassword;
    @FXML
    private PasswordField userPasswordConfirm;
    @FXML
    public Tab emptyUser;

    @FXML
    public Tab fromPerson;

    @FXML
    public TabPane modalUserTab;

    public UserModalController() throws IOException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initLabel();
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
        } else {
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

    private void initLabel() {
        label_user_name.setText(properties.getProperty("NAME_LABEL_USERFORM"));
        label_user_surname.setText(properties.getProperty("SURNAME_LABEL_USERFORM"));
        label_u_email.setText(properties.getProperty("EMAIL_LABEL_USERFORM"));
        label_u_password.setText(properties.getProperty("PASSWORD_LABEL_USERFORM"));
        label_u_password_conf.setText(properties.getProperty("PASSWORD_CONF_USERFORM"));
        update_btn.setText(properties.getProperty("REGISTER_BTN_USERFORM"));
        title_user_form.setText(properties.getProperty("TITLE_LABEL_USERFORM"));
    }
}
