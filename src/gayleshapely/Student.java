package gayleshapely;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Uses the factory pattern; the constructor is private.
 * @author avantis
 */
public class Student implements Comparable<Student>{
	
	//class related stuff here...
	static HashMap<String, Student> studentFactoryLookup = new HashMap<String,Student>();
	/**
	 * Creates a student with name student name
	 * @param studentName
	 */
	static void createStudent(String studentName) {
		studentFactoryLookup.put(studentName, new Student(studentName));
	}
	
	/**
	 * Pulls precreated student with name studentName, throws exception if does not already exist
	 * @param studentName
	 * @return
	 */
	static Student getStudent(String studentName) {
		if (studentFactoryLookup.containsKey(studentName)) {
			return studentFactoryLookup.get(studentName);
		} else {
			throw new RuntimeException("Student with name "+studentName+" was not created");
		}
	}
	
	//instance-related stuff starts here...	
	final String studentName;	
	Preferences<School> schoolPreferences;
	ArrayList<School> currentRankSchoolSet;
	int currentRankSchoolSetIndex;
	boolean noMoreSchools = false;
	School engagedTo;
	private Student(String studentName) {
		this.studentName = studentName;
	}
	
	public void setSchoolPreferences(Preferences<School> schoolPreferences) {
		this.schoolPreferences = schoolPreferences;
	}
	
	/**
	 * If the student is not engaged to a school, and there's schools left on the student's
	 * preferences list, initiates proposal sequence
	 */
	public void proposeIfNecessary() {
		if (engagedTo == null) {
			if (!noMoreSchools) {
				School nextSchoolToProposeTo = getNextSchoolToProposeTo();
				if (nextSchoolToProposeTo != null) {
					nextSchoolToProposeTo.processApplication(this);
				} else {
					noMoreSchools = true;
				}
			}
		}
	}
	
	
	/**
	 * @return true if student is engaged to a school or there are no more schools to propose to.
	 */
	public boolean isDoneProposingForNow() {
		if (engagedTo != null || noMoreSchools) {
			return true;
		} else {
			return false;
		}
	}
	
	//returns null if we are at the bottom of the preferences list
	//if there are ties, jumbles up the order
	School getNextSchoolToProposeTo() {
		if (schoolPreferences == null) {
			throw new RuntimeException("School preferences for student "+this+" is null...");
		}
		//if this is the first round of proposals
		if (currentRankSchoolSet == null) {
			currentRankSchoolSet = schoolPreferences.atRank(0); //get the top ranked schools
			if (currentRankSchoolSet.size() > 1) {
				currentRankSchoolSet = Utility.randomise(currentRankSchoolSet);
			}
			currentRankSchoolSetIndex = 0;
		} else {
			if (currentRankSchoolSetIndex < (currentRankSchoolSet.size()-1)) {
				++currentRankSchoolSetIndex;
			} else {				
				currentRankSchoolSet = schoolPreferences.nextPreferred(currentRankSchoolSet.get(currentRankSchoolSetIndex));
				if (currentRankSchoolSet == null) {
					return null;
				}
				if (currentRankSchoolSet.size() > 1) {
					currentRankSchoolSet = Utility.randomise(currentRankSchoolSet);
				}
				currentRankSchoolSetIndex = 0;
			}
		}
		return currentRankSchoolSet.get(currentRankSchoolSetIndex);
	}
	
	public void jilt() {
		this.engagedTo = null;
	}
	
	public void getEngagedTo(School school) {
		this.engagedTo = school;
	}
	
	/**
	 * resets the student so that we can run Gayle-Shapely again
	 */
	public void reset() {
		currentRankSchoolSet = null;
		currentRankSchoolSetIndex = 0;
		noMoreSchools = false;
		engagedTo = null;
	}
	
	@Override
	public String toString() {
		return studentName;
	}

	@Override
	public int compareTo(Student arg0) {
		return this.studentName.compareTo(arg0.studentName);
	}
	
}
