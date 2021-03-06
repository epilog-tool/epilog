package org.epilogtool.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.epilogtool.common.Txt;
import org.epilogtool.gui.EpiGUI;
import org.epilogtool.project.Project;

public class EpiTreePopupMenu extends JPopupMenu {
	private static final long serialVersionUID = 1483544836367897496L;

	public EpiTreePopupMenu() {
		JMenuItem newepi = new JMenuItem(Txt.get("s_MENU_EPI_NEW"));
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

		JMenuItem delete = new JMenuItem(Txt.get("s_MENU_EPI_DEL"));
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				EpiGUI.getInstance().deleteEpithelium();
			}
		});
		this.add(delete);

		JMenuItem edit = new JMenuItem(Txt.get("s_MENU_EPI_EDIT"));
		edit.addActionListener(new ActionListener() {
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
		this.add(edit);

		JMenuItem clone = new JMenuItem(Txt.get("s_MENU_EPI_CLONE"));
		clone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EpiGUI.getInstance().cloneEpithelium();
			}
		});
		this.add(clone);
	}
	
	public void notifySelection(boolean thisEnable, boolean itemsEnable) {
		//TODO: getitem(0) should only be enabled if there is at least one SBML
		boolean hasmodel = false;
		if (Project.getInstance().getModelNames().size()>0) hasmodel = true;
		this.getComponent(0).setEnabled(hasmodel);
		this.getComponent(1).setEnabled(itemsEnable);
		this.getComponent(2).setEnabled(itemsEnable);
		this.getComponent(3).setEnabled(itemsEnable);
	}
}
