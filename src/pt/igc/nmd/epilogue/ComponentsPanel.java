package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

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

	private JCheckBox nodeBox[];
	private ColorButton colorChooser[];
	public Hashtable<NodeInfo, Boolean> isEnv;
	private Hashtable<JCheckBox, NodeInfo> jcheckbox2Node;
	public Hashtable<NodeInfo, ColorButton> colorChooser2Node;

	private JPanel properComponents;
	private JPanel inputComponents;
	private JPanel auxiliaryPanel[];

	public ComponentsPanel(MainPanel mainPanel, Color color) {
		super(color);
		this.mainPanel = mainPanel;

	}

	public void init() {

		Color backgroundColor = mainPanel.backgroundColor;
	

		jcheckbox2Node = new Hashtable<JCheckBox, NodeInfo>();
		colorChooser2Node = new Hashtable<NodeInfo, ColorButton>();
		isEnv = new Hashtable<NodeInfo, Boolean>();

		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);

		properComponents = new JPanel();
		inputComponents = new JPanel();

		properComponents.setPreferredSize(new Dimension(630, 110));
		inputComponents.setPreferredSize(new Dimension(630, 110));

		properComponents.setBackground(backgroundColor);
		inputComponents.setBackground(backgroundColor);

		properComponents.setLayout(layout);
		inputComponents.setLayout(layout);

		hexagonsPanel = this.mainPanel.hexagonsPanel;
		setLayout(layout);
		setBackground(backgroundColor);

		model = mainPanel.getEpithelium().getUnitaryModel();

		LineBorder border = new LineBorder(Color.black, 1, true);
		TitledBorder titleProperComponents = new TitledBorder(border,
				"Proper Components", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new Font("Arial", Font.ITALIC,
						14), Color.black);

		TitledBorder titleInputs = new TitledBorder(border, "Inputs",
				TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, new Font(
						"Arial", Font.ITALIC, 14), Color.black);

		properComponents.setBorder(titleProperComponents);
		inputComponents.setBorder(titleInputs);

		if (model != null) {

			listNodes = model.getNodeOrder();
			nodeBox = new JCheckBox[listNodes.size()];
			auxiliaryPanel = new JPanel[listNodes.size()];

			colorChooser = new ColorButton[listNodes.size()];
			hexagonsPanel.initializeCellGenes(listNodes.size());

			for (int i = 0; i < listNodes.size(); i++) {



				if (!listNodes.get(i).isInput()) {

					auxiliaryPanel[i] = new JPanel();
					auxiliaryPanel[i].setLayout(layout);
					auxiliaryPanel[i].setBackground(backgroundColor);
					auxiliaryPanel[i].setPreferredSize(new Dimension(115, 30));

					nodeBox[i] = new JCheckBox(listNodes.get(i).getNodeID());
					nodeBox[i].setBackground(backgroundColor);
					nodeBox[i].setPreferredSize(new Dimension(60, 25));
					nodeBox[i].setToolTipText(listNodes.get(i).getNodeID());

					jcheckbox2Node.put(nodeBox[i], listNodes.get(i));

					mainPanel.getEpithelium().setActiveComponents(
							listNodes.get(i), false);

					nodeBox[i].addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent event) {
							JCheckBox src = (JCheckBox) event.getSource();
							if (src.isSelected()) {
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
					colorChooser[i].setPreferredSize(new Dimension(20, 25));
					colorChooser[i].setBackground(mainPanel.getEpithelium()
							.getColors().get(listNodes.get(i)));
					colorChooser2Node.put(listNodes.get(i), colorChooser[i]);

					auxiliaryPanel[i].add(nodeBox[i]);
					auxiliaryPanel[i].add(colorChooser[i]);
					properComponents.add(auxiliaryPanel[i]);

				}
				if (listNodes.get(i).isInput()) {
					auxiliaryPanel[i] = new JPanel();
					auxiliaryPanel[i].setLayout(layout);
					auxiliaryPanel[i].setBackground(backgroundColor);

					nodeBox[i] = new JCheckBox(listNodes.get(i).getNodeID());
					nodeBox[i].setBackground(backgroundColor);

					nodeBox[i].setPreferredSize(new Dimension(80, 25));
					nodeBox[i].setToolTipText(listNodes.get(i).getNodeID());

					jcheckbox2Node.put(nodeBox[i], listNodes.get(i));

					mainPanel.getEpithelium().setActiveComponents(
							listNodes.get(i), false);

					nodeBox[i].addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent event) {
							JCheckBox src = (JCheckBox) event.getSource();
							if (src.isSelected()) {
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
					colorChooser[i].setPreferredSize(new Dimension(20, 25));
					colorChooser[i].setBackground(mainPanel.getEpithelium()
							.getColors().get(listNodes.get(i)));
					colorChooser2Node.put(listNodes.get(i), colorChooser[i]);

//					System.out.println(listNodes.get(i).getNodeID()
//							+ " "
//							+ mainPanel.getEpithelium()
//									.getIntegrationComponents()
//									.get(listNodes.get(i)));
					
					if (mainPanel.getEpithelium().getIntegrationComponents()
							.get(listNodes.get(i))) {
						nodeBox[i].setVisible(!mainPanel.getEpithelium()
								.getIntegrationComponents()
								.get(listNodes.get(i)));
						colorChooser[i].setVisible(!mainPanel.getEpithelium()
								.getIntegrationComponents()
								.get(listNodes.get(i)));

						
					}
					
					auxiliaryPanel[i].add(nodeBox[i]);
					auxiliaryPanel[i].add(colorChooser[i]);
					inputComponents.add(auxiliaryPanel[i]);
				}
			}
			add(properComponents);
			add(inputComponents);

		}

	}

	public void fireCheckBoxChange(Boolean bool, JCheckBox box) {

		mainPanel.getEpithelium().setActiveComponents(jcheckbox2Node.get(box),
				bool);

	}

}
