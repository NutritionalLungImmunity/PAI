package edu.uf.interactable.covid;

import java.util.List;

import edu.uf.Diffusion.Diffuse;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Molecule;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class SAMP extends Molecule{
	
	public static final String NAME = "SAMP";
	public static final int NUM_STATES = 1;
	
	private static SAMP molecule = null;
    
    protected SAMP(double[][][][] qttys, Diffuse diffuse, int[] phenotypes) {
		super(qttys, diffuse, phenotypes);
	}
    
    public static SAMP getMolecule(double[][][][] values, Diffuse diffuse, int[] phenotypes) {
    	if(molecule == null) {
    		molecule = new SAMP(values, diffuse, phenotypes);
    	}
    	return molecule;
    }
    
    public static Molecule getMolecule() {
    	return molecule;
    }

    public void turnOver(int x, int y, int z) { //REVIEW
        //degrade(Constants.SAMP_HALF_LIFE, x, y, z);
    }
    
    public void degrade() {}

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if(interactable instanceof Pneumocyte) {
        	Pneumocyte cell = (Pneumocyte) interactable;
	        if (cell.hasPhenotype(this.getPhenotype()))//# and interactable.state == Neutrophil.INTERACTING:
        		this.inc(Constants.SAMP_QTTY, 0, x, y, z);
	        return true;
        }
        /*if(interactable instanceof Neutrophil) {
        	Neutrophil cell = (Neutrophil) interactable;
	        if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_SAMP, cell.getClock())) {
	        	cell.bind(SAMP.MOL_IDX);
	        	this.dec(Constants.SAMP_QTTY, x, y, z);
	        }
	        if (this.getSecretionPhenotype().contains(cell.getPhenotype()))//# and interactable.state == Neutrophil.INTERACTING:
        		this.inc(Constants.SAMP_QTTY, 0, x, y, z);
	        return true;
        }*/
        
        if(interactable instanceof Macrophage) {
        	Macrophage cell = (Macrophage) interactable;
        	cell.bind(this, Util.activationFunction5(this.get(0, x, y, z), Constants.Kd_SAMP));
	        if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_SAMP)) {
	        	cell.bind(SAMP.MOL_IDX);
	        	this.dec(Constants.SAMP_QTTY, 0, x, y, z);
	        }
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
