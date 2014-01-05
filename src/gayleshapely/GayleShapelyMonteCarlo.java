package gayleshapely;

import gayleshapely.MatchingSolnMonteCarlo.SchoolCounts;

import java.util.HashMap;

/**
 * Runs a GayleShapely multiple times, and tallies up the results
 * @author avantis
 */
public class GayleShapelyMonteCarlo {
	
	GayleShapely gayleShapely;
	MatchingSolnMonteCarlo matchingSolnMonteCarlo;
	int totalIterations = 0;
	public GayleShapelyMonteCarlo(GayleShapely gayleShapely) {
		this.gayleShapely = gayleShapely;
		this.matchingSolnMonteCarlo = new MatchingSolnMonteCarlo(gayleShapely.allStudents.studentSet, gayleShapely.allSchools.schoolSet);		
	}
	
	/**
	 * Runs gayleShapely for numIterations and tallies up the results.
	 * @param numIterations
	 */
	public void run(int numIterations) {
		for (int i = 1; i <= numIterations; i++) {
			totalIterations++;
			gayleShapely.reset();
			MatchingSoln soln = gayleShapely.iterateTillConvergence_randomProposalOrders();
			matchingSolnMonteCarlo.incorporateMatchingSolution(soln);
		}
	}
	
	/**
	 * Runs gayleShapely for numIterations and tallies up the results.
	 * @returns hashmap of fraction of times that each student is matched to each school
	 * @param numIterations
	 */
	public HashMap<Student, SchoolCounts> simulate(int numIterations) {
		for (int i = 1; i <= numIterations; i++) {
			totalIterations++;
			gayleShapely.reset();
			MatchingSoln soln = gayleShapely.iterateTillConvergence_randomProposalOrders();
			matchingSolnMonteCarlo.incorporateMatchingSolution(soln);
		}
		return matchingSolnMonteCarlo.getStudentToSchoolCounts();
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Number of iterations: "+totalIterations+"\n");
		sb.append(matchingSolnMonteCarlo.toString());
		return sb.toString();
	}
}
