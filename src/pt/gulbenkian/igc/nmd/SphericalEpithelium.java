package pt.gulbenkian.igc.nmd;

import org.colomoto.logicalmodel.LogicalModel;

public class SphericalEpithelium implements Epithelium {

	private int width = 0;
	private int height = 0;
	private LogicalModel model = null;

	public SphericalEpithelium(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void setModel(LogicalModel model) {
		this.model = model;
	}

	@Override
	public LogicalModel getModel() {
		return this.model;
	}

//	public boolean neighbours(int xPosition, int yPosition, int maxNeighbors,
//			int minNeighbours, int gene, int expressionLevel) {
//		int count = 0;
//
//		for (int i = 0; i < NeighboursDEF.lenght(); i = i + 1) {
//			if (xPosition % 2 == 0) {
//				int xPositionNeighbour = xPosition + NeighboursDEFx[0][i];
//				int yPositionNeighbour = yPosition + NeighboursDEFy[i];
//			}
//
//			else if (xPosition % 2 != 0) {
//				int xPositionNeighbour = xPosition + NeighboursDEFx[1][i];
//				int yPositionNeighbour = yPosition + NeighboursDEFy[i];
//			}
//			if (SpericalEpithelium.getExpression(xPositionNeighbour,
//					yPositionNeighbour, gene) == expressionLevel)
//				count = count + 1;
//
//		}
//		if (count >= minNeighbours && count <= maxNeighbors)
//			return true;
//		else
//			return false;
//	}
}
