package gayleshapely;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Class that specifies the mapping from student to school and school to student
 * in the final solution. Uses the "Null school" to
 * store the set of students not assigned to any school.
 * @author avantis
 */
public class MatchingSoln {
	
	HashMap<Student, School> studentToSchool = new HashMap<Student,School>();
	HashMap<School,Set<Student>> schoolToStudents = new HashMap<School,Set<Student>>();
	
	public MatchingSoln(Collection<School> allSchools) {
		for (School school : allSchools) {
			schoolToStudents.put(school, new HashSet<Student>());
		}
		schoolToStudents.put(School.getNullSchool(), new HashSet<Student>());
	}
	
	/**
	 * Adds a student to the set of students attending a particular school in the solution
	 * @param student
	 * @param school
	 */
	public void addStudentToSchool(Student student, School school) {
		studentToSchool.put(student, school);
		if (school != null) {
			if (schoolToStudents.containsKey(school) == false) {
				throw new RuntimeException("Student "+student+" assigned to school "+school+" which is not in the set of all schools");
			}
			schoolToStudents.get(school).add(student);
		} else {
			schoolToStudents.get(School.getNullSchool()).add(student);
		}
	}
	
	/**
	 * Returns the school a particular student attends - returns null if student is not assigned to a school
	 * @param student
	 * @return
	 */
	public School getSchoolForStudent(Student student) {
		return studentToSchool.get(student);
	}
	
	/**
	 * Returns the set of students attending a particular school. If pass in
	 * the nullSchool, will get the set of students not assigned to any school.
	 * @param school
	 * @return
	 */
	public Set<Student> getStudentsForSchool(School school) {
		return schoolToStudents.get(school);
	}
	
	/**
	 * Returns true if a student attends a particular school, false otherwise.
	 * Here, null is used to test the case where a student is not assigned to a school.
	 * @param student
	 * @param school
	 * @return
	 */
	public boolean attendsSchool(Student student, School school) {
		if (studentToSchool.get(student) == school) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Student student : studentToSchool.keySet()) {
			sb.append(student.studentName+" - "+studentToSchool.get(student).schoolName+"\n");
		}
		return sb.toString();
	}
}
