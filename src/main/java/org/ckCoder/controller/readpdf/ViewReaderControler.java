package org.ckCoder.controller.readpdf;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.commons.io.FileUtils;
import org.ckCoder.models.Book;
import org.ckCoder.models.Critique;
import org.ckCoder.models.User;

import java.io.File;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class ViewReaderControler implements Initializable {

    @FXML
    private Button commentBtn;
    @FXML
    private WebView web;
    WebEngine engine;
    @FXML
    public Button saveBtn;

    private byte[] booksBytes = new byte[0];
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        engine = web.getEngine();
        String url = getClass().getResource("/js/web/viewer.html").toExternalForm();

        //connect to file css to customize pdf.js appearence
        engine.setUserStyleSheetLocation(getClass().getResource("/css/web.css").toExternalForm());

        engine.setJavaScriptEnabled(true);

        engine.load(url);

        commentBtn.setOnAction(event ->{
            try {
                byte[] data = FileUtils.readFileToByteArray(new File("/home/lerenard/Téléchargements/10601-apprenez-a-programmer-en-java.pdf"));
                String base64 = Base64.getEncoder().encodeToString(data);
                engine.executeScript("openFileFromBase64('" + base64 + "')");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        saveBtn.setOnAction(event -> {

        });


        /*String base64 = Base64.getEncoder().encodeToString(data);
        // call JS fuction from java code
        engine.executeScript("openFileFromBase64('" + base64 + "')");*/




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
                        String base64 = Base64.getEncoder().encodeToString(booksBytes);
                        // call JS fuction from java code
                        engine.executeScript("openFileFromBase64('" + base64 + "')");
                    }
                });


    }

    @FXML
    private boolean saveFile(User user) {

        return false;
    }

    @FXML
    private Critique comment(Book book, User user) {

        return null;
    }



    public void setBooksBytes(byte[] booksBytes) {
        this.booksBytes = booksBytes;
    }
}
