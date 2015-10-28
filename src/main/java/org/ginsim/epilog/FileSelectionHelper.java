package org.ginsim.epilog;

import java.io.File;

import javax.swing.JFileChooser;

import org.ginsim.epilog.io.EpilogFileFilter;

public class FileSelectionHelper {
	private static final String DIRKEY = "lastWorkingDirectory";
	private static String lastDirectory = OptionStore.getOption(DIRKEY,
			System.getProperty("user.dir"));

	public static String openFilename() {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(lastDirectory));
		fc.setFileFilter(new EpilogFileFilter("peps"));
		fc.setDialogTitle("Open file");

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

	public static String saveFilename() {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(lastDirectory));
		fc.setFileFilter(new EpilogFileFilter("peps"));
		fc.setDialogTitle("Save file");

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
