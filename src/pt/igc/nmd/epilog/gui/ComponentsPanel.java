package pt.igc.nmd.epilog.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.colomoto.logicalmodel.NodeInfo;

import pt.igc.nmd.epilog.MapColorPanel;
import pt.igc.nmd.epilog.SphericalEpithelium;

public class ComponentsPanel extends MapColorPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -498635471090715555L;

	private SphericalEpithelium epithelium;
	public MainFrame mainPanel;
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

	public ComponentsPanel(MainFrame mainPanel, Color color,
			SphericalEpithelium epithelium) {
		super(color);
		this.mainPanel = mainPanel;
		this.epithelium = epithelium;
		init();
	}

	public void init() {

		this.epithelium = mainPanel.getEpithelium();

		jcheckbox2Node = new Hashtable<JCheckBox, NodeInfo>();
		colorChooser2Node = new Hashtable<NodeInfo, ColorButton>();
		isEnv = new Hashtable<NodeInfo, Boolean>();

		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);

		properComponents = new JPanel();
		inputComponents = new JPanel();

		properComponents.setLayout(layout);
		inputComponents.setLayout(layout);

		hexagonsPanel = this.mainPanel.hexagonsPanel;
		setLayout(new BorderLayout());

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

		if (epithelium.getUnitaryModel() != null) {


			listNodes = epithelium.getUnitaryModel().getNodeOrder();
			nodeBox = new JCheckBox[listNodes.size()];
			auxiliaryPanel = new JPanel[listNodes.size()];

			colorChooser = new ColorButton[listNodes.size()];


			for (int i = 0; i < listNodes.size(); i++) {

				if (!listNodes.get(i).isInput()) {

					auxiliaryPanel[i] = new JPanel();
					auxiliaryPanel[i].setLayout(layout);
					auxiliaryPanel[i].setBorder(BorderFactory
							.createEtchedBorder(EtchedBorder.LOWERED));
					nodeBox[i] = new JCheckBox(listNodes.get(i).getNodeID());
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
								mainPanel.simulation.fillHexagons();
							} else
								fireCheckBoxChange(false, src);

							mainPanel.hexagonsPanel.paintComponent(mainPanel.hexagonsPanel.getGraphics());
						}

					});
					colorChooser[i] = new ColorButton(this, listNodes.get(i));
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
					auxiliaryPanel[i].setBorder(BorderFactory
							.createEtchedBorder(EtchedBorder.LOWERED));
					nodeBox[i] = new JCheckBox(listNodes.get(i).getNodeID());
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
								mainPanel.simulation.fillHexagons();
							} else
								fireCheckBoxChange(false, src);

							mainPanel.hexagonsPanel.paintComponent(mainPanel.hexagonsPanel.getGraphics());

						}

					});
					colorChooser[i] = new ColorButton(this, listNodes.get(i));
					colorChooser[i].setBackground(mainPanel.getEpithelium()
							.getColor(listNodes.get(i)));
					colorChooser2Node.put(listNodes.get(i), colorChooser[i]);

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
			add(properComponents, BorderLayout.PAGE_START);
			add(inputComponents, BorderLayout.CENTER);

		}

	}

	public void fireCheckBoxChange(Boolean bool, JCheckBox box) {

		mainPanel.getEpithelium().setActiveComponent(jcheckbox2Node.get(box),
				bool);

	}

}
