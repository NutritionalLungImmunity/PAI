package edu.uf.main.initialize;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.Voxel;
import edu.uf.control.MultiThreadExec;
import edu.uf.interactable.Hepcidin;
import edu.uf.interactable.IL10;
import edu.uf.interactable.IL6;
import edu.uf.interactable.Iron;
import edu.uf.interactable.Lactoferrin;
import edu.uf.interactable.MIP1B;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.TAFC;
import edu.uf.interactable.TGFb;
import edu.uf.interactable.TNFa;
import edu.uf.interactable.Transferrin;
import edu.uf.interactable.covid.Neutrophil;
import edu.uf.utils.Constants;

public class InitializeInvitromacrophages extends InitializeBaseModel{

	public void initializeMolecules(Voxel[][][] grid, int xbin, int ybin, int zbin, Diffuse diffuse, boolean verbose) {
    	if(verbose) {
    		System.out.println("Initializing Iron, TAFC, Lactoferrin, Transferrin, Hepcidin, IL6, TNF-a, IL10, TGF-b, MIP2, MIP1-b");
    	}
    	Iron iron = Iron.getMolecule(new double[1][xbin][ybin][zbin], null, new int[] {});
    	TAFC tafc = TAFC.getMolecule(new double[2][xbin][ybin][zbin], diffuse, new int[] {});
    	Lactoferrin lactoferrin = Lactoferrin.getMolecule(new double[3][xbin][ybin][zbin], diffuse, new int[] {Neutrophil.ACTIVE});
    	Transferrin transferrin = Transferrin.getMolecule(new double[3][xbin][ybin][zbin], diffuse, new int[] {});
    	Hepcidin hepcidin = Hepcidin.getMolecule(new double[1][xbin][ybin][zbin], null, new int[] {});
    	IL6 il6 = IL6.getMolecule(new double[1][xbin][ybin][zbin], diffuse, new int[] {Macrophage.M1, Macrophage.M2B});
    	TNFa tnfa = TNFa.getMolecule(new double[1][xbin][ybin][zbin], diffuse, new int[] {Macrophage.M1, Macrophage.M2B});
    	IL10 il10 = IL10.getMolecule(new double[1][xbin][ybin][zbin], diffuse, new int[] {Macrophage.M1, Macrophage.M2A, Macrophage.M2B, Macrophage.M2C});
    	TGFb tgfb = TGFb.getMolecule(new double[1][xbin][ybin][zbin], diffuse, new int[] {Macrophage.M2C});
    	MIP2 mip2 = MIP2.getMolecule(new double[1][xbin][ybin][zbin], diffuse, new int[] {Macrophage.M1});
    	MIP1B mip1b = MIP1B.getMolecule(new double[1][xbin][ybin][zbin], diffuse, new int[] {Macrophage.M1});
    	//Granule gran = Granule.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	
    	
    	MultiThreadExec.setMolecule(iron);
    	MultiThreadExec.setMolecule(tafc);
    	MultiThreadExec.setMolecule(lactoferrin);
    	MultiThreadExec.setMolecule(transferrin);
    	MultiThreadExec.setMolecule(hepcidin);
    	MultiThreadExec.setMolecule(il6);
    	MultiThreadExec.setMolecule(tnfa);
    	MultiThreadExec.setMolecule(il10);
    	MultiThreadExec.setMolecule(tgfb);
    	MultiThreadExec.setMolecule(mip2);
    	MultiThreadExec.setMolecule(mip1b);
    	//MultiThreadExec.setMolecule(gran);
    	
    	
    	Voxel.setMolecule(Iron.NAME, iron, true);
    	Voxel.setMolecule(TAFC.NAME, tafc, true);
    	Voxel.setMolecule(Lactoferrin.NAME, lactoferrin);
    	for(int x = 0; x < xbin; x++) 
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) {
        			transferrin.set(Constants.DEFAULT_APOTF_CONCENTRATION, 0, x, y, z);
        			transferrin.set(Constants.DEFAULT_TFFE_CONCENTRATION, 0, x, y, z);
        			transferrin.set(Constants.DEFAULT_TFFE2_CONCENTRATION, 0, x, y, z);
        			hepcidin.set(Constants.THRESHOLD_HEP * Constants.VOXEL_VOL, 0, x, y, z);
        			tnfa.set(1E-21, 0, x, y, z);
        		}
    	Voxel.setMolecule(Transferrin.NAME, transferrin);
    	Voxel.setMolecule(Hepcidin.NAME, hepcidin);
    	Voxel.setMolecule(IL6.NAME, il6);
    	Voxel.setMolecule(TNFa.NAME, tnfa);
    	Voxel.setMolecule(IL10.NAME, il10);
    	Voxel.setMolecule(TGFb.NAME, tgfb);
    	Voxel.setMolecule(MIP2.NAME, mip2);
    	Voxel.setMolecule(MIP1B.NAME, mip1b);
    	//Voxel.setMolecule(Granule.NAME, gran, true);
    	
    	//this.setSecretionPhenotypes();
    }
	
}
