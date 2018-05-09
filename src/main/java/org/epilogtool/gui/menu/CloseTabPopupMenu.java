package org.epilogtool.gui.menu;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.epilogtool.common.Txt;
import org.epilogtool.gui.EpiGUI;

public class CloseTabPopupMenu extends JPopupMenu {
	private static final long serialVersionUID = 2780965787947918483L;

	public CloseTabPopupMenu() {
		JLabel jlClose = new JLabel("  Close");
		jlClose.setForeground(Color.GRAY);
		this.add(jlClose);
		
		this.addSeparator();
		
		JMenuItem activeTab = new JMenuItem(Txt.get("s_MENU_WINDOW_ACTIVE"));
		activeTab.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					EpiGUI.getInstance().epiTabCloseActiveTab();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		this.add(activeTab);

		JMenuItem otherTabs = new JMenuItem(Txt.get("s_MENU_WINDOW_O_ACTIVE"));
		otherTabs.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				EpiGUI.getInstance().epiTabCloseOtherTabs();
			}
		});
		this.add(otherTabs);

		JMenuItem closeAll = new JMenuItem(Txt.get("s_MENU_WINDOW_ALL"));
		closeAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EpiGUI.getInstance().epiTabCloseAllTabs();
			}
		});
		this.add(closeAll);

		this.addSeparator();
		
		JMenuItem closeEpiTabs = new JMenuItem(Txt.get("s_MENU_WINDOW_SEL_EPI"));
		closeEpiTabs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EpiGUI.getInstance().epiTabCloseActiveEpi(false);
			}
		});
		this.add(closeEpiTabs);

		JMenuItem closeOtherEpiTabs = new JMenuItem(Txt.get("s_MENU_WINDOW_O_SEL_EPI"));
		closeOtherEpiTabs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EpiGUI.getInstance().epiTabCloseOtherEpis();
			}
		});
		this.add(closeOtherEpiTabs);
	}
}
