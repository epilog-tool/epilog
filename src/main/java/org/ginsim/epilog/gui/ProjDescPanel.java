package org.ginsim.epilog.gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class ProjDescPanel extends JPanel {
	private static final long serialVersionUID = -8691538114476162311L;
	
	private static final String LABEL = "Dimensions: ";
	private JLabel jlDimensions;
	private DefaultListModel<String> listModel;

	public ProjDescPanel() {
		JPanel jpWholeLine = new JPanel(new BorderLayout());

		JLabel jlDim = new JLabel(LABEL);
		Font font = jlDim.getFont();
		jlDim.setFont(new Font(font.getFontName(), Font.BOLD, font.getSize()));
		jpWholeLine.add(jlDim, BorderLayout.LINE_START);

		this.jlDimensions = new JLabel();
		jpWholeLine.add(this.jlDimensions, BorderLayout.CENTER);

		this.add(jpWholeLine, BorderLayout.PAGE_START);

		this.listModel = new DefaultListModel<String>();
		JList<String> list = new JList<String>(this.listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scroll = new JScrollPane(list);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//		list.setSize(list.getMaximumSize());
		this.add(scroll, BorderLayout.CENTER);
		this.clean();
	}

	public void setDimension(int w, int h) {
		this.jlDimensions.setText(w + " x " + h);
	}

	public void addModel(String model) {
		if (model.isEmpty() || this.listModel.contains(model))
			return;
		this.listModel.addElement(model);
	}

	public void delModel(String model) {
		if (this.listModel.contains(model)) {
			this.listModel.removeElement(model);
		}
	}
	
	public void clean() {
		this.jlDimensions.setText("? x ?");
		this.listModel.clear();
	}
}
