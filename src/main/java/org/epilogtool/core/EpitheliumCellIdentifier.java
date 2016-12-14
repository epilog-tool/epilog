package org.epilogtool.core;

public class EpitheliumCellIdentifier {
	
	private int root;
	private String identifier;
	private int depth;
	
	public EpitheliumCellIdentifier(int root) {
		this.root = root;
		this.depth = 0;
		this.identifier = "";
	}
	
	public int getRoot() {
		return this.root;
	}
	
	public void setRoot(int root) {
		this.root = root;
		if (root == 0 ) {
			this.depth = 0;
			this.identifier = "";
		}
	}
	
	public int getDepth() {
		return this.depth;
	}
	
	public void addDepth(boolean bool) {
		this.depth+=1;
		if (bool==true) {
			this.identifier += "1";
		} else {
			this.identifier += "0";
		}
	}
	
	//TODO: correct this
	public EpitheliumCellIdentifier getParent() {
		if (this.depth==0) return null;
		EpitheliumCellIdentifier parentID = new EpitheliumCellIdentifier(this.root);
		parentID.depth = this.depth-1;
		parentID.identifier = this.identifier.substring(0, identifier.length()-1);
		return parentID;
	}
	
	public boolean equals(Object o) {
		EpitheliumCellIdentifier oECI = (EpitheliumCellIdentifier) o;
		return this.root==oECI.root && this.depth==oECI.depth && this.identifier.equals(oECI.identifier);
	}
	
	public EpitheliumCellIdentifier clone() {
		EpitheliumCellIdentifier newECI= new EpitheliumCellIdentifier(this.root);
		newECI.depth = this.depth;
		newECI.identifier = new String(this.identifier);
		return newECI;
	}
	
	public String toString() {
		String stg = Integer.toString(this.root) + "\t";
		stg += this.identifier;
		return stg;
	}
	
	public int hashCode() {
		int hash = 1;
		hash = hash * 17 + this.root;
		hash = hash * 31 + this.identifier.hashCode();
		hash = hash * 13 + (this.depth);
		return hash;
	}
}
