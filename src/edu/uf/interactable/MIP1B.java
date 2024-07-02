package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.GridFactory;
import edu.uf.intracellularState.Phenotype;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class MIP1B extends Chemokine{
    public static final String NAME = "MIP1B";
    public static final int NUM_STATES = 1;
    
    private static MIP1B molecule = null; 

    private MIP1B(double[][][][] qttys, Diffuse diffuse) {
        super(qttys, diffuse);
        Macrophage.setChemokine(MIP1B.NAME);
        this.setPhenotye(Phenotype.createPhenotype());
    }
    
    public static MIP1B getMolecule(Diffuse diffuse) {
    	if(molecule == null) {
    		double[][][][] values = new double[NUM_STATES][GridFactory.getXbin()][GridFactory.getYbin()][GridFactory.getZbin()];
    		molecule = new MIP1B(values, diffuse); 
    	}
    	return molecule;
    }
    
    public static MIP1B getMolecule() {
    	return getMolecule(null);
    }
    
    @Override
    public double getKd() {
    	return Constants.Kd_MIP1B;
    }
    
    public void degrade() {
    	degrade(Constants.MIP1B_HALF_LIFE, 0);
    }

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof PneumocyteII) 
        	return Interactions.secrete((Cell) interactable, this, Constants.P_MIP1B_QTTY, x, y, z, 0);
        
        if (interactable instanceof Macrophage) 
        	return Interactions.secrete((Leukocyte) interactable, this, Constants.MA_MIP1B_QTTY, x, y, z, 0);
        
        return interactable.interact(this, x, y, z); 
    }

    public double chemoatract(int x, int y, int z) {
    	//System.out.println("MIP: " + this.values[0][x][y][z]);
        return Util.activationFunction(this.get(0, x, y, z), Constants.Kd_MIP1B, Constants.VOXEL_VOL, 1, Constants.STD_UNIT_T) + Constants.DRIFT_BIAS;
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
