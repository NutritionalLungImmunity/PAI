package edu.uf.interactable.Afumigatus;

public class AfumigatusNoIron extends Afumigatus{
	
	public AfumigatusNoIron(double xRoot, double yRoot, double zRoot, double xTip, double yTip, double zTip, 
    		double dx, double dy, double dz, int growthIteration, double ironPool, int status, boolean isRoot) {
		super(xRoot, yRoot, zRoot, xTip, yTip, zTip, dx, dy, dz, growthIteration, ironPool, status, isRoot);
	}

	protected boolean canGrow() {
    	return true;//Rand.getRand().randunif() < Util.activationFunction(this.getIronPool(), Constants.Kd_GROW, 1.0, Constants.HYPHAE_VOL, 1.0);
    }
	
	
	protected Afumigatus createAfumigatus(double xRoot, double yRoot, double zRoot, double xTip, double yTip, double zTip, 
    		double dx, double dy, double dz, int growthIteration, double ironPool, int status, boolean isRoot) {
    	return new AfumigatusNoIron(xRoot, yRoot, zRoot,
    			xTip, yTip, zTip,
                dx, dy, dy, growthIteration,
                ironPool, status, isRoot);
    }
	
}
