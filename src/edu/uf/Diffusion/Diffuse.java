package edu.uf.Diffusion;

public interface Diffuse {

	/**
	 * PDE solver for the diffusion problem. Solves the PDE in 3D for the state at index "index."
	 * <br/><br/>
	 * Some molecules, such as siderophores, have more than one state (e.g., free/bound-to-iron). 
	 * These states are accommodated into a fourth array dimension.
	 * @param space
	 * @param index
	 */
    public abstract void solver(double[][][][] space, int index);
    
}
