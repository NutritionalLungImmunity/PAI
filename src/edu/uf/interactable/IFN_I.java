package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class IFN_I extends Molecule{
	
	public static final String NAME = "IFNa/b";
	public static final int NUM_STATES = 1;
	
	private static IFN_I molecule = null;
    
    private IFN_I(double[][][][] qttys, Diffuse diffuse, int[] phenotypes) {
		super(qttys, diffuse, phenotypes);
	}
    
    public static IFN_I getMolecule(double[][][][] values, Diffuse diffuse, int[] phenotypes) {
    	if(molecule == null) {
    		molecule = new IFN_I(values, diffuse, phenotypes);
    	}
    	return molecule;
    }
    
    public static IFN_I getMolecule() {
    	return molecule;
    }
    
    public void degrade() {
    	degrade(Constants.TNF_HALF_LIFE, 0);
    }

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Macrophage) {
        	Macrophage macro = (Macrophage) interactable;
        	if(macro.hasPhenotype(this.getPhenotype()))
        		this.inc(Constants.MA_IFN_I_QTTY, 0, x, y, z);
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
		return 0;
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
