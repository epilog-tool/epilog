package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import javax.swing.tree.TreePath;


import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.core.topology.Topology;
import org.epilogtool.gui.EpiGUI.ProjChangeNotifyTab;
import org.epilogtool.gui.color.ColorUtils;
import org.epilogtool.gui.tab.EpiTabDefinitions.TabProbablyChanged;
import org.epilogtool.gui.widgets.GridInformation;
import org.epilogtool.gui.widgets.VisualGrid;
import org.epilogtool.gui.widgets.VisualGridMonteCarlo;
import org.epilogtool.io.ButtonFactory;
import org.epilogtool.project.MonteCarlo;
import org.epilogtool.project.ProjectFeatures;

public class EpiTabMonteCarlo extends EpiTabTools {
	private static final long serialVersionUID = 1394895739386499680L;
	
	private JButton jbRun;
	private MonteCarlo monteCarlo;
	private VisualGridMonteCarlo vgMonteCarlo;
	private JPanel jpRight;
	private JPanel jpLeft;
	
	private JButton jbRewind;
	private JButton jbBack;
	private JButton jbForward;
	private JButton jbFastFwr;
	private JLabel jlStep;
	private int iter;
	private Color backColor;


	public EpiTabMonteCarlo(Epithelium e, TreePath path,
			ProjChangeNotifyTab projChanged, ProjectFeatures projectFeatures,
			MonteCarlo monteCarlo) {
		super(e, path, projChanged);
		
		this.monteCarlo = monteCarlo;
		this.jbRun = new JButton("Run");
		this.jpRight = new JPanel(new BorderLayout());
		this.jpLeft = new JPanel(new BorderLayout());
		this.vgMonteCarlo = new VisualGridMonteCarlo(this.epithelium);
		this.backColor = Color.WHITE;
	

		
	}

	public void initialize() {
		this.setLayout(new BorderLayout());
			
		//MonteCarlo Definitions Panel
		JPanel monteCarloDefinitions = createMonteCarloDefinitions();
		jpLeft.add(monteCarloDefinitions,BorderLayout.PAGE_START);
		
		JPanel monteCarloInfo = createMonteCarloInfo();
		jpLeft.add(monteCarloInfo,BorderLayout.PAGE_END);
		
		JPanel monteCarloVisualDefinitions = createMonteCarloVisualDefinitions();
		jpLeft.add(monteCarloVisualDefinitions,BorderLayout.CENTER);
		
		
		//Bottom Right Panel
		JPanel jpButtons = new JPanel(new BorderLayout());
		jpButtons.setBackground(backColor);
		JPanel jpButtonsC = new JPanel();
		jpButtonsC.setBackground(backColor);
		jpButtons.add(jpButtonsC, BorderLayout.CENTER);

		JScrollPane jspButtons = new JScrollPane(jpButtons,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		jspButtons.setPreferredSize(new Dimension(
//				jspButtons.getPreferredSize().width, jspButtons
//						.getPreferredSize().height
//						+ jspButtons.getHorizontalScrollBar()
//								.getVisibleAmount() * 3));
		jspButtons.setBorder(BorderFactory.createEmptyBorder());
		jspButtons.setBackground(backColor);
		
		
		this.jbRewind = ButtonFactory
				.getImageNoBorder("media_rewind-26x24.png");
		this.jbRewind
				.setToolTipText("Go back to the beginning of the simulation");
		this.jbRewind.setEnabled(false);
		this.jbRewind.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				simulationRewind();
				//TODO
			}
		});

		
		jpButtonsC.add(this.jbRewind);
		this.jbBack = ButtonFactory
				.getImageNoBorder("media_step_back-24x24.png");
		this.jbBack.setToolTipText("Go back one step");
		this.jbBack.setEnabled(false);
		this.jbBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				simulationStepBack();
//				TODO
			}
		});
		
		jpButtonsC.add(this.jbBack);
		this.jbForward = ButtonFactory
				.getImageNoBorder("media_step_forward-24x24.png");
		this.jbForward.setToolTipText("Go forward one step");
		this.jbForward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				simulationStepFwr();
//				TODO
			}
		});

		jpButtonsC.add(this.jbForward);
		

		this.jbFastFwr = ButtonFactory
				.getImageNoBorder("media_fast_forward-26x24.png");
		this.jbFastFwr.setToolTipText("Go forward a burst of 'n' steps");
		this.jbFastFwr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				simulationFastFwr();
//				TODO
			}
		});
		jpButtonsC.add(this.jbFastFwr);

		JPanel jpButtonsR = new JPanel();
		JButton jbClone = ButtonFactory.getNoMargins("Clone");
		jbClone.setToolTipText("Create a new Epithelium with initial conditions as the current grid");
		jbClone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				cloneEpiWithCurrGrid();
//				TODO
			}
		});
		jpButtonsR.add(jbClone);

		// Button to save an image from the simulated grid
		JButton jbPicture = ButtonFactory
				.getImageNoBorder("fotography-24x24.png");
		jbPicture.setToolTipText("Save the image of the current grid to file");
		jbPicture.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				saveEpiGrid2File();
//				TODO
			}
		});
		jpButtonsR.add(jbPicture);		
		


		
		jpButtons.add(jpButtonsR, BorderLayout.LINE_END);

		JPanel jpButtonsL = new JPanel();
		this.jlStep = new JLabel("Iteration: " + this.iter);
		jpButtonsL.add(this.jlStep);

		jpButtons.add(jpButtonsL, BorderLayout.LINE_START);
		
		this.jbRewind.setBackground(backColor);
		this.jbBack.setBackground(backColor);
		this.jbForward.setBackground(backColor);
		this.jbFastFwr.setBackground(backColor);
		this.jlStep.setBackground(backColor);
		jpButtonsR.setBackground(backColor);
		jpButtonsL.setBackground(backColor);
		
		
//		this.jpRight.add(this.vgMonteCarlo,BorderLayout.CENTER);


		this.jpRight.add(jspButtons, BorderLayout.PAGE_END);

		this.add(jpRight, BorderLayout.PAGE_END);
		this.add(jpLeft,BorderLayout.WEST);
		this.add(this.vgMonteCarlo,BorderLayout.CENTER);
		this.repaint();
		this.revalidate();

	}

	private JPanel createMonteCarloVisualDefinitions() {
		JPanel monteCarloVisualDefinitions = new JPanel(new BorderLayout());
		
		JPanel monteCarloVisualDefinitionsTOP = new JPanel(new BorderLayout());
		
		JPanel monteCarloVisualDefinitionsCenter = new JPanel();
		
		monteCarloVisualDefinitions.setBorder(BorderFactory.createTitledBorder("MonteCarlo Visual Definitions"));
		ButtonGroup group = new ButtonGroup();
		JRadioButton jrbStableStates = new JRadioButton("Stable States");
		jrbStableStates.setSelected(true);
		
		  //add jrbStableStates listener
		jrbStableStates.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            fireStableStatesGrid(monteCarloVisualDefinitionsCenter);
	        }
	    });
	    
		JRadioButton jrbCumulative = new JRadioButton("Cumulative");
		
		  //add jrbStableStates listener
		jrbCumulative.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            fireCumulativeGrid(monteCarloVisualDefinitionsCenter);

	        }
	    });
		
		
		group.add(jrbStableStates);
		group.add(jrbCumulative);
		monteCarloVisualDefinitionsTOP.add(jrbCumulative,BorderLayout.EAST);
		monteCarloVisualDefinitionsTOP.add(jrbStableStates,BorderLayout.WEST);
		
		if (jrbStableStates.isSelected())
			fireStableStatesGrid(monteCarloVisualDefinitionsCenter);
		else
			fireCumulativeGrid(monteCarloVisualDefinitionsCenter);
		
		
		monteCarloVisualDefinitions.add(monteCarloVisualDefinitionsTOP,BorderLayout.PAGE_START);
		monteCarloVisualDefinitions.add(monteCarloVisualDefinitionsCenter,BorderLayout.CENTER);
		
		return monteCarloVisualDefinitions;
	}

	protected JPanel fireCumulativeGrid(JPanel jpanel) {
		// TODO Auto-generated method stub
//		jpanel.setBackground(Color.BLUE);
		initializeCumulativeVisualGrid();
		return jpanel;
	}

	private void initializeCumulativeVisualGrid() {
		// TODO Auto-generated method stub
//		this.visualGrid.setBackground(Color.BLUE);
//		this.visualGrid.repaint();
	}

	protected JPanel fireStableStatesGrid(JPanel jpanel) {
		// TODO Auto-generated method stub
		initializeStableStatesVisualGrid();
		return jpanel;
	}

	private void initializeStableStatesVisualGrid() {
		// TODO Auto-generated method stub
//		this.visualGrid.setBackground(Color.YELLOW);
		
		Topology topology = this.epithelium.getEpitheliumGrid().getTopology();
//		this.visualGrid.add(this.vgMonteCarlo,BorderLayout.CENTER);
//		this.repaint();
//		this.visualGrid.repaint();
//		this.add(this.vgMonteCarlo,BorderLayout.CENTER);
		

	}

	private JPanel createMonteCarloInfo() {
		JPanel monteCarloInfo = new JPanel(new BorderLayout());
		
		JPanel monteCarloInfoUp= new JPanel(new BorderLayout());
		JPanel monteCarloInfoCenter= new JPanel(new BorderLayout());
		
		monteCarloInfo.setBorder(BorderFactory.createTitledBorder("MonteCarlo Specifications"));
//		monteCarloInfo.setSize(250,500);
		
		monteCarloInfoUp.add(new JLabel("Epithelium: " + this.epithelium.getName()),BorderLayout.PAGE_START);
		monteCarloInfoUp.add(new JLabel("Models: " + this.epithelium.getUsedModels()),BorderLayout.CENTER);
		
		monteCarloInfoCenter.add(new JLabel("Update Mode: " + this.epithelium.getUpdateSchemeInter().getUpdateMode()),BorderLayout.PAGE_START);
		monteCarloInfoCenter.add(new JLabel("Alpha: " + this.epithelium.getUpdateSchemeInter().getAlpha()),BorderLayout.CENTER);
		monteCarloInfoCenter.add(new JLabel("Sigma: " + this.epithelium.getUpdateSchemeInter().getCPSigmas()),BorderLayout.PAGE_END);

		monteCarloInfo.add(monteCarloInfoUp,BorderLayout.PAGE_START);
		monteCarloInfo.add(monteCarloInfoCenter,BorderLayout.CENTER);
		
		
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
		boolean flag = false;
		if (selectedItem.equals("Random")) flag = true;
		this.monteCarlo.setMonteCarloInitialConditions(flag);
		System.out.println("Just changed the initial conditions");
		
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