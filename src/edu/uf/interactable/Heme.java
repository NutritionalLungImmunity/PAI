package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.GridFactory;
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.interactable.invitro.Invitro;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class Heme extends Molecule{

	public static final String NAME = "Heme";
	public static final int NUM_STATES = 1;
	
	//private static double xSystem = 0.0;
	
	public static final TLRBinder heme = new TLRBinder();
	
	private static Heme molecule = null; 
  
    
    private Heme(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static Heme getMolecule(Diffuse diffuse) {
    	if(molecule == null) {
    		double[][][][] values = new double[NUM_STATES][GridFactory.getXbin()][GridFactory.getYbin()][GridFactory.getZbin()];
    		molecule = new Heme(values, diffuse); 
    	}
    	return molecule;
    }
    
    public static Heme getMolecule() {
    	return getMolecule(null);
    }
    
    @Override
    public double getKd() {
    	return Constants.Kd_Heme;
    }
    
    public void degrade() {}//REVIEW
    
    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }
    
    public int getInteractionId() {
    	return TLRBinder.getBinder().getInteractionId();
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	if(interactable instanceof Blood) 
    		return Interactions.set(this, (Cell) interactable, Constants.HEME_QTTY, x, y, z, 0);
    	
        if(interactable instanceof Afumigatus) 
        	return Interactions.aspergillusHemeUptake((PositionalInfectiousAgent) interactable, this, x, y, z);
        
        if(interactable instanceof Macrophage) 
        	return Interactions.bind((Leukocyte) interactable, this, x, y, z, 0);
        
        if(interactable instanceof Neutrophil) 
        	return Interactions.bind((Leukocyte) interactable, this, x, y, z, 0);
        
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
