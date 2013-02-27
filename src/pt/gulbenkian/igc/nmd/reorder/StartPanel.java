package pt.gulbenkian.igc.nmd.reorder;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.UIManager;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.io.sbml.SBMLFormat;

import composition.IntegrationFunctionMapping;
import composition.Topology;

public class StartPanel extends JPanel{

	private static int DEFAULT_WIDTH = 6;
	private static int DEFAULT_HEIGHT = 6;

	private  JTextField userDefinedWidth = new JTextField();
	private  JTextField userDefinedHeight = new JTextField();

	public JButton restartButton = new JButton();
	public JButton modelButton = new JButton();
	public JButton closeButton = new JButton();

	public StartPanel(){
		
	}
	
	
	public void initialize() {

		userDefinedWidth.setHorizontalAlignment(JTextField.CENTER);
		userDefinedHeight.setHorizontalAlignment(JTextField.CENTER);
		userDefinedWidth.setText("" + DEFAULT_WIDTH);
		userDefinedHeight.setText("" + DEFAULT_HEIGHT);

		userDefinedWidth.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent arg0) {
			}

			public void focusLost(FocusEvent arg0) {
				sanityCheckDimension(userDefinedWidth);
			}
		});

		userDefinedHeight.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent arg0) {
			}

			public void focusLost(FocusEvent arg0) {
				sanityCheckDimension(userDefinedHeight);
			}
		});
	}

	private void sanityCheckDimension(JTextField userDefined) {
		String dimString = userDefined.getText();
		int w = Integer.parseInt(dimString);
		w = (w % 2 == 0) ? w : w + 1;
		userDefined.setText("" + w);
	}

	public int getGridWidth() {
		// sanityCheckDimension(userDefinedWidth);
		return Integer.parseInt(userDefinedWidth.getText());
	}

	public int getGridHeight() {
		// sanityCheckDimension(userDefinedHeight);
		return Integer.parseInt(userDefinedHeight.getText());
	}

}
