package edu.uf.compartments;

import java.util.*;

import edu.uf.interactable.Cell;
import edu.uf.utils.Rand;

public abstract class Recruiter {
    
    /*public void recruit(List<Quadrant> quadrants) {
    	Voxel[][][] grid = GridFactory.getGrid();
    	int qtty = getQtty();
    	Collections.shuffle(quadrants, new Random());
    	for(Quadrant q : quadrants) {
    		int qttyQ = (int) this.getQtty(q);
    		for(int i = 0; i  < qttyQ; i++) {
    			int xmin = q.xMin;
                int ymin = q.yMin;
                int zmin = q.zMin;
                int xmax = q.xMax;
                int ymax = q.yMax;
                int zmax = q.zMax;

                int x = xmin < xmax ? Rand.getRand().randunif(xmin, xmax) : xmin;
                int y = ymin < ymax ? Rand.getRand().randunif(ymin, ymax) : ymin;
                int z = zmin < zmax ? Rand.getRand().randunif(zmin, zmax) : zmin;

                grid[x][y][z].setCell(this.createCell());
                
                if((qtty--) == 0)
                	return;
    		}
    	}
    }*/
    
    /**
     * Create "qtty" cells at random positions inside the simulated space. 
     * The type of cell depends on the implementation of the method "createCell."
     * The quantity depends on the method "getQtty."
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
     * Create a cell. Different implementations should create different kinds of cells 
     * (e.g., Macrophages, Neutrophils, NK, etc.)
     * @return
     */
    public abstract Cell createCell();
    
    /**
     * Compute the number of cells to be created in the next iteration, given the overall amount 
     * of chemokine in the simulated space. The implementation of this method should specify the 
     * chemokine and call "getQtty(double chemokine)."
     * @return
     */
    public abstract int getQtty();
    
    /**
     * Compute the number of cells to be created in the next iteration, given the overall 
     * amount of chemokine "c." The implementation can be tailored to the cell being recruited.
     * @param c
     * @return
     */
    protected abstract int getQtty(double c);

    //public abstract double chemoatract(Quadrant voxel);

    /**
     * This method should return if cells are able to leave the simulated space. 
     * Living in the simulated space is an additional way of decreasing the number 
     * of cells. The other way is death. 
     * <strong>This method is not being used.</strong>
     * @return
     */
    public abstract boolean leave();

    /**
     * <strong>This method is not being used.</strong>
     * @param voxel
     * @return
     */
    public abstract Cell getCell(Voxel voxel);
    
}
