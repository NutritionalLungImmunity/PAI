package edu.uf.interactable;

import java.util.HashMap;
import java.util.Map;

import edu.uf.Diffusion.Diffuse;

public abstract class Siderophore extends Molecule{

	public static final int NUM_STATES = 2;

    private static final Map<String, Integer> INDEXES;
    
    private boolean hasSiderophore;
    
    static {
    	INDEXES = new HashMap<>();
    	INDEXES.put("SID", 0); 
    	INDEXES.put("SIDBI", 1);
    }
    
    
    protected Siderophore(double[][][][] qttys, Diffuse diffuse) {
		super(qttys, diffuse);
	}
    
    
    @Override
    public double getKd() {
    	return -1;
    }
    
    public void degrade() {} //REVIEW

    public int getIndex(String str) {
        return Siderophore.INDEXES.get(str);
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    	this.totalMoleculesAux[1] = this.totalMoleculesAux[1] + this.get(0, x, y, z);
    }

    /*public void transferrinChelation(Molecule tf, int x, int y, int z, double km) {
    	double dfe2dt = Util.michaelianKinetics(
    			tf.get("TfFe2", x, y, z), this.get("SID", x, y, z), km, Constants.STD_UNIT_T
    	);
        double dfedt  = Util.michaelianKinetics(
        		tf.get("TfFe", x, y, z), this.get("SID", x, y, z), km, Constants.STD_UNIT_T
        );

        if (dfe2dt + dfedt > this.get("SID", x, y, z)) {
            double rel = this.get("SID", x, y, z) / (dfe2dt + dfedt);
            dfe2dt = dfe2dt * rel;
            dfedt = dfedt * rel;
        }
        tf.dec(dfe2dt, "TfFe2", x, y, z);
        tf.inc(dfe2dt, "TfFe", x, y, z);

        tf.dec(dfedt, "TfFe", x, y, z);
        tf.inc(dfedt, "Tf", x, y, z);

        this.inc(dfe2dt + dfedt, "SIDBI", x, y, z);
        this.dec(dfe2dt + dfedt, "SID", x, y, z);
    }
    
    public void ironChelation(Iron iron, int x, int y, int z) {
    	double qttyIron = iron.get("Iron", x, y, z);
        double qttyTafc = this.get("SID", x, y, z);
        double qtty = qttyTafc < qttyIron ? qttyTafc : qttyIron;
        this.dec(qtty, "SID", x, y, z);
        this.inc(qtty, "SIDBI", x, y, z);
        iron.dec(qtty, "Iron", x, y, z);
    }
    
    public void klebsiellaInteract(Cell k, int x, int y, int z, double sidQtty, double uptakeRate, boolean hasSiderophore) {
    	if(!k.isDead()) {
    		if(hasSiderophore) {
    			this.inc(sidQtty, "SID", x, y, z);
    		}
    		double qtty = uptakeRate * this.get("SIDBI", x, y, z);
    		qtty = qtty < this.get("SIDBI", x, y, z) ? qtty : this.get("SIDBI", x, y, z);
    		
    		this.dec(qtty, "SIDBI", x, y, z); 
            k.incIronPool(qtty);
    	}
    }*/
    
    public abstract double getSiderophoreQtty();
    
    public void setHasSiderophore(boolean siderophore) {
    	this.hasSiderophore = siderophore;
    }
    
    public boolean hasSiderophore() {
    	return this.hasSiderophore;
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
	public double getThreshold() {
		return 0;
	}
}
