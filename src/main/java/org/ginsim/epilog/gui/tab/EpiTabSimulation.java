package org.ginsim.epilog.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.ginsim.epilog.ProjectModelFeatures;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.gui.widgets.ComponentWidget;
import org.ginsim.epilog.gui.widgets.GridPanel;
import org.ginsim.epilog.gui.widgets.JComboCheckBox;
import org.ginsim.epilog.io.ButtonImageLoader;

public class EpiTabSimulation extends EpiTab {
	private static final long serialVersionUID = 1394895739386499680L;

	private ProjectModelFeatures modelFeatures;
	private JPanel left;
	private JSplitPane right;
	private JPanel rLeft;
	private JPanel rRight;
	private JSplitPane jspRRCenter;
	
	public EpiTabSimulation(Epithelium e, TreePath path, ProjectModelFeatures modelFeatures) {
		super(e,path);
		this.modelFeatures = modelFeatures;
	}
	
	public void initialize() {
		setLayout(new BorderLayout());

		this.left = new JPanel(new BorderLayout());
		this.add(this.left, BorderLayout.CENTER);

		GridPanel hexagons = new GridPanel(this.epithelium);
		this.left.add(hexagons, BorderLayout.CENTER);
		
		hexagons.paintComponent(hexagons.getGraphics());
		
		JPanel bLeft = new JPanel(new FlowLayout());
		JButton jbBack = new JButton("back");
		bLeft.add(jbBack);
		JButton jbForward = new JButton("fwr");
		bLeft.add(jbForward);
		JTextField jtSteps = new JTextField("30");
		bLeft.add(jtSteps);
		JButton jbFastFwr = new JButton("fstfwr");
		bLeft.add(jbFastFwr);
		JButton jbPicture = ButtonImageLoader.newButtonNoBorder("screenshot-16.png");
		bLeft.add(jbPicture);
		this.left.add(bLeft, BorderLayout.SOUTH);
		
		this.rLeft = new JPanel(new BorderLayout());
		
		JPanel rlTop = new JPanel();
		rlTop.setLayout(new BoxLayout(rlTop, BoxLayout.Y_AXIS));
		JButton jbReset = new JButton("Reset");
		rlTop.add(jbReset);
		JButton jbClone = new JButton("Clone");
		rlTop.add(jbClone);
		this.rLeft.add(rlTop, BorderLayout.PAGE_START);
		
		JPanel rlBottom = new JPanel();
		rlBottom.add(new JLabel("TODO: Analytics"));
		this.rLeft.add(rlBottom, BorderLayout.CENTER);
		
		this.rRight = new JPanel(new BorderLayout());
		
		JPanel rrTop = new JPanel();
		rrTop.setLayout(new BoxLayout(rrTop, BoxLayout.Y_AXIS));
		
		JPanel rrTopSel = new JPanel();
		rrTopSel.setLayout(new BoxLayout(rrTopSel, BoxLayout.X_AXIS));
		JButton jbSelectAll = new JButton("SelectAll");
		rrTopSel.add(jbSelectAll);
		JButton jbDeselectAll = new JButton("DeselectAll");
		rrTopSel.add(jbDeselectAll);
		rrTop.add(rrTopSel);
		
		// TODO: clean afterwards
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		JCheckBox[] items = new JCheckBox[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			items[i] = new JCheckBox(this.modelFeatures.getName(modelList.get(i)));
			items[i].setSelected(false);
		}
		JComboCheckBox jccb = new JComboCheckBox(items);
		rrTop.add(jccb);
		rrTop.setBorder(BorderFactory.createTitledBorder("Display options"));
		this.rRight.add(rrTop, BorderLayout.NORTH);
		
		this.jspRRCenter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		this.rRight.add(jspRRCenter, BorderLayout.CENTER);
		jccb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboCheckBox jccb = (JComboCheckBox) e.getSource();
				updateComponentList(jccb.getSelectedItems());
			}
		});

		this.right = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				rLeft, rRight);

		this.add(this.right, BorderLayout.LINE_END);
		updateComponentList(jccb.getSelectedItems());
	}
	
	private void updateComponentList(List<String> items) {
		this.jspRRCenter.removeAll();

		List<LogicalModel> lModels = new ArrayList<LogicalModel>();
		for (String modelName : items) {
			lModels.add(this.modelFeatures.getModel(modelName));
		}
		
		JPanel jpRRCTop = new JPanel();
		jpRRCTop.setLayout(new BoxLayout(jpRRCTop, BoxLayout.Y_AXIS));
		jpRRCTop.setBorder(BorderFactory.createTitledBorder("Proper components"));
//System.out.println("updateComponentList-items: " + items);
		Set<String> sProperCompsFromSelectedModels = this.epithelium.getComponentFeatures().getModelsComponents(lModels, false);
		for (String nodeID : sProperCompsFromSelectedModels) {
			Color c = this.epithelium.getComponentFeatures().getNodeColor(nodeID);
			JPanel jpComp = ComponentWidget.getNew(nodeID, c);
//System.out.println("updateComponentList: " + nodeID + " -> " + c.toString());
			jpRRCTop.add(jpComp);
		}
		this.jspRRCenter.add(jpRRCTop);
		
		JPanel jpRRCBottom = new JPanel();
		jpRRCBottom.setLayout(new BoxLayout(jpRRCBottom, BoxLayout.Y_AXIS));
		jpRRCBottom.setBorder(BorderFactory.createTitledBorder("Input components"));
		Set<String> sInputCompsFromSelectedModels = this.epithelium.getComponentFeatures().getModelsComponents(lModels, true);
		List<String> lEnvInputCompsFromSelectedModels = new ArrayList<String>();
		for (String nodeID : sInputCompsFromSelectedModels) {
			if (!this.epithelium.isIntegrationComponent(nodeID)) {
				lEnvInputCompsFromSelectedModels.add(nodeID);
			}
		}
		for (String nodeID : lEnvInputCompsFromSelectedModels) {
			Color c = this.epithelium.getComponentFeatures().getNodeColor(nodeID);
			JPanel jpComp = ComponentWidget.getNew(nodeID, c);
			jpRRCBottom.add(jpComp);
		}
		this.jspRRCenter.add(jpRRCBottom);
	}
}
