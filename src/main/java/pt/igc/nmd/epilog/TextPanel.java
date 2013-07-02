package pt.igc.nmd.epilog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.colomoto.logicalmodel.LogicalModel;
//import javax.swing.event.MouseInputAdapter;
//import java.awt.*;
//import java.awt.event.MouseEvent;

import pt.igc.nmd.epilog.gui.MainFrame;



public class TextPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MainFrame mainPanel;
	private LogicalModel model;

	private JLabel[] components;
	private JPanel[] panels;

//    Rectangle currentRect = null;
//    Rectangle rectToDraw = null;
//    Rectangle previousRectDrawn = new Rectangle();
	
	
	public TextPanel(MainFrame mainPanel) {
		this.mainPanel = mainPanel;
		init();
	}

	public JPanel init() {
		
//		  MyListener myListener = new MyListener();
//		    addMouseListener(myListener);
//		    addMouseMotionListener(myListener);
		this.removeAll();
		model = mainPanel.getEpithelium().getUnitaryModel();

		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.TRAILING);
		
		LineBorder border = new LineBorder(Color.black, 1, true);
		TitledBorder title = new TitledBorder(border, "Value Analytics",
				TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, new Font(
						"Arial", Font.ITALIC, 14), Color.black);

		
		
		setBorder(title);
		setPreferredSize(new Dimension(620, 350));

		if (model != null) {
			components = new JLabel[model.getNodeOrder().size()];
			panels = new JPanel[model.getNodeOrder().size()];

			for (int i = 0; i < model.getNodeOrder().size(); i++) {

				components[i] = new JLabel(model.getNodeOrder().get(i)
						.getNodeID());
				panels[i] = new JPanel();
				panels[i].setPreferredSize(new Dimension(100, 25));
				components[i] = new JLabel();

				components[i].setText(model.getNodeOrder().get(i).getNodeID()
						+ ": " + "0 - "+ model.getNodeOrder().get(i).getMax());
				panels[i].add(components[i]);
				add(panels[i]);
			}
		}
		return this;
	}
	

	public void restartAnalytics(){
		this.removeAll();
	}
	
//	     public void SelectionArea(ComponentsPanel panel ) {
//	
//	     	    MyListener myListener = new MyListener();
//	            addMouseListener(myListener);
//	            addMouseMotionListener(myListener);
//	        }
//	     
//	        private class MyListener extends MouseInputAdapter {
//	            public void mousePressed(MouseEvent e) {
//	                int x = e.getX();
//	                int y = e.getY();
//	                currentRect = new Rectangle(x, y, 0, 0);
//	                updateDrawableRect(getWidth(), getHeight());
//	                repaint();
//	            }
//	     
//	            public void mouseDragged(MouseEvent e) {
//	                updateSize(e);
//	            }
//	     
//	            public void mouseReleased(MouseEvent e) {
//	                updateSize(e);
//	            }
//	     
//	            /* 
//	             * Update the size of the current rectangle
//	             * and call repaint.  Because currentRect
//	             * always has the same origin, translate it
//	             * if the width or height is negative.
//	             * 
//	             * For efficiency (though
//	             * that isn't an issue for this program),
//	             * specify the painting region using arguments
//	             * to the repaint() call.
//	             * 
//	             */
//	            void updateSize(MouseEvent e) {
//	                int x = e.getX();
//	                int y = e.getY();
//	                currentRect.setSize(x - currentRect.x,
//	                                    y - currentRect.y);
//	                updateDrawableRect(getWidth(), getHeight());
//	                Rectangle totalRepaint = rectToDraw.union(previousRectDrawn);
//	                repaint(totalRepaint.x, totalRepaint.y,
//	                        totalRepaint.width, totalRepaint.height);
//	            }
//	        }
//	     
//	        protected void paintComponent(Graphics g) {
//	            super.paintComponent(g); //paints the background and image
//	     
//	            //If currentRect exists, paint a box on top.
//	            if (currentRect != null) {
//	                //Draw a rectangle on top of the image.
//	                g.setXORMode(Color.red); //Color of line varies
//	                                           //depending on image colors
//	                g.drawRect(rectToDraw.x, rectToDraw.y, 
//	                           rectToDraw.width - 1, rectToDraw.height - 1);
//	     
//	            }
//	        }
//	     
//	        private void updateDrawableRect(int compWidth, int compHeight) {
//	            int x = currentRect.x;
//	            int y = currentRect.y;
//	            int width = currentRect.width;
//	            int height = currentRect.height;
//	     
//	            //Make the width and height positive, if necessary.
//	            if (width < 0) {
//	                width = 0 - width;
//	                x = x - width + 1; 
//	                if (x < 0) {
//	                    width += x; 
//	                    x = 0;
//	                }
//	            }
//	            if (height < 0) {
//	                height = 0 - height;
//	                y = y - height + 1; 
//	                if (y < 0) {
//	                    height += y; 
//	                    y = 0;
//	                }
//	            }
//	     
//	            //The rectangle shouldn't extend past the drawing area.
//	            if ((x + width) > compWidth) {
//	                width = compWidth - x;
//	            }
//	            if ((y + height) > compHeight) {
//	                height = compHeight - y;
//	            }
//	           
//	            //Update rectToDraw after saving old value.
//	            if (rectToDraw != null) {
//	                previousRectDrawn.setBounds(
//	                            rectToDraw.x, rectToDraw.y, 
//	                            rectToDraw.width, rectToDraw.height);
//	                rectToDraw.setBounds(x, y, width, height);
//	            } else {
//	                rectToDraw = new Rectangle(x, y, width, height);
//	            }
//	        }
	    }

