package edu.uf.main.initialize;

import java.util.ArrayList;
import java.util.List;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.Voxel;
import edu.uf.control.MultiThreadExec;
import edu.uf.interactable.covid.Neutrophil;
import edu.uf.interactable.covid.Pneumocyte;
import edu.uf.interactable.covid.SarsCoV2;
import edu.uf.interactable.covid.H2O2;
import edu.uf.utils.Constants;

public class InitializeCytotoxicity extends InitializeBaseModel{

	public void initializeMolecules(Voxel[][][] grid, int xbin, int ybin, int zbin, Diffuse diffuse, boolean verbose) {
    	H2O2 h2o2 = H2O2.getMolecule(new double[1][xbin][ybin][zbin], diffuse, new int[] {Neutrophil.ACTIVE});
    	SarsCoV2 cov = SarsCoV2.getMolecule(new double[1][xbin][ybin][zbin], diffuse, new int[] {});

    	MultiThreadExec.setMolecule(h2o2);
    	MultiThreadExec.setMolecule(cov);

    	for(int x = 0; x < xbin; x++) 
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) 
        			cov.set(Constants.Kd_SarsCoV2*Constants.VOXEL_VOL*10.0, 0, x, y, x);
        		
    	Voxel.setMolecule(H2O2.NAME, h2o2);
    	Voxel.setMolecule(SarsCoV2.NAME, cov);
    	
    	
    	//this.setSecretionPhenotypes();
    	
    }
	
	/*@Override
	protected void setSecretionPhenotypes() {
		H2O2.getMolecule().addPhenotype(Phenotypes.ACTIVE);
	}*/
	
	public List initializeNeutrophils(Voxel[][][] grid, int xbin, int ybin, int zbin,  int numNeut) {
    	List<Neutrophil> list = new ArrayList<>();
    	for (int i = 0; i < numNeut; i++) {
            int x = randint(0, xbin-1);
            int y = randint(0, ybin-1);
            int z = randint(0, zbin-1);
            Neutrophil n = new Neutrophil();
            list.add(n);
            grid[x][y][z].setCell(n);
    	}
    	return list;
    }
	
	public List initializePneumocytes(Voxel[][][] grid, int xbin, int ybin, int zbin, int numCells) {
        int k = 0;
        List<Pneumocyte> list = new ArrayList<>();
        while (k < numCells) {
            int x = randint(0, xbin-1);
            int y = randint(0, ybin-1);
            int z = randint(0, zbin-1);
            if (grid[x][y][z].getCells().isEmpty()) {
            	Pneumocyte p = new Pneumocyte();
            	//EndothelialCells ec = new EndothelialCells();
                grid[x][y][z].setCell(p);
                //grid[x][y][z].setCell(ec);
                list.add(p);
                k = k + 1;
            }
            if(k%100000==0)System.out.println(k +  " pneumocytes initialized ...");
        }
        return list;
    }
	
}
