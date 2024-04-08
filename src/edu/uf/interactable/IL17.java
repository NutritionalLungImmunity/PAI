package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class IL17 extends Molecule{
	
	public static final String NAME = "IL17";
	public static final int NUM_STATES = 1;
	
	private static IL17 molecule = null;
    
    private IL17(double[][][][] qttys, Diffuse diffuse, int[] phenotypes) {
		super(qttys, diffuse, phenotypes);
	}
    
    public static IL17 getMolecule(double[][][][] values, Diffuse diffuse, int[] phenotypes) {
    	if(molecule == null) {
    		molecule = new IL17(values, diffuse, phenotypes);
    	}
    	return molecule;
    }
    
    public static IL17 getMolecule() {
    	return molecule;
    }
    
    @Override
    public double getKd() {
    	return Constants.Kd_IL17;
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
        if (interactable instanceof Macrophage) 
        	return Util.bind((Macrophage) interactable, this, x, y, z, 0);
        
        if (interactable instanceof PneumocyteII) 
        	return Util.bind((PneumocyteII) interactable, this, x, y, z, 0);
        
        if (interactable instanceof DeltaGammaT) 
        	return Util.secrete((DeltaGammaT) interactable, this, Constants.IL17_QTTY, x, y, z, 0);
        
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
