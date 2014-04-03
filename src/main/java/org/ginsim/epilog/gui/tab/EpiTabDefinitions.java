package org.ginsim.epilog.gui.tab;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.tree.TreePath;

import org.ginsim.epilog.core.Epithelium;

public abstract class EpiTabDefinitions extends EpiTab {
	protected JPanel center;
	private JPanel south;
	private boolean changed;

	protected EpiTabDefinitions(Epithelium e, TreePath path) {
		super(e, path);
		this.changed = false;
		this.initializeGUI();
	}

	private void initializeGUI() {
		center = new JPanel();
		south = new ModificationsPanel();
		this.setLayout(new BorderLayout());
		this.add(center, BorderLayout.CENTER);
		this.add(south, BorderLayout.SOUTH);
	}

	private class ModificationsPanel extends JPanel {
		private JButton accept;
		private JButton cancel;

		public ModificationsPanel() {
			this.setLayout(new FlowLayout());
			this.accept = new JButton("Accept");
			this.cancel = new JButton("Cancel");
			this.add(accept);
			this.add(cancel);
		}
	}
}
