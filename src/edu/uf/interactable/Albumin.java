package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class Albumin extends Molecule{
	
	public static final String NAME = "Albumin";
	public static final int NUM_STATES = 1;
	
	private static Albumin molecule = null;  
	
	//public static final double NON_HEMORHAGE = 0.0;
    
    private Albumin(double[][][][] qttys, Diffuse diffuse, int[] phenotypes) {
		super(qttys, diffuse, phenotypes);
	}
    
    public static Albumin getMolecule(double[][][][] values, Diffuse diffuse, int[] phenotypes) {
    	if(molecule == null) {
    		molecule = new Albumin(values, diffuse, phenotypes);
    	}
    	return molecule;
    }
    
    public static Albumin getMolecule() {
    	return molecule;
    }
    
    public void degrade() {
    	//degrade(Constants.IL10_HALF_LIFE, 0);
    }

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	if (interactable instanceof Albumin){
    		if(Blood.getBlood().hasBlood(x, y, z))
    			this.set(Constants.ALBUMIN_INIT_QTTY, 0, x, y, z);
    		return true;
    	}
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
	public boolean isTime() {
		return true;
	}
}
