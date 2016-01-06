package org.epilogtool.gui.widgets;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;


public class JComboWideBox extends JComboBox{ 
	
	//acquired from
	//http://www.jroller.com/santhosh/entry/make_jcombobox_popup_wide_enough

	public JComboWideBox() { 
    } 
	
    public JComboWideBox(final Object items[]) { 
        super(items); 
    } 
	
	public JComboWideBox(Vector items) {
		super(items);
	}

	public JComboWideBox(ComboBoxModel aModel) {
		super(aModel);
	}
	
    public JComboWideBox(final String items[]){ 
        super(items); 
    } 

 
    private boolean layingOut = false; 
 
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
            dim.width = Math.max(dim.width, getPreferredSize().width); 
        return dim; 
    } 
}
