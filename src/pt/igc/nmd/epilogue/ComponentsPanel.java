package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.colomoto.logicalmodel.NodeInfo;

public class ComponentsPanel extends MapColorPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -498635471090715555L;
	
	private SphericalEpithelium epithelium;
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

	public ComponentsPanel(MainPanel mainPanel, Color color, SphericalEpithelium epithelium) {
		super(color);
		this.mainPanel = mainPanel;
		this.epithelium = epithelium;
	}

	public void init() {
	
		//epithelium = new SphericalEpithelium(mainPanel.getTopology());
		System.out.println("epithelium at compoenents: " + epithelium);
		System.out.println("mainpanel" + mainPanel.getEpithelium());
		this.epithelium = epithelium.getEpithelium();
		System.out.println("epithelium at compoentens Panel" + this.epithelium);
		
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

		System.out.println("@compoentnes Panel: " + epithelium.getUnitaryModel());
		if (epithelium.getUnitaryModel() != null) {
			System.out.println("@componentsPanel after model!=null"+ epithelium.getUnitaryModel());
			listNodes = epithelium.getUnitaryModel().getNodeOrder();
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

					mainPanel.getEpithelium().setActiveComponent(
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
							.getColor(listNodes.get(i)));
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

					mainPanel.getEpithelium().setActiveComponent(
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
							.getColor(listNodes.get(i)));
					colorChooser2Node.put(listNodes.get(i), colorChooser[i]);

					// System.out.println(listNodes.get(i).getNodeID()
					// + " "
					// + mainPanel.getEpithelium()
					// .getIntegrationComponents()
					// .get(listNodes.get(i)));

					if (mainPanel.getEpithelium().isIntegrationComponent(
							listNodes.get(i))) {
						nodeBox[i].setVisible(false);
						colorChooser[i].setVisible(false);

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

		mainPanel.getEpithelium().setActiveComponent(jcheckbox2Node.get(box),
				bool);

	}

}
