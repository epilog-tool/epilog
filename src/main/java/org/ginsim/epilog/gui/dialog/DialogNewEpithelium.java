package org.ginsim.epilog.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.ginsim.epilog.core.topology.RollOver;
import org.ginsim.epilog.gui.color.ColorUtils;
import org.ginsim.epilog.services.TopologyService;

public class DialogNewEpithelium extends EscapableDialog {
	private static final long serialVersionUID = 1877338344309723137L;

	private final int DEFAULT_WIDTH = 20;

	private final String DEFAULT_WIDTH_STRING = "20";
	private final String DEFAULT_HEIGHT_STRING = "20";
	
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
	private boolean bIsOK;

	public DialogNewEpithelium(Set<String> sSBMLs, List<String> lEpiNames) {
		this.listSBMLs = new ArrayList<String>(sSBMLs);
		this.listEpiNames = lEpiNames;
		setLayout(new BorderLayout());
		
		// PAGE_START begin
		JPanel top = new JPanel(new FlowLayout());
		top.add(new JLabel("Width:"));
		jtfWidth = new JTextField(DEFAULT_WIDTH_STRING, 3);
		jtfWidth.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				validateTextFieldDimensions(e);
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		jtfWidth.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				validateEvenDimension(e);
				System.out.println(e);
			}

			@Override
			public void focusGained(FocusEvent e) {
			}
		});
		top.add(jtfWidth);
		top.add(new JLabel("Height:"));
		jtfHeight = new JTextField(DEFAULT_HEIGHT_STRING, 3);
		jtfHeight.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				validateTextFieldDimensions(e);
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		jtfHeight.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				validateEvenDimension(e);
			}

			@Override
			public void focusGained(FocusEvent e) {
			}
		});
		top.add(jtfHeight);
		top.add(new JLabel("Topology:"));
		DefaultComboBoxModel<String> cbModel = new DefaultComboBoxModel<String>();
		// Dynamically loads Topologies in the classpath
		for (String topID : TopologyService.getManager()
				.getTopologyDescriptions()) {
			cbModel.addElement(topID);
		}
		jcbLayout = new JComboBox<String>(cbModel);
		top.add(jcbLayout);
		this.add(top, BorderLayout.PAGE_START);
		
		//CENTER
		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		
		
		// Name chooser
		JPanel jpName = new JPanel(new BorderLayout());
		jpName.add(new JLabel("Name    "), BorderLayout.LINE_START);
		jtfEpiName = new JTextField(DEFAULT_WIDTH);
		jtfEpiName.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				validateDialog();
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		jpName.add(jtfEpiName, BorderLayout.CENTER);
		center.add(jpName);

		
		
		// SBML chooser
		JPanel jpSBML = new JPanel(new BorderLayout());
		jpSBML.add(new JLabel("SBML     "), BorderLayout.LINE_START);
		jcbSBMLs = new JComboBox<String>(
				this.listSBMLs.toArray(new String[this.listSBMLs.size()]));
		jcbSBMLs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				validateDialog();
			}
		});
		jpSBML.add(jcbSBMLs, BorderLayout.CENTER);
		center.add(jpSBML);

		// Rollover chooser
		JPanel jpRollover = new JPanel(new BorderLayout());
		jpRollover.add(new JLabel("Rollover"), BorderLayout.LINE_START);
		jcbRollover = new JComboBox<RollOver>(new RollOver[] {
				RollOver.NOROLLOVER, RollOver.HORIZONTAL, RollOver.VERTICAL });
		jpRollover.add(jcbRollover, BorderLayout.CENTER);
		center.add(jpRollover);
		this.add(center, BorderLayout.CENTER);

		// PAGE_END begin
		JPanel bottom = new JPanel(new FlowLayout());
		buttonCancel = new JButton("Cancel");
		buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonAction(false);
			}
		});
		bottom.add(buttonCancel);
		
		buttonOK = new JButton("OK");
		buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonAction(true);
			}
		});
		bottom.add(buttonOK);
		this.add(bottom,BorderLayout.PAGE_END);
		this.validateDialog();
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
		System.out.println(jtfWidth.getText());
		return Integer.parseInt(jtfWidth.getText());
	}

	public int getEpitheliumHeight() {
		return Integer.parseInt(jtfHeight.getText());
	}

	public String getTopologyLayout() {
		String desc = (String) this.jcbLayout.getSelectedItem();
		return TopologyService.getManager().getTopologyID(desc);
	}

	private boolean validateTextField() {
		boolean valid = false;
		if (!this.listEpiNames.contains(this.jtfEpiName.getText())
				&& !this.jtfEpiName.getText().trim().isEmpty()) {
			valid = true;
		}
		if (!valid) {
			this.jtfEpiName.setBackground(ColorUtils.LIGHT_RED);
		} else {
			this.jtfEpiName.setBackground(Color.WHITE);
		}
		return valid;
	}
	
	private void validateTextFieldDimensions(KeyEvent e) {
		JTextField jtf = (JTextField) e.getSource();
		boolean valid = false;
		try {
			int x = Integer.parseInt(jtf.getText());
			if (x >= 1) {
				valid = true;
			}
		} catch (NumberFormatException nfe) {
		}
		if (!valid) {
			jtf.setBackground(ColorUtils.LIGHT_RED);
		} else {
			jtf.setBackground(Color.WHITE);
		}
		this.validateDialog();
	}

	private void validateEvenDimension(FocusEvent e) {
		JTextField jtf = (JTextField) e.getSource();
		try {
			int x = Integer.parseInt(jtf.getText());
			if (x % 2 == 1) {
				jtf.setText("" + (x + 1));
			}
		} catch (NumberFormatException nfe) {
		}
	}
	
	private boolean validateComboBox() {
		return true;
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
		boolean isValid = false;
		if (this.validateTextField() && this.validateComboBox()) {
			isValid = true;
		}
		this.buttonOK.setEnabled(isValid);

		try {
			this.width = Integer.parseInt(this.jtfWidth.getText());
			this.height = Integer.parseInt(this.jtfHeight.getText());
		} catch (NumberFormatException nfe) {
			isValid = false;
		}
		this.buttonOK.setEnabled(isValid);
		return isValid;
	}
	
	@Override
	public void focusComponentOnLoad() {
		this.jtfEpiName.requestFocusInWindow();
	}
	
	//ATENTION
//	@Override
//	public void focusComponentOnLoad() {
//		this.jtfWidth.requestFocusInWindow();
//	}
}
