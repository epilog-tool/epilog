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

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.colomoto.biolqm.LogicalModel;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.common.Txt;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.core.cell.CellFactory;
import org.epilogtool.core.cell.LivingCell;
import org.epilogtool.core.topology.Topology;
import org.epilogtool.gui.tab.EpiTabDefinitions.TabProbablyChanged;
import org.epilogtool.project.Project;

public class VisualGridModel extends VisualGridDefinitions {
	private static final long serialVersionUID = -8878704517273291774L;

	private EpitheliumGrid epiGridClone;
	private String selModelName;
	private boolean isRectFill;
	private Tuple2D<Integer> initialRectPos;
	private GridInformation valuePanel;
	private JPanel jpModelsUsed;

	public VisualGridModel(int gridX, int gridY, Topology topology, EpitheliumGrid epiGrid,
			GridInformation valuePanel, TabProbablyChanged tpc, JPanel jpModelsUsed) {
		super(gridX, gridY, topology, tpc);
		this.epiGridClone = epiGrid;
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

	/**
	 * Updates the list of assigned models in the grid. These changes reflect on the
	 * models assigned panel.
	 */
	public void updateModelUsed() {
		this.jpModelsUsed.removeAll();

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(0, 0, 0, 0);

		int i = 0;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		List<String> models = new ArrayList<String>();
		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {
				gbc.gridy = i;
				i++;
		
				if (this.epiGridClone.getAbstCell(x, y).isLivingCell()){
					LogicalModel model =((LivingCell) (this.epiGridClone.getAbstCell(x, y))).getModel();
					String modelString = Project.getInstance().getProjectFeatures().getModelName(model);
					if (!models.contains(modelString)) {
						models.add(modelString);
						JLabel modelName = new JLabel(modelString);
						this.jpModelsUsed.add(modelName, gbc);
					}
				}
			}
		}
		this.jpModelsUsed.repaint();
		this.revalidate();

	}

	private void drawRectangleOverSelectedCells() {
		// Get selected model color
		LogicalModel m = Project.getInstance().getProjectFeatures().getModel(this.selModelName);
		Color c = Project.getInstance().getProjectFeatures().getModelColor(m);

		// Paint the rectangle
		super.highlightCellsOverRectangle(this.initialRectPos, this.mouseGrid, c);
		updateModelUsed();
	}

	private void updateComponentValues(Tuple2D<Integer> pos) {
		if (!isInGrid(pos))
			return;

		this.valuePanel.updateValues(pos.getX(), pos.getY(), null, this.epiGridClone.getCellGrid());
	}

	public void setSelModelName(String name) {
		this.selModelName = name;
	}

	public void isRectangleFill(boolean fill) {
		this.isRectFill = fill;
	}


	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		this.radius = this.topology.computeBestRadius(this.gridX, this.gridY, this.getSize().width,
				this.getSize().height);

		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {
				Color c;
				if (this.epiGridClone.getAbstCell(x, y).isLivingCell()) {
					LogicalModel m = ((LivingCell) this.epiGridClone.getAbstCell(x, y)).getModel();
					c =  Project.getInstance().getProjectFeatures().getModelColor(m);
				}
				else {
					c =  Project.getInstance().getProjectFeatures().getAbstCellColor(this.epiGridClone.getAbstCell(x, y).getName());
				}
					
				Tuple2D<Double> center = topology.getPolygonCenter(this.radius, x, y);
				Polygon polygon = topology.createNewPolygon(this.radius, center);
				this.paintPolygon(this.strokeBasic, c, polygon, g2);
			}
		}
	}

	@Override
	protected void applyDataAt(int x, int y) {
		
		if (this.selModelName == null)
		return;
		
		
	if (this.selModelName.equals(Txt.get("s_INVALID_CELL"))) {
		if (!this.epiGridClone.getAbstCell(x, y).getName().equals(Txt.get("s_INVALID_CELL")))
			this.tpc.setChanged();
		this.epiGridClone.setAbstractCell(CellFactory.newInvalidCell(new Tuple2D<Integer>(x,y)));
		
	}
	else if (this.selModelName.equals(Txt.get("s_EMPTY_CELL"))) {
		if (!this.epiGridClone.getAbstCell(x, y).getName().equals(Txt.get("s_EMPTY_CELL")))
			this.tpc.setChanged();
		this.epiGridClone.setAbstractCell(CellFactory.newEmptyCell(new Tuple2D<Integer>(x,y)));
	}
	else if (this.selModelName.equals(Txt.get("s_DEAD_CELL"))) {
		if (!this.epiGridClone.getAbstCell(x, y).getName().equals(Txt.get("s_DEAD_CELL")))
			this.tpc.setChanged();
		this.epiGridClone.setAbstractCell(CellFactory.newDeadCell(new Tuple2D<Integer>(x,y)));
	}
	else  {
		if (!this.epiGridClone.getAbstCell(x, y).getName().equals(Txt.get("s_LIVING_CELL")) || !((LivingCell) this.epiGridClone.getAbstCell(x, y)).getModel().equals(Project.getInstance().getModel(this.selModelName))){
			this.tpc.setChanged();
		}
		this.epiGridClone.setAbstractCell(CellFactory.newLivingCell(new Tuple2D<Integer>(x,y), Project.getInstance().getModel(this.selModelName)));
	}
	
	updateModelUsed();
	}
}