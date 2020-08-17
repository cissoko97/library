package org.ckCoder.service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.ckCoder.MainApp;
import org.ckCoder.utils.InfoTool;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;

public class DownloadFromLocalServer extends Service<Boolean> {
    private final Logger logger = Logger.getLogger(DownloadFromLocalServer.class);
    private static final String DOMAINE = "domaine";
    private static final String USERNAME = "update";
    private static final String PASSWORD = "Start001";
    private volatile FileChannel zip;
    private volatile boolean stopThread;
    private final NtlmPasswordAuthentication authentication = new NtlmPasswordAuthentication(DOMAINE, USERNAME, PASSWORD);

    private SmbFile smbFile;
    private long totalBytes;
    private final ObjectProperty<URL> remoteResourceLocation = new SimpleObjectProperty<>();
    private final ObjectProperty<Path> pathToLocalResource = new SimpleObjectProperty<>();
    private boolean succeeded;

    private Thread copyThread;

    public DownloadFromLocalServer() {
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

                //creation new file for new jar
                File destinationFile = new File(pathToLocalResource.get().toString());
                super.updateMessage("Connecting with server");
                String failMessage;

                try {
                    // Open the Connection and get totalBytes
                    totalBytes = Long.parseLong(smbFile.getHeaderField("content-Length"));

                    copyThread = new Thread(() -> {
                        try {
                            zip = FileChannel.open(pathToLocalResource.get(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);

                            zip.transferFrom(Channels.newChannel(smbFile.getInputStream()), 0, Long.MAX_VALUE);
                        } catch (Exception ex) {
                            stopThread = true;
                            MainApp.loggerMessage("Download failed");
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

                        MainApp.loggerMessage("Current Bytes:" + outPutFileLength + " ,|, TotalBytes:" + totalBytes + " ,|, Current Progress: " + (outPutFileLength * 100) / totalBytes + " %");
                        /*System.out.println(
                                "Current Bytes:" + outPutFileLength + " ,|, TotalBytes:" + totalBytes + " ,|, Current Progress: " + (outPutFileLength * 100) / totalBytes + " %");*/

                        // Sleep
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException ex) {
                            logger.info(ex);
                        }
                    }

                    //Update to show 100%
                    super.updateMessage("Downloading: [ " + InfoTool.getFileSizeEdited(totalBytes) + " ] Progress: [ " + (outPutFileLength * 100) / totalBytes + " % ]");
                    super.updateProgress((outPutFileLength * 100) / totalBytes, 100);

                    //log message in textFlow
                    MainApp.loggerMessage("Current Bytes:" + outPutFileLength + " ,|, TotalBytes:" + totalBytes + " ,|, Current Progress: " + (outPutFileLength * 100) / totalBytes + " %");

                    System.out.println(
                            "Current Bytes:" + outPutFileLength + " ,|, TotalBytes:" + totalBytes + " ,|, Current Progress: " + (outPutFileLength * 100) / totalBytes + " %");

                    // 2 Seconds passed without response
                    if (millisecondsFailTime == 40)
                        succeeded = false;
                } catch (Exception ex) {
                    succeeded = false;
                    // Stop the External Thread which is updating the %100 progress
                    stopThread = true;
                    logger.log(Priority.ERROR, "DownloadService failed", ex);
                }

                return succeeded;
            }
        };
    }

    public void startDownload(URL remoteResourceLocation, Path pathToLocalResource) throws MalformedURLException {
        smbFile = new SmbFile(remoteResourceLocation.getPath(), authentication);
        if (!isRunning() && pathToLocalResource != null && remoteResourceLocation != null) {

            //Set
            this.remoteResourceLocation.set(remoteResourceLocation);
            this.pathToLocalResource.set(pathToLocalResource);

            // setRemoteResourceLocation(new URL(java.net.URLDecoder.decode(remoteResourceLocation, "UTF-8")))

            //TotalBytes
            totalBytes = 0;

            //Restart
            restart();
        } else
            logger.warn("Please specify [Remote Resource Location] and [ Path to Local Resource ]");
    }
}
