package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class IL23 extends Molecule{
	
	public static final String NAME = "IL23";
	public static final int NUM_STATES = 1;
	
	private static IL23 molecule = null;
    
    private IL23(double[][][][] qttys, Diffuse diffuse, int[] phenotypes) {
		super(qttys, diffuse, phenotypes);
	}
    
    public static IL23 getMolecule(double[][][][] values, Diffuse diffuse, int[] phenotypes) {
    	if(molecule == null) {
    		molecule = new IL23(values, diffuse, phenotypes);
    	}
    	return molecule;
    }
    
    public static IL23 getMolecule() {
    	return molecule;
    }
    
    @Override
    public double getKd() {
    	return Constants.Kd_IL23;
    }
    
    public void degrade() {
    	degrade(Constants.TNF_HALF_LIFE, 0);
    }

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof DeltaGammaT) 
        	return Util.bind((DeltaGammaT) interactable, this, x, y, z, 0);
        
        if (interactable instanceof Macrophage) 
        	return Util.secrete((Macrophage) interactable, this, Constants.MA_IL23_QTTY, x, y, z, 0);
        
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
	
	@Override
	public boolean isTime() {
		return true;
	}
	
}
