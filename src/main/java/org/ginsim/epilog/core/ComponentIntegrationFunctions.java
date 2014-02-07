package org.ginsim.epilog.core;

import java.util.ArrayList;
import java.util.List;

public class ComponentIntegrationFunctions {
	private List<String> integrationFunction;

	public ComponentIntegrationFunctions(int maxValue) {
		this.integrationFunction = new ArrayList<String>();
		for (int i = 0; i < maxValue; i++) {
			this.integrationFunction.add("");
		}
	}

	public List<String> get() {
		return this.integrationFunction;
	}

	public void set(List<String> functions) {
		this.integrationFunction = functions;
	}
}
