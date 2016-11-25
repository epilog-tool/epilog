package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.epilogtool.common.ObjectComparator;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.core.topology.Topology;
import org.epilogtool.gui.EpiGUI.ProjChangeNotifyTab;
import org.epilogtool.gui.color.ColorUtils;
import org.epilogtool.gui.tab.EpiTabDefinitions.TabProbablyChanged;
import org.epilogtool.gui.widgets.GridInformation;
import org.epilogtool.gui.widgets.JComboWideBox;
import org.epilogtool.gui.widgets.VisualGrid;
import org.epilogtool.gui.widgets.VisualGridMonteCarlo;
import org.epilogtool.gui.widgets.VisualGridSimulation;
import org.epilogtool.io.ButtonFactory;
import org.epilogtool.project.MonteCarlo;
import org.epilogtool.project.ProjectFeatures;

public class EpiTabMonteCarlo extends EpiTabTools {
	private static final long serialVersionUID = 1394895739386499680L;
	
	private ProjectFeatures projectFeatures;
	private MonteCarlo monteCarlo;
	private VisualGridSimulation vgCellState;
	private GridInformation gridInformation;
	private EpitheliumGrid epiGrid;
	
	private JPanel jpRight;
	private JPanel jpLeft;
	private JPanel monteCarloVisualDefinitionsCenter;
	private JPanel jpRCenter;
	private JPanel rTop;
	
	private int lastStableStateIndex;
	
//	private JButton jbRewind;
	private JButton jbRun;
	private JButton jbBack;
	private JButton jbForward;
	private JLabel jlStep;
	
	private Color backColor;
	
	private JRadioButton jrbCumulative ;
	private JRadioButton jrbStableStates ;
	

	private Map<String, JButton> mNode2JButton;
	private Map<String, JCheckBox> mNode2Checkbox;
	private List<String> lCompON;
	private LogicalModel selectedModel;
	private List<String> lNodeInPanel;
	private Map<String, Byte> mNode2ValueSelected;
	

	public EpiTabMonteCarlo(Epithelium e, TreePath path,
			ProjChangeNotifyTab projChanged, ProjectFeatures projectFeatures,
			MonteCarlo monteCarlo) {
		super(e, path, projChanged);
		
		this.projectFeatures = projectFeatures;
		this.gridInformation = new GridInformation(
				this.epithelium.getIntegrationFunctions(), this.projectFeatures);
		
		
		this.monteCarlo = monteCarlo;
		this.jbRun = new JButton("Run");
		this.jpRight = new JPanel(new BorderLayout());
		this.jpLeft = new JPanel(new BorderLayout());
		
		this.jrbCumulative = new JRadioButton("Cumulative");
		this.jrbStableStates = new JRadioButton("Stable States");
		
		this.epiGrid = this.epithelium.getEpitheliumGrid();
		this.lCompON = new ArrayList<String>();
		this.mNode2ValueSelected = new HashMap<String, Byte>();
		this.lNodeInPanel = new ArrayList<String>();
		// Create everything at the beginning for every nodeID
		this.mNode2JButton = new HashMap<String, JButton>();
		this.mNode2Checkbox = new HashMap<String, JCheckBox>();
		for (LogicalModel m : this.epiGrid.getModelSet()) {
			this.createGUIForModel(m);
		}
		
	
		
//		this.colorButton2Node = new HashMap<JButton, String>();
//		this.mSelCheckboxes = new HashMap<String, Boolean>();
//		

		this.lastStableStateIndex = 0;
		
		
		this.jbBack = ButtonFactory
				.getImageNoBorder("media_step_back-24x24.png");
		this.jbForward = ButtonFactory
				.getImageNoBorder("media_step_forward-24x24.png");
		
//		this.jbRewind.setEnabled(false);
		this.jbBack.setEnabled(false);
		this.jbForward.setEnabled(false);
//		this.jbFastFwr.setEnabled(false);
//		this.jlStep.setEnabled(false);
	}

	public void initialize() {
		this.setLayout(new BorderLayout());

		
		
		this.backColor = Color.WHITE;	
			
		//MonteCarlo Definitions Panel
		JPanel monteCarloDefinitions = createMonteCarloDefinitions();
		jpLeft.add(monteCarloDefinitions,BorderLayout.PAGE_START);
		
		JPanel monteCarloInfo = createMonteCarloInfo();
		jpLeft.add(monteCarloInfo,BorderLayout.PAGE_END);
		
		JPanel monteCarloVisualDefinitions = createMonteCarloVisualDefinitions();
		jpLeft.add(monteCarloVisualDefinitions,BorderLayout.CENTER);
		
		
		//VIsualGrid
		for (NodeInfo node: this.selectedModel.getNodeOrder()){
			this.lCompON.add(node.getNodeID());
		}
		this.vgCellState = new VisualGridSimulation(this.epithelium.getEpitheliumGrid(),this.projectFeatures,this.lCompON,this.gridInformation);
		
		
		
		
		//MonteCarlo GridInformation
		
		if (!this.jrbCumulative.isSelected()){
			JPanel left = new JPanel(new BorderLayout());
			left.add(jpLeft,BorderLayout.WEST);
			left.add(this.gridInformation,BorderLayout.CENTER);
			jpLeft = left;
			
		}
		
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
		
		
//		this.jbRewind = ButtonFactory
//				.getImageNoBorder("media_rewind-26x24.png");
//		this.jbRewind
//				.setToolTipText("Go back to the beginning of the simulation");
//		this.jbRewind.setEnabled(false);
//		this.jbRewind.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
////				simulationRewind();
//				//TODO
//			}
//		});

//		jpButtonsC.add(this.jbRewind);


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

		this.jbForward.setToolTipText("Go forward one step");
		this.jbForward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulationStepFwr();
			}
		});

		jpButtonsC.add(this.jbForward);
		

//		this.jbFastFwr = ButtonFactory
//				.getImageNoBorder("media_fast_forward-26x24.png");
//		this.jbFastFwr.setToolTipText("Go forward a burst of 'n' steps");
//		this.jbFastFwr.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
////				simulationFastFwr();
////				TODO
//			}
//		});
//		jpButtonsC.add(this.jbFastFwr);

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
		this.jlStep = new JLabel("");
		jpButtonsL.add(this.jlStep);

		jpButtons.add(jpButtonsL, BorderLayout.LINE_START);
		
//		this.jbRewind.setBackground(backColor);
		this.jbBack.setBackground(backColor);
		this.jbForward.setBackground(backColor);
//		this.jbFastFwr.setBackground(backColor);
		this.jlStep.setBackground(backColor);
		jpButtonsR.setBackground(backColor);
		jpButtonsL.setBackground(backColor);
		
		
//		this.jpRight.add(this.vgMonteCarlo,BorderLayout.CENTER);


		this.jpRight.add(jspButtons, BorderLayout.PAGE_END);

		this.add(jpRight, BorderLayout.PAGE_END);
		this.add(jpLeft,BorderLayout.WEST);
		this.add(this.vgCellState,BorderLayout.CENTER);
		
		this.repaint();
		this.revalidate();

	}
	protected void simulationStepFwr() {
		// TODO Auto-generated method stub
		this.lastStableStateIndex = this.lastStableStateIndex + 1;
		EpitheliumGrid stableState = this.monteCarlo.getStableStates().get(this.lastStableStateIndex);
		this.vgCellState = new VisualGridSimulation(stableState,this.projectFeatures,this.lCompON,this.gridInformation);
		updatejlIteration(stableState);
		this.jbBack.setEnabled(true);
		this.vgCellState.repaint();
		this.repaint();
		
	}

	private int updatejlIteration(EpitheliumGrid stableState) {
		int iteration = this.monteCarlo.getStableState2Iteration().get(stableState);
		this.jlStep.setText("Stable State: " +this.lastStableStateIndex + " of "+this.monteCarlo.getStableState2Iteration().size()+ " [Iteration: " +iteration+"]");
		this.jlStep.repaint();
		return iteration;
	}

	private JPanel createMonteCarloVisualDefinitions() {
		JPanel monteCarloVisualDefinitions = new JPanel(new BorderLayout());
		
		JPanel monteCarloVisualDefinitionsTOP = new JPanel(new BorderLayout());
		this.monteCarloVisualDefinitionsCenter = new JPanel();
		
		monteCarloVisualDefinitions.setBorder(BorderFactory.createTitledBorder("MonteCarlo Visual Definitions"));
		ButtonGroup group = new ButtonGroup();

		this.jrbStableStates.setSelected(true);
		
		  //add jrbStableStates listener
		this.jrbStableStates.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            fireStableStatesGrid(monteCarloVisualDefinitionsCenter);
	        }
	    });
	    
		
		
		  //add jrbStableStates listener
		this.jrbCumulative.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            fireCumulativeGrid(monteCarloVisualDefinitionsCenter);

	        }
	    });
		
		group.add(this.jrbStableStates);
		group.add(this.jrbCumulative);
		monteCarloVisualDefinitionsTOP.add(this.jrbCumulative,BorderLayout.EAST);
		monteCarloVisualDefinitionsTOP.add(this.jrbStableStates,BorderLayout.WEST);
		
//		if (jrbStableStates.isSelected())
//			fireStableStatesGrid(monteCarloVisualDefinitionsCenter);
//		else
//			fireCumulativeGrid(monteCarloVisualDefinitionsCenter);
		
		
		
		
		
		
		
		
		
		
		//monteCarloVisualDefinitionsCenter
		
		this.rTop = new JPanel();
		this.rTop.setLayout(new BoxLayout(this.rTop, BoxLayout.Y_AXIS));

		// Model selection list
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.epithelium.getEpitheliumGrid().getModelSet());
		JComboBox<String> jcbSBML = this.newModelCombobox(modelList);
		this.rTop.add(jcbSBML);
		this.selectedModel = this.epithelium.getProjectFeatures().getModel((String) jcbSBML.getSelectedItem());

		// Select / Deselect buttons
		JPanel rTopSel = new JPanel(new FlowLayout());
		// rTopSel.setLayout(new BoxLayout(rTopSel, BoxLayout.X_AXIS));
		JButton jbSelectAll = new JButton("Select All");
		jbSelectAll.setMargin(new Insets(0, 0, 0, 0));
		jbSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (String nodeID : lNodeInPanel) {
					mNode2Checkbox.get(nodeID).setSelected(true);
				}
				vgCellState.paintComponent(vgCellState.getGraphics());
			}
		});
		rTopSel.add(jbSelectAll);
		JButton jbDeselectAll = new JButton("Deselect All");
		jbDeselectAll.setMargin(new Insets(0, 0, 0, 0));
		jbDeselectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (String nodeID : lNodeInPanel) {
					mNode2Checkbox.get(nodeID).setSelected(false);
					mNode2ValueSelected.remove(nodeID);
				}
				vgCellState.paintComponent(vgCellState.getGraphics());
			}
		});
		rTopSel.add(jbDeselectAll);
		this.rTop.add(rTopSel);

		this.rTop
				.setBorder(BorderFactory.createTitledBorder("Display options"));
		
		

		this.monteCarloVisualDefinitionsCenter.add(rTop, BorderLayout.NORTH);
		
		
		//Components
		this.jpRCenter = new JPanel();
		
		
		this.jpRCenter.setLayout(new BoxLayout(jpRCenter, BoxLayout.Y_AXIS));
		JScrollPane jscroll = new JScrollPane(this.jpRCenter);
		
		jscroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.jpRCenter.setBackground(backColor);
		this.monteCarloVisualDefinitionsCenter.add(jscroll, BorderLayout.CENTER);

		this.monteCarloVisualDefinitionsCenter.setBackground(backColor);
		monteCarloVisualDefinitions.add(monteCarloVisualDefinitionsTOP,BorderLayout.PAGE_START);
		monteCarloVisualDefinitions.add(this.monteCarloVisualDefinitionsCenter,BorderLayout.CENTER);
		
		return monteCarloVisualDefinitions;
	}



	protected JPanel fireCumulativeGrid(JPanel jpanel) {
		// TODO Auto-generated method stub
//		initializeCumulativeVisualGrid();
		return jpanel;
	}

//	private void initializeCumulativeVisualGrid() {
//		// TODO Auto-generated method stub
//
//	}

	protected JPanel fireStableStatesGrid(JPanel jpanel) {
		// TODO Auto-generated method stub
		initializeStableStatesVisualGrid();
		return jpanel;
	}

	private void initializeStableStatesVisualGrid() {
		// TODO Auto-generated method stub
		
		

		

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
		if (this.monteCarlo.getStableStates()!=null & this.monteCarlo.getStableStates().size()>0){
			this.jbForward.setEnabled(true);
			EpitheliumGrid stableState= this.monteCarlo.getStableStates().get(0);
			this.vgCellState = new VisualGridSimulation(stableState,this.projectFeatures,this.lCompON,this.gridInformation);
			this.lastStableStateIndex = this.lastStableStateIndex+1;
			updatejlIteration(stableState);
			this.vgCellState.repaint();
		}
			
			
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
	
	
	
	private JComboBox<String> newModelCombobox(List<LogicalModel> modelList) {
		// Model selection list
		String[] saSBML = new String[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			saSBML[i] = this.projectFeatures.getModelName(modelList.get(i));
		}
		JComboBox<String> jcb = new JComboWideBox(saSBML);
		jcb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> jcb = (JComboBox<String>) e.getSource();
				setSelectedModel((String) jcb.getSelectedItem());
				updateComponentList((String) jcb.getSelectedItem());
			}
		});
		return jcb;
	}
	
	private void setSelectedModel(String model){
		this.selectedModel = this.projectFeatures.getModel(model);
	}

	//Assign a new color to a component
	private void setNewColor(JButton jb) {
		String nodeID = jb.getToolTipText();
		Color newColor = JColorChooser.showDialog(jb, "Color chooser - "
				+ nodeID, jb.getBackground());
		if (newColor != null
				&& !newColor.equals(projectFeatures.getNodeColor(nodeID))) {
			jb.setBackground(newColor);
			this.epithelium.getProjectFeatures().setNodeColor(nodeID,
					newColor);
			this.projChanged.setChanged(this);
			if (this.mNode2ValueSelected.containsKey(nodeID)) {
				// Paint only if NodeID is selected!!
				this.vgCellState.paintComponent(this.vgCellState
						.getGraphics());
			}
		}
	}

	//When a new model is selected, and at the beggining 
	private void updateComponentList(String sModel) {
		this.jpRCenter.removeAll();
		this.mNode2ValueSelected.clear();
		this.lNodeInPanel.clear();

		LogicalModel m = this.projectFeatures.getModel(sModel);
//		this.visualGridICs.setModel(m);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 5, 1, 0);
		int y = 0;

		// Internal components

		List<String> lInternal = new ArrayList<String>(this.epithelium
				.getProjectFeatures().getModelNodeIDs(m, false));
		Collections.sort(lInternal, ObjectComparator.STRING);
		
		if (lInternal.size()>0){
		
		JPanel jpRRCTop = new JPanel(new GridBagLayout());
		jpRRCTop.setBorder(BorderFactory
				.createTitledBorder("Internal components"));
		for (String nodeID : lInternal) {
			gbc.gridy = y;
			y++;
			this.lNodeInPanel.add(nodeID);
			if (this.mNode2Checkbox.get(nodeID).isSelected()) {
				//TODO
			}
			gbc.gridx = 0;
			gbc.anchor = GridBagConstraints.WEST;
			jpRRCTop.add(this.mNode2Checkbox.get(nodeID), gbc);
			gbc.gridx = 1;
			jpRRCTop.add(this.mNode2Checkbox.get(nodeID), gbc);
			gbc.gridx = 2;
			JButton jbTmp = this.mNode2JButton.get(nodeID);
			jbTmp.setBackground(this.epithelium.getProjectFeatures()
					.getNodeColor(nodeID));
			jpRRCTop.add(jbTmp, gbc);
		}
		this.jpRCenter.add(jpRRCTop);
		}
		// Input components
		
		List<String> lInputs = new ArrayList<String>(this.epithelium
				.getProjectFeatures().getModelNodeIDs(m, true));
		Collections.sort(lInputs, ObjectComparator.STRING);
		List<String> lEnvInputCompsFromSelectedModels = new ArrayList<String>();
		for (String nodeID : lInputs) {
			if (!this.epithelium.isIntegrationComponent(this.epithelium
					.getProjectFeatures().getNodeInfo(nodeID, m))) {
				lEnvInputCompsFromSelectedModels.add(nodeID);
			}
		}
		//If no env inputs then the box is not created
		if (lEnvInputCompsFromSelectedModels.size()!=0)
			{
		JPanel jpRRCBottom = new JPanel(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 5, 1, 0);
		jpRRCBottom.setBorder(BorderFactory
				.createTitledBorder("Input components"));

		y = 0;
		for (String nodeID : lEnvInputCompsFromSelectedModels) {
			gbc.gridy = y;
			y++;
			this.lNodeInPanel.add(nodeID);
			if (this.mNode2Checkbox.get(nodeID).isSelected()) {
				//TODO
			}
			gbc.gridx = 0;
			gbc.anchor = GridBagConstraints.WEST;
			jpRRCBottom.add(this.mNode2Checkbox.get(nodeID), gbc);
			gbc.gridx = 1;
			jpRRCBottom.add(this.mNode2Checkbox.get(nodeID), gbc);
			gbc.gridx = 2;
			JButton jbTmp = this.mNode2JButton.get(nodeID);
			jbTmp.setBackground(this.epithelium.getProjectFeatures()
					.getNodeColor(nodeID));
			jpRRCBottom.add(jbTmp, gbc);
		}
		this.jpRCenter.add(jpRRCBottom);

	
	}
		// Re-Paint
		this.getParent().repaint();}
	
	private void createGUIForModel(LogicalModel m) {
		for (NodeInfo node : m.getNodeOrder()) {
			String nodeID = node.getNodeID();
			// Color
			JButton jButton = new JButton();
			jButton.setBackground(this.epithelium.getProjectFeatures()
					.getNodeColor(nodeID));
			jButton.setToolTipText(nodeID);
			jButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setNewColor((JButton) e.getSource());
				}
			});
			this.mNode2JButton.put(nodeID, jButton);
			// Checkbox
			JCheckBox jcheckb = new JCheckBox(nodeID, false);
			jcheckb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JCheckBox jcb = (JCheckBox) e.getSource();
					String nodeID = jcb.getText();
				
					// Repaint
					vgCellState.paintComponent(vgCellState.getGraphics());
				}
			});
			this.mNode2Checkbox.put(nodeID, jcheckb);

		}
	}

	
	
}