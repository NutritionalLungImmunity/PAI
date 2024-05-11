package edu.uf.interactable;


import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.GridFactory;
import edu.uf.intracellularState.Phenotype;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class IL1 extends Molecule{
	public static final String NAME = "IL1";
	public static final int NUM_STATES = 1;
	
	private static IL1 molecule = null;   
	
	public boolean hasInteractWithLiver = false;
    
    private IL1(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
		this.setPhenotye(Phenotype.createPhenotype());
	}
    
    public static IL1 getMolecule(Diffuse diffuse) {
    	if(molecule == null) {
    		double[][][][] values = new double[NUM_STATES][GridFactory.getXbin()][GridFactory.getYbin()][GridFactory.getZbin()];
    		molecule = new IL1(values, diffuse); 
    	}
    	return molecule;
    }
    
    public static IL1 getMolecule() {
    	return getMolecule(null);
    }
    
    @Override
    public double getKd() {
    	return Constants.Kd_IL1;
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
    	if (interactable instanceof Macrophage) 
    		return Interactions.secrete((Macrophage) interactable, this, Constants.MA_IL1_QTTY, x, y, z, 0);
        
    	if (interactable instanceof PneumocyteII) 
    		return Interactions.secrete((PneumocyteII) interactable, this, Constants.MA_IL1_QTTY, x, y, z, 0);
        
        if (interactable instanceof Neutrophil) 
        	return Interactions.secrete((Neutrophil) interactable, this, Constants.MA_IL1_QTTY, x, y, z, 0);
        
        if(interactable instanceof Liver) { //TO DO!!!
        	if(hasInteractWithLiver)return true;
        	Liver liver = (Liver) interactable; 
        	for(int k = 0; k < Liver.ENSEMBLE_SIZE; k++) {
        		double globalQtty = this.getTotalMolecule(0)/(2*Constants.SPACE_VOL);
        		if (Util.activationFunction(globalQtty, Constants.Kd_IL1)) {
        			liver.getBooleanNetworkEnsemble()[k][Liver.IL1R] = 1;
        		}else {
        			liver.getBooleanNetworkEnsemble()[k][Liver.IL1R] = 0;
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
