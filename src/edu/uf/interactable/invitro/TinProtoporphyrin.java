/*package edu.uf.interactable.invitro;

import edu.uf.Diffusion.Diffuse;
import edu.uf.interactable.Afumigatus;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Molecule;
import edu.uf.intracellularState.Molecules;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class TinProtoporphyrin extends Molecule implements Invitro{

	public static final String NAME = Molecules.TinProtoporphyrin;
	public static final int NUM_STATES = 1;
	
	private static TinProtoporphyrin molecule = null; 
    
    private TinProtoporphyrin(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static TinProtoporphyrin getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new TinProtoporphyrin(values, diffuse);
    	}
    	return molecule;
    }
    
    public static TinProtoporphyrin getMolecule() {
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
            	double qtty = Constants.HEME_UP * this.values[0][x][y][z];
            	this.dec(qtty, x, y, z);
            	if(Util.activationFunction(this.get(x, y, z), Constants.Kd_Heme, Constants.STD_UNIT_T) > Rand.getRand().randunif()) {
            		afumigatus.setBooleanNetwork(InvitroAfumigatus.Glu, 1);
            	}
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
	public String getReceptor() {
		return null; //REVIEW
	}
	
}*/
