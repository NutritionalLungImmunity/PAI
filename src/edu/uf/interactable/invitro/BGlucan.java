package edu.uf.interactable.invitro;

import java.util.List;

import edu.uf.Diffusion.Diffuse;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Molecule;
//import edu.uf.interactable.covid.DAMP;
import edu.uf.interactable.Neutrophil;  //WARNING!! It uses to be .covid.Neutrophil
import edu.uf.interactable.PneumocyteII;  //WARNING!! It uses to be .covid.Pneumocyte
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class BGlucan extends Molecule{
	
	public static final String NAME = "BGlucan";
	public static final int NUM_STATES = 1;
	
	private static BGlucan molecule = null;
    
    protected BGlucan(double[][][][] qttys, Diffuse diffuse, int[] phenotypes) {
		super(qttys, diffuse, phenotypes);
	}
    
    public static BGlucan getMolecule(double[][][][] values, Diffuse diffuse, int[] phenotypes) {
    	if(molecule == null) {
    		molecule = new BGlucan(values, diffuse, phenotypes);
    	}
    	return molecule;
    }
    
    public static Molecule getMolecule() {
    	return molecule;
    }
    
    @Override
    public double getKd() {
    	return Constants.Kd_BGLUCAN;
    }

    public void turnOver(int x, int y, int z) { //REVIEW
        //degrade(Constants.DAMP_HALF_LIFE, x, y, z);
    }
    
    public void degrade() {} //REVIEW

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if(interactable instanceof PneumocyteII) {
        	PneumocyteII cell = (PneumocyteII) interactable;
        	cell.bind(this, Util.activationFunction5(this.get(0, x, y, z), Constants.Kd_BGLUCAN));
	        return true;
        }
        if(interactable instanceof Neutrophil) {
        	Neutrophil cell = (Neutrophil) interactable;
        	cell.bind(this, Util.activationFunction5(this.get(0, x, y, z), Constants.Kd_BGLUCAN));
	        return true;
        }
        
        if(interactable instanceof Macrophage) {
        	Macrophage cell = (Macrophage) interactable;
        	cell.bind(this, Util.activationFunction5(this.get(0, x, y, z), Constants.Kd_BGLUCAN));
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
