package org.ckCoder.controller.book;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import org.ckCoder.models.Critique;
import org.ckCoder.utils.UtilForArray;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CardCritiqueController extends ListCell<Critique> {

    @FXML
    public BorderPane root_borderPane;
    @FXML
    public Text usernameText;
    @FXML
    public Text noteText;
    @FXML
    public Text commentTextArray;

    @Override
    protected void updateItem(Critique item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/utils/critique_card.fxml"));
            loader.setController(this);
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            usernameText.setText(item.getUser().getEmail());
            noteText.setText(item.getNote()+"");
            commentTextArray.setText(item.getComment());
            setText(null);
            setGraphic(root_borderPane);
        }
    }
}
