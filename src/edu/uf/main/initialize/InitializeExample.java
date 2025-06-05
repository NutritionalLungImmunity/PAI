package edu.uf.main.initialize;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.GridFactory;
import edu.uf.compartments.Voxel;
import edu.uf.control.MultiThreadExec;
import edu.uf.interactable.EndothelialCell;
import edu.uf.interactable.IL10;
import edu.uf.interactable.IL4;
import edu.uf.interactable.IL6;
import edu.uf.interactable.Iron;
import edu.uf.interactable.Lactoferrin;
import edu.uf.interactable.Liver;
import edu.uf.interactable.MIP1B;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.PneumocyteI;
import edu.uf.interactable.TGFb;
import edu.uf.interactable.TNFa;
import edu.uf.interactable.Transferrin;
import edu.uf.interactable.Afumigatus.TAFC;
import edu.uf.intracellularState.ECIntracellularModel;
import edu.uf.intracellularState.IntracellularModelFactory;
import edu.uf.intracellularState.PneumocyteIEmptyModel;
import edu.uf.utils.Constants;

public class InitializeExample extends InitializeBaseModel{

    public void initializeMolecules(Diffuse diffuse, boolean verbose) { //verbose is not being used.
    	
    	int xbin = GridFactory.getXbin();
    	int ybin = GridFactory.getYbin();
    	int zbin = GridFactory.getZbin();
    	
    	Iron iron = Iron.getMolecule(null);
    	TAFC tafc = TAFC.getMolecule(diffuse);
    	Lactoferrin lactoferrin = Lactoferrin.getMolecule(diffuse);
    	Transferrin transferrin = Transferrin.getMolecule( diffuse);
    	TNFa tnfa = TNFa.getMolecule(diffuse);
    	IL10 il10 = IL10.getMolecule(diffuse);
    	TGFb tgfb = TGFb.getMolecule(diffuse);
    	MIP2 mip2 = MIP2.getMolecule(diffuse);
    	MIP1B mip1b = MIP1B.getMolecule(diffuse);
    	
    	IL4 il4 = IL4.getMolecule(diffuse);
    	
    	
    	MultiThreadExec.setMolecule(iron);
    	MultiThreadExec.setMolecule(tafc);
    	MultiThreadExec.setMolecule(lactoferrin);
    	MultiThreadExec.setMolecule(transferrin);
    	MultiThreadExec.setMolecule(tnfa);
    	MultiThreadExec.setMolecule(il10);
    	MultiThreadExec.setMolecule(tgfb);
    	MultiThreadExec.setMolecule(mip2);
    	MultiThreadExec.setMolecule(mip1b);
    	
    	MultiThreadExec.setMolecule(il4);
    	
    	
    	Voxel.setMolecule(Iron.NAME, iron, true, true);
    	Voxel.setMolecule(TAFC.NAME, tafc, true, true);
    	Voxel.setMolecule(Lactoferrin.NAME, lactoferrin, false, true);
    	for(int x = 0; x < xbin; x++) 
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) {
        			transferrin.set(Constants.DEFAULT_APOTF_CONCENTRATION, 0, x, y, z);
        			transferrin.set(Constants.DEFAULT_TFFE_CONCENTRATION, 1, x, y, z);
        			transferrin.set(Constants.DEFAULT_TFFE2_CONCENTRATION, 2, x, y, z);
        		}
    	Voxel.setMolecule(Transferrin.NAME, transferrin, false, true);
    	Voxel.setMolecule(TNFa.NAME, tnfa);
    	Voxel.setMolecule(IL10.NAME, il10);
    	Voxel.setMolecule(TGFb.NAME, tgfb);
    	Voxel.setMolecule(MIP2.NAME, mip2);
    	Voxel.setMolecule(MIP1B.NAME, mip1b);
    	
    	Voxel.setMolecule(IL4.NAME, il4);
    	
    }
    
    
    
    public void initializeEC() {
    	int xbin = GridFactory.getXbin();
    	int ybin = GridFactory.getYbin();
    	int zbin = GridFactory.getZbin();
    	Voxel[][][] grid = GridFactory.getGrid();
    	for(int x = 0; x < xbin; x++)
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) 
                    grid[x][y][z].setCell(new EndothelialCell(new ECIntracellularModel())); 
    }
    
    public void initializeTypeIPneumocytes(int numCells) { //numCells will not be used
    	int xbin = GridFactory.getXbin();
    	int ybin = GridFactory.getYbin();
    	int zbin = GridFactory.getZbin();
    	Voxel[][][] grid = GridFactory.getGrid();
    	for(int x = 0; x < xbin; x++)
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) 
                    grid[x][y][z].setCell(new PneumocyteI(new PneumocyteIEmptyModel())); 
    }
	
}
