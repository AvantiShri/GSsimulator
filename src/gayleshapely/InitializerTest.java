package gayleshapely;
import org.junit.Test;

public class InitializerTest {

	@Test
	public void test() {
		Initializer initializer = new Initializer("src/resources/test1.tsv");
		GayleShapely gayleShapely = initializer.initialize();
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
		
	}

}
