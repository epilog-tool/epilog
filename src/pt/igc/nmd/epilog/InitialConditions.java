package pt.igc.nmd.epilog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

import pt.igc.nmd.epilog.gui.MainFrame;

public class InitialConditions extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5470169327482303234L;

	private MainFrame mainFrame;

	private Hashtable<JComboBox, Integer> Jcombo2Node;
	private Hashtable<JCheckBox, Integer> Jcheck2Node;
	private Hashtable<JButton, NodeInfo> button2Node;

	private JComboBox[] node2Jcombo;
	private JCheckBox[] node2Jcheck;
	private JComboBox[] initialStatePerComponent;
	private JCheckBox nodeBox[];

	private JPanel auxiliaryPanel[];

	private JButton[] colorButton;

	private JTextField setName;
	private JComboBox sets;

	public InitialConditions(MainFrame mainPanel) {

		this.mainFrame = mainPanel;
		init();
	}

	public void init() {

		Color backgroundColor = mainFrame.getBackground();

		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);

		setLayout(new BorderLayout());

		LogicalModel unitaryModel = mainFrame.getEpithelium().getUnitaryModel();

		if (unitaryModel != null) {

			// PAGE START PANEL

			layout.setAlignment(FlowLayout.LEFT);
			JPanel optionsPanel = new JPanel();
			optionsPanel.setLayout(layout);

			JButton buttonMarkAll = new JButton("Apply All");
			JButton buttonClearAll = new JButton("Clear All");
			JButton buttonFill = new JButton("Fill");

			buttonFill.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					// TO DO: Fill has to check for each component individually
					// if
					// they have non zero expression neighbors. The fact that
					// one
					// component closes doesn't mean that the others do
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

			buttonAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO: Add the selected grid to the sets of initial
					// conditions with the name of the textfield
					initialConditionsAdd();
				}
			});

			buttonClear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO: Erase from the list of sets a specific set
				}
			});

			sets.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO: Choose a set and insert the name of that set on the
					// textfield
					loadInitialconditions();
				}
			});

			endPanel.add(setName);
			endPanel.add(buttonAdd);
			endPanel.add(sets);
			endPanel.add(buttonClear);

			add(endPanel, BorderLayout.PAGE_END);

			// CENTER PANEL
			JPanel panelCenter = new JPanel();
			panelCenter.setLayout(layout);
			List<NodeInfo> listNodes = mainFrame.epithelium.getUnitaryModel()
					.getNodeOrder();

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

				if (!listNodes.get(i).isInput()) {

					auxiliaryPanel[i] = new JPanel();
					auxiliaryPanel[i].setLayout(layout);
					auxiliaryPanel[i].setBackground(backgroundColor);
					auxiliaryPanel[i].setBorder(BorderFactory
							.createEtchedBorder(EtchedBorder.LOWERED));

					nodeBox[i] = new JCheckBox(listNodes.get(i).getNodeID());
					nodeBox[i].setBackground(backgroundColor);
					nodeBox[i].setToolTipText(listNodes.get(i).getNodeID());

					colorButton[i] = new JButton("");
					button2Node.put(colorButton[i], listNodes.get(i));
					colorButton[i].setBackground(mainFrame.epithelium
							.getColor(listNodes.get(i)));

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
							fillHexagons();
						}
					});

					initialStatePerComponent[i] = new JComboBox();
					for (int maxValue = 0; maxValue < listNodes.get(i).getMax() + 1; maxValue++) {
						initialStatePerComponent[i].addItem(maxValue);
					}
					Jcombo2Node.put(initialStatePerComponent[i], i);
					node2Jcombo[i] = initialStatePerComponent[i];

					mainFrame.epithelium.setInitialState(listNodes
							.get(Jcombo2Node.get(initialStatePerComponent[i])),
							(byte) 0);

					initialStatePerComponent[i]
							.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent arg0) {
									JComboBox src = (JComboBox) arg0
											.getSource();
									fireInitialStateChange(src);
								}
							});

					auxiliaryPanel[i].add(nodeBox[i]);
					auxiliaryPanel[i].add(initialStatePerComponent[i]);
					auxiliaryPanel[i].add(colorButton[i]);
					panelCenter.add(auxiliaryPanel[i]);
				}
			}
			add(panelCenter, BorderLayout.CENTER);
		}

	}

	private void setNewColor(JButton src) {

		Color newColor = JColorChooser.showDialog(src, "Color Chooser",
				mainFrame.epithelium.getColor(button2Node.get(src)));
		src.setBackground(newColor);
		mainFrame.epithelium.setColor(button2Node.get(src), newColor);
		fillHexagons();
	}

	private void loadInitialconditions() {
		setName.setText((String) sets.getSelectedItem());
	}

	private void fireInitialStateChange(JComboBox combo) {
		mainFrame.getEpithelium().setInitialState(
				mainFrame.getEpithelium().getUnitaryModel().getNodeOrder()
						.get(Jcombo2Node.get(combo)),
				((Integer) combo.getSelectedItem()).byteValue());
	}

	public void setComponentDisplay(int i, boolean b) {
		mainFrame.getEpithelium().setDefinitionsComponentDisplay(i, b);
	}

	public void clearAllCells() {
		// adicionar try catch para textFx e fy
		mainFrame.epithelium.initializeGrid();
		fillHexagons();
	}

	public void markAllCells() {

		for (int i = 0; i < mainFrame.topology.getWidth(); i++) {
			for (int j = 0; j < mainFrame.topology.getHeight(); j++) {
				mainFrame.epithelium.setInitialState(i, j);
			}
		}
		fillHexagons();
	}

	public void fillHexagons() {
		mainFrame.fillHexagons();
	}

	// End Methods

	private void initialConditionsAdd() {
		String name = setName.getText();
		if (!mainFrame.epithelium.getInitialStateSet().containsKey(name))
			sets.addItem(name);
		mainFrame.epithelium.setInitalConditionsSet(name);
		System.out.println(name);

	}



}
