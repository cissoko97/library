package org.ckCoder;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.log4j.PropertyConfigurator;
import org.ckCoder.controller.PrimaryScene;
import org.ckCoder.models.VersionApp;
import org.ckCoder.service.VersionService;
import org.ckCoder.utils.*;

import java.io.*;
import java.sql.SQLException;
import java.util.Properties;

public class MainApp extends Application {

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

    private VersionApp versionApp;


    private final VersionService versionService = new VersionService();

    public MainApp() throws IOException {

    }

    public static void main(String[] args) {
        PropertyConfigurator.configure(MainApp.class.getResource("/properties/log4j.properties"));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        //prepare window
        primaryStage.centerOnScreen();

        // load icon
        primaryStage.getIcons().add(InfoTool.getImageFromResourcesFolder("/img/home_icone.jpeg"));
        Properties properties = SelectedLanguage.getInstace();

        // check last version in the data base
        versionApp = versionService.checkVersion();

        // declare a propertie who content a current version for this application
        Properties pr = new Properties();
        InputStream stream = getClass().getResourceAsStream("/properties/config.properties");
        pr.load(stream);

        //check current version

        /**
         * Update to download
         */
        double currentVersion;
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

                openUpdateApp();
            } else {

                System.exit(0);

            }

        } else {
            // window.setResizable(true);
            primaryScene.constructPrimaryStage(primaryStage);
        }


    }


    private void openUpdateApp() {
        System.out.println(InfoTool.getBasePathForClass(MainApp.class));


        String pathOfUpdateApp = InfoTool.getBasePathForClass(MainApp.class) + "updateapp" + File.separator;
        String updateApp = pathOfUpdateApp + "auto-update.jar";

        File updateFile = new File(updateApp);

        // if auto-aupdate app do not exist exit app

        if (!updateFile.exists()) {

           /* ActionTool.showNotification("unable to acces file", "unable to acces "
            + updateApp, Duration.minutes(1), NotificationType2.ERROR);*/
           System.exit(0);
        }

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
                        System.out.println(line);

                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }


        }, "restart.app").start();
    }
}
