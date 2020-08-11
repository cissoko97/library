package org.ckCoder.controller.book;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Window;
import org.ckCoder.models.Book;
import org.ckCoder.models.Critique;
import org.ckCoder.models.User;
import org.ckCoder.service.CritiqueService;
import org.ckCoder.utils.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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

    @FXML
    private Label note_label;
    @FXML
    private Label comment_label;
    @FXML
    private Text title_view_label;

    private Book book;

    private boolean isGood;

    private final CritiqueService critiqueService = new CritiqueService();
    private Properties properties = SelectedLanguage.getInstace();

    public CritiqueController() throws IOException {
    }

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
        try {
            internationnalisation();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            NotificationUtil.showNotiication(NotificationType.SUCCES.toString(),
                properties.getProperty("MESSAGE_SAVE_CRITIQUE_TITLE"),
                properties.getProperty("MESSAGE_SAVE_CRITIQUE"));
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

    private void internationnalisation() throws IOException {
        Properties properties = SelectedLanguage.getInstace();
        title_view_label.setText(properties.getProperty("TITLE_CRITIQUEPAGE"));
        note_combobox.setPromptText(properties.getProperty("NOTE_LABEL_CRITIQUEPAGE"));
        comment_label.setText(properties.getProperty("COMMENT_LABEL_CRITIQUEPAGE"));
        saveBotton.setText(properties.getProperty("SAVE_BTN_CRITIQUEPAGE"));
        note_label.setText(properties.getProperty("NOTE_LABEL_CRITIQUEPAGE"));
    }
}
