package pt.igc.nmd.epilog.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;


public class InitialConditions extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5470169327482303234L;

	private MainFrame mainFrame;

	private Hashtable<JComboBox, Integer> Jcombo2Node;
	public Hashtable<JCheckBox, Integer> Jcheck2Node;
	private Hashtable<JButton, NodeInfo> button2Node;

	private JComboBox[] node2Jcombo;
	private JCheckBox[] node2Jcheck;
	private JComboBox[] initialStatePerComponent;
	public JCheckBox nodeBox[];

	private JPanel auxiliaryPanel[];

	private JButton[] colorButton;

	private JTextField setName;
	private JComboBox sets;

	public JButton buttonFill;

	public InitialConditions(MainFrame mainPanel) {

		this.mainFrame = mainPanel;
		init();
	}

	/**
	 * Initializes the initial conditions panel, to be inserted in the tab on
	 * Epilog's main panel.
	 * 
	 */
	public void init() {

		Color backgroundColor = this.mainFrame.getBackground();

		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);

		setLayout(new BorderLayout());

		LogicalModel unitaryModel = this.mainFrame.epithelium.getUnitaryModel();

		if (unitaryModel != null) {

			// PAGE START PANEL

			layout.setAlignment(FlowLayout.LEFT);
			JPanel optionsPanel = new JPanel();
			optionsPanel.setLayout(layout);

			JButton buttonMarkAll = new JButton("Apply All");
			JButton buttonClearAll = new JButton("Clear All");
			buttonFill = new JButton("Rectangle Fill");
			JButton buttonDeselectAll = new JButton("Deselect all");
			JButton buttonSelectAll = new JButton("Select all");

			buttonFill.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fireOnFill();

				}
			});

			buttonDeselectAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					for (JCheckBox singleNodeBox : nodeBox) {

						singleNodeBox.setSelected(false);
						setComponentDisplay(Jcheck2Node.get(singleNodeBox),
								singleNodeBox.isSelected());
					}
					mainFrame.fillHexagons();
				}
			});

			buttonSelectAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					for (JCheckBox singleNodeBox : nodeBox) {

						singleNodeBox.setSelected(true);
						setComponentDisplay(Jcheck2Node.get(singleNodeBox),
								singleNodeBox.isSelected());
					}
					mainFrame.fillHexagons();
				}
			});

			buttonMarkAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					markAllCells();
				}
			});

			buttonClearAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					clearAllCells();
				}
			});

			optionsPanel.add(buttonMarkAll);
			optionsPanel.add(buttonClearAll);
			optionsPanel.add(buttonFill);
			optionsPanel.add(buttonDeselectAll);
			optionsPanel.add(buttonSelectAll);

			add(optionsPanel, BorderLayout.PAGE_START);

			// END PANEL

			JPanel endPanel = new JPanel(layout);

			JButton buttonAdd = new JButton("+");
			JButton buttonClear = new JButton("-");
			setName = new JTextField("", 15);
			sets = new JComboBox();
			sets.setPreferredSize(new Dimension(
					setName.getPreferredSize().width,
					sets.getPreferredSize().height));

			if (this.mainFrame.epithelium.getInitialStateSet() != null) {
				for (String key : this.mainFrame.epithelium
						.getInitialStateSet().keySet()) {
					sets.addItem(key);
				}
			}

			buttonAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO: Add the selected grid to the sets of initial
					// conditions with the name of the textfield
					initialConditionsAdd();
				}
			});

			buttonClear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					removeElementFromSet();
				}
			});

			sets.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO: Choose a set and insert the name of that set on the
					// textfield
					loadInitialConditionsSet();
					mainFrame.fillHexagons();
				}
			});

			endPanel.add(setName);
			endPanel.add(buttonAdd);
			endPanel.add(sets);
			endPanel.add(buttonClear);

			add(endPanel, BorderLayout.PAGE_END);

			// CENTER PANEL
			JPanel panelCenter = new JPanel();
			JPanel panelEnd = new JPanel();
			JPanel panelCenterAux = new JPanel(new BorderLayout());
			panelCenter.setLayout(layout);
			panelEnd.setLayout(layout);
			List<NodeInfo> listNodes = this.mainFrame.epithelium
					.getUnitaryModel().getNodeOrder();

			auxiliaryPanel = new JPanel[listNodes.size()];

			nodeBox = new JCheckBox[listNodes.size()];
			colorButton = new JButton[listNodes.size()];

			node2Jcheck = new JCheckBox[listNodes.size()];
			node2Jcombo = new JComboBox[listNodes.size()];

			initialStatePerComponent = new JComboBox[listNodes.size()];

			Jcheck2Node = new Hashtable<JCheckBox, Integer>();
			Jcombo2Node = new Hashtable<JComboBox, Integer>();
			button2Node = new Hashtable<JButton, NodeInfo>();

			for (int i = 0; i < listNodes.size(); i++) {

				auxiliaryPanel[i] = new JPanel();
				auxiliaryPanel[i].setLayout(layout);
				auxiliaryPanel[i].setBackground(backgroundColor);
				auxiliaryPanel[i].setBorder(BorderFactory
						.createEtchedBorder(EtchedBorder.LOWERED));

				nodeBox[i] = new JCheckBox(listNodes.get(i).getNodeID());
				nodeBox[i].setBackground(backgroundColor);
				nodeBox[i].setToolTipText(listNodes.get(i).getNodeID());

				colorButton[i] = new JButton("");
				colorButton[i].setBackground(this.mainFrame.epithelium
						.getColor(listNodes.get(i)));
				button2Node.put(colorButton[i], listNodes.get(i));

				colorButton[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						JButton src = (JButton) arg0.getSource();
						setNewColor(src);
					}
				});

				Jcheck2Node.put(nodeBox[i], i);
				node2Jcheck[i] = nodeBox[i];

				nodeBox[i].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						JCheckBox src = (JCheckBox) arg0.getSource();
						setComponentDisplay(Jcheck2Node.get(src),
								src.isSelected());
						mainFrame.fillHexagons();
					}
				});

				initialStatePerComponent[i] = new JComboBox();
				for (int maxValue = 0; maxValue < listNodes.get(i).getMax() + 1; maxValue++) {
					initialStatePerComponent[i].addItem(maxValue);
				}
				Jcombo2Node.put(initialStatePerComponent[i], i);
				node2Jcombo[i] = initialStatePerComponent[i];

				this.mainFrame.epithelium.setInitialState(listNodes
						.get(Jcombo2Node.get(initialStatePerComponent[i])),
						(byte) 0);

				initialStatePerComponent[i]
						.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								JComboBox src = (JComboBox) arg0.getSource();
								fireInitialStateChange(src);
							}
						});

				auxiliaryPanel[i].add(nodeBox[i]);
				auxiliaryPanel[i].add(initialStatePerComponent[i]);
				auxiliaryPanel[i].add(colorButton[i]);

				if (this.mainFrame.epithelium.isIntegrationComponent(i))
					auxiliaryPanel[i].setVisible(false);
				else
					auxiliaryPanel[i].setVisible(true);

				if (listNodes.get(i).isInput())
					panelEnd.add(auxiliaryPanel[i]);
				else
					panelCenter.add(auxiliaryPanel[i]);

				LineBorder border = new LineBorder(Color.black, 1, true);
				TitledBorder titleProperComponents = new TitledBorder(border,
						"Proper Components", TitledBorder.LEFT,
						TitledBorder.DEFAULT_POSITION, new Font("Arial",
								Font.ITALIC, 14), Color.black);

				TitledBorder titleInputs = new TitledBorder(border, "Inputs",
						TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
						new Font("Arial", Font.ITALIC, 14), Color.black);

				panelCenter.setBorder(titleProperComponents);
				panelEnd.setBorder(titleInputs);

			} // End Center Panel

			panelCenterAux.add(panelCenter, BorderLayout.CENTER);
			panelCenterAux.add(panelEnd, BorderLayout.PAGE_END);

			add(panelCenterAux, BorderLayout.CENTER);

		}

	}

	
	//Color and Drawing
	
	/**
	 * Changes the color associated with a component.
	 * 
	 * @param src button associated with a components color
	 * @see mainFrame.epithelium.setColor
	 */
	public void setNewColor(JButton src) {

		Color newColor = JColorChooser.showDialog(src, "Color Chooser",
				this.mainFrame.epithelium.getColor(button2Node.get(src)));
		src.setBackground(newColor);
		this.mainFrame.epithelium.setColor(button2Node.get(src), newColor);
		mainFrame.fillHexagons();
	}

	
	/**
	 * Sets the component as selected or not.
	 * 
	 * @param i index of the component in the nodeList
	 * @param b boolean value: true if node selected, false otherwise
	 * @see this.mainFrame.epithelium.setDefinitionsComponentDisplay()
	 */
	public void setComponentDisplay(int i, boolean b) {
		this.mainFrame.epithelium.setDefinitionsComponentDisplay(i, b);
	}
	
	
	/**
	 * Clear all instances to a initial state of zero.
	 * 
	 * @see mainFrame.fillHexagons()
	 * @see this.mainFrame.epithelium.initializeGrid();
	 */
	public void clearAllCells() {
		this.mainFrame.epithelium.initializeGrid();
		this.mainFrame.fillHexagons();
	}
	
	
	/**
	 * Marks all instances with the selected components values.
	 * 
	 * @see mainFrame.fillHexagons()
	 * @see this.mainFrame.epithelium.setInitialState(instance);
	 */
	public void markAllCells() {
		
		for (int instance = 0; instance < this.mainFrame.topology
				.getNumberInstances(); instance++) {
			
			this.mainFrame.epithelium.setInitialState(instance);
		}
		this.mainFrame.fillHexagons();
	}
	
	/**
	 * Changes the color of the rectangle fill to yellow if selected and sets the value of the control variable fill as true or false.
	 * 
	 * @see mainFrame.setFill(boolean b);
	 * 
	 */
	private void fireOnFill() {
		if (!this.mainFrame.isFillOn()) {
			buttonFill.setBackground(Color.yellow);
			this.mainFrame.setFill(true);
		} else {
			this.mainFrame.setFill(false);
			buttonFill.setBackground(this.getBackground());
		}
	}
	
	
	/**
	 * Loads the initial conditions set selected when changing the sets at the initial conditions panel.
	 * 
	 */
	private void loadInitialConditionsSet() {
		setName.setText((String) sets.getSelectedItem());
		if (sets.getSelectedItem() != null)
			if (this.mainFrame.epithelium.getInitialStateSet().get(
					(String) sets.getSelectedItem()) != null)
				this.mainFrame.epithelium.setSelectedInitialSet((String) sets
						.getSelectedItem());
	}

	/**
	 * Changes the initial state associated with a component.
	 * 
	 * @param combo ComboBox associated with an initial value and a component.
	 */
	private void fireInitialStateChange(JComboBox combo) {
		this.mainFrame.epithelium.setInitialState(this.mainFrame.epithelium
				.getUnitaryModel().getNodeOrder().get(Jcombo2Node.get(combo)),
				((Integer) combo.getSelectedItem()).byteValue());
	}



	// End Methods
	
	/**
	 * Adds an initial conditions set. If a name is already used, then the new set replaces the old one.
	 * 
	 */
	private void initialConditionsAdd() {
		String name = setName.getText();
		if (!this.mainFrame.epithelium.getInitialStateSet().containsKey(name))
			sets.addItem(name);
		this.mainFrame.epithelium.setInitialStateSet(name);
	}

	/**
	 * Removes an element from the set of initial conditions sets.
	 * 
	 */
	private void removeElementFromSet() {
		String setToRemove = (String) sets.getSelectedItem();
		this.mainFrame.epithelium.getInitialStateSet().remove(setToRemove);
		setName.setText("");
		sets.removeAllItems();

		for (String string : this.mainFrame.epithelium.getInitialStateSet()
				.keySet())
			if (string != "none")
				sets.addItem(string);
	}

}
