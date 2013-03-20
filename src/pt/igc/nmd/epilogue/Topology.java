package pt.igc.nmd.epilogue;

public class Topology {

	public int width;
	public int height;

	public Topology(int width, int height) {
		super();
		width = (width % 2 == 0) ? width : width + 1;
		height = (height % 2 == 0) ? height : height + 1;
		this.width = width;
		this.height = height;

	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getNumberInstances() {
		return this.height * this.width;
	}

	/* Methods to transform instance index to coordinates and vice-versa */
	public int instance2Row(int instance, int width) {

		int row = instance / width;
		return row;
	}

	public int instance2Column(int instance, int gridWidth) {

		int column = instance % gridWidth;
		return column;
	}

	public int coords2instance(int row, int column) {
		return row * this.width + column;
	}
	

}
