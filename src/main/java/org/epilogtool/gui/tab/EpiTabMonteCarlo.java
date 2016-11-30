package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.Random;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import org.epilogtool.gui.EpiGUI.ProjChangeNotifyTab;
import org.epilogtool.gui.color.ColorUtils;
import org.epilogtool.gui.widgets.GridInformation;
import org.epilogtool.gui.widgets.JComboCheckBox;
import org.epilogtool.gui.widgets.VisualGridSimulation;
import org.epilogtool.io.ButtonFactory;
import org.epilogtool.io.EpilogFileFilter;
import org.epilogtool.io.FileIO;
import org.epilogtool.project.MonteCarlo;
import org.epilogtool.project.ProjectFeatures;

public class EpiTabMonteCarlo extends EpiTabTools {
	private static final long serialVersionUID = 1394895739386499680L;
	
	private ProjectFeatures projectFeatures;
	private Epithelium clonedEpi;
	private MonteCarlo monteCarlo;
	private VisualGridSimulation vgCellState;
	private GridInformation gridInformation;
	private EpitheliumGrid epiGrid;
	
	private JPanel jpRight;
	private JPanel jpLeft;
	private JPanel monteCarloVisualDefinitionsCenter;
	private JPanel jpRCenter;
	private JPanel rTop;
	private JPanel informationPanel;
	
	private int lastStableStateIndex;
	
	private JButton jbRewind;
	private JButton jbRun;
	private JButton jbBack;
	private JButton jbStep;
	private JButton jbFastFwr;
	
	private JButton jbClone;
	private JButton jbPicture;
	private JButton jbSaveAll;
	
	private JLabel jlStep;
	
	private JLabel uniqueSS;
	private JLabel notReachedSS;
	
	private JComboCheckBox jccb;
	private Color backColor;
	
	private JRadioButton jrbCumulative ;
	private JRadioButton jrbStableStates ;
	private Map<JButton, String> colorButton2Node;

	private Map<String, JButton> mNode2JButton;
	private Map<String, JCheckBox> mNode2Checkbox;
	private Map<String, Boolean> mSelCheckboxes;
	private Map<String, JCheckBox> mNodeID2Checkbox;
	
	private List<String> lCompON;
	private List<String> lPresentComps;

	private List<String> lNodeInPanel;
	private Map<String, Byte> mNode2ValueSelected;
	

	public EpiTabMonteCarlo(Epithelium e, TreePath path,
			ProjChangeNotifyTab projChanged, ProjectFeatures projectFeatures,
			MonteCarlo monteCarlo) {
		super(e, path, projChanged);
		
		this.projectFeatures = projectFeatures;
		this.gridInformation = new GridInformation(
				this.epithelium.getIntegrationFunctions(), this.projectFeatures);
		
		this.clonedEpi = this.epithelium.clone();
		
		this.mSelCheckboxes = new HashMap<String, Boolean>();
		this.mNodeID2Checkbox = new HashMap<String, JCheckBox>();
		this.colorButton2Node = new HashMap<JButton, String>();
		
		for (LogicalModel m : this.epithelium.getEpitheliumGrid().getModelSet()) {
			for (NodeInfo node : m.getNodeOrder()) {
				this.mSelCheckboxes.put(node.getNodeID(), false);
			}
		}
		
		this.monteCarlo = monteCarlo;
		this.jbRun = new JButton("Run");
		this.jpRight = new JPanel(new BorderLayout());
		this.jpLeft = new JPanel(new BorderLayout());
		this.informationPanel = new JPanel(new BorderLayout());
		
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

		this.lastStableStateIndex = 0;
		
		
		this.jbBack = ButtonFactory
				.getImageNoBorder("media_step_back-24x24.png");
		this.jbStep = ButtonFactory
				.getImageNoBorder("media_step_forward-24x24.png");
		this.jbRewind = ButtonFactory
				.getImageNoBorder("media_rewind-26x24.png");
		this.jbFastFwr = ButtonFactory
				.getImageNoBorder("media_fast_forward-26x24.png");
		this.jbPicture = ButtonFactory
				.getImageNoBorder("fotography-24x24.png");
		this.jbSaveAll = ButtonFactory
				.getImageNoBorder("fotography-mult-24x24.png");
		this.jbClone = ButtonFactory.getNoMargins("Clone");
		
		
		this.jbRewind.setEnabled(false);
		this.jbBack.setEnabled(false);
		this.jbStep.setEnabled(false);
		this.jbFastFwr.setEnabled(false);
		this.jbClone.setEnabled(false);
		this.jbPicture.setEnabled(false);
		this.jbSaveAll.setEnabled(false);
	}

	public void  initialize()  {
		this.setLayout(new BorderLayout());

		this.backColor = Color.WHITE;	
			
		//MonteCarlo Definitions Panel
		JPanel monteCarloDefinitions = createMonteCarloDefinitions();
		jpLeft.add(monteCarloDefinitions,BorderLayout.PAGE_START);
		
		JPanel monteCarloInfo = createMonteCarloInfo();
		jpLeft.add(monteCarloInfo,BorderLayout.PAGE_END);
		
		this.vgCellState = new VisualGridSimulation(this.clonedEpi.getEpitheliumGrid(),this.projectFeatures,this.lCompON,this.gridInformation);

		JPanel monteCarloVisualDefinitions = createMonteCarloVisualDefinitions();
		jpLeft.add(monteCarloVisualDefinitions,BorderLayout.CENTER);
		
		//MonteCarlo GridInformation
		createInformationPanel();

		JPanel left = new JPanel(new BorderLayout());
		left.add(jpLeft,BorderLayout.WEST);
		left.add(informationPanel,BorderLayout.CENTER);
		jpLeft = left;

		
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
		
		
		this.jbRewind
				.setToolTipText("Go to the first Stable State");
		this.jbRewind.setEnabled(false);
		this.jbRewind.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulationRewind();
			}
		});
		jpButtonsC.add(this.jbRewind);


		this.jbBack.setToolTipText("Go to the previous Stable State");
		this.jbBack.setEnabled(false);
		this.jbBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulationStepBack();
			}
		});
		
		jpButtonsC.add(this.jbBack);

		this.jbStep.setToolTipText("Go to the next Stable State");
		this.jbStep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulationStepFwr();
			}
		});

		jpButtonsC.add(this.jbStep);
		
		this.jbFastFwr.setToolTipText("Go to the last Stable State");
		this.jbFastFwr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulationFastFwr();
			}
		});
		jpButtonsC.add(this.jbFastFwr);

		JPanel jpButtonsR = new JPanel();

		this.jbClone.setToolTipText("Create a new Epithelium with initial conditions as the current grid");
		this.jbClone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cloneEpiWithCurrGrid();
			}
		});
		this.jbClone.setEnabled(false);
		jpButtonsR.add(this.jbClone);

		// Button to save an image from the simulated grid
		this.jbPicture.setToolTipText("Save the image of the current grid to file");
		this.jbPicture.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveEpiGrid2File();
			}
		});
		this.jbPicture.setEnabled(false);
		jpButtonsR.add(this.jbPicture);
		

		// Button to save all simulated grid images
		this.jbSaveAll
				.setToolTipText("Save all the simulation grids into different files");
		this.jbSaveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveAllEpiGrid2File();
			}
		});
		this.jbSaveAll.setEnabled(false);
		jpButtonsR.add(this.jbSaveAll);	
		
		jpButtons.add(jpButtonsR, BorderLayout.LINE_END);

		JPanel jpButtonsL = new JPanel();
		this.jlStep = new JLabel("");
		jpButtonsL.add(this.jlStep);

		jpButtons.add(jpButtonsL, BorderLayout.LINE_START);
		
		this.jbRewind.setBackground(backColor);
		this.jbBack.setBackground(backColor);
		this.jbStep.setBackground(backColor);
		this.jbFastFwr.setBackground(backColor);
		this.jlStep.setBackground(backColor);
		jpButtonsR.setBackground(backColor);
		jpButtonsL.setBackground(backColor);
		
		this.jpRight.add(jspButtons, BorderLayout.PAGE_END);

		this.add(jpRight, BorderLayout.PAGE_END);
		this.add(jpLeft,BorderLayout.WEST);
		this.add(this.vgCellState,BorderLayout.CENTER);
		updateComponentList(this.jccb.getSelectedItems());
		this.vgCellState.paintComponent(this.vgCellState.getGraphics());

		this.repaint();
		this.revalidate();
		this.vgCellState.repaint();
	}
	
	private void createInformationPanel() {
		// TODO
		
		JPanel informationPanelMonteCarlo = new JPanel(new GridBagLayout());
		informationPanelMonteCarlo.setBackground(backColor);
		
		informationPanelMonteCarlo.setBorder(BorderFactory.createTitledBorder("Monte Carlo"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		

		gbc.gridy = 0;
		gbc.gridx = 0;
		this.uniqueSS = new JLabel("");
		this.uniqueSS.setText("Unique SS: " );
		informationPanelMonteCarlo.add(this.uniqueSS,gbc);
		
		gbc.gridy = 1;
		gbc.gridx = 0;
		this.notReachedSS = new JLabel("");
		this.notReachedSS.setText("Not reached: ");
		informationPanelMonteCarlo.add(this.notReachedSS,gbc);
		
		informationPanel.add(this.gridInformation,BorderLayout.CENTER);
		informationPanel.add(informationPanelMonteCarlo,BorderLayout.PAGE_START);
		return ;
	}

	// get current simulation step
	private void saveEpiGrid2File() {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new EpilogFileFilter("png"));
		if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			String file = fc.getSelectedFile().getAbsolutePath();
			String ext = "PNG";
			file += (file.endsWith(ext) ? "" : "." + ext);
			FileIO.writeEpitheliumGrid2File(file, this.vgCellState,
					ext);
		}
	}
	
	private void saveAllEpiGrid2File() {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new EpilogFileFilter("png"));
		if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			String file = fc.getSelectedFile().getAbsolutePath();
			String ext = "PNG";
			file += (file.endsWith(ext) ? "" : "." + ext);
			int i = 1;
			for (EpitheliumGrid stableState: this.monteCarlo.getUniqueStableStates().keySet()) {
				String file_name = file.replace(".", "_" + i + ".");;
				this.vgCellState.setEpitheliumGrid(stableState);
				i = i+1;
				FileIO.writeEpitheliumGrid2File(file_name,
						this.vgCellState, ext);
			}
		}
	}
	
	protected void simulationFastFwr() {
		this.lastStableStateIndex = this.monteCarlo.getStableStates().size();
		EpitheliumGrid stableState = this.monteCarlo.getStableStates().get(this.lastStableStateIndex-1);
		for (int x = 0; x < stableState.getX(); x++) {
			for (int y = 0; y < stableState.getY(); y++) {
				this.clonedEpi.getEpitheliumGrid().setCellState(x, y, stableState.getCellState(x, y));
			}
			}
		
		updatejlIteration(stableState);
		if (this.lastStableStateIndex >1){
			this.jbBack.setEnabled(true);
			this.jbRewind.setEnabled(true);
			}
			else{
				this.jbBack.setEnabled(false);
				this.jbRewind.setEnabled(false);	
			}
		if (this.lastStableStateIndex !=this.monteCarlo.getStableStates().size()){
			this.jbStep.setEnabled(true);
			this.jbFastFwr.setEnabled(true);
		}
		else{
			this.jbStep.setEnabled(false);
			this.jbFastFwr.setEnabled(false);
		}
		updateComponentList(this.jccb.getSelectedItems());
		this.vgCellState.repaint();
		this.repaint();	
	}


	protected void simulationStepBack() {
			this.lastStableStateIndex = this.lastStableStateIndex-1;
			EpitheliumGrid stableState = this.monteCarlo.getStableStates().get(this.lastStableStateIndex-1);
			for (int x = 0; x < stableState.getX(); x++) {
				for (int y = 0; y < stableState.getY(); y++) {
					this.clonedEpi.getEpitheliumGrid().setCellState(x, y, stableState.getCellState(x, y));
				}
				}
			updatejlIteration(stableState);
			if (this.lastStableStateIndex >1){
				this.jbBack.setEnabled(true);
				this.jbRewind.setEnabled(true);
				}
				else{
					this.jbBack.setEnabled(false);
					this.jbRewind.setEnabled(false);	
				}
			if (this.lastStableStateIndex !=this.monteCarlo.getStableStates().size()){
				this.jbStep.setEnabled(true);
				this.jbFastFwr.setEnabled(true);
			}
			else{
				this.jbStep.setEnabled(false);
				this.jbFastFwr.setEnabled(false);
			}
			updateComponentList(this.jccb.getSelectedItems());
			this.vgCellState.repaint();
			this.repaint();
			
	}

	protected void simulationRewind() {
		this.lastStableStateIndex = 1;
		EpitheliumGrid stableState = this.monteCarlo.getStableStates().get(this.lastStableStateIndex-1);
		for (int x = 0; x < stableState.getX(); x++) {
			for (int y = 0; y < stableState.getY(); y++) {
				this.clonedEpi.getEpitheliumGrid().setCellState(x, y, stableState.getCellState(x, y));
			}
			}
		
		updatejlIteration(stableState);
		if (this.lastStableStateIndex >1){
			this.jbBack.setEnabled(true);
			this.jbRewind.setEnabled(true);
			}
			else{
				this.jbBack.setEnabled(false);
				this.jbRewind.setEnabled(false);	
			}
		if (this.lastStableStateIndex !=this.monteCarlo.getStableStates().size()){
			this.jbStep.setEnabled(true);
			this.jbFastFwr.setEnabled(true);
		}
		else{
			this.jbStep.setEnabled(false);
			this.jbFastFwr.setEnabled(false);
		}
		updateComponentList(this.jccb.getSelectedItems());
		this.vgCellState.repaint();
		this.repaint();
	}

	protected void simulationStepFwr() {
		this.lastStableStateIndex = this.lastStableStateIndex + 1;
		EpitheliumGrid stableState = this.monteCarlo.getStableStates().get(this.lastStableStateIndex-1);
		for (int x = 0; x < stableState.getX(); x++) {
			for (int y = 0; y < stableState.getY(); y++) {
				this.clonedEpi.getEpitheliumGrid().setCellState(x, y, stableState.getCellState(x, y));
			}
			}
		
		updatejlIteration(stableState);
		if (this.lastStableStateIndex >1){
			this.jbBack.setEnabled(true);
			this.jbRewind.setEnabled(true);
			}
			else{
				this.jbBack.setEnabled(false);
				this.jbRewind.setEnabled(false);	
			}
		if (this.lastStableStateIndex !=this.monteCarlo.getStableStates().size()){
			this.jbStep.setEnabled(true);
			this.jbFastFwr.setEnabled(true);
		}
		else{
			this.jbStep.setEnabled(false);
			this.jbFastFwr.setEnabled(false);
		}
		this.vgCellState = new VisualGridSimulation(stableState,this.projectFeatures,this.lCompON,this.gridInformation);
		updateComponentList(this.jccb.getSelectedItems());
		
		this.vgCellState.repaint();
		this.repaint();	
	}

	private int updatejlIteration(EpitheliumGrid stableState) {
		//TODO: The iteration must be a range
		//TODO: Only the unique should be shown
		//TODO: How many repeated stable states are there
		int iteration = this.monteCarlo.getStableState2Iteration().get(stableState);
		this.jlStep.setText("Stable State: " +this.lastStableStateIndex + " of "+this.monteCarlo.getUniqueStableStates().size()+ " [Iteration: " +iteration+"]");
		this.jlStep.repaint();
		return iteration;
	}

	
	
	private JPanel createMonteCarloVisualDefinitionsTop(){
		
		JPanel monteCarloVisualDefinitionsTOP = new JPanel(new BorderLayout());
		
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
		
		return monteCarloVisualDefinitionsTOP;
	}
	
	private void createMonteCarloVisualDefinitionsCenter(){
		
		this.monteCarloVisualDefinitionsCenter = new JPanel(new BorderLayout());
		this.jpRCenter = new JPanel();
		
		this.rTop = new JPanel();
		this.rTop.setLayout(new BoxLayout(this.rTop, BoxLayout.Y_AXIS));

		// Model selection list
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.clonedEpi.getEpitheliumGrid().getModelSet());
				this.newModelCombobox();
		
		this.rTop.add(jccb);
		this.jccb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboCheckBox jccb = (JComboCheckBox) e.getSource();

				jccb.updateSelected();
				updateComponentList(jccb.getSelectedItems());
			}
		});

		// Select / Deselect buttons
		JPanel rTopSel = new JPanel(new FlowLayout());
		// rTopSel.setLayout(new BoxLayout(rTopSel, BoxLayout.X_AXIS));
		JButton jbSelectAll = new JButton("Select All");
		jbSelectAll.setMargin(new Insets(0, 0, 0, 0));
		jbSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (String nodeID : lPresentComps) {
				if (mNodeID2Checkbox.containsKey(nodeID)) {
					mNodeID2Checkbox.get(nodeID).setSelected(true);
				}
				mSelCheckboxes.put(nodeID, true);
				if (!lCompON.contains(nodeID))
					lCompON.add(nodeID);
			}
				fireVisualChange();
			}
		});
		rTopSel.add(jbSelectAll);
		
		JButton jbDeselectAll = new JButton("Deselect All");
		jbDeselectAll.setMargin(new Insets(0, 0, 0, 0));
		jbDeselectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (String nodeID : lPresentComps) {
				if (mNodeID2Checkbox.containsKey(nodeID)) {
					mNodeID2Checkbox.get(nodeID).setSelected(false);
				}
				mSelCheckboxes.put(nodeID, false);
				lCompON.remove(nodeID);
			}
				fireVisualChange();
			}
		});
		rTopSel.add(jbDeselectAll);

		this.rTop.add(rTopSel);

		this.rTop
				.setBorder(BorderFactory.createTitledBorder("Display options"));
		
		this.monteCarloVisualDefinitionsCenter.add(this.rTop, BorderLayout.PAGE_START);
	

		
		this.jpRCenter.setLayout(new BoxLayout(jpRCenter, BoxLayout.Y_AXIS));
		JScrollPane jscroll = new JScrollPane(this.jpRCenter);
		
		jscroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.jpRCenter.setBackground(backColor);
		this.monteCarloVisualDefinitionsCenter.add(jscroll, BorderLayout.CENTER);

		this.monteCarloVisualDefinitionsCenter.setBackground(backColor);
		
	}
	
	private JPanel createMonteCarloVisualDefinitions() {
		
		JPanel monteCarloVisualDefinitions = new JPanel(new BorderLayout());
		
		JPanel monteCarloVisualDefinitionsTOP = createMonteCarloVisualDefinitionsTop();
		createMonteCarloVisualDefinitionsCenter();
		
		monteCarloVisualDefinitions.setBorder(BorderFactory.createTitledBorder("MonteCarlo Visual Definitions"));
		monteCarloVisualDefinitions.add(monteCarloVisualDefinitionsTOP,BorderLayout.PAGE_START);
		monteCarloVisualDefinitions.add(this.monteCarloVisualDefinitionsCenter,BorderLayout.CENTER);
		
		return monteCarloVisualDefinitions;
	}



	protected JPanel fireCumulativeGrid(JPanel jpanel) {
		//TODO
		System.out.println("Starting the Cumulative");
		this.monteCarlo.createCumulative();
		
		return jpanel;
	}


	protected JPanel fireStableStatesGrid(JPanel jpanel) {
		return jpanel;
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
		
		JComboBox<String> jcbInitialConditions = new JComboBox<String>(ListInitialConditions);
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
		if (!flag)
			setInicialConditions();
		
		fireVisualChange();
		this.monteCarlo.setMonteCarloInitialConditions(flag);		
		
	}

	private void setInicialConditions(){
			for (int x = 0; x < this.clonedEpi.getX(); x++) {
				for (int y = 0; y < this.clonedEpi.getY(); y++) {
					this.clonedEpi.getEpitheliumGrid().setCellState(x, y, this.epithelium.getEpitheliumGrid().getCellState(x, y));
					}}}

	protected void fireEnableRun(boolean b) {
		this.jbRun.setEnabled(b);
		this.repaint();
	}

	protected void fireChangeMaxIter(int text) {
		this.monteCarlo.setMaxIter(text);	
	}

	protected void fireChangeNumRuns(int nRuns) {
		this.monteCarlo.setNumberRuns(nRuns);
//		System.out.println("Number of Runs changed to: " + nRuns);
	}

	protected void fireRun() {
		this.lastStableStateIndex=0;
		this.monteCarlo.run(this.epithelium.clone());
		
			if (this.lastStableStateIndex >1){
				this.jbBack.setEnabled(true);
				this.jbRewind.setEnabled(true);
				}
				else{
					this.jbBack.setEnabled(false);
					this.jbRewind.setEnabled(false);	
				}
			if (this.lastStableStateIndex !=this.monteCarlo.getStableStates().size()){
				this.jbStep.setEnabled(true);
				this.jbFastFwr.setEnabled(true);
			}
			else{
				this.jbStep.setEnabled(false);
				this.jbFastFwr.setEnabled(false);
			}
			if (this.monteCarlo.getStableStates().size()>0){
			EpitheliumGrid stableState= this.monteCarlo.getStableStates().get(0);
			this.vgCellState = new VisualGridSimulation(stableState,this.projectFeatures,this.lCompON,this.gridInformation);
			this.repaint();
			simulationStepFwr();
			this.jbClone.setEnabled(true);
			this.jbPicture.setEnabled(true);
			this.jbSaveAll.setEnabled(true);


			updatejlIteration(stableState);
			this.repaint();
			}
			
			updateInformationPanel();
			
			this.vgCellState.repaint();
			this.repaint();
		}
			
			
	

	private void updateInformationPanel() {

		System.out.println(this.monteCarlo.getStableStates().size());
		this.uniqueSS.setText("Unique SS: " + this.monteCarlo.getUniqueStableStates().size());
		this.notReachedSS.setText("Not reached SS: " + (this.monteCarlo.getNumberRuns() - this.monteCarlo.getStableStates().size()));
		this.uniqueSS.repaint();
		this.notReachedSS.repaint();
		this.informationPanel.repaint();
		this.jpLeft.repaint();
		
	}

	@Override
	public String getName() {
		return "MonteCarlo";
	}

	@Override
	public boolean canClose() {
		return true;
	}

	@Override
	public void applyChange() {
		//No changes are applied
	}
	
	
	private void newModelCombobox(){
	// Model combobox
	List<LogicalModel> modelList = new ArrayList<LogicalModel>(
			this.clonedEpi.getEpitheliumGrid().getModelSet());
	JCheckBox[] items = new JCheckBox[modelList.size()];
	for (int i = 0; i < modelList.size(); i++) {
		items[i] = new JCheckBox(
				this.projectFeatures.getModelName(modelList.get(i)));
		items[i].setSelected(false);
	}
	this.jccb = new JComboCheckBox(items);
	
	}
	//When a new model is selected, and at the beggining 
	private void updateComponentList(List<String> modelNames) {
		this.jpRCenter.removeAll();
		this.mNode2ValueSelected.clear();
		this.lCompON.clear();

		this.lCompON.clear();

		List<LogicalModel> lModels = new ArrayList<LogicalModel>();
		for (String modelName : modelNames) {
			lModels.add(this.projectFeatures.getModel(modelName));
		}
		this.lPresentComps = new ArrayList<String>();

		Set<String> sInternalNodeIDs = new HashSet<String>();
		Set<String> sInputNodeIDs = new HashSet<String>();
		Set<String> sCommonNodeIDs = new HashSet<String>();

		List<NodeInfo> lInternal = new ArrayList<NodeInfo>(this.projectFeatures.getModelsNodeInfos(lModels, false));

		for (NodeInfo node : lInternal)
			sInternalNodeIDs.add(node.getNodeID());

		List<NodeInfo> lInputs = new ArrayList<NodeInfo>(this.projectFeatures.getModelsNodeInfos(lModels, true));

		for (NodeInfo node : lInputs) {
			if (sInternalNodeIDs.contains(node.getNodeID())) {
				sCommonNodeIDs.add(node.getNodeID());
				sInternalNodeIDs.remove(node.getNodeID());
			} else {
				sInputNodeIDs.add(node.getNodeID());
			}
		}

		if (!sCommonNodeIDs.isEmpty())
			this.setComponentTypeList(sCommonNodeIDs, "Input/Internal Components");
		if (!sInternalNodeIDs.isEmpty())
			this.setComponentTypeList(sInternalNodeIDs, "Internal Components");
		if (!sInputNodeIDs.isEmpty())
			this.setComponentTypeList(sInputNodeIDs, "Input Components");
		
//		this.vgCellState.paintComponent(this.vgCellState.getGraphics());
		this.jpRCenter.revalidate();
		this.jpRCenter.repaint();
	}
	
	private void setComponentTypeList(Set<String> sNodeIDs, String titleBorder) {
		JPanel jpRRC = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 4, 0);
		jpRRC.setBorder(BorderFactory.createTitledBorder(titleBorder));
		List<String> nodeList = new ArrayList<String>(sNodeIDs);
		Collections.sort(nodeList, ObjectComparator.STRING);
		int y = 0;
		for (String nodeID : nodeList) {
			this.lPresentComps.add(nodeID);
			if (mSelCheckboxes.get(nodeID)) {
				this.lCompON.add(nodeID);
			}
			this.getCompMiniPanel(jpRRC, gbc, y, nodeID);
			y++;
		}
		this.jpRCenter.add(jpRRC);
	}

	
	private void fireVisualChange(){
		if (this.vgCellState
				.getGraphics()!=null){
		this.vgCellState.paintComponent(this.vgCellState
				.getGraphics());
		this.vgCellState.repaint();
		}
		this.repaint();
	}
	
	private void getCompMiniPanel(JPanel jp, GridBagConstraints gbc, int y,
			String nodeID) {
		
		JLabel percentage = new JLabel(this.clonedEpi.getEpitheliumGrid().getPercentage(nodeID));
		
		gbc.gridy = y;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		JCheckBox jcb = this.mNodeID2Checkbox.get(nodeID);
		JPanel jpanel = new JPanel(new BorderLayout());
		
		if (jcb == null) {
			jcb = new JCheckBox(nodeID, mSelCheckboxes.get(nodeID));
			jcb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JCheckBox jcb = (JCheckBox) e.getSource();
					mSelCheckboxes.put(jcb.getText(), jcb.isSelected());
					fireVisualChange();
					if (jcb.isSelected()) {
						lCompON.add(jcb.getText());
					} else {
						lCompON.remove(jcb.getText());
					}
					fireVisualChange();
				}
			});
			this.mNodeID2Checkbox.put(nodeID, jcb);
		}
		
		jpanel.add(jcb,BorderLayout.CENTER);
//		jpanel.add(this.monteCarlo.getPercentage(nodeID,this.clonedEpi.getEpitheliumGrid()),BorderLayout.EAST);
		
//		this.clonedEpi.getEpitheliumGrid().getPercentage(nodeID);
		
//		jp.add(jcb, gbc);
		jp.add(jpanel,gbc);
		gbc.gridx = 1;
		JButton jbColor = new JButton();
		jbColor.setBackground(this.projectFeatures
				.getNodeColor(nodeID));
		jbColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setNewColor((JButton) e.getSource());
				fireVisualChange();
			}
		});
		jp.add(jbColor, gbc);
		gbc.gridx = 2;
		jp.add(percentage,gbc);
		this.colorButton2Node.put(jbColor, nodeID);
		
	}


	
	private void setNewColor(JButton jb) {
		String nodeID = this.colorButton2Node.get(jb);
		Color newColor = JColorChooser.showDialog(jb, "Color chooser - "
				+ nodeID, jb.getBackground());
		if (newColor != null
				&& !newColor.equals(this.projectFeatures.getNodeColor(nodeID))) {
			jb.setBackground(newColor);
			this.clonedEpi.getProjectFeatures().setNodeColor(nodeID, newColor);
			this.projChanged.setChanged(this);
			this.vgCellState.paintComponent(this.vgCellState
					.getGraphics());
		}
	}
	
	
	
	private void createGUIForModel(LogicalModel m) {

		for (NodeInfo node : m.getNodeOrder()) {
			String nodeID = node.getNodeID();
			// Color
			JButton jButton = new JButton();
			jButton.setBackground(this.projectFeatures
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

	private void cloneEpiWithCurrGrid() {
//		this.epithelium.cloneEpithelium(this.epithelium,
//				this.monteCarlo.getStableStates().get(this.lastStableStateIndex));
//		System.out.println("NOT WORKING");
		JPanel frame = new JPanel();
		JOptionPane.showMessageDialog(frame, "Not Working yet");
		
	}
	
	
}