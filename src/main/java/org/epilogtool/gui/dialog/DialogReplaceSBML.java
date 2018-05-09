package org.epilogtool.gui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.epilogtool.common.Txt;
import org.epilogtool.core.Epithelium;

public class DialogReplaceSBML extends EscapableDialog {
	private static final long serialVersionUID = 1877338344309723137L;

	private JComboBox<String> jcbModelName;
	private List<String> listModelNames;
	private boolean bIsOK;

	private JButton buttonCancel;
	private JButton buttonOK;
	private List<String> selectedEpiList; // Epithelium List selected y the user to modify

	/**
	 * This method creates the Dialog that appears when replacing an SBML. Once the
	 * replacing model is defined, the user must choose in which epitheliums this
	 * change is to be made.
	 * 
	 * @param model
	 *            -> Model to be replaced
	 * @param modelNames
	 *            -> Models existing in the project (STRING)
	 * @param mapModel2Epithelium
	 *            -> Map that receives as key a model and as value a list of
	 *            epitheliums that contain the model
	 */
	public DialogReplaceSBML(String model, List<String> modelNames, List<Epithelium> epiList) {
		// TODO: Replace receiving list in Dialog

		this.listModelNames = modelNames;
		this.listModelNames.remove(model);
		this.listModelNames.remove("Empty cell");
		this.selectedEpiList = new ArrayList<String>();

		this.setLayout(new BorderLayout());

		// North Panel
		JPanel northPanel = new JPanel(new FlowLayout());

		northPanel.add(new JLabel("Replace Cellular Model " + model + " by "));

		// Name JComboBox
		String[] array = this.listModelNames.toArray(new String[this.listModelNames.size()]);
		this.jcbModelName = new JComboBox<String>(array);

		northPanel.add(this.jcbModelName);
		this.add(northPanel, BorderLayout.NORTH);

		// Center Panel
		JPanel jpCenter = new JPanel();
		jpCenter.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;

		jpCenter.add(new JLabel(Txt.get("s_REPLACE_SBML")), c);

		for (Epithelium epi : epiList) {
			c.gridy += 1;
			final String name = epi.getName();
			JPanel jp = new JPanel();
			JCheckBox jcheckb = new JCheckBox(name, false);
			jp.add(jcheckb);
			jcheckb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JCheckBox jcb = (JCheckBox) e.getSource();
					if (jcb.isSelected()) {
						addEpithelium2SelectedList(name);
					} else {
						removeEpithelium2SelectedList(name);
					}
				}

			});
			jpCenter.add(jp, c);
			jpCenter.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		}

		this.add(jpCenter, BorderLayout.CENTER);

		// Bottom Panel
		JPanel bottom = new JPanel(new FlowLayout());
		this.buttonCancel = new JButton("Cancel");
		this.buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonAction(false);
			}
		});
		bottom.add(this.buttonCancel);

		this.buttonOK = new JButton("OK");
		this.buttonOK.setEnabled(false);
		this.buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonAction(true);
			}
		});
		bottom.add(this.buttonOK);

		this.add(bottom, BorderLayout.SOUTH);
	}

	private void buttonAction(boolean bIsOK) {
		this.bIsOK = bIsOK;
		this.dispose();
	}

	public boolean isDefined() {
		return this.bIsOK;
	}

	public String getModelName() {
		return (String) jcbModelName.getSelectedItem();
	}

	public List<String> getEpiList() {
		return this.selectedEpiList;
	}

	private void addEpithelium2SelectedList(String name) {
		if (!this.selectedEpiList.contains(name)) {
			this.selectedEpiList.add(name);
		}
		this.buttonOK.setEnabled(true);
	}

	private void removeEpithelium2SelectedList(String name) {
		if (this.selectedEpiList.contains(name))
			this.selectedEpiList.remove(name);
		if (selectedEpiList.isEmpty())
			this.buttonOK.setEnabled(false);
	}

	@Override
	public void focusComponentOnLoad() {
		// TODO Auto-generated method stub

	}

}
