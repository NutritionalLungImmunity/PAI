package edu.uf.main.initialize.bayesianLearning;

import java.util.List;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.Voxel;
import edu.uf.control.MultiThreadExec;
import edu.uf.interactable.Afumigatus;
import edu.uf.interactable.IL1;
import edu.uf.interactable.IL10;
import edu.uf.interactable.IL6;
import edu.uf.interactable.MCP1;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.TGFb;
import edu.uf.interactable.TNFa;
import edu.uf.interactable.covid.SAMP;
import edu.uf.interactable.invitro.BGlucan;
import edu.uf.interactable.invitro.GM_CSF;
import edu.uf.interactable.invitro.IL8;
import edu.uf.interactable.invitro.LPS;
import edu.uf.intracellularState.Phenotypes;
import edu.uf.main.initialize.InitializeBaseModel;

public class InitializeLearnCytokinesSecretion extends InitializeBaseModel{

	TNFa tnfa;
	IL10 il10;
	
	BGlucan bglucan;
	SAMP samp;
	LPS lps;
	
	@Override
	public void initializeMolecules(Voxel[][][] grid, int xbin, int ybin, int zbin, Diffuse diffuse, boolean verbose) {
		if(verbose) {
    		System.out.println("Initializing Iron, TAFC, Lactoferrin, Transferrin, Hepcidin, IL6, TNF-a, IL10, TGF-b, MIP2, MIP1-b");
    	}
		IL1 il1 = IL1.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	IL6 il6 = IL6.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	IL8 il8 = IL8.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	tnfa = TNFa.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	il10 = IL10.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	TGFb tgfb = TGFb.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	MIP2 mip2 = MIP2.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	MCP1 mcp1 = MCP1.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	
    	samp = SAMP.getMolecule(new double[1][xbin][ybin][zbin], null);
    	lps = LPS.getMolecule(new double[1][xbin][ybin][zbin], null);
    	bglucan = BGlucan.getMolecule(new double[1][xbin][ybin][zbin], null);
    	
    	
    	MultiThreadExec.setMolecule(il1);
    	MultiThreadExec.setMolecule(il6);
    	MultiThreadExec.setMolecule(il8);
    	MultiThreadExec.setMolecule(tnfa);
    	MultiThreadExec.setMolecule(il10);
    	MultiThreadExec.setMolecule(tgfb);
    	MultiThreadExec.setMolecule(mip2);
    	MultiThreadExec.setMolecule(mcp1);
    	
    	MultiThreadExec.setMolecule(samp);
    	MultiThreadExec.setMolecule(lps);
    	MultiThreadExec.setMolecule(bglucan);
    	
    	
    	
    	Voxel.setMolecule(IL1.NAME, il1);
    	Voxel.setMolecule(IL6.NAME, il6);
    	Voxel.setMolecule(IL8.NAME, il8);
    	Voxel.setMolecule(TNFa.NAME, tnfa);
    	Voxel.setMolecule(IL10.NAME, il10);
    	Voxel.setMolecule(TGFb.NAME, tgfb);
    	Voxel.setMolecule(MIP2.NAME, mip2);
    	Voxel.setMolecule(MCP1.NAME, mcp1);
    	
    	Voxel.setMolecule(SAMP.NAME, samp);
    	Voxel.setMolecule(LPS.NAME, lps);
    	Voxel.setMolecule(BGlucan.NAME, bglucan);
    	
    	this.setSecretionPhenotypes();
		
	}

	@Override
	public void initializeLiver(Voxel[][][] grid, int xbin, int ybin, int zbin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeErytrocytes(Voxel[][][] grid, int xbin, int ybin, int zbin) {
		// TODO Auto-generated method stub
		
	}
	
	public void initializeSAMP(int xbin, int ybin, int zbin, int qtty) {
		for(int i = 0; i < qtty; i++) {
			int x = randint(0, xbin-1);
            int y = randint(0, ybin-1);
            int z = randint(0, zbin-1);
            samp.inc(1.0, 0, x, y, z);
		}
	}
	
	public void removeSAMP(int xbin, int ybin, int zbin) {
		for(int x = 0; x < xbin; x++) 
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) 
        			samp.set(0, 0, x, y, z);
	}
	
	public void initializeTNF(int xbin, int ybin, int zbin, double qtty) {
    	for(int x = 0; x < xbin; x++) 
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) 
        			tnfa.set(qtty, 0, x, y, z);
    	
	}
	
	public void initializeIL10(int xbin, int ybin, int zbin, double qtty) {
    	for(int x = 0; x < xbin; x++) 
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) 
        			il10.set(qtty, 0, x, y, z);
    	
	}
	
	public void initializeBGlucan(int xbin, int ybin, int zbin, double qtty) {
    	for(int x = 0; x < xbin; x++) 
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) 
        			bglucan.set(qtty, 0, x, y, z);
	}
	
	public void initializeLPS(int xbin, int ybin, int zbin, double qtty) {
    	for(int x = 0; x < xbin; x++) 
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) 
        			lps.set(qtty, 0, x, y, z);
    	
	}

	@Override
	protected void setSecretionPhenotypes() {
		//IL10.getMolecule().addPhenotype(Phenotypes.ACTIVE); //
		IL10.getMolecule().addPhenotype(Phenotypes.MIX_ACTIVE);
		IL10.getMolecule().addPhenotype(Phenotypes.ALT_ACTIVE);
		IL10.getMolecule().addPhenotype(Phenotypes.INACTIVE);
		IL1.getMolecule().addPhenotype(Phenotypes.ACTIVE);
		IL1.getMolecule().addPhenotype(Phenotypes.MIX_ACTIVE);
		IL6.getMolecule().addPhenotype(Phenotypes.ACTIVE);
		IL6.getMolecule().addPhenotype(Phenotypes.MIX_ACTIVE);
		MCP1.getMolecule().addPhenotype(Phenotypes.ACTIVE);
		MIP2.getMolecule().addPhenotype(Phenotypes.ACTIVE);
		IL8.getMolecule().addPhenotype(Phenotypes.ACTIVE);
		TGFb.getMolecule().addPhenotype(Phenotypes.INACTIVE);
		TNFa.getMolecule().addPhenotype(Phenotypes.ACTIVE);
		TNFa.getMolecule().addPhenotype(Phenotypes.MIX_ACTIVE);
		
	}

}
