package org.epilogtool.common;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Retrieve text from resource bundles: define GUI text outside of the code.
 * 
 * NOTE: EpiLog will push at least one bundle, an extension can push its own
 * bundle(s) with the <tt>push</tt> method.
 */
public class Txt {

	private static List<ResourceBundle> bundles = new ArrayList<ResourceBundle>();

	/**
	 * The main method: retrieve the text for a given key
	 *
	 * @param key
	 * @return the text to insert (or the key if not found)
	 */
	public static String get(String key) {
		for (ResourceBundle bundle : bundles) {
			if (bundle.containsKey(key))
				return bundle.getString(key);
		}
		return key;
	}

	/**
	 * Retrieve the text for a given key, and replace marks by the provided
	 * parameters.
	 *
	 * @param key
	 * @param params
	 * @return
	 */
	public static String t(String key, String... params) {
		String result = get(key);

		for (String param : params) {
			result = result.replaceFirst("%s", param);
		}

		return result;
	}

	/**
	 * Retrieve a group of strings at once.
	 *
	 * @param keys
	 * @return
	 */
	public static String[] getStrings(String[] keys) {
		String[] ret = new String[keys.length];
		for (int i = 0; i < keys.length; i++) {
			ret[i] = get(keys[i]);
		}

		return ret;
	}

	/**
	 * Add a bundle to the text retriever.
	 *
	 * @param name
	 */
	public static void push(String name) {
		ResourceBundle bundle = ResourceBundle.getBundle(name);
		if (bundle == null) {
//			LogManager.error("Bundle not found: " + name);
			return;
		}
		bundles.add(bundle);
	}
}