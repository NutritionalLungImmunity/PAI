package edu.uf.interactable.invitro;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.GridFactory;
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
    

    private IL8(double[][][][] qttys, Diffuse diffuse) {
    	super(qttys, diffuse, NAME);
        //Neutrophil.setChemokine(IL8.NAME);
    }

    public static IL8 getMolecule(Diffuse diffuse) {
    	if(molecule == null) {
    		double[][][][] values = new double[NUM_STATES][GridFactory.getXbin()][GridFactory.getYbin()][GridFactory.getZbin()];
    		molecule = new IL8(values, diffuse); 
    	}
    	return molecule;
    }
    
    public static IL8 getMolecule() {
    	return getMolecule(null);
    }
    
    @Override
    public double getKd() {
    	return Constants.Kd_IL8;
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
        	else */if (neutro.getBooleanNetwork().hasPhenotype(this.getPhenotype())){//#interactable.status == Phagocyte.ACTIVE and interactable.state == Neutrophil.INTERACTING:
        		this.inc(Constants.N_IL8_QTTY, 0, x, y, z);
                if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_IL8)){}
                    //neutro.interaction = 0
            //#if Util.activation_function(this.values[0], Constants.Kd_MIP2) > random():
            //#    this.pdec(0.5)
        	}
            return true;
        }
        if (interactable instanceof PneumocyteII) {
            if (((PneumocyteII)interactable).getBooleanNetwork().hasPhenotype(this.getPhenotype()))//#interactable.status == Phagocyte.ACTIVE:
            	this.inc(Constants.N_IL8_QTTY, 0, x, y, z);
            return true; 
        }
        //#if type(interactable) is Hepatocytes:
        //#    return False
        if (interactable instanceof Macrophage) {
        	Macrophage macro = (Macrophage) interactable;
        	if(macro.getBooleanNetwork().hasPhenotype(this.getPhenotype())) //#interactable.status == Phagocyte.ACTIVE:# and interactable.state == Neutrophil.INTERACTING:
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
