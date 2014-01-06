package gayleshapely;

import gayleshapely.MatchingSolnMonteCarlo.SchoolCounts;

import java.util.HashMap;

public class MarketSimulation {

	public HashMap<Student,SchoolCounts> SimulateBiUniformMarket(int numstudents,int numschools) {
		
		PreferenceGenerator generator = new PreferenceGenerator();
		GayleShapely gayleShapely = generator.simulateUniformMarket(numstudents,numschools);
		GayleShapelyMonteCarlo gsmc = new GayleShapelyMonteCarlo(gayleShapely);
		return gsmc.simulate(1000);
	}
	
	public HashMap<Student,SchoolCounts> SimulateMatchingFromFile() {
		Initializer initializer = new Initializer("src/resources/test1.tsv");
		GayleShapely gayleShapely = initializer.initialize();
		GayleShapelyMonteCarlo gsmc = new GayleShapelyMonteCarlo(gayleShapely);
		return gsmc.simulate(1000);
	}
	
}
