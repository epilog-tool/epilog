package pt.igc.nmd.epilogue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.io.sbml.SBMLFormat;

public class Save {

	MainPanel mainPanel;
	private JFileChooser fc = new JFileChooser();
	LogicalModel unitaryModel = null;
	LogicalModel composedModel = null;
	SBMLFormat sbmlFormat = new SBMLFormat();

	public Save(MainPanel mainPanel) {

		this.mainPanel = mainPanel;
	}

	public void saveFiles() throws IOException {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		try {
			fos = new FileOutputStream("t.tmp");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			oos = new ObjectOutputStream(fos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		oos.writeObject(mainPanel.getSimulation().getComposedState());
		oos.writeObject(mainPanel.getEpithelium().getComposedModel());
		oos.writeObject(mainPanel.getEpithelium().getUnitaryModel());

		oos.close();
	}
	
	private void loadFiles() {

		File file = fc.getSelectedFile();
		LogicalModel logicalModel = null;

		try {
			logicalModel = sbmlFormat.importFile(file);
			//composedModel = importfile(file);
		} catch (IOException e) {
			System.err.println("Cannot import file " + file.getAbsolutePath()
					+ ": " + e.getMessage());
		}

	}
	
	
	
}
