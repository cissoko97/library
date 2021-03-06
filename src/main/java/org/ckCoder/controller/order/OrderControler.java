package org.ckCoder.controller.order;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.ckCoder.models.Command;
import org.ckCoder.service.OrderService;
import org.ckCoder.utils.SelectedLanguage;
import org.ckCoder.utils.UtilForArray;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Properties;
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
    @FXML
    public Text titleView_label;
    @FXML
    public Label begin_label;
    @FXML
    public Label end_label;
    @FXML
    public Text size_label;

    private ObservableList<Command> observableList;
    private org.ckCoder.utils.pagination.Pagination<Command> commandPagination;
    private final OrderService orderService = new OrderService();
    private Properties properties = SelectedLanguage.getInstace();

    public OrderControler() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initPage();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        titleView_label.setText(properties.getProperty("TITLE_LABEL_ORDERPAGE").toUpperCase());
        begin_label.setText(properties.getProperty("BEGINDATE_LABEL_ORDERPAGE"));
        end_label.setText(properties.getProperty("ENDDATE_LABEL_ORDERPAGE"));
        size_label.setText(properties.getProperty("SIZE_LABEL_ORDERPAGE"));
        searchTextField.setPromptText(properties.getProperty("SEARCH_PLACEHOLDER_ORDERPAGE"));
        submitDateBtn.setText(properties.getProperty("SEARCH_BTN_ORDERPAGE"));
        submitDateBtn.setOnAction(event -> {
            try {
                findOrderBetweenTwoDate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        reload_table_btn.setText(properties.getProperty("RELOAD_BTN_ORDERPAGE"));
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
        resiCombobox.setValue(25);

        TableColumn<Command, Double> priceCol = new TableColumn<>();
        priceCol.setText(properties.getProperty("TOTAL_COLTABLE_ORDERPAGE"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        TableColumn<Command, Double> acceptedCol = new TableColumn<>();
        acceptedCol.setText(properties.getProperty("STATUT_COLTABLE_ORDERPAGE"));
        acceptedCol.setCellValueFactory(new PropertyValueFactory<>("accepted"));

        TableColumn<Command, String> nameCol = new TableColumn<>();
        nameCol.setText(properties.getProperty("NAME_COLTABLE_ORDERPAGE"));
        nameCol.setCellValueFactory(item -> new ReadOnlyObjectWrapper<>(item.getValue().getUser().getPerson().getName()));

        TableColumn<Command, Double> created_at = new TableColumn<>();
        created_at.setText(properties.getProperty("DATECREATION_COLTABLE_ORDERPAGE"));
        created_at.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        TableColumn<Command, Double> lastupdate = new TableColumn<>();
        lastupdate.setText(properties.getProperty("LASTUP_COLTABLE_ORDERPAGE"));
        lastupdate.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));

        TableColumn<Command, String> surnameCol = new TableColumn<>();
        surnameCol.setText(properties.getProperty("SURNAME_COLTABLE_ORDERPAGE"));
        surnameCol.setCellValueFactory(item -> new ReadOnlyObjectWrapper<>(item.getValue().getUser().getPerson().getSurname()));


        TableColumn<Command, Void> btnCol2 = new TableColumn<>("");
        UtilForArray.setwidthBtn(btnCol2);

        Callback<TableColumn<Command, Void>, TableCell<Command, Void>> cellData2 = new Callback<TableColumn<Command, Void>, TableCell<Command, Void>>() {
            @Override
            public TableCell<Command, Void> call(TableColumn<Command, Void> param) {
                final TableCell<Command, Void> cell = new TableCell<Command, Void>(){
                    final Button btn = new Button(properties.getProperty("VIEW_COL_BTN"));
                    {
                        btn.getStyleClass().add("round-red");
                        btn.setCursor(Cursor.HAND);
                        btn.setOnAction(e->{
                            try {
                                opendetailOrderTemplate(e, getTableView().getItems().get(getIndex()));
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
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

        resiCombobox.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                try {
                    commandPagination = orderService.findAll(paginate.getCurrentPageIndex(), resiCombobox.getValue());
                    //observableList.clear();
                    observableList.setAll(commandPagination.getRessource().resource);
                    tableCmd_tableView.getSelectionModel().clearSelection();
                    paginate.setPageCount(commandPagination.getRessource().info.totalPage);
                    paginate.setCurrentPageIndex(0);
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


    private void opendetailOrderTemplate(ActionEvent e, Command cmd) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/order/order_view_detail.fxml"));
        OrderViewDetail orderViewDetail = new OrderViewDetail(cmd);
        loader.setController(orderViewDetail);

        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.initOwner(((Control) e.getSource()).getScene().getWindow());

        stage.setScene(scene);
        stage.showAndWait();
        stage.setTitle(properties.getProperty("ORDERDETAIL_TITLE_WINDOW"));
        if(orderViewDetail.getCommand().getAccepted() != cmd.getAccepted())
            observableList.set(observableList.indexOf(cmd), orderViewDetail.getCommand());
    }


}
