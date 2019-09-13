package org.epilogtool.services;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.epilogtool.core.topology.RollOver;
import org.epilogtool.core.topology.Topology;

/**
 * List available "services". To manage topologies at
 * org.epilogtool.core.topology.*
 * 
 * @author Pedro T. Monteiro
 */
public class TopologyService {

	public static final String FILTER_DOT = "org.epilogtool.core.topology.Topology";
	public static final String FILTER_SLH = "org/epilogtool/core/topology/Topology";
	private final String CLASS = ".class";
	private final static String SEP = "/";

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
		
		URL resource = ClassLoader.getSystemResource(Topology.class.getName().replace(".", SEP) + this.CLASS);
		try {
			String basedir = URLDecoder.decode(resource.getFile(), "UTF-8");
			if (basedir.contains(".jar!")) {
				// either a "jar:file:" or a "file:"
				// the new File() already translates internal / to \ in Windows
				JarFile jarf = new JarFile(new File(basedir.substring(basedir.indexOf("file:") + 5, basedir.indexOf("!/"))));
				Enumeration<JarEntry> jeIter = jarf.entries();
				while (jeIter.hasMoreElements()) {
					String filename = jeIter.nextElement().getName();
					if (filename.endsWith(this.CLASS) && filename.contains(FILTER_SLH)) {
						String className = filename.replace(SEP, ".");
						className = className.substring(0, className.length() - this.CLASS.length());
						this.addTopology(cLoader, className);
					}
				}
				jarf.close();
			} else { // it's just a directory with all .class files
				basedir = basedir.substring(basedir.indexOf(File.separator), basedir.lastIndexOf(File.separator));
				File fdir = new File(basedir);
				for (File file : fdir.listFiles()) {
					String name = file.toString();
					if (name.contains(FILTER_SLH)) {
						String className = FILTER_DOT + name.substring(name.indexOf(FILTER_SLH) + FILTER_SLH.length(),
								name.length() - this.CLASS.length());
						this.addTopology(cLoader, className);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addTopology(ClassLoader cLoader, String className) {
		try {
			@SuppressWarnings("unchecked")
			Class<Topology> cTop = (Class<Topology>) cLoader.loadClass(className);
			if (!Modifier.isAbstract(cTop.getModifiers())) {
				Constructor<Topology> c = cTop.getConstructor(Integer.TYPE, Integer.TYPE, RollOver.class);
				Topology instance = c.newInstance(0, 0, RollOver.NONE);
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

	public Topology getNewTopology(String topologyID, int gridX, int gridY, RollOver rollover)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
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
