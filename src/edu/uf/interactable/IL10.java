package edu.uf.interactable;

import java.util.List;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class IL10 extends Molecule{
    
	public static final String NAME = "IL10";
	public static final int NUM_STATES = 1;
	
	private static IL10 molecule = null;    
    
    private IL10(double[][][][] qttys, Diffuse diffuse, int[] phenotypes) {
		super(qttys, diffuse, phenotypes);
	}
    
    public static IL10 getMolecule(double[][][][] values, Diffuse diffuse, int[] phenotypes) {
    	if(molecule == null) {
    		molecule = new IL10(values, diffuse, phenotypes);
    	}
    	return molecule;
    }
    
    public static IL10 getMolecule() {
    	return molecule;
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
        if (interactable instanceof Macrophage){//# or type(interactable) is Neutrophil: 
        	Macrophage macro = (Macrophage) interactable;
        	if (macro.hasPhenotype(this.getPhenotype()))//(macro.getStatus() == Phagocyte.ACTIVE && macro.getState() == Neutrophil.INTERACTING) 
        		this.inc(Constants.MA_IL10_QTTY, 0, x, y, z);
        		//macro.secrete(NAME);
            if (!macro.isDead()) { 
            	macro.bind(this, Util.activationFunction5(this.get(0, x, y, z), Constants.Kd_IL10));
                	//macro.setStatus(macro.getStatus() != Phagocyte.INACTIVE ? Phagocyte.INACTIVATING : Phagocyte.INACTIVE);
                	//macro.interation = 0;
            }
            return true; 
        }
        //System.out.println(interactable);
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
