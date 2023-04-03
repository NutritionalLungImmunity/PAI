package edu.uf.interactable.covid;

import edu.uf.Diffusion.Diffuse;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Molecule;
import edu.uf.intracellularState.EukaryoteSignalingNetwork;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class DAMP extends Molecule{
	public static final String NAME = "DAMP";
	public static final int NUM_STATES = 1;
	public static final int MOL_IDX = getReceptors();
	
	private static DAMP molecule = null;
    
    protected DAMP(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static DAMP getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new DAMP(values, diffuse);
    	}
    	return molecule;
    }
    
    public static Molecule getMolecule() {
    	return molecule;
    }

    public void turnOver(int x, int y, int z) { //REVIEW
        //degrade(Constants.DAMP_HALF_LIFE, x, y, z);
    }
    
    public void degrade() {}

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	EukaryoteSignalingNetwork.TLR4_o.add(DAMP.MOL_IDX);
        if(interactable instanceof Pneumocyte) {
        	Pneumocyte cell = (Pneumocyte) interactable;
	        if (cell.inPhenotype(this.getSecretionPhenotype()))//# and interactable.state == Neutrophil.INTERACTING:
        		this.inc(Constants.DAMP_QTTY, 0, x, y, z);
	        return true;
        }
        if(interactable instanceof Neutrophil) {
        	Neutrophil cell = (Neutrophil) interactable;
	        if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_DAMP)) {
	        	cell.bind(DAMP.MOL_IDX);
	        	this.dec(Constants.DAMP_QTTY, 0, x, y, z);
	        }
	        if (cell.inPhenotype(this.getSecretionPhenotype()))//# and interactable.state == Neutrophil.INTERACTING:
        		this.inc(Constants.DAMP_QTTY, 0, x, y, z);
	        return true;
        }
        
        if(interactable instanceof Macrophage) {
        	Macrophage cell = (Macrophage) interactable;
	        if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_DAMP)) {
	        	cell.bind(DAMP.MOL_IDX);
	        	this.dec(Constants.DAMP_QTTY, 0, x, y, z);
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
