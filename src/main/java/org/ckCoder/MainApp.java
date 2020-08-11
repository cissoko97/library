package org.ckCoder;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.log4j.PropertyConfigurator;
import org.ckCoder.controller.PrimaryScene;
import org.ckCoder.database.Connexion;
import org.ckCoder.utils.ActionTool;
import org.ckCoder.utils.InfoTool;
import org.ckCoder.utils.NotificationType2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainApp extends Application {

    // Logger logger = Logger.getLogger(getClass());

    private PrimaryScene primaryScene = new PrimaryScene();
    /**
     * Download update as a ZIP Folder , this is the prefix name of the ZIP
     * folder
     */
    private static String foldersNamePrefix;

    /**
     * Update to download
     */
    private static int update;

    private boolean isConnect = false;

    public MainApp() throws IOException {
    }

    public static void main(String[] args) {
        PropertyConfigurator.configure(MainApp.class.getResource("/properties/log4j.properties"));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        primaryScene.constructPrimaryStage(primaryStage);
    }

    @Override
    public void init() throws Exception {
        if (!Connexion.getConnection().isClosed())
            this.isConnect = true;
    }

    public static void restartApplication(String appName) {

        // Restart XR3Player
        new Thread(() -> {
            String path = InfoTool.getBasePathForClass(MainApp.class);
            String[] applicationPath = {new File(path + appName + ".jar").getAbsolutePath()};

            //Show message that application is restarting
            Platform.runLater(() -> ActionTool.showNotification("Starting " + appName,
                    "Application Path:[ " + applicationPath[0] + " ]\n\tIf this takes more than [20] seconds either the computer is slow or it has failed....", Duration.seconds(25),
                    NotificationType2.INFORMATION));

            try {

                //Delete the ZIP Folder
                deleteZipFolder();

                //------------Wait until Application is created
                File applicationFile = new File(applicationPath[0]);
                while (!applicationFile.exists()) {
                    Thread.sleep(50);
                    System.out.println("Waiting " + appName + " Jar to be created...");
                }

                System.out.println(appName + " Path is : " + applicationPath[0]);

                //Create a process builder
                ProcessBuilder builder = new ProcessBuilder("java", "-jar", applicationPath[0], !"XR3PlayerUpdater".equals(appName) ? "" : String.valueOf(update));
                builder.redirectErrorStream(true);
                Process process = builder.start();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                // Wait n seconds
                PauseTransition pause = new PauseTransition(Duration.seconds(10));
                pause.setOnFinished(f -> Platform.runLater(() -> ActionTool.showNotification("Starting " + appName + " failed",
                        "\nApplication Path: [ " + applicationPath[0] + " ]\n\tTry to do it manually...", Duration.seconds(10), NotificationType2.ERROR)));
                pause.play();

                // Continuously Read Output to check if the main application started
                String line;
                while (process.isAlive())
                    while ((line = bufferedReader.readLine()) != null) {
                        if (line.isEmpty())
                            break;
                            //This line is being printed when XR3Player Starts
                            //So the AutoUpdater knows that it must exit
                        else if (line.contains("XR3Player ready to rock!"))
                            System.exit(0);
                    }

            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.INFO, null, ex);

                // Show failed message
                Platform.runLater(() -> Platform.runLater(() -> ActionTool.showNotification("Starting " + appName + " failed",
                        "\nApplication Path: [ " + applicationPath[0] + " ]\n\tTry to do it manually...", Duration.seconds(10), NotificationType2.ERROR)));

            }
        }, "Start Application Thread").start();
    }

    public static boolean deleteZipFolder() {
        return new File(foldersNamePrefix + ".zip").delete();
    }
}
