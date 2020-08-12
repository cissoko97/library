package org.ckCoder.service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.ckCoder.utils.InfoTool;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DownloadService extends Service<Boolean> {
    /**
     * The logger of the class
     */
    private final Logger logger = Logger.getLogger(DownloadService.class.getName());

    // -----
    private long totalBytes;
    private boolean succeeded;
    private volatile boolean stopThread;


    private volatile FileChannel zip;
    private final ObjectProperty<URL> remoteResourceLocation = new SimpleObjectProperty<>();
    private final ObjectProperty<Path> pathToLocalResource = new SimpleObjectProperty<>();

    private Thread copyThread;

    public DownloadService() {
        setOnSucceeded(s -> {
            System.out.println("Succeeded with value: " + super.getValue() + " , Copy Thread is Alive? " + copyThread.isAlive());
            done();
        });

        setOnFailed(f -> {
            System.out.println("Failed with value: " + super.getValue() + " , Copy Thread is Alive? " + copyThread.isAlive());
            done();
        });

        setOnCancelled(c -> {
            System.out.println("Cancelled with value: " + super.getValue() + " , Copy Thread is Alive? " + copyThread.isAlive());
            done();
        });
    }

    /**
     * The Service is done
     */
    private boolean done() {

        boolean fileDeleted = false;

        //Check if The Service Succeeded
        if (!succeeded)
            fileDeleted = new File(pathToLocalResource.get().toString()).delete();

        return fileDeleted;
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                succeeded = Boolean.TRUE;

                File destinationFile = new File(pathToLocalResource.get().toString());

                super.updateMessage("Connecting with server");
                String failMessage;

                try {
                    // Open the Connection and get totalBytes
                    URLConnection connection = remoteResourceLocation.get().openConnection();
                    totalBytes = Long.parseLong(connection.getHeaderField("content-Length"));

                    copyThread = new Thread(() -> {
                        try {
                            zip = FileChannel.open(pathToLocalResource.get(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);

                            zip.transferFrom(Channels.newChannel(connection.getInputStream()), 0, Long.MAX_VALUE);
                        } catch (Exception ex) {
                            stopThread = true;
                            logger.log(Level.WARNING, "DownloadService failed", ex);
                        } finally {
                            try {
                                zip.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    //Set to Deamon
                    copyThread.setDaemon(true);

                    //Start the THread
                    copyThread.start();

                    // --------------------------------------------------Check the %100 Progress-------------------------------------------------------------
                    long outPutFileLength;
                    long previousLength = 0;
                    //actually it is millisecondsFailTime*50(cause Thread is sleeping for 50 milliseconds
                    int millisecondsFailTime = 40;
                    // While Loop
                    while ((outPutFileLength = destinationFile.length()) < totalBytes && !stopThread) {

                        // Check the previous length
                        if (previousLength != outPutFileLength) {
                            previousLength = outPutFileLength;
                            millisecondsFailTime = 0;
                        } else
                            ++millisecondsFailTime;

                        // 2 Seconds passed without response
                        if (millisecondsFailTime == 40 || stopThread)
                            break;

                        // Update Progress
                        super.updateMessage("Downloading: [ " + InfoTool.getFileSizeEdited(totalBytes) + " ] Progress: [ " + (outPutFileLength * 100) / totalBytes + " % ]");
                        super.updateProgress((outPutFileLength * 100) / totalBytes, 100);
                        System.out.println(
                                "Current Bytes:" + outPutFileLength + " ,|, TotalBytes:" + totalBytes + " ,|, Current Progress: " + (outPutFileLength * 100) / totalBytes + " %");

                        // Sleep
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException ex) {
                            logger.log(Level.WARNING, "", ex);
                        }
                    }

                    //Update to show 100%
                    super.updateMessage("Downloading: [ " + InfoTool.getFileSizeEdited(totalBytes) + " ] Progress: [ " + (outPutFileLength * 100) / totalBytes + " % ]");
                    super.updateProgress((outPutFileLength * 100) / totalBytes, 100);
                    System.out.println(
                            "Current Bytes:" + outPutFileLength + " ,|, TotalBytes:" + totalBytes + " ,|, Current Progress: " + (outPutFileLength * 100) / totalBytes + " %");

                    // 2 Seconds passed without response
                    if (millisecondsFailTime == 40)
                        succeeded = false;
                } catch (Exception ex) {
                    succeeded = false;
                    // Stop the External Thread which is updating the %100 progress
                    stopThread = true;
                    logger.log(Level.WARNING, "DownloadService failed", ex);
                }
                return succeeded;
            }
        };
    }

    public void startDownload(URL remoteResourceLocation, Path pathToLocalResource) {
        if (!isRunning() && pathToLocalResource != null && remoteResourceLocation != null) {

            //Set
            this.setRemoteResourceLocation(remoteResourceLocation);
            this.setPathToLocalResource(pathToLocalResource);

            // setRemoteResourceLocation(new URL(java.net.URLDecoder.decode(remoteResourceLocation, "UTF-8")))

            //TotalBytes
            totalBytes = 0;

            //Restart
            restart();
        } else
            logger.log(Level.INFO, "Please specify [Remote Resource Location] and [ Path to Local Resource ]");
    }

    /**
     * Set the remote resource location
     *
     * @param remoteResourceLocation
     */
    public final void setRemoteResourceLocation(URL remoteResourceLocation) {
        this.remoteResourceLocation.set(remoteResourceLocation);
    }

    /**
     * Set the path to the local resource
     *
     * @param pathToLocalResource
     */
    public final void setPathToLocalResource(Path pathToLocalResource) {
        this.pathToLocalResource.set(pathToLocalResource);
    }
}
