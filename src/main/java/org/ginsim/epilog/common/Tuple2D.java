package org.ginsim.epilog.common;

public class Tuple2D {
	private int x;
	private int y;

	public Tuple2D(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Tuple2D clone() {
		return new Tuple2D(this.x, this.y);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String toString() {
		return "(" + this.x + "," + this.y + ")";
	}

	public Tuple2D getMin(Tuple2D t) {
		return new Tuple2D((this.x < t.getX()) ? this.x : t.getX(),
				(this.y < t.getY()) ? this.y : t.getY());
	}

	public Tuple2D getMax(Tuple2D t) {
		return new Tuple2D((this.x > t.getX()) ? this.x : t.getX(),
				(this.y > t.getY()) ? this.y : t.getY());
	}
}
