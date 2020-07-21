package org.ckCoder.controller.book;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import org.ckCoder.models.Author;
import org.ckCoder.models.Book;
import org.ckCoder.models.Category;
import org.ckCoder.service.AuthorService;
import org.ckCoder.service.BookService;
import org.ckCoder.service.CategoryService;
import org.ckCoder.service.contract.IService;
import org.ckCoder.utils.Verification;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class SaveBookControler implements Initializable {
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
    }

    @FXML
    public void onUpLoadFieldName(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file nbook");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Text Files", "*.pdf"
        ));
         selectedFile = fileChooser.showOpenDialog(((Control) actionEvent.getSource()).getScene().getWindow());
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
            errorList.add("the price value must be a number");
        }

        if (book.getId() == 0) {
            if(authorField.getItems().size() == 0){
                Verification.dangerField(authorField);
                errorList.add("please select at last one author");
            }
        }

        if(category_textField.getValue() == null){
           Verification.dangerField(category_textField);
           errorList.add("please select a catégory");
        }

        if(title_textField.getText().equals("")){
            Verification.dangerField(title_textField);
            errorList.add("the title field cannot be empty");
        }

        if(anneeEditionTextField.getText().equals("")){
            Verification.dangerField(anneeEditionTextField);
        } else if (!Verification.numeric(anneeEditionTextField.getText()) ||
                    anneeEditionTextField.getText().length() != 4) {
                errorList.add("the value of field year is not correctly. " +
                        "Please enter one numeric value and length of value is 4 characters");
            Verification.dangerField(anneeEditionTextField);
        }

        /*if(valeurNominal_textField.getText().equals("")){
            Verification.dangerField(valeurNominal_textField);
            errorList.add("what is à nominal value of book?");
        } else if (!Verification.numeric(valeurNominal_textField.getText())) {
            errorList.add("the nominal value must be a number");
            Verification.dangerField(valeurNominal_textField);
        }*/
        if(type_textField.getValue() == null){
            Verification.dangerField(type_textField);
        }

        if (book.getId() == 0) {
            if(selectedFile == null){
                Verification.dangerField(fileName_btn);
                errorList.add("please select file book");
            }
            if(selectedImg == null){
                Verification.dangerField(uploadImgName_btn);
                errorList.add("pleace select image book");
            }
        }

        if(descriptiopn_text_array.getText() == null || descriptiopn_text_array.getText().equals("")){
            Verification.dangerField(descriptiopn_text_array);
            errorList.add("pleace put a description for this book");
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

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("register book status");
            alert.setContentText("this operation is succes");
            alert.show();
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
}
