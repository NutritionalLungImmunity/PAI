package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class Hepcidin extends Molecule{

	public static final String NAME = "Hepcidin";
	public static final int NUM_STATES = 1;
	
	private static Hepcidin molecule = null;    
    
    private Hepcidin(double[][][][] qttys, Diffuse diffuse, int[] phenotypes) {
		super(qttys, diffuse, phenotypes);
	}
    
    public static Hepcidin getMolecule(double[][][][] values, Diffuse diffuse, int[] phenotypes) {
    	if(molecule == null) {
    		molecule = new Hepcidin(values, diffuse, phenotypes);
    	}
    	return molecule;
    }
    
    public static Molecule getMolecule() {
    	return molecule;
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
        	return Util.bind((Macrophage) interactable, this, x, y, z, 0);
        
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
