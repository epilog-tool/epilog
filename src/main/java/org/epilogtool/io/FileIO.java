package org.epilogtool.io;

import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.io.LogicalModelFormat;
import org.colomoto.biolqm.io.sbml.SBMLFormat;
import org.epilogtool.OptionStore;
import org.epilogtool.notification.NotificationManager;
import org.epilogtool.project.Project;

public class FileIO {

	private final static String CONFIG_FILE = "config.txt";

	public static File unzipPEPSTmpDir(String zipFile) throws IOException {
		File outputTempDir = FileIO.createTempDirectory();
		FileIO.unZipIt(zipFile, outputTempDir);
		return outputTempDir;
	}

	public static void zipTmpDir(File tmpDir, String zipFileName) {
		try {
			byte[] buffer = new byte[1024];
			FileOutputStream fout = new FileOutputStream(zipFileName);
			ZipOutputStream zout = new ZipOutputStream(fout);

			File[] files = tmpDir.listFiles();
			for (int i = 0; i < files.length; i++) {

				FileInputStream fin = new FileInputStream(files[i]);
				zout.putNextEntry(new ZipEntry(files[i].getName()));
				int length;

				while ((length = fin.read(buffer)) > 0) {
					zout.write(buffer, 0, length);
				}
				zout.closeEntry();
				fin.close();
			}
			zout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File createTempDirectory() throws IOException {
		final File temp;

		temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

		if (!(temp.delete())) {
			throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
		}

		if (!(temp.mkdir())) {
			throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
		}

		return temp;
	}

	public static File createTmpFileInDir(File destDir, String filename) {
		return new File(destDir.getAbsolutePath() + "/" + filename);
	}

	public static void deleteTempDirectory(File fEntry) {
		if (fEntry.isDirectory()) {
			for (File fSubEntry : fEntry.listFiles())
				deleteTempDirectory(fSubEntry);
		}
		fEntry.delete();
	}

	public static File copyFile(File srcFile, String destDir) {
		File fDestDir = new File(destDir + "/" + srcFile.getName());

		try {
			InputStream inStream = null;
			OutputStream outStream = null;
			inStream = new FileInputStream(srcFile);
			outStream = new FileOutputStream(fDestDir);

			byte[] buffer = new byte[1024];
			int length;
			// copy the file content in bytes
			while ((length = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, length);
			}

			inStream.close();
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return fDestDir;
	}

	private static void unZipIt(String zipFile, File folder) throws IOException {

		byte[] buffer = new byte[1024];

		if (!folder.exists()) {
			folder.mkdir();
		}

		// get the zip file content
		ZipInputStream zis = new ZipInputStream(new FileInputStream(new File(zipFile)));
		// get the zipped file list entry
		ZipEntry ze = zis.getNextEntry();

		while (ze != null) {

			String fileName = ze.getName().split("/")[ze.getName().split("/").length - 1];
			File newFile = new File(folder + File.separator + fileName);

			// create all non exists folders
			// else you will hit FileNotFoundException for compressed folder
			new File(newFile.getParent()).mkdirs();

			FileOutputStream fos = new FileOutputStream(newFile);

			int len;
			while ((len = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}

			fos.close();
			ze = zis.getNextEntry();
		}

		zis.closeEntry();
		zis.close();
	}

	public static LogicalModel loadSBMLModel(File file) throws Exception {
		LogicalModelFormat sbmlFormat = new SBMLFormat();
		return sbmlFormat.load(file);
	}

	/**
	 * Reads the configuration file (config.txt) from the peps model. There are two
	 * different messages in case there is a configuration file missing or the
	 * configuration couldn't be loaded.
	 * 
	 * @param filename
	 *            -> name of the peps file
	 * @return boolean -> needed for the tests, otherwise could be void
	 * 
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 */
	public static boolean loadPEPS(String filename) throws IOException {
		File tmpFolder = FileIO.unzipPEPSTmpDir(filename);
		boolean load = false;
		// Loads all the epithelium from the config.txt configuration file

		File confFile = new File(tmpFolder, CONFIG_FILE);
		if (confFile.exists()) {
			try {
				Project.getInstance().reset();
				Parser.loadConfigurations(confFile);
				load = true;
			} catch (Exception e) {
				NotificationManager.error("Loading PEPS file: ", e.getMessage() + "\n"
						+ "Help us improve EpiLog, please send us this file to support@epilog-tool.org.");
			}
		} else {
			NotificationManager.warning("Loading PEPS file",
					"Configuration file " + CONFIG_FILE + " not found inside " + filename);
		}

		// Deletes the unzip temporary folder
		FileIO.deleteTempDirectory(tmpFolder);
		Project.getInstance().setFilenamePEPS(filename);
		OptionStore.addRecentFile(filename);
		return load;
	}

	public static void savePEPS(String newPEPSFile) throws Exception {
		// Create new PEPS temp directory
		File newPEPSTmpDir = FileIO.createTempDirectory();

		// Save config.txt to tmpDir
		String configFile = newPEPSTmpDir.getAbsolutePath() + "/" + CONFIG_FILE;
		PrintWriter w = new PrintWriter(new FileWriter(configFile));
		Parser.saveConfigurations(w);
		w.close();

		// Save all SBML files to tmpDir
		for (String sSBML : Project.getInstance().getModelNames()) {
			String sFile = newPEPSTmpDir.getAbsolutePath() + "/" + sSBML;
			LogicalModelFormat sbmlFormat = new SBMLFormat();
			sbmlFormat.export(Project.getInstance().getModel(sSBML), sFile);
		}

		// Save PEPS to file
		newPEPSFile += (newPEPSFile.endsWith(".peps") ? "" : ".peps");

		FileIO.zipTmpDir(newPEPSTmpDir, newPEPSFile);
		OptionStore.addRecentFile(newPEPSFile);
	}

	public static void writeEpitheliumGrid2File(String file, Container c, String ext) {
		BufferedImage dest = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
		c.paint(dest.getGraphics());
		File fOutput = new File(file);
		try {
			ImageIO.write(dest, ext, fOutput);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}