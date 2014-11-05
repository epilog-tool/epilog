package org.ginsim.epilog.common;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

public class Web {

	public static boolean openURI(String uri) {
		try {
			Desktop.getDesktop().browse(new URI(uri));
			return true;
		} catch (Exception e) {
			try {
				// If java.awt.Desktop class is not supported tries xdg-open
				Runtime.getRuntime().exec("xdg-open " + uri);
				return true;
			} catch (IOException e1) {
				return false;
			}
		}
	}
}
