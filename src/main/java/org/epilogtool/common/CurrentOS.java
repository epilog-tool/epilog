package org.epilogtool.common;

/**
 * Detect the current Operating System and provide the information in an easier
 * way for some OS-dependent operations.
 * 
 * Copied from GINsim by Pedro T. Monteiro
 * 
 * @author Aurelien Naldi
 */
public enum CurrentOS {

	UNKNOWN, LINUX, MACOSX, WINDOWS;

	public final static CurrentOS CURRENT_OS;

	static {
		String os_name = System.getProperty("os.name").toLowerCase();
		if (os_name.startsWith("windows")) {
			CURRENT_OS = WINDOWS;
		} else if (os_name.startsWith("mac")) {
			CURRENT_OS = MACOSX;
		} else if (os_name.startsWith("linux")) {
			CURRENT_OS = LINUX;
		} else {
			CURRENT_OS = UNKNOWN;
		}
	}
}
