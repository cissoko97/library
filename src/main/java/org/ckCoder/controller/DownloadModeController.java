package org.ckCoder.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextFlow;
import org.ckCoder.MainApp;
import org.ckCoder.utils.ActionTool;
import org.ckCoder.utils.InfoTool;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DownloadModeController extends BorderPane {

    //-----------------------------------------------------

    @FXML
    private Rectangle rectangle;

    @FXML
    private ProgressIndicator progressBar;

    @FXML
    private Label progressLabel;

    @FXML
    private StackPane failedStackPane;

    @FXML
    private Button tryAgainButton;

    @FXML
    private Button downloadManually;
    @FXML
    private TextFlow flowUpdate;

    // -------------------------------------------------------------

    /**
     * The logger.
     */
    private final Logger logger = Logger.getLogger(getClass().getName());

    /**
     * Constructor.
     */
    public DownloadModeController() {

        // ------------------------------------FXMLLOADER ----------------------------------------
        FXMLLoader loader = new FXMLLoader(getClass().getResource(InfoTool.FXMLS + "DownloadModeController.fxml"));
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "", ex);
        }

    }

    /**
     * Called as soon as .FXML is loaded from FXML Loader
     */
    @FXML
    private void initialize() {

        //-- failedStackPane
        failedStackPane.setVisible(false);

        //-- tryAgainButton
        tryAgainButton.setOnAction(a -> {
            MainApp.restartApplication(MainApp.APPLICATION_NAME);
            tryAgainButton.setDisable(true);
        });

        //== Download Manually
        downloadManually.setOnAction(a -> ActionTool.openWebSite("https://sourceforge.net/projects/xr3player/"));

    }

    public ProgressIndicator getProgressBar() {
        return progressBar;
    }

    public Label getProgressLabel() {
        return progressLabel;
    }

    /**
     * @return the failedStackPane
     */
    public StackPane getFailedStackPane() {
        return failedStackPane;
    }

    /**
     * @return the downloadManually
     */
    public Button getDownloadManually() {
        return downloadManually;
    }

    public TextFlow getFlowUpdate() {
        return flowUpdate;
    }
}
