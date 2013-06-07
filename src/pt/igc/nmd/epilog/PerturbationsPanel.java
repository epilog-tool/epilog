package pt.igc.nmd.epilog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
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
	private JLabel showPerturbation;
	private JPanel centerCenterPanel;
	private String perturbationString = "";
	private JComboBox componentBox;
	private int selectedIndex;
	private JPanel centerPanelC11;
	private JPanel centerPanelC12;
	private JTextField namePerturbation;
	private JTextField setName;
	private JComboBox sets;
	private JComboBox mutationsList;
	private DefaultListModel listModel;
	private List<Integer> selectedIndexes = null;
	private JList perturbationsList;

	List<AbstractPerturbation> perturbation = null;

	public PerturbationsPanel(MainFrame mainFrame) {

		this.mainFrame = mainFrame;
		init();
	}

	public void init() {

		// Color backgroundColor = mainFrame.getBackground();

		setLayout(new BorderLayout());

		// PAGE START PANEL

		JPanel optionsPanel = new JPanel();
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);

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
		sets.setPreferredSize(new Dimension(setName.getPreferredSize().width,
				sets.getPreferredSize().height));

		buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: Add the selected grid to the sets of initial
				// conditions with the name of the textfield
				addElementToSet();
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

		// CenterPanel

		JPanel centerPanel = new JPanel(layout);

		JPanel centerC1 = new JPanel(new BorderLayout());
		JPanel centerC2 = new JPanel(new BorderLayout());
		JPanel centerC3 = new JPanel(new BorderLayout());
		JPanel centerC4 = new JPanel(new BorderLayout());
		JPanel centerC5 = new JPanel(new BorderLayout());

		listModel = new DefaultListModel();
		perturbationsList = new JList(listModel);

		perturbationsList
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		perturbationsList.setBackground(centerPanel.getBackground());
		perturbationsList.setVisibleRowCount(4);
		this.add(new JScrollPane(perturbationsList));

		final ListSelectionModel lsm = perturbationsList.getSelectionModel();

		perturbationsList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {

				// selectedIndexes = new ArrayList<Integer>();
				// // Find out which indexes are selected.
				// int minIndex = lsm.getMinSelectionIndex();
				// int maxIndex = lsm.getMaxSelectionIndex();
				// for (int i = minIndex; i <= maxIndex; i++) {
				// if (lsm.isSelectedIndex(i)) {
				// selectedIndexes.add(i);
				// setPriorityClassActivated(e.getSource());
				// }
				// }
			}
		});

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

		perturbation = new ArrayList<AbstractPerturbation>();
		string2Node = new Hashtable<String, NodeInfo>();

		componentBox = new JComboBox();

		for (NodeInfo node : mainFrame.epithelium.getUnitaryModel()
				.getNodeOrder()) {
			componentBox.addItem(node.getNodeID());
			string2Node.put(node.getNodeID(), node);
		}

		componentBox.setSelectedItem(mainFrame.epithelium.getUnitaryModel()
				.getNodeOrder().get(0).getNodeID());
		selectedPerturbedComponent = mainFrame.epithelium.getUnitaryModel()
				.getNodeOrder().get(0).getNodeID();

		JComboBox minValueBox = getPerturbedExpressionCombo();
		JComboBox maxValueBox = getPerturbedExpressionCombo();

		mutationsList = new JComboBox();

		// showPerturbation = new JLabel();

		maxValueBox.setSelectedItem(maxValueBox.getItemCount() - 1);

		// Action Listeners

		componentBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JComboBox src = (JComboBox) arg0.getSource();
				setIndex(src.getSelectedIndex());
				setSelectedPerturbedComponent((String) src.getSelectedItem());
				resetMinAndMaxCombo(centerCenterPanel);
			}
		});

		minValueBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JComboBox src = (JComboBox) arg0.getSource();
				setMinValue((byte) Integer.parseInt(src.getSelectedItem()
						.toString()));
				System.out.println("minValue" + minValue);
			}
		});

		maxValueBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JComboBox src = (JComboBox) arg0.getSource();
				setMaxValue((byte) Integer.parseInt(src.getSelectedItem()
						.toString()));
				System.out.println("maxValue" + maxValue);
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
			}
		});

		mutationsList.addActionListener(new ActionListener() {
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

		centerPanelC51.add(mutationsList, BorderLayout.CENTER);
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
	}

	// Methods

	private void setPerturbation(String selectedPerturbedComponent,
			int minValue, int maxValue) {
		NodeInfo node = string2Node.get(selectedPerturbedComponent);
		if (minValue == maxValue) {
			FixedValuePerturbation a = new FixedValuePerturbation(node,
					minValue);
			listModel.addElement(a);

		} else {
			RangePerturbation a = new RangePerturbation(node, minValue,
					maxValue);
			listModel.addElement(a);
		}
	}

	// private void removeComponentFromBox() {
	// componentBox.removeItemAt(selectedIndex);
	// }

	private void setIndex(int i) {
		selectedIndex = i;
	}

	// public void setShowPerturbation(String string) {
	// perturbationString = string;
	// showPerturbation.setText(perturbationString);
	//
	// centerCenterPanel.repaint();
	// centerCenterPanel.revalidate();
	// }

	public void setMinValue(byte min) {
		minValue = min;
		System.out.println(min);
	}

	public void setMaxValue(byte max) {
		maxValue = max;
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

	// CenterCenterPanel auxiliary methods

	private void saveMutation() {

		List perturbation = new ArrayList<AbstractPerturbation>();

		for (int i = 0; i < perturbationsList.getSelectedValues().length; i++) {
			perturbation.add(perturbationsList.getSelectedValues()[i]);
		}

		MultiplePerturbation mutation = new MultiplePerturbation(perturbation);

		mutationsList.addItem(mutation);
	}

	private void setActivePerturbation(JComboBox combo) {
		mainFrame.epithelium.setActivePerturbation((AbstractPerturbation) combo.getSelectedItem());
	}

	// ENd Panel Auxiliary Functions


	private void addElementToSet() {
		String name = setName.getText();
		sets.addItem(name);
		// mainFrame.epithelium.setPerturbationsSet(name,finalPerturbation());
	}

	private void loadInitialconditions() {
		setName.setText((String) sets.getSelectedItem());
	}
}
