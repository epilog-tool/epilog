package org.epilogtool.common;

public class Tuple2D<T extends Number> {
	private T x;
	private T y;

	public Tuple2D(T x, T y) {
		this.x = x;
		this.y = y;
	}

	public Tuple2D<T> clone() {
		return new Tuple2D<T>(this.x, this.y);
	}

	public T getX() {
		return x;
	}

	public void setX(T x) {
		this.x = x;
	}

	public T getY() {
		return y;
	}

	public void setY(T y) {
		this.y = y;
	}

	public String toString() {
		return "(" + this.x + "," + this.y + ")";
	}

	public Tuple2D<T> getMin(Tuple2D<T> t) {
		Tuple2D<T> newTuple = t.clone();
		if (this.x instanceof Integer) {
			if (this.x.intValue() < t.x.intValue())
				newTuple.setX(this.x);
			if (this.y.intValue() < t.y.intValue())
				newTuple.setY(this.y);
		} else if (this.x instanceof Double) {
			if (this.x.doubleValue() < t.x.doubleValue())
				newTuple.setX(this.x);
			if (this.y.doubleValue() < t.y.doubleValue())
				newTuple.setY(this.y);
		} else if (this.x instanceof Float) {
			if (this.x.floatValue() < t.x.floatValue())
				newTuple.setX(this.x);
			if (this.y.floatValue() < t.y.floatValue())
				newTuple.setY(this.y);
		} else if (this.x instanceof Long) {
			if (this.x.longValue() < t.x.longValue())
				newTuple.setX(this.x);
			if (this.y.longValue() < t.y.longValue())
				newTuple.setY(this.y);
		} else if (this.x instanceof Short) {
			if (this.x.shortValue() < t.x.shortValue())
				newTuple.setX(this.x);
			if (this.y.shortValue() < t.y.shortValue())
				newTuple.setY(this.y);
		}
		return newTuple;
	}

	public Tuple2D<T> getMax(Tuple2D<T> t) {
		Tuple2D<T> newTuple = t.clone();
		if (this.x instanceof Integer) {
			if (this.x.intValue() > t.x.intValue())
				newTuple.setX(this.x);
			if (this.y.intValue() > t.y.intValue())
				newTuple.setY(this.y);
		} else if (this.x instanceof Double) {
			if (this.x.doubleValue() > t.x.doubleValue())
				newTuple.setX(this.x);
			if (this.y.doubleValue() > t.y.doubleValue())
				newTuple.setY(this.y);
		} else if (this.x instanceof Float) {
			if (this.x.floatValue() > t.x.floatValue())
				newTuple.setX(this.x);
			if (this.y.floatValue() > t.y.floatValue())
				newTuple.setY(this.y);
		} else if (this.x instanceof Long) {
			if (this.x.longValue() > t.x.longValue())
				newTuple.setX(this.x);
			if (this.y.longValue() > t.y.longValue())
				newTuple.setY(this.y);
		} else if (this.x instanceof Short) {
			if (this.x.shortValue() > t.x.shortValue())
				newTuple.setX(this.x);
			if (this.y.shortValue() > t.y.shortValue())
				newTuple.setY(this.y);
		}
		return newTuple;
	}
}
