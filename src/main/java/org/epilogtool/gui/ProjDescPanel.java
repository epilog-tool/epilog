package org.epilogtool.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

public class ProjDescPanel extends JPanel {
	private static final long serialVersionUID = -8691538114476162311L;

	private static final String LABEL = "Models Loaded: ";
	private JList<String> listSBMLs;

	public ProjDescPanel() {
		this.setLayout(new BorderLayout());
		
		// PAGE_START
		JPanel jpWholeLine = new JPanel(new BorderLayout());
		JLabel jlModels = new JLabel(LABEL);
		Font font = jlModels.getFont();
		jlModels.setFont(new Font(font.getFontName(), Font.BOLD, font.getSize()));
		jpWholeLine.add(jlModels, BorderLayout.LINE_START);
		this.add(jpWholeLine, BorderLayout.PAGE_START);

		// CENTER
		ListModel<String> listModel = new DefaultListModel<String>();
		this.listSBMLs = new JList<String>(listModel);
		this.listSBMLs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scroll = new JScrollPane(this.listSBMLs);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setMinimumSize(new Dimension(0, 100));
		this.add(scroll, BorderLayout.CENTER);

	}

	public void addModel(String model) {
		if (model.isEmpty() || this.hasModel(model))
			return;
		((DefaultListModel<String>) this.listSBMLs.getModel())
				.addElement(model);
	}

	public void removeModel(String model) {
		((DefaultListModel<String>) this.listSBMLs.getModel()).removeElement(model);
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
