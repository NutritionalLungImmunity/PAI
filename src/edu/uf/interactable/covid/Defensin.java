package edu.uf.interactable.covid;

import java.util.List;

import edu.uf.Diffusion.Diffuse;
import edu.uf.interactable.Cell;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Molecule;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class Defensin extends Molecule{

	public static final String NAME = "Defensin";
	public static final int NUM_STATES = 1;
	
	private static Defensin molecule = null;
    
    protected Defensin(double[][][][] qttys, Diffuse diffuse, int[] phenotypes) {
		super(qttys, diffuse, phenotypes);
	}
    
    public static Defensin getMolecule(double[][][][] values, Diffuse diffuse, int[] phenotypes) {
    	if(molecule == null) {
    		molecule = new Defensin(values, diffuse, phenotypes);
    	}
    	return molecule;
    }
    
    public static Molecule getMolecule() {
    	return molecule;
    }
    
    public void turnOver(int x, int y, int z) {
    	this.pdec(1-Constants.SarsCoV2_HALF_LIFE, 0, x, y, z);
    	/*double voxelQtty = this.get(0, x, y, z);
    	double voxelConentration = voxelQtty / Constants.VOXEL_VOL;
		//double dsdt = Constants.TURNOVER_RATE * (voxelConentration * Constants.SERUM_VOL - Constants.DEFENSIN_RESTING_CONCENTRATION * Constants.VOXEL_VOL);
		double dvdt = 1e-11 * (Constants.DEFENSIN_RESTING_CONCENTRATION - voxelConentration);
		dvdt = dvdt + voxelQtty  < 0 ? -voxelQtty : dvdt;
		this.inc(dvdt, 0, x, y, z);
		//System.out.println(dvdt + " " + voxelQtty + " " + this.get(0, x, y, z) + " " + voxelConentration);*/
    }
    
    public void degrade() {}

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if(interactable instanceof Neutrophil) {
        	Neutrophil cell = (Neutrophil) interactable;
        	
	        if (cell.hasPhenotype(Neutrophil.ACTIVE) && !cell.isDeganuled()) {// && cell.getClock().toc(Cell.IT_CLOCK, Constants.CYT_BIND_T/Constants.TIME_STEP_SIZE)) {
	        	//System.out.println(Constants.N_DEFENSIN_QTTY*10);
	        	cell.degranuled();
        		this.inc(Constants.N_DEFENSIN_QTTY, 0, x, y, z);
	        }
	        return true;
        }
        if(interactable instanceof SarsCoV2) {
        	Molecule mol = (Molecule) interactable;
        	double d = this.get(0, x, y, z);
        	double s = mol.get(0, x, y, z);
        	double bound = Util.activationFunction(d, Constants.DEFENSIN_Kd, Constants.VOXEL_VOL, 1.0);
        	double qtty = bound*d > bound*s ? bound*s : bound*d;
        	//if(d > 0.0)bound = (s + d + Constants.DEFENSIN_Kd - Math.sqrt((s + d + Constants.DEFENSIN_Kd)*(s + d + Constants.DEFENSIN_Kd) - 4.0*s*d))/(2.0*d); //equation from 10.1016/j.jmb.2021.167225 
        	//System.out.println(s + " " + d + " " + bound);
        	this.dec(qtty, 0, x, y, z);
        	mol.dec(qtty, 0, x, y, z);
	        return true;
        }
        return interactable.interact(this, x, y, z);
    }

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public double getThreshold() {
		return -1;
	}

	@Override
	public int getNumState() {
		return NUM_STATES;
	}
	
	@Override
	public boolean isTime() {
		return true;
	}
	
}
