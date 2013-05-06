package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Hashtable;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.io.sbml.SBMLFormat;

import pt.igc.nmd.epilogue.integrationgrammar.IntegrationFunctionSpecification;
import pt.igc.nmd.epilogue.integrationgrammar.IntegrationFunctionSpecification.IntegrationExpression;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

public class SphericalEpithelium implements Epithelium, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1473898083699327135L;

	public Color colors[] = { Color.orange, Color.green, Color.blue,
			Color.pink, Color.yellow, Color.magenta, Color.cyan, Color.red,
			Color.LIGHT_GRAY, Color.black };

	private transient LogicalModel unitaryModel;
	private transient LogicalModel composedModel;
	private String unitarySBML = null;
	private String composedSBML = null;

	private Color[] nodeColor = null;
	private boolean[] displayComponents = null;
	private String[][] integrationFunctionStrings = null;
	private byte[] initialState = null;
	private byte grid[][] = null; // {instance , {nodeindex , value}}

	private Topology topology = null;

	public SphericalEpithelium(Topology topology) {

		this.topology = topology;
	}

	public void setInitialState(NodeInfo node, byte value) {
		this.initialState[getUnitaryModel().getNodeOrder().indexOf(node)] = value;
	}

	public byte getInitialState(NodeInfo node) {
		return this.initialState[getUnitaryModel().getNodeOrder().indexOf(node)];
	}

	private void initializeColors() {

		this.nodeColor = new Color[getUnitaryModel().getNodeOrder().size()];
		for (int i = 0; i < this.nodeColor.length; i++)
			this.nodeColor[i] = colors[i < colors.length ? i
					: colors.length - 1];
	}

	private void initializeDisplayComponents() {
		this.displayComponents = new boolean[getUnitaryModel().getNodeOrder()
				.size()];
		for (int i = 0; i < this.displayComponents.length; i++)
			this.displayComponents[i] = false;

	}

	private void initializeInitialState() {
		this.initialState = new byte[getUnitaryModel().getNodeOrder().size()];
	}

	public boolean isIntegrationComponent(NodeInfo node) {
		return this.integrationFunctionStrings[getUnitaryModel().getNodeOrder()
				.indexOf(node)] != null;
	}

	public String getIntegrationFunction(NodeInfo node, byte value) {
		return this.integrationFunctionStrings[getUnitaryModel().getNodeOrder()
				.indexOf(node)][value - 1];
	}

	public IntegrationExpression string2Expression(
			String integrationfunctionString) {

		IntegrationFunctionSpecification spec = new IntegrationFunctionSpecification();
		IntegrationExpression expression = null;

		try {
			expression = spec.parse(integrationfunctionString);
		} catch (org.antlr.runtime.RecognitionException e) {
			// TODO: Must send to interface
			e.printStackTrace();
		}

		return expression;

	}

	public void setIntegrationFunctions(NodeInfo node, byte value,
			String expression) {
		if (this.integrationFunctionStrings[getUnitaryModel().getNodeOrder()
				.indexOf(node)] == null)
			this.integrationFunctionStrings[getUnitaryModel().getNodeOrder()
					.indexOf(node)] = new String[node.getMax()];
		this.integrationFunctionStrings[getUnitaryModel().getNodeOrder()
				.indexOf(node)][value - 1] = expression;

	}

	public Topology getTopology() {
		return this.topology;
	}

	@Override
	public void setUnitaryModel(LogicalModel model) {
		this.unitaryModel = model;
		initializeColors();
		initializeDisplayComponents();
		initializeIntegrationFunctions();
		initializeInitialState();
		initializeGrid();

		SBMLFormat format = new SBMLFormat();
		ByteOutputStream bos = new ByteOutputStream();
		try {
			format.export(this.unitaryModel, bos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.unitarySBML = new String(bos.getBytes());

	}

	private void initializeIntegrationFunctions() {
		this.integrationFunctionStrings = new String[getUnitaryModel()
				.getNodeOrder().size()][];
	}

	@Override
	public LogicalModel getUnitaryModel() {
		if (this.unitaryModel == null && this.unitarySBML != null) {
			File tempFile = new File("temp" + System.currentTimeMillis()
					+ ".sbml");
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(tempFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				fos.write(this.unitarySBML.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			SBMLFormat format = new SBMLFormat();
			try {
				setUnitaryModel(format.importFile(tempFile));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			tempFile.delete();

		}
		return this.unitaryModel;
	}

	@Override
	public LogicalModel getComposedModel() {
		return this.composedModel;
	}

	public void setComposedModel(LogicalModel composedModel) {
		this.composedModel = composedModel;
	}

	public void setColor(NodeInfo node, Color color) {
		this.nodeColor[getUnitaryModel().getNodeOrder().indexOf(node)] = color;
	}

	public boolean isDisplayComponentOn(NodeInfo node) {
		return this.displayComponents[getUnitaryModel().getNodeOrder().indexOf(
				node)];
	}

	public void setActiveComponent(NodeInfo node, boolean bool) {
		this.displayComponents[getUnitaryModel().getNodeOrder().indexOf(node)] = bool;
	}

	public void setActiveComponent(NodeInfo node) {
		setActiveComponent(node, true);
	}

	public Color getColor(NodeInfo node) {
		return nodeColor[getUnitaryModel().getNodeOrder().indexOf(node)];
	}

	public void initializeGrid() {

		this.grid = new byte[topology.getNumberInstances()][];
		for (int i = 0; i < topology.getNumberInstances(); i++) {
			this.grid[i] = new byte[getUnitaryModel().getNodeOrder().size()];
			for (NodeInfo node : getUnitaryModel().getNodeOrder())
				grid[i][getUnitaryModel().getNodeOrder().indexOf(node)] = 0;
		}
	}

	public byte getGridValue(Integer instance, NodeInfo node) {

		return this.grid[instance][getUnitaryModel().getNodeOrder().indexOf(
				node)];
	}

	public void setGrid(Integer instance, NodeInfo node, byte value) {
		this.grid[instance][getUnitaryModel().getNodeOrder().indexOf(node)] = value;
	}

	public SphericalEpithelium getEpithelium(){
		return this;
	}
}
