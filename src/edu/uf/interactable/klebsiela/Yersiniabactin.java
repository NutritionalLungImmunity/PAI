package edu.uf.interactable.klebsiela;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.GridFactory;
import edu.uf.interactable.InfectiousAgent;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Iron;
import edu.uf.interactable.MCP1;
import edu.uf.interactable.Molecule;
import edu.uf.interactable.Siderophore;
import edu.uf.interactable.Transferrin;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants;

public class Yersiniabactin extends Siderophore{
    

	public static final String NAME = "Yersiniabactin";
    
    private static Yersiniabactin molecule = null;
    
    
    private Yersiniabactin(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static Yersiniabactin getMolecule(Diffuse diffuse) {
    	if(molecule == null) {
    		double[][][][] values = new double[NUM_STATES][GridFactory.getXbin()][GridFactory.getYbin()][GridFactory.getZbin()];
    		molecule = new Yersiniabactin(values, diffuse); 
    	}
    	return molecule;
    }
    
    public static Yersiniabactin getMolecule() {
    	return getMolecule(null);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	if (interactable instanceof Transferrin) 
    		return Interactions.siderophoreTransferrinChelation((Molecule) interactable, this, Constants.K_M_TF_YER, x, y, z);
        
        if (interactable instanceof Klebsiella) {
        	Interactions.secreteSiderophore((InfectiousAgent) interactable, this, x, y, z);
        	return Interactions.uptakeSiderophore((InfectiousAgent) interactable, this, Constants.YER_UP_RATE, x, y, z);
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
	public double getSiderophoreQtty() {
		return Constants.YER_QTTY;
	}
}
