package edu.uf.interactable.invitro;

import edu.uf.Diffusion.Diffuse;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Molecule;
import edu.uf.interactable.covid.Neutrophil;
import edu.uf.interactable.covid.Pneumocyte;
import edu.uf.intracellularState.EukaryoteSignalingNetwork;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class LPS extends Molecule {
	
	public static final String NAME = "LPS";
	public static final int NUM_STATES = 1;
	public static final int MOL_IDX = getReceptors();
	
	private static LPS molecule = null;
    
    protected LPS(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static LPS getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new LPS(values, diffuse);
    	}
    	return molecule;
    }
    
    public static Molecule getMolecule() {
    	return molecule;
    }

    public void turnOver(int x, int y, int z) { //REVIEW
        //degrade(Constants.DAMP_HALF_LIFE, x, y, z);
    }
    
    public void degrade() {} //REVIEW

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	//EukaryoteSignalingNetwork.LPS_e = LPS.MOL_IDX;
    	EukaryoteSignalingNetwork.TLR4_o.add(LPS.MOL_IDX);
        if(interactable instanceof Pneumocyte) {
        	Pneumocyte cell = (Pneumocyte) interactable;
        	if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_LPS, cell.getClock()))
	        	cell.bind(LPS.MOL_IDX);
	        return true;
        }
        if(interactable instanceof Neutrophil) {
        	Neutrophil cell = (Neutrophil) interactable;
	        if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_LPS, cell.getClock())) 
	        	cell.bind(LPS.MOL_IDX);
	        return true;
        }
        
        if(interactable instanceof Macrophage) {
        	Macrophage cell = (Macrophage) interactable;
	        if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_LPS, cell.getClock())) 
	        	cell.bind(LPS.MOL_IDX);
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
