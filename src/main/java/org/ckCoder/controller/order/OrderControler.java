package org.ckCoder.controller.order;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.ckCoder.models.Book;
import org.ckCoder.models.Command;
import org.ckCoder.service.OrderService;
import org.ckCoder.utils.UtilForArray;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.ResourceBundle;

public class OrderControler implements Initializable {
    @FXML
    public TextField searchTextField;
    @FXML
    public ComboBox<Integer> resiCombobox;
    @FXML
    public TableView<Command> tableCmd_tableView;
    @FXML
    public Pagination paginate;
    @FXML
    public Button submitDateBtn;
    @FXML
    public DatePicker dateBegin;
    @FXML
    public DatePicker dateEnd;
    @FXML
    public Button reload_table_btn;

    private ObservableList<Command> observableList;
    private org.ckCoder.utils.pagination.Pagination<Command> commandPagination;
    private final OrderService orderService = new OrderService();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initPage();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        submitDateBtn.setText("search");
        submitDateBtn.setOnAction(event -> {
            try {
                findOrderBetweenTwoDate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        reload_table_btn.setText("reload");
        reload_table_btn.setOnAction(event -> {
            try {
                commandPagination = orderService.findAll(0, 25);
                observableList.clear();
                observableList.addAll(commandPagination.getRessource().resource);
                paginate.setPageCount(commandPagination.getRessource().info.totalPage);
                paginate.setCurrentPageIndex(0);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

    }

    private void initPage() throws SQLException {
        resiCombobox.getItems().addAll(10, 25, 50, 100, 150, 200);
        resiCombobox.setValue(10);

        TableColumn<Command, Double> priceCol = new TableColumn<>();
        priceCol.setText("total");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        TableColumn<Command, Double> acceptedCol = new TableColumn<>();
        acceptedCol.setText("isValid");
        acceptedCol.setCellValueFactory(new PropertyValueFactory<>("accepted"));

        TableColumn<Command, String> nameCol = new TableColumn<>();
        nameCol.setText("name");
        nameCol.setCellValueFactory(item -> new ReadOnlyObjectWrapper<>(item.getValue().getUser().getPerson().getName()));

        TableColumn<Command, Double> created_at = new TableColumn<>();
        created_at.setText("date creation");
        created_at.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        TableColumn<Command, Double> lastupdate = new TableColumn<>();
        lastupdate.setText("last update");
        lastupdate.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));

        TableColumn<Command, String> surnameCol = new TableColumn<>();
        surnameCol.setText("surname");
        surnameCol.setCellValueFactory(item -> new ReadOnlyObjectWrapper<>(item.getValue().getUser().getPerson().getSurname()));
/*
        TableColumn<Command, Void> btnCol = new TableColumn<>("");
        UtilForArray.setwidthBtn(btnCol);

        Callback<TableColumn<Command, Void>, TableCell<Command, Void>> cellData = new Callback<TableColumn<Command, Void>, TableCell<Command, Void>>() {
            @Override
            public TableCell<Command, Void> call(TableColumn<Command, Void> param) {
                final TableCell<Command, Void> cell = new TableCell<Command, Void>(){
                    final Button btn = new Button();
                    {
                        if(getTableView().getItems().get(getIndex()).getAccepted()){
                            btn.setText("V");
                            btn.setDisable(true);
                        }
                        else {
                            btn.setText("X");
                            btn.setOnAction(e->{

                            });
                        }
                        btn.getStyleClass().add("round-blue");

                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };*/

       // btnCol.setCellFactory(cellData);


        TableColumn<Command, Void> btnCol2 = new TableColumn<>("");
        UtilForArray.setwidthBtn(btnCol2);

        Callback<TableColumn<Command, Void>, TableCell<Command, Void>> cellData2 = new Callback<TableColumn<Command, Void>, TableCell<Command, Void>>() {
            @Override
            public TableCell<Command, Void> call(TableColumn<Command, Void> param) {
                final TableCell<Command, Void> cell = new TableCell<Command, Void>(){
                    final Button btn = new Button("View");
                    {
                        btn.getStyleClass().add("round-red");
                        btn.setOnAction(e->{
                            //
                        });
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        btnCol2.setCellFactory(cellData2);

        rangeData();

        tableCmd_tableView.getColumns().addAll(nameCol,surnameCol,priceCol,acceptedCol,created_at, lastupdate, btnCol2);


    }


    private void rangeData() throws SQLException {
        commandPagination = orderService.findAll(0, 25);
        observableList = FXCollections.observableArrayList();
        observableList.addAll(commandPagination.getRessource().resource);

        FilteredList<Command> filteredList = new FilteredList<>(observableList, p-> true);

        searchTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                filteredList.setPredicate(p-> {
                    if(t1 == null || t1.isEmpty())
                        return true;
                    else if (p.getUser().getPerson().getName().toLowerCase().contains(t1.toLowerCase()))
                        return true;
                    else if (p.getUser().getPerson().getSurname().toLowerCase().contains(t1.toLowerCase()))
                        return true;
                    else if (UtilForArray.isIntegr(t1))
                        return p.getTotalPrice() >= Double.parseDouble(t1);

                    return false;
                });
            }
        });

        tableCmd_tableView.setItems(filteredList);


        /*
            * gestion pagination
         */
        paginate.setPageCount(commandPagination.getRessource().info.totalPage);
        paginate.setCurrentPageIndex(0);
        paginate.setMaxPageIndicatorCount(10);
        paginate.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {
                    commandPagination = orderService.findAll((int) newValue, resiCombobox.getValue());
                    observableList.clear();
                    paginate.setCurrentPageIndex((int) newValue);
                    observableList.addAll(commandPagination.getRessource().resource);
                    tableCmd_tableView.getSelectionModel().clearSelection();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    private void findOrderBetweenTwoDate() throws SQLException {
        if (dateBegin.getValue() != null && dateEnd.getValue() != null) {
            observableList.clear();
            observableList.addAll(orderService.findBetwenTwoDate(dateBegin.getValue().atTime(LocalTime.now()), dateEnd.getValue().atTime(LocalTime.now())));
            tableCmd_tableView.getSelectionModel().clearSelection();
            paginate.setPageCount(1);
            paginate.setCurrentPageIndex(0);
        }
    }


}
