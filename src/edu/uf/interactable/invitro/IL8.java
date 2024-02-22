package edu.uf.interactable.invitro;

import java.util.List;

import edu.uf.Diffusion.Diffuse;
import edu.uf.interactable.Chemokine;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.PneumocyteII;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class IL8 extends Chemokine{

	public static final String NAME = "IL8";
    public static final int NUM_STATES = 1;
    
    private static IL8 molecule = null;
    

    private IL8(double[][][][] qttys, Diffuse diffuse, int[] phenotypes) {
    	super(qttys, diffuse, phenotypes);
        //Neutrophil.setChemokine(IL8.NAME);
    }

    public static IL8 getMolecule(double[][][][] values, Diffuse diffuse, int[] phenotypes) {
    	if(molecule == null) {
    		molecule = new IL8(values, diffuse, phenotypes);
    	}
    	return molecule;
    }
    
    public static IL8 getMolecule() {
    	return molecule;
    }
    
    public void degrade() {
    	degrade(Constants.IL8_HALF_LIFE, 0);
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
            /*EukaryoteSignalingNetwork.MIP2_e = MOL_IDX;
        	if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_MIP2, neutro.getClock())) 
                neutro.bind(MOL_IDX);
        	else */if (neutro.hasPhenotype(this.getPhenotype())){//#interactable.status == Phagocyte.ACTIVE and interactable.state == Neutrophil.INTERACTING:
        		this.inc(Constants.N_IL8_QTTY, 0, x, y, z);
                if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_IL8)){}
                    //neutro.interaction = 0
            //#if Util.activation_function(this.values[0], Constants.Kd_MIP2) > random():
            //#    this.pdec(0.5)
        	}
            return true;
        }
        if (interactable instanceof PneumocyteII) {
            if (((PneumocyteII)interactable).hasPhenotype(this.getPhenotype()))//#interactable.status == Phagocyte.ACTIVE:
            	this.inc(Constants.N_IL8_QTTY, 0, x, y, z);
            return true; 
        }
        //#if type(interactable) is Hepatocytes:
        //#    return False
        if (interactable instanceof Macrophage) {
        	Macrophage macro = (Macrophage) interactable;
        	if(macro.hasPhenotype(this.getPhenotype())) //#interactable.status == Phagocyte.ACTIVE:# and interactable.state == Neutrophil.INTERACTING:
        		this.inc(Constants.N_IL8_QTTY, 0, x, y, z);
            return true;
        }
        return interactable.interact(this, x, y, z); 
    }

    public double chemoatract(int x, int y, int z) {
        return Util.activationFunction(this.get(0, x, y, z), Constants.Kd_IL8, Constants.VOXEL_VOL, 1, Constants.STD_UNIT_T) + Constants.DRIFT_BIAS;
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
