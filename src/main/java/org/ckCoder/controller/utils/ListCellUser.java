package org.ckCoder.controller.utils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import org.ckCoder.models.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ListCellUser extends ListCell<User> implements Initializable {
    FXMLLoader mLLoader = null;

    @FXML
    public BorderPane gridPane;

    @FXML
    public Text filigrane;
    @FXML
    public Text name;
    @FXML
    public Text surname;
    @FXML
    public Text email;

    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/view/utils/user_card_item.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            filigrane.setText(String.valueOf(item.getPerson().getName().charAt(0)).toUpperCase());
            //
            name.setText(item.getPerson().getName());
            surname.setText(item.getPerson().getSurname());
            email.setText(item.getEmail());

            setText(null);
            setGraphic(gridPane);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
