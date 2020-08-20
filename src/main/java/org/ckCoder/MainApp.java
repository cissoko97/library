package org.ckCoder;

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
import org.ckCoder.models.VersionApp;
import org.ckCoder.service.DownloadFromLocalServer;
import org.ckCoder.service.ExportZipService;
import org.ckCoder.service.VersionService;
import org.ckCoder.utils.*;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;

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
    private static String prefixNewJarFile;

    private VersionApp versionApp;

    public static final String APPLICATION_NAME = "firstApp-";
    private static final String PREFIXE_REMOTE = "smb://";


    //Create a change listener
    ChangeListener<? super Number> listener = (observable, oldValue, newValue) -> {
        if (newValue.intValue() == 1) {
          //  exportUpdate();
        }
    };
    //Create a change listener
    ChangeListener<? super Number> listener2 = (observable, oldValue, newValue) -> {
        if (newValue.intValue() == 1) {

           // packageUpdate();
        }
    };


    /**
     * Update to download
     */

    private static double currentVersion;
    //================Services================

    //DownloadService downloadService;
    ExportZipService exportZipService;
    DownloadFromLocalServer downloadService;
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
        window.centerOnScreen();

        // load icon
        window.getIcons().add(InfoTool.getImageFromResourcesFolder("/img/home_icone.jpeg"));
        Properties properties = SelectedLanguage.getInstace();

        // check last version in the data base
        versionApp = versionService.checkVersion();

        // declare a propertie who content a current version for this application
        Properties pr = new Properties();
        InputStream stream = getClass().getResourceAsStream("/properties/config.properties");
        pr.load(stream);

        //check current version

        if (UtilForArray.isDouble(pr.getProperty("version"))) {
            currentVersion = Double.parseDouble(pr.getProperty("version"));
        } else {
            currentVersion = 1.0;
        }

        /*
         * compare current version witch last version
         * if last version is more than current version,
         * propose at user to update the application
         */
        if (currentVersion < versionApp.getNumVerson()) {
            Alert alert = Verification.alertMessage(properties.getProperty("MESSAGE_DIALOG_UPDATE_APP_TITLE"),
                    properties.getProperty("MESSAGE_DIALOG_UPDATE_APP_CONTENT"), Alert.AlertType.INFORMATION);

            alert.showAndWait();


            if (alert.getResult() == ButtonType.OK) {
               /* // Scene
                Scene scene = new Scene(downloadMode);
                scene.getStylesheets().add(getClass().getResource(InfoTool.STYLES + InfoTool.APPLICATIONCSS).toExternalForm());
                window.setScene(scene);

                //reseize window
                window.setResizable(false);
                //Show
                window.show();

                //Start
                // prepareForUpdate();*/




                openUpdateApp();
            } else {

                System.exit(0);
                //primaryScene.constructPrimaryStage(primaryStage);
            }

        } else {
            // window.setResizable(true);
            primaryScene.constructPrimaryStage(window);
        }


    }


    private void openUpdateApp() {
        System.out.println(InfoTool.getBasePathForClass(MainApp.class));


        String pathOfUpdateApp = InfoTool.getBasePathForClass(MainApp.class) + "updateapp" + File.separator;
        String updateApp = pathOfUpdateApp + "auto-update.jar";


        new Thread(()-> {
            //create a processing builder
            System.out.println("base path : " + updateFolder.getAbsolutePath());
            System.out.println("remote url : " + versionApp.getUrl());
            ProcessBuilder builder = new ProcessBuilder("java", "-jar", updateApp, updateFolder.getAbsolutePath(), versionApp.getUrl());

            builder.redirectErrorStream(true);

            try {
                Process process = builder.start();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ( ( line = bufferedReader.readLine() ) != null) {
                    if (line.isEmpty()) {
                        break;
                    }
                    //This line is being printed when XR3Player Starts
                    //So the AutoUpdater knows that it must exit
                    /*else if (line.contains("XR3Player ready to rock!"))
                        System.exit(0);*/

                    else {
                        System.out.println("info runing " + line);

                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }


        }, "restart.app").start();
    }

    private void prepareForUpdate() {
        window.setTitle(primaryScene.getEtatUpdateApp());
        prefixNewJarFile = updateFolder.getAbsolutePath() + File.separator + APPLICATION_NAME + versionApp.getNumVersion() + "-update";

        System.out.println("foldersNamePrefix " + prefixNewJarFile);
        downloadMode.getFlowUpdate().getChildren().add(new Text(("foldersNamePrefix " + prefixNewJarFile + "\n")));
        if (checkPermissions()) {
            downloadMode.getProgressLabel().setText(properties.getProperty("MESSAGE_ALERT_PERMISSION"));


            // downloadUpdate("https://github.com/cissoko97/library/raw/master/" +APPLICATION_NAME + versionApp + "-SNAPSHOT"+".jar");

            downloadUpdate(PREFIXE_REMOTE + versionApp.getUrl());

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

    /**
     * Try to download the Update
     */
    private void downloadUpdate(String downloadURL) {

        if (InfoTool.isReachableByPing("www.google.com")) {

            //Download it
            try {
                //Delete the ZIP Folder
                deleteZipFolder();

                //Create the downloadService
                downloadService = new DownloadFromLocalServer();

                //Add Bindings
                downloadMode.getProgressBar().progressProperty().bind(downloadService.progressProperty());
                downloadMode.getProgressLabel().textProperty().bind(downloadService.messageProperty());


                downloadMode.getProgressLabel().textProperty().addListener((observable, oldValue, newValue) -> {

                    //Give try again option to the user
                    /*if (newValue.toLowerCase().contains("failed")){
                        downloadMode.getFailedStackPane().setVisible(true);*/
                });
                downloadMode.getProgressBar().progressProperty().addListener(listener);
                window.setTitle(properties.getProperty("MESSAGE_ETAT_DOWNLOAD_APP") + " (" + this.APPLICATION_NAME + " )"
                        + properties.getProperty("MESSAGE_ETAT_UPDATE_APP") + " -> " + currentVersion);

                //Start


                downloadService.startDownload(downloadURL, Paths.get(prefixNewJarFile + ".jar"));
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

    public void restartApplication(String applicationName) throws InterruptedException {

        // Restart virual library

        loggerMessage("\nrestart application\n");
        downloadMode.getProgressLabel().setText("restart application");
        Thread.sleep(2500);

        new Thread(()->{
            Platform.runLater(()-> {
                try {
                    window.close();
                    deleteZipFolder();
                    Thread.sleep(2000);
                    primaryScene.constructPrimaryStage(window);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

        }, "restart thread").start();
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

        /*System.out.println("téléchargement " + downloadMode.getProgressLabel().getText()+ " : " +
                downloadMode.getProgressBar().getProgress());
        downloadMode.getFlowUpdate().getChildren().add(new Text("téléchargement " + downloadMode.getProgressLabel().getText()+ " : " +
                downloadMode.getProgressBar().getProgress()));*/

        exportZipService.exportZip(prefixNewJarFile + ".jar", updateFolder.getAbsolutePath());

        System.out.println("name file download : " + prefixNewJarFile);
        System.out.println("update path directory : " + updateFolder.getAbsolutePath());
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
        downloadMode.getProgressLabel().setText(properties.getProperty("PROGRESS_LABEL_TEXT3") + " " + properties.getProperty("INDEX_TITLE_DIALOG") + "...");

        //Delete the ZIP Folder
        deleteZipFolder();

        downloadMode.getProgressLabel().setText("relaunch application");
        // System.out.println("redémarrage de l'application");
        loggerMessage("relaunch application");
        //Start XR3Player
        try {
            restartApplication(APPLICATION_NAME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public static boolean deleteZipFolder() {
        return new File(prefixNewJarFile).delete();
    }

    public static void loggerMessage(String message) {
        Platform.runLater(() -> downloadMode.getFlowUpdate().getChildren().add(new Text(message + "\n")));
    }

}
