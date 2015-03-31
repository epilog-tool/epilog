package org.ginsim.epilog.core;

public class EpitheliumUpdateSchemeInter {
	public static float DEFAULT_ALPHA = (float) 1.0;

	private float alphaAsyncParam;

	public EpitheliumUpdateSchemeInter(float alpha) {
		this.alphaAsyncParam = alpha;
	}

	public EpitheliumUpdateSchemeInter clone() {
		return new EpitheliumUpdateSchemeInter(this.alphaAsyncParam);
	}

	public void setAlpha(float alpha) {
		this.alphaAsyncParam = alpha;
	}

	public float getAlpha() {
		return this.alphaAsyncParam;
	}
	
	public boolean equals(Object o) {
		EpitheliumUpdateSchemeInter otherObj = (EpitheliumUpdateSchemeInter) o;
		return this.alphaAsyncParam == otherObj.getAlpha();
	}
}
