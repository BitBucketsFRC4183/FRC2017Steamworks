package org.usfirst.frc.team4183.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * Use this class for communication between Commands using named event flags
 * @author twilson
 */
public class Flags {

	private static Set<String> setOfFlags = new HashSet<>();

	/**
	 * Set a flag. If flag was already set, then it'll still be set.
	 * @param key The name of the flag
	 */
	public static void set( String key) {
		setOfFlags.add(key);			
	}

	/**
	 * Clear a flag. If flag was not set, nothing happens.
	 * @param key The name of the flag
	 */
	public void clear( String key) {
		test(key);
	}

	/**
	 * Test a flag, and clear if set (i.e. returns true only ONCE for each set()).
	 * @param key The name of the flag
	 * @return True if flag was set, false if not.
	 */
	public static boolean test( String key) {		
		return setOfFlags.remove(key);
	}

	/**
	 * Test a flag, don't clear if was set.
	 * @param key The name of the flag
	 * @return True if flag was set, false if not.
	 */	
	public static boolean peek( String key) {
		return setOfFlags.contains(key);
	}
}
