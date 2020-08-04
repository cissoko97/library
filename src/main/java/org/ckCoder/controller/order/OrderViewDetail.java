package org.ckCoder.controller.order;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.ckCoder.models.Command;
import org.ckCoder.models.Line;
import org.ckCoder.service.OrderService;
import org.ckCoder.utils.MailGateway;
import org.ckCoder.utils.SelectedLanguage;
import org.ckCoder.utils.Verification;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class OrderViewDetail implements Initializable {
    @FXML
    public Text title_text;
    @FXML
    public TableView<Line> tableView;
    @FXML
    public Button acceptOrder_btn;

    private Command command;

    Properties properties = SelectedLanguage.getInstace();

    private final OrderService orderService = new OrderService();

    public OrderViewDetail(Command cmd) throws IOException {
        this.command = cmd;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            setDataTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



    private void setDataTable() throws SQLException {
        ObservableList<Line> observableList = FXCollections.observableArrayList(orderService.findOrderAndLineOrder(command.getId()));
        title_text.setText(properties.getProperty("ORDERDETAIL_TITLE_VIEW").toUpperCase() + this.command.getId());
        acceptOrder_btn.setText("Accept order");
        if(command.getAccepted())
            acceptOrder_btn.setDisable(true);
        else {
            acceptOrder_btn.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle(properties.getProperty("ORDERPAGE_VALID_ORDER"));
                alert.setContentText(properties.getProperty("ORDERPAGE_CONFIRM_MESSAGE_VALIDED_ORDER"));
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() == ButtonType.OK) {
                    try {
                        if (!orderService.change_status_command(command.getId())) {
                            command.setAccepted(true);
                            Verification.alertMessage(properties.getProperty("ORDERPAGE_SUCCESFULL_VALID_ORDER"), Alert.AlertType.INFORMATION);
                            MailGateway.instanciate(command.getUser(), observableList);
                        }
                    } catch (SQLException | MessagingException throwables) {
                        throwables.printStackTrace();
                    }
                }
            });
        }

        TableColumn<Line, String> titreCol = new TableColumn<>();
        titreCol.setText(properties.getProperty("TITLE_LABEL_BOOKPAGE"));
        titreCol.setCellValueFactory(item -> new ReadOnlyObjectWrapper<>(item.getValue().getBook().getTitle()));

        TableColumn<Line, Double> priceCol = new TableColumn<>();
        priceCol.setText(properties.getProperty("PRICE_BOOK_LABEL_BOOKPAGE"));
        priceCol.setCellValueFactory(item -> new ReadOnlyObjectWrapper<>(item.getValue().getBook().getPrice()));

        TableColumn<Line, LocalDateTime> createdCol = new TableColumn<>();
        createdCol.setText(properties.getProperty("CREATION_BOOK_LABEL_BOOKPAGE"));
        createdCol.setCellValueFactory(item -> new ReadOnlyObjectWrapper<>(item.getValue().getBook().getCreatedAt()));

        TableColumn<Line, LocalDateTime> updateCol = new TableColumn<>();
        updateCol.setText(properties.getProperty("UPDATE_BOOK_LABEL_BOOKPAGE"));
        updateCol.setCellValueFactory(item -> new ReadOnlyObjectWrapper<>(item.getValue().getBook().getUpdatedAt()));

        tableView.setItems(observableList);
        tableView.getColumns().addAll(titreCol, priceCol, createdCol, updateCol);

    }

    public Command getCommand() {
        return command;
    }
}
