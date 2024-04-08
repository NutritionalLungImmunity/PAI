package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class IFN_II extends Molecule{
	
	public static final String NAME = "IFNg";
	public static final int NUM_STATES = 1;
	
	private static IFN_II molecule = null;
    
    private IFN_II(double[][][][] qttys, Diffuse diffuse, int[] phenotypes) {
		super(qttys, diffuse, phenotypes);
	}
    
    public static IFN_II getMolecule(double[][][][] values, Diffuse diffuse, int[] phenotypes) {
    	if(molecule == null) {
    		molecule = new IFN_II(values, diffuse, phenotypes);
    	}
    	return molecule;
    }
    
    public static IFN_II getMolecule() {
    	return molecule;
    }
    
    @Override
    public double getKd() {
    	return Constants.Kd_IFNg;
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
        if (interactable instanceof NK) 
        	return Util.secrete((NK) interactable, this, Constants.IFN_II_QTTY, x, y, z, 0);
        
        if (interactable instanceof Macrophage) 
        	return Util.bind((Macrophage) interactable, this, x, y, z, 0);
        
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
