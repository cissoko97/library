package org.ckCoder.service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
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
                    URLConnection connection = remoteResourceLocation.get().openConnection();
                    totalBytes = Long.parseLong(connection.getHeaderField("content-Length"));


                } catch (Exception ex) {

                }
                return succeeded;
            }
        };
    }

    /**
     * Start the Download Service [[SuppressWarningsSpartan]]
     */
    public void startDownload(URL remoteResourceLocation , Path pathToLocalResource) {

    }

    //----------------------@Overrided methods--------------------------------------

    @Override
    protected void succeeded() {
        super.succeeded();
    }

    @Override
    protected void cancelled() {
        super.cancelled();
    }

    @Override
    protected void failed() {
        super.failed();
    }
}
