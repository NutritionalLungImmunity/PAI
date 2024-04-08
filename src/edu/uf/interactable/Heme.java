package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.interactable.invitro.Invitro;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class Heme extends Molecule{

	public static final String NAME = "Heme";
	public static final int NUM_STATES = 1;
	
	//private static double xSystem = 0.0;
	
	public static final TLRBinder heme = new TLRBinder();
	
	private static Heme molecule = null; 
  
    
    private Heme(double[][][][] qttys, Diffuse diffuse, int[] phenotypes) {
		super(qttys, diffuse, phenotypes);
	}
    
    public static Heme getMolecule(double[][][][] values, Diffuse diffuse, int[] phenotypes) {
    	if(molecule == null) {
    		molecule = new Heme(values, diffuse, phenotypes);
    	}
    	return molecule;
    }
    
    public static Heme getMolecule() {
    	return molecule;
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
    	//if(interactable instanceof Invitro)return interactable.interact(this, x, y, z);
    	/*if(interactable instanceof Liver) {
    		Liver.getLiver().setHeme(Liver.getLiver().getHeme() + this.values[0][x][y][z]/2.0); //BASED ON IL6 ...
            return true;
    	}*/
    	/*if(interactable instanceof Heme) {
    		if(this.get(1, x, y, z) > 0) {
    			double qtty = Constants.HEME_TURNOVER_RATE * (Constants.HEME_SYSTEM_CONCENTRATION - this.get(0, x, y, z));
    			this.inc(qtty, 1, x, y, z);
    		}
    		return true;
    	}*/
        /*if(interactable instanceof Hemoglobin) {
        	Hemoglobin hb = (Hemoglobin) interactable;
        	double qtty = Constants.K_HB * hb.get(0, x, y, z);
        	this.inc(qtty, 0, x, y, z);
        	hb.dec(qtty, 0, x, y, z);
        	return true; 
        }*/
    	//if(!(interactable instanceof Molecule))System.out.println(interactable);
    	/*if(interactable instanceof Erythrocyte) {
    		Erythrocyte erythrocyte = (Erythrocyte) interactable;
        	double qtty = erythrocyte.getBurst() * Constants.ERYTROCYTE_HEMOGLOBIN_CONCENTRATION;
        	//System.out.println("> " + qtty);
        	this.inc(qtty, 0, x, y, z);
        	erythrocyte.setBurst(0);
        	return true;
    	}*/
    	if(interactable instanceof Blood) {
    		if(Blood.getBlood().hasBlood(x, y, z)) {
    			this.set(Constants.HEME_QTTY, 0, x, y, z);
    		}
    		return true;
    	}
        if(interactable instanceof Afumigatus) {
        	if(((Afumigatus) interactable).getStatus() == Afumigatus.HYPHAE) {
        		Afumigatus afumigatus = (Afumigatus) interactable;
            	double qtty = Constants.HEME_UP * this.get(0, x, y, z);
            	//System.out.println(qtty);
            	afumigatus.incHeme(qtty);
            	afumigatus.incIronPool(qtty);
            	this.dec(qtty, 0, x, y, z);
        	}
        	
        	return true;
        	/*if(((Afumigatus) interactable).getStatus() == Afumigatus.HYPHAE) 
        		this.set(1.0, 1, x, y, z);
        		
        	Afumigatus afumigatus = (Afumigatus) interactable;
        	double qtty = Constants.HEME_UP * this.get(0, x, y, z);
        	afumigatus.incIronPool(qtty);
        	this.dec(qtty, 0, x, y, z);
        	return true;*/
        }
        if(interactable instanceof Macrophage) 
        	return Util.bind((Macrophage) interactable, this, x, y, z, 0);
        
        if(interactable instanceof Neutrophil) 
        	return Util.bind((Neutrophil) interactable, this, x, y, z, 0);
        
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
