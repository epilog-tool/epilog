package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

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
	
	private JButton closeButton;
	private JButton modelButton;
	private JButton initialConditionsButton;
	private JButton restartButton;
	private JButton runButton;
	private JButton stepButton;
	private JButton quitButton;
	private IterationLabel iterationLabel;

	JLabel labelFilename = new JLabel();
	JLabel iterationNumber = new JLabel();

	private JComboBox rollOver;
	private static JTextField userDefinedWidth = new JTextField();
	private static JTextField userDefinedHeight = new JTextField();
	private JLabel selectedFilenameLabel;
	private JFileChooser fc = new JFileChooser();
	private LogicalModel model;

	// private ComponentsPanel componentsPanel = new ComponentsPanel();
	private MainPanel mainPanel = null;

	public StartPanel(MainPanel mainPanel) {
		this.mainPanel = mainPanel;
		init();
		setWidth();
		setHeight();

	}

	private JPanel init() {

		// Close and Quit buttons

		
		modelButton = new JButton("Model");
		closeButton = new JButton("Close");

		quitButton = new JButton("Quit");

		quitButton.setBackground(Color.red);
		add(quitButton);
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainPanel.dispose();
			}
		});

		closeButton.setVisible(false);
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		
				mainPanel.getSimulation().resetIterationNumber();
				iterationNumber.setText(""
						+ mainPanel.getSimulation().getIterationNumber());
				mainPanel.hexagonsPanel.paintComponent(mainPanel.hexagonsPanel
						.getGraphics());
				mainPanel.getSimulation().setHasInitiated(false);
				
				mainPanel.componentsPanel.setVisible(false);
				mainPanel.watcherPanel.setVisible(false);
				selectedFilenameLabel.setText("");
				mainPanel.componentsPanel.setVisible(false);
				mainPanel.watcherPanel.setVisible(false);
				labelFilename.setVisible(false);
				stepButton.setVisible(false);
				runButton.setVisible(false);
				stepButton.setVisible(false);
				initialConditionsButton.setVisible(false);
				iterationLabel.setVisible(false);
				iterationNumber.setVisible(false);
				restartButton.setVisible(false);
				closeButton.setVisible(false);

			}
		});
		
		restartButton = new JButton("Restart");
		restartButton.setVisible(false);
		restartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainPanel.getSimulation().resetIterationNumber();
				iterationNumber.setText(""
						+ mainPanel.getSimulation().getIterationNumber());
				mainPanel.hexagonsPanel.paintComponent(mainPanel.hexagonsPanel
						.getGraphics());
				
			}	});

		// Dimensions

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
		
		//To make sure that the height is updated
		addMouseListener (new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				sanityCheckDimension(userDefinedWidth);
				sanityCheckDimension(userDefinedHeight);
				setWidth();
				setHeight();
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				sanityCheckDimension(userDefinedWidth);
				sanityCheckDimension(userDefinedHeight);
				setWidth();
				setHeight();
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});

		// Model

		modelButton.setBounds(230, 13, 100, 30);
		

		selectedFilenameLabel.setBounds(335, 13, 100, 30);

		modelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				askModel();
				mainPanel.getContentPane().repaint();
				mainPanel.getSimulation().resetIterationNumber();
				iterationNumber.setText(""
						+ mainPanel.getSimulation().getIterationNumber());
				closeButton.setVisible(true);
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

		/* Options */

		runButton = new RunStopButton();
		stepButton = new JButton("Step");
		iterationLabel = new IterationLabel();
		initialConditionsButton = new JButton("Initial Conditions");

		// Step Button
		stepButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Simulation.step();
				System.out.println("step");
				mainPanel.getSimulation().step();
				iterationNumber.setText(""
						+ mainPanel.getSimulation().getIterationNumber());
			}
		});

		// Run Button
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Simulation.step();

				mainPanel.getSimulation().run();
				iterationNumber.setText(""
						+ mainPanel.getSimulation().getIterationNumber());
			}
		});
		
		stepButton.setVisible(false);
		runButton.setVisible(false);
		iterationLabel.setVisible(false);
		initialConditionsButton.setVisible(false);

		setLayout(new FlowLayout());

		JLabel setWidth = new JLabel();
		JLabel setHeight = new JLabel();
		JLabel emptySpaceLabel = new JLabel();

		emptySpaceLabel.setText("    ");

		setWidth.setText("Width: ");
		setHeight.setText("Height: ");
		labelFilename.setText("Filename: ");
		iterationNumber.setText(""
				+ mainPanel.getSimulation().getIterationNumber());
		iterationNumber.setVisible(false);
		labelFilename.setVisible(false);
		iterationLabel.setText("Iteration:");
		iterationLabel.setVisible(false);
		

		add(setWidth);
		add(userDefinedWidth);
		add(setHeight);
		add(userDefinedHeight);
		add(rollOver);

		add(modelButton);

		add(labelFilename);
		add(selectedFilenameLabel);
		add(runButton);
		add(stepButton);
		add(iterationLabel);
		add(iterationNumber);
		add(initialConditionsButton);
		add(emptySpaceLabel);
		add(restartButton);

		add(closeButton);
		add(quitButton);

		initialConditionsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final InitialConditions initialConditionsPanel = new InitialConditions(
						mainPanel);
				initialConditionsPanel.initialize();
				initialConditionsPanel
						.initializeCells(initialConditionsPanel.cells);

			}
		});

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
		mainPanel.repaint();
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
		mainPanel.watcherPanel.setVisible(true);
		labelFilename.setVisible(true);
		stepButton.setVisible(true);
		runButton.setVisible(true);
		stepButton.setVisible(true);
		initialConditionsButton.setVisible(true);
		iterationLabel.setVisible(true);
		iterationNumber.setVisible(true);
		restartButton.setVisible(true);

		setUnitaryModel(logicalModel);

	}

	public void setUnitaryModel(LogicalModel chosenmodel) {
		model = chosenmodel;
	}

	public LogicalModel getUnitaryModel() {
		return this.model;
	}
}
