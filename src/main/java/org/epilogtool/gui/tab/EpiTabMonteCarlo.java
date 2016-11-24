package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.epilogtool.common.ObjectComparator;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.gui.EpiGUI;
import org.epilogtool.gui.EpiGUI.ProjChangeNotifyTab;
import org.epilogtool.gui.EpiGUI.SimulationEpiClone;
import org.epilogtool.gui.color.ColorUtils;
import org.epilogtool.gui.widgets.GridInformation;
import org.epilogtool.gui.widgets.JComboCheckBox;
import org.epilogtool.gui.widgets.SimulationInformation;
import org.epilogtool.gui.widgets.VisualGridSimulation;
import org.epilogtool.io.ButtonFactory;
import org.epilogtool.io.EpilogFileFilter;
import org.epilogtool.io.FileIO;
import org.epilogtool.project.ProjectFeatures;
import org.epilogtool.project.Simulation;

public class EpiTabMonteCarlo extends EpiTabTools {
	private static final long serialVersionUID = 1394895739386499680L;
	
	private static int WIDTH = 145;
	private int numberRuns;
	private int maxNumberIterations;

	public EpiTabMonteCarlo(Epithelium e, TreePath path,
			ProjChangeNotifyTab projChanged, ProjectFeatures projectFeatures,
			SimulationEpiClone simEpiClone) {
		super(e, path, projChanged);
	}

	public void initialize() {
		setLayout(new BorderLayout());
		
		JPanel monteCarloDefinitions = createMonteCarloDefinitions();
		this.add(monteCarloDefinitions,BorderLayout.PAGE_START);
		
		
	}

	private JPanel createMonteCarloDefinitions() {

		JPanel monteCarloDefinitions = new JPanel();
		monteCarloDefinitions.setBorder(BorderFactory.createTitledBorder("MonteCarlo Definitions"));
		
		JButton jbRun = new JButton("Run");
		jbRun.setToolTipText("Run the Monte Carlo ");
		jbRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO run 
			}
		});
		
		JPanel jpRunNum = new JPanel();
		
		jpRunNum.add(new JLabel("Number of Simulations "));
		
		JTextField jtfNumRuns = new JTextField("100");
		jtfNumRuns.setToolTipText("Insert number of simulations");
		jtfNumRuns.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO run 
			}
		});
		
		jpRunNum.add(jtfNumRuns);

		//TODO Maximum Number of Iterations

		monteCarloDefinitions.add(jbRun);
		monteCarloDefinitions.add(jpRunNum);
		return monteCarloDefinitions;
	}

	@Override
	public String getName() {
		return "MonteCarlo";
	}

	@Override
	public boolean canClose() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void applyChange() {
		// TODO Auto-generated method stub
		
	}
}