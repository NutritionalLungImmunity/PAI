package edu.uf.interactable;

import edu.uf.intracellularState.IntracellularModel;

public abstract class InfectiousAgent extends Cell{
    
	public InfectiousAgent(IntracellularModel booleanNetwork) {
		super(booleanNetwork);
	}

	/**
	 * Create new cells or cells of the Infectious agent. These cells may cross different voxels.
	 * This method is equivalent to "grow(x, y, z, xbin, ybin, zbin, null)."
	 * @param x
	 * @param y
	 * @param z
	 * @param xbin
	 * @param ybin
	 * @param zbin
	 */
	public void grow(int x, int y, int z, int xbin, int ybin, int zbin) {
		grow(x, y, z, xbin, ybin, zbin, null);
	}
	
	/**
	 * Create new cells or cells of the Infectious agent. These cells may cross different voxels.
	 * If the leukocyte argument is not null, new cells will be created inside the leukocyte. Outside 
	 * otherwise. Calling "grow(x, y, z, xbin, ybin, zbin, null)" is equivalent to call 
	 * "grow(x, y, z, xbin, ybin, zbin)."
	 * @param x
	 * @param y
	 * @param z
	 * @param xbin
	 * @param ybin
	 * @param zbin
	 * @param phagocyte
	 */
    public abstract void grow(int x, int y, int z, int xbin, int ybin, int zbin, Leukocyte phagocyte);
    
    /**
     * Returns true if the infectious agent (e.g., Aspergillus, Klebsiella, etc.) is able to secrete a 
     * siderophore. For example, Aspergillus can secrete TAFC but not Aerobactin.
     * @param siderophore
     * @return
     */
    public abstract boolean hasSiderophore(Siderophore siderophore);
    
    /**
     * Returns true if the infectious agent (e.g., Aspergillus, Klebsiella, etc.) is in the secreting-this-siderophore-phenotype.
     * @param mol
     * @return
     */
    public abstract boolean isSecretingSiderophore(Siderophore mol);
    
    /**
     * Returns true if the infectious agent (e.g., Aspergillus, Klebsiella, etc.) is in the uptaking-this-siderophore-phenotype.
     * @param mol
     * @return
     */
    public abstract boolean isUptakingSiderophore(Siderophore mol);
    
    //public abstract void secreteSiderophore(Siderophore mol, int x, int y, int z);
	
}
