package edu.uf.interactable.covid;

import edu.uf.Diffusion.Diffuse;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Molecule;
import edu.uf.intracellularState.EukaryoteSignalingNetwork;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class IFN1 extends Molecule{
	public static final String NAME = "IFN_I";
	public static final int NUM_STATES = 1;
	public static final int MOL_IDX = getReceptors();
	
	private static IFN1 molecule = null;
    
    protected IFN1(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static IFN1 getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new IFN1(values, diffuse);
    	}
    	return molecule;
    }
    
    public static Molecule getMolecule() {
    	return molecule;
    }
    
    public void degrade() {
    	degrade(Constants.IFN1_HALF_LIFE, 0);
    }

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	EukaryoteSignalingNetwork.IFNG_e = IFN1.MOL_IDX;
        if(interactable instanceof Pneumocyte) {
        	Pneumocyte cell = (Pneumocyte) interactable;
	        if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_IFNG))
	        	cell.bind(IFN1.MOL_IDX);
	        if (cell.inPhenotype(this.getSecretionPhenotype()))//# and interactable.state == Neutrophil.INTERACTING:
        		this.inc(Constants.MA_IFN_QTTY, 0, x, y, z);
	        return true;
        }
        if(interactable instanceof Macrophage) {
        	Macrophage cell = (Macrophage) interactable;
	        if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_IFNG))
	        	cell.bind(IFN1.MOL_IDX);
	        if (cell.inPhenotype(this.getSecretionPhenotype()))//# and interactable.state == Neutrophil.INTERACTING:
        		this.inc(Constants.MA_IFN_QTTY, 0, x, y, z);
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
