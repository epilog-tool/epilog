package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import javax.swing.tree.TreePath;


import org.epilogtool.core.Epithelium;
import org.epilogtool.gui.EpiGUI.ProjChangeNotifyTab;
import org.epilogtool.gui.color.ColorUtils;
import org.epilogtool.gui.tab.EpiTabDefinitions.TabProbablyChanged;
import org.epilogtool.project.MonteCarlo;
import org.epilogtool.project.ProjectFeatures;

public class EpiTabMonteCarlo extends EpiTabTools {
	private static final long serialVersionUID = 1394895739386499680L;
	
//	private static int WIDTH = 145;
//	private int numberRuns;
//	private int maxNumberIterations;
	private JButton jbRun;
	private TabProbablyChanged tpc;

	private MonteCarlo monteCarlo;

	public EpiTabMonteCarlo(Epithelium e, TreePath path,
			ProjChangeNotifyTab projChanged, ProjectFeatures projectFeatures,
			MonteCarlo monteCarlo) {
		super(e, path, projChanged);
		
		this.monteCarlo = monteCarlo;
		this.jbRun = new JButton("Run");
	}

	public void initialize() {
		setLayout(new BorderLayout());
		
		
		JPanel left = new JPanel(new BorderLayout());
		
		//MonteCarlo Definitions Panel
		JPanel monteCarloDefinitions = createMonteCarloDefinitions();
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
		
		JTextField jtfNumRuns = new JTextField("100",10);

		jtfNumRuns.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				
				JTextField jtf = (JTextField) e.getSource();
				tpc.setChanged();
				try {
					int nRuns = Integer.parseInt(jtf.getText());
					if (nRuns>0){
					jtf.setBackground(Color.WHITE);
					fireChangeNumRuns(nRuns);
					fireEnableRun(true);
					}
					
					else{
						jtf.setBackground(ColorUtils.LIGHT_RED);
						fireEnableRun(false);
					}
				} catch (NumberFormatException nfe) {
					jtf.setBackground(ColorUtils.LIGHT_RED);
					fireEnableRun(false);
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
		
		this.monteCarlo.setNumberRuns(Integer.parseInt(jtfNumRuns.getText()));
		
		jtfNumRuns.setToolTipText("Insert number of simulations");
		jpRunNum.add(jtfNumRuns);

		// Maximum Number of Iterations
		JPanel jpMaxIte = new JPanel();
		
		jpMaxIte.add(new JLabel("Maximum Number of Iterations"));
		
		JTextField jtfMaxIte = new JTextField("100");
		jtfMaxIte.setToolTipText("Insert maximum iteration number per simulation");
		jtfMaxIte.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				
				JTextField jtf = (JTextField) e.getSource();
				try {
					tpc.setChanged();
					int maxIter = Integer.parseInt(jtf.getText());
					if (maxIter>0){
					jtf.setBackground(Color.WHITE);
					fireChangeMaxIter(maxIter);
					fireEnableRun(true);
					}
					else{
						jtf.setBackground(ColorUtils.LIGHT_RED);
						fireEnableRun(false);
					}
				} catch (NumberFormatException nfe) {
					jtf.setBackground(ColorUtils.LIGHT_RED);
					fireEnableRun(false);
				}

				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				
			}
		});

		monteCarlo.setMaxIter(Integer.parseInt(jtfMaxIte.getText()));
		
		jpMaxIte.add(jtfMaxIte);

		
		//Choose Initial Conditions
		
		String[] ListInitialConditions = new String[2];
	
		ListInitialConditions[0]="Epithelium Initial Conditions";
		ListInitialConditions[1]="Random";
		
		JComboBox<String> jcbInitialConditions = new JComboBox(ListInitialConditions);
		jcbInitialConditions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> jcbInitialConditions = (JComboBox<String>) e.getSource();
				
				changeMonteCarloInitialConditions((String) jcbInitialConditions.getSelectedItem());
				tpc.setChanged();
			}
		});
		
		
		//Run Button

		jbRun.setToolTipText("Run Monte Carlo ");
		jbRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireRun();
			}
		});
		
		//Arrange Panels
		JPanel textPanels = new JPanel(new BorderLayout());
		
		textPanels.add(jpRunNum,BorderLayout.PAGE_START);
		textPanels.add(jpMaxIte,BorderLayout.CENTER);
		
		monteCarloDefinitions.add(textPanels,BorderLayout.PAGE_START);
		monteCarloDefinitions.add(jcbInitialConditions,BorderLayout.CENTER);
		monteCarloDefinitions.add(jbRun,BorderLayout.PAGE_END);
		
		return monteCarloDefinitions;
	}

	protected void changeMonteCarloInitialConditions(String selectedItem) {
		this.monteCarlo.setMonteCarloInitialConditions(selectedItem);
		
	}

	protected void fireEnableRun(boolean b) {
		this.jbRun.setEnabled(b);
		this.repaint();
		
	}

	protected void fireChangeMaxIter(int text) {
		this.monteCarlo.setMaxIter(text);
		
	}

	protected void fireChangeNumRuns(int nRuns) {
		this.monteCarlo.setNumberRuns(nRuns);
		System.out.println("Number of Runs changed to: " + nRuns);
	}

	protected void fireRun() {
		this.monteCarlo.run();
		
	}

	@Override
	public String getName() {
		return "MonteCarlo";
	}

	@Override
	public boolean canClose() {
		return false;
	}

	@Override
	public void applyChange() {
		// TODO Auto-generated method stub
		
	}
}