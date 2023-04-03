package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.intracellularState.EukaryoteSignalingNetwork;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class IL1 extends Molecule{
	public static final String NAME = "IL1";
	public static final int NUM_STATES = 1;
	public static final int MOL_IDX = getReceptors();
	
	private static IL1 molecule = null;   
	
	public boolean hasInteractWithLiver = false;
    
    private IL1(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static IL1 getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new IL1(values, diffuse);
    	}
    	return molecule;
    }
    
    public static IL1 getMolecule() {
    	return molecule;
    }
    
    public void degrade() {
    	degrade(Constants.IL1_HALF_LIFE, 0);
    }

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	EukaryoteSignalingNetwork.IL1B_e = MOL_IDX;
    	if (interactable instanceof Macrophage) {
            Macrophage macro = (Macrophage) interactable;
            if(macro.inPhenotype(this.getSecretionPhenotype())) 
                this.inc(Constants.MA_IL1_QTTY, 0, x, y, z);
            if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_IL1))
            	((Macrophage)interactable).bind(MOL_IDX);
            return true;
        }
    	if (interactable instanceof Pneumocyte) {
    		Pneumocyte pneumo = (Pneumocyte) interactable;
        	if(pneumo.inPhenotype(this.getSecretionPhenotype()))
                this.inc(Constants.MA_IL1_QTTY, 0, x, y, z);
            if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_IL1))
                ((Pneumocyte)interactable).bind(MOL_IDX);
            return true;
        }
        if (interactable instanceof Neutrophil) {
            Neutrophil neutro = (Neutrophil) interactable;
        	if(neutro.inPhenotype(this.getSecretionPhenotype()))
                this.inc(Constants.N_IL1_QTTY, 0, x, y, z);
            if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_IL1)) 
                ((Neutrophil)interactable).bind(MOL_IDX);
            return true;
        }
        if(interactable instanceof Liver) { //TO DO!!!
        	if(hasInteractWithLiver)return true;
        	Liver liver = (Liver) interactable; 
        	for(int k = 0; k < Liver.ENSEMBLE_SIZE; k++) {
        		double globalQtty = this.getTotalMolecule(0)/(2*Constants.SPACE_VOL);
        		if (Util.activationFunction(globalQtty, Constants.Kd_IL1)) {
        			liver.getBooleanNetwork()[k][Liver.IL1R] = 1;
        		}else {
        			liver.getBooleanNetwork()[k][Liver.IL1R] = 0;
        		}
        	}
        }
        hasInteractWithLiver = true; 
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
	public void resetCount() {
		super.resetCount();
		hasInteractWithLiver = false;
	}
	
	@Override
	public boolean isTime() {
		return true;
	}
	
}
