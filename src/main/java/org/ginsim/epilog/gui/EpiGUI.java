package org.ginsim.epilog.gui;

import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.colomoto.logicalmodel.LogicalModel;
import org.ginsim.epilog.common.Web;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumGrid;
import org.ginsim.epilog.gui.dialog.DialogAbout;
import org.ginsim.epilog.gui.dialog.DialogNewEpithelium;
import org.ginsim.epilog.gui.dialog.DialogNewProject;
import org.ginsim.epilog.gui.dialog.DialogRenameEpithelium;
import org.ginsim.epilog.gui.tab.EpiTab;
import org.ginsim.epilog.gui.tab.EpiTabInitialConditions;
import org.ginsim.epilog.gui.tab.EpiTabIntegrationFunctions;
import org.ginsim.epilog.gui.tab.EpiTabModelGrid;
import org.ginsim.epilog.gui.tab.EpiTabPerturbations;
import org.ginsim.epilog.gui.tab.EpiTabPriorityClasses;
import org.ginsim.epilog.gui.tab.EpiTabSimulation;
import org.ginsim.epilog.gui.widgets.CloseTabButton;
import org.ginsim.epilog.io.EpilogFileFilter;
import org.ginsim.epilog.io.FileIO;
import org.ginsim.epilog.project.Project;

public class EpiGUI extends JFrame {
	private static final long serialVersionUID = -3266121588934662490L;

	private final static String NAME = "Epilog";
	private final static int DIVIDER_SIZE = 3;

	private JMenuBar epiMenu;
	private JSplitPane epiMainFrame;
	private JScrollPane scrollTree;
	private JTabbedPane epiRightFrame;
	private ProjDescPanel projDescPanel;
	private JTree epiTree;
	private Project project;
	private JButton buttonAdd;
	private JButton buttonRemove;

	public EpiGUI() {
		super(NAME);

		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
			// UI Alternatives
			// com.sun.java.swing.plaf.gtk.GTKLookAndFeel <- +/-
			// com.sun.java.swing.plaf.motif.MotifLookAndFeel <- v
			// com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel <- v
			// com.sun.java.swing.plaf.windows.WindowsLookAndFeel <- x
			// javax.swing.plaf.basic.BasicLookAndFeel
			// javax.swing.plaf.metal.MetalLookAndFeel <- v
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

		this.initializeMenus();

		JPanel topLeftFrame = new JPanel();
		topLeftFrame
				.setLayout(new BoxLayout(topLeftFrame, BoxLayout.PAGE_AXIS));
		this.projDescPanel = new ProjDescPanel();
		topLeftFrame.add(this.projDescPanel);
		JPanel addRemoveModelPanel = new JPanel(new FlowLayout());
		buttonAdd = new JButton("Add");
		buttonAdd.setMargin(new Insets(0, 10, 0, 10));
		buttonAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					addSBML();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		addRemoveModelPanel.add(buttonAdd);
		buttonRemove = new JButton("Delete");
		buttonRemove.setMargin(new Insets(0, 10, 0, 10));
		buttonRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeSBML();
			}
		});
		addRemoveModelPanel.add(buttonRemove);
		topLeftFrame.add(addRemoveModelPanel);

		this.scrollTree = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JSplitPane epiLeftFrame = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				topLeftFrame, this.scrollTree);
		epiLeftFrame.setDividerSize(DIVIDER_SIZE);
		initEpitheliumJTree();

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
		this.epiMainFrame = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				epiLeftFrame, this.epiRightFrame);
		this.epiMainFrame.setDividerSize(DIVIDER_SIZE);

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
				exitProject();
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}
		});

		this.validateGUI();
		this.pack();
		this.setSize(1070, 768);
		this.setVisible(true);
	}

	private void initializeMenus() {
		this.epiMenu = new JMenuBar();

		// File menu
		JMenu menuFile = new JMenu("File");
		
		//Create New Project
		JMenuItem itemNew = new JMenuItem("New Project");
		itemNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					newProject();
				} catch (IOException e1) {
					userMessageError("You have selected an invalid SBML file!", "New project");
				}
			}
		});
		menuFile.add(itemNew);
		
		//Open Existing Project
		JMenuItem itemOpen = new JMenuItem("Load Project");
		itemOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					loadProject();
				} catch (Exception e1) {
					userMessageError("Invalid project (PEPS) file!",
							"Load project");
				}
			}
		});
		menuFile.add(itemOpen);
		
		//Save existing project
		menuFile.addSeparator();
		JMenuItem itemSave = new JMenuItem("Save");
		itemSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					savePEPS();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		menuFile.add(itemSave);
		
		//Save new Project
		JMenuItem itemSaveAs = new JMenuItem("Save As");
		itemSaveAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					saveAsPEPS();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		menuFile.add(itemSaveAs);
		
		//exit EpiLog
		menuFile.addSeparator();
		JMenuItem itemExit = new JMenuItem("Exit");
		itemExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exitProject();
			}
		});
		menuFile.add(itemExit);
		this.epiMenu.add(menuFile);

		// Epithelium menu
		JMenu menuEpithelium = new JMenu("Epithelium");
		JMenuItem itemNewEpi = new JMenuItem("New");
		itemNewEpi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					newEpithelium();
				} catch (Exception e1) {
					// TODO: handle java reflection in the future
					e1.printStackTrace();
				}
			}
		});
		menuEpithelium.add(itemNewEpi);
		JMenuItem itemDelEpi = new JMenuItem("Delete");
		itemDelEpi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteEpithelium();
			}
		});
		menuEpithelium.add(itemDelEpi);
		JMenuItem itemRenameEpi = new JMenuItem("Rename");
		itemRenameEpi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				renameEpithelium();
			}
		});
		menuEpithelium.add(itemRenameEpi);
		JMenuItem itemCloneEpi = new JMenuItem("Clone");
		itemCloneEpi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cloneEpithelium();
			}
		});
		menuEpithelium.add(itemCloneEpi);
		this.epiMenu.add(menuEpithelium);

		// Help menu
		JMenu menuHelp = new JMenu("Help");
		JMenuItem itemHelpDoc = new JMenuItem("Documentation");
		itemHelpDoc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Web.openURI("http://ginsim.org/epilog-doc/");
			}
		});
		menuHelp.add(itemHelpDoc);
		JMenuItem itemHelpAbout = new JMenuItem("About");
		itemHelpAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aboutDialog();
			}
		});
		menuHelp.add(itemHelpAbout);
		this.epiMenu.add(menuHelp);

		this.setJMenuBar(this.epiMenu);
	}

	private void aboutDialog() {
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

	private void userMessageError(String msg, String title) {
		this.userMessage(msg, title, JOptionPane.ERROR_MESSAGE);
	}

	private void newEpithelium() throws InstantiationException,
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
			Epithelium newEpi = this.project.newEpithelium(dialogPanel.getX(),dialogPanel.getY(),dialogPanel.getTopologyLayout(),
					dialogPanel.getEpiName(), dialogPanel.getSBMLName(),
					dialogPanel.getRollOver());
			this.addEpi2JTree(newEpi);
			this.project.setChanged(true);
			this.validateGUI();
		}
	}

	private void cloneEpithelium() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.epiTree
				.getLastSelectedPathComponent();
		Epithelium epi = (Epithelium) node.getUserObject();
		Epithelium epiClone = this.project.cloneEpithelium(epi);
		this.addEpithelium2JTree(epiClone);
	}

	private void addEpithelium2JTree(Epithelium epiClone) {
		this.addEpi2JTree(epiClone);
		this.project.setChanged(true);
		this.validateGUI();
	}

	private void deleteEpithelium() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.epiTree
				.getLastSelectedPathComponent();
		Epithelium epi = (Epithelium) node.getUserObject();
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
			// Remove from JTree
			DefaultTreeModel model = (DefaultTreeModel) this.epiTree.getModel();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model
					.getRoot();
			root.remove(node);
			model.reload();
			if (root.getChildCount() == 0) {
				this.initEpitheliumJTree();
			}
			this.project.setChanged(true);
			this.validateGUI();
		}
	}

	private void renameEpithelium() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.epiTree
				.getLastSelectedPathComponent();
		Epithelium epi = (Epithelium) node.getUserObject();

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

	private void newProject() throws IOException {
		if (!this.canClose("Do you really want a new project?")) {
			return;
		}
		DialogNewProject dialogPanel = new DialogNewProject();

		Window win = SwingUtilities.getWindowAncestor(this);
		JDialog dialog = new JDialog(win, "Create Model and add SBML Models",
				ModalityType.APPLICATION_MODAL);
		dialog.getContentPane().add(dialogPanel);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);

		if (dialogPanel.isDefined()) {
			Project p = new Project();
			for (File fSBML : dialogPanel.getFileList()) {
				p.addModel(fSBML.getName(), FileIO.loadSBMLModel(fSBML));
			}
			// Only when all SBML are properly loaded
			// is the this.project and the GUI updated
			this.project = p;
			this.cleanGUI();
			this.setTitle(NAME + " - Unsaved");
			for (String sbmlName : this.project.getModelNames()) {
				this.projDescPanel.addModel(sbmlName);
			}
//			this.projDescPanel.setDimension(dialogPanel.getProjWidth(),
//					dialogPanel.getProjHeight());
			initEpitheliumJTree();
			this.validateGUI();
		}
	}

	private void validateGUI() {
		boolean bIsValid = false;
		if (this.project != null) {
			bIsValid = true;
			if (this.project.hasChanged() && !this.getTitle().endsWith("*")) {
				this.setTitle(this.getTitle() + "*");
			}
			if (!this.project.hasChanged() && this.getTitle().endsWith("*")) {
				this.setTitle(this.getTitle().substring(0,
						this.getTitle().length() - 1));
			}
		}
		this.epiMenu.getMenu(1).setEnabled(bIsValid);
		JMenu file = this.epiMenu.getMenu(0);
		file.getItem(3).setEnabled(bIsValid && this.project.hasChanged()); // Save
		file.getItem(4).setEnabled(bIsValid); // Save As
		this.buttonAdd.setEnabled(bIsValid);
		this.buttonRemove.setEnabled(bIsValid
				&& this.projDescPanel.countModels() > 1);
		
		boolean eIsValid = false;
		if (epiTree.getRowCount()==1) eIsValid=false;
		else eIsValid=true;
		System.out.println(project);
		
		JMenu epithelium = this.epiMenu.getMenu(1);
		epithelium.getItem(1).setEnabled(eIsValid);
		epithelium.getItem(2).setEnabled(eIsValid);
		epithelium.getItem(3).setEnabled(eIsValid);


		this.validateJTreeExpansion();
	}

	private boolean canClose(String msg) {
		if (this.project != null && this.project.hasChanged()) {
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

	private void exitProject() {
		if (this.canClose("Do you really want to quit?")) {
			System.exit(EXIT_ON_CLOSE);
		}
	}

	private void loadProject() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException {
		if (!this.canClose("Do you really want load another project?")) {
			return;
		}
		if (this.loadPEPS()) {
			this.cleanGUI();
			this.setTitle(NAME + " - " + this.project.getFilenamePEPS());
//			this.projDescPanel.setDimension(this.project.getX(),
//					this.project.getY());
			for (String sbml : this.project.getModelNames()) {
				this.projDescPanel.addModel(sbml);
			}

			this.initEpitheliumJTree();
			for (Epithelium epi : this.project.getEpitheliumList()) {
				this.addEpi2JTree(epi);
			}
			this.validateTreeNodeSelection();
			this.validateGUI();
		}
	}

	private void addEpi2JTree(Epithelium epi) {
		DefaultMutableTreeNode epiNode = new DefaultMutableTreeNode(epi);
		((DefaultMutableTreeNode) this.epiTree.getModel().getRoot())
				.add(epiNode);

		DefaultMutableTreeNode ic = new DefaultMutableTreeNode(
				"Initial Condition");
		epiNode.add(ic);
		DefaultMutableTreeNode it = new DefaultMutableTreeNode(
				"Integration Components");
		epiNode.add(it);
		DefaultMutableTreeNode pt = new DefaultMutableTreeNode("Perturbations");
		epiNode.add(pt);
		DefaultMutableTreeNode pr = new DefaultMutableTreeNode("Priorities");
		epiNode.add(pr);
		DefaultMutableTreeNode gm = new DefaultMutableTreeNode("Model Grid");
		epiNode.add(gm);
		DefaultMutableTreeNode sim = new DefaultMutableTreeNode("Simulation");
		epiNode.add(sim);

		DefaultTreeModel model = (DefaultTreeModel) this.epiTree.getModel();
		model.reload();

		validateJTreeExpansion();
	}

	private void validateJTreeExpansion() {
		for (int i = 0; i < this.epiTree.getRowCount(); i++) {
			this.epiTree.expandRow(i);
		}
		this.epiTree.setRootVisible(false);
	}

	private void initEpitheliumJTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(
				"Epithelium list:");
		this.epiTree = new JTree(root);
		// --
		ToolTipManager.sharedInstance().registerComponent(this.epiTree);
		TreeCellRenderer renderer = new ToolTipTreeCellRenderer();
		this.epiTree.setCellRenderer(renderer);
		// --
		this.epiTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.scrollTree.setViewportView(this.epiTree);
		this.epiTree.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				checkDoubleClickEpitheliumJTree(e);
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		this.epiTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				validateTreeNodeSelection();
			}
		});
		this.epiTree.addTreeExpansionListener(new TreeExpansionListener() {
			@Override
			public void treeExpanded(TreeExpansionEvent event) {
			}

			@Override
			public void treeCollapsed(TreeExpansionEvent event) {
				validateJTreeExpansion();
			}
		});
	}

	private void validateTreeNodeSelection() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.epiTree
				.getLastSelectedPathComponent();
		boolean bActive = true;
		if (node == null || node.isLeaf() || node.isRoot()) {
			bActive = false;
		}
		JMenu epithelium = this.epiMenu.getMenu(1);
		epithelium.getItem(1).setEnabled(bActive);
		epithelium.getItem(2).setEnabled(bActive);
		epithelium.getItem(3).setEnabled(bActive);
		for (int i = 0; i < this.epiTree.getRowCount(); i++) {
			this.epiTree.expandRow(i);
		}
	}

	private void checkDoubleClickEpitheliumJTree(MouseEvent e) {
		if (e.getClickCount() != 2)
			return;
		int selRow = this.epiTree.getRowForLocation(e.getX(), e.getY());
		if (selRow == -1)
			return;

		TreePath selPath = this.epiTree.getPathForLocation(e.getX(), e.getY());
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath
				.getLastPathComponent();
		// Only opens tabs for leafs
		if (!node.isLeaf()) {
			return;
		}
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node
				.getParent();

		int tabIndex = -1;
		for (int i = 0; i < this.epiRightFrame.getTabCount(); i++) {
			EpiTab epi = (EpiTab) this.epiRightFrame.getComponentAt(i);
			if (epi.containsPath(selPath)) {
				tabIndex = i;
				break;
			}
		}
		if (tabIndex < 0 && parent != null) {
			// Create new Tab
			Epithelium epi = (Epithelium) parent.getUserObject();
			EpiTab epiTab = null;
			String title = ((Epithelium) parent.getUserObject()).getName()
					+ ":" + node;
			EpiTabChanged tabChanged = new EpiTabChanged();
			if (node.toString() == "Initial Condition") {
				epiTab = new EpiTabInitialConditions(epi, selPath, tabChanged,
						this.project.getModelFeatures());
			} else if (node.toString() == "Integration Components") {
				epiTab = new EpiTabIntegrationFunctions(epi, selPath,
						tabChanged, this.project.getModelFeatures());
			} else if (node.toString() == "Perturbations") {
				epiTab = new EpiTabPerturbations(epi, selPath, tabChanged,
						this.project.getModelFeatures());
			} else if (node.toString() == "Priorities") {
				epiTab = new EpiTabPriorityClasses(epi, selPath, tabChanged,
						this.project.getModelFeatures());
			} else if (node.toString() == "Model Grid") {
				epiTab = new EpiTabModelGrid(epi, selPath, tabChanged,
						this.project.getModelFeatures());
			} else if (node.toString() == "Simulation") {
				epiTab = new EpiTabSimulation(epi, selPath,
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

	private boolean loadPEPS() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new EpilogFileFilter("peps"));

		fc.setDialogTitle("Open file");
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				this.project = FileIO.loadPEPS(fc.getSelectedFile());
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
		try {
			this.project = FileIO.loadPEPS(new File(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.cleanGUI();
		this.setTitle(NAME + " - " + this.project.getFilenamePEPS());
		for (String sbml : this.project.getModelNames()) {
			this.projDescPanel.addModel(sbml);
		}

		this.initEpitheliumJTree();
		for (Epithelium epi : this.project.getEpitheliumList()) {
			this.addEpi2JTree(epi);
		}
		this.validateTreeNodeSelection();
		this.validateGUI();
	}

	private void saveAsPEPS() throws IOException {
		JFileChooser fc = new JFileChooser();

		if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			String newUserPEPSFile = fc.getSelectedFile().getAbsolutePath();
			newUserPEPSFile += (newUserPEPSFile.endsWith(".peps") ? ""
					: ".peps");
			FileIO.savePEPS(this.project, newUserPEPSFile);
			this.project.setFilenamePEPS(newUserPEPSFile);
			this.project.setChanged(false);
			this.setTitle(NAME + " - " + newUserPEPSFile);
			this.validateGUI();
		}
	}

	private void savePEPS() throws IOException {
		//System.out.println(this.project.getFilenamePEPS());
		String fName = this.project.getFilenamePEPS();
		if (fName == null) {
			saveAsPEPS();
		} else {
			FileIO.savePEPS(this.project, fName);
			this.project.setChanged(false);
			this.validateGUI();
		}
	}

	private void addSBML() throws IOException {
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

	private void removeSBML() {
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
		this.setTitle(NAME);
		this.initEpitheliumJTree();
		while (this.epiRightFrame.getTabCount() > 0) {
			this.epiRightFrame.removeTabAt(0);
		}

		this.buttonAdd.setEnabled(false);
		this.buttonRemove.setEnabled(false);
	}

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
		public EpiTabChanged() {
		}

		public void setEpiChanged() {
			project.setChanged(true);
			validateGUI();
		}
	}
}
