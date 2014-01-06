package gayleshapely;

import gayleshapely.Preferences.ItemRawRank;
import gayleshapely.Preferences.ItemRawRanks;

import java.util.ArrayList;
import java.util.Random;

public class PreferenceGenerator {

	AllStudents allStudents;
	AllSchools allSchools;
	String[] allStudentNames;
	String[] allSchoolNames;
	
	public PreferenceGenerator(){
		
	}
	
	/**
	 * Generates market in which students have uniform prefs over schools,
	 * schools have even capacities and uniform prefs over students.
	 * @returns GayleShapely instance to be used in GayleShapelyMonteCarlo
	 * @param numstudents, numschools
	 */
	public GayleShapely simulateUniformMarket(int numstudents, int numschools){
		GayleShapely gayleShapely = null;
		genStudents(numstudents);
		genUniformCapacitySchools(numstudents,numschools);
		genUniformStudentsRankingSchools(allStudentNames, allSchoolNames);
		genUniformSchoolsRankingStudents(allStudentNames, allSchoolNames);
		
		gayleShapely = new GayleShapely(allStudents, allSchools);
		return gayleShapely;
	}

	/**
	 * Generates market in which students have identical prefs over schools,
	 * schools have even capacities and uniform prefs over students.
	 * @returns GayleShapely instance to be used in GayleShapelyMonteCarlo
	 * @param numstudents, numschools
	 */
	public GayleShapely simulateIdenticalStudentMarket(int numstudents, int numschools){
		GayleShapely gayleShapely = null;
		genStudents(numstudents);
		genUniformCapacitySchools(numstudents,numschools);
		genIdenticalStudentsRankingSchools(allStudentNames, allSchoolNames);
		genUniformSchoolsRankingStudents(allStudentNames, allSchoolNames);
		
		gayleShapely = new GayleShapely(allStudents, allSchools);
		return gayleShapely;
	}
	
	/**
	 * Generates market in which students have identical prefs over schools,
	 * schools have even capacities.
	 * Schools have prefs: [1,2,3,..randind,a,b,c,d,e,..], uniform after randindex
	 * @returns GayleShapely instance to be used in GayleShapelyMonteCarlo
	 * @param numstudents, numschools
	 */
	public GayleShapely simulateSemiIdentSchoolMarket(int numstudents, int numschools, int randIndex){
		GayleShapely gayleShapely = null;
		genStudents(numstudents);
		genUniformCapacitySchools(numstudents,numschools);
		genUniformStudentsRankingSchools(allStudentNames, allSchoolNames);
		genSemiIdentSchoolsRankingStudents(allStudentNames, allSchoolNames, randIndex);
		
		gayleShapely = new GayleShapely(allStudents, allSchools);
		return gayleShapely;
	}

	
	/**
	 * Generates market in which students have identical prefs over schools,
	 * schools have even capacities;
	 * Schools have prefs: [1,x,x,x,y,z,a,b,c,d,e,..] in which 1 is identical, 
	 * there are numCopies copies of some random choice x, and a,b,c,.. are uniform over the rest.
	 * @returns GayleShapely instance to be used in GayleShapelyMonteCarlo
	 * @param numstudents, numschools, randIndex = index at which randomization starts, numcopies
	 */
	public GayleShapely simulateCopiedSchoolMarket(int numstudents, int numschools, int randIndex, int numcopies){
		GayleShapely gayleShapely = null;
		genStudents(numstudents);
		genUniformCapacitySchools(numstudents,numschools);
		genIdenticalStudentsRankingSchools(allStudentNames, allSchoolNames);
		genCopiedSchoolsRankingStudents(allStudentNames, allSchoolNames, randIndex, numcopies);
		
		gayleShapely = new GayleShapely(allStudents, allSchools);
		return gayleShapely;
	}
	
	
	void genStudents(int numstudents) {
		if (numstudents <1 ) {
			throw new RuntimeException("Need at least one student");
		}
		allStudents = new AllStudents();
		allStudentNames = new String[numstudents];
		for (int i=0; i < numstudents; i++) {
			String studentName = "student" + String.valueOf(i);
			Student.createStudent(studentName);
			allStudents.addStudent(Student.getStudent(studentName));
			allStudentNames[i] = studentName;
		}
	}
	
	void genSchools(int numschools, int[] capacityArr) {
		if (numschools < 1) {
			throw new RuntimeException("Need at least one school");
		}
		allSchools = new AllSchools();
		allSchoolNames = new String[numschools];
		for (int i=0; i < numschools; i++) {
			String schoolName = "school" +  String.valueOf(i);
			int schoolCap = capacityArr[i];
			School.createSchool(schoolName,schoolCap);
			allSchools.addSchool(School.getSchool(schoolName));
			allSchoolNames[i] = schoolName;
		}
	}
	
	void genUniformCapacitySchools(int numstudents,int numschools){
		int numSlots;
		int numExcess;
		if(numstudents>numschools){
			numSlots = (int) Math.floor(numstudents/numschools);
			numExcess = numstudents%numschools;
		}
		else{
			numSlots = 1;
			numExcess = 0;
		}
		int[] capArr = new int[numschools];
		for(int i=0; i<numschools;i++){
			if(i<numExcess){
				capArr[i]=numSlots+1;
			}
			else{
				capArr[i]=numSlots;
			}
		}
		genSchools(numschools, capArr);
	}
	
	/**
	 * Helper function for ranking generation functions.
	 * Sets particular rankings over schools for one student
	 * @param studentName, schoolNamesInRankOrder= array of school names in order of rank
	 */
	void genOneStudentsRankingSchools(String studentName, String[] schoolNamesInRankOrder) {

		Student student = Student.getStudent(studentName); 
		if (allStudents.contains(student) == false) {
			throw new RuntimeException("Student "+student+" not defined in set of students");
		}
		//Temporarily assume that all schools must be ranked (to relax):
		if (schoolNamesInRankOrder.length != (allSchools.size())) {
			throw new RuntimeException("Prefs for student "+student+" has length "+(schoolNamesInRankOrder.length)+" but "+allSchools.size()+" schools were defined");
		}
		
		ItemRawRanks<School> rawSchoolRanks = new ItemRawRanks<School>();
		for (int i = 0; i < schoolNamesInRankOrder.length; i++) {
			School theSchool = School.getSchool(schoolNamesInRankOrder[i]);
			if (allSchools.contains(theSchool) == false) {
				throw new RuntimeException("School "+theSchool+" not defined in set of schools");
			}
			
			ItemRawRank<School> rawSchoolRank = new ItemRawRank<School>(theSchool,i);
			rawSchoolRanks.addItemRawRank(rawSchoolRank);
		}
		student.setSchoolPreferences(new Preferences<School>(rawSchoolRanks));			
	}
	
	/**
	 * Sets uniform rankings over all schools for all students
	 * @param studentNames, schoolNames
	 */
	void genUniformStudentsRankingSchools(String[] studentNames, String[] schoolNames) {
		for(String studentName :studentNames){
			String[] randomPrefs = shuffleStrArray(schoolNames);
			genOneStudentsRankingSchools(studentName, randomPrefs);
		}
	}
	
	/**
	 * Sets identical rankings over all schools for all students
	 * @param studentNames, schoolNames
	 */
	void genIdenticalStudentsRankingSchools(String[] studentNames, String[] schoolNames) {
		for(String studentName :studentNames){
//			String[] randomPrefs = shuffleStrArray(schoolNames);
			genOneStudentsRankingSchools(studentName, schoolNames);
		}
	}
	
	/**
	 * Helper function for ranking generation functions.
	 * Sets particular rankings over students for one school
	 * @param schoolName, studentNamesInRankOrder= array of student names in order of rank
	 */
	void genOneSchoolsRankingStudents(String schoolName, String[] studentNamesInRankOrder) {
			School school = School.getSchool(schoolName); //first entry is the school name
			if (allSchools.contains(school) == false) {
				throw new RuntimeException("School "+school+" not defined in set of schools");
			}
			//Assume all students must be ranked - fairly reasonable assumption that we can keep.
			if (studentNamesInRankOrder.length != (allStudents.size())) {
				throw new RuntimeException("Prefs for school "+school+" has length "+(studentNamesInRankOrder.length)+" but "+allStudents.size()+" students were defined");
			}
			
			ItemRawRanks<Student> rawStudentRanks = new ItemRawRanks<Student>();
			for (int i = 0; i < studentNamesInRankOrder.length; i++) {
				Student theStudent = Student.getStudent(studentNamesInRankOrder[i]);
				if (allStudents.contains(theStudent) == false) {
					throw new RuntimeException("Student "+theStudent+" not defined in set of students");
				}
				ItemRawRank<Student> rawStudentRank = new ItemRawRank<Student>(theStudent,i);
				rawStudentRanks.addItemRawRank(rawStudentRank);
			}
			school.setStudentPreferences(new Preferences<Student>(rawStudentRanks));			
		}
	
	
	/**
	 * Sets uniform rankings over all students for all schools
	 * @param studentNames, schoolNames
	 */
	void genUniformSchoolsRankingStudents(String[] studentNames, String[] schoolNames) {
		for(String schoolName : schoolNames){
			String[] randomPriorities = shuffleStrArray(studentNames);
			genOneSchoolsRankingStudents(schoolName, randomPriorities);
		}
	}	

	
	
	/**
	 * Helper function to deal with missing student names
	 * @param studentName, schoolName, lastPlaceIndex in order to rank missing vals last
	 * Note: It's ok to force schools to rank all students but not necessarily vice versa
	 */
	ItemRawRank<Student> setEmptySchoolOnStudentRanking(String studentName, String schoolName, int lastPlace){
//		School school = School.getSchool(schoolName);
		Student theStudent = Student.getStudent(studentName);
		ItemRawRank<Student> rawStudentRank = new ItemRawRank<Student>(theStudent,lastPlace);
		return rawStudentRank;
	}
	
	/**
	 * Sets semi-copied/semi-uniform rankings over all students for one student as described above
	 * @param schoolName, studentNamesInRankOrder, missingStudentNames = array of students not ranked due to copying
	 */
	void genCopiedOneSchoolsRankingStudents(String schoolName, String[] studentNamesInRankOrder, String[] missingStudentNames) {
		School school = School.getSchool(schoolName); //first entry is the school name
		if (allSchools.contains(school) == false) {
			throw new RuntimeException("School "+school+" not defined in set of schools");
		}
		//Assume all students must be ranked - fairly reasonable assumption that we can keep.
		if (studentNamesInRankOrder.length != (allStudents.size())) {
			throw new RuntimeException("Prefs for school "+school+" has length "+(studentNamesInRankOrder.length)+" but "+allStudents.size()+" students were defined");
		}
		
		ItemRawRanks<Student> rawStudentRanks = new ItemRawRanks<Student>();
		for (int i = 0; i < studentNamesInRankOrder.length; i++) {
			Student theStudent = Student.getStudent(studentNamesInRankOrder[i]);
			if (allStudents.contains(theStudent) == false) {
				throw new RuntimeException("Student "+theStudent+" not defined in set of students");
			}
			ItemRawRank<Student> rawStudentRank = new ItemRawRank<Student>(theStudent,i);
			rawStudentRanks.addItemRawRank(rawStudentRank);
		}
		for(String missingStudentName:missingStudentNames){
//			Student missingStudent = Student.getStudent(missingStudentName);
			ItemRawRank<Student> rawStudentRank = setEmptySchoolOnStudentRanking(missingStudentName,schoolName,studentNamesInRankOrder.length-1);
			rawStudentRanks.addItemRawRank(rawStudentRank);
		}
		school.setStudentPreferences(new Preferences<Student>(rawStudentRanks));			
	}

	
	
	/**
	 * Sets semi-identical rankings over students for all schools as detailed above
	 * @param studentNames, schoolNames, randIndex
	 */
	void genSemiIdentSchoolsRankingStudents(String[] studentNames, String[] schoolNames, int randIndex) {
		for(String schoolName : schoolNames){
			String [] subPriorities = new String[studentNames.length - randIndex];
			for(int j = randIndex; j < studentNames.length; j++){
				subPriorities[j-randIndex] = studentNames[j];
			}
			String[] randomSubPriorities = shuffleStrArray(subPriorities);
			String[] priorities = new String[studentNames.length ];
			for(int j = 0; j < studentNames.length; j++){
				if(j<randIndex){
					priorities[j] = studentNames[j];
				}
				else{
					priorities[j] = randomSubPriorities[j-randIndex];
				}	
			}
			genOneSchoolsRankingStudents(schoolName, priorities);
		}
	}	
	
	/**
	 * Sets semi-copied rankings over students for all schools as detailed above
	 * @param studentNames, schoolNames, randIndex, numcopies
	 */
	void genCopiedSchoolsRankingStudents(String[] studentNames, String[] schoolNames, int randIndex,int numcopies) {
		
		for(String schoolName : schoolNames){
			Random random = new Random();
			String [] subPriorities = new String[studentNames.length - randIndex];
			for(int j = randIndex; j < studentNames.length; j++){
				subPriorities[j-randIndex] = studentNames[j];
			}
			String[] randomSubPriorities = shuffleStrArray(subPriorities);
			String[] priorities = new String[studentNames.length ];
			String[] missingStudNames = new String[numcopies-1];
			int dumCopyIndex = random.nextInt(studentNames.length - randIndex - 1);
			int copyIndex = dumCopyIndex+1;
			int missingCounter=0;
			for(int j = 0; j < studentNames.length; j++){
				if(j<randIndex){
					if((j>0) && (j<=numcopies)){
						priorities[j] = studentNames[copyIndex];
						if(j != copyIndex){
							missingStudNames[missingCounter]=studentNames[j];
							missingCounter++;
						}
					}
					else{
						priorities[j] = studentNames[j];
					}
				}
				else{
					priorities[j] = randomSubPriorities[j-randIndex];
				}	
			}
			genCopiedOneSchoolsRankingStudents(schoolName, priorities,missingStudNames);
		}
	}	
	
	public static int[] shuffleArray(int[] array)
	{
	    int index, temp;
	    Random random = new Random();
	    for (int i = array.length - 1; i > 0; i--)
	    {
	        index = random.nextInt(i + 1);
	        temp = array[index];
	        array[index] = array[i];
	        array[i] = temp;
	    }
	    return array;
	}
	
	String[] shuffleStrArray(String[] inputarr){
		int index;
		String temp;
		Random random = new Random();
		for(int i=inputarr.length-1; i>0 ; i--){
	        index = random.nextInt(i + 1);
	        temp = inputarr[index];
	        inputarr[index] = inputarr[i];
	        inputarr[i] = temp;
		}
		return inputarr;
	}
	
		
	
}
