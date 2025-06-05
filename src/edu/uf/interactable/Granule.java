package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.GridFactory;
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants;

public class Granule extends Molecule{

	   
	public static final String NAME = "Granule";
	public static final int NUM_STATES = 1;
	
	private static Granule molecule = null;    
    
    private Granule(double[][][][] qttys, Diffuse diffuse) {
    	super(qttys, diffuse, NAME);
	}
    
    public static Granule getMolecule(Diffuse diffuse) {
    	if(molecule == null) {
    		double[][][][] values = new double[NUM_STATES][GridFactory.getXbin()][GridFactory.getYbin()][GridFactory.getZbin()];
    		molecule = new Granule(values, diffuse); 
    	}
    	return molecule;
    }
    
    public static Granule getMolecule() {
    	return getMolecule(null);
    }
    
    @Override
    public double getKd() {
    	return Constants.Kd_Granule;
    }
    
    public void degrade() {
    	degrade(Constants.Granule_HALF_LIFE, 0);
    }

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);// .values[0][x][y][z];
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	if (interactable instanceof Neutrophil)
    		return Interactions.secrete((Leukocyte) interactable, this, Constants.GRANULE_QTTY, x, y, z, 0);
        
        if(interactable instanceof Afumigatus) 
        	return Interactions.kill((PositionalInfectiousAgent) interactable, this, x, y, z);
        
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
