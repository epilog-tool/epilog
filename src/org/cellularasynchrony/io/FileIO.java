package org.cellularasynchrony.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class FileIO {

	public static File unzipZepiTmpDir(String zipFilePath) throws IOException {
		File outputTempDir = FileIO.createTempDirectory();
		FileIO.unZipIt(zipFilePath, outputTempDir);
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
			throw new IOException("Could not delete temp file: "
					+ temp.getAbsolutePath());
		}

		if (!(temp.mkdir())) {
			throw new IOException("Could not create temp directory: "
					+ temp.getAbsolutePath());
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

	public static File getSBMLfileInDir(File fdir) {
		for (File entry : fdir.listFiles()) {
			if (entry.getName().endsWith(".sbml")
					|| entry.getName().endsWith(".SBML")) {
				return entry;
			}
		}
		return null;
	}

	/**
	 * Unzip it
	 * 
	 * @param zipFile
	 *            input zip file
	 * @param output
	 *            zip file output folder
	 */
	private static void unZipIt(String zipFile, File folder) {

		byte[] buffer = new byte[1024];

		try {

			// System.out.println(folder.getAbsolutePath());
			if (!folder.exists()) {
				folder.mkdir();
			}

			// get the zip file content
			ZipInputStream zis = new ZipInputStream(
					new FileInputStream(zipFile));
			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {

				String fileName = ze.getName().split("/")[ze.getName().split(
						"/").length - 1];
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

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}