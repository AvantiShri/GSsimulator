package gayleshapely;

/**
 * Class that finds a GS solution to a particular matching problem.
 * @author avantis
 */
public class GayleShapely {
	AllStudents allStudents;
	AllSchools allSchools;
	Boolean done = false;
	
	public GayleShapely(AllStudents allStudents, AllSchools allSchools) {
		this.allStudents = allStudents;
		this.allSchools = allSchools;
	}
	
	public MatchingSoln iterateTillConvergence_randomProposalOrders() {
		MatchingSoln soln = new MatchingSoln(allSchools.schoolSet);
		while (!done) {
			allStudents.randomizeProposalOrder();
			iterate();
		}
		for (Student student : allStudents) {
			soln.addStudentToSchool(student, student.engagedTo);
		}
		
		return soln;
	}
	
	void iterate() {
		boolean done = true;
		for (Student aStudent : allStudents) {
			aStudent.proposeIfNecessary();
		}
		for (Student aStudent : allStudents) {
			done &= aStudent.isDoneProposingForNow();
		}
		this.done = done;
	}
	
	/**
	 * Resets everything so you can run Gayle-Shapely again
	 */
	public void reset() {
		for (Student student : allStudents) {
			student.reset();
		}
		for (School school : allSchools) {
			school.reset();
		}
		done = false;
	}
}
