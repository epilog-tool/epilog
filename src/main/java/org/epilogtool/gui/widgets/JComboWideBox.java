package org.epilogtool.gui.widgets;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;


public class JComboWideBox extends JComboBox{
	
	protected int openDim;
    protected boolean layingOut = false;
	//acquired from
	//http://www.jroller.com/santhosh/entry/make_jcombobox_popup_wide_enough

	public JComboWideBox() { 
		this.setPreferredWidth();
		this.setDimensions();
    } 
	
    public JComboWideBox(final Object items[]) { 
        super(items); 
        this.setPreferredWidth();
        this.setDimensions();
    } 
	
	public JComboWideBox(Vector items) {
		super(items);
		this.setPreferredWidth();
		this.setDimensions();
	}

	public JComboWideBox(ComboBoxModel aModel) {
		super(aModel);
		this.setPreferredWidth();
		this.setDimensions();
	}
	
    public JComboWideBox(final String items[]){ 
        super(items); 
        this.setPreferredWidth();
        this.setDimensions();
    } 
    
    public void setPreferredWidth(){
    	this.openDim = this.getPreferredSize().width;
    }
    
    public int getPreferredWidth(){
    	return this.openDim;
    }

    public void setDimensions(){
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
            dim.width = Math.max(dim.width, this.getPreferredWidth()); 
        return dim; 
    } 
}
