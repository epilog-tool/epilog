package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Hashtable;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

public class ComponentsPanel extends MapColorPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LogicalModel model = null;
	public MainPanel mainPanel;
	private DrawPolygon hexagonsPanel;

	// Nodes Information

	private List<NodeInfo> listNodes;
	private Color colors[] = { Color.orange, Color.green, Color.blue,
			Color.pink, Color.yellow, Color.magenta, Color.cyan, Color.red,
			Color.LIGHT_GRAY, Color.black };
	private JCheckBox nodeBox[];
	private ColorButton colorChooser[];
	public Hashtable<NodeInfo, Boolean> isEnv;
	private Hashtable<JCheckBox, NodeInfo> jcheckbox2Node;

	public ComponentsPanel(MainPanel mainPanel, Color color) {
		super(color);
		this.mainPanel = mainPanel;

	}

	public void init() {

		jcheckbox2Node = new Hashtable<JCheckBox, NodeInfo>();

		isEnv = new Hashtable<NodeInfo, Boolean>();
		hexagonsPanel = this.mainPanel.hexagonsPanel;
		setLayout(null);
		setBackground(Color.white);
		model = mainPanel.getEpithelium().getUnitaryModel();
		if (model != null) {

			listNodes = model.getNodeOrder();
			nodeBox = new JCheckBox[listNodes.size()];

			colorChooser = new ColorButton[listNodes.size()];
			hexagonsPanel.initializeCellGenes(listNodes.size());

			for (int i = 0; i < listNodes.size(); i++) {

				mainPanel.getEpithelium().setColor(listNodes.get(i), colors[i]);

				isEnv.put(listNodes.get(i), false);

				nodeBox[i] = new JCheckBox(listNodes.get(i).getNodeID());
				nodeBox[i].setBackground(Color.white);
				nodeBox[i].setBounds(0, i * 40, 50, 25);

				jcheckbox2Node.put(nodeBox[i], listNodes.get(i));
				// System.out.println(jcheckbox2Node);

				final JCheckBox checkbox = nodeBox[i];
				// final String nodeID = listNodes.get(i).getNodeID();

				mainPanel.getEpithelium().setActiveComponents(listNodes.get(i),
						false);

				nodeBox[i].addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent event) {
						JCheckBox src = (JCheckBox) event.getSource();
						if (checkbox.isSelected()) {
							fireCheckBoxChange(true, src);
							
							mainPanel.hexagonsPanel.repaint();
							mainPanel.getSimulation().fillHexagons();
						} else
							fireCheckBoxChange(false, src);

						
						hexagonsPanel.paintComponent(hexagonsPanel
								.getGraphics());

					}

				});
				colorChooser[i] = new ColorButton(this, listNodes.get(i));
				colorChooser[i].setBounds(50, i * 40, 20, 25);
				colorChooser[i].setBackground(mainPanel.getEpithelium()
						.getColors().get(listNodes.get(i)));

				add(nodeBox[i]);
				add(colorChooser[i]);

				JComboBox inputComboChooser = null;

				if (listNodes.get(i).isInput()) {
					isEnv.put(listNodes.get(i), true);
					inputComboChooser = new JComboBox();
					inputComboChooser.setBounds(95, i * 40, 120, 25);
					inputComboChooser
							.addItem(InputOption
									.getDescriptionString(InputOption.ENVIRONMENTAL_INPUT));
					inputComboChooser
							.addItem(InputOption
									.getDescriptionString(InputOption.INTEGRATION_INPUT));
					add(inputComboChooser);

					final JPanel jpanel = new JPanel();
					jpanel.setBackground(Color.white);
					jpanel.setBounds(215, i * 40, 300, 60);
					jpanel.setLayout(null);
					// final ColorButton colorChooserBtn = colorChooser[i];
					final int j = i;
					inputComboChooser.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent event) {
							JComboBox source = (JComboBox) event.getSource();
							String optionString = (String) source
									.getSelectedItem();

							InputOption option = InputOption
									.getOptionFromString(optionString);

							if (option != null) {
								switch (option) {
								case ENVIRONMENTAL_INPUT:
									isEnv.put(listNodes.get(j), true);

									break;

								case INTEGRATION_INPUT: {
									isEnv.put(listNodes.get(j), false);
									final JTextField textFormula = new JTextField();
									textFormula.setBounds(10, 0, 250, 25);
									jpanel.removeAll();
									jpanel.add(textFormula);
									jpanel.revalidate();
									jpanel.repaint();
									add(jpanel);
									revalidate();
									repaint();

									textFormula
											.addActionListener(new ActionListener() {
												@Override
												public void actionPerformed(
														ActionEvent e) {
													String logicalFunction = textFormula
															.getText();
													String[] result = logicalFunction
															.split(";");

													// System.out
													// .println(result[0]);
													// System.out
													// .println(result[1]);
													try {

														// ANTLRDemo.Aeval(result);
														ANTLRDemo
																.teste(result[0]);
													} catch (Exception e1) {
														// TODO Auto-generated
														// catch block
														e1.printStackTrace();
													}
												}
											});

								}
									break;
								default: {
									jpanel.removeAll();
									jpanel.revalidate();
									jpanel.repaint();
									add(jpanel);
									revalidate();
									repaint();
								}
									break;
								}
							}
						}

					});
				}
			}

		}

	}

	public void fireCheckBoxChange(Boolean bool, JCheckBox box) {

		mainPanel.getEpithelium().setActiveComponents(jcheckbox2Node.get(box),
				bool);

	}

	private enum InputOption {
		ENVIRONMENTAL_INPUT, INTEGRATION_INPUT;

		public static String getDescriptionString(InputOption option) {
			switch (option) {
			case ENVIRONMENTAL_INPUT:
				return "Env input";
			case INTEGRATION_INPUT:
				return "Int input";
			default:
				return "";
			}
		}

		public static InputOption getOptionFromString(String optionString) {
			if (optionString.equals(InputOption
					.getDescriptionString(ENVIRONMENTAL_INPUT)))
				return ENVIRONMENTAL_INPUT;
			else if (optionString.equals(InputOption
					.getDescriptionString(INTEGRATION_INPUT)))
				return INTEGRATION_INPUT;
			else
				return null;

		}
	}
}
