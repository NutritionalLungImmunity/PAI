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

    public abstract Cell createCell();
    
    public abstract int getQtty();
    
    protected abstract int getQtty(double c);

    //public abstract double chemoatract(Quadrant voxel);

    public abstract boolean leave();

    public abstract Cell getCell(Voxel voxel);
    
}
