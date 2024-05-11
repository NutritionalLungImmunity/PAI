package edu.uf.interactable;

import java.util.HashMap;
import java.util.Map;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.GridFactory;
import edu.uf.intracellularState.Phenotype;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class Lactoferrin extends Molecule{
    

	public static final String NAME = "Lactoferrin";
	public static final int NUM_STATES = 3;
    private static final double THRESHOLD = Constants.K_M_TF_LAC * Constants.VOXEL_VOL / 1.0e6;
    
    private static Lactoferrin molecule = null; 

    private static final Map<String, Integer> INDEXES;
    
    static {
    	INDEXES = new HashMap<>();
    	INDEXES.put("Lactoferrin", 0);
    	INDEXES.put("LactoferrinFe", 1);
    	INDEXES.put("LactoferrinFe2", 2);
    }
    
    private Lactoferrin(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
		this.setPhenotye(Phenotype.createPhenotype());
	}
    
    public static Lactoferrin getMolecule(Diffuse diffuse) {
    	if(molecule == null) {
    		double[][][][] values = new double[NUM_STATES][GridFactory.getXbin()][GridFactory.getYbin()][GridFactory.getZbin()];
    		molecule = new Lactoferrin(values, diffuse); 
    	}
    	return molecule;
    }
    
    public static Lactoferrin getMolecule() {
    	return getMolecule(null);
    }
    
    @Override
    public double getKd() {
    	return -1;
    }
    
    public void degrade() {} //REVIEW

    public int getIndex(String str) {
        return Lactoferrin.INDEXES.get(str);
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    	this.totalMoleculesAux[1] = this.totalMoleculesAux[1] + this.get(1, x, y, z);
    	this.totalMoleculesAux[2] = this.totalMoleculesAux[2] + this.get(2, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Macrophage)
        	return Interactions.lactoferrinMacrophageUpatake((Macrophage) interactable, this, x, y, z);
        	
        if (interactable instanceof Neutrophil) 
        	return Interactions.lactoferrinDegranulation((Neutrophil) interactable, this, x, y, z);
        
        if (interactable instanceof Transferrin) 
           return Interactions.lactoferrinTransferrinChelation((Molecule) interactable, this, x, y, z);
        
        if (interactable instanceof Iron) 
        	return Interactions.transferrinIronChelation(this, (Molecule) interactable, x, y, z); //this method can be used for both lactoferrin and transferrin.
        
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
