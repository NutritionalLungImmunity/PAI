package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class TGFb extends Molecule{

	public static final String NAME = "TGFb";
	public static final int NUM_STATES = 1;
	
	private static TGFb molecule = null;
    

    private TGFb(double[][][][] qtty, Diffuse diffuse, int[] phenotypes) {
		super(qtty, diffuse, phenotypes);
	}
    
    public static TGFb getMolecule(double[][][][] values, Diffuse diffuse, int[] phenotypes) {
    	if(molecule == null) {
    		molecule = new TGFb(values, diffuse, phenotypes);
    	}
    	return molecule;
    }
    
    public static TGFb getMolecule() {
    	return molecule;
    }
    
    @Override
    public double getKd() {
    	return Constants.Kd_TGF;
    }
    
    public void degrade() {
    	degrade(Constants.TGF_HALF_LIFE, 0);
    }
        

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Macrophage) {
            Macrophage macro = (Macrophage) interactable; //EukaryoteSignalingNetwork
        	if (macro.hasPhenotype(this.getPhenotype())) 
        		this.inc(Constants.MA_TGF_QTTY, 0, x, y, z);
        	else if (!macro.isDead()) 
        		macro.bind(this, Util.activationFunction5(this.get(0, x, y, z), Constants.Kd_TGF));
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
