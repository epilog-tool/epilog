package org.epilogtool.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.epilogtool.core.Epithelium;
import org.epilogtool.gui.color.ColorUtils;

public class DialogReplaceSBML extends EscapableDialog {
	private static final long serialVersionUID = 1877338344309723137L;


	private JComboBox jcbModelName;


	private List<String> listModelNames;
	private boolean bIsOK;
	private boolean bIsNameOK;

	private JButton buttonCancel;
	private JButton buttonOK;
	private String model;
	private String newModel;
	private  List<Epithelium> epiList;

	private JPanel jpCenter;
	
	/**
	 * This method creates the Dialog that appears when replacing an SBML. Once the replacing model is defined, the user
	 * must choose in which epitheliums this change is to be made.
	 * 
	 * @param model -> Model to be replaced
	 * @param modelNames -> Models existing in the project (STRING)
	 * @param mapModel2Epithelium -> Map that receives as key a model and as value a list of epitheliums that contain the model
	 */
	public DialogReplaceSBML(String model, List<String> modelNames, List<Epithelium> epiList) {
		//TODO: Replace receiving list in Dialog
		this.listModelNames = modelNames;
		this.listModelNames.remove(model);
		this.listModelNames.remove("Empty cell");
		this.model = model;
		this.epiList = epiList;
		this.newModel = null;
		this.jpCenter = new JPanel();

		this.setLayout(new BorderLayout());
		
		// North Panel
		JPanel northPanel = new JPanel(new FlowLayout());

		northPanel.add(new JLabel("Replace: " + model + " with: "));

		// Name JComboBox
		String[] array = this.listModelNames.toArray(new String[this.listModelNames.size()]);
		this.jcbModelName = new JComboBox(array);
		
		
		jcbModelName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				String selectedModel = (String) jcbModelName.getSelectedItem();
				System.out.println(selectedModel);
			}
		});

		northPanel.add(this.jcbModelName);
		this.add(northPanel,BorderLayout.NORTH);
		
		//Center Panel
		
		this.jpCenter.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;

		this.jpCenter.add(new JLabel("Epithelium(s) to replace:"), c);
		
			for (Epithelium epi: epiList ){
				c.gridy = c.gridy+1;
				JCheckBox jcheckb = new JCheckBox(epi.getName(), false);
				this.jpCenter.add(jcheckb, c);
//				this.jpCenter.add(new JLabel(epi.getName()), c);
			
			}
		
		this.add(jpCenter);
	
		
		
		
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
		this.buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonAction(true);
			}
		});
		bottom.add(this.buttonOK);

		this.add(bottom, BorderLayout.SOUTH);

		this.validateTextField();

	}

	private void validateTextField() {
		this.bIsNameOK = false;
		
		List<String> listShortModelNames = new ArrayList<String>();
		for (String modelName:listModelNames){
			String newName = modelName.substring(0, modelName.length()-5);
			listShortModelNames.add(newName);
		}
		
	}

	private void buttonAction(boolean bIsOK) {
		this.bIsOK = bIsOK;
		this.dispose();
	}

	public boolean isDefined() {
		return this.bIsOK;
	}


	public String getModelName() {
		return this.jcbModelName.getName();
	}


	@Override
	public void focusComponentOnLoad() {
		// TODO Auto-generated method stub
		
	}

}
