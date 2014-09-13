package org.ginsim.epilog.services;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.ginsim.epilog.core.topology.Topology;

/**
 * List available "services". To manage topologies at
 * org.ginsim.epilog.core.topology.*
 * 
 * @author Pedro T. Monteiro
 */
public class TopologyService {

	public static final String FILTER_DOT = "org.ginsim.epilog.core.topology.Topology";
	public static final String FILTER_SLH = "org/ginsim/epilog/core/topology/Topology";
	private final String CLASS = ".class";
	private static TopologyService MANAGER = null;

	/**
	 * Retrieve the single-instance service manager.
	 * 
	 * @return the service manager
	 */
	public static TopologyService getManager() {
		if (MANAGER == null) {
			MANAGER = new TopologyService();
		}
		return MANAGER;
	}

	private final List<String> lTopologyNames;

	private TopologyService() {
		this.lTopologyNames = new ArrayList<String>();
		ClassLoader cLoader = Topology.class.getClassLoader();

		URL topologyURL = cLoader.getResource(Topology.class.getName().replace(
				'.', File.separatorChar)
				+ this.CLASS);
		String basedir = (topologyURL != null) ? topologyURL.toString() : "";

		if (basedir.startsWith("file:")) {
			basedir = basedir.substring(basedir.indexOf("/"),
					basedir.lastIndexOf("/"));
			File fdir = new File(basedir);
			for (File file : fdir.listFiles()) {
				String name = file.toString();
				if (name.contains(FILTER_SLH)) {
					String className = FILTER_DOT
							+ name.substring(name.indexOf(FILTER_SLH)
									+ FILTER_SLH.length(), name.length()
									- this.CLASS.length());
					this.addTopology(cLoader, className);
				}
			}
		} else if (basedir.startsWith("jar:file:")) {
			try {
				JarFile jarf = new JarFile(new File(basedir.substring(9,
						basedir.indexOf("!/"))));
				Enumeration<JarEntry> jeIter = jarf.entries();
				while (jeIter.hasMoreElements()) {
					String filename = jeIter.nextElement().getName();
					if (filename.endsWith(this.CLASS)
							&& filename.contains(FILTER_SLH)) {
						String className = filename.replace(File.separatorChar,
								'.');
						className = className.substring(0, className.length()
								- this.CLASS.length());
						this.addTopology(cLoader, className);
					}
				}
				jarf.close();
			} catch (IOException e) {
			}
		}
	}

	private void addTopology(ClassLoader cLoader, String className) {
		try {
			@SuppressWarnings("unchecked")
			Class<Topology> cTop = (Class<Topology>) cLoader
					.loadClass(className);
			if (!Modifier.isAbstract(cTop.getModifiers()))
				this.lTopologyNames
						.add(className.substring(FILTER_DOT.length()));
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}

	/**
	 * Get the available topology classes
	 * 
	 * @return all available topology classes
	 */
	public List<String> getTopologyNames() {
		return Collections.unmodifiableList(this.lTopologyNames);
	}
}
