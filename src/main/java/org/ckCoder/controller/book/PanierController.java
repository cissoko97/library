package org.ckCoder.controller.book;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;
import org.ckCoder.models.Book;
import org.ckCoder.service.OrderService;
import org.ckCoder.utils.SessionManager;
import org.ckCoder.utils.Verification;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class PanierController implements Initializable {
    private SessionManager manager = SessionManager.getInstance();
    private Logger logger = Logger.getLogger(this.getClass());

    private OrderService orderService = new OrderService();


    @FXML
    private Text total_value;

    @FXML
    private TableView tableBook;

    @FXML
    private Text total_labet;

    @FXML
    private Text totalLabelContent;

    @FXML
    private Button commander_btn;
    private Set<Long> listId = new HashSet<>();
    private ObservableList<Book> observableListBook;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info(manager.getBookSet());
        initTableColumn();
        initDataInTable();
    }

    public void onOrder(ActionEvent event) throws SQLException {
        if (manager.getBookSet().size() != 0) {
            orderService.created(this.listId, manager.getUser().getId());
            manager.getBookSet().clear();
            this.listId.clear();
            this.observableListBook.clear();
           // this.tableBook.getSelectionModel().clearSelection();
            Verification.alertMessage("your order is loading", Alert.AlertType.INFORMATION);
            ((Control) event.getSource()).getScene().getWindow().hide();
        } else {
            Verification.alertMessage("Pas de livres dans votre panier", Alert.AlertType.INFORMATION);
        }
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

        manager.getBookSet().stream().forEach(book -> listId.add(book.getId()));
        logger.info("List des id dans le panier");
        logger.info(listId);
        total_value.setText(String.valueOf(totalPrice));


    }
}
