package org.ginsim.epilog.services;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.ginsim.epilog.core.topology.RollOver;
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
	public static final String FILTER_SLH_W = "org\\ginsim\\epilog\\core\\topology\\Topology";
	private final String CLASS = ".class";
	private static TopologyService MANAGER = null;

	private Map<String, String> mDesc2topID;
	private Map<String, Constructor<Topology>> mtopID2Constructor;
	
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

	private TopologyService() {
		this.mDesc2topID = new HashMap<String, String>();
		this.mtopID2Constructor = new HashMap<String, Constructor<Topology>>();
		ClassLoader cLoader = Topology.class.getClassLoader();
		URL topologyURL = cLoader.getResource(Topology.class.getName().replace(
				'.', File.separatorChar)
				+ this.CLASS);
		String basedir = (topologyURL != null) ? topologyURL.toString() : "";
		
		if (basedir.startsWith("file:")) {
			basedir = basedir.substring(basedir.indexOf("/"),
					basedir.lastIndexOf("/")) + "/org/ginsim/epilog/core/topology";
			if (System.getProperty("os.name").startsWith("Windows")) {
				//if we're working on a Windows OS, path must be redefined (why???) and 
				//FILTER_SLH variable must be adapted (switch from "/" to "\")
				basedir = basedir.substring(1);
				File fdir_w = new File(basedir);
				for (File file_w : fdir_w.listFiles()) {
					String name_w = file_w.toString();
					if (name_w.contains(FILTER_SLH_W)) {
						String className = FILTER_DOT
								+ name_w.substring(name_w.indexOf(FILTER_SLH_W)
										+ FILTER_SLH_W.length(), name_w.length()
										- this.CLASS.length());
						this.addTopology(cLoader, className);
					}
				}
			}
			else {
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
			} 
		}
		else if (basedir.startsWith("jar:file:")) {
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
			if (!Modifier.isAbstract(cTop.getModifiers())) {
				Constructor<Topology> c = cTop.getConstructor(Integer.TYPE,
						Integer.TYPE, RollOver.class);
				Topology instance = c.newInstance(0, 0, RollOver.NOROLLOVER);
				String desc = instance.getDescription();
				String topID = className.substring(FILTER_DOT.length());
				this.mtopID2Constructor.put(topID, c);
				this.mDesc2topID.put(desc, topID);
			}
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public Topology getNewTopology(String topologyID, int gridX, int gridY,
			RollOver rollover) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException {
		Constructor<Topology> c = this.mtopID2Constructor.get(topologyID);
		return c.newInstance(gridX, gridY, rollover);
	}

	public Set<String> getTopologyDescriptions() {
		return Collections.unmodifiableSet(this.mDesc2topID.keySet());
	}

	public String getTopologyID(String desc) {
		return this.mDesc2topID.get(desc);
	}
}
