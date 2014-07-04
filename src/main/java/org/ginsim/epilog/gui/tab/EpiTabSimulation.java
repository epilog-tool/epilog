package org.ginsim.epilog.gui.tab;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.tree.TreePath;

import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.gui.widgets.GridPanel;

public class EpiTabSimulation extends EpiTab {
	private static final long serialVersionUID = 1394895739386499680L;

	private JPanel left;
	private JSplitPane right;
	private JPanel rLeft;
	private JPanel rRight;
	
	public EpiTabSimulation(Epithelium e, TreePath path) {
		super(e,path);
		this.setVisible(true);
	}
	
	public void initialize() {
		setLayout(new FlowLayout());

		this.left = new JPanel(new BorderLayout());
		this.add(this.left);

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
		this.rRight.add(rrTop, BorderLayout.NORTH);
		
		this.right = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				rLeft, rRight);
		
		add(this.right);
	}
}
