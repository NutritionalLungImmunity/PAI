package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;

public abstract class Chemokine extends Molecule{
	
	
	protected Chemokine(double[][][][] qttys, Diffuse diffuse, String name) {
		super(qttys, diffuse, name);
	}  

	/**
	 * Computes the chemoattraction in the voxel located at the specified (x, y, z) coordinates,
	 * based on the local chemokine concentration.
	 *
	 * @param x the x-coordinate in the grid
	 * @param y the y-coordinate in the grid
	 * @param z the z-coordinate in the grid
	 * @return the computed chemoattraction value
	 */
	public abstract double chemoatract(int x, int y, int z);

}
