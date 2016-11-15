package org.epilogtool;

import java.io.File;

import javax.swing.JFileChooser;

import org.epilogtool.io.EpilogFileFilter;

public class FileSelectionHelper {
	private static final String DIRKEY = "lastWorkingDirectory";
	private static String lastDirectory = OptionStore.getOption(DIRKEY,
			System.getProperty("user.dir"));

	public static String openFilename() {
		return FileSelectionHelper.openFileDialog("Open file");
	}
	
	public static String saveFilename() {
		return FileSelectionHelper.saveFileDialog("Save file");
	}
	
	private static String saveFileDialog(String title) {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(lastDirectory));
		fc.setFileFilter(new EpilogFileFilter("peps"));
		fc.setDialogTitle(title);
		if (fc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		File f = fc.getSelectedFile();
		if (f == null) {
			return null;
		}
		lastDirectory = fc.getCurrentDirectory().getAbsolutePath();
		OptionStore.setOption(DIRKEY, lastDirectory);

		return f.getAbsolutePath();
		
	}
	
	private static String openFileDialog(String title) {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(lastDirectory));
		fc.setFileFilter(new EpilogFileFilter("peps"));
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
