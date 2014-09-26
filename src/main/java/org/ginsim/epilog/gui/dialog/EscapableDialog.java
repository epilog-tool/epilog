package org.ginsim.epilog.gui.dialog;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public abstract class EscapableDialog extends JPanel {
	private static final long serialVersionUID = -5132346812804222892L;

	public EscapableDialog() {
		Action a = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		};
		this.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "ESCAPE");
		this.getActionMap().put("ESCAPE", a);
		this.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
			}

			@Override
			public void focusGained(FocusEvent e) {
				focusComponentOnLoad();
			}
		});
	}

	protected void dispose() {
		Window win = SwingUtilities.getWindowAncestor(this);
		if (win != null) {
			win.dispose();
		}
	}

	public abstract void focusComponentOnLoad();
}
