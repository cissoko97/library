package org.ckCoder.controller.book;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;
import org.ckCoder.models.Book;
import org.ckCoder.utils.SessionManager;

import java.net.URL;
import java.util.ResourceBundle;

public class PanierController implements Initializable {
    public Text total_value;
    SessionManager manager = SessionManager.getInstance();
    Logger logger = Logger.getLogger(this.getClass());


    public TableView tableBook;
    public Text total_labet;
    public Text totalLabelContent;
    public Button commander_btn;

    private ObservableList<Book> observableListBook;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info(manager.getBookSet());
        initTableColumn();
        initDataInTable();
    }

    public void onOrder(ActionEvent actionEvent) {
    }

    private void initTableColumn() {
        TableColumn<Book, Long> columnId = new TableColumn<>();
        TableColumn<Book, String> columnName = new TableColumn<>();
        TableColumn<Book, String> columnPrice = new TableColumn<>();

        columnId.setText("Id");
        columnName.setText("Title");
        columnPrice.setText("Price");

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableBook.getColumns().addAll(columnId, columnName, columnPrice);
    }

    private void initDataInTable() {
        observableListBook = FXCollections.observableArrayList();
        observableListBook.addAll(manager.getBookSet());
        tableBook.setItems(observableListBook);

        double totalPrice = manager.getBookSet().stream().mapToDouble(value -> value.getPrice()).sum();
        total_value.setText(String.valueOf(totalPrice));
    }
}
