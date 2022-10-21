package edu.uf.interactable.invitro;

import edu.uf.Diffusion.Diffuse;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Molecule;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.TNFa;
import edu.uf.intracellularState.EukaryoteSignalingNetwork;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class GM_CSF extends Molecule{
	
	public static final String NAME = "GM_CSF";
	public static final int NUM_STATES = 1;
	public static final int MOL_IDX = getReceptors();
	
	private static GM_CSF molecule = null;
    
    private GM_CSF(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static GM_CSF getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new GM_CSF(values, diffuse);
    	}
    	return molecule;
    }
    
    public static GM_CSF getMolecule() {
    	return molecule;
    }
    
    public void turnOver(int x, int y, int z) {
        //degrade(Constants.TNF_HALF_LIFE, x, y, z); //Using TNF half-life, its ok!!!
        //degrade(Util.turnoverRate(1, 0, Constants.TURNOVER_RATE, Constants.REL_CYT_BIND_UNIT_T, 1), x, y, z);
    }
    
    public void degrade() {
    	degrade(Constants.GM_CSF_HALF_LIFE, 0);
    }

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	EukaryoteSignalingNetwork.GM_CSF_e = MOL_IDX;
        if (interactable instanceof Macrophage) {
            Macrophage macro = (Macrophage) interactable;
        	/*if (this.getSecretionPhenotype().contains(macro.getPhenotype()))//# and interactable.state == Neutrophil.INTERACTING:
        		this.inc(Constants.MA_TNF_QTTY, 0, x, y, z);*/
            if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_GM_CSF, macro.getClock()))
                macro.bind(MOL_IDX);
            return true;
        }
        /*if (interactable instanceof Neutrophil) { 
            Neutrophil neutro = (Neutrophil) interactable;
        	if (this.getSecretionPhenotype().contains(neutro.getPhenotype())) //# and interactable.state == Neutrophil.INTERACTING:
        		this.inc(Constants.N_TNF_QTTY, 0, x, y, z);
            if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_TNF, neutro.getClock()))
                neutro.bind(MOL_IDX);
            return true;
        }*/
        return interactable.interact(this, x, y, z);
    }

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public double getThreshold() {
		return 0;
	}

	@Override
	public int getNumState() {
		return NUM_STATES;
	}
}
