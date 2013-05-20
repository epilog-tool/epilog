package pt.igc.nmd.epilog;


import org.colomoto.logicalmodel.LogicalModel;

public interface Epithelium{

	public LogicalModel getUnitaryModel();
	public void setUnitaryModel(LogicalModel logicalModel);
	public LogicalModel getComposedModel();	
}
