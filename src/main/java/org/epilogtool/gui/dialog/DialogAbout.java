package org.epilogtool.gui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.epilogtool.common.Web;
import org.epilogtool.io.FileResource;

public class DialogAbout extends EscapableDialog implements HyperlinkListener {
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
		jPane.addHyperlinkListener(this);
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
		s+= "<img src='" + FileResource.getResource("logo_epilog.png") + "'/>";
		s += "<p>EpiLog is a tool used for qualitative simulations ";
		s += "of <b>Epi</b>thelium <b>Log</b>ical models.<br/>\n";
		s += "It makes use of Cellular Automata to visualize the ";
		s += "evolution of the pattern formation.</p>\n";
		s += "<a href=\"http://epilog-tool.org\">http://epilog-tool.org</a><br/>\n";
		s += "<h3>Current Team</h3>\n";
		s += "<table border=1>\n";
		s += "<tr><td rowspan=\"2\">Project coordination</td><td>Claudine Chaouiya</td></tr>\n";
		s += "<tr><td>Pedro T. Monteiro</td></tr>\n";
		s += "<tr><td>Biological applications</td><td>Adrien Faur&eacute;</td></tr>\n";
		s += "<tr><td rowspan=\"3\">Software development</td><td>Camila Veludo</td></tr>\n";
		s += "<tr><td>Pedro L. Varela</td></tr>\n";
		s += "<tr><td>Pedro T. Monteiro</td></tr>\n";
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
	public void hyperlinkUpdate(HyperlinkEvent event) {
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			Web.openURI(event.getDescription());
		}
	}

	@Override
	public void focusComponentOnLoad() {
	}
}
