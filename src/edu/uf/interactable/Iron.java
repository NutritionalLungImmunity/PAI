package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.GridFactory;
import edu.uf.intracellularState.IntracellularModel;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants;

public class Iron extends Molecule{

	public static final String NAME = "Iron";
	public static final int NUM_STATES = 1;
	
	private static Iron molecule = null; 
    
    private Iron(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static Iron getMolecule(Diffuse diffuse) {
    	if(molecule == null) {
    		double[][][][] values = new double[NUM_STATES][GridFactory.getXbin()][GridFactory.getYbin()][GridFactory.getZbin()];
    		molecule = new Iron(values, diffuse); 
    	}
    	return molecule;
    }
    
    public static Iron getMolecule() {
    	return getMolecule(null);
    }
    
    @Override
    public double getKd() {
    	return -1;
    }
    

    public int getIndex(String str) {
        return 0;
    }

    public void turnOver(int x, int y, int z) {
    	this.set(100*6.4e-18*0, 0, x, y, z);
    }
    
    /**
     * Disabled.
     */
    public void degrade() {}
    

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Macrophage) 
        	return Interactions.releaseIron((Macrophage) interactable, this, IntracellularModel.NECROTIC, x, y, z);
        
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
