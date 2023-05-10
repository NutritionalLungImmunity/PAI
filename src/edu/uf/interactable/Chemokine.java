package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;

public abstract class Chemokine extends Molecule{
	
	
	protected Chemokine(double[][][][] qttys, Diffuse diffuse, int[] phenotypes) {
		super(qttys, diffuse, phenotypes); 
	}  
	
	public abstract String getName();

	public abstract double chemoatract(int x, int y, int z);

}
