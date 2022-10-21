package edu.uf.compartments;

import java.util.*;

import edu.uf.interactable.Cell;
import edu.uf.utils.Rand;

public abstract class Recruiter {
	
    /*public void recruit_legacy(Voxel[][][] grid, List<Quadrant> quadrants) {
        int qtty = (int) this.getQtty();
        //int atempts = 0;
        if (qtty > 0) {
        	Collections.shuffle(quadrants);
            while(true) {
                for (Quadrant q : quadrants) {
                	//atempts++;
                    if (Rand.getRand().randunif() < this.chemoatract(q)) {
                    	//atempts--;
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
                        qtty = qtty - 1;
                        if (qtty <= 0) {
                        	//System.out.println("recurit atempts = " + atempts);
                            return;
                        }
                    }
                }
            }
        }
        //System.out.println("recurit atempts = " + atempts);
    }*/
    
    public void recruit(Voxel[][][] grid, List<Quadrant> quadrants) {
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
    }

    public abstract Cell createCell();
    
    public abstract int getQtty();

    public abstract int getQtty(Quadrant q);
    
    protected abstract int getQtty(double c);

    //public abstract double chemoatract(Quadrant voxel);

    public abstract boolean leave();

    public abstract Cell getCell(Voxel voxel);
    
}
