package org.epilogtool.core;

public class EpitheliumCellIdentifier {
	
	private int root;
	private String identifier;
	
	public EpitheliumCellIdentifier() {
		this.root = -1;
		this.identifier = "";
	}
	
	public EpitheliumCellIdentifier(int root) {
		this.root = root;
		this.identifier = "";
	}
	
	public int getRoot() {
		return this.root;
	}
	
	public void setRoot(int root) {
		this.root = root;
		if (root == -1) {
			this.identifier = "";
		}
	}
	
	public void setIdentifier(String identifier) {
		assert !(this.root==-1);
		this.identifier = identifier;
	}
	
	public int getDepth() {
		return this.identifier.length();
	}
	
	public void addDepth(boolean bool) {
		if (bool==true) {
			this.identifier += "1";
		} else {
			this.identifier += "0";
		}
	}
	
	//TODO: correct this
	public EpitheliumCellIdentifier getParent() {
		if (this.getDepth()==0) return null;
		EpitheliumCellIdentifier parentID = new EpitheliumCellIdentifier(this.root);
		parentID.identifier = this.identifier.substring(0, identifier.length()-1);
		return parentID;
	}
	
	public void clear() {
		this.root = -1;
		this.identifier = "";
	}
	
	public boolean equals(Object o) {
		EpitheliumCellIdentifier oECI = (EpitheliumCellIdentifier) o;
		return this.root==oECI.root && this.identifier.equals(oECI.identifier);
	}
	
	public EpitheliumCellIdentifier clone() {
		EpitheliumCellIdentifier newECI= new EpitheliumCellIdentifier(this.root);
		newECI.identifier = new String(this.identifier);
		return newECI;
	}
	
	public String toString() {
		String stg = Integer.toString(this.root) + "-";
		stg += this.identifier;
		return stg;
	}
	
	public int hashCode() {
		int hash = 1;
		hash = hash * 17 + this.root;
		hash = hash * 31 + this.identifier.hashCode();
		return hash;
	}
}
