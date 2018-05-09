package org.epilogtool.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.epilogtool.common.Txt;
import org.epilogtool.gui.menu.CellularModelPopupMenu;

/**
 * Container with the list of SBMLs. In this class it is defined if the options
 * in the menu bar are enabled, the dimensions of the panel with the list of
 * SBML, visual operations of loading, deleting and renaming SBML.
 * 
 */
public class ProjDescPanel extends JPanel {
	private static final long serialVersionUID = -8691538114476162311L;

	private JList<String> listSBMLs;
	private JMenu menu;
	private CellularModelPopupMenu popupmenu;

	public ProjDescPanel(JMenu sbmlMenu) {
		this.menu = sbmlMenu;
		this.popupmenu = new CellularModelPopupMenu();
		this.setLayout(new BorderLayout());

		// PAGE_START
		this.add(EpiLogGUIFactory.getJLabelBold(Txt.get("s_PROJ_PANEL_TITLE")), BorderLayout.PAGE_START);

		// CENTER
		ListModel<String> listModel = new DefaultListModel<String>();
		this.listSBMLs = new JList<String>(listModel);
		this.listSBMLs.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				@SuppressWarnings("rawtypes")
				JList l = (JList) e.getSource();
				int index = l.locationToIndex(e.getPoint());
				if (index > -1) {
					l.setToolTipText(l.getModel().getElementAt(index).toString());
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});
		this.listSBMLs.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					boolean hasModel = listSBMLs.getSelectionModel().getMinSelectionIndex() >= 0;
					boolean hasMore1 = listSBMLs.getModel().getSize() > 1;
					popupmenu.updateMenuItems(hasModel, hasMore1);
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
		this.listSBMLs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel lsModel = this.listSBMLs.getSelectionModel();
		lsModel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				updateSBMLMenuItems();
			}
		});
		JScrollPane scroll = new JScrollPane(this.listSBMLs);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setMinimumSize(new Dimension(0, 100));
		this.add(scroll, BorderLayout.CENTER);
	}

	/**
	 * Updates the SBML Menu in the tool bar. At any time a new SBML can be loaded
	 * (always enabled); the options remove, rename or export are only enabled if
	 * there is at least one SBML model loaded The user can replace a model if there
	 * are at-least 2 SBML models loaded.
	 */
	public void updateSBMLMenuItems() {
		boolean hasModel = this.listSBMLs.getSelectionModel().getMinSelectionIndex() >= 0;
		boolean hasMore1 = this.listSBMLs.getModel().getSize() > 1;
		// Menu
		this.menu.getItem(0).setEnabled(true);
		this.menu.getItem(1).setEnabled(hasModel);
		this.menu.getItem(2).setEnabled(hasModel);
		this.menu.getItem(3).setEnabled(hasModel);
		this.menu.getItem(4).setEnabled(hasModel && hasMore1);
		// Popup menu
		this.popupmenu.updateMenuItems(hasModel, hasMore1);
	}

	public void addModel(String model) {
		if (model.isEmpty() || this.hasModel(model))
			return;
		((DefaultListModel<String>) this.listSBMLs.getModel()).addElement(model);
	}

	public void removeModel(String model) {
		((DefaultListModel<String>) this.listSBMLs.getModel()).removeElement(model);
	}

	public void renameModel(String model, String newModel) {
		for (int index = 0; index < ((DefaultListModel<String>) this.listSBMLs.getModel()).size(); index++) {
			if (this.listSBMLs.getModel().getElementAt(index).equals(model)) {
				((DefaultListModel<String>) this.listSBMLs.getModel()).removeElement(model);
				((DefaultListModel<String>) this.listSBMLs.getModel()).add(index, newModel);
				break;
			}
		}
	}

	public boolean hasModel(String model) {
		return ((DefaultListModel<String>) this.listSBMLs.getModel()).contains(model);
	}

	public int countModels() {
		return this.listSBMLs.getModel().getSize();
	}

	public String getSelected() {
		return this.listSBMLs.getSelectedValue();
	}

	public void clean() {
		while (this.countModels() > 0) {
			String model = this.listSBMLs.getModel().getElementAt(0);
			this.removeModel(model);
		}
	}

}
