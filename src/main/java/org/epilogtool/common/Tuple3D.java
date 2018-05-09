package org.epilogtool.common;

public class Tuple3D<T extends Number> {
	private T x;
	private T y;
	private String node;

	public Tuple3D(T x, T y, String node) {
		this.x = x;
		this.y = y;
		this.node = node;
	}

//	public Tuple3D<T> clone() {
//		return new Tuple3D<T>(this.x, this.y);
//	}
	
	public String getNode() {
		return this.node;
	}

	public void setNode(String node) {
		this.node = node;
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
		return "(" + this.x + "," + this.y + "," +node +")";
	}
//
//	public Tuple3D<T> getMin(Tuple3D<T> t) {
//		Tuple3D<T> newTuple = t.clone();
//		if (this.x instanceof Integer) {
//			if (this.x.intValue() < t.x.intValue())
//				newTuple.setX(this.x);
//			if (this.y.intValue() < t.y.intValue())
//				newTuple.setY(this.y);
//		} else if (this.x instanceof Double) {
//			if (this.x.doubleValue() < t.x.doubleValue())
//				newTuple.setX(this.x);
//			if (this.y.doubleValue() < t.y.doubleValue())
//				newTuple.setY(this.y);
//		} else if (this.x instanceof Float) {
//			if (this.x.floatValue() < t.x.floatValue())
//				newTuple.setX(this.x);
//			if (this.y.floatValue() < t.y.floatValue())
//				newTuple.setY(this.y);
//		} else if (this.x instanceof Long) {
//			if (this.x.longValue() < t.x.longValue())
//				newTuple.setX(this.x);
//			if (this.y.longValue() < t.y.longValue())
//				newTuple.setY(this.y);
//		} else if (this.x instanceof Short) {
//			if (this.x.shortValue() < t.x.shortValue())
//				newTuple.setX(this.x);
//			if (this.y.shortValue() < t.y.shortValue())
//				newTuple.setY(this.y);
//		}
//		return newTuple;
//	}
//
//	public Tuple3D<T> getMax(Tuple3D<T> t) {
//		Tuple3D<T> newTuple = t.clone();
//		if (this.x instanceof Integer) {
//			if (this.x.intValue() > t.x.intValue())
//				newTuple.setX(this.x);
//			if (this.y.intValue() > t.y.intValue())
//				newTuple.setY(this.y);
//		} else if (this.x instanceof Double) {
//			if (this.x.doubleValue() > t.x.doubleValue())
//				newTuple.setX(this.x);
//			if (this.y.doubleValue() > t.y.doubleValue())
//				newTuple.setY(this.y);
//		} else if (this.x instanceof Float) {
//			if (this.x.floatValue() > t.x.floatValue())
//				newTuple.setX(this.x);
//			if (this.y.floatValue() > t.y.floatValue())
//				newTuple.setY(this.y);
//		} else if (this.x instanceof Long) {
//			if (this.x.longValue() > t.x.longValue())
//				newTuple.setX(this.x);
//			if (this.y.longValue() > t.y.longValue())
//				newTuple.setY(this.y);
//		} else if (this.x instanceof Short) {
//			if (this.x.shortValue() > t.x.shortValue())
//				newTuple.setX(this.x);
//			if (this.y.shortValue() > t.y.shortValue())
//				newTuple.setY(this.y);
//		}
//		return newTuple;
//	}
//	
	public boolean equals(Object o){
		@SuppressWarnings("unchecked")
		Tuple3D<T> otherTuple = (Tuple3D<T>) o;
		return this.getX()==otherTuple.getX() & this.getY() == otherTuple.getY();
	}
	
	public int hashCode(){
		return this.getX().intValue() * 31 + this.getY().intValue();
	}
}
