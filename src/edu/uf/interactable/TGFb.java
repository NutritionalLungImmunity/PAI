package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.intracellularState.EukaryoteSignalingNetwork;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class TGFb extends Molecule{

	public static final String NAME = "TGFb";
	public static final int NUM_STATES = 1;
	public static final int MOL_IDX = getReceptors();
	
	private static TGFb molecule = null;
    

    private TGFb(double[][][][] qtty, Diffuse diffuse) {
		super(qtty, diffuse);
	}
    
    public static TGFb getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new TGFb(values, diffuse);
    	}
    	return molecule;
    }
    
    public static TGFb getMolecule() {
    	return molecule;
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
            EukaryoteSignalingNetwork.TGFb_e = MOL_IDX;
        	if (macro.inPhenotype(this.getSecretionPhenotype())) 
        		this.inc(Constants.MA_TGF_QTTY, 0, x, y, z);
        	else if (!macro.isDead()) 
                if (Util.activationFunction(this.get(0, x, y, z), Constants.Kd_TGF)) 
                	macro.bind(MOL_IDX);
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
