/*
 *
 */
package org.ckCoder.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class which has a lot of useful methods.
 *
 * @author GOXR3PLUS
 */
public final class ActionTool {

    /**
     * The logger for this class
     */
    private static final Logger logger = Logger.getLogger(ActionTool.class.getName());

    private static Properties properties;

    static {
        try {
            properties = SelectedLanguage.getInstace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tries to open that URI on the default browser
     *
     * @param uri
     * @return <b>True</b> if succeeded , <b>False</b> if not
     */
    public static boolean openWebSite(String uri) {

        try {
            //Check if Desktop is supported
            if (!Desktop.isDesktopSupported()) {
                ActionTool.showNotification(properties.getProperty("PROBLEM_OCCURED"), properties.getProperty("PROBLEM_MSG") + ":\n[" + uri + " ]", Duration.millis(2500), NotificationType2.INFORMATION);
                return false;
            }

            ActionTool.showNotification(properties.getProperty("OPENING_WEB"), properties.getProperty("OPENING_WEB_MSG") + ":\n" + uri, Duration.millis(1500), NotificationType2.INFORMATION);
            Desktop.getDesktop().browse(new URI(uri));
        } catch (IOException | URISyntaxException ex) {
            ActionTool.showNotification(properties.getProperty("PROBLEM_OCCURED"), properties.getProperty("PROBLEM_MSG") + ":\n[" + uri + " ]", Duration.millis(2500), NotificationType2.INFORMATION);
            logger.log(Level.INFO, "", ex);
            return false;
        }
        return true;
    }

    /**
     * Private Constructor.
     */
    private ActionTool() throws IOException {
    }

    /**
     * Copy a file from source to destination.
     *
     * @param source      the source
     * @param destination the destination
     * @return True if succeeded , False if not
     */
    public static boolean copy(InputStream source, String destination) {
        boolean succeess = true;

        System.out.println("Copying ->" + source + "\n\tto ->" + destination);

        try {
            Files.copy(source, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "", ex);
            succeess = false;
        }

        return succeess;
    }

    /**
     * Show a notification.
     *
     * @param title The notification title
     * @param text  The notification text
     * @param d     The duration that notification will be visible
     * @param t     The notification type
     */
    public static void showNotification(String title, String text, Duration d, NotificationType2 t) {

        //Check if it is JavaFX Application Thread
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> showNotification(title, text, d, t));
            return;
        }

        Notifications notification1 = Notifications.create().title(title).text(text);
        notification1.hideAfter(d);

        switch (t) {
            case CONFIRM:
                notification1.showConfirm();
                break;
            case ERROR:
                notification1.showError();
                break;
            case INFORMATION:
                notification1.showInformation();
                break;
            case SIMPLE:
                notification1.show();
                break;
            case WARNING:
                notification1.showWarning();
                break;
            default:
                break;
        }

    }

    /**
     * Makes a question to the user.
     *
     * @param text the text
     * @return true, if successful
     */
    public static boolean doQuestion(String text, Stage window) {
        boolean[] questionAnswer = {false};

        // Show Alert
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.initOwner(window);
        alert.setHeaderText("Question");
        alert.setContentText(text);
        alert.showAndWait().ifPresent(answer -> questionAnswer[0] = (answer == ButtonType.OK));

        return questionAnswer[0];
    }

}
