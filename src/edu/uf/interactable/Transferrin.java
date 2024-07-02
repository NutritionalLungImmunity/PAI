package edu.uf.interactable;
import java.util.HashMap;
import java.util.Map;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.GridFactory;
import edu.uf.intracellularState.AspergillusMacrophage;
import edu.uf.intracellularState.FMacrophageBooleanNetwork;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;


public class Transferrin extends Molecule{
    

	public static final String NAME = "Transferrin";
	public static final int NUM_STATES = 3;
    private static final double THRESHOLD = Constants.K_M_TF_TAFC *Constants.VOXEL_VOL / 1.0e6;
    
    private static Transferrin molecule = null;

    private static final Map<String, Integer> INDEXES;
    
    static {
    	INDEXES = new HashMap<>();
    	INDEXES.put("Tf", 0);
    	INDEXES.put("TfFe", 1); 
    	INDEXES.put("TfFe2", 2);
    }
    
    private Transferrin(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static Transferrin getMolecule(Diffuse diffuse) {
    	if(molecule == null) {
    		double[][][][] values = new double[NUM_STATES][GridFactory.getXbin()][GridFactory.getYbin()][GridFactory.getZbin()];
    		molecule = new Transferrin(values, diffuse); 
    	}
    	return molecule;
    }
    
    public static Transferrin getMolecule() {
    	return getMolecule(null);
    }
    
    @Override
    public double getKd() {
    	return Constants.Kd_TfR2;
    }
    
    public void turnOver(int x, int y, int z) {}
    
    /**
     * Disabled.
     */
    public void degrade() {}

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    	this.totalMoleculesAux[1] = this.totalMoleculesAux[1] + this.get(1, x, y, z);
    	this.totalMoleculesAux[2] = this.totalMoleculesAux[2] + this.get(2, x, y, z);
    }

    public int getIndex(String str) {
        return Transferrin.INDEXES.get(str);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Macrophage)
        	return Interactions.transferrinMacrophage((Leukocyte) interactable, this, AspergillusMacrophage._FPN, AspergillusMacrophage.M1, x, y, z);
        	
        if(interactable instanceof Iron)
        	return Interactions.transferrinIronChelation(this, (Molecule) interactable, x, y, z);
        
        return interactable.interact(this, x, y, z);
    }

	@Override
	public String getName() {
		return NAME;
	} 

	@Override
	public double getThreshold() {
		return THRESHOLD;
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
