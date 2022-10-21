package edu.uf.interactable.covid;

import edu.uf.Diffusion.Diffuse;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Molecule;
import edu.uf.intracellularState.EukaryoteSignalingNetwork;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class VEGF extends Molecule{
   
	public static final String NAME = "VEGF";
	public static final int NUM_STATES = 1;
	public static final int MOL_IDX = getReceptors();
	
	private static VEGF molecule = null;
    
    protected VEGF(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static VEGF getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new VEGF(values, diffuse);
    	}
    	return molecule;
    }
    
    public static Molecule getMolecule() {
    	return molecule;
    }
    
    public void degrade() {
    	degrade(Constants.VEGF_HALF_LIFE, 0);
    }

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if(interactable instanceof EndothelialCells) {
        	EndothelialCells cell = (EndothelialCells) interactable;
			EukaryoteSignalingNetwork.VEGF_e = VEGF.MOL_IDX;
	        if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_VEGF, cell.getClock()))
	        	cell.bind(VEGF.MOL_IDX);
	        if (cell.inPhenotype(this.getSecretionPhenotype()))//# and interactable.state == Neutrophil.INTERACTING:
        		this.inc(Constants.VEGF_QTTY, 0, x, y, z);
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
