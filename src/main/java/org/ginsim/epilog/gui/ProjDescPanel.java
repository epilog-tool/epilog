package org.ginsim.epilog.gui;

import java.awt.BorderLayout;
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

	private static final String LABEL = "Dimensions: ";
	private JLabel jlDimensions;
	private JList<String> listSBMLs;

	public ProjDescPanel() {
		// PAGE_START
		JPanel jpWholeLine = new JPanel(new BorderLayout());
		JLabel jlDim = new JLabel(LABEL);
		Font font = jlDim.getFont();
		jlDim.setFont(new Font(font.getFontName(), Font.BOLD, font.getSize()));
		jpWholeLine.add(jlDim, BorderLayout.LINE_START);
		this.jlDimensions = new JLabel();
		jpWholeLine.add(this.jlDimensions, BorderLayout.CENTER);
		this.add(jpWholeLine, BorderLayout.PAGE_START);

		// CENTER
		ListModel<String> listModel = new DefaultListModel<String>();
		this.listSBMLs = new JList<String>(listModel);
		this.listSBMLs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scroll = new JScrollPane(this.listSBMLs);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.add(scroll, BorderLayout.CENTER);

		this.clean();
	}

	public void setDimension(int w, int h) {
		this.jlDimensions.setText(w + " x " + h);
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

	public void clean() {
		this.jlDimensions.setText("? x ?");
		((DefaultListModel<String>) this.listSBMLs.getModel()).clear();
	}

	public String getSelected() {
		return this.listSBMLs.getSelectedValue();
	}

}
