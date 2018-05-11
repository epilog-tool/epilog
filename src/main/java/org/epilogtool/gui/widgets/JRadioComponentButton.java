package org.epilogtool.gui.widgets;

import javax.swing.JRadioButton;

public class JRadioComponentButton extends JRadioButton{

	private static final long serialVersionUID = 8578386573896903417L;

	private String shortComponentName;

	public JRadioComponentButton(String componentName){
		super(componentName);
		this.shortComponentName = componentName.length()>30?
				(componentName.substring(0, 26) + "..."):
				(componentName);
	}
	
	@Override
	public String getText(){
		if (super.getText().length()>30){
			return this.shortComponentName;
		}
		else {
			return super.getText();
		}
	}
	
	public String getComponentText(){
		return super.getText();
	}
}
