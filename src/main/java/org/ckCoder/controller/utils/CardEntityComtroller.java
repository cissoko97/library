package org.ckCoder.controller.utils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.ckCoder.models.Book;
import org.ckCoder.utils.UtilForArray;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class CardEntityComtroller extends ListCell<Book> implements Initializable {
    @FXML
    public HBox principalPanelGrildPane;
    @FXML
    public Text nameBook_text;
    @FXML
    public Text noteTextField;
    @FXML
    public Text typeTextFied;
    @FXML
    public Text priceTextField;


    public ImageView imgView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    protected void updateItem(Book item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/utils/card_entity.fxml"));
            loader.setController(this);
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            nameBook_text.setText(item.getTitle());
            noteTextField.setText((item.getValeurNominal() + item.getValeurCritique())+"");
            typeTextFied.setText(item.getType());
            if(item.getPrice() > 0.0)
                priceTextField.setText(item.getPrice()+" XAF");
            else
                priceTextField.setText("");

            InputStream file = new ByteArrayInputStream(item.getImgBinary());
            Image image = new Image(file);
            imgView.setImage(image);
            ImageView imageView = UtilForArray.getImageView(100, item.getImgBinary());
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);
            setText(null);
            setGraphic(principalPanelGrildPane);
        }

    }
}
