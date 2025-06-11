package edu.uf.Diffusion;

public interface Diffuse {

	/**
	 * Solves the partial differential equation (PDE) for diffusion in 3D space, 
	 * targeting the molecule state at the specified {@code index}.
	 * <br/><br/>
	 * Some molecules—such as siderophores—can exist in multiple states 
	 * (e.g., free or bound to iron). These states are represented as a fourth dimension 
	 * in the array structure.
	 *
	 * @param space the 4D array representing molecule concentrations across space and state
	 * @param index the index of the molecule state to solve for
	 */
    public abstract void solver(double[][][][] space, int index);
    
}
