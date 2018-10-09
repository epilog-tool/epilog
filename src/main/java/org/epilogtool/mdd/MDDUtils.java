package org.epilogtool.mdd;

import java.util.HashMap;
import java.util.Map;

import org.colomoto.biolqm.LogicalModel;

public class MDDUtils {
	private Map<LogicalModel, ModelMDD> mModelMDDs;
	private static MDDUtils mddutils;

	public static MDDUtils getInstance() {
		if (mddutils == null) {
			mddutils = new MDDUtils();
		}
		return mddutils;
	}

	private MDDUtils() {
		this.mModelMDDs = new HashMap<LogicalModel, ModelMDD>();
	}
	public ModelMDD getModelMDDs(LogicalModel m) {
		if (!this.mModelMDDs.containsKey(m)) {
			this.mModelMDDs.put(m, new ModelMDD(m));
		}
		return this.mModelMDDs.get(m);
	}
}
