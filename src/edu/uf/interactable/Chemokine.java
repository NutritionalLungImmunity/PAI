package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;

public abstract class Chemokine extends Molecule{
	
	
	protected Chemokine(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}  
	
	public abstract String getName();

	/**
	 * Compute chemoattraction in the voxel at position (x, y, z) given its concentration of chemokine.
	 * @param x axis position in the grid
	 * @param y axis position in the grid
	 * @param z axis position in the grid
	 * @return
	 */
	public abstract double chemoatract(int x, int y, int z);

}
