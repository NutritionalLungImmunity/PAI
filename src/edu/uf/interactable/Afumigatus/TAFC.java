package edu.uf.interactable.Afumigatus;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.GridFactory;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Iron;
import edu.uf.interactable.Molecule;
import edu.uf.interactable.PositionalInfectiousAgent;
import edu.uf.interactable.Siderophore;
import edu.uf.interactable.Transferrin;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants; 

public class TAFC extends Siderophore{
    

	public static final String NAME = "TAFC";
	public static final int NUM_STATES = 2;
    private static final double THRESHOLD = Constants.K_M_TF_TAFC * Constants.VOXEL_VOL / 1.0e6;
    
    private static TAFC molecule = null;
    
    
    private TAFC(double[][][][] qttys, Diffuse diffuse) {
    	super(qttys, diffuse, NAME);
	}
    
    public static TAFC getMolecule(Diffuse diffuse) {
    	if(molecule == null) {
    		double[][][][] values = new double[NUM_STATES][GridFactory.getXbin()][GridFactory.getYbin()][GridFactory.getZbin()];
    		molecule = new TAFC(values, diffuse); 
    	}
    	return molecule;
    }
    
    public static TAFC getMolecule() {
    	return getMolecule(null);
    }
    
    @Override
    public double getKd() {
    	return -1;
    }
    
    public void degrade() {} //REVIEW

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    	this.totalMoleculesAux[1] = this.totalMoleculesAux[1] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Transferrin) {
        	//Interactions.transferrinIronChelation((Molecule) interactable, this, x, y, z);
            return Interactions.siderophoreTransferrinChelation((Molecule) interactable, this, Constants.K_M_TF_TAFC, x, y, z);
        }
        if (interactable instanceof Afumigatus) {
        	PositionalInfectiousAgent af = (PositionalInfectiousAgent) interactable;
        	Interactions.uptakeSiderophore(af, this, Constants.TAFC_UP, x, y, z);
            return Interactions.secreteSiderophore(af, this, x, y, z);
        }
        if (interactable instanceof Iron)
        	return Interactions.siderophoreIronChelation((Molecule) interactable, this, x, y, z);

        return interactable.interact(this, x, y, z);
    }

	@Override
	public String getName() {
		return NAME;
	}
 
	@Override
	public double getThreshold() {
		return THRESHOLD;
	}

	@Override
	public int getNumState() {
		return NUM_STATES;
	}
	
	@Override
	public boolean isTime() {
		return true;
	}

	@Override
	public double getSiderophoreQtty() {
		return Constants.TAFC_QTTY;
	}
}
