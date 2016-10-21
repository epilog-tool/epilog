package org.epilogtool.gui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.epilogtool.core.Epithelium;

public class DialogReplaceSBML extends EscapableDialog {
	private static final long serialVersionUID = 1877338344309723137L;


	private JComboBox jcbModelName;


	private List<String> listModelNames;
	private boolean bIsOK;

	private JButton buttonCancel;
	private JButton buttonOK;
	private String model;				        // Model to be replaced
	private  List<Epithelium> epiList;         // Epithelium list that contain the model to be replaced
	private  List<Epithelium> selectedEpiList; //Epithelium List selected y the user to modify

	
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
		this.epiList = selectedEpiList;

		this.setLayout(new BorderLayout());
		
		// North Panel
		JPanel northPanel = new JPanel(new FlowLayout());

		northPanel.add(new JLabel("Replace: " + model + " with: "));

		// Name JComboBox
		String[] array = this.listModelNames.toArray(new String[this.listModelNames.size()]);
		this.jcbModelName = new JComboBox(array);
		
//		jcbModelName.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				@SuppressWarnings("unchecked")
//				String selectedModel = (String) jcbModelName.getSelectedItem();
//				System.out.println(selectedModel);
//			}
//		});

		northPanel.add(this.jcbModelName);
		this.add(northPanel,BorderLayout.NORTH);
		
		//Center Panel
		
		JPanel jpCenter = new JPanel();
		jpCenter.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;

		jpCenter.add(new JLabel("Epithelium(s) to replace:"), c);
		
			for (Epithelium epi: epiList ){
				c.gridy = c.gridy+1;
				JCheckBox jcheckb = new JCheckBox(epi.getName(), false);
				
				jpCenter.add(jcheckb, c);
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
	
	public List<Epithelium> getEpiList() {
		return this.selectedEpiList;
	}
	

	@Override
	public void focusComponentOnLoad() {
		// TODO Auto-generated method stub
		
	}



}
