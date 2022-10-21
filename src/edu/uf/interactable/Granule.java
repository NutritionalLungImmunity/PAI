package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class Granule extends Molecule{

	   
		public static final String NAME = "Granule";
		public static final int NUM_STATES = 1;
		public static final int MOL_IDX = getReceptors();
		
		private static Granule molecule = null;    
	    
	    private Granule(double[][][][] qttys, Diffuse diffuse) {
			super(qttys, diffuse);
		}
	    
	    public static Granule getMolecule(double[][][][] values, Diffuse diffuse) {
	    	if(molecule == null) {
	    		molecule = new Granule(values, diffuse);
	    	}
	    	return molecule;
	    }
	    
	    public static Granule getMolecule() {
	    	return molecule;
	    }
	    
	    public void degrade() {
	    	degrade(Constants.Granule_HALF_LIFE, 0);
	    }

	    public int getIndex(String str) {
	        return 0;
	    }

	    public void computeTotalMolecule(int x, int y, int z) {
	    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);// .values[0][x][y][z];
	    }

	    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
	    	if (interactable instanceof Neutrophil){//# or type(interactable) is Neutrophil: 
	        	Neutrophil neutrophil = (Neutrophil) interactable;
	        	if(neutrophil.inPhenotype(this.getSecretionPhenotype())) {
	        	//if((neutrophil.getStatus() == Macrophage.ACTIVE)){ // || Rand.getRand().getRand().randunif() < Constants.PR_DEPLETION) && !neutrophil.depleted){
	        		this.inc(Constants.GRANULE_QTTY, 0, x, y, z);
	        		//neutrophil.depleted = true;
	        	}
	            
	            return true; 
	        }
	        
	        if(interactable instanceof Afumigatus) {
	        	Afumigatus af = (Afumigatus) interactable;
	        	if(Util.activationFunction(this.get(0, x, y, z), Constants.Kd_Granule, 1, 1) > Rand.getRand().randunif()) {
	        		af.die();
	        	}
	        	return true;
	        }
	        //System.out.println(interactable);
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
	
}
