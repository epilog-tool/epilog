package org.ginsim.epilog.gui;

import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.ginsim.epilog.Project;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.gui.tab.EpiTab;
import org.ginsim.epilog.gui.tab.EpiTabInitialConditions;
import org.ginsim.epilog.gui.tab.EpiTabIntegrationFunctions;
import org.ginsim.epilog.gui.tab.EpiTabModelGrid;
import org.ginsim.epilog.gui.tab.EpiTabPerturbations;
import org.ginsim.epilog.gui.tab.EpiTabPriorityClasses;
import org.ginsim.epilog.gui.tab.EpiTabSimulation;
import org.ginsim.epilog.io.FileIO;

public class EpiGUI extends JFrame {
	private static final long serialVersionUID = -3266121588934662490L;

	private final static String NAME = "Epilog";
	private JMenuBar epiMenu;
	private JSplitPane epiMainFrame;
	private JSplitPane epiLeftFrame;
	private JTabbedPane epiRightFrame;
	private JPanel projDescPanel;
	private JTree epiTree;
	private Project project;
	private JDialog dialog; // TODO clean in the future?

	public EpiGUI() {
		super(NAME);

		this.initializeMenus();

		this.projDescPanel = new ProjDescPanel();

		this.epiLeftFrame = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				this.projDescPanel, null);
		initEpitheliumJTree();

		this.epiRightFrame = new JTabbedPane();
		this.epiMainFrame = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				this.epiLeftFrame, this.epiRightFrame);

		this.add(epiMainFrame);
		this.pack();
		this.setVisible(true);
	}

	private void initializeMenus() {
		epiMenu = new JMenuBar();

		// File menu
		JMenu menuFile = new JMenu("File");
		JMenuItem itemNew = new JMenuItem("New Project");
		itemNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newProject();
			}
		});
		menuFile.add(itemNew);
		JMenuItem itemOpen = new JMenuItem("Open Project");
		itemOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadProject();
			}
		});
		menuFile.add(itemOpen);
		menuFile.addSeparator();
		JMenuItem itemSave = new JMenuItem("Save");
		menuFile.add(itemSave);
		JMenuItem itemSaveAs = new JMenuItem("Save As");
		menuFile.add(itemSaveAs);
		menuFile.addSeparator();
		JMenuItem itemExit = new JMenuItem("Exit");
		itemExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(EXIT_ON_CLOSE);
			}
		});
		menuFile.add(itemExit);
		epiMenu.add(menuFile);

		// Epithelium menu
		JMenu menuEpithelium = new JMenu("Epithelium");
		JMenuItem itemNewEpi = new JMenuItem("New");
		menuEpithelium.add(itemNewEpi);
		JMenuItem itemDelEpi = new JMenuItem("Delete");
		menuEpithelium.add(itemDelEpi);
		JMenuItem itemRenameEpi = new JMenuItem("Rename");
		menuEpithelium.add(itemRenameEpi);
		JMenuItem itemCloneEpi = new JMenuItem("Clone");
		menuEpithelium.add(itemCloneEpi);
		epiMenu.add(menuEpithelium);

		// SBML menu
		JMenu menuSBML = new JMenu("SBML");
		JMenuItem itemLoadSBML = new JMenuItem("Load");
		menuSBML.add(itemLoadSBML);
		JMenuItem itemRemoveSBML = new JMenuItem("Remove");
		menuSBML.add(itemRemoveSBML);
		epiMenu.add(menuSBML);

		// Help menu
		JMenu menuHelp = new JMenu("Help");
		JMenuItem itemHelpAbout = new JMenuItem("About");
		menuHelp.add(itemHelpAbout);
		JMenuItem itemHelpWWW = new JMenuItem("WWW");
		menuHelp.add(itemHelpWWW);
		epiMenu.add(menuHelp);

		this.setJMenuBar(epiMenu);
	}

	private void newProject() {
		NewProjectDialog dialogPanel = new NewProjectDialog();
		
		Window win = SwingUtilities.getWindowAncestor(this);
		dialog = new JDialog(win, "New Project Definitions",
				ModalityType.APPLICATION_MODAL);
		dialog.getContentPane().add(dialogPanel);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
		
		if (dialogPanel.isDefined()) {
			System.out.println(dialogPanel.getProjWidth() + " x "
					+ dialogPanel.getProjHeight());
		} else {
			System.out.println("He canceled!!");
		}
	}

	private void loadProject() {
		if (this.loadPEPS()) {
			this.cleanGUI();
			this.setTitle(NAME + " - " + this.project.getFilenamePEPS());
			((ProjDescPanel) this.projDescPanel).setDimension(
					this.project.getX(), this.project.getY());
			for (String sbml : this.project.getModelNames()) {
				((ProjDescPanel) this.projDescPanel).addModel(sbml);
			}

			// TODO Epithelium Tree
			this.initEpitheliumJTree();
			for (Epithelium epi : this.project.getEpitheliumList()) {
				DefaultMutableTreeNode epiNode = new DefaultMutableTreeNode(epi);
				((DefaultMutableTreeNode) this.epiTree.getModel().getRoot())
						.add(epiNode);

				DefaultMutableTreeNode sim = new DefaultMutableTreeNode(
						"Simulation");
				epiNode.add(sim);
				DefaultMutableTreeNode ic = new DefaultMutableTreeNode(
						"Initial Conditions");
				epiNode.add(ic);
				DefaultMutableTreeNode it = new DefaultMutableTreeNode(
						"Integration Components");
				epiNode.add(it);
				DefaultMutableTreeNode pt = new DefaultMutableTreeNode(
						"Perturbations");
				epiNode.add(pt);
				DefaultMutableTreeNode pr = new DefaultMutableTreeNode(
						"Priorities");
				epiNode.add(pr);
				DefaultMutableTreeNode gm = new DefaultMutableTreeNode(
						"Model Grid");
				epiNode.add(gm);
			}
			for (int i = 0; i < this.epiTree.getRowCount(); i++) {
				this.epiTree.expandRow(i);
			}
			this.epiTree.setRootVisible(false);
		}
	}

	private void initEpitheliumJTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Empty");
		this.epiTree = new JTree(root);
		this.epiTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.epiLeftFrame.setRightComponent(this.epiTree);
		MouseListener ml = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				checkDoubleClickEpitheliumJTree(e);
			}
		};
		this.epiTree.addMouseListener(ml);
	}

	private void checkDoubleClickEpitheliumJTree(MouseEvent e) {
		int selRow = this.epiTree.getRowForLocation(e.getX(), e.getY());
		TreePath selPath = this.epiTree.getPathForLocation(e.getX(), e.getY());
		if (selRow != -1) {
			if (e.getClickCount() == 2) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath
						.getLastPathComponent();
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node
						.getParent();

				int tabIndex = -1;
				Component[] comps = this.epiRightFrame.getComponents();
				for (int i = 0; i < comps.length; i++) {
					if (((EpiTab) comps[i]).hasPath(selPath)) {
						tabIndex = i;
						break;
					}
				}
				if (tabIndex < 0) {
					// Create new Tab
					Epithelium epi = (Epithelium) parent.getUserObject();
					JPanel epiTab = null;
					if (node.toString() == "Simulation") {
						epiTab = new EpiTabSimulation(epi, selPath);
					} else if (node.toString() == "Initial Conditions") {
						epiTab = new EpiTabInitialConditions(epi, selPath);
					} else if (node.toString() == "Integration Components") {
						epiTab = new EpiTabIntegrationFunctions(epi, selPath);
					} else if (node.toString() == "Perturbations") {
						epiTab = new EpiTabPerturbations(epi, selPath);
					} else if (node.toString() == "Priorities") {
						epiTab = new EpiTabPriorityClasses(epi, selPath);
					} else if (node.toString() == "Model Grid") {
						epiTab = new EpiTabModelGrid(epi, selPath);
					}
					if (epiTab != null) {
						this.epiRightFrame.addTab(parent + ":" + node, epiTab);
						tabIndex = this.epiRightFrame.getComponentCount() - 1;
					}
				}
				// Select existing Tab
				this.epiRightFrame.setSelectedIndex(tabIndex);

			}
		}

	}

	private boolean loadPEPS() {
		FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
				"peps files (*.peps)", "peps");
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(xmlfilter);

		fc.setDialogTitle("Open file");
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				this.project = FileIO.loadPEPS(fc.getSelectedFile());
				return true;
			} catch (IOException e1) {
				// TODO: send this to User Error message!!! ptgm
				e1.printStackTrace();
			}
		}
		return false;
	}

	private void cleanGUI() {
		// Close & delete all TABS
		this.setTitle(NAME);
		((ProjDescPanel) this.projDescPanel).clean();
		this.epiTree = new JTree();
	}
}
