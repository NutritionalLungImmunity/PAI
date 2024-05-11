package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.GridFactory;
import edu.uf.intracellularState.IntracellularModel;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class ROS extends Molecule{

	public static final String NAME = "ROS";
	public static final int NUM_STATES = 1;
	
	private static ROS molecule = null;
    
    protected ROS(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static ROS getMolecule(Diffuse diffuse) {
    	if(molecule == null) {
    		double[][][][] values = new double[NUM_STATES][GridFactory.getXbin()][GridFactory.getYbin()][GridFactory.getZbin()];
    		molecule = new ROS(values, diffuse); 
    	}
    	return molecule;
    }
    
    public static ROS getMolecule() {
    	return getMolecule(null);
    }
    
    @Override
    public double getKd() {
    	return -1;
    }
    
    public void turnOver(int x, int y, int z) {
    	this.pdec(1-Constants.H2O2_HALF_LIFE, 0, x, y, z);
    }
    
    public void degrade() {
    	degrade(Constants.H2O2_HALF_LIFE, 0);
    }

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if(interactable instanceof PneumocyteII) 
        	return Interactions.rosActivation((PneumocyteII) interactable, this, Cell.APOPTOTIC, x, y, z);

        if(interactable instanceof Neutrophil) 
        	return Interactions.secrete((Neutrophil) interactable, this, Constants.H2O2_QTTY, x, y, z, 0);
        
        if(interactable instanceof Macrophage) 
        	return Interactions.secrete((Macrophage) interactable, this, Constants.H2O2_QTTY, x, y, z, 0);
        
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
