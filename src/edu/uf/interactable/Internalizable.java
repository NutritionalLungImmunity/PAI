package edu.uf.interactable;

public interface Internalizable {

	/**
	 * Returns true if the "Internalizable" agent is being phagocytosed. 
	 * False if otherwise. Notice it returns false before and after phagocytose. 
	 * It only returns true during. This method is important to flag that an agent 
	 * is both in the voxel and phagosome lists. Once it is only one or the other, 
	 * it should return false.
	 * @return
	 */
	public boolean isInternalizing();
	
}
