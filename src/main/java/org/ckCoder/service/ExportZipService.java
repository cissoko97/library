/*
 * 
 */
package org.ckCoder.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.util.Duration;
import org.ckCoder.MainApp;
import org.ckCoder.utils.ActionTool;
import org.ckCoder.utils.NotificationType;
import org.ckCoder.utils.NotificationType2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * This class is used to import an XR3Player database (as .zip folder)
 * 
 * @author SuperGoliath
 *
 */
public class ExportZipService extends Service<Boolean> {
	
	/** The logger. */
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	/** The given ZIP file */
	private String zipFile;
	
	/** The output folder */
	private String destinationFolder;
	
	/** The exception. */
	private String exception;
	
	/**
	 * Constructor.
	 */
	public ExportZipService() {
		
		setOnSucceeded(s -> {
			
			//Check the value
			if (!getValue()) {
				ActionTool.showNotification("Exporting ZIP File", exception, Duration.seconds(2), NotificationType2.ERROR);
				done();
			} else {
				ActionTool.showNotification("Exporting ZIP File", "Successfully exported the ZIP File", Duration.seconds(2), NotificationType2.INFORMATION);
				
			}
			
		});
		
		setOnFailed(failed -> {
			done();
			ActionTool.showNotification("Exporting ZIP File", exception, Duration.seconds(2), NotificationType2.ERROR);
		});
		
		setOnCancelled(c -> {
			done();
			ActionTool.showNotification("Exporting ZIP File", exception, Duration.seconds(2), NotificationType2.ERROR);
			
		});
	}
	
	/**
	 * Done.
	 */
	private static void done() {
		
		//		Main.updateScreen.setVisible(false);
		//		Main.updateScreen.getProgressBar().progressProperty().unbind();
	}
	
	/**
	 * This Services initialises and external Thread to Export a ZIP folder to a
	 * Destination Folder
	 *
	 * @param zipFolder
	 *        The absolute path of the ZIP folder
	 * @param destinationFolder
	 *        The absolutePath of the destination folder
	 */
	public void exportZip(String zipFolder , String destinationFolder) {
		
		//-----
		this.zipFile = zipFolder;
		this.destinationFolder = destinationFolder;
		
		reset();
		restart();
	}
	
	/*
	 * (non-Javadoc)
	 * @see javafx.concurrent.Service#createTask()
	 */
	@Override
	protected Task<Boolean> createTask() {
		return new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				
				//---------------------Move on Importing the Database-----------------------------------------------
				
				// get the zip file content
				try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
					
					// create output directory is not exists
					File folder = new File(destinationFolder);
					System.out.println(zipFile);
					System.out.println(destinationFolder);
					if (!folder.exists())
						folder.mkdir();
					
					// get the zipped file list entry
					ZipEntry ze = zis.getNextEntry();
					
					// Count entries
					ZipFile zip = new ZipFile(zipFile);
					double counter = 0 , total = zip.size();
					MainApp.loggerMessage("count file : " + counter+ " total size : " + total);
					//Start
					MainApp.loggerMessage("transFert de fichier");
					for (byte[] buffer = new byte[1024]; ze != null;) {


						String fileName = ze.getName();
						System.out.print("unzip file : " + ze.getName());

						File newFile = new File(destinationFolder + File.separator + fileName);
						if(!newFile.exists())
							newFile.getParentFile().mkdir();
						System.out.println("\t" + newFile.exists());
						
						// Refresh the dataLabel text
						updateMessage("Exporting: [ " + newFile.getName() + " ]");
						
						// create all non exists folders else you will hit FileNotFoundException for compressed folder
						//new File(newFile.getParent());

						//Create File OutputStream
						try (FileOutputStream fos = new FileOutputStream(newFile)) {
							
							// Copy byte by byte
							System.out.println("copy file => " + newFile.getAbsolutePath());
							int len;
							while ( ( len = zis.read(buffer) ) > 0)
								fos.write(buffer, 0, len);
							
						} catch (IOException ex) {
							exception = ex.getMessage();

							//logger.log(Level.WARNING, "warning 1", ex);
						}
						
						//Get next entry
						ze = zis.getNextEntry();
						
						//Update the progress
						updateProgress(++counter / total, 1);
					}
					
					zis.closeEntry();
					zis.close();
					zip.close();
					
				} catch (IOException ex) {
					exception = ex.getMessage();
					MainApp.loggerMessage(ex.getMessage());
					logger.log(Level.WARNING, "warning 2", ex);
					return false;
				}
				
				return true;
			}
			
		};
	}
}
