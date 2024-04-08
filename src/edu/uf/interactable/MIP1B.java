package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class MIP1B extends Chemokine{
    public static final String NAME = "MIP1B";
    public static final int NUM_STATES = 1;
    
    private static MIP1B molecule = null;    

    private MIP1B(double[][][][] qttys, Diffuse diffuse, int[] phenotypes) {
        super(qttys, diffuse, phenotypes);
        Macrophage.setChemokine(MIP1B.NAME);
    }
    
    public static MIP1B getMolecule(double[][][][] values, Diffuse diffuse, int[] phenotypes) {
    	if(molecule == null) {
    		molecule = new MIP1B(values, diffuse, phenotypes); 
    	}
    	return molecule;
    }
    
    public static MIP1B getMolecule() {
    	return molecule;
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
        	return Util.secrete((PneumocyteII) interactable, this, Constants.P_MIP1B_QTTY, x, y, z, 0);
        
        if (interactable instanceof Macrophage) 
        	return Util.secrete((Macrophage) interactable, this, Constants.MA_MIP1B_QTTY, x, y, z, 0);
        
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
