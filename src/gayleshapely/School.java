package gayleshapely;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Uses the factory pattern; the constructor is private.
 * @author avantis
 */
public class School implements Comparable<School>{
	//class related stuff here...
	static HashMap<String, School> schoolFactoryLookup = new HashMap<String,School>();	
	static String NULLSCHOOLNAME = "nullSchool";
	static {
		schoolFactoryLookup.put(NULLSCHOOLNAME, new School(NULLSCHOOLNAME,1)); //create the null school		
	}
	
	/**
	 * Creates a school with name school name
	 * @param schoolName
	 */
	public static void createSchool(String schoolName, Integer capacity) {
		schoolFactoryLookup.put(schoolName, new School(schoolName, capacity));
	}
	
	/**
	 * @return a school representing the 'null school'
	 */
	static School getNullSchool() {
		return getSchool(NULLSCHOOLNAME);
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
	final String schoolName;	
	Preferences<Student> studentPreferences;
	Integer maxCapacity;
	PriorityQueue<Student> acceptedStudents;
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
	
	public void setStudentPreferences(Preferences<Student> studentPreferences) {
		this.studentPreferences = studentPreferences;
	}
	
	/**
	 * If the school has room, the student is accepted.
	 * If the student is preferred to the least preferred student, the student is accepted, and the least
	 * preferred student is jilted.
	 * Otherwise, nothing happens.
	 * @param student
	 */
	public void processApplication(Student student) {
		if (this.studentPreferences == null) {
			throw new RuntimeException("Student preferences is null for school "+this);
		}
		
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
	}
	
	public Integer remainingCapacity() {
		return this.maxCapacity - this.acceptedStudents.size();
	}
	
	//returns the least liked student
	Student leastPreferred() {
		return acceptedStudents.peek();
	}
	
	//kicks out the least liked student
	void jilt() {
		Student dislikedStudent = acceptedStudents.poll();
		dislikedStudent.jilt();
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
	
	/**
	 * Resets the school so we can run Gayle-Shapely again
	 */
	public void reset() {
		acceptedStudents = new PriorityQueue<Student>(maxCapacity, studentPreferenceComparator);
	}
	
	@Override
	public String toString() {
		return this.schoolName;
	}

	/**
	 * The null school is always sorted last. Otherwise, sorted alphabetically.
	 */
	@Override
	public int compareTo(School arg0) {
		if (this==arg0) {
			return 0;
		}
		if (this==School.getNullSchool()) {
			return 1;
		}
		if (arg0 == School.getNullSchool()) {
			return -1;
		}
		return this.schoolName.compareTo(arg0.schoolName);
	}
}
