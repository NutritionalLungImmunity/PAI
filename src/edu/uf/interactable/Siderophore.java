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
    
    /**
     * Disabled. <strong> review: siderophores undergo turnover</strong>
     * @param index
     * @param inc
     */
    public void degrade() {} //REVIEW

    public int getIndex(String str) {
        return Siderophore.INDEXES.get(str);
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    	this.totalMoleculesAux[1] = this.totalMoleculesAux[1] + this.get(0, x, y, z);
    }
    
    /**
     * Returns the quantity of siderophore that should be secreted if conditions apply. 
     * If the cell or phenotype has activation levels, this should refer to the amount 
     * secreted in the maximum activation level. 
     * @return
     */
    public abstract double getSiderophoreQtty();
    
    /**
     * <strong>Not used</strong>
     * @param siderophore
     */
    public void setHasSiderophore(boolean siderophore) {
    	this.hasSiderophore = siderophore;
    }
    
    /**
     * <strong>Used by Klebsiella, but hasSiderophore is never initialized.</strong>
     * @param siderophore
     */
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
