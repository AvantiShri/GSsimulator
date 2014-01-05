package gayleshapely;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MatchingSolnMonteCarlo {
	
	HashMap<Student,SchoolCounts> studentToSchoolCounts = new HashMap<Student,SchoolCounts>();
	
	public MatchingSolnMonteCarlo(Collection<Student> students, Collection<School> schools) {
		for (Student student : students) {
			studentToSchoolCounts.put(student, new SchoolCounts(schools));
		}
	}
	
	public HashMap<Student, SchoolCounts> getStudentToSchoolCounts(){
		return studentToSchoolCounts;
	}
	
	/**
	 * Adds the result of matchingSoln to the current running counts.
	 * @param matchingSoln
	 */
	public void incorporateMatchingSolution(MatchingSoln matchingSoln) {
		for (Student student : matchingSoln.studentToSchool.keySet()) {
			studentToSchoolCounts.get(student).incrementCount(matchingSoln.studentToSchool.get(student));
		}
	}
	
	/**
	 * Gets the fraction of the time student is assigned to school.
	 * @param student
	 * @param school
	 * @return
	 */
	public Double getFraction(Student student, School school) {
		if (studentToSchoolCounts.containsKey(student) == false) {
			throw new RuntimeException("Student "+student+" not defined in the initial set of students");
		}
		return studentToSchoolCounts.get(student).getFraction(school);
	}
	
	@Override
	public String toString() {
		return toString(null,null);
	}
	
	public String toString(List<Student> students, List<School> schools) {
		StringBuilder sb = new StringBuilder();
		if (students == null) {
			students = new ArrayList<Student>();
			students.addAll(studentToSchoolCounts.keySet());
			Collections.sort(students);
		}
		if (schools == null) {
			schools = new ArrayList<School>();
			schools.addAll(studentToSchoolCounts.get(students.iterator().next()).schoolCounts.keySet());
			Collections.sort(schools);
		}
		sb.append("student\\school");
		for (School school : schools) {
			sb.append("\t"+school.schoolName);
		}
		sb.append("\n");
		for (Student student : students) {
			sb.append(student.studentName);
			for (School school : schools) {
				sb.append("\t"+getFraction(student,school));
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	/**
	 * Class is intended to keep track of the number of times a given student is assigned to various schools
	 * @author avantis
	 */
	public static class SchoolCounts {
		HashMap<School,Integer> schoolCounts = new HashMap<School,Integer>();
		HashMap<School,Double> normalizedCounts = null;
		boolean normalizationUpToDate = false;
		public SchoolCounts(Collection<School> schools) {
			for (School aSchool : schools) {
				schoolCounts.put(aSchool, 0);
			}			
			//also include the null school
			schoolCounts.put(School.getNullSchool(), 0);
		}
		
		void incrementCount(School school, Integer amt) {
			if (school == null) {
				school = School.getNullSchool();
			}
			normalizationUpToDate = false;
			if (schoolCounts.containsKey(school) == false) {
				throw new RuntimeException("SchoolCounts was not initialized with "+school.schoolName);
			}
			schoolCounts.put(school, schoolCounts.get(school)+amt);
		}
		
		/**
		 * Increments the count for a particular school by 1
		 * @param school
		 */
		public void incrementCount(School school) {
			incrementCount(school, 1);
		}
		
		Integer getCount(School school) {
			return schoolCounts.get(school);
		}
		
		/**
		 * Gets the fraction of counts for a particular school. In practice, this is the fraction
		 * of the time a particular student is assigned to the school.
		 * @param school
		 * @return
		 */
		public Double getFraction(School school) {
			if (!normalizationUpToDate) {
				normalize();
			}
			if (normalizedCounts.containsKey(school) == false) {
				throw new RuntimeException("SchoolCounts was not initialized with "+school.schoolName);
			}
			return normalizedCounts.get(school);
		}
		
		void normalize() {
			Double total = 0.0;
			for (Integer count : schoolCounts.values()) {
				total += count;
			}
			normalizedCounts = new HashMap<School,Double>();
			for (School school : schoolCounts.keySet()) {
				normalizedCounts.put(school, getCount(school)/total);
			}
			normalizationUpToDate = true;
		}
		
	}
	
}
