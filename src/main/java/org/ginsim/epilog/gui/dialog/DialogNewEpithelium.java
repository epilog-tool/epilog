package org.ginsim.epilog.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.ginsim.epilog.core.topology.RollOver;
import org.ginsim.epilog.gui.color.ColorUtils;
import org.ginsim.epilog.gui.widgets.JComboImageBox;
import org.ginsim.epilog.io.FileResource;
import org.ginsim.epilog.services.TopologyService;

public class DialogNewEpithelium extends EscapableDialog {
	private static final long serialVersionUID = 1877338344309723137L;

	private final int DEFAULT_JLABEL_WIDTH = 20;

	private final String DEFAULT_WIDTH_STRING = "15";
	private final String DEFAULT_HEIGHT_STRING = "15";

	private JTextField jtfWidth;
	private JTextField jtfHeight;
	private JComboBox<String> jcbLayout;
	private JTextField jtfEpiName;
	private JComboBox<String> jcbSBMLs;
	private List<String> listSBMLs;
	private List<String> listEpiNames;
	private JButton buttonCancel;
	private JButton buttonOK;
	private JComboBox<RollOver> jcbRollover;

	private int width;
	private int height;
	private boolean bIsNameOK;
	private boolean bIsWidthOK;
	private boolean bIsHeightOK;
	private boolean bIsOK;

	public DialogNewEpithelium(Set<String> sSBMLs, List<String> lEpiNames) {
		this.listSBMLs = new ArrayList<String>(sSBMLs);
		this.listEpiNames = lEpiNames;
		this.setLayout(new BorderLayout());

		// PAGE_START begin
		JPanel top = new JPanel(new FlowLayout());
		top.add(new JLabel("Width:"));
		this.jtfWidth = new JTextField(DEFAULT_WIDTH_STRING, 3);
		this.jtfWidth.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				validateWidth();
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		top.add(this.jtfWidth);
		top.add(new JLabel("Height:"));
		this.jtfHeight = new JTextField(DEFAULT_HEIGHT_STRING, 3);
		this.jtfHeight.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				validateHeight();
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		top.add(this.jtfHeight);
		top.add(new JLabel("Topology:"));
		List<String> lDescs = new ArrayList<String>(TopologyService
				.getManager().getTopologyDescriptions());
		String[] names = new String[lDescs.size()];
		for (int i = 0; i < lDescs.size(); i++) {
			names[i] = lDescs.get(i);
		}
		this.jcbLayout = new JComboImageBox(names);
		top.add(this.jcbLayout);
		this.add(top, BorderLayout.PAGE_START);
		
		// CENTER
		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

		// Name chooser
		JPanel jpName = new JPanel(new BorderLayout());
		jpName.add(new JLabel("Name    "), BorderLayout.LINE_START);
		this.jtfEpiName = new JTextField(DEFAULT_JLABEL_WIDTH);
		this.jtfEpiName.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				validateTextField();
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		jpName.add(this.jtfEpiName, BorderLayout.CENTER);
		center.add(jpName);

		// SBML chooser
		JPanel jpSBML = new JPanel(new BorderLayout());
		jpSBML.add(new JLabel("SBML     "), BorderLayout.LINE_START);
		this.jcbSBMLs = new JComboBox<String>(
				this.listSBMLs.toArray(new String[this.listSBMLs.size()]));
		this.jcbSBMLs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				validateDialog();
			}
		});
		jpSBML.add(this.jcbSBMLs, BorderLayout.CENTER);
		center.add(jpSBML);

		// Rollover chooser
		JPanel jpRollover = new JPanel(new BorderLayout());
		jpRollover.add(new JLabel("Rollover"), BorderLayout.LINE_START);
		this.jcbRollover = new JComboBox<RollOver>(new RollOver[] {
				RollOver.NOROLLOVER, RollOver.HORIZONTAL, RollOver.VERTICAL });
		this.jcbRollover.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				validateWidth();
				validateHeight();
			}
		});
		jpRollover.add(this.jcbRollover, BorderLayout.CENTER);
		center.add(jpRollover);
		this.add(center, BorderLayout.CENTER);

		// PAGE_END begin
		JPanel bottom = new JPanel(new FlowLayout());
		this.buttonCancel = new JButton("Cancel");
		this.buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonAction(false);
			}
		});
		bottom.add(this.buttonCancel);

		this.buttonOK = new JButton("OK");
		this.buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonAction(true);
			}
		});
		bottom.add(this.buttonOK);
		this.add(bottom, BorderLayout.PAGE_END);
		this.validateTextField();
		this.validateWidth();
		this.validateHeight();
	}

	public String getEpiName() {
		return this.jtfEpiName.getText();
	}

	public String getSBMLName() {
		return (String) this.jcbSBMLs.getSelectedItem();
	}

	public RollOver getRollOver() {
		return (RollOver) this.jcbRollover.getSelectedItem();
	}

	public int getEpitheliumWidth() {
		return this.width;
	}

	public int getEpitheliumHeight() {
		return this.height;
	}

	public String getTopologyLayout() {
		String desc = (String) this.jcbLayout.getSelectedItem();
		return TopologyService.getManager().getTopologyID(desc);
	}

	private void validateTextField() {
		this.bIsNameOK = false;
		if (!this.listEpiNames.contains(this.jtfEpiName.getText())
				&& !this.jtfEpiName.getText().isEmpty()
				&& !this.jtfEpiName.getText().matches(".*(\\s).*")) {
			this.bIsNameOK = true;
		}
		this.jtfEpiName.setBackground(this.bIsNameOK ? Color.WHITE
				: ColorUtils.LIGHT_RED);
		this.validateDialog();
	}

	private void validateWidth() {
		this.bIsWidthOK = false;
		try {
			this.width = Integer.parseInt(this.jtfWidth.getText());
			if (this.width >= 1) {
				RollOver ro = (RollOver) this.jcbRollover.getSelectedItem();
				this.bIsWidthOK = (this.width % 2 == 0)
						|| (ro != RollOver.HORIZONTAL);
			}
		} catch (NumberFormatException nfe) {
		}
		this.jtfWidth.setBackground(this.bIsWidthOK ? Color.WHITE
				: ColorUtils.LIGHT_RED);
		this.validateDialog();
	}

	private void validateHeight() {
		this.bIsHeightOK = false;
		try {
			this.height = Integer.parseInt(this.jtfHeight.getText());
			if (this.height >= 1) {
				RollOver ro = (RollOver) this.jcbRollover.getSelectedItem();
				this.bIsHeightOK = (this.height % 2 == 0)
						|| (ro != RollOver.VERTICAL);
			}
		} catch (NumberFormatException nfe) {
		}
		this.jtfHeight.setBackground(this.bIsHeightOK ? Color.WHITE
				: ColorUtils.LIGHT_RED);
		this.validateDialog();
	}

	private void buttonAction(boolean bIsOK) {
		this.bIsOK = bIsOK;
		if (bIsOK && !this.validateDialog()) {
			return;
		}
		this.jtfEpiName.setText(this.jtfEpiName.getText().trim());
		this.dispose();
	}

	public boolean isDefined() {
		return this.bIsOK;
	}

	private boolean validateDialog() {
		boolean isValid = bIsNameOK && bIsWidthOK && bIsHeightOK;
		this.buttonOK.setEnabled(isValid);
		return isValid;
	}

	@Override
	public void focusComponentOnLoad() {
		this.jtfEpiName.requestFocusInWindow();
	}
}
