package org.ginsim.epilog.gui.color;

import java.awt.Color;
import java.util.Random;

public abstract class EpiColor {
	protected Color color;

	protected EpiColor() {
		color = this.getRandomColor();
	}

	private int getRandomNumber(int n) {
		return (new Random()).nextInt(n);
	}

	private Color getRandomColor() {
		return new Color(this.getRandomNumber(256), this.getRandomNumber(256),
				this.getRandomNumber(256));
	}

	public Color getColor() {
		return this.color;
	}
}
