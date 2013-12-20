package gayleshapely;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

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
	static void createSchool(String schoolName, Integer capacity) {
		if (schoolFactoryLookup.containsKey(schoolName)) {
			throw new RuntimeException("A school with name "+schoolName+" already exists");
		} else {
			schoolFactoryLookup.put(schoolName, new School(schoolName, capacity));
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
	Integer maxCapacity;
	PriorityQueue<Student> acceptedStudents;
	Boolean full;
	StudentComparator studentPreferenceComparator;
	private School(String schoolName, Integer maxCapacity) {
		this.schoolName = schoolName;
		this.maxCapacity = maxCapacity;
		if (maxCapacity == 0) {
			throw new RuntimeException("Max capacity should not be 0");
		}
		this.studentPreferenceComparator = new StudentComparator();
		this.acceptedStudents = new PriorityQueue<Student>(maxCapacity, studentPreferenceComparator);
	}
	
	/**
	 * If the school has room, the student is accepted.
	 * If the student is preferred to the least preferred student, the student is accepted, and the least
	 * preferred student is jilted.
	 * Otherwise, nothing happens.
	 * @param student
	 */
	public void processApplication(Student student) {
		if (remainingCapacity() > 0) {
			accept(student);
		} else {
			//a negative number is returned from the comparison if the leastPreferred() student
			//is less preferable to the current student.
			if (studentPreferenceComparator.compare(leastPreferred(), student) < 0) {
				jilt(); //eject the unwanted student
				accept(student);
			}
		}
	}
	
	void accept(Student student) {
		this.acceptedStudents.add(student);
		student.getEngagedTo(this);
		if (remainingCapacity() == 0) {
			full = true;
		}
	}
	
	public Integer remainingCapacity() {
		return this.maxCapacity - this.acceptedStudents.size();
	}
	
	Student leastPreferred() {
		return acceptedStudents.peek();
	}
	
	void jilt() {
		Student dislikedStudent = acceptedStudents.poll();
		dislikedStudent.jilt();
	}
	
	public Boolean isFull() {
		return full;
	}
	
	class StudentComparator implements Comparator<Student> {		
		/**
		 * If a student has a lower rank in student preferences, want to give that student a higher
		 * priority in the priority queue so they will be evicted sooner
		 */
		@Override
		public int compare(Student arg0, Student arg1) {
			return -1*studentPreferences.rankOf(arg0).compareTo(studentPreferences.rankOf(arg1));
		}		
	}
	
	
	@Override
	public String toString() {
		return this.schoolName;
	}
}
