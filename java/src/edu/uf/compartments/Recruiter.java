package edu.uf.compartments;

import edu.uf.interactable.Cell;
import edu.uf.utils.Rand;

public abstract class Recruiter {
    
	/**
	 * Creates cells at random positions within the simulated space. 
	 * The type of each cell is determined by the implementation of the {@code createCell} method, 
	 * and the quantity is determined by the {@code getQtty} method.
	 */
    public void recruit() {
    	Voxel[][][] grid = GridFactory.getGrid();
    	int qtty = getQtty();
    	for(int i = 0; i < qtty; i++) {
    		int x = Rand.getRand().randunif(0, GridFactory.getXbin());
    		int y = Rand.getRand().randunif(0, GridFactory.getYbin());
    		int z = Rand.getRand().randunif(0, GridFactory.getZbin());
    		
    		grid[x][y][z].setCell(this.createCell());
    	}
    }

    /**
     * Creates a cell. Different implementations should create different types of cells 
     * (e.g., Macrophages, Neutrophils, NK cells, etc.).
     *
     * @return the created cell
     */
    public abstract Cell createCell();
    
    /**
     * Computes the number of cells to be created in the next iteration, 
     * based on the overall amount of chemokine in the simulated space. 
     * The implementation of this method should specify which chemokine to use 
     * and call {@code getQtty(double chemokine)} accordingly.
     *
     * @return the number of cells to create
     */
    public abstract int getQtty();
    
    /**
     * Computes the number of cells to be created in the next iteration, 
     * based on the overall amount of chemokine {@code c}. 
     * The implementation can be tailored to the specific type of cell being recruited.
     *
     * @param c the concentration of chemokine
     * @return the number of cells to create
     */
    protected abstract int getQtty(double c);

    //public abstract double chemoatract(Quadrant voxel);

    /**
     * Indicates whether cells are allowed to leave the simulated space. 
     * Leaving the simulated space is an additional mechanism for reducing 
     * the number of cells, alongside cell death.
     * <strong>Note:</strong> This method is currently not used.
     *
     * @return {@code true} if cells can leave the simulated space; {@code false} otherwise
     */
    public abstract boolean leave();

    /**
     * <strong>This method is currently not used.</strong>
     * @param voxel
     * @return
     */
    public abstract Cell getCell(Voxel voxel);
    
}
