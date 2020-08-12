package org.ckCoder;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.log4j.PropertyConfigurator;
import org.ckCoder.controller.DownloadModeController;
import org.ckCoder.controller.PrimaryScene;
import org.ckCoder.service.DownloadService;
import org.ckCoder.service.ExportZipService;
import org.ckCoder.service.VersionService;
import org.ckCoder.utils.*;
import org.ckCoder.utils.ActionTool;
import org.ckCoder.utils.InfoTool;
import org.ckCoder.utils.NotificationType2;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainApp extends Application {

    // Logger logger = Logger.getLogger(getClass());
    /**
     * This is the folder where the update will take place [ obviously the
     * parent folder of the application]
     */
    private final File updateFolder = new File(InfoTool.getBasePathForClass(MainApp.class));

    private final PrimaryScene primaryScene = new PrimaryScene();
    /**
     * Download update as a ZIP Folder , this is the prefix name of the ZIP
     * folder
     */
    private static String foldersNamePrefix;

    private double update;

    public static final String APPLICATION_NAME ="book_shore";

    //Create a change listener
    ChangeListener<? super Number> listener = (observable , oldValue , newValue) -> {
        if (newValue.intValue() == 1)
            exportUpdate();
    };
    //Create a change listener
    ChangeListener<? super Number> listener2 = (observable , oldValue , newValue) -> {
        if (newValue.intValue() == 1)
            packageUpdate();
    };


    /**
     * Update to download
     */

    private static double currentVersion;
    //================Services================

    DownloadService downloadService;
    ExportZipService exportZipService;

    //=============================================

    private final VersionService versionService = new VersionService();


    private static Stage window;
    private static final DownloadModeController downloadMode = new DownloadModeController();
    private final Properties properties = SelectedLanguage.getInstace();
    public MainApp() throws IOException {

    }

    public static void main(String[] args) {
        PropertyConfigurator.configure(MainApp.class.getResource("/properties/log4j.properties"));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        //prepare window
        window = primaryStage;
        window.setResizable(false);
        window.centerOnScreen();

        // load icon
        window.getIcons().add(InfoTool.getImageFromResourcesFolder("/img/home_icone.jpeg"));
        Properties properties = SelectedLanguage.getInstace();
        update =versionService.checkVersion();

        // declare a propertie who content a current version for this application
        Properties pr = new Properties();
        InputStream stream = getClass().getResourceAsStream("/properties/config.properties");
        pr.load(stream);

        //chec current version
        System.out.println(UtilForArray.isDouble(pr.getProperty("version")));
        if (UtilForArray.isDouble(pr.getProperty("version"))) {
            currentVersion = Double.parseDouble(pr.getProperty("version"));
        } else
            currentVersion = 1.0;
        if (currentVersion < update) {
            Optional<ButtonType> optional = Verification.alertMessage(properties.getProperty("MESSAGE_DIALOG_UPDATE_APP_TITLE"),
                    properties.getProperty("MESSAGE_DIALOG_UPDATE_APP_CONTENT"), Alert.AlertType.CONFIRMATION).showAndWait();
            if(optional.get() == ButtonType.OK) {

                window.setOnCloseRequest(exit -> {
                    if(exportZipService != null && exportZipService.isRunning()) {
                        ActionTool.showNotification(properties.getProperty("MESSAGE_NOTIFICATION_EXIT_TITLE"),
                                properties.getProperty("MESSAGE_NOTIFICATION_EXIT_CONTENT"), Duration.seconds(5),
                                NotificationType2.WARNING);
                        exit.consume();
                        return;
                    }

                    if (!ActionTool.doQuestion(properties.getProperty("MESSAGE_NOTIFICATION_EXIT_CONTENT"), window)) {
                        exit.consume();
                    } else {
                        deleteZipFolder();
                        System.exit(0);
                    }
                });
            }


            // Scene
            Scene scene = new Scene(downloadMode);
            scene.getStylesheets().add(getClass().getResource(InfoTool.STYLES + InfoTool.APPLICATIONCSS).toExternalForm());
            window.setScene(scene);

            //Show
            window.show();

            //Start
            prepareForUpdate();

        } else {
            primaryScene.constructPrimaryStage(primaryStage);
        }


    }

    private void prepareForUpdate() {
        window.setTitle(primaryScene.getEtatUpdateApp());
        foldersNamePrefix = updateFolder.getAbsolutePath() + File.separator + APPLICATION_NAME + "_Update_package"
                + currentVersion;

        System.out.println("foldersNamePrefix " + foldersNamePrefix);
        downloadMode.getFlowUpdate().getChildren().add(new Text(("foldersNamePrefix " + foldersNamePrefix + "\n")));
        if (checkPermissions()) {
            downloadMode.getProgressLabel().setText(properties.getProperty("MESSAGE_ALERT_PERMISSION"));
            //downloadUpdate("https://github.com/goxr3plus/XR3Player/releases/download/V3." + APPLICATION_NAME + "/XR3Player.Update." + update + ".zip");
            downloadMode.getFlowUpdate().getChildren().add(new Text("\nDebbut du téléchargement\n"));
            downloadUpdate("https://github.com/cissoko97/library/raw/master/firstApp-1.0-SNAPSHOT.jar");

        } else {

            //Update
            downloadMode.getProgressBar().setProgress(-1);
            downloadMode.getProgressLabel().setText("Please close the updater");

            //Show Message
            ActionTool.showNotification("Permission Denied[FATAL ERROR]",
                    "Application has no permission to write inside this folder:\n [ " + updateFolder.getAbsolutePath()
                            + " ]\n -> I am working to find a solution for this error\n -> You can download " + APPLICATION_NAME + " manually :) ]",
                    Duration.minutes(1), NotificationType2.ERROR);
        }
    }

    public boolean checkPermissions() {

        //Check for permission to Create
        try {
            File sample = new File(updateFolder.getAbsolutePath() + File.separator + "empty123123124122354345436.txt");
            /*
             * Create and delete a dummy file in order to check file
             * permissions. Maybe there is a safer way for this check.
             */
            sample.createNewFile();
            sample.deleteOnExit();
        } catch (IOException e) {
            //Error message shown to user. Operation is aborted
            return false;
        }

        //Also check for Read and Write Permissions
        return updateFolder.canRead() && updateFolder.canWrite();
    }

    /** Try to download the Update */
    private void downloadUpdate(String downloadURL) {

        if (InfoTool.isReachableByPing("www.google.com")) {

            //Download it
            try {
                //Delete the ZIP Folder
                deleteZipFolder();

                //Create the downloadService
                downloadService = new DownloadService();

                //Add Bindings
                downloadMode.getProgressBar().progressProperty().bind(downloadService.progressProperty());
                downloadMode.getProgressLabel().textProperty().bind(downloadService.messageProperty());

                /*System.out.println("téléchargement " + downloadMode.getProgressLabel().getText()+ " : " +
                        downloadMode.getProgressBar().getProgress());

                downloadMode.getFlowUpdate().getChildren().add(new Text("téléchargement " + downloadMode.getProgressLabel().getText()+ " : " +
                        downloadMode.getProgressBar().getProgress()));*/

                downloadMode.getProgressLabel().textProperty().addListener((observable , oldValue , newValue) -> {

                    //Give try again option to the user
                    /*if (newValue.toLowerCase().contains("failed")){
                        downloadMode.getFailedStackPane().setVisible(true);*/
                });
                downloadMode.getProgressBar().progressProperty().addListener(listener);
                window.setTitle(properties.getProperty("MESSAGE_ETAT_DOWNLOAD_APP") + " (" + this.APPLICATION_NAME + " )"
                        + properties.getProperty("MESSAGE_ETAT_UPDATE_APP") + " -> " + currentVersion);

                //Start
               /* System.out.println("téléchargement " + downloadMode.getProgressLabel().getText()+ " : " +
                        downloadMode.getProgressBar().getProgress());*/
                downloadMode.getFlowUpdate().getChildren().add(new Text(("\n debut téléchargement " + downloadMode.getProgressLabel().getText()) + " : " +
                        downloadMode.getProgressBar().getProgress() + "\n"));
                downloadService.startDownload(new URL(downloadURL), Paths.get(foldersNamePrefix + ".zip"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        } else {
            //Update
            downloadMode.getProgressBar().setProgress(-1);
            downloadMode.getProgressLabel().setText("No internet Connection,please exit...");

            //Delete the ZIP Folder
            deleteZipFolder();

            //Give try again option to the user
//            downloadMode.getFailedStackPane().setVisible(true);
        }
    }

    public static void restartApplication(String applicationName) {

        // Restart virual library
        new Thread(() -> {
            String path = InfoTool.getBasePathForClass(MainApp.class);
            String[] applicationPath = {new File(path + applicationName + ".jar").getAbsolutePath()};

            //Show message that application is restarting
            Platform.runLater(() -> {
                try {
                    ActionTool.showNotification(SelectedLanguage.getInstace().getProperty("MESSAGE_ETAT_RESTART_APP")+
                                    " " + applicationName, SelectedLanguage.getInstace().getProperty("MESSAGE_NOTIFICATION_CONTENT_APP_PATH")
                            + applicationPath[0] + "]\n\t" + SelectedLanguage.getInstace().getProperty("MESSAGE_NOTIFIACTION_CONTENT_DURATION"),
                            Duration.seconds(25),
                            NotificationType2.INFORMATION);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            try {

                //Delete the ZIP Folder
                deleteZipFolder();

                //------------Wait until Application is created
                File applicationFile = new File(applicationPath[0]);
                while (!applicationFile.exists()) {
                    Thread.sleep(50);
                    downloadMode.getFlowUpdate().getChildren().add(new Text("Waiting " + applicationName + " Jar to be created..."));
                    downloadMode.getFlowUpdate().getChildren().add(new Text(applicationName + " Path is : " + applicationPath[0]));
                }

                //Create a process builder
                ProcessBuilder builder = new ProcessBuilder("java", "-jar", applicationPath[0]);
                builder.redirectErrorStream(true);
                Process process = builder.start();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                // Wait n seconds
                PauseTransition pause = new PauseTransition(Duration.seconds(10));
                pause.setOnFinished(f -> Platform.runLater(() -> ActionTool.showNotification("Starting " + applicationName + " failed",
                        "\nApplication Path: [ " + applicationPath[0] + " ]\n\tTry to do it manually...", Duration.seconds(10), NotificationType2.ERROR)));
                pause.play();

                // Continuously Read Output to check if the main application started
                String line;
                while (process.isAlive())
                    while ((line = bufferedReader.readLine()) != null) {
                        if (line.isEmpty())
                            break;
                           /* //This line is being printed when XR3Player Starts
                            //So the AutoUpdater knows that it must exit
                        else if (line.contains("XR3Player ready to rock!"))
                            System.exit(0);*/
                    }

            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.INFO, null, ex);

                // Show failed message
                Platform.runLater(() -> Platform.runLater(() -> ActionTool.showNotification("Starting " + applicationName + " failed",
                        "\nApplication Path: [ " + applicationPath[0] + " ]\n\tTry to do it manually...", Duration.seconds(10), NotificationType2.ERROR)));

            }
        }, "Start Application Thread").start();
    }

    private void exportUpdate() {

        //Create the ExportZipService
        exportZipService = new ExportZipService();

        //Remove Listeners
        downloadMode.getProgressBar().progressProperty().removeListener(listener);

        //Add Bindings
        downloadMode.getProgressBar().progressProperty().bind(exportZipService.progressProperty());
        downloadMode.getProgressLabel().textProperty().bind(exportZipService.messageProperty());
        downloadMode.getProgressBar().progressProperty().addListener(listener2);

        //Start it

        System.out.println("téléchargement " + downloadMode.getProgressLabel().getText()+ " : " +
                downloadMode.getProgressBar().getProgress());
        downloadMode.getFlowUpdate().getChildren().add(new Text("téléchargement " + downloadMode.getProgressLabel().getText()+ " : " +
                downloadMode.getProgressBar().getProgress()));

        exportZipService.exportZip(foldersNamePrefix + ".zip", updateFolder.getAbsolutePath());

    }

    /**
     * After the exporting has been done i must delete the old update files and
     * add the new ones
     */
    private void packageUpdate() {

        //Remove Listeners
        downloadMode.getProgressBar().progressProperty().removeListener(listener2);

        //Bindings
        downloadMode.getProgressBar().progressProperty().unbind();
        downloadMode.getProgressLabel().textProperty().unbind();

        //Packaging
        downloadMode.getProgressBar().setProgress(-1);
        downloadMode.getProgressLabel().setText(properties.getProperty("PROGRESS_LABEL_TEXT3") + " " +properties.getProperty("INDEX_TITLE_DIALOG") + "...");

        //Delete the ZIP Folder
        deleteZipFolder();
        System.out.println("redémarrage de l'application");
        downloadMode.getFlowUpdate().getChildren().add(new Text("redémarrage de l'application"));
        //Start XR3Player
        restartApplication(APPLICATION_NAME);

    }

    public static boolean deleteZipFolder() {
        return new File(foldersNamePrefix + ".zip").delete();
    }

}
