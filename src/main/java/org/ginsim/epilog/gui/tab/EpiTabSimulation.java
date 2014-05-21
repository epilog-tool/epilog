package org.ginsim.epilog.gui.tab;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.tree.TreePath;

import org.ginsim.epilog.core.Epithelium;

public class EpiTabSimulation extends EpiTab {
	private static final long serialVersionUID = 1394895739386499680L;

	private JPanel left;
	private JSplitPane right;
	private JPanel rLeft;
	private JPanel rRight;
	
	public EpiTabSimulation(Epithelium e, TreePath path) {
		super(e,path);
		this.initializeGUI();
	}
	
	private void initializeGUI() {
		this.left = new JPanel();
		
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
		
		setLayout(new FlowLayout());
		add(this.left);
		add(this.right);
	}
}
