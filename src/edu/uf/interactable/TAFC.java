package edu.uf.interactable;
import java.util.HashMap;
import java.util.Map;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants; 
import edu.uf.utils.Util;

public class TAFC extends Molecule{
    

	public static final String NAME = "TAFC";
	public static final int NUM_STATES = 2;
    private static final double THRESHOLD = Constants.K_M_TF_TAFC * Constants.VOXEL_VOL / 1.0e6;
    public static final int MOL_IDX = getReceptors();
    
    private static TAFC molecule = null;

    private static final Map<String, Integer> INDEXES;
    
    static {
    	INDEXES = new HashMap<>();
    	INDEXES.put("TAFC", 0); 
    	INDEXES.put("TAFCBI", 1);
    }
    
    
    private TAFC(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    public static TAFC getMolecule(double[][][][] values, Diffuse diffuse) {
    	if(molecule == null) {
    		molecule = new TAFC(values, diffuse);
    	}
    	return molecule;
    }
    
    public static TAFC getMolecule() {
    	return molecule;
    }
    
    public void degrade() {} //REVIEW

    public int getIndex(String str) {
        return TAFC.INDEXES.get(str);
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    	this.totalMoleculesAux[1] = this.totalMoleculesAux[1] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Transferrin) {
            Molecule tf = (Molecule) interactable;
        	double dfe2dt = Util.michaelianKinetics(
        			tf.get("TfFe2", x, y, z), this.get("TAFC", x, y, z), Constants.K_M_TF_TAFC, Constants.STD_UNIT_T
        	);
            double dfedt  = Util.michaelianKinetics(
            		tf.get("TfFe", x, y, z), this.get("TAFC", x, y, z), Constants.K_M_TF_TAFC, Constants.STD_UNIT_T
            );

            if (dfe2dt + dfedt > this.get("TAFC", x, y, z)) {
                double rel = this.get("TAFC", x, y, z) / (dfe2dt + dfedt);
                dfe2dt = dfe2dt * rel;
                dfedt = dfedt * rel;
            }
            tf.dec(dfe2dt, "TfFe2", x, y, z);
            tf.inc(dfe2dt, "TfFe", x, y, z);

            tf.dec(dfedt, "TfFe", x, y, z);
            tf.inc(dfedt, "Tf", x, y, z);

            this.inc(dfe2dt + dfedt, "TAFCBI", x, y, z);
            this.dec(dfe2dt + dfedt, "TAFC", x, y, z);

            //double v = dfe2dt + dfedt;

            return true;
        }
        if (interactable instanceof Afumigatus) {
            Afumigatus af = (Afumigatus) interactable;
        	if (af.getState() == Afumigatus.FREE && af.getStatus() != Afumigatus.DYING && af.getStatus() != Afumigatus.DEAD) {
                if (af.createBooleanNetwork().getBooleanNetwork()[Afumigatus.MirB] == 1 && af.createBooleanNetwork().getBooleanNetwork()[Afumigatus.EstB] == 1) {
                    double qtty = this.get("TAFCBI", x, y, z) * Constants.TAFC_UP;
                    qtty = qtty < this.get("TAFCBI", x, y, z) ? qtty : this.get("TAFCBI", x, y, z);

                    this.dec(qtty, "TAFCBI", x, y, z); 
                    af.incIronPool(qtty);
                }
                if (af.createBooleanNetwork().getBooleanNetwork()[Afumigatus.Tafc] == 1 && 
                        (af.getStatus() == Afumigatus.SWELLING_CONIDIA || 
                        af.getStatus() == Afumigatus.HYPHAE ||
                        af.getStatus() == Afumigatus.GERM_TUBE)) {  // # SECRETE TAFC
                    this.inc(af.getTAFCQTTY(), "TAFC", x, y, z);
                    //System.out.println(af.getBooleanNetwork(InvitroAfumigatus.Orn));
                }   
        	}
            return true;
        }
        if (interactable instanceof Iron) {
            Iron iron = (Iron) interactable;
        	double qttyIron = iron.get("Iron", x, y, z);
            double qttyTafc = this.get("TAFC", x, y, z);
            double qtty = qttyTafc < qttyIron ? qttyTafc : qttyIron;
            this.dec(qtty, "TAFC", x, y, z);
            this.inc(qtty, "TAFCBI", x, y, z);
            iron.dec(qtty, "Iron", x, y, z);

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
		return THRESHOLD;
	}

	@Override
	public int getNumState() {
		return NUM_STATES;
	}
}
