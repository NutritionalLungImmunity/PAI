package edu.uf.interactable.covid;

import java.util.HashMap;
import java.util.Map;

import edu.uf.Diffusion.Diffuse;
import edu.uf.interactable.IL6;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Molecule;
import edu.uf.intracellularState.EukaryoteSignalingNetwork;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class IL6Complex extends Molecule{
   

	public static final String NAME = "IL6Complex";
	public static final int NUM_STATES = 3;
	public static final int MOL_IDX = getReceptors();
	
	private static IL6Complex molecule = null;
	
	private static final Map<String, Integer> INDEXES;
    
    static {
    	INDEXES = new HashMap<>();
    	INDEXES.put("IL6", 0); 
    	INDEXES.put("sIL6R", 1);
    	INDEXES.put("IL6_sIL6R", 2);
    }
    
    protected IL6Complex(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static IL6Complex getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new IL6Complex(values, diffuse);
    	}
    	return molecule;
    }
    
    public static Molecule getMolecule() {
    	return molecule;
    }
    
    public void degrade() {
    	degrade(Constants.IL6_HALF_LIFE, 0);
    }

    public int getIndex(String str) {
        return IL6Complex.INDEXES.get(str);
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    	this.totalMoleculesAux[1] = this.totalMoleculesAux[1] + this.get(1, x, y, z);
    	this.totalMoleculesAux[2] = this.totalMoleculesAux[2] + this.get(2, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if(interactable instanceof EndothelialCells) {
        	EndothelialCells cell = (EndothelialCells) interactable;
			EukaryoteSignalingNetwork.IL6_e = IL6Complex.MOL_IDX;
	        if (Util.activationFunction(this.get(2, x, y, z), Constants.Kd_sIL6R, cell.getClock())) 
	        	cell.bind(IL6Complex.MOL_IDX);
	        return true;
        }
        if (interactable instanceof Macrophage) {
            Macrophage macro = (Macrophage) interactable;
        	if (macro.inPhenotype(IL6.getMolecule().getSecretionPhenotype()))//# and interactable.state == Neutrophil.INTERACTING:
        		this.inc(Constants.MA_IL6_QTTY, 0, x, y, z);
            return true; 
        }
        if (interactable instanceof IL6Complex) {
        	double totalIL6 = this.get(0, x, y, z) + this.get(2, x, y, z);
        	double totalsIL6R = this.get(1, x, y, z) + this.get(2, x, y, z);
        	double il6Complex = Util.activationFunction(totalIL6, Constants.Kd_IL6, Constants.VOXEL_VOL, 1.0) * totalsIL6R;
        	il6Complex = il6Complex > 0.5*totalsIL6R ? 0.9*totalsIL6R : il6Complex;
        	this.set(totalIL6 - il6Complex, 0, x, y, z);
        	this.set(totalsIL6R - il6Complex, 1, x, y, z);
        	this.set(il6Complex, 2, x, y, z);
            return true;
        }
        if(interactable instanceof Pneumocyte) {
        	Pneumocyte cell = (Pneumocyte) interactable;
        	//System.out.println(IL6Complex.getMolecule().get(0, x, y, z));
        	if (cell.inPhenotype(IL6.getMolecule().getSecretionPhenotype()))//# and interactable.state == Neutrophil.INTERACTING:
        		this.inc(Constants.P_IL6_QTTY, 0, x, y, z);
        	if (cell.inPhenotype(this.getSecretionPhenotype()))//# and interactable.state == Neutrophil.INTERACTING:
        		this.inc(Constants.sIL6R_QTTY, 1, x, y, z);
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
