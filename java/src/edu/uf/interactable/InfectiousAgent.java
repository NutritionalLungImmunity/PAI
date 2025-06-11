package edu.uf.interactable;

import edu.uf.intracellularState.IntracellularModel;

public abstract class InfectiousAgent extends Cell{
    
	public InfectiousAgent(IntracellularModel booleanNetwork) {
		super(booleanNetwork);
	}

	/**
	 * Creates new cells or cells associated with an infectious agent. These cells may span multiple voxels.
	 * This method is functionally equivalent to calling {@code grow(x, y, z, xbin, ybin, zbin, null)}.
	 *
	 * @param x     the x-coordinate in the grid
	 * @param y     the y-coordinate in the grid
	 * @param z     the z-coordinate in the grid
	 * @param xbin  the x-axis bin resolution
	 * @param ybin  the y-axis bin resolution
	 * @param zbin  the z-axis bin resolution
	 */
	public void grow(int x, int y, int z, int xbin, int ybin, int zbin) {
		grow(x, y, z, xbin, ybin, zbin, null);
	}
	
	/**
	 * Creates new cells, either freely in the tissue or associated with an infectious agent.
	 * These cells may span across multiple voxels. If the {@code phagocyte} parameter is not null,
	 * the new cells will be created inside the specified leukocyte (phagocytosed); otherwise, they
	 * will be created externally. 
	 * <p>
	 * Calling {@code grow(x, y, z, xbin, ybin, zbin, null)} is equivalent to 
	 * {@code grow(x, y, z, xbin, ybin, zbin)}.
	 *
	 * @param x         the x-coordinate in the grid
	 * @param y         the y-coordinate in the grid
	 * @param z         the z-coordinate in the grid
	 * @param xbin      the x-axis bin resolution
	 * @param ybin      the y-axis bin resolution
	 * @param zbin      the z-axis bin resolution
	 * @param phagocyte the leukocyte in which to place the new cells, or {@code null} to create them outside
	 */
    public abstract void grow(int x, int y, int z, int xbin, int ybin, int zbin, Leukocyte phagocyte);
    
    /**
     * Returns {@code true} if the infectious agent (e.g., Aspergillus, Klebsiella, etc.) is capable of 
     * secreting the specified siderophore. For example, Aspergillus can secrete TAFC but not Aerobactin.
     *
     * @param siderophore the {@link Siderophore} object representing the siderophore to check
     * @return {@code true} if the agent can secrete the given siderophore; {@code false} otherwise
     */
    public abstract boolean hasSiderophore(Siderophore siderophore);
    
    /**
     * Returns {@code true} if the infectious agent is currently in a phenotype capable of secreting 
     * the specified siderophore (e.g., TAFC, Aerobactin).
     * 
     * @param mol the {@link Siderophore} object representing the siderophore to check
     * @return {@code true} if the agent is in a phenotype that secretes the given siderophore; 
     *         {@code false} otherwise
     */
    public abstract boolean isSecretingSiderophore(Siderophore mol);
    
    /**
     * Returns {@code true} if the infectious agent (e.g., Aspergillus, Klebsiella, etc.) is currently 
     * in a phenotype capable of uptaking the specified siderophore.
     *
     * @param mol the {@link Siderophore} object representing the siderophore to check
     * @return {@code true} if the agent can uptake the given siderophore; {@code false} otherwise
     */
    public abstract boolean isUptakingSiderophore(Siderophore mol);
    
    //public abstract void secreteSiderophore(Siderophore mol, int x, int y, int z);
	
}
