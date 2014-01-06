package gayleshapely;

import org.junit.Test;

public class PreferenceTester {

	@Test
	public void uniformTest() {
		int numstudents = 3;
		int numschools = 3;
		PreferenceGenerator generator = new PreferenceGenerator();
		GayleShapely gayleShapely = generator.simulateUniformMarket(numstudents,numschools);
		System.out.println("STUDENTS: \n");
		for (Student student : gayleShapely.allStudents) {
			System.out.println("Name: "+student.studentName);
			System.out.println("Prefs: "+student.schoolPreferences);
			System.out.println("");
		}
		System.out.println("SCHOOLS: \n");
		for (School school : gayleShapely.allSchools) {
			System.out.println("Name: "+school.schoolName);
			System.out.println("Prefs: "+school.studentPreferences);
			System.out.println("");
		}
		
		GayleShapelyMonteCarlo gsmc = new GayleShapelyMonteCarlo(gayleShapely);
		gsmc.run(1000);
		System.out.println(gsmc);
		
	}
	
	@Test
	public void CopiedSchoolTest() {
		int numstudents = 5;
		int numschools = 4;
		PreferenceGenerator generator = new PreferenceGenerator();
//		GayleShapely gayleShapely = generator.simulateCopiedSchoolMarket(numstudents,numschools,3,2);
		GayleShapely gayleShapely = generator.simulateSemiIdentSchoolMarket(numstudents,numschools,3);
		
		System.out.println("STUDENTS: \n");
		for (Student student : gayleShapely.allStudents) {
			System.out.println("Name: "+student.studentName);
			System.out.println("Prefs: "+student.schoolPreferences);
			System.out.println("");
		}
		System.out.println("SCHOOLS: \n");
		for (School school : gayleShapely.allSchools) {
			System.out.println("Name: "+school.schoolName);
			System.out.println("Prefs: "+school.studentPreferences);
			System.out.println("");
		}
		
		GayleShapelyMonteCarlo gsmc = new GayleShapelyMonteCarlo(gayleShapely);
		gsmc.run(1000);
		System.out.println(gsmc);
		
	}
	

	
}
