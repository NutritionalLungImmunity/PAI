package edu.uf.interactable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class Hemopexin extends Molecule{
	
	public static final String NAME = "Hpx";
	public static final int NUM_STATES = 2;
	
	//private static double xSystem = 0.0;
	
	private static Hemopexin molecule = null;  
	
	private static final Map<String, Integer> INDEXES;
	
	private double hemopexinSystemConcentration;
    
    static {
    	INDEXES = new HashMap<>();
    	INDEXES.put("Hp", 0); 
    	INDEXES.put("HpHb", 1);
    }
    
    private Hemopexin(double[][][][] qttys, Diffuse diffuse, int[] phenotypes) {
		super(qttys, diffuse, phenotypes);
		this.hemopexinSystemConcentration = Constants.DEFAULT_HPX_CONCENTRATION;
	}
    
    public static Hemopexin getMolecule(double[][][][] values, Diffuse diffuse, int[] phenotypes) {
    	if(molecule == null) {
    		molecule = new Hemopexin(values, diffuse, phenotypes);
    	}
    	return molecule;
    }
    
    public static Hemopexin getMolecule() {
    	return molecule;
    }

    public void turnOver(int x, int y, int z) {} //Hp turnover done at interaction!
    
    public void degrade() {}//REVIEW

    public int getIndex(String str) {
        return Hemopexin.INDEXES.get(str);
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    	this.totalMoleculesAux[1] = this.totalMoleculesAux[1] + this.get(1, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	if(interactable instanceof Hemopexin) {
    		if(this.get(2, x, y, z) > 0) {
    			this.inc((Constants.HEME_TURNOVER_RATE * (this.hemopexinSystemConcentration - this.get(0, x, y, z))), 0, x, y, z);
    			this.dec((Constants.HEME_TURNOVER_RATE * this.get(1, x, y, z)), 1, x, y, z);
    		}
    		return true;
    	}
    	if(interactable instanceof Afumigatus) {
        	if(((Afumigatus) interactable).getStatus() == Afumigatus.HYPHAE) 
        		this.set(1.0, 2, x, y, z);
        	return true;
    	}
    	
    	if(interactable instanceof Liver) {
    		Liver liver = (Liver) interactable;
    		this.hemopexinSystemConcentration = liver.getBooleanEnsemble()[Liver.HPX]/((double)Liver.ENSEMBLE_SIZE);
        	//if(hpxQtty < Liver.LIVER_BN_EPS)return true;
        	
    		this.hemopexinSystemConcentration = (
    				this.hemopexinSystemConcentration * Constants.L_HPX_QTTY + 
    				(1 - this.hemopexinSystemConcentration) * Constants.L_REST_HPX_QTTY
    		) * Constants.VOXEL_VOL;

    		return true; 
    	}
        if (interactable instanceof Macrophage){//# or type(interactable) is Neutrophil: 
        	((Macrophage)interactable).bind(this, Util.activationFunction5(this.get(1, x, y, z), Constants.Kd_HPX));
            return true;
        }
        if(interactable instanceof Heme) {
        	Heme heme = (Heme) interactable;
        	double hpxheme = Util.michaelianKinetics(
        			this.get(0, x, y, z), 
        			heme.get(0,x,y,z), 
        			Constants.KM_HPX, 
        			Constants.STD_UNIT_T, 
        			Constants.KCAT_HPX, 
        			Constants.VOXEL_VOL
        	);
        	
        	//System.out.println(hpxheme + "\t" + heme.get(0,x,y,z) + "\t" + this.values[0][x][y][z]);
        	this.dec(hpxheme, 0, x, y, z);
        	this.inc(hpxheme, 1, x, y, z);
        	heme.dec(hpxheme, 0, x, y, z);
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
