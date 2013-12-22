package gayleshapely;


import org.junit.Test;

public class GayleShapelyTest {

	@Test
	public void test() {
		Initializer initializer = new Initializer("src/resources/test1.tsv");
		GayleShapely gayleShapely = initializer.initialize();
		MatchingSoln soln = gayleShapely.iterateTillConvergence_randomProposalOrders();
		System.out.println(soln);
	}

}
