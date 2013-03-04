package pt.igc.nmd.epilogue;

public class Topology {
	
	public int width;
	public int height;
	public static int DEFAULT_WIDTH = 6;
	public static int DEFAULT_HEIGHT = 6;
	
	
	public Topology() {
		super();
		this.width =DEFAULT_WIDTH;
		this.height = DEFAULT_HEIGHT;
	}
	
	public Topology(int width, int height) {
		super();
		this.width = width;
		this.height = height;
	}
	
	
	
	public void setHeight(int height){
		this.height = height;
	}

	public void setWidth(int width){
		this.width = width;
	}
	
	public int getWidth(){
		return this.width;
	}
	
	public int getHeight(){
		return this.height;
	}
	
	public int getTotalInstances(){
		return this.height*this.width;
	}
	
	/* Methods to transform instance index to coordinates and vice-versa */
	public  int matrixRow(int instance, int width) {

		int row = instance / width;
		return row;
	}

	public  int matrixColumn(int instance, int gridWidth) {

		int column = instance % gridWidth;
		return column;
	}

	public  int coords2instance(int row, int column) {
		return row * this.width + column;
	}
	
}
