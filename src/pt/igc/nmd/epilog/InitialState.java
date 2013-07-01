package pt.igc.nmd.epilog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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

public class InitialState extends JPanel {

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

	public InitialState(MainFrame mainPanel) {

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
			buttonFill = new JButton("Fill");
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
					fillHexagons();
				}
			});

			buttonSelectAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					for (JCheckBox singleNodeBox : nodeBox) {

						singleNodeBox.setSelected(true);
						setComponentDisplay(Jcheck2Node.get(singleNodeBox),
								singleNodeBox.isSelected());
					}
					fillHexagons();
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

			if (mainFrame.epithelium.getInitialStateSet() != null) {
				for (String key : mainFrame.epithelium.getInitialStateSet()
						.keySet()) {
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
					loadInitialconditions();
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
			panelCenter.setLayout(layout);
			List<NodeInfo> listNodes = mainFrame.epithelium.getUnitaryModel()
					.getNodeOrder();

			List<NodeInfo> properComponentsList = new ArrayList<NodeInfo>();

			for (int i = 0; i < listNodes.size(); i++) {
				if (!listNodes.get(i).isInput())
					properComponentsList.add(listNodes.get(i));
			}
			auxiliaryPanel = new JPanel[listNodes.size()];

			nodeBox = new JCheckBox[properComponentsList.size()];
			colorButton = new JButton[properComponentsList.size()];

			node2Jcheck = new JCheckBox[properComponentsList.size()];
			node2Jcombo = new JComboBox[properComponentsList.size()];

			initialStatePerComponent = new JComboBox[properComponentsList
					.size()];

			Jcheck2Node = new Hashtable<JCheckBox, Integer>();
			Jcombo2Node = new Hashtable<JComboBox, Integer>();
			button2Node = new Hashtable<JButton, NodeInfo>();

			for (int i = 0; i < properComponentsList.size(); i++) {

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
					colorButton[i].setBackground(mainFrame.epithelium
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
		if (sets.getSelectedItem() != null)
			if (mainFrame.epithelium.getInitialStateSet().get(
					(String) sets.getSelectedItem()) != null)
				mainFrame.epithelium.setSelectedInitialSet((String) sets
						.getSelectedItem());
	}

	private void fireInitialStateChange(JComboBox combo) {
		mainFrame.epithelium.setInitialState(mainFrame.epithelium
				.getUnitaryModel().getNodeOrder().get(Jcombo2Node.get(combo)),
				((Integer) combo.getSelectedItem()).byteValue());
	}

	public void setComponentDisplay(int i, boolean b) {
		mainFrame.epithelium.setDefinitionsComponentDisplay(i, b);
	}

	public void clearAllCells() {
		// adicionar try catch para textFx e fy
		mainFrame.epithelium.initializeGrid();
		fillHexagons();
	}

	public void markAllCells() {

		for (int instance = 0; instance < mainFrame.topology
				.getNumberInstances(); instance++) {

			mainFrame.epithelium.setInitialState(instance);
		}
		fillHexagons();
	}

	private void fireOnFill() {
		if (!mainFrame.isFillOn()) {
			buttonFill.setBackground(Color.yellow);
			mainFrame.setFill(true);
		} else {
			mainFrame.setFill(false);
			buttonFill.setBackground(this.getBackground());
		}
	}

	public void fillHexagons() {
		mainFrame.fillHexagons();
	}

	// End Methods

	private void initialConditionsAdd() {
		String name = setName.getText();
		if (!mainFrame.epithelium.getInitialStateSet().containsKey(name))
			sets.addItem(name);
		mainFrame.epithelium.setInitialStateSet(name);
		System.out.println(name);

	}

	private void removeElementFromSet() {
		String setToRemove = (String) sets.getSelectedItem();
		mainFrame.epithelium.getInitialStateSet().remove(setToRemove);
		System.out.println("I want to remove: " + setToRemove);
		setName.setText("");
		sets.removeAllItems();

		for (String string : mainFrame.epithelium.getInitialStateSet().keySet())
			if (string != "none")
				sets.addItem(string);
	}

}
