package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.intracellularState.EukaryoteSignalingNetwork;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class MIP2 extends Chemokine{
    public static final String NAME = "MIP2";
    public static final int NUM_STATES = 1;
    public static final int MOL_IDX = getReceptors();
    
    private static MIP2 molecule = null;
    

    private MIP2(double[][][][] qttys, Diffuse diffuse) {
    	super(qttys, diffuse);
        Neutrophil.setChemokine(MIP2.NAME);
    }

    public static MIP2 getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new MIP2(values, diffuse);
    	}
    	return molecule;
    }
    
    public static MIP2 getMolecule() {
    	return molecule;
    }
    
    public void turnOver(int x, int y, int z) {
    	this.pdec(1-Constants.MIP2_HALF_LIFE, 0, x, y, z);
    }
    
    public void degrade() {
    	degrade(Constants.MIP2_HALF_LIFE, 0);
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    public int getIndex(String str) {
        return 0;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Neutrophil) {
            Neutrophil neutro = (Neutrophil) interactable;
            EukaryoteSignalingNetwork.MIP2_e = MOL_IDX;
        	if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_MIP2, neutro.getClock())) 
                neutro.bind(MOL_IDX);
        	else if (neutro.inPhenotype(this.getSecretionPhenotype())){//#interactable.status == Phagocyte.ACTIVE and interactable.state == Neutrophil.INTERACTING:
        		this.inc(Constants.N_MIP2_QTTY, 0, x, y, z);
                if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_MIP2, neutro.getClock())){}
                    //neutro.interaction = 0
            //#if Util.activation_function(this.values[0], Constants.Kd_MIP2) > random():
            //#    this.pdec(0.5)
        	}
            return true;
        }
        if (interactable instanceof Pneumocyte) {
            if (((Pneumocyte)interactable).inPhenotype(this.getSecretionPhenotype()))//#interactable.status == Phagocyte.ACTIVE:
            	this.inc(Constants.P_MIP2_QTTY, 0, x, y, z);
            return true; 
        }
        //#if type(interactable) is Hepatocytes:
        //#    return False
        if (interactable instanceof Macrophage) {
        	Macrophage macro = (Macrophage) interactable;
        	if (macro.inPhenotype(this.getSecretionPhenotype())) //#interactable.status == Phagocyte.ACTIVE:# and interactable.state == Neutrophil.INTERACTING:
        		this.inc(Constants.MA_MIP2_QTTY, 0, x, y, z);
            return true;
        }
        return interactable.interact(this, x, y, z); 
    }

    public double chemoatract(int x, int y, int z) {
        return Util.activationFunction(this.get(0, x, y, z), Constants.Kd_MIP2, Constants.VOXEL_VOL, 1, Constants.STD_UNIT_T) + Constants.DRIFT_BIAS;
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
