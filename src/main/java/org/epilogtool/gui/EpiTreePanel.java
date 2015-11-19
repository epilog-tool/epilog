package org.epilogtool.gui;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.epilogtool.core.Epithelium;
import org.epilogtool.gui.menu.EpiTreePopupMenu;
import org.epilogtool.project.ProjectFeatures;

public class EpiTreePanel extends JPanel {
	private static final long serialVersionUID = -2143708024027520789L;

	private JScrollPane scrollTree;
	private JMenu menu;
	private JTree epiTree;
	private EpiTreePopupMenu popupmenu;

	public EpiTreePanel(JMenu epiMenu) {
		this.menu = epiMenu;
		this.epiTree = null;
		this.popupmenu = new EpiTreePopupMenu();

		this.setLayout(new BorderLayout());
		this.add(EpilogGUIFactory.getJLabelBold("List of Epithelium's:"),
				BorderLayout.PAGE_START);
		this.scrollTree = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(this.scrollTree, BorderLayout.CENTER);
	}

	public void initEpitheliumJTree(ProjectFeatures projectFeatures) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(
				"Epithelium list:");
		this.epiTree = new JTree(root);

		ToolTipManager.sharedInstance().registerComponent(this.epiTree);
		TreeCellRenderer renderer = new ToolTipTreeCellRenderer(projectFeatures);
		this.epiTree.setCellRenderer(renderer);

		this.epiTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.scrollTree.setViewportView(this.epiTree);
		this.epiTree.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) {
					checkDoubleClickEpitheliumJTree(e);
				} else if (e.isPopupTrigger()) {
					// popupmenu.updateMenuItems(listSBMLs.getSelectedValue() !=
					// null);
					popupmenu.show(e.getComponent(), e.getX(), e.getY());
				}
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

	public Epithelium getSelectedEpithelium() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.epiTree
				.getLastSelectedPathComponent();
		return (Epithelium) node.getUserObject();
	}

	public void remove(ProjectFeatures projectFeatures) {
		// Remove from JTree
		DefaultTreeModel model = (DefaultTreeModel) this.epiTree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.epiTree
				.getLastSelectedPathComponent();
		root.remove(node);
		model.reload();
		if (root.getChildCount() == 0) {
			this.initEpitheliumJTree(projectFeatures);
		}
	}

	public void validateJTreeExpansion() {
		if (this.epiTree != null) {
			for (int i = 0; i < this.epiTree.getRowCount(); i++) {
				this.epiTree.expandRow(i);
			}
			this.epiTree.setRootVisible(false);
		}
	}

	public void updateEpiMenuItems() {
		this.validateTreeNodeSelection();
	}

	private void validateTreeNodeSelection() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.epiTree
				.getLastSelectedPathComponent();
		boolean bActive = node != null && !node.isLeaf() && !node.isRoot();
		this.menu.getItem(1).setEnabled(bActive);
		this.menu.getItem(2).setEnabled(bActive);
		this.menu.getItem(3).setEnabled(bActive);
		this.popupmenu.notifySelection(this.menu.isEnabled(), bActive);
	}

	public void addEpi2JTree(Epithelium epi) {
		DefaultMutableTreeNode epiNode = new DefaultMutableTreeNode(epi);
		((DefaultMutableTreeNode) this.epiTree.getModel().getRoot())
				.add(epiNode);

		DefaultMutableTreeNode gm = new DefaultMutableTreeNode("Model Grid");
		epiNode.add(gm);
		DefaultMutableTreeNode it = new DefaultMutableTreeNode(
				"Integration Components");
		epiNode.add(it);
		DefaultMutableTreeNode ic = new DefaultMutableTreeNode(
				"Initial Condition");
		epiNode.add(ic);
		DefaultMutableTreeNode ptc = new DefaultMutableTreeNode(
				"Component Perturbations");
		epiNode.add(ptc);
		DefaultMutableTreeNode pte = new DefaultMutableTreeNode(
				"Epithelial Perturbations");
		epiNode.add(pte);
		DefaultMutableTreeNode mu = new DefaultMutableTreeNode("Model Updating");
		epiNode.add(mu);
		DefaultMutableTreeNode eu = new DefaultMutableTreeNode(
				"Epithelial Updating");
		epiNode.add(eu);
		DefaultMutableTreeNode sim = new DefaultMutableTreeNode("Simulation");
		epiNode.add(sim);

		DefaultTreeModel model = (DefaultTreeModel) this.epiTree.getModel();
		model.reload();

		this.validateJTreeExpansion();
	}

	private void checkDoubleClickEpitheliumJTree(MouseEvent e) {
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

		if (parent != null) {
			EpiGUI.getInstance().openEpiTab(
					(Epithelium) parent.getUserObject(), selPath,
					node.toString());
		}
	}
}