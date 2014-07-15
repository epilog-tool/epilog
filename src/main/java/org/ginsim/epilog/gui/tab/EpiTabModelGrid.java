package org.ginsim.epilog.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.ginsim.epilog.ProjectModelFeatures;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.gui.widgets.GridPanel;
import org.ginsim.epilog.gui.widgets.SBMLPanel;

public class EpiTabModelGrid extends EpiTabDefinitions {
	private static final long serialVersionUID = -5262665948855829161L;
	
	private LogicalModel[][] modelGrid;
	private Map<String, LogicalModel> modelMap;

	public EpiTabModelGrid(Epithelium e, TreePath path, ProjectModelFeatures modelFeatures) {
		super(e, path, modelFeatures);
	}

	public void initialize() {
		this.center.setLayout(new BorderLayout());
		ButtonGroup group = new ButtonGroup();
		
		GridPanel hexagons = new GridPanel(this.epithelium);
		this.center.add(hexagons, BorderLayout.CENTER);
		
		JPanel rTop = new JPanel(new FlowLayout());
		JButton jbRectFill = new JButton("Rectangle Fill");
		rTop.add(jbRectFill);
		JButton jbApplyAll = new JButton("Apply All");
		rTop.add(jbApplyAll);
		JButton jbClearAll = new JButton("Clear All");
		rTop.add(jbClearAll);
		
		JPanel rCenter = new JPanel();
		rCenter.setLayout( new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		int i = 0;
		for (String name : this.modelFeatures.getNames()) {
			LogicalModel m = this.modelFeatures.getModel(name);
			Color c = this.modelFeatures.getColor(m);
			SBMLPanel p = SBMLPanel.getNew(name, c);
			gbc.gridx = 0;
			gbc.gridy = i++;
			rCenter.add(p, gbc);
		}

		JPanel right = new JPanel(new BorderLayout());
		right.add(rTop, BorderLayout.NORTH);
		right.add(rCenter, BorderLayout.CENTER);
		this.center.add(right, BorderLayout.LINE_END);
	}

	@Override
	protected void buttonCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void buttonAccept() {
		// TODO Auto-generated method stub

	}

	// TODO: Accept button -> calls epigrid.updateModelSet()
}
