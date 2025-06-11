package edu.uf.intracellularState;

public class Phenotype {
	
	private static int phenotype = 0;
	
	/**
	 * Creates and returns a unique, sequential numeric phenotype identifier.
	 *
	 * <p>This method is typically used to assign a distinct ID to each new phenotype, ensuring
	 * consistency across molecules and intracellular models.</p>
	 *
	 * @return a unique phenotype ID as an {@code int}
	 */
	public static int createPhenotype() {
		Phenotype.phenotype = Phenotype.phenotype + 1;
		return Phenotype.phenotype; 
	}

}
