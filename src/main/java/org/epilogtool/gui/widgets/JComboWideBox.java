package org.epilogtool.gui.widgets;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;


public class JComboWideBox extends JComboBox{
	
	protected int adjustedDim;
    protected boolean layingOut = false;
	//acquired from
	//http://www.jroller.com/santhosh/entry/make_jcombobox_popup_wide_enough

	public JComboWideBox() { 
		this.setDimensions();
    } 
	
    public JComboWideBox(final Object items[]) { 
        super(items); 
        this.setDimensions();
    } 
	
	public JComboWideBox(Vector items) {
		super(items);
		this.setDimensions();
	}

	public JComboWideBox(ComboBoxModel aModel) {
		super(aModel);
		this.setDimensions();
	}
	
    public JComboWideBox(final String items[]){ 
        super(items); 
        this.setDimensions();
    } 

    private void setDimensions(){
    	this.adjustedDim = this.getPreferredSize().width;
    	this.setMinimumSize(new Dimension(200, 25));
    	this.setMinimumSize(new Dimension(200, 25));
    	this.setPreferredSize(new Dimension(200, 25));
    } 
 
    public void doLayout(){ 
        try{ 
            layingOut = true; 
            super.doLayout(); 
        }finally{ 
            layingOut = false; 
        } 
    } 
 
    public Dimension getSize(){ 
        Dimension dim = super.getSize();
        if(!layingOut) 
            dim.width = Math.max(dim.width, this.adjustedDim); 
        return dim; 
    } 
}
