package org.epilogtool.gui.widgets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.colomoto.logicalmodel.LogicalModel;
import org.epilogtool.project.ProjectFeatures;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.topology.Topology;
import org.epilogtool.gui.tab.EpiTabDefinitions.TabProbablyChanged;

public class VisualGridModel extends VisualGridDefinitions {
	private static final long serialVersionUID = -8878704517273291774L;

	private LogicalModel[][] modelGridClone;
	private Map<LogicalModel, Color> colorMapClone;
	private ProjectFeatures projectFeatures;
	private String selModelName;
	private boolean isRectFill;
	private Tuple2D<Integer> initialRectPos;
	private GridInformation valuePanel;
	private JPanel jpModelsUsed;

	public VisualGridModel(int gridX, int gridY, Topology topology,
			LogicalModel[][] modelGridClone,
			Map<LogicalModel, Color> colorMapClone,
			ProjectFeatures projectFeatures, GridInformation valuePanel,
			TabProbablyChanged tpc, JPanel jpModelsUsed) {
		super(gridX, gridY, topology, tpc);
		this.modelGridClone = modelGridClone;
		this.colorMapClone = colorMapClone;
		this.projectFeatures = projectFeatures;
		this.selModelName = null;
		this.isRectFill = false;
		this.initialRectPos = null;
		this.valuePanel = valuePanel;
		this.jpModelsUsed = jpModelsUsed;

		this.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mousePosition2Grid(e);
				updateComponentValues(mouseGrid);
				updateModelUsed();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				mousePosition2Grid(e);
				if (isRectFill) {
					drawRectangleOverSelectedCells();
				} else {
					paintCellAt(mouseGrid);
					updateModelUsed();
				}
			}
		});
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (isRectFill) {
					applyRectangleOnCells(initialRectPos, mouseGrid);
					updateModelUsed();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (isRectFill) {
					initialRectPos = mouseGrid.clone();
				} else {
					paintCellAt(mouseGrid);
					updateModelUsed();
				}
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
	}
	
	private void updateModelUsed(){
		this.jpModelsUsed.removeAll();
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 5, 1, 0);
		
		int i = 0;
		List<String> models = new ArrayList<String>();
		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {
				gbc.gridy = i;
				i++;
				gbc.gridx = 0;
//				gbc.anchor = GridBagConstraints.WEST;
				LogicalModel model = this.modelGridClone[x][y];
				String modelString = this.projectFeatures.getModelName(model);
				if (!models.contains(modelString)){
					models.add(modelString);
					JLabel modelName = new JLabel(modelString);
					this.jpModelsUsed.add(modelName, gbc);
		}}}
		this.jpModelsUsed.repaint();
		this.revalidate();
		
	}

	private void drawRectangleOverSelectedCells() {
		// Get selected model color
		LogicalModel m = this.projectFeatures.getModel(this.selModelName);
		Color c = this.colorMapClone.get(m);

		// Paint the rectangle
		super.highlightCellsOverRectangle(this.initialRectPos, this.mouseGrid,
				c);
		updateModelUsed();
	}

	private void updateComponentValues(Tuple2D<Integer> pos) {
		if (!isInGrid(pos))
			return;

		this.valuePanel.updateValues(pos.getX(), pos.getY(), null,
				this.modelGridClone);
	}

	public void setSelModelName(String name) {
		this.selModelName = name;
	}

	public void isRectangleFill(boolean fill) {
		this.isRectFill = fill;
	}

	protected void applyDataAt(int x, int y) {
		if (this.selModelName == null)
			return;
		LogicalModel m = this.projectFeatures.getModel(this.selModelName);
		if (!this.tpc.isChanged() && !this.modelGridClone[x][y].equals(m)) {
			this.tpc.setChanged();
		}
		this.modelGridClone[x][y] = m;
		updateModelUsed();
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		this.radius = this.topology.computeBestRadius(this.gridX, this.gridY,
				this.getSize().width, this.getSize().height);

		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {
				Tuple2D<Double> center = topology.getPolygonCenter(this.radius,
						x, y);
				Polygon polygon = topology
						.createNewPolygon(this.radius, center);
				Color c = this.colorMapClone.get(this.modelGridClone[x][y]);
				this.paintPolygon(this.strokeBasic, c, polygon, g2);
			}
		}
	}
}