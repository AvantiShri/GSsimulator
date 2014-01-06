package gayleshapely;


import org.junit.Test;

public class GayleShapelyTest {

	@Test
	public void test1() {
		Initializer initializer = new Initializer("src/resources/test1.tsv");
		GayleShapely gayleShapely = initializer.initialize();

		GayleShapelyMonteCarlo gsmc = new GayleShapelyMonteCarlo(gayleShapely);
		gsmc.run(1000);
		System.out.println(gsmc);
	}
	
	@Test
	public void test2() {
		Initializer initializer = new Initializer("src/resources/test2.tsv");
		GayleShapely gayleShapely = initializer.initialize();
		GayleShapelyMonteCarlo gsmc = new GayleShapelyMonteCarlo(gayleShapely);
		gsmc.run(1000);
		System.out.println(gsmc);
	}

}
