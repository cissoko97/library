package org.ckCoder.controller.book;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Window;
import org.ckCoder.models.Book;
import org.ckCoder.models.Critique;
import org.ckCoder.models.User;
import org.ckCoder.service.CritiqueService;
import org.ckCoder.utils.SessionManager;
import org.ckCoder.utils.Verification;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CritiqueController implements Initializable {

    SessionManager manager = SessionManager.getInstance();
    User user = manager.getUser();
    @FXML
    public TextArea comment_textArray;
    @FXML
    public ComboBox<Integer> note_combobox;

    @FXML
    private Button saveBotton;

    private Book book;

    private boolean isGood;

    private final CritiqueService critiqueService = new CritiqueService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        note_combobox.getItems().addAll(1,2,3,4,5);
        note_combobox.setOnKeyTyped(event-> {
            if (event.getCode().equals(KeyCode.ENTER)) {
              note_combobox.setFocusTraversable(false);
              comment_textArray.requestFocus();
            }
        });

        comment_textArray.setOnKeyTyped(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if(valideAndSumitForm())
                    ((Control) event.getSource()).getScene().getWindow().hide();
            }
        });

        saveBotton.setOnAction(e ->{
            if (valideAndSumitForm()) {

                ((Control) e.getSource()).getScene().getWindow().hide();
            }
        });
    }


    private boolean valideAndSumitForm() {
        List<String> errorMessage = new ArrayList<>();
        if (note_combobox.getValue() == null) {
            Verification.dangerField(note_combobox);
            errorMessage.add("Please select one note");
        }
        if (comment_textArray.getText().equals("")) {
            Verification.dangerField(comment_textArray);
            errorMessage.add("pleace write about your observation");
        }

        if (errorMessage.size() > 0) {
            Verification.alertMessage(errorMessage, Alert.AlertType.ERROR);
            return false;
        } else {
            Critique critique = new Critique();
            critique.setNote(note_combobox.getValue());
            critique.setComment(comment_textArray.getText());
            critique.setUser(user);
            critique.setBook(book);
            //sauvegarde de la critique
            critiqueService.create(critique);

            Verification.alertMessage("your critique is save. Take you for your participation", Alert.AlertType.INFORMATION);
            isGood = true;
            return true;
        }
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public boolean isGood() {
        return isGood;
    }
}
