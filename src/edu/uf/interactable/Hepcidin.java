package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.GridFactory;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants;

public class Hepcidin extends Molecule{

	public static final String NAME = "Hepcidin";
	public static final int NUM_STATES = 1;
	
	private static Hepcidin molecule = null;    
    
    private Hepcidin(double[][][][] qttys, Diffuse diffuse) {
    	super(qttys, diffuse, NAME);
	}
    
    public static Hepcidin getMolecule(Diffuse diffuse) {
    	if(molecule == null) {
    		double[][][][] values = new double[NUM_STATES][GridFactory.getXbin()][GridFactory.getYbin()][GridFactory.getZbin()];
    		molecule = new Hepcidin(values, diffuse); 
    	}
    	return molecule;
    }
    
    public static Hepcidin getMolecule() {
    	return getMolecule(null);
    }
    
    @Override
    public double getKd() {
    	return Constants.Kd_Hep;
    }

    public void turnOver(int x, int y, int z) {}
    public void degrade() {}//REVIEW

    public int getIndex(String str) {
        return 0;
    }

    

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Macrophage) 
        	return Interactions.bind((Leukocyte) interactable, this, x, y, z, 0);
        
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
