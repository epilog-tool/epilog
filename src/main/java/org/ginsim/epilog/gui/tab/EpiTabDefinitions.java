package org.ginsim.epilog.gui.tab;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.tree.TreePath;

import org.ginsim.epilog.core.Epithelium;

public abstract class EpiTabDefinitions extends EpiTab {
	private static final long serialVersionUID = -2587480492648550086L;

	protected JPanel center;
	private JPanel south;
	private boolean changed;

	protected EpiTabDefinitions(Epithelium e, TreePath path) {
		super(e, path);
		this.changed = false;
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
		private JButton cancel;

		public ModificationsPanel() {
			this.setLayout(new FlowLayout());
			this.cancel = new JButton("Cancel");
			this.cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					buttonCancel();
				}
			});
			this.add(cancel);
			this.accept = new JButton("Accept");
			this.accept.setEnabled(false);
			this.accept.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					buttonAccept();
				}
			});
			this.add(accept);
		}
	}

	abstract protected void buttonCancel();

	abstract protected void buttonAccept();
}
