package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.GridFactory;
import edu.uf.intracellularState.Phenotype;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class IL4 extends Molecule{
	public static final String NAME = "IL4";
	public static final int NUM_STATES = 1;
	private static IL4 molecule = null;   
    
    private IL4(double[][][][] qttys, Diffuse diffuse) {
    	super(qttys, diffuse, NAME);
		this.setPhenotye(Phenotype.createPhenotype());
	}
    public static IL4 getMolecule(Diffuse diffuse) {
    	if(molecule == null) {
    		double[][][][] values = new double[NUM_STATES][GridFactory.getXbin()][GridFactory.getYbin()][GridFactory.getZbin()];
    		molecule = new IL4(values, diffuse); 
    	}
    	return molecule;
    }
    public static IL4 getMolecule() {
    	return getMolecule(null);
    }
    
    @Override
    public double getKd() {
    	return Constants.Kd_IL4;
    }
    
    public void degrade() {
    	degrade(Constants.IL10_HALF_LIFE, 0);
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
        		if(cell.getBooleanNetwork().hasPhenotype(this.getPhenotype()))
        			this.inc(Constants.IL4_QTTY, 0, x, y, z);
        		cell.bind(this, Util.activationFunction5(this.get(0, x, y, z), this.getKd()));
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
