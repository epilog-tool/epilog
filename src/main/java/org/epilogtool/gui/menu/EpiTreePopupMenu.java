package org.epilogtool.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.epilogtool.gui.EpiGUI;

public class EpiTreePopupMenu extends JPopupMenu {
	private static final long serialVersionUID = 1483544836367897496L;

	public EpiTreePopupMenu() {
		JMenuItem newepi = new JMenuItem("New");
		newepi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					EpiGUI.getInstance().newEpithelium();
				} catch (Exception e1) {
					// TODO: handle java reflection in the future
					e1.printStackTrace();
				}
			}
		});
		this.add(newepi);

		JMenuItem delete = new JMenuItem("Delete");
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				EpiGUI.getInstance().deleteEpithelium();
			}
		});
		this.add(delete);

		JMenuItem rename = new JMenuItem("Edit");
		rename.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					EpiGUI.getInstance().editEpithelium();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		this.add(rename);

		JMenuItem clone = new JMenuItem("Clone");
		clone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EpiGUI.getInstance().cloneEpithelium();
			}
		});
		this.add(clone);
	}
	
	public void notifySelection(boolean thisEnable, boolean itemsEnable) {
		this.getComponent(0).setEnabled(thisEnable);
		this.getComponent(1).setEnabled(itemsEnable);
		this.getComponent(2).setEnabled(itemsEnable);
		this.getComponent(3).setEnabled(itemsEnable);
	}
}
