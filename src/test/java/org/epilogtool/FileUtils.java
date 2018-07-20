package org.epilogtool;

import java.io.File;

public class FileUtils {
	static File resourceFolder;
	static {
		resourceFolder = new File("target", "test-classes");
		if (!resourceFolder.isDirectory()) {
			throw new RuntimeException("No resource folder");
		}
	}

	public static File getResource(String group, String name) {
		File dir = resourceFolder;
		
		if (group != null && group.length() > 0) {
			dir = new File(resourceFolder, group);
			if (!dir.isDirectory()) {
				throw new RuntimeException("resource group not found: "+dir.getAbsolutePath());
			}
		}
		
		return new File(dir, name);
	}
}
