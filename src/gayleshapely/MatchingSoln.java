package gayleshapely;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MatchingSoln {
	
	HashMap<Student, School> studentToSchool = new HashMap<Student,School>();
	HashMap<School,Set<Student>> schoolToStudents = new HashMap<School,Set<Student>>();
	
	public MatchingSoln(Collection<School> allSchools) {
		for (School school : allSchools) {
			schoolToStudents.put(school, new HashSet<Student>());
		}
		schoolToStudents.put(School.getNullSchool(), new HashSet<Student>());
	}
	
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
	
	public School getSchoolForStudent(Student student) {
		return studentToSchool.get(student);
	}
	
	public Set<Student> getStudentsForSchool(School school) {
		return schoolToStudents.get(school);
	}

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
