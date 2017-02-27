package org.cellularasynchrony;

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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.colomoto.logicalmodel.NodeInfo;

public class ComponentsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -498635471090715555L;

	private MainFrame mainFrame;
	private List<NodeInfo> listNodes;

	private JCheckBox nodeBox[];
	private JButton[] colorButton;
	public Hashtable<NodeInfo, Boolean> isEnv;
	private Hashtable<JCheckBox, NodeInfo> jcheckbox2Node;
	private Hashtable<JButton, NodeInfo> button2Node;

	private JPanel properComponents;
	private JPanel inputComponents;
	private JPanel auxiliaryPanel[];

	public JButton saveAsInitialState;
	public JTextField setICName;

	public ComponentsPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;

		init();
	}

	/**
	 * Generates a panel composed by two panels: one with proper components and
	 * another with environment inputs, if there are any. Associated with each
	 * component there is a color, and a selection checkbox.
	 * 
	 * @see pt.igc.nmd.epilog.gui.DrawPolygon
	 * 
	 * @return componentsPanel
	 */
	public JPanel init() {

		jcheckbox2Node = new Hashtable<JCheckBox, NodeInfo>();
		isEnv = new Hashtable<NodeInfo, Boolean>();

		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);

		properComponents = new JPanel();
		inputComponents = new JPanel();

		properComponents.setLayout(layout);
		inputComponents.setLayout(layout);
		
		properComponents.setPreferredSize(new Dimension(400,400));  
		inputComponents.setPreferredSize(new Dimension(400,400));  

		setLayout(new BorderLayout());

		// PAGE START
		JPanel options = new JPanel(layout);

		saveAsInitialState = new JButton("Save State");
		setICName = new JTextField("", 15);
		JButton buttonDeselectAll = new JButton("Deselect all");
		JButton buttonSelectAll = new JButton("Select all");

		saveAsInitialState.setEnabled(false);
		setICName.setEnabled(false);

		saveAsInitialState.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				mainFrame.initial.initialConditionsAdd(setICName.getText());

			}
		});

		buttonDeselectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (JCheckBox singleNodeBox : nodeBox) {
					if (singleNodeBox != null) {
						singleNodeBox.setSelected(false);
						mainFrame.epithelium.setActiveComponent(
								jcheckbox2Node.get(singleNodeBox), false);
					}
				}
				mainFrame.fillHexagons();
				mainFrame.repaint();
			}
		});

		buttonSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (JCheckBox singleNodeBox : nodeBox) {
					if (singleNodeBox != null) {
						singleNodeBox.setSelected(true);
						mainFrame.epithelium.setActiveComponent(
								jcheckbox2Node.get(singleNodeBox), true);
					}
				}
				mainFrame.fillHexagons();
				mainFrame.repaint();
			}
		});

		options.add(buttonSelectAll);
		options.add(buttonDeselectAll);
		options.add(saveAsInitialState);
		options.add(setICName);

		// CENTER
		

		LineBorder border = new LineBorder(Color.black, 1, true);
		TitledBorder titleProperComponents = new TitledBorder(border,
				"Proper Components", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new Font("Arial", Font.ITALIC,
						14), Color.black);

		TitledBorder titleInputs = new TitledBorder(border,
				"Environment Inputs", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new Font("Arial", Font.ITALIC,
						14), Color.black);

		TitledBorder titleOptions = new TitledBorder(border, "Display options",
				TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, new Font(
						"Arial", Font.ITALIC, 14), Color.black);
		
		
		//properComponents.setPreferredSize(this.getPreferredSize());
		
		JScrollPane properComponentsScrollPanel = new JScrollPane(properComponents);
		JScrollPane inputComponentsScrollPanel = new JScrollPane(inputComponents);
		inputComponentsScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		inputComponentsScrollPanel.setSize(getPreferredSize().width, 0);
		
//		inputComponentsScrollPanel.setPreferredSize(this.getPreferredSize());
		
		properComponentsScrollPanel.setBorder(titleProperComponents);
		inputComponentsScrollPanel.setBorder(titleInputs);
		options.setBorder(titleOptions);

		if (this.mainFrame.epithelium.getUnitaryModel() != null) {

			button2Node = new Hashtable<JButton, NodeInfo>();
			listNodes = this.mainFrame.epithelium.getUnitaryModel()
					.getNodeOrder();
			nodeBox = new JCheckBox[listNodes.size()];
			colorButton = new JButton[listNodes.size()];
			auxiliaryPanel = new JPanel[listNodes.size()];

			for (int i = 0; i < listNodes.size(); i++) {

				if (!listNodes.get(i).isInput()) {

					auxiliaryPanel[i] = new JPanel(layout);
					auxiliaryPanel[i].setBorder(BorderFactory
							.createEtchedBorder(EtchedBorder.LOWERED));
					nodeBox[i] = new JCheckBox(listNodes.get(i).getNodeID());
					nodeBox[i].setToolTipText(listNodes.get(i).getNodeID());

					colorButton[i] = new JButton("");

					if (this.mainFrame.epithelium.getColor(listNodes.get(i)) == Color.white) {
						colorButton[i].setBackground(this.mainFrame
								.getRandomColor());
						this.mainFrame.epithelium.setColor(listNodes.get(i),
								colorButton[i].getBackground());

					} else {
						colorButton[i].setBackground(this.mainFrame.epithelium
								.getColor(listNodes.get(i)));
					}
					button2Node.put(colorButton[i], listNodes.get(i));

					jcheckbox2Node.put(nodeBox[i], listNodes.get(i));

					this.mainFrame.epithelium.setActiveComponent(
							listNodes.get(i), false);

					nodeBox[i].addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent event) {
							JCheckBox src = (JCheckBox) event.getSource();
							if (src.isSelected()) {
								fireCheckBoxChange(true, src);

								mainFrame.hexagonsPanel.repaint();
								mainFrame.fillHexagons();
							} else
								fireCheckBoxChange(false, src);

							mainFrame.hexagonsPanel
									.paintComponent(mainFrame.hexagonsPanel
											.getGraphics());
						}
					});

					colorButton[i].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							JButton src = (JButton) arg0.getSource();
							setNewColor(src);

						}
					});

					auxiliaryPanel[i].add(nodeBox[i]);
					auxiliaryPanel[i].add(colorButton[i]);
					properComponents.add(auxiliaryPanel[i]);

				}
				if (listNodes.get(i).isInput()
						& !this.mainFrame.epithelium
								.isIntegrationComponent(listNodes.get(i))) {

					auxiliaryPanel[i] = new JPanel(layout);
					auxiliaryPanel[i].setBorder(BorderFactory
							.createEtchedBorder(EtchedBorder.LOWERED));
					nodeBox[i] = new JCheckBox(listNodes.get(i).getNodeID());
					nodeBox[i].setToolTipText(listNodes.get(i).getNodeID());

					jcheckbox2Node.put(nodeBox[i], listNodes.get(i));
					colorButton[i] = new JButton("");

					if (this.mainFrame.epithelium.getColor(listNodes.get(i)) == Color.white) {
						colorButton[i].setBackground(this.mainFrame
								.getRandomColor());
						this.mainFrame.epithelium.setColor(listNodes.get(i),
								colorButton[i].getBackground());

					} else {
						colorButton[i].setBackground(this.mainFrame.epithelium
								.getColor(listNodes.get(i)));
					}
					button2Node.put(colorButton[i], listNodes.get(i));

					this.mainFrame.getEpithelium().setActiveComponent(
							listNodes.get(i), false);

					nodeBox[i].addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent event) {
							JCheckBox src = (JCheckBox) event.getSource();
							if (src.isSelected()) {
								fireCheckBoxChange(true, src);

								mainFrame.hexagonsPanel.repaint();
								mainFrame.fillHexagons();
							} else
								fireCheckBoxChange(false, src);

							mainFrame.hexagonsPanel
									.paintComponent(mainFrame.hexagonsPanel
											.getGraphics());
						}
					});

					auxiliaryPanel[i].add(nodeBox[i]);
					auxiliaryPanel[i].add(colorButton[i]);
					inputComponents.add(auxiliaryPanel[i]);
				}
			}
			add(options, BorderLayout.PAGE_START);
			
			JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,properComponentsScrollPanel,inputComponentsScrollPanel);
			splitPane.setDividerLocation(200);
			splitPane.setDividerSize(3);
			
			add(splitPane, BorderLayout.CENTER);
			inputComponentsScrollPanel.setVisible(checkEnvironmentInputsExistence());

		}
		return this;
	}

	private boolean checkEnvironmentInputsExistence() {
		boolean check = false;
		for (NodeInfo node : mainFrame.epithelium.getUnitaryModel()
				.getNodeOrder())
			if (node.isInput()
					&& !mainFrame.epithelium.isIntegrationComponent(node))
				check = true;
		return check;
	}

	/**
	 * Changes the color associated with a component.
	 * 
	 * @param src
	 *            button associated with a components color
	 * @see mainFrame.epithelium.setColor
	 */
	private void setNewColor(JButton src) {
		Color newColor = JColorChooser.showDialog(src, "Color Chooser - "
				+ button2Node.get(src).getNodeID(),
				this.mainFrame.epithelium.getColor(button2Node.get(src)));
		src.setBackground(newColor);
		this.mainFrame.epithelium.setColor(button2Node.get(src), newColor);
		this.mainFrame.fillHexagons();

	}

	/**
	 * Sets the component as selected or not.
	 * 
	 * @param box
	 *            checkbox associated with a component
	 * @param bool
	 *            boolean value: true if node selected, false otherwise
	 * 
	 * @see mainFrame.epithelium.setActiveComponent()
	 */
	public void fireCheckBoxChange(Boolean bool, JCheckBox box) {

		this.mainFrame.epithelium.setActiveComponent(jcheckbox2Node.get(box),
				bool);
	}

}
