package gayleshapely;


import org.junit.Test;

public class GayleShapelyTest {

	@Test
	public void test() {
		Initializer initializer = new Initializer("src/resources/test1.tsv");
		GayleShapely gayleShapely = initializer.initialize();
		GayleShapelyMonteCarlo gsmc = new GayleShapelyMonteCarlo(gayleShapely);
		gsmc.run(1000);
		System.out.println(gsmc);
		
	}

}
