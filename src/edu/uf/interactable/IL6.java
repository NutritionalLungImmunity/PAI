package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class IL6 extends Molecule{
    

	public static final String NAME = "IL6";
	public static final int NUM_STATES = 1;
	
	private static IL6 molecule = null;
    
    protected IL6(double[][][][] qttys, Diffuse diffuse, int[] phenotypes) {
		super(qttys, diffuse, phenotypes);
	}
    
    public static IL6 getMolecule(double[][][][] values, Diffuse diffuse, int[] phenotypes) {
    	if(molecule == null) {
    		molecule = new IL6(values, diffuse, phenotypes);
    	}
    	return molecule;
    }
    
    public static Molecule getMolecule() {
    	return molecule;
    }
    
    @Override
    public double getKd() {
    	return Constants.Kd_IL6;
    }
    
    public void degrade() {
    	degrade(Constants.IL6_HALF_LIFE, 0);
    }

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Macrophage) 
        	return Util.secrete((Macrophage) interactable, this, Constants.MA_IL6_QTTY, x, y, z, 0); 
        
        if (interactable instanceof Neutrophil) 
        	return Util.secrete((Neutrophil) interactable, this, Constants.MA_IL6_QTTY, x, y, z, 0); 
        
        return interactable.interact(this, x, y, z);
    }

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return NAME;
	}

	@Override
	public double getThreshold() {
		// TODO Auto-generated method stub
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
