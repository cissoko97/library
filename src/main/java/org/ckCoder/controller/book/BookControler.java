package org.ckCoder.controller.book;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.ckCoder.controller.IndexController;
import org.ckCoder.controller.utils.BottonComponent;
import org.ckCoder.controller.utils.CardEntityComtroller;
import org.ckCoder.controller.utils.ControlBtn;
import org.ckCoder.models.Author;
import org.ckCoder.models.Book;
import org.ckCoder.models.Category;
import org.ckCoder.service.BookService;
import org.ckCoder.service.CategoryService;
import org.ckCoder.service.contract.IService;
import org.ckCoder.utils.UtilForArray;
import org.ckCoder.utils.Verification;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BookControler implements Initializable {
    @FXML
    public Text idBook;
    @FXML
    public Text updateDate;
    @FXML
    public Text creatrionDate;
    @FXML
    public Text typeBook;
    @FXML
    public Text pricebook;
    @FXML
    public Text critiqueValueBook;
    @FXML
    public Text nominalValueBook;
    @FXML
    public Text editionYearBook;
    @FXML
    public Text availabilityBook;
    @FXML
    public Text nbre_vue;
    @FXML
    public Text titleBook;
    @FXML
    public TextFlow descriptionField;
    @FXML
    public VBox imageBox;
    @FXML
    public Text originalNameText;

    @FXML
    public ControlBtn btn_controlController;
    public TextFlow seecriptionBook_TextFlow;
    @FXML
    public VBox panelCommentVbox;
    @FXML
    public TextFlow aboutAuthor_textFlow;
    @FXML
    public ImageView imageView;
    @FXML
    public ListView<Book> cardPaneBook_listview;
    @FXML
    private BottonComponent bottonComponentController;

    private final ObservableList<Category> observableList = FXCollections.observableArrayList();

    private final IService<Category, Long> categoryService = new CategoryService();
    private Category category = new Category();
    private Book book = new Book();
    private Set<Book> books = new HashSet<>();
    private BookService bookService = new BookService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            init();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            getAllBookCard();
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }


        btn_controlController.getAdd_btn().setOnAction(event ->      /*Stage stage = new Stage();
            Scene scene = null;

            try {
                scene = new Scene(IndexController.loadFXML("/view/partial/form_save_book"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setScene(scene);
            stage.setTitle("add book form");
            scene.getStylesheets().addAll("/css/stylesheet.css", "/css/buttonStyle.css");
            //do this for get current stage
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.initOwner(((Control) event.getSource()).getScene().getWindow());
            stage.setOnCloseRequest(event1 -> {

            });
            // or do this
            //stage.initOwner(this.btn.getScene().getWindows());
            stage.showAndWait();*/
        {
            try {
                openSaveBookForm(new Book(), event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        btn_controlController.getUpdate_btn3().setOnAction(event -> {
            try {
                openSaveBookForm(book, event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void init() throws SQLException {
        bottonComponentController.tableView.getColumns().clear();
        observableList.addAll(categoryService.findAll(new Category()));
        bottonComponentController.title_labe.setText("title");
        bottonComponentController.decription_label.setText("description");
        bottonComponentController.submitOrUpdude_btn.setText("valider");

        btn_controlController.getAdd_btn().getStyleClass().add("rich-blue");
        btn_controlController.getDelete_btn().getStyleClass().add("rich-blue");
        btn_controlController.getUpdate_btn3().getStyleClass().add("rich-blue");
        btn_controlController.getLoad_btn().getStyleClass().add("rich-blue");
        btn_controlController.getLockUser_btn().getStyleClass().add("rich-blue");
        btn_controlController.getAddFavory_btn().getStyleClass().add("rich-blue");
        btn_controlController.getAddCaddyBtn().getStyleClass().add("rich-blue");

        TableColumn<Category, String> idCatColumn = new TableColumn<>();
        idCatColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCatColumn.setText("Id");
        idCatColumn.setMaxWidth(200);
        idCatColumn.setMinWidth(50);

        TableColumn<Category, String> titleCatColumn = new TableColumn<>();
        titleCatColumn.setText("Flat");
        titleCatColumn.setCellValueFactory(new PropertyValueFactory<>("flag"));
        titleCatColumn.setMaxWidth(300);
        titleCatColumn.setMinWidth(200);

        TableColumn<Category, String> descriptionCatColumn = new TableColumn<>();
        descriptionCatColumn.setText("Description");
        descriptionCatColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        bottonComponentController.tableView.getColumns().add(idCatColumn);
        bottonComponentController.tableView.getColumns().add(titleCatColumn);
        bottonComponentController.tableView.getColumns().add(descriptionCatColumn);

        bottonComponentController.tableView.setItems(observableList);

        addDeleteButtonToTable();
        addUpDateButtonToTable();

        bottonComponentController.submitOrUpdude_btn.getStyleClass().add("dark-blue");
        bottonComponentController.submitOrUpdude_btn.setOnAction(event -> {
            if (controlForm()) {
                this.category.setFlag(bottonComponentController.titleTextFied.getText());
                this.category.setDescription(bottonComponentController.descriptionTextArray.getText());

                try {
                    if (category.getId() == 0) {
                        this.category = categoryService.create(category);
                        observableList.add(this.category);
                    } else {
                        Category cat = this.category;
                        this.category = categoryService.update(cat);
                        observableList.set(observableList.indexOf(cat), this.category);
                    }
                } catch (SQLException | IOException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        bottonComponentController.reset_btn.getStyleClass().add("dark-blue");
        bottonComponentController.reset_btn.setOnAction(event -> {
            this.category = new Category();

            bottonComponentController.titleTextFied.setText("");
            bottonComponentController.descriptionTextArray.setText("");
        });
    }

    private boolean controlForm() {
        List<String> errors = new ArrayList<>();
        bottonComponentController.formArray.getChildren().forEach(Verification::remouveDangerClass);

        if (bottonComponentController.titleTextFied.getText().equals("")) {
            Verification.dangerField(bottonComponentController.titleTextFied);
            errors.add("A field title do not be empty please get value for this field");
        }
        if (bottonComponentController.descriptionTextArray.getText().equals("")) {
            Verification.dangerField(bottonComponentController.descriptionTextArray);
            errors.add("A field description do not be empty");
        }

        if (errors.size() > 0) {
            Verification.alertMessage(errors, Alert.AlertType.ERROR);
            return false;
        } else
            return true;
    }


    private void addDeleteButtonToTable() {
        TableColumn<Category, Void> colBtn = new TableColumn<>("");
        setwidthBtn(colBtn);
        Callback<TableColumn<Category, Void>, TableCell<Category, Void>> cellDetele= new Callback<TableColumn<Category, Void>, TableCell<Category, Void>>() {

            @Override
            public TableCell<Category, Void> call(TableColumn<Category, Void> param) {
                final TableCell<Category, Void> cell = new TableCell<Category, Void>(){
                    final Button btn = new Button("supp");
                    {
                        btn.getStyleClass().add("round-red");
                        btn.setOnAction(event -> {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Supprimer etudiant");
                            alert.setContentText("êtes vous sur de vouloir supprimer cet étudiant?");
                            Optional<ButtonType> option = alert.showAndWait();

                            if (option.get() == ButtonType.OK) {
                                Category category = getTableView().getItems().get(getIndex());
                                try {
                                    categoryService.delete(category.getId());
                                    observableList.remove(category);
                                    bottonComponentController.tableView.getSelectionModel().clearSelection();
                                } catch (Exception throwables) {
                                    throwables.printStackTrace();
                                }
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

        colBtn.setCellFactory(cellDetele);

        bottonComponentController.tableView.getColumns().add(colBtn);

    }

    private void addUpDateButtonToTable() {
        TableColumn<Category, Void> colBtn = new TableColumn<>("");
        setwidthBtn(colBtn);

        Callback<TableColumn<Category, Void>, TableCell<Category, Void>> cellDetele= new Callback<TableColumn<Category, Void>, TableCell<Category, Void>>() {

            @Override
            public TableCell<Category, Void> call(TableColumn<Category, Void> param) {
                final TableCell<Category, Void> cell = new TableCell<Category, Void>(){
                    final Button btn = new Button("up");
                    {
                        btn.getStyleClass().add("round-blue");
                        btn.setOnAction(event -> {
                            category = getTableView().getItems().get(getIndex());
                            bottonComponentController.titleTextFied.setText(category.getFlag());
                            bottonComponentController.descriptionTextArray.setText(category.getDescription());

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

        colBtn.setCellFactory(cellDetele);

        bottonComponentController.tableView.getColumns().add(colBtn);

    }

    private void setwidthBtn(TableColumn colBtn) {
        colBtn.setMaxWidth(68);
        colBtn.setMinWidth(68);
    }

    private void openSaveBookForm(Book book, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/partial/form_save_book.fxml"));
        Stage stage = new Stage();
        Scene scene = null;
        SaveBookControler saveBookControler = new SaveBookControler();
        loader.setController(saveBookControler);
        try {
            // scene = new Scene(IndexController.loadFXML("/view/partial/form_save_book"));
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setTitle("add book form");
        scene.getStylesheets().addAll("/css/stylesheet.css", "/css/buttonStyle.css");
        //do this for get current stage
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.initOwner(((Control) event.getSource()).getScene().getWindow());
            /*stage.setOnCloseRequest(event1 -> {

            });*/
        // or do this
        //stage.initOwner(this.btn.getScene().getWindows());

        saveBookControler.setBook(book);
        stage.showAndWait();
    }

    private void getAllBookCard() throws SQLException, IOException {
        books = bookService.findAll(new Book());

        ObservableList<Book> observableList = FXCollections.observableArrayList(books);
        cardPaneBook_listview.setItems(observableList);
        cardPaneBook_listview.setCellFactory(book-> new CardEntityComtroller());

        cardPaneBook_listview.setOnMouseClicked(event -> {
            if (cardPaneBook_listview.getSelectionModel().getSelectedItem() != null) {
                try {
                    this.book = bookService.findAllBookAndtherElement(cardPaneBook_listview.getSelectionModel().getSelectedItem().getId());
                    loadBook(this.book);

                } catch (SQLException | IOException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        for (Book b : books) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/utils/card_entity.fxml"));
            CardEntityComtroller cardEntityComtroller = new CardEntityComtroller();
            loader.setController(cardEntityComtroller);
            HBox cardBook = loader.load();

            /*cardEntityComtroller.nameBook_text.setText(b.getTitle());
            cardEntityComtroller.noteTextField.setText((b.getValeurNominal() + b.getValeurCritique())+"");
            cardEntityComtroller.typeTextFied.setText(b.getType());
            if(b.getPrice() > 0.0)
                cardEntityComtroller.priceTextField.setText(b.getPrice()+" XAF");
            else
                cardEntityComtroller.priceTextField.setText("");

            InputStream file = new ByteArrayInputStream(b.getImgBinary());
            Image image = new Image(file);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);

            cardEntityComtroller.paneImg_vbox.getChildren().add(imageView);*/

            cardBook.setOnMouseClicked(event -> {

            });

        }
    }

    private void loadBook(Book book) {
        idBook.setText(book.getId() +"");
        titleBook.setText(book.getTitle());
        availabilityBook.setText(book.getAvailability() +"");
        editionYearBook.setText(book.getEditionYear() + "");
        nominalValueBook.setText(book.getValeurNominal()+"");
        critiqueValueBook.setText(book.getValeurCritique()+"");
        pricebook.setText(book.getPrice()+"");
        typeBook.setText(book.getType());
        creatrionDate.setText(book.getCreatedAt().format(DateTimeFormatter.ISO_DATE));
        updateDate.setText(book.getUpdatedAt().format(DateTimeFormatter.ISO_DATE));
        descriptionField.setTextAlignment(TextAlignment.JUSTIFY);
        descriptionField.setLineSpacing(4.0);

        descriptionField.getChildren().clear();
        descriptionField.getChildren().add(new Text(book.getDescription()));

        InputStream file = new ByteArrayInputStream(book.getImgBinary());
        Image image = new Image(file);
        imageView.setImage(image);

        originalNameText.setText(book.getImgName());
        /*
         *a propos de l'autheur
         */

        aboutAuthor_textFlow.getChildren().clear();
        int lengthAuthor = book.getAuthors().size();

        Text line1;
        if(lengthAuthor == 0)
            line1 = new Text("this book has " + lengthAuthor
                    + " Author \n");
        else
            line1 = new Text("this book has " + lengthAuthor
                    + " Authors \n");

        aboutAuthor_textFlow.getChildren().add(line1);
        book.getAuthors().forEach(aut-> {
            Text line2 = new Text("name author : " + aut.getPerson().getName() + aut.getPerson().getSurname());
            Text line3 = new Text(aut.getBibliography());

            aboutAuthor_textFlow.getChildren().addAll(line2, line3);
        });
    }



}
