package org.ginsim.epilog.gui.tab;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.ginsim.epilog.ProjectModelFeatures;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.gui.widgets.GridPanel;
import org.ginsim.epilog.gui.widgets.JComboCheckBox;

public class EpiTabSimulation extends EpiTab {
	private static final long serialVersionUID = 1394895739386499680L;

	private ProjectModelFeatures modelFeatures;
	private JPanel left;
	private JSplitPane right;
	private JPanel rLeft;
	private JPanel rRight;
	
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
		
		JPanel bLeft = new JPanel();
		bLeft.add(new JLabel("TODO: Play commands + photo"));
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
		
		JPanel rrTop = new JPanel(new FlowLayout());
		JButton jbSelectAll = new JButton("Select All");
		rrTop.add(jbSelectAll);
		JButton jbDeselectAll = new JButton("Deselect All");
		rrTop.add(jbDeselectAll);
		// TODO: clean afterwards
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		JCheckBox[] items = new JCheckBox[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			items[i] = new JCheckBox(this.modelFeatures.getName(modelList.get(i)));
		}
		JComboCheckBox jccb = new JComboCheckBox(items);
		rrTop.add(jccb);
		rrTop.setBorder(BorderFactory.createTitledBorder("Display options"));
		this.rRight.add(rrTop, BorderLayout.NORTH);
		
		JSplitPane jspRRCenter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		
		this.rRight.add(jspRRCenter, BorderLayout.CENTER);
		
		this.right = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				rLeft, rRight);

		this.add(this.right, BorderLayout.LINE_END);
	}
}
