package edu.uf.interactable.invitrogrowth;

import edu.uf.compartments.Voxel;
import edu.uf.intracellularState.BooleanNetwork;
import edu.uf.utils.LinAlg;
import edu.uf.utils.Rand;

public class Afumigatus extends edu.uf.interactable.Afumigatus{

public static final String NAME = "Afumigatus";

	private boolean growable = true;
	private static long totalCells;
    
    public Afumigatus() {
    	this(0,0,0, 0,0,0, Rand.getRand().randunif(), Rand.getRand().randunif(), Rand.getRand().randunif(),
    			0, 0, RESTING_CONIDIA, 0, true);
    }


    public Afumigatus(double xRoot, double yRoot, double zRoot, double xTip, double yTip, double zTip, 
    		double dx, double dy, double dz, int growthIteration, double ironPool, int status, 
    		int state, boolean isRoot) {
        super(xRoot, yRoot, zRoot, xTip, yTip, zTip, dx, dy, dz, growthIteration, 0.0, status, state, isRoot);
        totalCells++;
    }
    
    protected BooleanNetwork createNewBooleanNetwork() {
    	return new BooleanNetwork() {

			@Override
			public void processBooleanNetwork() {
				// TODO Auto-generated method stub
				
			}
    		
    	};
    }
    
    public static long getCellCount() {
    	return totalCells;
    }
    
    protected boolean canGrow() {
    	if(this.getxTip() <= Field.X_BIN && this.getxTip() >= 0 && this.getyTip() <= Field.Y_BIN && this.getyTip() >= 0 && this.getzTip() <= Field.Z_BIN && this.getzTip() >= 0)
    		return true;//Rand.getRand().randunif() < Util.activationFunction(this.getIronPool(), Constants.Kd_GROW, 1.0, Constants.HYPHAE_VOL, 1.0);
    	else {
    		this.growable = false;
    		//System.out.println(this.getxTip() + " " + Field.X_BIN + " " + this.getyTip() + " " + Field.Y_BIN + " " + this.getzTip() + " " + Field.Z_BIN);
    		return false;
    	}
    }
    
    public synchronized void grow(Field field) {          
    	Afumigatus nextSepta = (Afumigatus) this.elongate();
        if (nextSepta != null)
        	field.setCell(nextSepta);
        nextSepta = (Afumigatus) this.branch();
        if (nextSepta != null)
        	field.setCell(nextSepta);
	}
    
    protected Afumigatus createAfumigatus(double xRoot, double yRoot, double zRoot, double xTip, double yTip, double zTip, 
    		double dx, double dy, double dz, int growthIteration, double ironPool, int status, 
    		int state, boolean isRoot) {
    	double[] ds = new double[] {dx, dy, dz};
        LinAlg.multiply(ds, 1.0/LinAlg.norm(ds));
    	return new Afumigatus(xRoot, yRoot, zRoot,
    			xTip, yTip, zTip,
                ds[0], ds[1], ds[2], growthIteration,
                ironPool, status, state, isRoot);
    }
    
    public boolean isGrowable() {
    	return super.isGrowable() && this.growable;
    }
	
}
