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
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.epilogtool.common.Web;
import org.epilogtool.gui.EpiGUI;
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
		jPane.addHyperlinkListener(this);

		// Stylesheet
		HTMLEditorKit kit = new HTMLEditorKit();
		jPane.setEditorKit(kit);
		StyleSheet styleSheet = kit.getStyleSheet();
		styleSheet.addRule("td {text-align: center;}");
		Document doc = kit.createDefaultDocument();
		jPane.setDocument(doc);

		// Content
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
		s += "<img src='" + FileResource.getResource("logo_epilog.png") + "'/>";
		s += "<p>EpiLog (" + EpiGUI.getInstance().getVersion() + ") is a tool used for qualitative simulations ";
		s += "of <b>Epi</b>thelium <b>Log</b>ical models.<br/>\n";
		s += "It makes use of Cellular Automata to visualize the ";
		s += "evolution of the pattern formation.</p>\n";
		s += "<a href=\"http://epilog-tool.org\">http://epilog-tool.org</a><br/>\n";
		s += "<br/>\n";
		s += "<table border=\"1\" >\n";
		s += "<tr><td colspan=\"4\"><b>Current Team</h2></b></tr>";
		s += "<tr><td></td><td>Project<br/>coordination</td><td>Software<br/>development</td><td>Biological<br/>applications</td></tr>\n";
		s += "<tr><td>Claudine Chaouiya</td><td>&#10003;</td><td></td><td></td></tr>\n";
		s += "<tr><td>Pedro T. Monteiro</td><td>&#10003;</td><td>&#10003;</td><td></td></tr>\n";
		s += "<tr><td>Pedro L. Varela</td><td></td><td>&#10003;</td><td></td></tr>\n";
		s += "<tr><td>Camila Veludo</td><td></td><td>&#10003;</td><td>&#10003;</td></tr>\n";

		s += "<tr><td colspan=\"4\"><b>Past Contributors</b></td></tr>";
		s += "<tr><td>Adrien Faur&eacute;</td><td></td><td></td><td>&#10003;</td></tr>\n";
		s += "<tr><td>Nuno D. Mendes</td><td></td><td>&#10003;</td><td></td></tr>\n";
		s += "</table>\n";
		s += "\n";
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
