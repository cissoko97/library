package org.ckCoder.controller.book;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.ckCoder.controller.book.readpdf.ViewReaderControler;
import org.ckCoder.controller.utils.BottonComponent;
import org.ckCoder.controller.utils.CardEntityComtroller;
import org.ckCoder.controller.utils.ControlBtn;
import org.ckCoder.models.*;
import org.ckCoder.service.BookService;
import org.ckCoder.service.CategoryService;
import org.ckCoder.service.CritiqueService;
import org.ckCoder.service.contract.IService;
import org.ckCoder.utils.SessionManager;
import org.ckCoder.utils.Verification;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

public class BookControler implements Initializable {
    /*@FXML
    public Text idBook;*/
    SessionManager manager = SessionManager.getInstance();

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
    public VBox imageBox;
    @FXML
    public Text originalNameText;

    @FXML
    public ControlBtn btn_controlController;

    @FXML
    public TextArea descriptionBook_TextFlow;
    @FXML
    public VBox panelCommentVbox;
    @FXML
    public TextFlow aboutAuthor_textFlow;
    @FXML
    public ImageView imageView;
    @FXML
    public ListView<Book> cardPaneBook_listview;

    public ListView<Critique> critique_listView;
    @FXML
    private BottonComponent bottonComponentController;

    private final ObservableList<Category> observableList = FXCollections.observableArrayList();

    private final IService<Category, Long> categoryService = new CategoryService();
    private Category category = new Category();
    private Book book = new Book();
    private Set<Book> books = new HashSet<>();
    private final BookService bookService = new BookService();
    private final CritiqueService critiqueService = new CritiqueService();

    private ObservableList<Book> observableListBook;


    private final User user = manager.getUser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boolean isAdmin = false;
        for (Profil p : user.getProfils()) {
            if (p.getLabel().toLowerCase().equals("admin")) {
                isAdmin = true;
                break;
            }
        }
        if(!isAdmin){
            btn_controlController.gridPaneRoot.getChildren()
                    .removeAll(btn_controlController.getAdd_btn(), btn_controlController.getUpdate_btn3(),
                            btn_controlController.getLockUser_btn());
        }

        btn_controlController.getLoad_btn().setDisable(true);
        btn_controlController.gridPaneRoot.getChildren().remove(btn_controlController.getDelete_btn());
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


        btn_controlController.getAdd_btn().setOnAction(event ->
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

        btn_controlController.getLoad_btn().setOnAction(event -> {
            try {
                openPDF(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btn_controlController.getAddCaddyBtn().setOnAction(event -> {
            this.addBooktoCart(this.book);
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
        btn_controlController.getAddCaddyBtn().setText("Dans le panier");


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

        // addDeleteButtonToTable();
        addUpDateButtonToTable();
        addSeeBookForCategoryButtonToTable();
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


    /*
     * cette partie a été commenté car on ne supprime pas une catégorie déja créé
     */
    private void addDeleteButtonToTable() {
        TableColumn<Category, Void> colBtn = new TableColumn<>("");
        setwidthBtn(colBtn);
        Callback<TableColumn<Category, Void>, TableCell<Category, Void>> cellDetele = new Callback<TableColumn<Category, Void>, TableCell<Category, Void>>() {

            @Override
            public TableCell<Category, Void> call(TableColumn<Category, Void> param) {
                final TableCell<Category, Void> cell = new TableCell<Category, Void>() {
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

        Callback<TableColumn<Category, Void>, TableCell<Category, Void>> cellDetele = new Callback<TableColumn<Category, Void>, TableCell<Category, Void>>() {

            @Override
            public TableCell<Category, Void> call(TableColumn<Category, Void> param) {
                final TableCell<Category, Void> cell = new TableCell<Category, Void>() {
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

    private void addSeeBookForCategoryButtonToTable() {
        TableColumn<Category, Void> colBtn = new TableColumn<>("");
        setwidthBtn(colBtn);

        Callback<TableColumn<Category, Void>, TableCell<Category, Void>> cellGet = new Callback<TableColumn<Category, Void>, TableCell<Category, Void>>() {

            @Override
            public TableCell<Category, Void> call(TableColumn<Category, Void> param) {
                final TableCell<Category, Void> cell = new TableCell<Category, Void>() {
                    final Button btn = new Button("see");

                    {
                        btn.getStyleClass().add("round-red");
                        btn.setOnAction(event -> {
                            CategoryAndBookController categoryAndBookController = new CategoryAndBookController();
                            category = getTableView().getItems().get(getIndex());

                            //loading of category in controller
                            categoryAndBookController.addButtonToBook(category);

                            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/view/book/catalogue_book.fxml"));
                            loader.setController(categoryAndBookController);
                            Stage stage = new Stage();
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.initStyle(StageStyle.UTILITY);

                            bottonComponentController.titleTextFied.setText(category.getFlag());
                            bottonComponentController.descriptionTextArray.setText(category.getDescription());

                            stage.setTitle("Category-" + category.getFlag());


                            try {
                                Scene scene = new Scene(loader.load());
                                scene.getStylesheets().addAll("/css/stylesheet.css", "/css/buttonStyle.css");
                                stage.setScene(scene);
                                stage.showAndWait();

                                if (categoryAndBookController.getCurrentBook() != null) {
                                    book = categoryAndBookController.getCurrentBook();
                                    loadBook(book);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //do this for get current stage


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

        colBtn.setCellFactory(cellGet);

        bottonComponentController.tableView.getColumns().add(colBtn);

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
        if (saveBookControler.getIsNewBook())
            observableListBook.add(saveBookControler.getBook());
        else {
            int index = observableListBook.indexOf(saveBookControler.getBook());
            observableListBook.set(index, saveBookControler.getBook());
        }
    }

    private void openPDF(ActionEvent event) throws IOException {
        if (book.getId() == 0) {
            Verification.alertMessage("Please select one book", Alert.AlertType.ERROR);
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/web/view_reader.fxml"));
        ViewReaderControler viewReaderControler = new ViewReaderControler();
        loader.setController(viewReaderControler);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();

        stage.setWidth(width);
        stage.setHeight(height);

        stage.setScene(scene);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initOwner(((Control) event.getSource()).getScene().getWindow());

        viewReaderControler.setBook(book);

        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
                stage.hide();
            }
        });

        stage.showAndWait();


        /*
         *si une critique a été ajouté on recharge la page
         */
        if(viewReaderControler.isGood())
            getCritiqueCard();

        /*Platform.runLater(new Runnable() {
            @Override
            public void run() {

            }
        });*/

    }

    private void getAllBookCard() throws SQLException, IOException {
        books = bookService.findAll(new Book());

        observableListBook = FXCollections.observableArrayList(books);
        cardPaneBook_listview.setItems(observableListBook);
        cardPaneBook_listview.setCellFactory(book -> new CardEntityComtroller());

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

    }

    private void getCritiqueCard() {
        Set<Critique> critiques = critiqueService.findAllCritiqueByIdBook(book.getId());
        ObservableList<Critique> critiqueObservableList = FXCollections.observableArrayList(critiques);
        critique_listView.setItems(critiqueObservableList);
        critique_listView.setCellFactory(critique-> new CardCritiqueController());

        /*critique_listView.setOnMouseClicked(event -> {
            if (critique_listView.getSelectionModel().getSelectedItem() != null) {
                try {
                    this.book = bookService.findAllBookAndtherElement(cardPaneBook_listview.getSelectionModel().getSelectedItem().getId());
                    loadBook(this.book);

                } catch (SQLException | IOException throwables) {
                    throwables.printStackTrace();
                }
            }
        });*/
    }

    private void loadBook(Book book) {
        //idBook.setText(book.getId() +"");
        titleBook.setText(book.getTitle());
        availabilityBook.setText(book.getAvailability() + "");
        editionYearBook.setText(book.getEditionYear() + "");
        nominalValueBook.setText(book.getValeurNominal() + "");
        critiqueValueBook.setText(book.getValeurCritique() + "");
        pricebook.setText(book.getPrice() + "");
        typeBook.setText(book.getType());
        creatrionDate.setText(book.getCreatedAt().format(DateTimeFormatter.ISO_DATE));
        updateDate.setText(book.getUpdatedAt().format(DateTimeFormatter.ISO_DATE));

        descriptionBook_TextFlow.getStyleClass().clear();
        descriptionBook_TextFlow.setText(book.getDescription());
        descriptionBook_TextFlow.setStyle("-fx-border-width: 0");
        descriptionBook_TextFlow.setStyle("-fx-border-color:white");

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
        if (lengthAuthor == 0)
            line1 = new Text("this book has " + lengthAuthor
                    + " Author \n");
        else
            line1 = new Text("this book has " + lengthAuthor
                    + " Authors \n");

        aboutAuthor_textFlow.getChildren().add(line1);
        book.getAuthors().forEach(aut -> {
            Text line2 = new Text("name author : " + aut.getPerson().getName() + aut.getPerson().getSurname());
            Text line3 = new Text(aut.getBibliography());

            aboutAuthor_textFlow.getChildren().addAll(line2, line3);
        });

        // on charge les critiques sur le livre

        getCritiqueCard();
        btn_controlController.getLoad_btn().setDisable(false);
    }

    private void setwidthBtn(TableColumn colBtn) {
        colBtn.setMaxWidth(68);
        colBtn.setMinWidth(68);
    }

    private void addBooktoCart(Book book) {
        manager.getBookSet().add(book);

        System.out.println(manager.getBookSet());
    }
}
