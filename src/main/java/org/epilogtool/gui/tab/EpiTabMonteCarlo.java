package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import javax.swing.tree.TreePath;


import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.gui.EpiGUI.ProjChangeNotifyTab;
import org.epilogtool.gui.EpiGUI.SimulationEpiClone;
import org.epilogtool.project.MonteCarlo;
import org.epilogtool.project.ProjectFeatures;
import org.epilogtool.project.Simulation;

public class EpiTabMonteCarlo extends EpiTabTools {
	private static final long serialVersionUID = 1394895739386499680L;
	
	private static int WIDTH = 145;
//	private int numberRuns;
//	private int maxNumberIterations;

	private MonteCarlo monteCarlo;

	public EpiTabMonteCarlo(Epithelium e, TreePath path,
			ProjChangeNotifyTab projChanged, ProjectFeatures projectFeatures,
			MonteCarlo monteCarlo) {
		super(e, path, projChanged);
		
		this.monteCarlo = monteCarlo;
	}

	public void initialize() {
		setLayout(new BorderLayout());
		
		
		JPanel left = new JPanel(new BorderLayout());
		
		//MonteCarlo Definitions Panel
		JPanel monteCarloDefinitions = createMonteCarloDefinitions();
		JPanel aux = createMonteCarloDefinitions();
		left.add(monteCarloDefinitions,BorderLayout.PAGE_START);
		
		JPanel monteCarloInfo = createMonteCarloInfo();
		left.add(monteCarloInfo,BorderLayout.PAGE_END);
		
		JPanel monteCarloVisualDefinitions = createMonteCarloVisualDefinitions();
		left.add(monteCarloVisualDefinitions,BorderLayout.CENTER);
		
		this.add(left);
	}

	private JPanel createMonteCarloVisualDefinitions() {
		JPanel monteCarloVisualDefinitions = new JPanel(new BorderLayout());
		
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
//		monteCarloInfo.setSize(250,500);
		
		monteCarloInfo.add(new JLabel("Update Mode: " + this.epithelium.getUpdateSchemeInter().getUpdateMode()));
		monteCarloInfo.add(new JLabel("Alpha: " + this.epithelium.getUpdateSchemeInter().getAlpha()));
		monteCarloInfo.add(new JLabel("Sigma: " + this.epithelium.getUpdateSchemeInter().getCPSigmas()));

		return monteCarloInfo;
	}

	private JPanel createMonteCarloDefinitions() {
		
		JPanel monteCarloDefinitions = new JPanel(new BorderLayout());
		monteCarloDefinitions.setBorder(BorderFactory.createTitledBorder("MonteCarlo Definitions"));
//		monteCarloDefinitions.setSize(250,500);;

		//Number of Runs
		JPanel jpRunNum = new JPanel();
		
		jpRunNum.add(new JLabel("Number of Simulations "));
		
		JTextField jtfNumRuns = new JTextField("100");
		jtfNumRuns.setToolTipText("Insert number of simulations");
		jtfNumRuns.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireChangeNumRuns(jtfNumRuns.getText());
			}
		});
		
		this.monteCarlo.setNumberRuns(Integer.parseInt(jtfNumRuns.getText()));
		
		jpRunNum.add(jtfNumRuns);

		// Maximum Number of Iterations
		JPanel jpMaxIte = new JPanel();
		
		jpMaxIte.add(new JLabel("Maximum Number of Iterations"));
		
		JTextField jtfMaxIte = new JTextField("100");
		jtfMaxIte.setToolTipText("Insert maximum iteration number per simulation");
		jtfMaxIte.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireChangeMaxIter(jtfMaxIte.getText());
			}
		});

		monteCarlo.setMaxIter(Integer.parseInt(jtfMaxIte.getText()));
		
		jpMaxIte.add(jtfMaxIte);

		//Run Button
		JButton jbRun = new JButton("Run");
		jbRun.setToolTipText("Run Monte Carlo ");
		jbRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO run 
				fireRun();
			}
		});
		
		//Add to Panel
		monteCarloDefinitions.add(jpRunNum,BorderLayout.PAGE_START);
		monteCarloDefinitions.add(jpMaxIte,BorderLayout.CENTER);
		monteCarloDefinitions.add(jbRun,BorderLayout.PAGE_END);
		
		return monteCarloDefinitions;
	}

	protected void fireChangeMaxIter(String text) {
		this.monteCarlo.setMaxIter(Integer.parseInt(text));
		
	}

	protected void fireChangeNumRuns(String text) {
		this.monteCarlo.setNumberRuns(Integer.parseInt(text));
	}

	protected void fireRun() {
		// TODO Auto-generated method stub
		this.monteCarlo.run();
		
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