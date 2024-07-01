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
