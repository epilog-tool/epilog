package org.epilogtool.gui.dialog;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.epilogtool.common.Txt;
import org.epilogtool.core.topology.RollOver;
import org.epilogtool.gui.color.ColorUtils;
import org.epilogtool.gui.widgets.JComboImageBox;
import org.epilogtool.services.TopologyService;

public class DialogNewEpithelium extends EscapableDialog {
	private static final long serialVersionUID = 1877338344309723137L;

	private final String DEFAULT_WIDTH_STRING = "15";
	private final String DEFAULT_HEIGHT_STRING = "15";
	private final String ROLLOVER_WARNING = "This dimension must be even due to rollover selection!";

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
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		// natural height, maximum width
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(3, 3, 3, 3);

		// Width JLabel
		c.gridx = 0;
		c.gridy = 0;
		this.add(new JLabel(Txt.get("s_NEWEPI_WIDTH")), c);

		// Width JTextField
		this.jtfWidth = new JTextField(DEFAULT_WIDTH_STRING);
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
		c.gridx = 1;
		c.gridy = 0;
		this.add(this.jtfWidth, c);

		// Height JLabel
		c.gridx = 0;
		c.gridy = 1;
		this.add(new JLabel(Txt.get("s_NEWEPI_HEIGHT")), c);

		// Height JTextField
		this.jtfHeight = new JTextField(DEFAULT_HEIGHT_STRING);
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
		c.gridx = 1;
		c.gridy = 1;
		this.add(this.jtfHeight, c);

		// Name JLabel
		c.gridx = 0;
		c.gridy = 2;
		this.add(new JLabel(Txt.get("s_NEWEPI_NAME")), c);

		// Name JTextField
		this.jtfEpiName = new JTextField();
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
		c.gridx = 1;
		c.gridy = 2;
		this.add(this.jtfEpiName, c);

		// SBML JLabel
		c.gridx = 0;
		c.gridy = 3;
		this.add(new JLabel(Txt.get("s_NEWEPI_SBML")), c);

		// SBML JComboBox
		this.jcbSBMLs = new JComboBox<String>(
				this.listSBMLs.toArray(new String[this.listSBMLs.size()]));
		this.jcbSBMLs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				validateDialog();
			}
		});
		c.gridx = 1;
		c.gridy = 3;
		this.add(this.jcbSBMLs, c);

		// Rollover JLabel
		c.gridx = 0;
		c.gridy = 4;
		this.add(new JLabel(Txt.get("s_NEWEPI_ROLLOVER")), c);

		// Rollover JComboBox
		this.jcbRollover = new JComboBox<RollOver>(new RollOver[] {
				RollOver.NONE, RollOver.HORIZ, RollOver.VERT,
				RollOver.HORIZ_VERT });
		this.jcbRollover.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				validateWidth();
				validateHeight();
			}
		});
		c.gridx = 1;
		c.gridy = 4;
		this.add(this.jcbRollover, c);

		// Topology JLabel
		c.gridx = 0;
		c.gridy = 5;
		this.add(new JLabel(Txt.get("s_NEWEPI_TOPOLOGY")), c);

		// Topology JComboBox
		List<String> lDescs = new ArrayList<String>(TopologyService
				.getManager().getTopologyDescriptions());
		String[] names = new String[lDescs.size()];
		for (int i = 0; i < lDescs.size(); i++) {
			names[i] = lDescs.get(i);
		}
		this.jcbLayout = new JComboImageBox<String>(names);
		c.gridx = 1;
		c.gridy = 5;
		this.add(this.jcbLayout, c);

		// Bottom Panel
		JPanel bottom = new JPanel(new FlowLayout());
		this.buttonCancel = new JButton(Txt.get("s_CANCEL"));
		this.buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonAction(false);
			}
		});
		bottom.add(this.buttonCancel);

		this.buttonOK = new JButton(Txt.get("s_OK"));
		this.buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonAction(true);
			}
		});
		bottom.add(this.buttonOK);
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 2;
		this.add(bottom, c);

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

	public String getTopologyID() {
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
						|| (!ro.isHorizontal());
			}
		} catch (NumberFormatException nfe) {
		}
		if (this.bIsWidthOK) {
			this.jtfWidth.setBackground(Color.WHITE);
			this.jtfWidth.setToolTipText(null);
		} else {
			this.jtfWidth.setBackground(ColorUtils.LIGHT_RED);
			this.jtfWidth.setToolTipText(ROLLOVER_WARNING);
		}
		this.validateDialog();
	}

	private void validateHeight() {
		this.bIsHeightOK = false;
		try {
			this.height = Integer.parseInt(this.jtfHeight.getText());
			if (this.height >= 1) {
				RollOver ro = (RollOver) this.jcbRollover.getSelectedItem();
				this.bIsHeightOK = (this.height % 2 == 0)
						|| (!ro.isVertical());
			}
		} catch (NumberFormatException nfe) {
		}
		if (this.bIsHeightOK) {
			this.jtfHeight.setBackground(Color.WHITE);
			this.jtfHeight.setToolTipText(null);
		} else {
			this.jtfHeight.setBackground(ColorUtils.LIGHT_RED);
			this.jtfHeight.setToolTipText(ROLLOVER_WARNING);
		}

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
