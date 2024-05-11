package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.GridFactory;
import edu.uf.intracellularState.Phenotype;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class TGFb extends Molecule{

	public static final String NAME = "TGFb";
	public static final int NUM_STATES = 1;
	
	private static TGFb molecule = null;

    private TGFb(double[][][][] qtty, Diffuse diffuse) {
		super(qtty, diffuse);
		this.setPhenotye(Phenotype.createPhenotype());
	}
    
    public static TGFb getMolecule(Diffuse diffuse) {
    	if(molecule == null) {
    		double[][][][] values = new double[NUM_STATES][GridFactory.getXbin()][GridFactory.getYbin()][GridFactory.getZbin()];
    		molecule = new TGFb(values, diffuse); 
    	}
    	return molecule;
    }
    
    public static TGFb getMolecule() {
    	return getMolecule(null);
    }
    
    @Override
    public double getKd() {
    	return Constants.Kd_TGF;
    }
    
    public void degrade() {
    	degrade(Constants.TGF_HALF_LIFE, 0);
    }
        

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Macrophage) {
        	Cell cell = (Cell) interactable;
        	if (!cell.isDead()) { 
        		Interactions.secrete(cell, this, Constants.MA_TGF_QTTY, x, y, z, 0);
        		Interactions.bind(cell, this, x, y, z, 0);
            }
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
