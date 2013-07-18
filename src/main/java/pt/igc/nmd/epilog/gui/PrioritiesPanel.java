package pt.igc.nmd.epilog.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.colomoto.logicalmodel.NodeInfo;

public class PrioritiesPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5470169327482303234L;

	private MainFrame mainFrame = null;
	private JPanel centerPanel = null;
	private List<JList> priorityClass = null;
	private List<Integer> selectedIndexes = null;
	private JTextField priorityChosenString;
	private int lastClass = 0;
	public Hashtable<String, NodeInfo> string2Node;

	private List<DefaultListModel> listOfListModel;

	private JTextField setName;
	private JComboBox sets;

	/**
	 * Generates the priorities panel, to be inserted in the tab on Epilog's
	 * main panel.
	 * 
	 * @param mainFrame
	 */

	public PrioritiesPanel(MainFrame mainFrame) {

		this.mainFrame = mainFrame;
		selectedIndexes = new ArrayList<Integer>();
		init();
	}

	/**
	 * Initializes the priorities panel.
	 */
	public void init() {

		string2Node = new Hashtable<String, NodeInfo>();
		setLayout(new BorderLayout());

		// PAGE START PANEL

		JPanel optionsPanel = new JPanel();
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);

		optionsPanel.setLayout(layout);

		JButton buttonIncreaseLeft = new JButton("->");
		JButton buttonIncreaseRight = new JButton("<-");
		JButton buttonDifferentiate = new JButton("Differentiate");
		

		buttonIncreaseLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				decreasePriority(centerPanel);
			}
		});

		buttonIncreaseRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				increasePriority(centerPanel);

			}
		});

		optionsPanel.add(buttonDifferentiate);
		optionsPanel.add(buttonIncreaseLeft);
		optionsPanel.add(buttonIncreaseRight);
		

		add(optionsPanel, BorderLayout.PAGE_START);
		
		
		//SELECTED PRIORITY DISPLAY
		
		JPanel selectedPriority = new JPanel(new FlowLayout());
		priorityChosenString = new JTextField("");
		selectedPriority.add(priorityChosenString);
		
		LineBorder border = new LineBorder(Color.black, 1, true);
		TitledBorder south = new TitledBorder(border, "Selected Priority Display", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
				new Font("Arial", Font.ITALIC, 14), Color.black);
		selectedPriority.setBorder(south);

		// END PANEL

		JPanel endPanel = new JPanel(layout);

		JButton buttonAdd = new JButton("+");
		JButton buttonClear = new JButton("-");
		setName = new JTextField("", 15);
		sets = new JComboBox();
		sets.setPreferredSize(new Dimension(setName.getPreferredSize().width,
				sets.getPreferredSize().height));

		if (mainFrame.epithelium.getPrioritiesSet() != null) {
			for (String key : mainFrame.epithelium.getPrioritiesSet().keySet()) {
				sets.addItem(key);
			}
		}

		buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				addElementToSet();
			}
		});

		buttonClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeElementFromSet();
			}
		});

		// JComboBox that selects the one
		sets.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadInitialconditions();
			}
		});

		endPanel.add(setName);
		endPanel.add(buttonAdd);
		endPanel.add(sets);
		endPanel.add(buttonClear);

		JPanel endTotal = new JPanel(new BorderLayout());
		endTotal.add(endPanel, BorderLayout.PAGE_END);
		endTotal.add(selectedPriority, BorderLayout.CENTER);
		add(endTotal, BorderLayout.PAGE_END);

		// CENTER PANEL

		centerPanel = new JPanel(layout);
		priorityClass = new ArrayList<JList>();

		priorityClass.add(createInitialPriority());
		centerPanel.add(priorityClass.get(0));
		add(centerPanel, BorderLayout.CENTER);

	}

	/**
	 * Sets the selected priority class as active.
	 * 
	 * @param object
	 */
	private void setPriorityClassActivated(Object object) {
		lastClass = priorityClass.indexOf(object);
	}

	/**
	 * Initializes the list of priorities.
	 * 
	 * @return a list of priorities, where there are no priorities
	 */
	private JList createInitialPriority() {

		JList initialPriorityClass = new JList(initialItens());

		initialPriorityClass
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		initialPriorityClass.setBackground(centerPanel.getBackground());
		initialPriorityClass.setVisibleRowCount(4);
		this.add(new JScrollPane(initialPriorityClass));

		final ListSelectionModel lsm = initialPriorityClass.getSelectionModel();

		initialPriorityClass
				.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {

						selectedIndexes = new ArrayList<Integer>();
						// Find out which indexes are selected.
						int minIndex = lsm.getMinSelectionIndex();
						int maxIndex = lsm.getMaxSelectionIndex();
						for (int i = minIndex; i <= maxIndex; i++) {
							if (lsm.isSelectedIndex(i)) {
								selectedIndexes.add(i);
								setPriorityClassActivated(e.getSource());
							}
						}
					}
				});

		return initialPriorityClass;
	}

	/**
	 * Generates the list model to use with the JList.
	 * 
	 * @return list model
	 */
	private ListModel initialItens() {

		listOfListModel = new ArrayList<DefaultListModel>();

		DefaultListModel listModel = new DefaultListModel();

		List<NodeInfo> nodes = mainFrame.epithelium.getUnitaryModel()
				.getNodeOrder();

		for (NodeInfo node : nodes)
			if (!node.isInput()) {
				listModel.addElement(node.getNodeID()+"+");
				listModel.addElement(node.getNodeID()+"-");
				string2Node.put(node.getNodeID(), node);
				string2Node.put(node.getNodeID()+"+", node);
				string2Node.put(node.getNodeID()+"-", node);
			}
		listOfListModel.add(listModel);
		return listModel;

	}

	/**
	 * Increases the priority of a component.
	 * 
	 * @param centerPanel
	 */
	private void increasePriority(JPanel centerPanel) {

		if (selectedIndexes.size() > 0 & lastClass > 0) {
			for (int j : selectedIndexes) {
				listOfListModel.get(lastClass - 1).addElement(
						listOfListModel.get(lastClass).get(j).toString());
				removeItem(listOfListModel.get(lastClass),
						listOfListModel.get(lastClass).get(j).toString());
			}
		}
		if (listOfListModel.get(lastClass).getSize() == 0) {
			this.listOfListModel = removeList(listOfListModel);
			priorityClass.remove(lastClass);
			centerPanel.remove(centerPanel.getComponentCount() - 1);
			centerPanel.remove(centerPanel.getComponentCount() - 1);
			centerPanel.revalidate();
			centerPanel.repaint();
		}
	}

	/**
	 * Remove a list from the priorities set.
	 * 
	 * @param lists
	 *            priorities list
	 * @return new priorities list without the removed item
	 */
	private List<DefaultListModel> removeList(List<DefaultListModel> lists) {
		List<DefaultListModel> listofListToReturn = new ArrayList<DefaultListModel>();
		for (int i = 0; i < lists.size(); i++) {
			if (lists.get(i).getSize() == 0) {
				// System.out.println("This List is empty");
			} else {
				listofListToReturn.add(lists.get(i));
			}
		}
		return listofListToReturn;

	}

	/**
	 * Remove the priority of a component.
	 * 
	 * @param centerPanel
	 */
	private void decreasePriority(JPanel centerPanel) {

		// Creates the mathematical relation
		JLabel faster = new JLabel();
		faster.setText("<");

		// checks if there are any selected itens in order to create a new class

		if (selectedIndexes.size() > 0) {
			// TO CREATE a new class
			if (priorityClass.size() - 1 == lastClass) {
				// System.out.println("I have to create a new class because"
				// + priorityClass.size() + " = " + lastClass + 1);
				// Itens to be added
				DefaultListModel listModel = new DefaultListModel();
				for (int j : selectedIndexes) {

					// System.out.println(listOfListModel.get(lastClass).get(j)
					// .toString());
					listModel.addElement(listOfListModel.get(lastClass).get(j)
							.toString());

					removeItem(listOfListModel.get(lastClass), listOfListModel
							.get(lastClass).get(j).toString());
				}
				listOfListModel.add(listModel);

				JList priority = new JList(listModel);
				priority.setBackground(centerPanel.getBackground());
				priority.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				final ListSelectionModel lsm = priority.getSelectionModel();
				priorityClass.add(priority);

				priority.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {

						selectedIndexes = new ArrayList<Integer>();
						// Find out which indexes are selected.
						int minIndex = lsm.getMinSelectionIndex();
						int maxIndex = lsm.getMaxSelectionIndex();
						for (int i = minIndex; i <= maxIndex; i++) {
							if (lsm.isSelectedIndex(i)) {
								selectedIndexes.add(i);
								setPriorityClassActivated(e.getSource());
							}
						}
					}
				});// Closes the addList

				centerPanel.add(faster);
				centerPanel.add(priority);
				centerPanel.repaint();
				centerPanel.revalidate();
			}// Closes the if that creates a new list

			else {
				for (int j : selectedIndexes) {
					String itemToG0 = listOfListModel.get(lastClass).get(j)
							.toString();
					listOfListModel.get(lastClass + 1).addElement(itemToG0);
					removeItem(listOfListModel.get(lastClass), listOfListModel
							.get(lastClass).get(j).toString());
				}
			}
		}// Closes the if that checks if there is anything to do

	}

	/**
	 * Remove a component from a list of priorities.
	 * 
	 * @param listModel
	 *            list model associated with the list of priorities
	 * @param item
	 *            component to be removed
	 */

	private void removeItem(DefaultListModel listModel, String item) {

		for (int i = 0; i < listModel.getSize(); i++) {
			if (item == listModel.getElementAt(i).toString()) {
				listModel.removeElementAt(i);
			}
		}
	}

	/**
	 * Priority list to be saved
	 * @return priority list
	 */
	public List<List<String>> finalPriorities() {

		List<List<String>> priorities = new ArrayList<List<String>>();

		for (int j = 0; j < priorityClass.size(); j++) {
			List<String> nodeList = new ArrayList<String>();

			for (int i = 0; i < priorityClass.get(j).getModel().getSize(); i++) {
				String element = priorityClass.get(j).getModel()
						.getElementAt(i).toString();
				//string2Node.get(element);
				nodeList.add(element);
			}
			priorities.add(nodeList);
		}
		return priorities;
	}

	// ENd Panel Auxiliary Functions
	
	/**
	 * Adds a priority set. If a name is already used, then the new set
	 * replaces the old one.
	 * 
	 */
	private void addElementToSet() {
		String name = setName.getText();
		if (!mainFrame.epithelium.getPrioritiesSet().containsKey(name))
			sets.addItem(name);
		mainFrame.epithelium.setPrioritiesSet(name, finalPriorities());
	}

	/**
	 * Removes an element from the set of priorities sets.
	 * 
	 */
	private void removeElementFromSet() {
		String setToRemove = (String) sets.getSelectedItem();
		mainFrame.epithelium.getPrioritiesSet().remove(setToRemove);
		System.out.println("I want to remove: " + setToRemove);
		setName.setText("");
		priorityChosenString.setText("");
		sets.removeAllItems();
		for (String string : mainFrame.epithelium.getPrioritiesSet().keySet())
			sets.addItem(string);

	}
	
	/**
	 * Load selected priority set.
	 */
	private void loadInitialconditions() {
		setName.setText((String) sets.getSelectedItem());
		
		List<List<String>> priorities = new ArrayList<List<String>>();

		if (sets.getSelectedItem() != null) {
			if (mainFrame.epithelium.getPrioritiesSet().get(
					(String) sets.getSelectedItem()) != null) {
				priorities = mainFrame.epithelium.getPrioritiesSet().get(
						(String) sets.getSelectedItem());
				int numberOfLists = priorities.size();

				String string = "";
				for (int index = 0; index < priorities.size(); index++) {
					// DefaultListModel listModel = new DefaultListModel();
					string = string + "[";
					for (String node : priorities.get(index)) {
						// listModel.addElement(node.getNodeID());
						string = string + " " + node;
					}
					string = string + "]";
					if (index < priorities.size() - 1) {
						string = string + " " + "<";
					}
				}
				priorityChosenString.setText(string);
			}
		}
	}

}
