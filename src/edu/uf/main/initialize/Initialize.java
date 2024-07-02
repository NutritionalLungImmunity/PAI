package edu.uf.main.initialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.GridFactory;
import edu.uf.compartments.Voxel;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.PneumocyteII;
import edu.uf.interactable.Afumigatus.Afumigatus;

public abstract class Initialize {
    
    static Random rand = new Random();
    
    private int numSamples = -1;
    
    /**
     * <strong> this method should be removed. Use Rand.getRand</strong>
     * @return
     */
    public static double random() {
    	return rand.nextDouble();
    }

    /**
     * <strong> this method should be removed. Use Rand.getRand</strong>
     * @return
     */
    public static int randint(int min, int max) {
    	return rand.nextInt(max - min) + min;
    }
    
    public void setNumSamples(int numSamples) {
    	this.numSamples = numSamples;
    }
    
    public int getNumSamples() {
    	return numSamples;
    }
    
    /**
     * Create a periodic grid.
     * <strong> too specific</strong>
     * @return
     */
    public Voxel[][][] createPeriodicGrid(int xbin, int ybin, int zbin){
    	GridFactory.set(xbin, ybin, zbin, numSamples, GridFactory.PERIODIC_GRID);
    	return GridFactory.getGrid();
    }

    public abstract void initializeMolecules(Diffuse diffuse, boolean verbose);

    public abstract void initializeLiver();
    
    public abstract void initializeTypeIPneumocytes(int numCells);
    
    public abstract void initializeBlood();

    public abstract List<PneumocyteII> initializePneumocytes(int numCells);

    public abstract List<Macrophage> initializeMacrophage(int numMacrophages);

    public abstract List<Neutrophil>  initializeNeutrophils(int numNeut);

    //public abstract  void infect(int numAspergillus, Voxel[][][] grid, int xmin, int xmax, int ymin, int ymax, int zmin, int zmax);
    
    public abstract List<Afumigatus> infect(int numAspergillus, int status, double initIron, double sigma, boolean verbose);
    
    //protected abstract void setSecretionPhenotypes();
}
