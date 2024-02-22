package edu.uf.interactable;

import java.util.HashMap;
import java.util.Map;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class Lactoferrin extends Molecule{
    

	public static final String NAME = "Lactoferrin";
	public static final int NUM_STATES = 3;
    private static final double THRESHOLD = Constants.K_M_TF_LAC * Constants.VOXEL_VOL / 1.0e6;
    
    private static Lactoferrin molecule = null; 

    private static final Map<String, Integer> INDEXES;
    
    static {
    	INDEXES = new HashMap<>();
    	INDEXES.put("Lactoferrin", 0);
    	INDEXES.put("LactoferrinFe", 1);
    	INDEXES.put("LactoferrinFe2", 2);
    }
    
    private Lactoferrin(double[][][][] qttys, Diffuse diffuse, int[] phenotypes) {
		super(qttys, diffuse, phenotypes);
	}
    
    public static Lactoferrin getMolecule(double[][][][] values, Diffuse diffuse, int[] phenotypes) {
    	if(molecule == null) {
    		molecule = new Lactoferrin(values, diffuse, phenotypes);
    	}
    	return molecule;
    }
    
    public static Lactoferrin getMolecule() {
    	return molecule;
    }
    
    public void degrade() {} //REVIEW

    public int getIndex(String str) {
        return Lactoferrin.INDEXES.get(str);
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    	this.totalMoleculesAux[1] = this.totalMoleculesAux[1] + this.get(1, x, y, z);
    	this.totalMoleculesAux[2] = this.totalMoleculesAux[2] + this.get(2, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Macrophage) { //#ADD UPTAKE
            Macrophage macro = (Macrophage) interactable;
        	double qttyFe2 = this.get("LactoferrinFe2", x, y, z) * Constants.MA_IRON_IMPORT_RATE * 
        			Constants.STD_UNIT_T;
            double qttyFe = this.get("LactoferrinFe", x, y, z) * Constants.MA_IRON_IMPORT_RATE * 
            		Constants.STD_UNIT_T;
            
            qttyFe2 = qttyFe2 < this.get("LactoferrinFe2", x, y, z) ? qttyFe2 : this.get("LactoferrinFe2", x, y, z);
            qttyFe = qttyFe < this.get("LactoferrinFe", x, y, z) ? qttyFe : this.get("LactoferrinFe", x, y, z);

            this.dec(qttyFe2, "LactoferrinFe2", x, y, z);
            this.dec(qttyFe, "LactoferrinFe", x, y, z);
            macro.incIronPool(2 * qttyFe2 + qttyFe);
            return true;
        }
        if (interactable instanceof Neutrophil) {
            Neutrophil neutro = (Neutrophil) interactable;
            //System.out.println((neutro.status == Neutrophil.ACTIVE) + " "  + (neutro.state == Neutrophil.INTERACTING));
        	if (neutro.hasPhenotype(this.getPhenotype()) && !neutro.hasDegranulated()){//(neutro.getStatus() == Neutrophil.ACTIVE && neutro.getState() == Neutrophil.INTERACTING) 
        		this.inc(Constants.LAC_QTTY, "Lactoferrin", x, y, z);
        		neutro.degranulate();
        	}
        	
            return true;
        }
        if (interactable instanceof Transferrin) {
            Molecule tf = (Molecule) interactable;
        	double dfe2dt = Util.michaelianKinetics(
        			tf.get("TfFe2", x, y, z), this.get("Lactoferrin", x, y, z), Constants.K_M_TF_LAC, Constants.STD_UNIT_T
        	);
            double dfedt  = Util.michaelianKinetics(
            		tf.get("TfFe", x, y, z), this.get("Lactoferrin", x, y, z), Constants.K_M_TF_LAC, Constants.STD_UNIT_T
            );

            double dfe2dtFe = Util.michaelianKinetics(
            		tf.get("TfFe2", x, y, z), this.get("LactoferrinFe", x, y, z), Constants.K_M_TF_LAC, Constants.STD_UNIT_T
            );
            double dfedtFe = Util.michaelianKinetics(
            		tf.get("TfFe", x, y, z), this.get("LactoferrinFe", x, y, z), Constants.K_M_TF_LAC, Constants.STD_UNIT_T
            );

            if (dfe2dt + dfedt > this.get("Lactoferrin", x, y, z)) {
                double rel = this.get("Lactoferrin", x, y, z) / (dfe2dt + dfedt);
                dfe2dt = dfe2dt * rel;
                dfedt = dfedt * rel;
            }

            if (dfe2dtFe + dfedtFe > this.get("LactoferrinFe", x, y, z)) {
                double rel = this.get("LactoferrinFe", x, y, z) / (dfe2dtFe + dfedtFe);
                dfe2dtFe = dfe2dtFe * rel;
                dfedtFe = dfedtFe * rel;
            }

            tf.dec(dfe2dt + dfe2dtFe, "TfFe2", x, y, z);
            tf.inc(dfe2dt + dfe2dtFe, "TfFe", x, y, z);

            tf.dec(dfedt + dfedtFe, "TfFe", x, y, z);
            tf.inc(dfedt + dfedtFe, "Tf", x, y, z);

            this.dec(dfe2dt + dfedt, "Lactoferrin", x, y, z);
            this.inc(dfe2dt + dfedt, "LactoferrinFe", x, y, z);

            this.dec(dfe2dtFe + dfedtFe, "LactoferrinFe", x, y, z);
            this.inc(dfe2dtFe + dfedtFe, "LactoferrinFe2", x, y, z);

            return true;
        }
        if (interactable instanceof Iron) {
            Molecule iron = (Molecule) interactable;
        	double qtty = iron.get("Iron", x, y, z);
            qtty  = qtty <= 2 * this.get("Lactoferrin", x, y, z) + this.get("LactoferrinFe", x, y, z) ?
            		qtty : 2 * this.get("Lactoferrin", x, y, z) + this.get("LactoferrinFe", x, y, z);
            double relTfFe = Util.ironTfReaction(qtty, this.get("Lactoferrin", x, y, z), this.get("LactoferrinFe", x, y, z));
            double tffeQtty = relTfFe * qtty;
            double tffe2Qtty = (qtty - tffeQtty) / 2;
            this.dec(tffeQtty + tffe2Qtty, "Lactoferrin", x, y, z);
            this.inc(tffeQtty, "LactoferrinFe", x, y, z);
            this.inc(tffe2Qtty, "LactoferrinFe2", x, y, z);
            iron.dec(qtty, "Iron", x, y, z);
            return true; 
        }
        

        //if itype is ROS:
        //    return False
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
        
}
