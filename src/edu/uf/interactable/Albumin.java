package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.GridFactory;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants;

public class Albumin extends Molecule{
	
	public static final String NAME = "Albumin";
	public static final int NUM_STATES = 1;
	
	private static Albumin molecule = null;  
	
	//public static final double NON_HEMORHAGE = 0.0;
    
    private Albumin(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static Albumin getMolecule(Diffuse diffuse) {
    	if(molecule == null) {
    		double[][][][] values = new double[NUM_STATES][GridFactory.getXbin()][GridFactory.getYbin()][GridFactory.getZbin()];
    		molecule = new Albumin(values, diffuse); 
    	}
    	return molecule;
    }
    
    public static Albumin getMolecule() {
    	return getMolecule(null);
    }
    
    @Override
    public double getKd() {
    	return -1;
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
    	if (interactable instanceof Albumin)
    		return Interactions.set(this, (Cell) interactable, Constants.ALBUMIN_INIT_QTTY, x, y, z, 0);
    	
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
