package edu.uf.main.initialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.Voxel;
import edu.uf.control.MultiThreadExec;
import edu.uf.interactable.Afumigatus;
import edu.uf.interactable.Cell;
import edu.uf.interactable.IL10;
import edu.uf.interactable.IL6;
import edu.uf.interactable.Lactoferrin;
import edu.uf.interactable.MCP1;
import edu.uf.interactable.covid.DAMP;
import edu.uf.interactable.covid.DC;
import edu.uf.interactable.covid.Defensin;
import edu.uf.interactable.covid.EndothelialCells;
import edu.uf.interactable.covid.H2O2;
import edu.uf.interactable.covid.IFN1;
import edu.uf.interactable.covid.IL6Complex;
import edu.uf.interactable.covid.NK;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.covid.Neutrophil;
import edu.uf.interactable.covid.Pneumocyte;
import edu.uf.interactable.TGFb;
import edu.uf.interactable.TNFa;
import edu.uf.interactable.covid.SAMP;
import edu.uf.interactable.covid.SarsCoV2;
import edu.uf.interactable.covid.VEGF;
import edu.uf.intracellularState.EukaryoteSignalingNetwork;
import edu.uf.intracellularState.Phenotypes;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;

public class InitializeCovidModel extends InitializeBaseModel{

	@Override
	public void initializeMolecules(Voxel[][][] grid, int xbin, int ybin, int zbin, Diffuse diffuse, boolean verbose) {
		if(verbose) {
    		System.out.println("Initializing Iron, TAFC, Lactoferrin, Transferrin, Hepcidin, IL6, TNF-a, IL10, TGF-b, MIP2, MIP1-b");
    	}
		
		//IL6Complex il6 = IL6Complex.getMolecule(new double[3][xbin][ybin][zbin], diffuse);
    	//TNFa tnfa = TNFa.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	//IL10 il10 = IL10.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	//TGFb tgfb = TGFb.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	MIP2 mip2 = MIP2.getMolecule(new double[1][xbin][ybin][zbin], null);
    	//MCP1 mcp1 = MCP1.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	IFN1 ifn1 = IFN1.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	//VEGF vegf = VEGF.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	SarsCoV2 virus = SarsCoV2.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	SAMP samp = SAMP.getMolecule(new double[1][xbin][ybin][zbin], null);
    	DAMP damp = DAMP.getMolecule(new double[1][xbin][ybin][zbin], null);
    	Lactoferrin def = Lactoferrin.getMolecule(new double[3][xbin][ybin][zbin], diffuse);
    	H2O2 h2o2 = H2O2.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	
    	//IL6.getMolecule(new double[1][1][1][1], null); //Dummy initialization. Necessary!
    	
    	/*for(int x = 0; x < xbin; x++) 
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++)
        			def.set(4.571428e-19, 0, x, y, z);*/
    	
    	
    	MultiThreadExec.setMolecule(ifn1);
    	//MultiThreadExec.setMolecule(vegf);
    	MultiThreadExec.setMolecule(samp);
    	MultiThreadExec.setMolecule(damp);
    	//MultiThreadExec.setMolecule(il6);
    	//MultiThreadExec.setMolecule(tnfa);
    	//MultiThreadExec.setMolecule(il10);
    	//MultiThreadExec.setMolecule(tgfb);
    	MultiThreadExec.setMolecule(mip2);
    	//MultiThreadExec.setMolecule(mcp1);
    	MultiThreadExec.setMolecule(virus);
    	MultiThreadExec.setMolecule(def);
    	MultiThreadExec.setMolecule(h2o2);
    	
    	
    	Voxel.setMolecule(IFN1.NAME, ifn1);
    	//Voxel.setMolecule(VEGF.NAME, vegf);
    	Voxel.setMolecule(SAMP.NAME, samp);
    	Voxel.setMolecule(DAMP.NAME, damp);
    	//Voxel.setMolecule(IL6Complex.NAME, il6);
    	//Voxel.setMolecule(TNFa.NAME, tnfa);
    	//Voxel.setMolecule(IL10.NAME, il10);
    	//Voxel.setMolecule(TGFb.NAME, tgfb);
    	Voxel.setMolecule(MIP2.NAME, mip2);
    	//Voxel.setMolecule(MCP1.NAME, mcp1);
    	Voxel.setMolecule(SarsCoV2.NAME, virus);
    	Voxel.setMolecule(Lactoferrin.NAME, def);
    	Voxel.setMolecule(H2O2.NAME, h2o2);
    	
    	this.setSecretionPhenotypes();
		
	}
	
	public void initializeCovid(Voxel[][][] grid, int xbin, int ybin, int zbin, Diffuse diffuse, double qtty) {
    	SarsCoV2 virus = (SarsCoV2) SarsCoV2.getMolecule();
    	
    	
    	for(int x = 0; x < xbin; x++) 
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++)
        			virus.set(qtty, 0, x, y, z);
    	
    	
    	Voxel.setMolecule(SarsCoV2.NAME, virus);
		
	}
	
	public void covidInfec(Voxel[][][] grid, int xbin, int ybin, int zbin, Diffuse diffuse, int qtty) {
    	
    	EukaryoteSignalingNetwork.VIRUS_e = SarsCoV2.MOL_IDX;
		int count = 0;
		while(count < qtty) {
			int x = randint(0, xbin-1);
            int y = randint(0, ybin-1);
            int z = randint(0, zbin-1);
            for(Entry<Integer, Cell> entry : grid[x][y][z].getCells().entrySet()) {
            	if(entry.getValue() instanceof Pneumocyte) {
            		((Pneumocyte) entry.getValue()).incViralLoad(Constants.SarsCoV2_UPTAKE_QTTY);
            		((Pneumocyte) entry.getValue()).bind(SarsCoV2.MOL_IDX);
            		count++;
            	}
            }
		}
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

	@Override
	public void initializeLiver(Voxel[][][] grid, int xbin, int ybin, int zbin) {
		// TODO Auto-generated method stub
	}

	@Override
	public List initializeNeutrophils(Voxel[][][] grid, int xbin, int ybin, int zbin, int numNeut) {
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
	
	public List initializeDC(Voxel[][][] grid, int xbin, int ybin, int zbin, int numNeut) {
		List<DC> list = new ArrayList<>();
    	for (int i = 0; i < numNeut; i++) {
            int x = randint(0, xbin-1);
            int y = randint(0, ybin-1);
            int z = randint(0, zbin-1);
            DC n = new DC();
            list.add(n);
            grid[x][y][z].setCell(n);
    	}
    	return list;
	}
	
	public List initializeNK(Voxel[][][] grid, int xbin, int ybin, int zbin, int numNK) {
		List<NK> list = new ArrayList<>();
    	for (int i = 0; i < numNK; i++) {
            int x = randint(0, xbin-1);
            int y = randint(0, ybin-1);
            int z = randint(0, zbin-1);
            NK n = new NK();
            list.add(n);
            grid[x][y][z].setCell(n);
    	}
    	return list;
	}

	@Override
	public void initializeErytrocytes(Voxel[][][] grid, int xbin, int ybin, int zbin) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Afumigatus> infect(int numAspergillus, Voxel[][][] grid, int xbin, int ybin, int zbin, int status,
			double initIron, double sigma, boolean verbose) {
		return null;
	}
	
	public void initializeAlveole(int numAspergillus, Voxel[][][] grid, int xbin, int ybin, int zbin) {
		
	}
	
	public void initializeEndothelialCells(int numEC, Voxel[][][] grid, int xbin, int ybin, int zbin) {

		int zz = 0;
		int yy = 0;
		int xx = 0;
		
		int voxels = xbin*ybin*zbin;
		
		double p = 0.5*((double)numEC)/((double)voxels);
		
		int[][][] voxelMap = new int[xbin][ybin][zbin];
		int cellCount = 0;
		
		
		for(int x = 0; x < xbin; x++) 
			for(int y = 0; y < ybin; y++)
				for(int z = 0; z < zbin; z++) {
					EndothelialCells ec = new EndothelialCells();
					if(voxelMap[x][y][z] == 0) {
						grid[x][y][z].setCell(ec);
						voxelMap[x][y][z] = 1;
						cellCount++;
					}else if(Rand.getRand().randunif() < p) {
						grid[x][y][z].setCell(ec);
						voxelMap[x][y][z] = 1;
						cellCount++;
					} else {
						continue;
					}
					
					for(int j = 0; j < 3; j++) {
						int i = 0;
						do {
							xx = x;
							yy = y;
							zz = z;
							i = Rand.getRand().randunif(0, 5);
							if(i == 0)     zz = z - 1;
							else if(i == 1)yy = y - 1;
							else if(i == 2)xx = x - 1;
							else if(i == 3)yy = y + 1; 
							else if(i == 4)xx = x + 1;
							else if(i == 5)zz = z + 1;
							
						}while((xx < 0 || xx >= xbin || yy < 0 || yy >= ybin || zz < 0 || zz >= zbin));
						
						
						grid[xx][yy][zz].setCell(ec);
						voxelMap[xx][yy][zz] = 1;
					}
				}
	}

	@Override
	protected void setSecretionPhenotypes() {
		//IL10.getMolecule().addPhenotype(Phenotypes.ACTIVE);
		//IL10.getMolecule().addPhenotype(Phenotypes.MIX_ACTIVE);
		//IL10.getMolecule().addPhenotype(Phenotypes.ALT_ACTIVE);
		//IL10.getMolecule().addPhenotype(Phenotypes.INACTIVE);
		//MIP2.getMolecule().addPhenotype(Phenotypes.ACTIVE);
		//MIP2.getMolecule().addPhenotype(Phenotypes.IRF3);
		//TGFb.getMolecule().addPhenotype(Phenotypes.INACTIVE);
		//TGFb.getMolecule().addPhenotype(Phenotypes.AKT);
		//TNFa.getMolecule().addPhenotype(Phenotypes.ACTIVE);
		//TNFa.getMolecule().addPhenotype(Phenotypes.MIX_ACTIVE);
		//TNFa.getMolecule().addPhenotype(Phenotypes.NFkB);
		
		IFN1.getMolecule().addPhenotype(Phenotypes.IRF3);
		IFN1.getMolecule().addPhenotype(Phenotypes.ACTIVE);
		DAMP.getMolecule().addPhenotype(Phenotypes.NECROTIC);
		SAMP.getMolecule().addPhenotype(Phenotypes.APOPTOTIC);
		//MCP1.getMolecule().addPhenotype(Phenotypes.ACTIVE);
		//MCP1.getMolecule().addPhenotype(Phenotypes.IRF3);
		//VEGF.getMolecule().addPhenotype(Phenotypes.STAT3);
		//VEGF.getMolecule().addPhenotype(Phenotypes.OPEN);
		//IL6Complex.getMolecule().addPhenotype(Phenotypes.ANG_ACTIVE);
		//IL6.getMolecule().addPhenotype(Phenotypes.NFkB);
		//IL6.getMolecule().addPhenotype(Phenotypes.ANG_ACTIVE);
		//IL6.getMolecule().addPhenotype(Phenotypes.ACTIVE);
		H2O2.getMolecule().addPhenotype(Phenotypes.ACTIVE);
		Lactoferrin.getMolecule().addPhenotype(Phenotypes.ACTIVE);
		
		
	}

}
