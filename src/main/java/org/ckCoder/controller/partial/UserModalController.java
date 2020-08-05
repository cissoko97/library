package org.ckCoder.controller.partial;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.ckCoder.models.User;
import org.ckCoder.utils.NotificationType;
import org.ckCoder.utils.NotificationUtil;
import org.ckCoder.utils.SelectedLanguage;
import org.ckCoder.utils.Verification;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;

public class UserModalController implements Initializable {

    @FXML
    private GridPane user_grid;

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

    private HashMap<String, String> validateField() {

        HashMap<String, String> errors = new HashMap<>();

        String username = userName.getText();
        String usersurname = userSurname.getText();
        String useremail = userEmail.getText();
        String password = userPassword.getText();
        String passwordConf = userPasswordConfirm.getText();

        if (username.equalsIgnoreCase("") || username.length() < 4) {
            Verification.dangerField(userName);
            errors.put("name", properties.getProperty("NAME_CONSTRAINST"));
        }

        if (usersurname.equalsIgnoreCase("") || usersurname.length() < 4) {
            errors.put("surname", properties.getProperty("SURNAME_CONSTRAINST"));
            Verification.dangerField(userSurname);
        }

        if (useremail.equalsIgnoreCase("") || !Verification.emailVerificatio(useremail)) {
            errors.put("email", properties.getProperty("EMAIL_CONSTRAINST"));
            Verification.dangerField(userEmail);
        }

        if (!password.equalsIgnoreCase(passwordConf) || password.length() < 6) {
            errors.put("password", properties.getProperty("PASSWORD_CONSTRAINT"));
            Verification.dangerField(userPassword);
            Verification.dangerField(userPasswordConfirm);
        }

        return errors;
    }

    private void initButton() {
        update_btn.setOnAction(event -> {
            user_grid.getChildren().forEach(p -> Verification.remouveDangerClass(p));

            boolean action = this.hydrateUserWithNewData();
            if (action) {
                okClicked = true;
                this.dialogStage.close();
                NotificationUtil.showNotiication(String.valueOf(NotificationType.SUCCES),
                        properties.getProperty("NOTIFICATION_TITLE_USER"),
                        properties.getProperty("NOTIFICATION_MESSAGE_USER"));
            }
        });

        save_btn.setOnAction(event -> {
            this.hydrateUserWithNewData();
            okClicked = true;
            this.dialogStage.close();
        });
    }

    private Boolean hydrateUserWithNewData() {
        //TODO:: make field validation before update user information
        // check if user are null and set new User instance()
        HashMap<String, String> errors = validateField();
        boolean decision = false;
        if (errors.size() == 0) {
            if (user == null) {
                flag = true;
                user = new User();
            }
            user.getPerson().setName(userName.getText());
            user.getPerson().setSurname(userSurname.getText());
            user.setEmail(userEmail.getText());
            user.setPassword(userPassword.getText());
            decision = true;
        } else {
            Verification.alertMessage(errors, Alert.AlertType.ERROR);
        }

        return decision;
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
