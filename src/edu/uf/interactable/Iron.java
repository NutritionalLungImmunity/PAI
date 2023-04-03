package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;

public class Iron extends Molecule{

	public static final String NAME = "Iron";
	public static final int NUM_STATES = 1;
	public static final int MOL_IDX = getReceptors();
	
	private static Iron molecule = null; 
    
    private Iron(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static Iron getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new Iron(values, diffuse);
    	}
    	return molecule;
    }
    
    public static Iron getMolecule() {
    	return molecule;
    }
    

    public int getIndex(String str) {
        return 0;
    }

    public void turnOver(int x, int y, int z) {}
    
    public void degrade() {}

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Macrophage) {
            Macrophage macro = (Macrophage) interactable;
        	if (macro.getStatus() == Macrophage.NECROTIC){
                this.inc(macro.getIronPool(), "Iron", x, y, z);
                macro.incIronPool(-macro.getIronPool());
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
