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
import org.ckCoder.utils.Verification;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class OrderViewDetail implements Initializable {
    @FXML
    public Text title_text;
    @FXML
    public TableView<Line> tableView;
    @FXML
    public Button acceptOrder_btn;

    private Command command;

    private final OrderService orderService = new OrderService();


    public OrderViewDetail(Command cmd) {
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
        title_text.setText("ORDER : " + this.command.getId());
        acceptOrder_btn.setText("Accept order");
        if(command.getAccepted())
            acceptOrder_btn.setDisable(true);
        else {
            acceptOrder_btn.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Valid purchasse order");
                alert.setContentText("are you sure you want to change the status of this command ?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() == ButtonType.OK) {
                    try {
                        if (orderService.change_status_command(command.getId())) {
                            command.setAccepted(true);
                            Verification.alertMessage("you have just validated this order", Alert.AlertType.INFORMATION);
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            });
        }

        TableColumn<Line, String> titreCol = new TableColumn<>();
        titreCol.setText("Title");
        titreCol.setCellValueFactory(item -> new ReadOnlyObjectWrapper<>(item.getValue().getBook().getTitle()));

        TableColumn<Line, Double> priceCol = new TableColumn<>();
        priceCol.setText("price");
        priceCol.setCellValueFactory(item -> new ReadOnlyObjectWrapper<>(item.getValue().getBook().getPrice()));

        TableColumn<Line, LocalDateTime> createdCol = new TableColumn<>();
        createdCol.setText("created");
        createdCol.setCellValueFactory(item -> new ReadOnlyObjectWrapper<>(item.getValue().getBook().getCreatedAt()));

        TableColumn<Line, LocalDateTime> updateCol = new TableColumn<>();
        updateCol.setText("last update");
        updateCol.setCellValueFactory(item -> new ReadOnlyObjectWrapper<>(item.getValue().getBook().getUpdatedAt()));

        ObservableList<Line> observableList = FXCollections.observableArrayList(orderService.findOrderAndLineOrder(command.getId()));

        tableView.setItems(observableList);
        tableView.getColumns().addAll(titreCol, priceCol, createdCol, updateCol);

    }

    public Command getCommand() {
        return command;
    }
}
