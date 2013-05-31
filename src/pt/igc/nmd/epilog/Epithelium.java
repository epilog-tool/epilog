package pt.igc.nmd.epilog;


import java.awt.Color;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

import pt.igc.nmd.epilog.integrationgrammar.IntegrationFunctionSpecification.IntegrationExpression;

public interface Epithelium{

	public LogicalModel getUnitaryModel();
	public void setUnitaryModel(LogicalModel logicalModel);
	public LogicalModel getComposedModel();	
	
	public byte getGridValue(Integer instance, NodeInfo node);
	
	
	public boolean isIntegrationComponent(NodeInfo node);
	public IntegrationExpression[] getIntegrationExpressionsForInput(NodeInfo node);
	public boolean isDisplayComponentOn(NodeInfo node);
	public Color getColor(NodeInfo node);
	
}
