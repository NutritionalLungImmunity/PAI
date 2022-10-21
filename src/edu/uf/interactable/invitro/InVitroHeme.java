/*package edu.uf.interactable.invitro;

import edu.uf.Diffusion.Diffuse;
import edu.uf.interactable.Hemoglobin;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Molecule;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class InVitroHeme extends Molecule implements Invitro{

	public static final String NAME = Molecules.InVitroHeme;
	public static final int NUM_STATES = 1;
	
	//private static double xSystem = 0.0;
	
	private static InVitroHeme molecule = null;  
  
    
    private InVitroHeme(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static InVitroHeme getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new InVitroHeme(values, diffuse);
    	}
    	return molecule;
    }
    
    public static InVitroHeme getMolecule() {
    	return molecule;
    }

    public void degrade(int x, int y, int z) {
    	degrade(Util.turnoverRate(1, 0, Constants.TURNOVER_RATE, Constants.REL_CYT_BIND_UNIT_T, 1), x, y, z);
    } 
    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.values[0][x][y][z];
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if(interactable instanceof Hemoglobin) {
        	Hemoglobin hb = (Hemoglobin) interactable;
        	double heme = Constants.K_HB * hb.values[0][x][y][z];
        	this.inc(heme, x, y, z);
        	hb.dec(heme, x, y, z);
        	return true; 
        }
        if(interactable instanceof Macrophage) {
        	Macrophage macrophage = (Macrophage) interactable;
        	if(Util.activationFunction(this.values[0][x][y][z], Constants.Kd_Heme, Constants.STD_UNIT_T) > Rand.getRand().randunif()) {
        		macrophage.setStatus(Macrophage.ACTIVATING);
        		//macrophage.setHeme(true);
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
	
}*/
