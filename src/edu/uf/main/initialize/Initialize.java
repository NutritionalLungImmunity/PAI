package edu.uf.main.initialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.Quadrant;
import edu.uf.compartments.Voxel;
import edu.uf.interactable.Afumigatus;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.Pneumocyte;

public abstract class Initialize {
    
    static Random rand = new Random();
    
    private int numSamples = -1;
    
    public static double random() {
    	return rand.nextDouble();
    }

    public static int randint(int min, int max) {
    	return rand.nextInt(max - min) + min;
    }
    
    public void setNumSamples(int numSamples) {
    	this.numSamples = numSamples;
    }
    
    public int getNumSamples() {
    	return numSamples;
    }
    
    public Voxel[][][] createPeriodicGrid(int xbin, int ybin, int zbin){
    	Voxel[][][] grid = new Voxel[xbin][ybin][zbin];
        for(int x = 0; x < xbin; x++)
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) {
        			grid[x][y][z] = new Voxel(x,y,z, numSamples);
        		}
        
        
        for(int x = 0; x < xbin; x++)
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) {
                    if (x + 1 < xbin)
                        grid[x][y][z].getNeighbors().add(grid[x + 1][y][z]);
                    else
                        grid[x][y][z].getNeighbors().add(grid[0][y][z]);
                    if (y + 1 < ybin)
                        grid[x][y][z].getNeighbors().add(grid[x][y + 1][z]);
                    else
                        grid[x][y][z].getNeighbors().add(grid[x][0][z]);
                    if (x - 1 >= 0)
                        grid[x][y][z].getNeighbors().add(grid[x - 1][y][z]);
                    else
                        grid[x][y][z].getNeighbors().add(grid[xbin-1][y][z]);
                    if (y - 1 >= 0)
                        grid[x][y][z].getNeighbors().add(grid[x][y - 1][z]);
                    else
                        grid[x][y][z].getNeighbors().add(grid[x][ybin-1][z]);
                    if (z + 1 < zbin)
                        grid[x][y][z].getNeighbors().add(grid[x][y][z + 1]);
                    else
                        grid[x][y][z].getNeighbors().add(grid[x][y][0]);
                    if (z - 1 >= 0)
                        grid[x][y][z].getNeighbors().add(grid[x][y][z - 1]);
                    else
                        grid[x][y][z].getNeighbors().add(grid[x][y][zbin-1]);
        		}
        
        return grid;
    }

    public List<Quadrant> createQuadrant(Voxel[][][] grid, int xbin, int ybin, int zbin, int xSize, int ySize, int zSize){
    	List<Quadrant> quadrants = new ArrayList<>();
        for(int i = 0; i < Math.ceil(xbin/xSize); i++)
        	for(int j = 0; j < Math.ceil(ybin/ySize); j++)
        		for(int k = 0; k < Math.ceil(zbin/zSize); k++) {
                    int xMin = i * xSize;
                    int xMax = xMin + xSize;
                    int yMin = j * ySize;
                    int yMax = yMin + ySize;
                    int zMin = k * zSize;
                    int zMax = zMin + zSize;
                    Quadrant q = new Quadrant();
                    quadrants.add(q);
                    for(int x = xMin; x < xMax; x++)
                    	for(int y = yMin; y < yMax; y++)
                    		for(int z = zMin; z < zMax; z++) {
                                if (x < xbin && y < ybin && z < zbin)
                                    grid[x][y][z].setQuadrant(q);
                    		}
        		}
        
        return quadrants;
    }
    
    public void setQuadrant(Voxel[][][] grid, int xbin, int ybin, int zbin) {
    	for(int i = 0; i < xbin; i++)
        	for(int j = 0; j < ybin; j++)
        		for(int k = 0; k < zbin; k++) 
        			if(grid[i][j][k].getQuadrant() != null)
        				grid[i][j][k].getQuadrant().updateChemokines(grid[i][j][k].getMolecules(), i, j, k);
    }

    public abstract void initializeMolecules(Voxel[][][] grid, int xbin, int ybin, int zbin, Diffuse diffuse, boolean verbose);

    public abstract void initializeLiver(Voxel[][][] grid, int xbin, int ybin, int zbin);

    public abstract List<Pneumocyte> initializePneumocytes(Voxel[][][] grid, int xbin, int ybin, int zbin, int numCells);

    public abstract List<Macrophage> initializeMacrophage(Voxel[][][] grid, int  xbin, int ybin, int zbin, int numMacrophages);

    public abstract List<Neutrophil>  initializeNeutrophils(Voxel[][][] grid, int  xbin, int ybin, int zbin, int numNeut);
    
    public abstract void  initializeErytrocytes(Voxel[][][] grid, int xbin, int ybin, int zbin);

    //public abstract  void infect(int numAspergillus, Voxel[][][] grid, int xmin, int xmax, int ymin, int ymax, int zmin, int zmax);
    
    public abstract List<Afumigatus> infect(int numAspergillus, Voxel[][][] grid, int xbin, int ybin, int zbin, int status, double initIron, double sigma, boolean verbose);
    
    //protected abstract void setSecretionPhenotypes();
}
