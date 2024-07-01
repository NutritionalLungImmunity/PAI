package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.GridFactory;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;
import edu.uf.utils.Util;

public class Adenosine extends Molecule{

	public static final String NAME = "Adenosine";
	public static final int NUM_STATES = 1;
	
	private static Adenosine molecule = null;    
    
    private Adenosine(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static Adenosine getMolecule(Diffuse diffuse) {
    	if(molecule == null) {
    		double[][][][] values = new double[NUM_STATES][GridFactory.getXbin()][GridFactory.getYbin()][GridFactory.getZbin()];
    		molecule = new Adenosine(values, diffuse); 
    	}
    	return molecule;
    }
    
    public static Adenosine getMolecule() {
    	return getMolecule(null);
    }
    
    @Override
    public double getKd() {
    	return Constants.Kd_Adenosine;
    }
    
    public void degrade() {
    	degrade(Constants.IL10_HALF_LIFE, 0); //CHANGE TO ADENOSINE HALF LIFE
    }

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	if (interactable instanceof PneumocyteII)
    		return Interactions.secrete((PneumocyteII) interactable, this, 1.0, x, y, z, 0);
    	
        if (interactable instanceof Macrophage)
        	return Interactions.leukocuteDampInteraction((Macrophage) interactable, this, x, y, z, 1.0);
        	
        if (interactable instanceof Neutrophil) 
        	return Interactions.leukocuteDampInteraction((Neutrophil) interactable, this, x, y, z, 1.0);
        
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
