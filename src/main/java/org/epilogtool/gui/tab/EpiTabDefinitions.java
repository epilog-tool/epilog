package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.epilogtool.common.Txt;
import org.epilogtool.core.Epithelium;
import org.epilogtool.gui.EpiGUI;
import org.epilogtool.gui.EpiGUI.ProjChangeNotifyTab;
import org.epilogtool.gui.EpiGUI.TabChangeNotifyProj;

public abstract class EpiTabDefinitions extends EpiTab {
	private static final long serialVersionUID = -2587480492648550086L;

	protected JPanel center;
	private JPanel south;
	private TabChangeNotifyProj tabChanged;

	protected EpiTabDefinitions(Epithelium e, TreePath path, ProjChangeNotifyTab projChanged,
			TabChangeNotifyProj tabChanged) {
		super(e, path, projChanged);
		this.tabChanged = tabChanged;
		this.initializeGUI();
	}

	public String getName() {
		DefaultMutableTreeNode epiNode = (DefaultMutableTreeNode) this.path.getLastPathComponent();
		return epiNode.toString();
	}

	private void initializeGUI() {
		this.setLayout(new BorderLayout());
		center = new JPanel();
		this.add(center, BorderLayout.CENTER);
		south = new ModificationsPanel();
		this.add(south, BorderLayout.SOUTH);
	}

	abstract protected void buttonReset();

	abstract protected void buttonAccept();

	abstract protected boolean isChanged();

	public boolean canClose() {
		if (!this.isChanged())
			return true;
		int n = JOptionPane.showConfirmDialog(this, Txt.get("s_TAB_CLOSE"), Txt.get("s_QUESTION"),
				JOptionPane.YES_NO_OPTION);
		return (n == JOptionPane.YES_OPTION);
	}

	private class ModificationsPanel extends JPanel {
		private static final long serialVersionUID = -2133956602678321512L;

		private JButton accept;
		private JButton reset;
		private JLabel changes;

		public ModificationsPanel() {
			this.setLayout(new FlowLayout());
			this.changes = new JLabel(Txt.get("s_TAB_CHANGED"));
			this.add(changes);
			this.changes.setForeground(this.getBackground());
			this.reset = new JButton(Txt.get("s_RESET"));
			this.reset.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					buttonReset();
					buttonEnable(false);
				}
			});
			this.add(reset);
			
			//Accept button
			this.accept = new JButton(Txt.get("s_ACCEPT"));
			this.accept.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (isChanged()) {
//						System.out.println("EpiTabDefinitions: Change is pressed");
						buttonAccept();
						tabChanged.setEpiChanged();
					}
					buttonEnable(false);
				}
			});
			this.add(accept);
			// Initial state is disabled
			this.buttonEnable(false);
		}

		public void buttonEnable(boolean enable) {
			this.accept.setEnabled(enable);
			this.reset.setEnabled(enable);
			this.changes.setForeground(enable ? Color.DARK_GRAY : this.getBackground());
		}

	}

	public class TabProbablyChanged {
		public void setChanged() {
			((ModificationsPanel) south).buttonEnable(true);
		}

		public boolean isChanged() {
			return ((ModificationsPanel) south).accept.isEnabled();
		}
	}

	public void notifyChange() {
//		System.out.println("EpiTabDefinitions.notifyChange()");
		if (!this.isInitialized) {
			return;
		}
		this.applyChange();
		EpiGUI.getInstance().selectTabJTreePath(this.path);
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}