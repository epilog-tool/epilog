package org.epilogtool.gui;

import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.epilogtool.FileSelectionHelper;
import org.epilogtool.OptionStore;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.gui.dialog.DialogAbout;
import org.epilogtool.gui.dialog.DialogNewEpithelium;
import org.epilogtool.gui.dialog.DialogRenameEpithelium;
import org.epilogtool.gui.menu.EpitheliumMenu;
import org.epilogtool.gui.menu.FileMenu;
import org.epilogtool.gui.menu.HelpMenu;
import org.epilogtool.gui.menu.SBMLMenu;
import org.epilogtool.gui.tab.EpiTab;
import org.epilogtool.gui.tab.EpiTabInitialConditions;
import org.epilogtool.gui.tab.EpiTabIntegrationFunctions;
import org.epilogtool.gui.tab.EpiTabModelGrid;
import org.epilogtool.gui.tab.EpiTabPerturbations;
import org.epilogtool.gui.tab.EpiTabSimulation;
import org.epilogtool.gui.tab.EpiTabUpdateScheme;
import org.epilogtool.gui.widgets.CloseTabButton;
import org.epilogtool.io.FileIO;
import org.epilogtool.project.Project;

public class EpiGUI extends JFrame {
	private static final long serialVersionUID = -3266121588934662490L;

	private final static String TITLE_APPNAME = "Epilog";
	private final static String TITLE_UNTITLED = " - Untitled";
	private final static String TITLE_MODIFIED_SYMBOL = "*";

	private final static int DIVIDER_SIZE = 3;
	private final static int DIVIDER_POS_X = 210;

	private JMenuBar epiMenu;
	private JSplitPane epiMainFrame;
	private JTabbedPane epiRightFrame;
	private ProjDescPanel projDescPanel;
	private EpiTreePanel epiTreePanel;
	private Project project;
	private static EpiGUI epigui;

	public static EpiGUI getInstance() {
		if (epigui == null) {
			epigui = new EpiGUI();
		}
		return epigui;
	}

	private EpiGUI() {
		super(TITLE_APPNAME);

		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
			// UI Alternatives
			// com.sun.java.swing.plaf.gtk.GTKLookAndFeel <- +/-
			// com.sun.java.swing.plaf.motif.MotifLookAndFeel <- ok
			// com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel <- ok
			// com.sun.java.swing.plaf.windows.WindowsLookAndFeel <- x
			// javax.swing.plaf.basic.BasicLookAndFeel
			// javax.swing.plaf.metal.MetalLookAndFeel <- ok
			// javax.swing.plaf.multi.MultiLookAndFeel <- X
			// javax.swing.plaf.nimbus.NimbusLookAndFeel <- +/-
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		} catch (InstantiationException e2) {
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			e2.printStackTrace();
		} catch (UnsupportedLookAndFeelException e2) {
			e2.printStackTrace();
		}

		// BEGIN -- Menu Bar
		this.epiMenu = new JMenuBar();

		// File menu
		JMenu fileMenu = FileMenu.getMenu();
		this.epiMenu.add(fileMenu);

		// SBML menu
		JMenu sbmlMenu = SBMLMenu.getMenu();
		this.epiMenu.add(sbmlMenu);

		// Epithelium menu
		JMenu epiMenu = EpitheliumMenu.getMenu();
		this.epiMenu.add(epiMenu);

		// Help menu
		JMenu helpMenu = HelpMenu.getMenu();
		this.epiMenu.add(helpMenu);

		this.setJMenuBar(this.epiMenu);
		// END -- Menu bar

		JPanel topLeftFrame = new JPanel();
		topLeftFrame
				.setLayout(new BoxLayout(topLeftFrame, BoxLayout.PAGE_AXIS));
		this.projDescPanel = new ProjDescPanel(sbmlMenu);
		topLeftFrame.add(this.projDescPanel);

		// TabbedPane
		this.epiRightFrame = new JTabbedPane();
		this.epiRightFrame.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane jtp = (JTabbedPane) e.getSource();
				if (jtp.getTabCount() == 0)
					return;
				int i = jtp.getSelectedIndex();
				EpiTab epitab = (EpiTab) epiRightFrame.getComponentAt(i);
				if (epitab == null)
					return;
				epitab.notifyChange();
			}
		});

		// Epithelium list
		this.epiTreePanel = new EpiTreePanel(epiMenu);
		JSplitPane epiLeftFrame = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				topLeftFrame, this.epiTreePanel);
		epiLeftFrame.setDividerSize(DIVIDER_SIZE);

		this.epiMainFrame = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				epiLeftFrame, this.epiRightFrame);
		this.epiMainFrame.setDividerSize(DIVIDER_SIZE);
		this.epiMainFrame.setDividerLocation(DIVIDER_POS_X);

		this.add(this.epiMainFrame);

		// JFrame close button
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				quitProject();
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}
		});

		this.pack();
		this.setSize(1070, 768);
		this.setVisible(true);
	}

	public void aboutDialog() {
		Window win = SwingUtilities.getWindowAncestor(this);
		JDialog dialog = new JDialog(win, "About",
				ModalityType.APPLICATION_MODAL);
		dialog.getContentPane().add(new DialogAbout());
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	private void userMessage(String msg, String title, int type) {
		JOptionPane.showMessageDialog(this, msg, title, type);

	}

	public void userMessageError(String msg, String title) {
		this.userMessage(msg, title, JOptionPane.ERROR_MESSAGE);
	}

	public void newEpithelium() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException {
		DialogNewEpithelium dialogPanel = new DialogNewEpithelium(
				this.project.getModelNames(),
				this.project.getEpitheliumNameList());

		Window win = SwingUtilities.getWindowAncestor(this);
		JDialog dialog = new JDialog(win, "New Epithelium",
				ModalityType.APPLICATION_MODAL);
		dialog.getContentPane().add(dialogPanel);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
		if (dialogPanel.isDefined()) {
			Epithelium newEpi = this.project.newEpithelium(
					dialogPanel.getEpitheliumWidth(),
					dialogPanel.getEpitheliumHeight(),
					dialogPanel.getTopologyLayout(), dialogPanel.getEpiName(),
					dialogPanel.getSBMLName(), dialogPanel.getRollOver());
			this.addEpithelium2JTree(newEpi);
		}
	}

	public void cloneEpithelium() {
		Epithelium epi = this.epiTreePanel.getSelectedEpithelium();
		Epithelium epiClone = this.project.cloneEpithelium(epi);
		this.addEpithelium2JTree(epiClone);
	}

	private void addEpithelium2JTree(Epithelium epiClone) {
		this.epiTreePanel.addEpi2JTree(epiClone);
		this.project.setChanged(true);
		this.validateGUI();
	}

	public void deleteEpithelium() {
		Epithelium epi = this.epiTreePanel.getSelectedEpithelium();
		int result = JOptionPane.showConfirmDialog(this,
				"Do you really want to delete epithelium:\n" + epi + " ?",
				"Question", JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			// Remove from core
			this.project.removeEpithelium(epi);
			// Remove from EpiTabs
			for (int i = this.epiRightFrame.getTabCount() - 1; i >= 0; i--) {
				EpiTab epitab = (EpiTab) this.epiRightFrame.getComponentAt(i);
				if (epitab.containsEpithelium(epi)) {
					epiRightFrame.removeTabAt(i);
				}
			}
			this.epiTreePanel.remove(this.project.getModelFeatures());
			this.project.setChanged(true);
			this.validateGUI();
		}
	}

	public void renameEpithelium() {
		Epithelium epi = this.epiTreePanel.getSelectedEpithelium();

		DialogRenameEpithelium dialogPanel = new DialogRenameEpithelium(
				epi.getName(), this.project.getEpitheliumNameList());
		Window win = SwingUtilities.getWindowAncestor(this);
		JDialog dialog = new JDialog(win, "Rename Epithelium",
				ModalityType.APPLICATION_MODAL);
		dialog.getContentPane().add(dialogPanel);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);

		if (dialogPanel.isDefined()
				&& !epi.toString().equals(dialogPanel.getEpitheliumName())) {
			this.project.setChanged(true);
			epi.setName(dialogPanel.getEpitheliumName());
			for (int i = this.epiRightFrame.getTabCount() - 1; i >= 0; i--) {
				EpiTab epitab = (EpiTab) this.epiRightFrame.getComponentAt(i);
				if (epitab.containsEpithelium(epi)) {
					DefaultMutableTreeNode epiNode = (DefaultMutableTreeNode) epitab
							.getPath().getLastPathComponent();
					String title = epi.getName() + ":" + epiNode;
					((CloseTabButton) epiRightFrame.getTabComponentAt(i))
							.changeTitle(title);
				}
			}
			this.validateGUI();
		}
	}

	public void newProject() throws IOException {
		if (!this.canClose("Do you really want a new project?")) {
			return;
		}
		this.project = new Project();
		this.cleanGUI();
		this.validateGUI();
	}

	private void validateGUI() {
		if (this.project.hasChanged()
				&& !this.getTitle().endsWith(TITLE_MODIFIED_SYMBOL)) {
			this.setTitle(this.getTitle() + TITLE_MODIFIED_SYMBOL);
		}
		if (!this.project.hasChanged()
				&& this.getTitle().endsWith(TITLE_MODIFIED_SYMBOL)) {
			this.setTitle(this.getTitle().substring(0,
					this.getTitle().length() - 1));
		}

		// File Menu
		boolean bIsValid = this.project.getFilenamePEPS() != null ? this.project
				.hasChanged() : this.projDescPanel.countModels() > 0;
		JMenu file = this.epiMenu.getMenu(0);
		file.getItem(4).setEnabled(bIsValid); // Save
		file.getItem(5).setEnabled(this.projDescPanel.countModels() > 0); // SaveAs

		// SBML Menu
		this.projDescPanel.updateSBMLMenuItems();

		// Epithelium Menu
		bIsValid = this.projDescPanel.countModels() > 0;
		JMenu sbml = this.epiMenu.getMenu(2);
		sbml.setEnabled(bIsValid);
		sbml.getItem(1).setEnabled(false);
		sbml.getItem(2).setEnabled(false);
		sbml.getItem(3).setEnabled(false);
		this.epiTreePanel.updateEpiMenuItems();

		this.epiTreePanel.validateJTreeExpansion();
	}

	private boolean canClose(String msg) {
		if (this.projDescPanel.countModels() > 0 && this.project.hasChanged()) {
			int n = JOptionPane.showConfirmDialog(this,
					"You have unsaved changes!\n" + msg, "Question",
					JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	public void quitProject() {
		if (this.canClose("Do you really want to quit?")) {
			OptionStore.saveOptions();
			System.exit(EXIT_ON_CLOSE);
		}
	}

	public void loadProject() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException {
		if (!this.canClose("Do you really want load another project?")) {
			return;
		}
		if (this.loadPEPS()) {
			this.cleanGUI();
			this.setTitle(TITLE_APPNAME + " - "
					+ this.project.getFilenamePEPS());
			// this.projDescPanel.setDimension(this.project.getX(),
			// this.project.getY());
			for (String sbml : this.project.getModelNames()) {
				this.projDescPanel.addModel(sbml);
			}
			this.project.setChanged(false);
			this.epiTreePanel.initEpitheliumJTree(this.project
					.getModelFeatures());
			for (Epithelium epi : this.project.getEpitheliumList()) {
				this.epiTreePanel.addEpi2JTree(epi);
			}
			this.validateGUI();
		}
	}

	private boolean loadPEPS() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException {
		if (!this.canClose("Do you really want load another project?")) {
			return false;
		}
		String filename = FileSelectionHelper.openFilename();
		if (filename != null) {
			try {
				this.project = FileIO.loadPEPS(filename);
				return true;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return false;
	}

	public void loadPEPS(String filename) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException {
		if (!this.canClose("Do you really want load another project?")) {
			return;
		}
		try {
			this.project = FileIO.loadPEPS(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.cleanGUI();
		this.setTitle(TITLE_APPNAME + " - " + this.project.getFilenamePEPS());
		for (String sbml : this.project.getModelNames()) {
			this.projDescPanel.addModel(sbml);
		}

		this.epiTreePanel.initEpitheliumJTree(this.project.getModelFeatures());
		for (Epithelium epi : this.project.getEpitheliumList()) {
			this.epiTreePanel.addEpi2JTree(epi);
		}
		this.validateGUI();
	}

	public void saveAsPEPS() throws IOException {
		String filename = FileSelectionHelper.saveFilename();

		if (filename != null) {
			filename += (filename.endsWith(".peps") ? "" : ".peps");
			FileIO.savePEPS(this.project, filename);
			this.project.setFilenamePEPS(filename);
			this.project.setChanged(false);
			this.setTitle(TITLE_APPNAME + " - " + filename);
			this.validateGUI();
		}
	}

	public void savePEPS() throws IOException {
		String fName = this.project.getFilenamePEPS();
		if (fName == null) {
			saveAsPEPS();
		} else {
			FileIO.savePEPS(this.project, fName);
			this.project.setChanged(false);
			this.validateGUI();
		}
	}

	public void loadSBML() throws IOException {
		FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
				"sbml files (*.sbml)", "sbml");
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(xmlfilter);
		fc.setDialogTitle("Open file");
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			if (this.projDescPanel.hasModel(fc.getSelectedFile().getName())) {
				JOptionPane.showMessageDialog(this,
						"A model with the same name '"
								+ fc.getSelectedFile().getName()
								+ "' already exists!", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			this.projDescPanel.addModel(fc.getSelectedFile().getName());
			LogicalModel m = FileIO.loadSBMLModel(fc.getSelectedFile());
			this.project.addModel(fc.getSelectedFile().getName(), m);
			this.notifyEpiModelGrids();
			this.validateGUI();
		}
	}

	public void removeSBML() {
		String model = this.projDescPanel.getSelected();
		if (model != null) {
			if (this.project.removeModel(model)) {
				this.projDescPanel.removeModel(model);
				this.notifyEpiModelGrids();
			} else {
				JOptionPane.showMessageDialog(this, "Model '" + model
						+ "' is being used!", "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		}
		this.validateGUI();
	}

	private void notifyEpiModelGrids() {
		for (int i = 0; i < this.epiRightFrame.getTabCount(); i++) {
			Component c = this.epiRightFrame.getComponentAt(i);
			if (c instanceof EpiTabModelGrid) {
				((EpiTabModelGrid) c).notifyChange();
			}
		}
	}

	private void cleanGUI() {
		// Close & delete all TABS
		this.setTitle(TITLE_APPNAME + TITLE_UNTITLED);
		this.epiTreePanel.initEpitheliumJTree(this.project.getModelFeatures());
		while (this.epiRightFrame.getTabCount() > 0) {
			this.epiRightFrame.removeTabAt(0);
		}
		this.projDescPanel.clean();
		// SBML Menu
		JMenu sbml = this.epiMenu.getMenu(1);
		sbml.getItem(0).setEnabled(false);
		sbml.getItem(1).setEnabled(false);
	}

	public void openEpiTab(Epithelium epi, TreePath selPath, String tabName) {
		int tabIndex = -1;
		for (int i = 0; i < this.epiRightFrame.getTabCount(); i++) {
			EpiTab epiInTab = (EpiTab) this.epiRightFrame.getComponentAt(i);
			if (epiInTab.containsPath(selPath)) {
				tabIndex = i;
				break;
			}
		}
		if (tabIndex < 0) {
			// Create new Tab
			EpiTab epiTab = null;
			String title = epi.getName() + ":" + tabName;
			EpiTabChanged tabChanged = new EpiTabChanged();
			ProjectChangedInTab projChanged = new ProjectChangedInTab();
			if (tabName.equals("Initial Condition")) {
				epiTab = new EpiTabInitialConditions(epi, selPath, projChanged,
						tabChanged, this.project.getModelFeatures());
			} else if (tabName.equals("Integration Components")) {
				epiTab = new EpiTabIntegrationFunctions(epi, selPath,
						projChanged, tabChanged,
						this.project.getModelFeatures());
			} else if (tabName.equals("Perturbations")) {
				epiTab = new EpiTabPerturbations(epi, selPath, projChanged,
						tabChanged, this.project.getModelFeatures());
			} else if (tabName.equals("Updating Scheme")) {
				epiTab = new EpiTabUpdateScheme(epi, selPath, projChanged,
						tabChanged, this.project.getModelFeatures());
			} else if (tabName.equals("Model Grid")) {
				epiTab = new EpiTabModelGrid(epi, selPath, projChanged,
						tabChanged, this.project.getModelFeatures());
			} else if (tabName.equals("Simulation")) {
				epiTab = new EpiTabSimulation(epi, selPath, projChanged,
						this.project.getModelFeatures(),
						new SimulationEpiClone());
			}
			if (epiTab != null) {
				this.epiRightFrame.addTab(title, epiTab);
				epiTab.initialize();

				CloseTabButton tabButton = new CloseTabButton(title,
						this.epiRightFrame);
				tabIndex = this.epiRightFrame.getTabCount() - 1;
				this.epiRightFrame.setTabComponentAt(tabIndex, tabButton);
			}
		}
		// Select existing Tab
		this.epiRightFrame.setSelectedIndex(tabIndex);
	}

	// Inner Classes
	public class SimulationEpiClone {
		public SimulationEpiClone() {
		}

		public void cloneEpithelium(Epithelium epi, EpitheliumGrid currGrid) {
			Epithelium epiClone = project.cloneEpithelium(epi);
			for (int x = 0; x < currGrid.getX(); x++) {
				for (int y = 0; y < currGrid.getY(); y++) {
					byte[] stateClone = currGrid.getCellState(x, y).clone();
					epiClone.getEpitheliumGrid().setCellState(x, y, stateClone);
				}
			}
			addEpithelium2JTree(epiClone);
		}
	}

	public class EpiTabChanged {
		public void setEpiChanged() {
			project.setChanged(true);
			validateGUI();
		}
	}

	public class ProjectChangedInTab {
		public void setChanged(EpiTab changedTab) {
			project.setChanged(true);
			for (int i = 0; i < epiRightFrame.getTabCount(); i++) {
				Component c = epiRightFrame.getComponentAt(i);
				if (c instanceof EpiTabSimulation
						|| c instanceof EpiTabInitialConditions) {
					EpiTab tab = (EpiTab) c;
					if (!tab.equals(changedTab)) {
						tab.notifyChange();
					}
				}
			}
			validateGUI();
		}
	}
}