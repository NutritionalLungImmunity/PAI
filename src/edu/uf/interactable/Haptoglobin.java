package edu.uf.interactable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class Haptoglobin extends Molecule{

	public static final String NAME = "Haptoglobin";
	public static final int NUM_STATES = 2;
	
	//private static double xSystem = 0.0;
	
	private static Haptoglobin molecule = null;  
	
	private static final Map<String, Integer> INDEXES;
    
    static {
    	INDEXES = new HashMap<>();
    	INDEXES.put("Hp", 0); 
    	INDEXES.put("HpHb", 1); 
    }
    
    private Haptoglobin(double[][][][] qttys, Diffuse diffuse, int[] phenotypes) {
		super(qttys, diffuse, phenotypes);
	}
    
    public static Haptoglobin getMolecule(double[][][][] values, Diffuse diffuse, int[] phenotypes) {
    	if(molecule == null) {
    		molecule = new Haptoglobin(values, diffuse, phenotypes);
    	}
    	return molecule;
    }
    
    public static Haptoglobin getMolecule() {
    	return molecule;
    }

    public void turnOver(int x, int y, int z) {} //Hp turnover done at interaction!
    public void degrade() {} //REVIEW

    public int getIndex(String str) {
        return Haptoglobin.INDEXES.get(str);
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);// .values[0][x][y][z];
    	this.totalMoleculesAux[1] = this.totalMoleculesAux[1] + this.get(1, x, y, z);//.values[1][x][y][z];
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	if(interactable instanceof Liver) {
    		Liver liver = (Liver) interactable;
    		double hpQtty = liver.getBooleanEnsemble()[Liver.HP]/((double)Liver.ENSEMBLE_SIZE);
        	//if(hpQtty < Liver.LIVER_BN_EPS)return true;
        	
        	hpQtty = hpQtty * Constants.L_HP_QTTY + (1 - hpQtty) * Constants.L_REST_HP_QTTY;
        	
        	double rate = Util.turnoverRate(this.get(0, x, y, z), hpQtty * Constants.VOXEL_VOL) - 1; //HEPCIDIN MODEL
        	
        	this.pinc(rate, 0, x, y, z);
        	
    		//Util.turnoverRate(this.values[0][x][y][z], rate);
    		return true; 
    	}
        if (interactable instanceof Macrophage){//# or type(interactable) is Neutrophil: 
        	((Macrophage)interactable).bind(this, Util.activationFunction5(this.get(1, x, y, z), Constants.Kd_HP));
            return true;
        }
        if(interactable instanceof Hemoglobin) {
        	Hemoglobin hb = (Hemoglobin) interactable;
        	double hphb = Util.michaelianKinetics(hb.get(0,x,y,z), this.get(0, x, y, z), Constants.KM_HP, Constants.STD_UNIT_T);
        	this.dec(hphb, 0, x, y, z);
        	this.inc(hphb, 1, x, y, z);
        	hb.dec(hphb, 0, x, y, z);
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
