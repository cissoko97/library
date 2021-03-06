package org.ckCoder.controller.book;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.ckCoder.models.Book;
import org.ckCoder.models.Category;
import org.ckCoder.service.BookService;
import org.ckCoder.utils.SelectedLanguage;
import org.ckCoder.utils.UtilForArray;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

public class CategoryAndBookController implements Initializable {

    @FXML
    private TableView<Book> bookTableView;
    @FXML
    private Text categorieName_text;

    private Category currentCategory;

    private Book currentBook;

    private Properties properties = SelectedLanguage.getInstace();
    ObservableList<Book> observableList = FXCollections.observableArrayList();

    private final BookService bookService = new BookService();

    public CategoryAndBookController() throws IOException {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        intTable();
        bookTableView.setItems(observableList);
    }


    public void addButtonToBook(Category category){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                try {
                    Set<Book> bookSet = bookService.findBookByCategory(category.getId());
                    observableList.addAll(bookSet);
                    categorieName_text.setText(category.getFlag());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        });
    }

    private void intTable() {
        bookTableView.getColumns().clear();
        TableColumn<Book, Long> idBookCol = new TableColumn<>();
        idBookCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idBookCol.setText(properties.getProperty("ID_BOOK_COL_TABLEVIEW"));
        idBookCol.setMaxWidth(200);
        idBookCol.setMinWidth(50);

        TableColumn<Book, String> titleCol = new TableColumn<>();
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setText(properties.getProperty("TITLE_BOOK_COL_TABLEVIEW"));

        TableColumn<Book, Double> priceCol = new TableColumn<>();
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setText(properties.getProperty("TYPE_BOOK_COL_TABLEVIEW"));

        TableColumn<Book, String> typeCol = new TableColumn<>();
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setText(properties.getProperty("PRICE_BOOK_COL_TABLEVIEW"));

        TableColumn<Book, Integer> nbreVueCol = new TableColumn<>();
        nbreVueCol.setCellValueFactory(new PropertyValueFactory<>("nbVue"));
        nbreVueCol.setText(properties.getProperty("LIKE_BOOK_COL_TABLEVIEW"));

        TableColumn<Book, String> editionYearCol = new TableColumn<>();
        editionYearCol.setCellValueFactory(new PropertyValueFactory<>("editionYear"));
        editionYearCol.setText(properties.getProperty("YEAR_BOOK_COL_TABLEVIEW"));

        TableColumn<Book, String> availabilityCol = new TableColumn<>();
        availabilityCol.setCellValueFactory(new PropertyValueFactory<>("availability"));
        availabilityCol.setText(properties.getProperty("AVAILABILITY_BOOK_COL_TABLEVIEW"));


        TableColumn<Book, Void> btnCol = new TableColumn<>("");
        UtilForArray.setwidthBtn(btnCol);

        Callback<TableColumn<Book, Void>, TableCell<Book, Void>> cellData = new Callback<TableColumn<Book, Void>, TableCell<Book, Void>>() {
            @Override
            public TableCell<Book, Void> call(TableColumn<Book, Void> param) {
                final TableCell<Book, Void> cell = new TableCell<Book, Void>(){
                    final Button btn = new Button("View");
                    {
                        btn.getStyleClass().add("round-red");
                        btn.setOnAction(e->{
                            currentBook = getTableView().getItems().get(getIndex());
                            this.btn.getScene().getWindow().hide();
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

        btnCol.setCellFactory(cellData);

        bookTableView.getColumns().addAll(idBookCol, titleCol,  typeCol, priceCol, availabilityCol, editionYearCol,
                nbreVueCol, btnCol);
    }

    public Book getCurrentBook() {
        return currentBook;
    }

}
