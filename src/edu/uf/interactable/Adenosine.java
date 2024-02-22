package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;
import edu.uf.utils.Util;

public class Adenosine extends Molecule{

	public static final String NAME = "Adenosine";
	public static final int NUM_STATES = 1;
	
	private static Adenosine molecule = null;    
    
    private Adenosine(double[][][][] qttys, Diffuse diffuse, int[] phenotypes) {
		super(qttys, diffuse, phenotypes);
	}
    
    public static Adenosine getMolecule(double[][][][] values, Diffuse diffuse, int[] phenotypes) {
    	if(molecule == null) {
    		molecule = new Adenosine(values, diffuse, phenotypes);
    	}
    	return molecule;
    }
    
    public static Adenosine getMolecule() {
    	return molecule;
    }
    
    public void degrade() {
    	degrade(Constants.IL10_HALF_LIFE, 0); //CHANGE TO ADENOSINE HALF LIFE
    }

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	if (interactable instanceof PneumocyteII){
    		PneumocyteII pneum = (PneumocyteII) interactable;
    		if(pneum.isDead()) {
    			this.inc(1.0, 0, x, y, z); //CHANGE TO ADO_QTTY
    		}
            return true; 
        }
        if (interactable instanceof Macrophage){
        	Macrophage macro = (Macrophage) interactable;
        	if(macro.isDead()) {
    			this.inc(1.0, 0, x, y, z); //CHANGE TO ADO_QTTY
    		}else {
    			macro.bind(this, Util.activationFunction5(this.get(0, x, y, z), Constants.Kd_TNF)); //CHANGE TO ADO_Kd
    		}
            return true; 
        }
        if (interactable instanceof Neutrophil) {
        	Neutrophil neutr = (Neutrophil) interactable;
        	if(neutr.isDead()) {
    			this.inc(1.0, 0, x, y, z); //CHANGE TO ADO_QTTY
    		}else {
    			neutr.bind(this, Util.activationFunction5(this.get(0, x, y, z), Constants.Kd_TNF)); //CHANGE TO ADO_Kd
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
