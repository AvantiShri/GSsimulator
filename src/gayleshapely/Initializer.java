package gayleshapely;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import fileProcessing.MyFileReader;
import gayleshapely.Preferences.ItemRawRanks;
import gayleshapely.Preferences.ItemRawRank;

public class Initializer {
	String inputFile;
	
	String STUDENTS_TAG = "STUDENTS";
	String SCHOOLS_TAG = "SCHOOLS";
	String STUDENTS_RANK_SCHOOLS_TAG = "STUDENTS RANK SCHOOLS";
	String SCHOOLS_RANK_STUDENTS_TAG = "SCHOOLS RANK STUDENTS";
	
	AllStudents allStudents;
	AllSchools allSchools;
	
	public Initializer(String inputFile) {
		this.inputFile = inputFile;
	}
	
	public GayleShapely initialize() {
		GayleShapely gayleShapely = null;
		try {
			MyFileReader fileReader = new MyFileReader(new URL(inputFile));
			Map<String, ArrayList<String>> contentsByTag = MyFileReader.splitByTags(fileReader.readRemainingLines());
			parseStudents(contentsByTag.get(STUDENTS_TAG));
			parseSchools(contentsByTag.get(SCHOOLS_TAG));
			parseStudentsRankingSchools(contentsByTag.get(STUDENTS_RANK_SCHOOLS_TAG));
			parseSchoolsRankingStudents(contentsByTag.get(SCHOOLS_RANK_STUDENTS_TAG));
			
			gayleShapely = new GayleShapely(allStudents, allSchools);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return gayleShapely;
	}
	
	//List of student names
	void parseStudents(ArrayList<String> studentStrings) {
		if (studentStrings == null) {
			throw new RuntimeException("null contents for tag: "+STUDENTS_TAG);
		}
		allStudents = new AllStudents();
		for (String studentName : studentStrings) {
			Student.createStudent(studentName);
			allStudents.addStudent(Student.getStudent(studentName));
		}
	}
	
	//List of school name and capacity, separated by a tab
	void parseSchools(ArrayList<String> schoolStrings) {
		if (schoolStrings == null) {
			throw new RuntimeException("null contents for tag: "+SCHOOLS_TAG);
		}
		allSchools = new AllSchools();
		for (String schoolString : schoolStrings) {
			String[] nameAndCapacity = schoolString.split("\t");
			String name = nameAndCapacity[0];
			Integer capacity = Integer.parseInt(nameAndCapacity[1]);
			School.createSchool(name,capacity);
			allSchools.addSchool(School.getSchool(name));
		}
	}
	
	void parseStudentsRankingSchools(ArrayList<String> rawRankingStrings) {
		if (rawRankingStrings == null) {
			throw new RuntimeException("null contents for tag: "+STUDENTS_RANK_SCHOOLS_TAG);
		}
		String[] schoolNames = rawRankingStrings.get(0).split("\t");
		ArrayList<School> schoolOrdering = new ArrayList<School>();
		for (int i = 1; i < schoolNames.length; i++) { //first entry is not a school name but rather "student\school"
			School theSchool = School.getSchool(schoolNames[i]);
			if (allSchools.contains(theSchool) == false) {
				throw new RuntimeException("School "+theSchool+" not defined in set of schools");
			}
			schoolOrdering.add(theSchool);
		}
		for (String studentPrefsString : rawRankingStrings.subList(1, rawRankingStrings.size())) {
			String[] prefsArr = studentPrefsString.split("\t");
			Student student = Student.getStudent(prefsArr[0]); //first entry is the student name
			if (allStudents.contains(student) == false) {
				throw new RuntimeException("Student "+student+" not defined in set of students");
			}
			if (prefsArr.length != (allSchools.size() + 1)) {
				throw new RuntimeException("Prefs for student "+student+" has length "+(prefsArr.length-1)+" but "+allSchools.size()+" schools were defined");
			}
			
			ItemRawRanks<School> rawSchoolRanks = new ItemRawRanks<School>();
			for (int i = 1; i < prefsArr.length; i++) {
				ItemRawRank<School> rawSchoolRank = new ItemRawRank<School>(schoolOrdering.get(i-1),Integer.parseInt(prefsArr[i]));
				rawSchoolRanks.addItemRawRank(rawSchoolRank);
			}
			student.setSchoolPreferences(new Preferences<School>(rawSchoolRanks));			
		}
	}
	
	void parseSchoolsRankingStudents(ArrayList<String> rawRankingStrings) {
		if (rawRankingStrings == null) {
			throw new RuntimeException("Null contents for tag: "+this.SCHOOLS_RANK_STUDENTS_TAG);
		}
		String[] studentNames = rawRankingStrings.get(0).split("\t");
		ArrayList<Student> studentOrdering = new ArrayList<Student>();
		for (int i = 1; i < studentNames.length; i++) { //first entry is not a student name but rather "school\student"
			Student theStudent = Student.getStudent(studentNames[i]);
			if (allStudents.contains(theStudent) == false) {
				throw new RuntimeException("Student "+theStudent+" not defined in set of students");
			}
			studentOrdering.add(theStudent);
		}
		for (String schoolPrefsString : rawRankingStrings.subList(1, rawRankingStrings.size())) {
			String[] prefsArr = schoolPrefsString.split("\t");
			School school = School.getSchool(prefsArr[0]); //first entry is the school name
			if (allSchools.contains(school) == false) {
				throw new RuntimeException("School "+school+" not defined in set of schools");
			}
			if (prefsArr.length != (allStudents.size() + 1)) {
				throw new RuntimeException("Prefs for school "+school+" has length "+(prefsArr.length-1)+" but "+allStudents.size()+" students were defined");
			}
			
			ItemRawRanks<Student> rawStudentRanks = new ItemRawRanks<Student>();
			for (int i = 1; i < prefsArr.length; i++) {
				ItemRawRank<Student> rawStudentRank = new ItemRawRank<Student>(studentOrdering.get(i-1),Integer.parseInt(prefsArr[i]));
				rawStudentRanks.addItemRawRank(rawStudentRank);
			}
			school.setStudentPreferences(new Preferences<Student>(rawStudentRanks));			
		}
	}
	
	
}
