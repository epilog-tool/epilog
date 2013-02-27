package pt.gulbenkian.igc.nmd;


import org.colomoto.logicalmodel.LogicalModel;

import composition.IntegrationFunctionMapping;
import composition.Topology;

public interface CompositionDialog {

	public Topology getTopology();
	public IntegrationFunctionMapping getMapping();
	public LogicalModel getUnitaryModel();
	
	

}
