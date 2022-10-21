package edu.uf.interactable.covid;

import edu.uf.Diffusion.Diffuse;
import edu.uf.interactable.Cell;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Molecule;
import edu.uf.intracellularState.EukaryoteSignalingNetwork;
import edu.uf.intracellularState.Phenotypes;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class H2O2 extends Molecule{

	public static final String NAME = "H2O2";
	public static final int NUM_STATES = 1;
	public static final int MOL_IDX = getReceptors();
	
	private static H2O2 molecule = null;
    
    protected H2O2(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static H2O2 getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new H2O2(values, diffuse);
    	}
    	return molecule;
    }
    
    public static H2O2 getMolecule() {
    	return molecule;
    }
    
    public void turnOver(int x, int y, int z) {
    	this.pdec(1-Constants.H2O2_HALF_LIFE, 0, x, y, z);
    }
    
    public void degrade() {
    	degrade(Constants.H2O2_HALF_LIFE, 0);
    }

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if(interactable instanceof Pneumocyte) {
        	Pneumocyte cell = (Pneumocyte) interactable;
	        if (Util.activationFunction(this.get(0, x, y, z)*this.get(0, x, y, z), Constants.Kd_H2O2*Constants.Kd_H2O2, Constants.VOXEL_VOL*Constants.VOXEL_VOL, cell.getClock())) {
	        	//System.out.println(this.get(0, x, y, z)/Constants.VOXEL_VOL + " " + Constants.Kd_H2O2);
	        	cell.addPhenotype(Phenotypes.APOPTOTIC);
	        }
	        return true;
        }
        if(interactable instanceof Neutrophil) {
        	Neutrophil cell = (Neutrophil) interactable;
	        if (cell.inPhenotype(this.getSecretionPhenotype()))//# and interactable.state == Neutrophil.INTERACTING:
        		this.inc(Constants.H2O2_QTTY, 0, x, y, z);
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
	
}
