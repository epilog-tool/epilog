package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Hashtable;
import java.util.List;

import javax.swing.JCheckBox;


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
	public Hashtable<NodeInfo, ColorButton> colorChooser2Node;

	public ComponentsPanel(MainPanel mainPanel, Color color) {
		super(color);
		this.mainPanel = mainPanel;

	}

	public void init() {

		jcheckbox2Node = new Hashtable<JCheckBox, NodeInfo>();
		colorChooser2Node = new Hashtable<NodeInfo, ColorButton>();

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
				colorChooser2Node.put(listNodes.get(i), colorChooser[i]);

				add(nodeBox[i]);
				add(colorChooser[i]);
				
//				Color color = colorChooser[i].panel.mainPanel.getEpithelium().getColor(node);
//				colorChooser[i].setBackground(color);
//				colorChooser[i].panel.mapcolor=color;
//				colorChooser[i].panel.revalidate();
//				colorChooser[i].panel.paint(colorButton.panel.getGraphics());
			}

		}

	}


	
	
	public void fireCheckBoxChange(Boolean bool, JCheckBox box) {

		mainPanel.getEpithelium().setActiveComponents(jcheckbox2Node.get(box),
				bool);

	}


}
