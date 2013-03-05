package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.io.sbml.SBMLFormat;

public class StartPanel extends JPanel {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private JButton restartButton;
	private JButton modelButton;

	private JComboBox rollOver;
	private static JTextField userDefinedWidth = new JTextField();
	private static JTextField userDefinedHeight = new JTextField();
	private JLabel selectedFilenameLabel;
	private JFileChooser fc = new JFileChooser();
	public static LogicalModel model = null;

	// private ComponentsPanel componentsPanel = new ComponentsPanel();
	private MainPanel mainPanel = null;

	public StartPanel(MainPanel mainPanel) {
		this.mainPanel = mainPanel;
		init();
		setWidth();
		setHeight();

	}

	private JPanel init() {

		restartButton = new JButton("Restart");
		modelButton = new JButton("Model");

		selectedFilenameLabel = new JLabel();

		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);

		userDefinedWidth.setPreferredSize(new Dimension(34, 26));
		userDefinedHeight.setPreferredSize(new Dimension(34, 26));

		userDefinedWidth.setHorizontalAlignment(JTextField.CENTER);
		userDefinedHeight.setHorizontalAlignment(JTextField.CENTER);
		userDefinedWidth.setText("" + mainPanel.getTopology().getWidth());
		userDefinedHeight.setText("" + mainPanel.getTopology().getHeight());

		userDefinedWidth.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				sanityCheckDimension(userDefinedWidth);
				setWidth();
			}
		});

		userDefinedHeight.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				sanityCheckDimension(userDefinedHeight);
				setHeight();
			}
		});

		restartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				mainPanel.hexagonsPanel.cellGenes.clear();
				mainPanel.hexagonsPanel.paintComponent(mainPanel.hexagonsPanel
						.getGraphics());
				mainPanel.getContentPane().repaint();

				mainPanel.optionsPanel.setVisible(false);
				mainPanel.componentsPanel.setVisible(false);
				mainPanel.textPanel.setVisible(false);
				selectedFilenameLabel.setText("");

			}
		});

		modelButton.setBounds(230, 13, 100, 30);

		selectedFilenameLabel.setBounds(335, 13, 100, 30);

		modelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				askModel();

			}

		});

		/* RollOver */

		rollOver = new JComboBox();

		rollOver.addItem("No Roll-Over");
		rollOver.addItem("Vertical Roll-Over");
		rollOver.addItem("Horizontal Roll-Over");
		rollOver.setBackground(Color.white);

		rollOver.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox source = (JComboBox) event.getSource();
				String optionString = (String) source.getSelectedItem();
				setRollOver(optionString);
				System.out.println(optionString);

			}
		});

		setLayout(new FlowLayout());
		add(userDefinedWidth);
		add(userDefinedHeight);
		add(restartButton);
		add(modelButton);
		add(selectedFilenameLabel);
		add(rollOver);

		return this;
	}

	static String roll;

	public static void setRollOver(String rollOver) {
		roll = rollOver;
	}

	public static String getRollOver() {
		return roll;
	}

	private void sanityCheckDimension(JTextField userDefined) {
		String dimString = userDefined.getText();
		int w = Integer.parseInt(dimString);
		w = (w % 2 == 0) ? w : w + 1;
		userDefined.setText("" + w);
	}

	private void setHeight() {
		mainPanel.getTopology().setHeight(
				Integer.parseInt(userDefinedHeight.getText()));

	}

	private void setWidth() {

		mainPanel.getTopology().setWidth(
				Integer.parseInt(userDefinedWidth.getText()));

	}

	private void askModel() {

		fc.setDialogTitle("Choose file");

		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			selectedFilenameLabel.setText(fc.getSelectedFile().getName());
			loadModel();

		}
	}

	private void loadModel() {

		File file = fc.getSelectedFile();
		SBMLFormat sbmlFormat = new SBMLFormat();
		LogicalModel logicalModel = null;

		try {
			logicalModel = sbmlFormat.importFile(file);
		} catch (IOException e) {
			System.err.println("Cannot import file " + file.getAbsolutePath()
					+ ": " + e.getMessage());
		}

		if (logicalModel == null)
			return;

		mainPanel.getEpithelium().setUnitaryModel(logicalModel);

		mainPanel.componentsPanel.removeAll();
		mainPanel.componentsPanel.init();
		mainPanel.getContentPane().repaint();
		mainPanel.componentsPanel.setVisible(true);
		mainPanel.optionsPanel.setVisible(true);
		mainPanel.textPanel.setVisible(true);

		setUnitaryModel(logicalModel);

	}

	public void setUnitaryModel(LogicalModel chosenmodel) {
		model = chosenmodel;
	}

	public static LogicalModel getUnitaryModel() {
		return model;
	}
}
