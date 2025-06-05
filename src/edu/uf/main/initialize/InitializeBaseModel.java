package edu.uf.main.initialize;

import java.util.ArrayList;
import java.util.List;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.GridFactory;
import edu.uf.compartments.Voxel;
import edu.uf.control.MultiThreadExec;
import edu.uf.interactable.Blood;
import edu.uf.interactable.Heme;
import edu.uf.interactable.IL10;
import edu.uf.interactable.IL6;
import edu.uf.interactable.Iron;
import edu.uf.interactable.Lactoferrin;
import edu.uf.interactable.Liver;
import edu.uf.interactable.MIP1B;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.PneumocyteII;
import edu.uf.interactable.PneumocyteI;
import edu.uf.interactable.TGFb;
import edu.uf.interactable.TNFa;
import edu.uf.interactable.Transferrin;
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.interactable.Afumigatus.TAFC;
import edu.uf.intracellularState.IntracellularModelFactory;
import edu.uf.utils.Constants;

public class InitializeBaseModel extends Initialize{

    public void initializeMolecules(Diffuse diffuse, boolean verbose) {
    	if(verbose) {
    		System.out.println("Initializing Iron, TAFC, Lactoferrin, Transferrin, Hepcidin, IL6, TNF-a, IL10, TGF-b, MIP2, MIP1-b");
    	}
    	
    	int xbin = GridFactory.getXbin();
    	int ybin = GridFactory.getYbin();
    	int zbin = GridFactory.getZbin();
    	
    	Iron iron = Iron.getMolecule(null);
    	TAFC tafc = TAFC.getMolecule(diffuse);
    	Lactoferrin lactoferrin = Lactoferrin.getMolecule(diffuse);
    	Transferrin transferrin = Transferrin.getMolecule( diffuse);
    	//Hepcidin hepcidin = Hepcidin.getMolecule(null); //REVIEW
    	IL6 il6 = IL6.getMolecule(diffuse);
    	TNFa tnfa = TNFa.getMolecule(diffuse);
    	IL10 il10 = IL10.getMolecule(diffuse);
    	TGFb tgfb = TGFb.getMolecule(diffuse);
    	MIP2 mip2 = MIP2.getMolecule(diffuse);
    	MIP1B mip1b = MIP1B.getMolecule(diffuse);
    	//Heme heme = Heme.getMolecule(null);
    	//DNAse dnase = DNAse.getMolecule(new double[1][xbin][ybin][zbin], null, new int[] {});
    	//Granule gran = Granule.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	
    	
    	MultiThreadExec.setMolecule(iron);
    	MultiThreadExec.setMolecule(tafc);
    	MultiThreadExec.setMolecule(lactoferrin);
    	MultiThreadExec.setMolecule(transferrin);
    	//MultiThreadExec.setMolecule(hepcidin);
    	MultiThreadExec.setMolecule(il6);
    	MultiThreadExec.setMolecule(tnfa);
    	MultiThreadExec.setMolecule(il10);
    	MultiThreadExec.setMolecule(tgfb);
    	MultiThreadExec.setMolecule(mip2);
    	MultiThreadExec.setMolecule(mip1b);
    	//MultiThreadExec.setMolecule(heme);
    	//MultiThreadExec.setMolecule(dnase);
    	//MultiThreadExec.setMolecule(gran);
    	
    	
    	Voxel.setMolecule(Iron.NAME, iron, true, true);
    	Voxel.setMolecule(TAFC.NAME, tafc, true, true);
    	Voxel.setMolecule(Lactoferrin.NAME, lactoferrin, false, true);
    	for(int x = 0; x < xbin; x++) 
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) {
        			transferrin.set(Constants.DEFAULT_APOTF_CONCENTRATION, 0, x, y, z);
        			transferrin.set(Constants.DEFAULT_TFFE_CONCENTRATION, 1, x, y, z);
        			transferrin.set(Constants.DEFAULT_TFFE2_CONCENTRATION, 2, x, y, z);
        			
        			//tafc.set(1e-4*6.4e-11, 1, x, y, z);
        			//iron.set(6.4e-18, 0, x, y, z);
        			//hepcidin.set(Constants.THRESHOLD_HEP * Constants.VOXEL_VOL, 0, x, y, x);
        		}
    	Voxel.setMolecule(Transferrin.NAME, transferrin, false, true);
    	//Voxel.setMolecule(Hepcidin.NAME, hepcidin);
    	Voxel.setMolecule(IL6.NAME, il6);
    	Voxel.setMolecule(TNFa.NAME, tnfa);
    	Voxel.setMolecule(IL10.NAME, il10);
    	Voxel.setMolecule(TGFb.NAME, tgfb);
    	Voxel.setMolecule(MIP2.NAME, mip2);
    	Voxel.setMolecule(MIP1B.NAME, mip1b);
    	//Voxel.setMolecule(Heme.NAME, heme, true, false);
    	//Voxel.setMolecule(DNAse.NAME, dnase, false, true);
    	//Voxel.setMolecule(Granule.NAME, gran, true);
    	
    	
    	
    	//this.setSecretionPhenotypes();
    }
    
    /*protected void setSecretionPhenotypes() {
		//Granule.getMolecule().addPhenotype(Phenotypes.ACTIVE);
		Hepcidin.getMolecule().addPhenotype(Phenotypes.ALT_ACTIVE);
		Hepcidin.getMolecule().addPhenotype(Phenotypes.MIX_ACTIVE);
		Hepcidin.getMolecule().addPhenotype(Phenotypes.INACTIVE);
		Hepcidin.getMolecule().addPhenotype(Phenotypes.RESTING);
		Hepcidin.getMolecule().addPhenotype(Phenotypes.ANERGIC);
		//IL1.getMolecule().addPhenotype(Phenotypes.ACTIVE);
		//IL1.getMolecule().addPhenotype(Phenotypes.MIX_ACTIVE);
		IL10.getMolecule().addPhenotype(Phenotypes.ACTIVE);
		IL10.getMolecule().addPhenotype(Phenotypes.MIX_ACTIVE);
		IL10.getMolecule().addPhenotype(Phenotypes.ALT_ACTIVE);
		IL10.getMolecule().addPhenotype(Phenotypes.INACTIVE);
		IL6.getMolecule().addPhenotype(Phenotypes.ACTIVE);
		IL6.getMolecule().addPhenotype(Phenotypes.MIX_ACTIVE);
		Lactoferrin.getMolecule().addPhenotype(Phenotypes.ACTIVE);
		MIP1B.getMolecule().addPhenotype(Phenotypes.ACTIVE);
		MIP2.getMolecule().addPhenotype(Phenotypes.ACTIVE);
		TGFb.getMolecule().addPhenotype(Phenotypes.INACTIVE);
		TNFa.getMolecule().addPhenotype(Phenotypes.ACTIVE);
		TNFa.getMolecule().addPhenotype(Phenotypes.MIX_ACTIVE);
    }*/

    public void initializeBlood() {
    	int xbin = GridFactory.getXbin();
    	int ybin = GridFactory.getYbin();
    	int zbin = GridFactory.getZbin();
    	Voxel[][][] grid = GridFactory.getGrid();
    	Blood blood = Blood.getBlood(xbin, ybin, zbin);
    	for(int x = 0; x < xbin; x++)
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) 
        			grid[x][y][z].setCell(blood);
    }
    
    public void initializeLiver() {
    	int xbin = GridFactory.getXbin();
    	int ybin = GridFactory.getYbin();
    	int zbin = GridFactory.getZbin();
    	Voxel[][][] grid = GridFactory.getGrid();
    	for(int x = 0; x < xbin; x++)
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) 
                    grid[x][y][z].setCell(Liver.getLiver()); //REVIEW
    }

    public List initializePneumocytes(int numCells) {
    	int xbin = GridFactory.getXbin();
    	int ybin = GridFactory.getYbin();
    	int zbin = GridFactory.getZbin();
    	Voxel[][][] grid = GridFactory.getGrid();
        int k = 0;
        List<PneumocyteII> list = new ArrayList<>();
        while (k < numCells) {
            int x = randint(0, xbin); // -1?
            int y = randint(0, ybin); // -1?
            int z = randint(0, zbin); // -1?
            if (grid[x][y][z].getCells().isEmpty()) {
            	PneumocyteII p = new PneumocyteII(IntracellularModelFactory.createBooleanNetwork(IntracellularModelFactory.PNEUMOCYTE_STATE_MODEL));
                grid[x][y][z].setCell(p);
                list.add(p);
                k = k + 1;
            }
            if(k%100000==0)System.out.println(k +  " pneumocytes initialized ...");
        }
        return list;
    }
    
    public void initializeTypeIPneumocytes(int numCells) {
    	int xbin = GridFactory.getXbin();
    	int ybin = GridFactory.getYbin();
    	int zbin = GridFactory.getZbin();
    	
    	int x0 = 0;
    	int y0 = 0;
    	int z0 = 0;
    	
    	double p  = numCells/((double) xbin*ybin*zbin);
    	Voxel[][][] grid = GridFactory.getGrid();
    	PneumocyteI cell = new PneumocyteI(IntracellularModelFactory.createBooleanNetwork(IntracellularModelFactory.PNEUMOCYTE_I_KLEBSIELLA_MODEL));
    	
    	for(int x = 0; x < xbin; x++)
    		for(int y = 0; y < ybin; y++)
    			for(int z = 0; z < zbin; z++) {
    				if(rand.nextDouble() < p || !grid[x][y][z].getNeighbors().contains(grid[x0][y0][z0]))
    					cell = new PneumocyteI(IntracellularModelFactory.createBooleanNetwork(IntracellularModelFactory.PNEUMOCYTE_I_KLEBSIELLA_MODEL));
                    grid[x][y][z].setCell(cell);
                    x0 = x;
                    y0 = y;
                    z0 = z;
    			}
    }

    public List<Macrophage> initializeMacrophage(int numMacrophages) {
    	int xbin = GridFactory.getXbin();
    	int ybin = GridFactory.getYbin();
    	int zbin = GridFactory.getZbin();
    	Voxel[][][] grid = GridFactory.getGrid();
    	List<Macrophage> list = new ArrayList<>();
        for (int i = 0; i < numMacrophages; i++) {
            int x = randint(0, xbin-1);
            int y = randint(0, ybin-1);
            int z = randint(0, zbin-1);
            Macrophage m = new Macrophage(Constants.MA_INTERNAL_IRON, IntracellularModelFactory.createBooleanNetwork(IntracellularModelFactory.ASPERGILLUS_MACROPHAGE));
            list.add(m);
            grid[x][y][z].setCell(m);
        }
        return list;
    }

    public List<Neutrophil> initializeNeutrophils(int numNeut) {
    	int xbin = GridFactory.getXbin();
    	int ybin = GridFactory.getYbin();
    	int zbin = GridFactory.getZbin();
    	Voxel[][][] grid = GridFactory.getGrid();
    	List<Neutrophil> list = new ArrayList<>();
    	for (int i = 0; i < numNeut; i++) {
            int x = randint(0, xbin-1);
            int y = randint(0, ybin-1);
            int z = randint(0, zbin-1);
            Neutrophil n = new Neutrophil(0.0, IntracellularModelFactory.createBooleanNetwork(IntracellularModelFactory.NEUTROPHIL_STATE_MODEL));
            list.add(n);
            grid[x][y][z].setCell(n);
    	}
    	return list;
    }

    public List<Afumigatus> infect(int numAspergillus, int status, double initIron, double sigma, boolean verbose) {
    	int xbin = GridFactory.getXbin();
    	int ybin = GridFactory.getYbin();
    	int zbin = GridFactory.getZbin();
    	Voxel[][][] grid = GridFactory.getGrid();
    	if(verbose)System.out.println("Infecting with " + numAspergillus + " conidia!");
    	List<Afumigatus> list = new ArrayList<>();
        for (int i = 0; i < numAspergillus; i++) {
        	int x = randint(0, xbin-1);
            int y = randint(0, ybin-1);
            int z = randint(0, zbin-1);
            Afumigatus a = new Afumigatus(
            		x, y, z, x, y, z, random(), random(), random(), 
            		0, initIron, status, true
            );
            list.add(a);
            grid[x][y][z].setCell(a);
        }
    	return list;
    }
}
