package org.ginsim.epilog.gui.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.ginsim.epilog.Tuple2D;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.topology.Topology;

public class GridPanel extends JPanel {
	private static final long serialVersionUID = 6126822003689575762L;

	private int gridX;
	private int gridY;
	private Epithelium epi;
	private double radius;
	
	private Tuple2D mouseGrid;

	/**
	 * Generates the hexagons grid.
	 * 
	 * @param mainframe
	 *            related mainframe
	 * 
	 * @see pt.igc.nmd.epilog.gui.MainFrame mainFrame
	 */
	public GridPanel(Epithelium epi) {
		this.gridX = epi.getEpitheliumGrid().getX();
		this.gridY = epi.getEpitheliumGrid().getY();
		this.epi = epi;
		// this.setBorder(BorderFactory.createTitledBorder("GridPanel"));
		this.setSize(800, 450);
		this.mouseGrid = new Tuple2D(-1, -1);
		this.addMousePositionListener();
	}

	private void addMousePositionListener() {
		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				Tuple2D mouseGrid = epi.getEpitheliumGrid().getTopology().getSelectedCell(radius, e.getX(), e.getY());
				
				System.out.println(e.getX() + "," + e.getY() + mouseGrid);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * Paints the hexagons grid. If there is no model loaded it paints in white,
	 * otherwise with the resulting color of the selected components.
	 * 
	 * @param g
	 *            graphics
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		BasicStroke stroke = new BasicStroke(1.0f);
		BasicStroke perturbedStroke = new BasicStroke(3.0f);
		Topology topology = this.epi.getEpitheliumGrid().getTopology();

		this.radius = topology.computeBestRadius(this.gridX, this.gridY,
				this.getSize().width, this.getSize().height);

		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {
				Polygon polygon = topology.createNewPolygon(this.radius, x, y);

				BasicStroke selectedStroke = stroke;
				if (this.epi.getEpitheliumGrid().getPerturbation(x, y) != null) {
					selectedStroke = perturbedStroke;
				}
				this.paintPolygon(selectedStroke,
						this.epi.getCellColor(x, y, new ArrayList<String>()),
						polygon, g2);
			}
		}
	}

	/**
	 * Paints all hexagons in white.
	 * 
	 * @param g
	 *            graphic (hexagons grid)
	 * 
	 * @see paintHexagons(BasicStroke stroke, Color color, Polygon polygon2,
	 *      Graphics2D g2)
	 */

	private void paintPolygon(BasicStroke stroke, Color color, Polygon polygon,
			Graphics2D g) {
		g.setStroke(stroke);
		g.setColor(color);
		g.fillPolygon(polygon);
		g.setColor(Color.black);
		g.drawPolygon(polygon);
	}
}