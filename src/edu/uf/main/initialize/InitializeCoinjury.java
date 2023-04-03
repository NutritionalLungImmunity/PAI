package edu.uf.main.initialize;

import java.util.ArrayList;
import java.util.List;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.Voxel;
import edu.uf.control.MultiThreadExec;
import edu.uf.interactable.Afumigatus;
import edu.uf.interactable.AfumigatusNoIron;
import edu.uf.interactable.Granule;
import edu.uf.interactable.Hepcidin;
import edu.uf.interactable.IL10;
import edu.uf.interactable.IL6;
import edu.uf.interactable.Lactoferrin;
import edu.uf.interactable.Liver;
import edu.uf.interactable.MIP1B;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.Pneumocyte;
import edu.uf.interactable.TGFb;
import edu.uf.interactable.TNFa;
import edu.uf.interactable.capa.CapaMacrophage;
import edu.uf.interactable.capa.CapaNeutrophil;
import edu.uf.interactable.capa.CapaPneumocyte;
import edu.uf.intracellularState.Phenotypes;
import edu.uf.utils.Constants;

public class InitializeCoinjury extends InitializeBaseModel{

	public void initializeMolecules(Voxel[][][] grid, int xbin, int ybin, int zbin, Diffuse diffuse, boolean verbose) {
    	if(verbose) {
    		System.out.println("Initializing Iron, TAFC, Lactoferrin, Transferrin, Hepcidin, IL6, TNF-a, IL10, TGF-b, MIP2, MIP1-b");
    	}
    	TNFa tnfa = TNFa.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	IL10 il10 = IL10.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	TGFb tgfb = TGFb.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	MIP2 mip2 = MIP2.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	MIP1B mip1b = MIP1B.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	Granule gran = Granule.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	
    	
    	Hepcidin.getMolecule(new double[1][1][1][1], null);
    	IL6.getMolecule(new double[1][1][1][1], null);
    	Lactoferrin.getMolecule(new double[1][1][1][1], null);
    	/*MIP2 mip2 = MIP2.getMolecule(new double[1][1][1][1], null);
    	MIP1B mip1b = MIP1B.getMolecule(new double[1][1][1][1], null);
    	Granule gran = Granule.getMolecule(new double[1][1][1][1], null);*/
    	
    	
    	MultiThreadExec.setMolecule(tnfa);
    	MultiThreadExec.setMolecule(il10);
    	MultiThreadExec.setMolecule(tgfb);
    	MultiThreadExec.setMolecule(mip2);
    	MultiThreadExec.setMolecule(mip1b);
    	MultiThreadExec.setMolecule(gran);
    	
    	
    	Voxel.setMolecule(TNFa.NAME, tnfa);
    	Voxel.setMolecule(IL10.NAME, il10);
    	Voxel.setMolecule(TGFb.NAME, tgfb);
    	Voxel.setMolecule(MIP2.NAME, mip2);
    	Voxel.setMolecule(MIP1B.NAME, mip1b);
    	Voxel.setMolecule(Granule.NAME, gran, true);
    	
    	this.setSecretionPhenotypes();
    }

    public void initializeLiver(Voxel[][][] grid, int xbin, int ybin, int zbin) {
    	for(int x = 0; x < xbin; x++)
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) 
                    grid[x][y][z].setCell(Liver.getLiver()); //REVIEW
    }


    public List<Afumigatus> infect(int numAspergillus, Voxel[][][] grid, int xbin, int ybin, int zbin, int status, double initIron, double sigma, boolean verbose) {
    	if(verbose)System.out.println("Infecting with " + numAspergillus + " conidia!");
    	List<Afumigatus> list = new ArrayList<>();
        for (int i = 0; i < numAspergillus; i++) {
        	int x = randint(0, xbin-1);
            int y = randint(0, ybin-1);
            int z = randint(0, zbin-1);
            AfumigatusNoIron a = new AfumigatusNoIron(
            		x, y, z, x, y, z, random(), random(), random(), 
            		0, initIron, 0, 0, true
            );
            list.add(a);
            a.setStatus(status);
            grid[x][y][z].setCell(a);
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
            	Pneumocyte p = new CapaPneumocyte();
                grid[x][y][z].setCell(p);
                list.add(p);
                k = k + 1;
            }
            if(k%100000==0)System.out.println(k +  " pneumocytes initialized ...");
        }
        return list;
    }

    public List<Macrophage> initializeMacrophage(Voxel[][][] grid, int  xbin, int ybin, int zbin,  int numMacrophages) {
    	List<Macrophage> list = new ArrayList<>();
        for (int i = 0; i < numMacrophages; i++) {
            int x = randint(0, xbin-1);
            int y = randint(0, ybin-1);
            int z = randint(0, zbin-1);
            Macrophage m = new CapaMacrophage(Constants.MA_INTERNAL_IRON);
            list.add(m);
            grid[x][y][z].setCell(m);
        }
        return list;
    }

    public List<Neutrophil> initializeNeutrophils(Voxel[][][] grid, int xbin, int ybin, int zbin,  int numNeut) {
    	List<Neutrophil> list = new ArrayList<>();
    	for (int i = 0; i < numNeut; i++) {
            int x = randint(0, xbin-1);
            int y = randint(0, ybin-1);
            int z = randint(0, zbin-1);
            Neutrophil n = new CapaNeutrophil(0.0);
            list.add(n);
            grid[x][y][z].setCell(n);
    	}
    	return list;
    }
	
}
