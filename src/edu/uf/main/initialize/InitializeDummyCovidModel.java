/*package edu.uf.main.initialize;

import java.util.ArrayList;
import java.util.List;

import edu.uf.Diffusion.Diffuse;
import edu.uf.control.MultiThreadExec;
import edu.uf.geometry.Voxel;
import edu.uf.interactable.Afumigatus;
import edu.uf.interactable.IL10;
import edu.uf.interactable.IL6;
import edu.uf.interactable.MIP1B;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.covid.Pneumocyte;
import edu.uf.interactable.TGFb;
import edu.uf.interactable.TNFa;
import edu.uf.interactable.covid.IFN1;
import edu.uf.interactable.covid.SarsCoV2;

public class InitializeDummyCovidModel extends InitializeBaseModel{

	@Override
	public void initializeMolecules(Voxel[][][] grid, int xbin, int ybin, int zbin, Diffuse diffuse, boolean verbose) {
		if(verbose) {
    		System.out.println("Initializing Iron, TAFC, Lactoferrin, Transferrin, Hepcidin, IL6, TNF-a, IL10, TGF-b, MIP2, MIP1-b");
    	}
    	IL6 il6 = IL6.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	TNFa tnfa = TNFa.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	IL10 il10 = IL10.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	TGFb tgfb = TGFb.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	MIP2 mip2 = MIP2.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	MIP1B mip1b = MIP1B.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	SarsCoV2 covid = SarsCoV2.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	IFN1 ifn = IFN1.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	
    	
    	
    	MultiThreadExec.setMolecule(il6);
    	MultiThreadExec.setMolecule(tnfa);
    	MultiThreadExec.setMolecule(il10);
    	MultiThreadExec.setMolecule(tgfb);
    	MultiThreadExec.setMolecule(mip2);
    	MultiThreadExec.setMolecule(mip1b);
    	MultiThreadExec.setMolecule(covid);
    	MultiThreadExec.setMolecule(ifn);
    	
    	Voxel.setMolecule(IL6.NAME, il6);
    	Voxel.setMolecule(TNFa.NAME, tnfa);
    	Voxel.setMolecule(IL10.NAME, il10);
    	Voxel.setMolecule(TGFb.NAME, tgfb);
    	Voxel.setMolecule(MIP2.NAME, mip2);
    	Voxel.setMolecule(MIP1B.NAME, mip1b);
    	Voxel.setMolecule(SarsCoV2.NAME, covid);
    	Voxel.setMolecule(IFN1.NAME, ifn);
		
	}
	
	public List<Pneumocyte> initializePneumocytes(Voxel[][][] grid, int xbin, int ybin, int zbin, int numCells) {
        int k = 0;
        List<Pneumocyte> list = new ArrayList<>();
        for(int x = 0; x < xbin; x++)
        	for(int y = 0; y < xbin; y++)
        		for(int z = 0; z < xbin; z++) {
        			Pneumocyte p = new Pneumocyte();
                    grid[x][y][z].setCell(p);
                    list.add(p);
        		}
        return list;
    }

	@Override
	public void initializeLiver(Voxel[][][] grid, int xbin, int ybin, int zbin) {
		// TODO Auto-generated method stub
	}

	@Override
	public void initializeErytrocytes(Voxel[][][] grid, int xbin, int ybin, int zbin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Afumigatus> infect(int numAspergillus, Voxel[][][] grid, int xbin, int ybin, int zbin, int status,
			double initIron, double sigma, boolean verbose) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void infect(double qtty, Voxel[][][] grid, int x, int y, int z) {
		grid[x][y][z].getMolecule(SarsCoV2.NAME).set(qtty, x, y, z);
	}

}*/
