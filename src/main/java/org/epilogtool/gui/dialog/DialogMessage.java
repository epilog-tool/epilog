package org.epilogtool.gui.dialog;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class DialogMessage {
	private List<String> messages;

	public DialogMessage() {
		this.messages = new ArrayList<String>();
	}

	public void addMessage(String msg) {
		this.messages.add(msg);
	}

	public void show(String title) {
		if (this.messages.isEmpty())
			return;
		String msg = "";
		for (String m : this.messages) {
			msg += m + "\n";
		}
		this.messages.clear();
		JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
	}

	public static void showError(Component component, String title, String msg) {
		JOptionPane.showMessageDialog(component, msg, title, JOptionPane.ERROR_MESSAGE);
	}
}
