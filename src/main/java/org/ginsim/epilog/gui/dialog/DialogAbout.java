package org.ginsim.epilog.gui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;

public class DialogAbout extends EscapableDialog {
	private static final long serialVersionUID = -1433694621928539481L;

	public DialogAbout() {
		setLayout(new BorderLayout());
		JEditorPane jPane = new JEditorPane();
		this.add(jPane, BorderLayout.CENTER);
		jPane.setContentType("text/html");
		jPane.setEditable(false);
		jPane.setEnabled(true);
		jPane.setBackground(SystemColor.window);
		jPane.setText(this.getContent());
		// Bottom
		JPanel bottom = new JPanel(new FlowLayout());
		JButton buttonClose = new JButton("Close");
		buttonClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		bottom.add(buttonClose);
		bottom.setBackground(SystemColor.window);
		this.add(bottom, BorderLayout.SOUTH);
	}

	private String getContent() {
		String s = "<body><center>\n";
		s += "<h2>EpiLog</h2>\n";
		s+="</center>\n";
		s += "<p>EpiLog is a tool used for qualitative simulations ";
		s += "of <b>Epi</b>thelium <b>Log</b>ical models.<br/>\n";
		s += "It makes use of Cellular Automata to visualize the ";
		s += "evolution of the pattern formation.</p>\n";
		s += "<br/><center>\n";
		s += "<h3>Current Team</h3>\n";
		s += "<table border=0>\n";
		s += "<tr><td>Pedro L. Varela</td><td>Software development</td></tr>\n";
		s += "<tr><td>Pedro T. Monteiro</td><td>Software development</td></tr>\n";
		s += "<tr><td>Adrien Faur&eacute;</td><td>Biological applications</td></tr>\n";
		s += "<tr><td>Claudine Chaouiya</td><td>Project coordination</td></tr>\n";
		s += "</table>\n";
		s += "\n";
		s += "<h3>Previous Contributors</h3>\n";
		s += "<table border=0>\n";
		s += "<tr><td>Nuno D. Mendes</td><td>Software development</td></tr>\n";
		s += "</table>\n";
		s += "</center></body>";
		return s;
	}

	@Override
	public void focusComponentOnLoad() {
	}

}
