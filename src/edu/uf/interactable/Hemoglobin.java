package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class Hemoglobin extends Molecule{

	public static final String NAME = "Hemoglobin";
	public static final int NUM_STATES = 1;
	public static final int MOL_IDX = getReceptors();
	
	private static Hemoglobin molecule = null;    
    
    private Hemoglobin(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static Hemoglobin getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new Hemoglobin(values, diffuse);
    	}
    	return molecule;
    }
    
    public static Hemoglobin getMolecule() {
    	return molecule;
    }
    
    public void degrade() {}//REVIEW

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        /*if (interactable instanceof Liver){//# or type(interactable) is Neutrophil: 
        	Liver.getLiver().setHemoglobin(Liver.getLiver().getHemoglobin() + this.values[0][x][y][z]/2.0); //BASED ON IL6 ...
            return true;
        }*/
        if(interactable instanceof Erythrocyte) {
        	Erythrocyte erythrocyte = (Erythrocyte) interactable;
        	double qtty = erythrocyte.getBurst() * Constants.ERYTROCYTE_HEMOGLOBIN_CONCENTRATION;;
        	this.inc(qtty, 0, x, y, z);
        	erythrocyte.setBurst(0);
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

}
