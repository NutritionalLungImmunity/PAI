/*package edu.uf.interactable.invitro;

import edu.uf.Diffusion.Diffuse;
import edu.uf.interactable.*;
import edu.uf.intracellularState.Molecules;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class FreeIron extends Molecule implements Invitro{

	public static final String NAME = Molecules.FreeIron;
	public static final int NUM_STATES = 1;
	
	private static FreeIron molecule = null; 
    
    private FreeIron(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static FreeIron getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new FreeIron(values, diffuse);
    	}
    	return molecule;
    }
    
    public static FreeIron getMolecule() {
    	return molecule;
    }
    

    public int getIndex(String str) {
        return 0;
    }

    public void degrade(int x, int y, int z) {}

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.values[0][x][y][z];
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Afumigatus) {
            Afumigatus afumigatus = (Afumigatus) interactable;
            if(afumigatus.getStatus() != Afumigatus.RESTING_CONIDIA) {
            	if(afumigatus.getBooleanNetwork(Afumigatus.RIA) == 1) {
            		double iron = Constants.REDUCTIVE_IRON_ASSIMILATION_RATE * this.get(x, y, z);
            		this.dec(iron, x, y, z);
            		if(this.get(x, y, z) < Constants.MIN_FREE_IRON) this.set(Constants.MIN_FREE_IRON, x, y, z);
            		afumigatus.incIronPool(iron);
            	}
            }
            return true;  
        }
        if(interactable instanceof TAFC) {
        	TAFC tafc = (TAFC) interactable;
        	double v = Util.michaelianKinetics(
        			this.get(x, y, z), 
        			tafc.get(x, y, z), 
        			Constants.KM_IRON, 
        			Constants.STD_UNIT_T, 
        			Constants.KCAT_IRON, 
        			Constants.VOXEL_VOL
        	);
        	this.dec(v, x, y, z);
        	if(this.get(x, y, z) < Constants.MIN_FREE_IRON) this.set(Constants.MIN_FREE_IRON, x, y, z);
        	tafc.dec(v, "TAFC", x, y, z);
        	tafc.inc(v, "TAFCBI", x, y, z);
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

}*/
