package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.GridFactory;
import edu.uf.intracellularState.Phenotype;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class MIP2 extends Chemokine{
    public static final String NAME = "MIP2";
    public static final int NUM_STATES = 1;
    
    private static MIP2 molecule = null;
    

    private MIP2(double[][][][] qttys, Diffuse diffuse) {
    	super(qttys, diffuse, NAME);
        Neutrophil.setChemokine(MIP2.NAME);
        this.setPhenotye(Phenotype.createPhenotype());
    }

    public static MIP2 getMolecule(Diffuse diffuse) {
    	if(molecule == null) {
    		double[][][][] values = new double[NUM_STATES][GridFactory.getXbin()][GridFactory.getYbin()][GridFactory.getZbin()];
    		molecule = new MIP2(values, diffuse); 
    	}
    	return molecule;
    }
    
    public static MIP2 getMolecule() {
    	return getMolecule(null);
    }
    
    @Override
    public double getKd() {
    	return Constants.Kd_MIP2;
    }
    
    /*public void turnOver(int x, int y, int z) {
    	this.pdec(1-Constants.MIP2_HALF_LIFE, 0, x, y, z);
    }*/
    
    public void degrade() {
    	degrade(Constants.MIP2_HALF_LIFE, 0);
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    public int getIndex(String str) {
        return 0;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Neutrophil) {
        	Cell cell = (Cell) interactable;
        	if (!cell.isDead()) { 
        		Interactions.secrete(cell, this, Constants.N_MIP2_QTTY, x, y, z, 0);
        		Interactions.bind(cell, this, x, y, z, 0);
            }
        	return true;
        }
        
        if (interactable instanceof PneumocyteII) 
        	return Interactions.secrete((Cell) interactable, this, Constants.P_MIP2_QTTY, x, y, z, 0); 
        
        if (interactable instanceof Macrophage) return true;
        	//return Interactions.secrete((Leukocyte) interactable, this, Constants.MA_MIP2_QTTY, x, y, z, 0);
        
        return interactable.interact(this, x, y, z); 
    }

    public double chemoatract(int x, int y, int z) {
        return Util.activationFunction(this.get(0, x, y, z), Constants.Kd_MIP2, Constants.VOXEL_VOL, 1, Constants.STD_UNIT_T) + Constants.DRIFT_BIAS;
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
