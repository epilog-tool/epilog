package org.ginsim.epilog.gui.tab;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.ginsim.epilog.project.ProjectModelFeatures;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumPriorityClasses;
import org.ginsim.epilog.core.ModelPriorityClasses;
import org.ginsim.epilog.io.ButtonFactory;

public class EpiTabPriorityClasses extends EpiTabDefinitions {
	private static final long serialVersionUID = 1176575422084167530L;

	private EpitheliumPriorityClasses userPriorityClasses;
	private LogicalModel selectedModel;
	private List<JList<String>> guiClasses;

	private JPanel jpBottom;

	public EpiTabPriorityClasses(Epithelium e, TreePath path,
			ProjectModelFeatures modelFeatures) {
		super(e, path, modelFeatures);
	}

	public void initialize() {
		this.center.setLayout(new BorderLayout());
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.epithelium.getEpitheliumGrid().getModelSet());
		this.userPriorityClasses = new EpitheliumPriorityClasses();
		for (LogicalModel m : modelList) {
			this.userPriorityClasses.addModelPriorityClasses(this.epithelium
					.getPriorityClasses(m).clone());
		}
		this.guiClasses = new ArrayList<JList<String>>();

		JPanel jpTop = new JPanel(new BorderLayout());
		jpTop.setBorder(BorderFactory.createTitledBorder("Navigation"));
		this.center.add(jpTop, BorderLayout.NORTH);
		JPanel jpTLeft = new JPanel();
		jpTop.add(jpTLeft, BorderLayout.LINE_START);
		JPanel jpTRight = new JPanel(new FlowLayout());
		jpTop.add(jpTRight, BorderLayout.CENTER);

		// Model selection list
		String[] saSBML = new String[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			saSBML[i] = this.modelFeatures.getName(modelList.get(i));
		}
		JComboBox<String> jcbSBML = new JComboBox<String>(saSBML);
		jcbSBML.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> jcb = (JComboBox<String>) e.getSource();
				LogicalModel m = modelFeatures.getModel((String) jcb
						.getSelectedItem());
				updatePriorityList(m);
				// Re-Paint
				getParent().repaint();
			}
		});
		jpTLeft.add(jcbSBML);

		// Button display options
		JButton jbSplit = ButtonFactory.getNoMargins("Split");
		jbSplit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				splitSelVars();
				// Repaint
				getParent().repaint();
			}
		});
		jpTRight.add(jbSplit);
		JButton jbUnsplit = ButtonFactory.getNoMargins("Unsplit");
		jbUnsplit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				unsplitSelVars();
				// Repaint
				getParent().repaint();
			}
		});
		jpTRight.add(jbUnsplit);
		JButton jbInc = ButtonFactory.getNoMargins("<-");
		jbInc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				incPriorityOfSelVars();
				// Repaint
				getParent().repaint();
			}
		});
		jpTRight.add(jbInc);
		JButton jbDec = ButtonFactory.getNoMargins("->");
		jbDec.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				decPriorityOfSelVars();
				// Repaint
				getParent().repaint();
			}
		});
		jpTRight.add(jbDec);
		JButton jbSingle = ButtonFactory.getNoMargins("Single class");
		jbSingle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createSingleClass();
				// Repaint
				getParent().repaint();
			}
		});
		jpTRight.add(jbSingle);

		this.jpBottom = new JPanel(new GridBagLayout());
		this.jpBottom.setBorder(BorderFactory.createTitledBorder("Priority sets"));
		this.center.add(jpBottom, BorderLayout.CENTER);
		LogicalModel m = this.modelFeatures.getModel((String) jcbSBML
				.getSelectedItem());
		this.updatePriorityList(m);
	}

	private void splitSelVars() {
		ModelPriorityClasses mpc = this.userPriorityClasses
				.getModelPriorityClasses(this.selectedModel);
		for (int i = 0; i < this.guiClasses.size(); i++) {
			List<String> values = this.guiClasses.get(i)
					.getSelectedValuesList();
			if (!values.isEmpty()) {
				for (String var : values)
					mpc.split(i, var);
				break;
			}
		}
		this.updatePriorityList(this.selectedModel);
	}

	private void unsplitSelVars() {
		ModelPriorityClasses mpc = this.userPriorityClasses
				.getModelPriorityClasses(this.selectedModel);
		for (int i = 0; i < this.guiClasses.size(); i++) {
			List<String> values = this.guiClasses.get(i)
					.getSelectedValuesList();
			if (!values.isEmpty()) {
				for (String var : values)
					mpc.unsplit(i, var);
				break;
			}
		}
		this.updatePriorityList(this.selectedModel);
	}

	private void incPriorityOfSelVars() {
		ModelPriorityClasses mpc = this.userPriorityClasses
				.getModelPriorityClasses(this.selectedModel);
		for (int i = 0; i < this.guiClasses.size(); i++) {
			List<String> values = this.guiClasses.get(i)
					.getSelectedValuesList();
			if (!values.isEmpty()) {
				mpc.incPriorities(i, values);
				break;
			}
		}
		this.updatePriorityList(this.selectedModel);
	}

	private void decPriorityOfSelVars() {
		ModelPriorityClasses mpc = this.userPriorityClasses
				.getModelPriorityClasses(this.selectedModel);
		for (int i = 0; i < this.guiClasses.size(); i++) {
			List<String> values = this.guiClasses.get(i)
					.getSelectedValuesList();
			if (!values.isEmpty()) {
				mpc.decPriorities(i, values);
				break;
			}
		}
		this.updatePriorityList(this.selectedModel);
	}

	private void createSingleClass() {
		this.userPriorityClasses.getModelPriorityClasses(this.selectedModel)
				.singlePriorityClass();
		this.updatePriorityList(this.selectedModel);
	}

	private void updatePriorityList(LogicalModel m) {
		this.jpBottom.removeAll();
		this.guiClasses.clear();
		this.selectedModel = m;
		ModelPriorityClasses mpc = this.userPriorityClasses
				.getModelPriorityClasses(m);
		List<List<String>> priorities = mpc.getPriorityList();

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weightx = 0.2;
		gbc.weighty = 1;
		for (int i = 0; i < priorities.size(); i++) {
			gbc.gridx = i;
			gbc.gridy = 0;
			JLabel jlTmp = new JLabel("Rank " + (i + 1));
			this.jpBottom.add(jlTmp, gbc);

			DefaultListModel<String> lModel = new DefaultListModel<String>();
			for (int v = 0; v < priorities.get(i).size(); v++) {
				lModel.addElement(priorities.get(i).get(v));
			}
			gbc.gridy = 1;
			JList<String> jList = new JList<String>(lModel);
			jList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			jList.addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {
					@SuppressWarnings("unchecked")
					JList<String> selJList = (JList<String>) e.getSource();
					System.out.println(selJList.getSelectedIndex() + " : "
							+ selJList.getSelectedValue());
					for (JList<String> list : guiClasses) {
						if (!list.equals(selJList)) {
							list.clearSelection();
						}
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
				}
			});
			this.guiClasses.add(jList);
			this.jpBottom.add(jList, gbc);
		}
	}

	@Override
	protected void buttonReset() {
		this.userPriorityClasses = new EpitheliumPriorityClasses();
		for (LogicalModel m : this.epithelium.getEpitheliumGrid().getModelSet()) {
			this.userPriorityClasses.addModelPriorityClasses(this.epithelium
					.getPriorityClasses(m).clone());
		}
		this.updatePriorityList(this.selectedModel);
		// Repaint
		this.getParent().repaint();
	}

	@Override
	protected void buttonAccept() {
		for (LogicalModel m : this.userPriorityClasses.getModelSet()) {
			ModelPriorityClasses clone = this.userPriorityClasses
					.getModelPriorityClasses(m).clone();
			this.epithelium.setPriorityClasses(clone);
		}
	}

	@Override
	protected boolean isChanged() {
		for (LogicalModel m : this.epithelium.getEpitheliumGrid().getModelSet()) {
			ModelPriorityClasses clone = this.userPriorityClasses
					.getModelPriorityClasses(m);
			ModelPriorityClasses orig = this.epithelium.getPriorityClasses(m);
			if (!clone.equals(orig))
				return true;
		}
		return false;
	}
}
