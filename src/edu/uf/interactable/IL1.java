package edu.uf.interactable;


import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class IL1 extends Molecule{
	public static final String NAME = "IL1";
	public static final int NUM_STATES = 1;
	
	private static IL1 molecule = null;   
	
	public boolean hasInteractWithLiver = false;
    
    private IL1(double[][][][] qttys, Diffuse diffuse, int[] phenotypes) {
		super(qttys, diffuse, phenotypes);
	}
    
    public static IL1 getMolecule(double[][][][] values, Diffuse diffuse, int[] phenotypes) {
    	if(molecule == null) {
    		molecule = new IL1(values, diffuse, phenotypes);
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
    	if (interactable instanceof Macrophage) {
            Macrophage macro = (Macrophage) interactable;
            if(macro.hasPhenotype(this.getPhenotype())) 
                this.inc(Constants.MA_IL1_QTTY, 0, x, y, z);
            macro.bind(this, Util.activationFunction5(this.get(0, x, y, z), Constants.Kd_IL1));
            return true;
        }
    	if (interactable instanceof PneumocyteII) {
    		PneumocyteII pneumo = (PneumocyteII) interactable;
        	if(pneumo.hasPhenotype(this.getPhenotype()))
                this.inc(Constants.MA_IL1_QTTY, 0, x, y, z);
        	pneumo.bind(this, Util.activationFunction5(this.get(0, x, y, z), Constants.Kd_IL1));
            return true;
        }
        if (interactable instanceof Neutrophil) {
            Neutrophil neutro = (Neutrophil) interactable;
        	if(neutro.hasPhenotype(this.getPhenotype()))
                this.inc(Constants.N_IL1_QTTY, 0, x, y, z);
        	neutro.bind(this, Util.activationFunction5(this.get(0, x, y, z), Constants.Kd_IL1));
            return true;
        }
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
