package pt.gulbenkian.igc.nmd;

import org.colomoto.logicalmodel.LogicalModel;

public class SphericalEpithelium implements Epithelium {

	private int width = 0;
	private int height = 0;
	private LogicalModel unitaryModel = null;
	private LogicalModel composedModel = null;

	public SphericalEpithelium(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void setUnitaryModel(LogicalModel model) {
		this.unitaryModel = model;
	}

	@Override
	public LogicalModel getUnitaryModel() {
		return this.unitaryModel;
	}

	@Override
	public LogicalModel getComposedModel() {
		return null;
	}


}
