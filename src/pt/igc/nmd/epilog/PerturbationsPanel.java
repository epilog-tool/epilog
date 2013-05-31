package pt.igc.nmd.epilog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pt.igc.nmd.epilog.gui.MainFrame;

public class PerturbationsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5470169327482303234L;

	private MainFrame mainFrame;

	public PerturbationsPanel(MainFrame mainFrame) {

		this.mainFrame = mainFrame;
		init();
	}

	public void init() {

		//Color backgroundColor = mainFrame.getBackground();

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
			//	markAllCells();

			}
		});

		buttonClearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//clearAllCells();

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
		JTextField setName = new JTextField("", 15);
		JComboBox sets = new JComboBox();
		sets.setPreferredSize(new Dimension(
				setName.getPreferredSize().width,
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
		
	}

	// protected void clearAllPerturbations() {
	//
	// for (int instance = 0; instance < topology.getNumberInstances();
	// instance++) {
	// // epithelium.setPerturbedInstance(instance, false);
	// }
	// }

	// protected void resetMinAndMaxCombo(JPanel line1) {
	// line1.remove(3);
	// line1.remove(2);
	// JComboBox perturbedExpressionMin = getperturbedExpressionCombo();
	// JComboBox perturbedExpressionMax = getperturbedExpressionCombo();
	//
	// perturbedExpressionMax.setSelectedItem(perturbedExpressionMax
	// .getItemCount() - 1);
	//
	// line1.add(perturbedExpressionMin);
	// line1.add(perturbedExpressionMax);
	// perturbedExpressionMin.setPreferredSize(new Dimension(60, 24));
	// perturbedExpressionMax.setPreferredSize(new Dimension(60, 24));
	// line1.repaint();
	// line1.revalidate();
	//
	// }
	//
	// private JComboBox getperturbedExpressionCombo() {
	// JComboBox perturbedExpressionCombo = new JComboBox();
	// for (int i = 0; i <= selectedPerturbedComponent.getMax(); i++)
	// perturbedExpressionCombo.addItem(i);
	// return perturbedExpressionCombo;
	// }

}
