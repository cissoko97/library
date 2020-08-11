package org.ckCoder.controller.book;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.ckCoder.models.Author;
import org.ckCoder.models.Book;
import org.ckCoder.models.Category;
import org.ckCoder.service.AuthorService;
import org.ckCoder.service.BookService;
import org.ckCoder.service.CategoryService;
import org.ckCoder.service.contract.IService;
import org.ckCoder.utils.NotificationType;
import org.ckCoder.utils.NotificationUtil;
import org.ckCoder.utils.SelectedLanguage;
import org.ckCoder.utils.Verification;

import javax.management.Notification;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class SaveBookControler implements Initializable {
    @FXML
    private Text tltle_view_bookPage;
    @FXML
    public Button resetBtn;
    @FXML
    public Button saveBtn;
    @FXML
    private Label authorLabel;
    @FXML
    private MenuButton authorField;
    @FXML
    private GridPane formContener;
    @FXML
    private ComboBox<Integer> valeurNominal_textField;
    @FXML
    private ComboBox<String> type_textField;
    @FXML
    private TextField price_textField;
    @FXML
    private TextField title_textField;
    @FXML
    private Label title_label;
    @FXML
    private Label anneeEdition_label;
    @FXML
    private Label valeurNominal_label;
    @FXML
    private Label type_label;
    @FXML
    private Label price_label;
    @FXML
    private Label fileName_label;
    @FXML
    private Label imgName_label;
    @FXML
    private Label description_label;
    @FXML
    private TextArea descriptiopn_text_array;
    @FXML
    private TextField anneeEditionTextField;
    @FXML
    private Button fileName_btn;
    @FXML
    private Button uploadImgName_btn;
    @FXML
    private Label category_label;
    @FXML
    private ComboBox<String> category_textField;


    private  File selectedFile;
    private File selectedImg;
    private Book book = new Book();
    private final Set<Long> idAuthor = new HashSet<>();
    private final IService<Category, Long> categoryLongIService = new CategoryService();
    private final IService<Author, Long> authorService = new AuthorService();
    private final BookService bookService = new BookService();
    private boolean isNewBook = true;

    private Properties properties = SelectedLanguage.getInstace();

    public SaveBookControler() throws IOException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initForm();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        type_textField.getItems().addAll("public", "privé");
        type_textField.setValue("public");

        type_textField.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals("public")) {
                    price_textField.setDisable(true);
                } else
                    price_textField.setDisable(false);
            }
        });

        valeurNominal_textField.getItems().addAll(10,25,50,75,100);
        valeurNominal_textField.setValue(10);
        internalisationNameOfField();
    }

    @FXML
    public void onUpLoadFieldName(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file nbook");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Text Files", "*.pdf"
        ));
         selectedFile = fileChooser.showOpenDialog(((Control) actionEvent.getSource()).getScene().getWindow());
         if(selectedFile != null)
            fileName_btn.setText(selectedFile.getName());
    }

    @FXML
    public void onUploadImgName(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image book");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Imge Files", "*.png", "*.jpg", "*.jpeg"
        ));
        selectedImg = fileChooser.showOpenDialog(((Control) actionEvent.getSource()).getScene().getWindow());
        if(selectedFile != null)
            uploadImgName_btn.setText(selectedImg.getName());
    }

    @FXML
    public void onSave(ActionEvent actionEvent) throws IOException, SQLException {
        if (book.getId() > 0) {
            isNewBook = false;
        }
        List<String> errorList = new ArrayList<>();
        formContener.getChildren().forEach(Verification::remouveDangerClass);

        if(!price_textField.isDisable() && !Verification.numeric(price_textField.getText())){
            Verification.dangerField(price_textField);
            errorList.add(properties.getProperty("FORM_BOOK_PRICE"));
        }

        if (book.getId() == 0) {
            if(authorField.getItems().size() == 0){
                Verification.dangerField(authorField);
                errorList.add(properties.getProperty("FORM_BOOK_AUTHOR"));
            }
        }

        if(category_textField.getValue() == null){
           Verification.dangerField(category_textField);
           errorList.add(properties.getProperty("FORM_BOOK_CATEGORY"));
        }

        if(title_textField.getText().equals("")){
            Verification.dangerField(title_textField);
            errorList.add(properties.getProperty("FORM_BBOK_TITLE"));
        }

        if(anneeEditionTextField.getText().equals("")){
            Verification.dangerField(anneeEditionTextField);
        } else if (!Verification.numeric(anneeEditionTextField.getText()) ||
                    anneeEditionTextField.getText().length() != 4) {
                errorList.add(properties.getProperty("FORM_BOOK_ANNEE_EDITION"));
            Verification.dangerField(anneeEditionTextField);
        }

        if (book.getId() == 0) {
            if(selectedFile == null){
                Verification.dangerField(fileName_btn);
                errorList.add(properties.getProperty("FROM_BOOK_FILE"));
            }
            if(selectedImg == null){
                Verification.dangerField(uploadImgName_btn);
                errorList.add(properties.getProperty("FORM_BOOK_IMG"));
            }
        }

        if(descriptiopn_text_array.getText() == null || descriptiopn_text_array.getText().equals("")){
            Verification.dangerField(descriptiopn_text_array);
            errorList.add(properties.getProperty("FORM_BOOK_DESCRIPTION"));
        }

        if (errorList.size() > 0) {
            Verification.alertMessage(errorList, Alert.AlertType.ERROR);
        } else {
            book.setCategory(new Category(convertToStringValue(category_textField.getValue())));
            // book.set
            book.setTitle(title_textField.getText());
            book.setEditionYear(Integer.parseInt(anneeEditionTextField.getText()));
            book.setValeurNominal(valeurNominal_textField.getValue());

            book.setType(type_textField.getValue());


            //On modifie la valeur du prix du livre que si le livre est privé

            if(!price_textField.isDisable())
                book.setPrice((double) Integer.parseInt(price_textField.getText()));
            book.setBookfile(selectedFile);
            book.setImgfile(selectedImg);
            book.setDescription(descriptiopn_text_array.getText());
            //if(price)

            book = bookService.create(book);

            if(idAuthor.size() > 0)
                bookService.createAndAffectAuthor_Categorie_AtBook(book.getId(), idAuthor);

            NotificationUtil.showNotiication(String.valueOf(NotificationType.SUCCES),
                    properties.getProperty("MESSAGE_SAVE_BOOK_TITLE"),
                    properties.getProperty("MESSAGE_SAVE_BBOK_CONTENT"));
        }

    }

    private void initForm() throws SQLException {
        book = new Book();
        price_textField.setDisable(true);
        Set<Category> categories = categoryLongIService.findAll(new Category());
        Set<Author> authors = authorService.findAll(new Author());

        categories.forEach(c-> category_textField.getItems().add(c.getId()+"_ " + c.getDescription()));

        saveBtn.setText("save");
        saveBtn.getStyleClass().add("round-blue");

        resetBtn.setText("reset");
        resetBtn.getStyleClass().add("round-blue");

        resetBtn.setOnAction(event -> {
            resetForm();
            book = new Book();
        });

        category_textField.valueProperty().addListener((observable, oldValue, newValue) ->{
            if(!newValue.equals(""))
                this.book.setCategory(new Category(convertToStringValue(newValue)));
        });


        List<MenuItem> menuItems = new ArrayList<>();
        for (Author author : authors) {
            menuItems.add(new CheckMenuItem(author.getId()+"_ "+author.getPerson().getName() + " " + author.getPerson().getSurname()));
        }

        authorField.getItems().clear();
        authorField.getItems().addAll(menuItems);

        for (MenuItem item : menuItems) {
            item.setOnAction(event -> {
                if (!idAuthor.contains(convertToStringValue(item.getText())))
                    idAuthor.add(convertToStringValue(item.getText()));
                else
                    idAuthor.remove(convertToStringValue(item.getText()));

                System.out.println(idAuthor);
            });

        }

    }


    private long convertToStringValue(String value) {
        return Long.parseLong(value.split("_")[0]);
    }


    private void resetForm() {
        idAuthor.clear();
        category_textField.setValue("");
        title_textField.setText("");
        anneeEditionTextField.setText("");
        valeurNominal_textField.setValue(10);
        price_textField.setText("");
        descriptiopn_text_array.setText("");
        selectedImg = null;
        selectedFile = null;
    }

    public void setBook(Book b) {
        this.book = b;
        if (this.book.getId() > 0) {
            category_textField.setValue(book.getCategory().getId()+"_"+book.getCategory().getFlag());
            title_textField.setText(book.getTitle());
            anneeEditionTextField.setText(book.getEditionYear()+"");
            valeurNominal_textField.setValue(book.getValeurNominal());
            type_textField.setValue(book.getType());
            if(book.getType().equals("privé")){
                price_textField.setDisable(false);
                price_textField.setText(book.getPrice() + "");
            }

        /*FileOutputStream fileBook = new FileOutputStream(book.getFileName(), true);
        fileBook.write(book.getBookBinary());
        fileBook.close();

        FileOutputStream fileBookImage = new FileOutputStream(book.getImgName(), true);
        fileBookImage.write(book.getImgBinary());
        fileBookImage.close();*/

            descriptiopn_text_array.setText(book.getDescription());
            fileName_btn.setVisible(false);
            uploadImgName_btn.setVisible(false);
            fileName_label.setVisible(false);
            imgName_label.setVisible(false);
        }

    }



    public Book getBook() {
        return book;
    }

    public boolean getIsNewBook() {
        return isNewBook;
    }

    private void internalisationNameOfField() {
        tltle_view_bookPage.setText(properties.getProperty("TITLE_VIEW_CREATEBOOKPAGE").toUpperCase());
        authorLabel.setText(properties.getProperty("AUTHOR_LABEL_CREATEBOOKPAGE"));
        category_label.setText(properties.getProperty("CATEGORY_LABEL_CREATEBOOKPAGE"));
        title_label.setText(properties.getProperty("TITLE_LABEL_CREATEDBOOKPAGE"));
        anneeEdition_label.setText(properties.getProperty("YEAR_LABEL_CREATEBOOKPAGE"));
        valeurNominal_label.setText(properties.getProperty("NOMINALVALUE_LABEL_CREATEBOOKPAGE"));
        type_label.setText(properties.getProperty("TYPE_LABEL_CREATEBOOKPAGE"));
        price_label.setText(properties.getProperty("PRICE_LABEL_CREATEBOOKPAGE"));
        fileName_label.setText(properties.getProperty("FILENAME_LABEL_CREATEBOOKPAGE"));
        fileName_btn.setText(properties.getProperty("BOOKPAGE_FILE_BTN"));
        uploadImgName_btn.setText(properties.getProperty("BOOKPAGE_IMG_BTN"));
        imgName_label.setText(properties.getProperty("IMGNAME_LABEL_CREATEBOOKPAGE"));
        description_label.setText(properties.getProperty("DESCRIPTION_LABEL_CREATEBOOKPAGE"));
        saveBtn.setText(properties.getProperty("SAVE_BTN_CREATEBOOKPAGE"));
        resetBtn.setText(properties.getProperty("RESET_BTN_CREATEBOOKPAGE"));
    }
}
