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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.epilogtool.core.Epithelium;
import org.epilogtool.core.topology.RollOver;
import org.epilogtool.gui.color.ColorUtils;
import org.epilogtool.gui.widgets.JComboImageBox;
import org.epilogtool.services.TopologyService;

public class DialogEditEpithelium extends EscapableDialog {
	private static final long serialVersionUID = 1877338344309723137L;

	// private final int COL_SIZE = 20;
	private final String ROLLOVER_WARNING = "This dimension must be even due to rollover selection!";

	private JTextField jtfEpiName;
	private JTextField jtfWidth;
	private JTextField jtfHeight;
	private JComboBox<RollOver> jcbRollover;
	private JComboBox<String> jcbLayout;

	private List<String> listEpiNames;
	private int width;
	private int height;
	private boolean bIsOK;
	private boolean bIsNameOK;
	private boolean bIsWidthOK;
	private boolean bIsHeightOK;

	private JButton buttonCancel;
	private JButton buttonOK;

	public DialogEditEpithelium(Epithelium epi, List<String> lEpiNames) {
		this.listEpiNames = lEpiNames;
		this.listEpiNames.remove(epi.getName());

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		// natural height, maximum width
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(3, 3, 3, 3);

		// Width JLabel
		c.gridx = 0;
		c.gridy = 0;
		this.add(new JLabel("Width:"), c);
		// Width JTextField
		this.jtfWidth = new JTextField("" + epi.getX());
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
		this.add(new JLabel("Height:"), c);

		// Height JTextField
		this.jtfHeight = new JTextField("" + epi.getY());
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
		this.add(new JLabel("Name:"), c);

		// Name JTextField
		this.jtfEpiName = new JTextField(epi.getName());
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

		// Rollover JLabel
		c.gridx = 0;
		c.gridy = 4;
		this.add(new JLabel("Rollover:"), c);

		// Rollover JComboBox
		this.jcbRollover = new JComboBox<RollOver>(
				new RollOver[] { RollOver.NONE, RollOver.HORIZ, RollOver.VERT, RollOver.HORIZ_VERT });
		this.jcbRollover.setSelectedItem(epi.getEpitheliumGrid().getTopology().getRollOver());
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
		this.add(new JLabel("Topology:"), c);

		// Topology JComboBox
		int iSelected = 0;
		List<String> lDescs = new ArrayList<String>(TopologyService.getManager().getTopologyDescriptions());
		String[] names = new String[lDescs.size()];
		for (int i = 0; i < lDescs.size(); i++) {
			if (lDescs.get(i).equals(epi.getEpitheliumGrid().getTopology().getDescription())) {
				iSelected = i;
			}
			names[i] = lDescs.get(i);
		}
		this.jcbLayout = new JComboImageBox<String>(names);
		this.jcbLayout.setSelectedIndex(iSelected);
		c.gridx = 1;
		c.gridy = 5;
		this.add(this.jcbLayout, c);

		// Bottom Panel
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
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 2;
		this.add(bottom, c);

		this.validateTextField();
		this.validateWidth();
		this.validateHeight();
	}

	private void validateWidth() {
		this.bIsWidthOK = false;
		try {
			this.width = Integer.parseInt(this.jtfWidth.getText());
			if (this.width >= 1) {
				RollOver ro = (RollOver) this.jcbRollover.getSelectedItem();
				this.bIsWidthOK = (this.width % 2 == 0) || (!ro.isHorizontal());
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
				this.bIsHeightOK = (this.height % 2 == 0) || (!ro.isVertical());
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

	private void validateTextField() {
		this.bIsNameOK = false;
		if (!this.listEpiNames.contains(this.jtfEpiName.getText()) && !this.jtfEpiName.getText().isEmpty()
				&& !this.jtfEpiName.getText().matches(".*(\\s).*")) {
			this.bIsNameOK = true;
		}
		this.jtfEpiName.setBackground(this.bIsNameOK ? Color.WHITE : ColorUtils.LIGHT_RED);
		this.validateDialog();
	}

	public String getEpitheliumName() {
		return this.jtfEpiName.getText();
	}

	public RollOver getRollOver() {
		return (RollOver) this.jcbRollover.getSelectedItem();
	}

	public String getTopologyID() {
		String desc = (String) this.jcbLayout.getSelectedItem();
		return TopologyService.getManager().getTopologyID(desc);
	}

	public int getGridX() {
		return this.width;
	}

	public int getGridY() {
		return this.height;
	}

	private void buttonAction(boolean bIsOK) {
		this.bIsOK = bIsOK;
		if (bIsOK && !this.validateDialog()) {
			// Not valid
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
