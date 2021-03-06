package org.ckCoder.controller.book.readpdf;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.ckCoder.controller.book.CritiqueController;
import org.ckCoder.models.Book;
import org.ckCoder.models.Critique;
import org.ckCoder.models.User;
import org.ckCoder.service.BookService;
import org.ckCoder.utils.SelectedLanguage;
import org.ckCoder.utils.SessionManager;
import org.ckCoder.utils.Verification;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Properties;
import java.util.ResourceBundle;

public class ViewReaderControler implements Initializable {

    @FXML
    private Button commentBtn;
    @FXML
    private WebView web;
    WebEngine engine;
    @FXML
    public Button saveBtn;

    @FXML
    private Button whshList_btn;
    @FXML
    private HBox panel_btn_hbox;

    private Book book;
    private boolean isGood;
    private final SessionManager manager = SessionManager.getInstance();
    private final BookService bookService = new BookService();
    Logger logger = Logger.getLogger(ViewReaderControler.class);
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        panel_btn_hbox.getChildren().remove(whshList_btn);
        engine = web.getEngine();
        String url = getClass().getResource("/js/web/viewer.html").toExternalForm();

        //connect to file css to customize pdf.js appearence
        engine.setUserStyleSheetLocation(getClass().getResource("/css/web.css").toExternalForm());

        engine.setJavaScriptEnabled(true);

        engine.load(url);

        commentBtn.setOnAction(event ->{
            try {
                openCritiqueDialog(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        saveBtn.setOnAction(event -> {
            manager.getBookSet().add(book);
            try {
                Verification.alertMessage(SelectedLanguage.getInstace().getProperty("READBOOKPAGE_ADD_CADDY_MESSAGE"), Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });



        engine.getLoadWorker()
                .stateProperty()
                .addListener((observableValue, state, t1) -> {
                   // JSObject window = (JSObject) engine.executeScript("window");
                  //  window.setMember("java", new JsLogListener());
                   // engine.executeScript("console.log = function(message){java.log(message)};");

                    if ((t1 == Worker.State.SUCCEEDED)) {
                        /*byte[] data1 = new byte[0];
                        try {
                            data1 = FileUtils.readFileToByteArray(new File("/home/lerenard/Téléchargements/10601-apprenez-a-programmer-en-java.pdf"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        String base64 = Base64.getEncoder().encodeToString(book.getBookBinary());
                        // call JS fuction from java code
                        engine.executeScript("openFileFromBase64('" + base64 + "')");
                    }
                });

        try {
            internationalisationLabel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private boolean saveFile(User user) {

        return false;
    }

    @FXML
    private Critique comment(Book book, User user) {

        return null;
    }

    private void openCritiqueDialog(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/book/critique.fxml"));
        CritiqueController critiqueController = new CritiqueController();
        loader.setController(critiqueController);

        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.initOwner(((Control) event.getSource()).getScene().getWindow());
        critiqueController.setBook(book);
        stage.setTitle(SelectedLanguage.getInstace().getProperty("CRITIQUEPAGE_TITLE_WINDOW"));
        stage.showAndWait();
        isGood = critiqueController.isGood();
    }



    public void setBook(Book book) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(book.getType().equalsIgnoreCase("privé"))
                    saveBtn.setDisable(true);
            }
        });
        this.book = book;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bookService.incrementNumberOfView(book.getId(), manager.getUser().getId());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }).start();
        logger.info("le livre " + book.getTitle() + " donc l'identifiant est : " + book.getId() + " vient d'être lu");
    }

    public boolean isGood() {
        return isGood;
    }

    private void internationalisationLabel() throws IOException {
        Properties properties = SelectedLanguage.getInstace();
        commentBtn.setText(properties.getProperty("COMMENTS_BTN_READBOOKPAGE"));
        saveBtn.setText(properties.getProperty("CADDY_BTN_READBOOKPAGE"));
    }
}
