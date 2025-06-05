package edu.uf.interactable;

public interface Internalizable {

	/**
	 * Returns {@code true} if the {@link Internalizable} agent is currently in the process of being phagocytosed; 
	 * otherwise, returns {@code false}.
	 * 
	 * <p>This method returns {@code true} only during the actual phagocytosis event—i.e., the short time span in which 
	 * the agent is present in both the voxel and phagosome lists. Before or after this transition, the method returns 
	 * {@code false}.</p>
	 * 
	 * <p>It is used to flag agents that are simultaneously tracked in both compartments, ensuring consistent state 
	 * management during phagocytosis.</p>
	 *
	 * @return {@code true} if the agent is in the process of being phagocytosed; {@code false} otherwise
	 */
	public boolean isInternalizing();
	
}
