package org.epilogtool.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
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

public class ProjDescPanel extends JPanel {
	private static final long serialVersionUID = -8691538114476162311L;

	private static final String LABEL = "List of loaded models: ";
	private JList<String> listSBMLs;
	private JMenu menu;

	public ProjDescPanel(JMenu sbmlMenu) {
		this.menu = sbmlMenu;
		this.setLayout(new BorderLayout());

		// PAGE_START
		this.add(EpilogGUIFactory.getJLabelBold(LABEL), BorderLayout.PAGE_START);

		// CENTER
		ListModel<String> listModel = new DefaultListModel<String>();
		this.listSBMLs = new JList<String>(listModel);
		this.listSBMLs.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				JList l = (JList) e.getSource();
				int index = l.locationToIndex(e.getPoint());
				if (index > -1) {
					l.setToolTipText(l.getModel().getElementAt(index)
							.toString());
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
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

	public void updateSBMLMenuItems() {
		menu.getItem(0).setEnabled(true);
		int index = this.listSBMLs.getSelectionModel().getMinSelectionIndex();
		menu.getItem(1).setEnabled(index >= 0);
	}

	public void addModel(String model) {
		if (model.isEmpty() || this.hasModel(model))
			return;
		((DefaultListModel<String>) this.listSBMLs.getModel())
				.addElement(model);
	}

	public void removeModel(String model) {
		((DefaultListModel<String>) this.listSBMLs.getModel())
				.removeElement(model);
	}

	public boolean hasModel(String model) {
		return ((DefaultListModel<String>) this.listSBMLs.getModel())
				.contains(model);
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
