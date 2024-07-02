package edu.uf.intracellularState;

public class Phenotype {
	
	private static int phenotype = 0;
	
	/**
	 * Creates a unique numeric sequential phenotype identifier. 
	 * @return
	 */
	public static int createPhenotype() {
		Phenotype.phenotype = Phenotype.phenotype + 1;
		return Phenotype.phenotype; 
	}

}
