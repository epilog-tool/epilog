package pt.igc.nmd.epilog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.colomoto.logicalmodel.NodeInfo;

import pt.igc.nmd.epilog.gui.MainFrame;

public class PrioritiesPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5470169327482303234L;

	private MainFrame mainFrame = null;
	private JPanel centerPanel = null;
	private List<JList> priorityClass = null;
	private List<Integer> selectedIndexes = null;
	private int lastClass = 0;

	private List<DefaultListModel> listOfListModel;

	public PrioritiesPanel(MainFrame mainFrame) {

		this.mainFrame = mainFrame;
		selectedIndexes = new ArrayList<Integer>();
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

		JButton buttonIncreaseLeft = new JButton("->");
		JButton buttonIncreaseRight = new JButton("<-");
		JButton buttonRemove = new JButton("Remove");

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

		buttonRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});

		optionsPanel.add(buttonIncreaseLeft);
		optionsPanel.add(buttonIncreaseRight);
		optionsPanel.add(buttonRemove);

		add(optionsPanel, BorderLayout.PAGE_START);

		// END PANEL

		JPanel endPanel = new JPanel(layout);

		JButton buttonAdd = new JButton("+");
		JButton buttonClear = new JButton("-");
		JTextField setName = new JTextField("", 15);
		JComboBox sets = new JComboBox();
		sets.setPreferredSize(new Dimension(setName.getPreferredSize().width,
				sets.getPreferredSize().height));

		buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: Add the selected grid to the sets of initial
				// conditions with the name of the textfield
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
			}
		});

		endPanel.add(setName);
		endPanel.add(buttonAdd);
		endPanel.add(sets);
		endPanel.add(buttonClear);

		add(endPanel, BorderLayout.PAGE_END);

		// CENTER PANEL

		centerPanel = new JPanel(layout);
		priorityClass = new ArrayList<JList>();

		priorityClass.add(createInitialPriority());
		centerPanel.add(priorityClass.get(0));
		add(centerPanel, BorderLayout.CENTER);

	}

	private void setPriorityClassActivated(Object object) {
		lastClass = priorityClass.indexOf(object);
	}

	private JList createInitialPriority() {

		JList initialPriorityClass = new JList(initialItens());

		initialPriorityClass
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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

	private ListModel initialItens() {

		listOfListModel = new ArrayList<DefaultListModel>();

		DefaultListModel listModel = new DefaultListModel();

		List<NodeInfo> nodes = mainFrame.epithelium.getUnitaryModel()
				.getNodeOrder();

		for (NodeInfo node : nodes)
			if (!node.isInput())
				listModel.addElement(node.getNodeID());
		listOfListModel.add(listModel);
		return listModel;

	}

	private void increasePriority(JPanel centerPanel) {

		if (selectedIndexes.size() > 0 & lastClass>0) {
			for (int j : selectedIndexes) {
				listOfListModel.get(lastClass-1).addElement(listOfListModel.get(lastClass).get(j).toString());
				removeItem(listOfListModel.get(lastClass),listOfListModel.get(lastClass).get(j).toString());
			}
		}
		if (listOfListModel.get(lastClass).getSize()==0){
			listOfListModel.get(lastClass).clear();
			//centerPanel.remove(centerPanel.getComponentCount()-1);
			
			centerPanel.repaint();
		}
	}

	private void decreasePriority(JPanel centerPanel) {

		// Creates the mathematical relation
		JLabel faster = new JLabel();
		faster.setText("<");

		// checks if there are any selected itens in order to create a new class

		if (selectedIndexes.size() > 0) {
			// TO CREATE a new class
			if (priorityClass.size() - 1 == lastClass) {

				// Itens to be added
				DefaultListModel listModel = new DefaultListModel();
				for (int j : selectedIndexes) {
					
					System.out.println(listOfListModel.get(lastClass).get(j).toString());
					listModel.addElement(listOfListModel.get(lastClass).get(j).toString());
					
					removeItem(listOfListModel.get(lastClass),listOfListModel.get(lastClass).get(j).toString());
				}
				listOfListModel.add(listModel);
				
				JList priority = new JList(listModel);
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
			
			else{
				for (int j : selectedIndexes) {
					String itemToG0 = listOfListModel.get(lastClass).get(j).toString();
					listOfListModel.get(lastClass+1).addElement(itemToG0);
					removeItem(listOfListModel.get(lastClass),listOfListModel.get(lastClass).get(j).toString());
			}
				
			}
		}// Closes the if that checks if there is anything to do

	}

	private void removeItem(DefaultListModel listModel ,String item){
		
		for (int i=0; i<listModel.getSize();i++){
			if (item==listModel.getElementAt(i).toString()){
				listModel.removeElementAt(i);
			}
				
		}
	}
	
}
