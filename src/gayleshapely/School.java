package gayleshapely;

import java.util.HashMap;

/**
 * Uses the factory pattern; the constructor is private.
 * @author avantis
 */
public class School {
	//class related stuff here...
	static HashMap<String, School> schoolFactoryLookup = new HashMap<String,School>();
	/**
	 * Creates a school with name school name, throws exception if already exists
	 * @param schoolName
	 */
	static void createSchool(String schoolName) {
		if (schoolFactoryLookup.containsKey(schoolName)) {
			throw new RuntimeException("A school with name "+schoolName+" already exists");
		} else {
			schoolFactoryLookup.put(schoolName, new School(schoolName));
		}
	}
	
	/**
	 * Pulls precreated school with name schoolName, throws exception if does not already exist
	 * @param schoolName
	 * @return
	 */
	static School getSchool(String schoolName) {
		if (schoolFactoryLookup.containsKey(schoolName)) {
			return schoolFactoryLookup.get(schoolName);
		} else {
			throw new RuntimeException("School with name "+schoolName+" was not created");
		}
	}
	
	//instance-related stuff starts here...	
	String schoolName;	
	Preferences<Student> studentPreferences;
	private School(String schoolName) {
		this.schoolName = schoolName;
	}
}
