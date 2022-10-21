package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class MIP1B extends Chemokine{
    public static final String NAME = "MIP1B";
    public static final int NUM_STATES = 1;
    public static final int MOL_IDX = getReceptors();
    
    private static MIP1B molecule = null;    

    private MIP1B(double[][][][] qttys, Diffuse diffuse) {
        super(qttys, diffuse);
        Macrophage.setChemokine(MIP1B.NAME);
    }
    
    public static MIP1B getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new MIP1B(values, diffuse); 
    	}
    	return molecule;
    }
    
    public static MIP1B getMolecule() {
    	return molecule;
    }
    
    public void degrade() {
    	degrade(Constants.MIP1B_HALF_LIFE, 0);
    }

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Pneumocyte) {
            if (((Pneumocyte)interactable).inPhenotype(this.getSecretionPhenotype()))//#interactable.status == Phagocyte.ACTIVE:
            	this.inc(Constants.P_MIP1B_QTTY, 0, x, y, z);
            return true;
        }
        if (interactable instanceof Macrophage) {
            if (((Macrophage)interactable).inPhenotype(this.getSecretionPhenotype()))//#interactable.status == Phagocyte.ACTIVE:# and interactable.state == Neutrophil.INTERACTING:
            	this.inc(Constants.MA_MIP1B_QTTY, 0, x, y, z);
            return true;
        }
        return interactable.interact(this, x, y, z); 
    }

    public double chemoatract(int x, int y, int z) {
    	//System.out.println("MIP: " + this.values[0][x][y][z]);
        return Util.activationFunction(this.get(0, x, y, z), Constants.Kd_MIP1B, Constants.VOXEL_VOL, 1, Constants.STD_UNIT_T) + Constants.DRIFT_BIAS;
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
