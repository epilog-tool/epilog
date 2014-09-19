package org.ginsim.epilog.gui.tab;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.tree.TreePath;

import org.ginsim.epilog.project.ProjectModelFeatures;
import org.ginsim.epilog.core.Epithelium;

public abstract class EpiTabDefinitions extends EpiTab {
	private static final long serialVersionUID = -2587480492648550086L;

	protected ProjectModelFeatures modelFeatures;
	protected JPanel center;
	private JPanel south;

	protected EpiTabDefinitions(Epithelium e, TreePath path,
			ProjectModelFeatures modelFeatures) {
		super(e, path);
		this.modelFeatures = modelFeatures;
		this.initializeGUI();
	}

	private void initializeGUI() {
		this.setLayout(new BorderLayout());
		center = new JPanel();
		this.add(center, BorderLayout.CENTER);
		south = new ModificationsPanel();
		this.add(south, BorderLayout.SOUTH);
	}

	private class ModificationsPanel extends JPanel {
		private static final long serialVersionUID = -2133956602678321512L;

		private JButton accept;
		private JButton reset;

		public ModificationsPanel() {
			this.setLayout(new FlowLayout());
			this.reset = new JButton("Reset");
			this.reset.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					buttonReset();
				}
			});
			this.add(reset);
			this.accept = new JButton("Accept");
			this.accept.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					buttonAccept();
				}
			});
			this.add(accept);
		}
	}

	abstract protected void buttonReset();

	abstract protected void buttonAccept();

	abstract protected boolean isChanged();

	public boolean canClose() {
		if (!this.isChanged())
			return true;
		int n = JOptionPane.showConfirmDialog(this,
				"Do you really want to close?\n"
						+ "You'll lose all modifications in this Tab!",
				"Question", JOptionPane.YES_NO_OPTION);
		return (n == JOptionPane.YES_OPTION);
	}
}
