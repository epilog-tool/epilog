package org.epilogtool;

import java.io.File;

import javax.swing.JFileChooser;

import org.epilogtool.io.EpilogFileFilter;

/**
 * Creates the dialog that allows the loading and saving of files. 
Works for any kind of file, where "filter" is the variable representing the extension (withtout the ." 
 *
 */
public class FileSelectionHelper {
	private static final String DIRKEY = "lastWorkingDirectory";
	private static String lastDirectory = OptionStore.getOption(DIRKEY,
			System.getProperty("user.dir"));

	public static String openFilename(String filter) {
		return FileSelectionHelper.openFileDialog("Open file", filter);
	}
	
	public static String saveFilename(String filter) {
		return FileSelectionHelper.saveFileDialog("Save file", filter);
	}
	
	private static String saveFileDialog(String title, String filter) {
		
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(lastDirectory));
		fc.setFileFilter(new EpilogFileFilter(filter));
		fc.setDialogTitle(title);

		if (fc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
			return null;
	}
		File f = fc.getSelectedFile();
		if (f == null) {
			return null;
		}
		
		// change the remembered directory
		lastDirectory = fc.getCurrentDirectory().getAbsolutePath();
		OptionStore.setOption(DIRKEY, lastDirectory);

		return f.getAbsolutePath();
	}
	
	
	private static String openFileDialog(String title, String filter) {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(lastDirectory));
		fc.setFileFilter(new EpilogFileFilter(filter));
		fc.setDialogTitle(title);

		if (fc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		
		File f = fc.getSelectedFile();
		if (f == null) {
			return null;
		}

		// change the remembered directory
		lastDirectory = fc.getCurrentDirectory().getAbsolutePath();
		OptionStore.setOption(DIRKEY, lastDirectory);

		return f.getAbsolutePath();
	}
}
