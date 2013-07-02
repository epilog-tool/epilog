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
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.colomoto.logicalmodel.perturbation.FixedValuePerturbation;
import org.colomoto.logicalmodel.perturbation.MultiplePerturbation;
import org.colomoto.logicalmodel.perturbation.RangePerturbation;

import pt.igc.nmd.epilog.gui.MainFrame;

public class PerturbationsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5470169327482303234L;

	private MainFrame mainFrame;
	public String selectedPerturbedComponent;
	public byte minValue;
	public byte maxValue;
	public Hashtable<String, NodeInfo> string2Node;
	private JPanel centerCenterPanel;
	private JComboBox componentBox;
	private int selectedIndex;
	private JPanel centerPanelC11;
	private JPanel centerPanelC12;
	private JPanel rightPanel;
	private JTextField setName;
	private JComboBox mutationsSetsCombo;
	private JComboBox mutationsListCombo;
	private DefaultListModel listModel;
	private List<Integer> selectedIndexes = null;
	private JList perturbationsList;

	public JButton buttonFill;

	// public List<AbstractPerturbation> perturbation = null;

	public PerturbationsPanel(MainFrame mainFrame) {

		this.mainFrame = mainFrame;
		init();
	}

	public void init() {

		setLayout(new BorderLayout());

		// PAGE START PANEL

		JPanel optionsPanel = new JPanel();
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);

		optionsPanel.setLayout(layout);

		JButton buttonMarkAll = new JButton("Apply All");
		JButton buttonClearAll = new JButton("Clear All");
		buttonFill = new JButton("Fill");

		buttonFill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireOnFill();
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

		JButton buttonSetAdd = new JButton("+");
		JButton buttonClear = new JButton("-");
		setName = new JTextField("", 15);
		mutationsSetsCombo = new JComboBox();
		mutationsSetsCombo.setPreferredSize(new Dimension(setName
				.getPreferredSize().width, mutationsSetsCombo
				.getPreferredSize().height));

		if (mainFrame.epithelium.getPerturbationsSet() != null) {
			for (String key : mainFrame.epithelium.getPerturbationsSet()
					.keySet()) {
				if (key != "none")
					mutationsSetsCombo.addItem(key);
			}
		}

		buttonSetAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addElementToSet();
			}
		});

		buttonClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeElementFromSet();
			}
		});

		mutationsSetsCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadSelectedPerturbationSet();
			}
		});

		endPanel.add(setName);
		endPanel.add(buttonSetAdd);
		endPanel.add(mutationsSetsCombo);
		endPanel.add(buttonClear);

		add(endPanel, BorderLayout.PAGE_END);

		// Right Panel

		rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

		add(rightPanel, BorderLayout.LINE_END);

		// CenterPanel

		JPanel centerPanel = new JPanel(layout);

		JPanel centerC1 = new JPanel(new BorderLayout());
		JPanel centerC2 = new JPanel(new BorderLayout());
		JPanel centerC3 = new JPanel(new BorderLayout());
		JPanel centerC4 = new JPanel(new BorderLayout());
		JPanel centerC5 = new JPanel(new BorderLayout());

		listModel = new DefaultListModel();

		if (mainFrame.epithelium.loadedPerturbations != null)
			for (int i = 0; i < mainFrame.epithelium.loadedPerturbations.size(); i++)
				listModel.addElement(mainFrame.epithelium.loadedPerturbations
						.get(i));

		perturbationsList = new JList(listModel);

		perturbationsList
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		perturbationsList.setBackground(centerPanel.getBackground());
		perturbationsList.setVisibleRowCount(4);
		this.add(new JScrollPane(perturbationsList));

		final ListSelectionModel lsm = perturbationsList.getSelectionModel();

		centerPanelC11 = new JPanel(layout);
		centerPanelC12 = new JPanel(layout);

		JPanel centerPanelC31 = new JPanel(new BorderLayout());
		JPanel centerPanelC51 = new JPanel(new BorderLayout());

		JButton addPerturbationButton = new JButton("->");
		JButton addMultiplePertubation = new JButton("Multiple");
		JButton deletePerturbation = new JButton("Delete");
		JButton deleteMutation = new JButton("Delete");

		JLabel min = new JLabel("Min:");
		JLabel max = new JLabel("Max:");

		centerPanel.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));

		// perturbation = new ArrayList<AbstractPerturbation>();
		string2Node = new Hashtable<String, NodeInfo>();

		componentBox = new JComboBox();

		for (NodeInfo node : mainFrame.epithelium.getUnitaryModel()
				.getNodeOrder()) {
			if (!node.isInput()) {
				componentBox.addItem(node.getNodeID());
				string2Node.put(node.getNodeID(), node);
			}
		}

		componentBox.setSelectedItem(mainFrame.epithelium.getUnitaryModel()
				.getNodeOrder().get(0).getNodeID());
		selectedPerturbedComponent = mainFrame.epithelium.getUnitaryModel()
				.getNodeOrder().get(0).getNodeID();
		setMinValue((byte) 0);
		setMaxValue(mainFrame.epithelium.getUnitaryModel().getNodeOrder()
				.get(0).getMax());

		JComboBox minValueBox = getPerturbedExpressionCombo();
		JComboBox maxValueBox = getPerturbedExpressionCombo();

		mutationsListCombo = new JComboBox();

		if (mainFrame.epithelium.loadedMutations != null)
			for (int i = 0; i < mainFrame.epithelium.loadedMutations.size(); i++)
				mutationsListCombo.addItem(mainFrame.epithelium.loadedMutations
						.get(i));

		maxValueBox.setSelectedItem(maxValueBox.getItemCount() - 1);

		// Action Listeners

		componentBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JComboBox src = (JComboBox) arg0.getSource();
				setIndex(src.getSelectedIndex());
				setSelectedPerturbedComponent((String) src.getSelectedItem());
				resetMinAndMaxCombo(centerCenterPanel);
				setMinValue((byte) 0);
				setMaxValue(src.getSelectedIndex());
			}
		});

		minValueBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JComboBox src = (JComboBox) arg0.getSource();
				setMinValue((byte) Integer.parseInt(src.getSelectedItem()
						.toString()));
				// System.out.println("minValue" + minValue);
			}
		});

		maxValueBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JComboBox src = (JComboBox) arg0.getSource();
				setMaxValue((byte) Integer.parseInt(src.getSelectedItem()
						.toString()));
				// System.out.println("maxValue" + maxValue);
			}
		});

		addPerturbationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setPerturbation(selectedPerturbedComponent, minValue, maxValue);
			}
		});

		perturbationsList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				selectedIndexes = new ArrayList<Integer>();
				int minIndex = lsm.getMinSelectionIndex();
				int maxIndex = lsm.getMaxSelectionIndex();
				for (int i = minIndex; i <= maxIndex; i++) {
					if (lsm.isSelectedIndex(i)) {
						selectedIndexes.add(i);
					}
				}
			}
		});// Closes the addList

		addMultiplePertubation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveMutation();
			}
		});

		deletePerturbation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// System.out.println(selectedIndexes);
				// perturbationsList.remove(selectedIndexs);
				// for (int index = perturbationsList.getModel().getSize();
				// index < 0; index--) {
				// listModel.removeElementAt(index);
				// perturbationsList.remove(index);
				// }
				deletePerturbation();

				// TODO
			}
		});

		deleteMutation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteMutation();
			}
		});

		mutationsListCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JComboBox src = (JComboBox) arg0.getSource();
				setActivePerturbation(src);
			}
		});

		centerPanelC11.add(min);
		centerPanelC11.add(minValueBox);
		centerPanelC12.add(max);
		centerPanelC12.add(maxValueBox);

		centerPanelC31.add(perturbationsList, BorderLayout.CENTER);
		centerPanelC31.add(deletePerturbation, BorderLayout.PAGE_END);

		centerPanelC51.add(mutationsListCombo, BorderLayout.CENTER);
		centerPanelC51.add(deleteMutation, BorderLayout.PAGE_END);

		centerC1.add(componentBox, BorderLayout.PAGE_START);
		centerC1.add(centerPanelC11, BorderLayout.CENTER);
		centerC1.add(centerPanelC12, BorderLayout.PAGE_END);

		centerC2.add(addPerturbationButton, BorderLayout.CENTER);
		centerC3.add(centerPanelC31, BorderLayout.CENTER);
		centerC4.add(addMultiplePertubation, BorderLayout.CENTER);
		centerC5.add(centerPanelC51, BorderLayout.CENTER);

		// centerEndPanel.add(showPerturbation);

		centerPanel.add(centerC1);
		centerPanel.add(centerC2);
		centerPanel.add(centerC3);
		centerPanel.add(centerC4);
		centerPanel.add(centerC5);
		add(centerPanel, BorderLayout.CENTER);
		rightPanel = initRightPanel();
	}

	// Methods

	private void deletePerturbation() {

		listModel = new DefaultListModel();
		List<AbstractPerturbation> perturbationToDelete = new ArrayList<AbstractPerturbation>();

		for (int index = 0; index < perturbationsList.getModel().getSize(); index++) {
			for (int i : selectedIndexes)
				if (i - index != 0)
					listModel.addElement(perturbationsList.getModel()
							.getElementAt(index));
				else
					perturbationToDelete.add((AbstractPerturbation) perturbationsList.getModel()
							.getElementAt(index));
		}
		perturbationsList.setModel(listModel);

		for (int i = 0; i < mutationsListCombo.getItemCount(); i++) {
			MultiplePerturbation p = (MultiplePerturbation) mutationsListCombo
					.getItemAt(i);
			for (int j = 0; j < perturbationToDelete.size(); j++) {
				if (p.perturbations.contains(perturbationToDelete.get(j))) {
		
					mainFrame.epithelium
							.setActivePerturbation(p);
					deleteMutation();
				}
			}
		}

	}

	private void deleteMutation() {

		List<AbstractPerturbation> newList = new ArrayList<AbstractPerturbation>();
		int index = -1;
		for (int i = 0; i < mutationsListCombo.getItemCount(); i++) {
			if (mutationsListCombo.getItemAt(i) != mainFrame.epithelium
					.getActivePerturbation())
				newList.add((AbstractPerturbation) mutationsListCombo
						.getItemAt(i));

		}
		mutationsListCombo.removeAllItems();
		for (AbstractPerturbation p : newList)
			mutationsListCombo.addItem(p);

		initRightPanel();
	}

	private JPanel initRightPanel() {

		if (mutationsListCombo != null)
			rightPanel.removeAll();
		for (int i = 0; i < mutationsListCombo.getItemCount(); i++) {
			JPanel auxiliaryPanel = new JPanel();
			JLabel test = new JLabel();
			JButton colorBox = new JButton();

			Color color = mainFrame.epithelium
					.getPerturbationColor(mutationsListCombo.getItemAt(i)
							.toString());

			if (color == null) {
				color = mainFrame.getRandomColor();

				while (mainFrame.epithelium.perturbationColor.values()
						.contains(color)) {
					color = mainFrame.getRandomColor();
				}
			}
			mainFrame.epithelium.setPerturbationColor(mutationsListCombo
					.getItemAt(i).toString(), color);

			colorBox.setBackground(mainFrame.epithelium
					.getPerturbationColor(mutationsListCombo.getItemAt(i)
							.toString()));

			test.setText(mutationsListCombo.getItemAt(i).toString());

			auxiliaryPanel.add(test);
			auxiliaryPanel.add(colorBox);
			rightPanel.add(auxiliaryPanel);
		}
		return rightPanel;

	}

	private void setPerturbation(String selectedPerturbedComponent,
			int minValue, int maxValue) {
		NodeInfo node = string2Node.get(selectedPerturbedComponent);
		if (minValue == maxValue) {
			FixedValuePerturbation a = new FixedValuePerturbation(node,
					minValue);
			if (!listModel.contains(a))
				listModel.addElement(a);

		} else {
			RangePerturbation a = new RangePerturbation(node, minValue,
					maxValue);
			if (!listModel.contains(a))
				listModel.addElement(a);
		}
	}

	private void setIndex(int i) {
		selectedIndex = i;
	}

	public void setMinValue(byte min) {
		minValue = min;
	}

	public void setMaxValue(byte max) {
		maxValue = max;
	}

	public void setMaxValue(int index) {
		maxValue = mainFrame.getEpithelium().getUnitaryModel().getNodeOrder()
				.get(index).getMax();
	}

	protected void resetMinAndMaxCombo(JPanel centerPanel) {
		centerPanelC11.remove(1);
		centerPanelC12.remove(1);

		JComboBox perturbedExpressionMin = getPerturbedExpressionCombo();
		JComboBox perturbedExpressionMax = getPerturbedExpressionCombo();

		perturbedExpressionMax.setSelectedItem(perturbedExpressionMax
				.getItemCount() - 1);

		centerPanelC11.add(perturbedExpressionMin, 1);
		centerPanelC12.add(perturbedExpressionMax, 1);

		centerPanelC11.repaint();
		centerPanelC11.revalidate();
		centerPanelC12.repaint();
		centerPanelC12.revalidate();

		perturbedExpressionMin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JComboBox src = (JComboBox) arg0.getSource();
				setMinValue((byte) Integer.parseInt(src.getSelectedItem()
						.toString()));
			}
		});

		perturbedExpressionMax.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JComboBox src = (JComboBox) arg0.getSource();
				setMaxValue((byte) Integer.parseInt(src.getSelectedItem()
						.toString()));
			}
		});
	}

	private JComboBox getPerturbedExpressionCombo() {
		JComboBox perturbedExpressionCombo = new JComboBox();

		for (int i = 0; i <= string2Node.get(selectedPerturbedComponent)
				.getMax(); i++)
			perturbedExpressionCombo.addItem(i);
		return perturbedExpressionCombo;
	}

	public void setSelectedPerturbedComponent(String string) {
		selectedPerturbedComponent = string;
	}

	public void fillHexagons() {
		mainFrame.fillHexagons();
	}

	public void clearAllCells() {
		for (int instance = 0; instance < mainFrame.topology
				.getNumberInstances(); instance++) {
			mainFrame.epithelium.setActivePerturbation(null);
			mainFrame.epithelium.setPerturbedInstance(instance);

		}
		repaint();
		fillHexagons();
	}

	public void markAllCells() {
		for (int instance = 0; instance < this.mainFrame.topology
				.getNumberInstances(); instance++) {
			mainFrame.epithelium.setPerturbedInstance(instance);
		}
		fillHexagons();
	}

	// CenterCenterPanel auxiliary methods

	private void saveMutation() {

		List perturbation = new ArrayList<AbstractPerturbation>();

		for (int i = 0; i < perturbationsList.getSelectedValues().length; i++) {
			perturbation.add(perturbationsList.getSelectedValues()[i]);
		}

		MultiplePerturbation mutation = new MultiplePerturbation(perturbation);

		mutationsListCombo.addItem(mutation);
		rightPanel = initRightPanel();
		this.repaint();

	}

	private void setActivePerturbation(JComboBox combo) {
		mainFrame.epithelium.setActivePerturbation((AbstractPerturbation) combo
				.getSelectedItem());
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

	// ENd Panel Auxiliary Functions

	private void addElementToSet() {
		String name = setName.getText();
		if (!mainFrame.epithelium.getPerturbationsSet().containsKey(name))
			mutationsSetsCombo.addItem(name);
		mainFrame.epithelium.setPerturbationSet(name);
	}

	private void removeElementFromSet() {
		String setToRemove = (String) mutationsSetsCombo.getSelectedItem();
		mainFrame.epithelium.getPerturbationsSet().remove(setToRemove);
		System.out.println("I want to remove: " + setToRemove);
		setName.setText("");
		mutationsSetsCombo.removeAllItems();
		for (String string : mainFrame.epithelium.getPerturbationsSet()
				.keySet())
			if (string != "none")
				mutationsSetsCombo.addItem(string);
	}

	private void loadSelectedPerturbationSet() {

		setName.setText((String) mutationsSetsCombo.getSelectedItem());
		mainFrame.epithelium
				.setSelectedPerturbation((String) mutationsSetsCombo
						.getSelectedItem());
		if (mutationsSetsCombo.getSelectedItem() != null) {
			AbstractPerturbation[] aux = mainFrame.epithelium
					.getPerturbationsSet().get(
							(String) mutationsSetsCombo.getSelectedItem());
			clearAllCells();
			for (int instance = 0; instance < mainFrame.topology
					.getNumberInstances(); instance++) {
				if (mainFrame.epithelium.isCellPerturbed(instance)) {

					mainFrame.epithelium.setActivePerturbation(aux[instance]);
					mainFrame.epithelium.setPerturbedInstance(instance);
				}
			}
			fillHexagons();
		}
	}

	// Saving Auxiliary Functions

	public String[] getPerturbationsStrings() {
		String[] perturbationsString = new String[listModel.getSize()];

		for (int i = 0; i < listModel.getSize(); i++) {
			perturbationsString[i] = listModel.getElementAt(i).toString();
		}
		return perturbationsString;
	}

	public AbstractPerturbation[] getMutationStrings() {
		AbstractPerturbation[] mutation = new AbstractPerturbation[mutationsListCombo
				.getItemCount()];

		for (int i = 0; i < mutationsListCombo.getItemCount(); i++) {
			mutation[i] = (AbstractPerturbation) mutationsListCombo
					.getItemAt(i);
		}
		return mutation;
	}
}
