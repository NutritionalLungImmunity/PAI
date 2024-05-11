package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.GridFactory;
import edu.uf.intracellularState.Phenotype;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class MCP1 extends Chemokine{

	public static final String NAME = "MCP1";
    public static final int NUM_STATES = 1;
    
    private static MCP1 molecule = null; 

    private MCP1(double[][][][] qttys, Diffuse diffuse) {
        super(qttys, diffuse);
        Macrophage.setChemokine(MCP1.NAME);
        this.setPhenotye(Phenotype.createPhenotype());
    }
    
    public static MCP1 getMolecule(Diffuse diffuse) {
    	if(molecule == null) {
    		double[][][][] values = new double[NUM_STATES][GridFactory.getXbin()][GridFactory.getYbin()][GridFactory.getZbin()];
    		molecule = new MCP1(values, diffuse); 
    	}
    	return molecule;
    }
    
    public static MCP1 getMolecule() {
    	return getMolecule(null);
    }
    
    @Override
    public double getKd() {
    	return Constants.Kd_MCP1;
    }
    
    public void turnOver(int x, int y, int z) {
    	this.pdec(1-Constants.MCP1_HALF_LIFE, 0, x, y, z);
    }
    
    
    
    public void degrade() {
    	degrade(Constants.MCP1_HALF_LIFE, 0);
    }

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof PneumocyteII) 
        	return Interactions.secrete((PneumocyteII) interactable, this, Constants.P_MCP1_QTTY, x, y, z, 0);
        
        if (interactable instanceof Macrophage) 
        	return Interactions.secrete((Macrophage) interactable, this, Constants.MA_MCP1_QTTY, x, y, z, 0);
        
        return interactable.interact(this, x, y, z); 
    }

    public double chemoatract(int x, int y, int z) {
        return Util.activationFunction(this.get(0, x, y, z), Constants.Kd_MCP1, Constants.VOXEL_VOL, 1, Constants.STD_UNIT_T) + Constants.DRIFT_BIAS;
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
