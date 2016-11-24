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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
		
		JPanel left = new JPanel();
		JPanel monteCarloDefinitions = createMonteCarloDefinitions();
		JPanel aux = createMonteCarloDefinitions();
		left.add(monteCarloDefinitions);
		
		JPanel monteCarloInfo = createMonteCarloInfo();
		left.add(monteCarloInfo);
		
		JPanel monteCarloVisualDefinitions = createMonteCarloVisualDefinitions();
		left.add(monteCarloVisualDefinitions);
		
		this.add(left);
	}

	private JPanel createMonteCarloVisualDefinitions() {
		JPanel monteCarloVisualDefinitions = new JPanel();
		
		monteCarloVisualDefinitions.setBorder(BorderFactory.createTitledBorder("MonteCarlo Visual Definitions"));
		ButtonGroup group = new ButtonGroup();
		JRadioButton jrbStableStates = new JRadioButton("Stable States");
		JRadioButton jrbCumulative = new JRadioButton("Cumulative");
		group.add(jrbStableStates);
		group.add(jrbCumulative);
		monteCarloVisualDefinitions.add(jrbCumulative);
		monteCarloVisualDefinitions.add(jrbStableStates);
		
		return monteCarloVisualDefinitions;
	}

	private JPanel createMonteCarloInfo() {
		JPanel monteCarloInfo = new JPanel();
		monteCarloInfo.setBorder(BorderFactory.createTitledBorder("MonteCarlo Specifications"));
		monteCarloInfo.setSize(250,500);
		
		monteCarloInfo.add(new JLabel("Update Mode: " + this.epithelium.getUpdateSchemeInter().getUpdateMode()));
		monteCarloInfo.add(new JLabel("Alpha: " + this.epithelium.getUpdateSchemeInter().getAlpha()));
		monteCarloInfo.add(new JLabel("Sigma: " + this.epithelium.getUpdateSchemeInter().getCPSigmas()));

		return monteCarloInfo;
	}

	private JPanel createMonteCarloDefinitions() {
		
		JPanel monteCarloDefinitions = new JPanel();
		monteCarloDefinitions.setBorder(BorderFactory.createTitledBorder("MonteCarlo Definitions"));
		monteCarloDefinitions.setSize(250,500);;
		
		JButton jbRun = new JButton("Run");
		jbRun.setToolTipText("Run Monte Carlo ");
		jbRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO run 
				fireRun();
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
		
		numberRuns = Integer.parseInt(jtfNumRuns.getText());
		jpRunNum.add(jtfNumRuns);

		// Maximum Number of Iterations
		JPanel jpMaxIte = new JPanel();
		
		jpMaxIte.add(new JLabel("Maximum Number of Iterations"));
		
		JTextField jtfMaxIte = new JTextField("100");
		jtfMaxIte.setToolTipText("Insert maximum iteration number per simulation");
		jtfMaxIte.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO run 
			}
		});

		maxNumberIterations = Integer.parseInt(jtfMaxIte.getText());
		
		jpMaxIte.add(jtfMaxIte);

		//Add to Panel
		monteCarloDefinitions.add(jpRunNum);
		monteCarloDefinitions.add(jpMaxIte);
		monteCarloDefinitions.add(jbRun);
		
		return monteCarloDefinitions;
	}

	protected void fireRun() {
		// TODO Auto-generated method stub
		for (int i = 0; i<this.numberRuns; i++){
			Simulation sim =new Simulation(epithelium);
			boolean flag = false;
			System.out.println("Running Simulation: "+i);
			for (int j=0; j<this.maxNumberIterations;j++){
				EpitheliumGrid nextGrid = sim.getGridAt(j + 1);
				if (sim.isStableAt(j+1)){
					flag = true;
					break;	
					
				}
			}
			if (flag)
				System.out.println("Found Stable State");
			
		}
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