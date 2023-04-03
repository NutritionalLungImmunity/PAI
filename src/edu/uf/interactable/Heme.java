package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.interactable.invitro.Invitro;
import edu.uf.intracellularState.EukaryoteSignalingNetwork;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class Heme extends Molecule{

	public static final String NAME = "Heme";
	public static final int NUM_STATES = 1;
	public static final int MOL_IDX = getReceptors();
	
	//private static double xSystem = 0.0;
	
	private static Heme molecule = null;  
  
    
    private Heme(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static Heme getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new Heme(values, diffuse);
    	}
    	return molecule;
    }
    
    public static Heme getMolecule() {
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
    	if(interactable instanceof Invitro)return interactable.interact(this, x, y, z);
    	/*if(interactable instanceof Liver) {
    		Liver.getLiver().setHeme(Liver.getLiver().getHeme() + this.values[0][x][y][z]/2.0); //BASED ON IL6 ...
            return true;
    	}*/
    	if(interactable instanceof Heme) {
    		if(this.get(1, x, y, z) > 0) {
    			double qtty = Constants.HEME_TURNOVER_RATE * (Constants.HEME_SYSTEM_CONCENTRATION - this.get(0, x, y, z));
    			this.inc(qtty, 1, x, y, z);
    		}
    		return true;
    	}
        if(interactable instanceof Hemoglobin) {
        	Hemoglobin hb = (Hemoglobin) interactable;
        	double heme = Constants.K_HB * hb.get(0, x, y, z);
        	this.inc(heme, 0, x, y, z);
        	hb.dec(heme, 0, x, y, z);
        	return true; 
        }
        if(interactable instanceof Afumigatus) {
        	if(((Afumigatus) interactable).getStatus() == Afumigatus.HYPHAE) 
        		this.set(1.0, 1, x, y, z);
        		
        	Afumigatus afumigatus = (Afumigatus) interactable;
        	double heme = Constants.HEME_UP * this.get(0, x, y, z);
        	afumigatus.incIronPool(heme);
        	this.dec(heme, 0, x, y, z);
        	return true;
        }
        if(interactable instanceof Macrophage) {
        	Macrophage macrophage = (Macrophage) interactable;
        	EukaryoteSignalingNetwork.Heme_e = MOL_IDX;
        	if(Util.activationFunction(this.get(0, x, y, z), Constants.Kd_Heme))
        		macrophage.bind(MOL_IDX);
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
