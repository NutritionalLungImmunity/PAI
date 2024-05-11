package edu.uf.interactable;

import java.util.HashMap;
import java.util.Map;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.GridFactory;
import edu.uf.interactable.Afumigatus.TAFC;
import edu.uf.intracellularState.Phenotype;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class Lipocalin2 extends Molecule{
	
	public static final String NAME = "Lpc2";
	public static final int NUM_STATES = 2;
	
	private static Lipocalin2 molecule = null;
	
private static final Map<String, Integer> INDEXES;
    
    static {
    	INDEXES = new HashMap<>();
    	INDEXES.put("LPC", 0); 
    	INDEXES.put("LPCBI", 1);
    }
    
    private Lipocalin2(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
		this.setPhenotye(Phenotype.createPhenotype());
	}
    
    public static Lipocalin2 getMolecule(Diffuse diffuse) {
    	if(molecule == null) {
    		double[][][][] values = new double[NUM_STATES][GridFactory.getXbin()][GridFactory.getYbin()][GridFactory.getZbin()];
    		molecule = new Lipocalin2(values, diffuse); 
    	}
    	return molecule;
    }
    
    public static Lipocalin2 getMolecule() {
    	return getMolecule(null);
    }
    
    @Override
    public double getKd() {
    	return -1;//Constants.Kd_IL23;
    }
    
    public void degrade() {
    	degrade(Constants.TNF_HALF_LIFE, 0);
    }

    public int getIndex(String str) {
        return Lipocalin2.INDEXES.get(str);
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    	this.totalMoleculesAux[1] = this.totalMoleculesAux[1] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof PneumocyteII) 
        	return Interactions.secrete((PneumocyteII) interactable, this, Constants.LPC2_QTTY, x, y, z, 0);
        /*if(interactable instanceof Transferrin) {
        	Transferrin tf = (Transferrin) interactable;
        	
        	double qtty = this.get("LPCBI", x, y, z);
        	
        	qtty = qtty <= 2 * tf.get("Tf", x, y, z) + tf.get("TfFe", x, y, z) ? qtty : 2 * tf.get("Tf", x, y, z) + tf.get("TfFe", x, y, z);
            double relTfFe = Util.ironTfReaction(qtty, tf.get("Tf", x, y, z), tf.get("TfFe", x, y, z));
            double tffeQtty = relTfFe * qtty;
            double tffe2Qtty = (qtty - tffeQtty) / 2.0;
            tf.dec(tffeQtty + tffe2Qtty, "Tf", x, y, z);
            tf.inc(tffeQtty, "TfFe", x, y, z);
            tf.inc(tffe2Qtty, "TfFe2", x, y, z);
            
            this.dec(qtty, "LPCBI", x, y, z);
            //this.inc(qtty, "LPC", x, y, z); Lipocalin2 should not be recicled!
        }*/
        
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
	public boolean isTime() {
		return true;
	}
}
