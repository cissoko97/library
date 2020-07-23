package org.ckCoder.controller.user;

import com.sun.istack.internal.NotNull;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.ckCoder.MainApp;
import org.ckCoder.controller.partial.UserModalController;
import org.ckCoder.controller.utils.BottonComponent;
import org.ckCoder.controller.utils.ControlBtn;
import org.ckCoder.controller.utils.ListCellUser;
import org.ckCoder.models.Profil;
import org.ckCoder.models.User;
import org.ckCoder.service.ProfilService;
import org.ckCoder.service.UserService;
import org.ckCoder.utils.DateConverted;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Set;

public class UserControler implements Initializable {

    //Logger devlaration
    Logger logger = Logger.getLogger(this.getClass());
    public ListView<User> userListView;
    // field binding
    public Text userId;
    public Text userUpdateddAt;
    public Text userCreatedAt;
    public Text userIsLocked;
    public Text userEmail;
    public Text personUpdatedAt;
    public Text personCreatedAt;
    public Text userSurname;
    public Text userName;
    //Service declaration
    ProfilService profilService = new ProfilService();
    UserService userService = new UserService();

    //Glogal Instance declaration
    Profil profilInstance = null;
    User selectedUser = null;
    private TableColumn<Profil, Long> fieldId = new TableColumn();
    private TableColumn<Profil, String> fieldLabel = new TableColumn();
    private TableColumn<Profil, String> fieldDescription = new TableColumn();


    //Observable declaration
    private ObservableList<Profil> observableListTable;
    private ObservableList<User> observableListView;

    @FXML
    private ControlBtn controlBtnController;

    @FXML
    private BottonComponent bottonComponentController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        showUserDetail(null);
        initBottomComponent();
        initBUttonComponent();

        initListViewUser();
    }

    private void initBottomComponent() {

        Set<Profil> profils = profilService.findAll((new Profil()));
        System.out.println("Liste de profils");
        System.out.println(profils);
        //Init table view for Profils Items

        bottonComponentController.tableView.getColumns().clear();
        fieldId.setText("id");
        fieldLabel.setText("Label");
        fieldDescription.setText("Description");

        fieldId.setCellValueFactory(new PropertyValueFactory<>("id"));
        fieldLabel.setCellValueFactory(new PropertyValueFactory<>("label"));
        fieldDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        observableListTable = FXCollections.observableArrayList();
        observableListTable.addAll(profils);
        bottonComponentController.tableView.getColumns().addAll(fieldId, fieldLabel, fieldDescription);

        bottonComponentController.tableView.setItems(observableListTable);
        this.bottonComponentController.submitOrUpdude_btn.setText("Save");
        this.bottonComponentController.title_labe.setText("title");
        this.bottonComponentController.decription_label.setText("Description");

        bottonComponentController.submitOrUpdude_btn.setOnAction(event -> {
            String label = this.bottonComponentController.titleTextFied.getText();
            String description = this.bottonComponentController.descriptionTextArray.getText();

            if (profilInstance == null) {
                profilInstance = new Profil();
            }
            profilInstance.setLabel(label);
            profilInstance.setDescription(description);

            try {
                Profil profil;
                if (profilInstance.getId() == 0) {
                    profil = profilService.create(profilInstance);
                    bottonComponentController.tableView.getItems().add(profil);
                } else {
                    profil = profilService.update(profilInstance);
                    int index = bottonComponentController.tableView.getItems().indexOf(profil);
                    bottonComponentController.tableView.getItems().set(index, profil);
                }
                clearBottomField();
                profilInstance = null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        bottonComponentController.tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            hydratBottomField((Profil) newSelection);
            profilInstance = (Profil) newSelection;
        });

        bottonComponentController.reset_btn.setOnAction(event -> {
            clearBottomField();
            profilInstance = null;
        });
    }

    private void initBUttonComponent() {

        //init label
        controlBtnController.getAdd_btn().setText("New User");
        controlBtnController.getDelete_btn().setText("Update User");
        controlBtnController.getDelete_btn().setDisable(true);
        controlBtnController.getUpdate_btn3().setVisible(Boolean.FALSE);
        controlBtnController.getLoad_btn().setVisible(Boolean.FALSE);
        controlBtnController.getAddCaddyBtn().setVisible(Boolean.FALSE);
        controlBtnController.getAddFavory_btn().setVisible(Boolean.FALSE);
        controlBtnController.getLockUser_btn().setText("Lock User");
        controlBtnController.getLockUser_btn().setDisable(true);

        controlBtnController.getAdd_btn().setOnAction(event -> {
            Stage stage = new Stage();
            Scene scene = null;
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource("/view/partial/form_add_user.fxml"));
                BorderPane page = loader.load();

                //do this for get current stage
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((Control) event.getSource()).getScene().getWindow());

                scene = new Scene(page);
                stage.setScene(scene);
                stage.setTitle("Save user Form");

                UserModalController userModalController = loader.getController();
                selectedUser = null;
                userModalController.setUser(selectedUser);
                userModalController.setDialogStage(stage);
                stage.showAndWait();

                boolean operation = userModalController.getFlag();
                boolean hasSave = userModalController.getIsOkClicked();
                selectedUser = userModalController.getUser();

                if (hasSave && operation) {
                    logger.warn(selectedUser);
                    User saveUser = userService.create(selectedUser);
                    if (saveUser != null) {
                        userListView.getItems().add(saveUser);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        controlBtnController.getDelete_btn().setOnAction(event -> {
            Stage stage = new Stage();
            Scene scene = null;
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource("/view/partial/form_add_user.fxml"));
                BorderPane page = loader.load();

                //do this for get current stage
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((Control) event.getSource()).getScene().getWindow());

                scene = new Scene(page);
                stage.setScene(scene);
                stage.setTitle("Update User");
                // Initialize Modal controller
                UserModalController userModalController = loader.getController();
                userModalController.setUser(selectedUser);
                userModalController.setDialogStage(stage);
                stage.showAndWait();

                boolean hasSave = userModalController.getIsOkClicked();
                boolean operation = userModalController.getFlag();

                if (hasSave && !operation) {
                    logger.debug("update existing user!!");
                    User updatedUser = userService.update(selectedUser);
                    if (updatedUser != null) {
                        int index = userListView.getItems().indexOf(updatedUser);
                        userListView.getItems().set(index, updatedUser);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        controlBtnController.getLockUser_btn().setOnAction(event -> {
            boolean result = userService.setStatus(selectedUser);
            if (result) {
                selectedUser.setLocked(!selectedUser.getLocked());
            }
        });
    }

    private void clearBottomField() {
        this.bottonComponentController.titleTextFied.setText("");
        this.bottonComponentController.descriptionTextArray.setText("");
    }

    private void hydratBottomField(Profil profil) {
        this.bottonComponentController.titleTextFied.setText(profil.getLabel());
        this.bottonComponentController.descriptionTextArray.setText(profil.getDescription());
    }

    private void initListViewUser() {
        Set<User> users = userService.findAll(null);
        observableListView = FXCollections.observableArrayList();
        observableListView.addAll(users);
        System.out.println(users);
        userListView.setItems(observableListView);
        userListView.setCellFactory(studentListView -> new ListCellUser());
        userListView.setOnMouseClicked(event -> {
            User selectedItem = userListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                selectedUser = selectedItem;
                controlBtnController.getDelete_btn().setDisable(false);
                controlBtnController.getLockUser_btn().setDisable(false);
                showUserDetail(selectedItem);
                setLabelOnLockButton(selectedItem);
            } else {
                controlBtnController.getDelete_btn().setDisable(true);
                controlBtnController.getLockUser_btn().setDisable(true);
            }
        });
    }

    private void showUserDetail(User user) {
        if (user == null) {
            userId.setText("");
            userUpdateddAt.setText("");
            userCreatedAt.setText("");
            userIsLocked.setText("");
            userEmail.setText("");
            personUpdatedAt.setText("");
            personCreatedAt.setText("");
            userSurname.setText("");
            userName.setText("");
        } else {
            userId.setText(String.valueOf(user.getId()));
            userUpdateddAt.setText(DateConverted.formatDateToString(user.getUpdatedAt()));
            userCreatedAt.setText(DateConverted.formatDateToString(user.getCreatedAt()));
            userIsLocked.setText(String.valueOf(user.getLocked()));
            userEmail.setText(user.getEmail());
            userSurname.setText(user.getPerson().getSurname());
            userName.setText(user.getPerson().getName());
            personUpdatedAt.setText(DateConverted.formatDateToString(user.getPerson().getUpdatedAt()));
            personCreatedAt.setText(DateConverted.formatDateToString(user.getPerson().getCreatedAt()));
        }
    }

    private void setLabelOnLockButton(@NotNull User user) {
        if (user.getLocked()) {
            controlBtnController.getLockUser_btn().setText("Un Lock");
        } else {
            controlBtnController.getLockUser_btn().setText("lock");
        }
    }
}
