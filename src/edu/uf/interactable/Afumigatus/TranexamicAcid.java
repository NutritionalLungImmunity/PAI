package edu.uf.interactable.Afumigatus;

import java.util.HashSet;
import java.util.Set;


import edu.uf.interactable.Blood;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Setter;

public class TranexamicAcid extends Setter{
	
	public static final String NAME = "TranexamicAcid";
	
	private static TranexamicAcid molecule = null; 
	
	private Set<Integer> iterations;
    
    private TranexamicAcid(int[] iterations) {
		this.iterations = new HashSet<>();
		for(int i : iterations)
			this.iterations.add(i);
	}
    
    public static TranexamicAcid getMolecule(int[] iterations) {
    	if(molecule == null) {
    		molecule = new TranexamicAcid(iterations);
    	}
    	return molecule;
    }
    
    public static TranexamicAcid getMolecule() {
    	return getMolecule(null);
    }
    
    @Override
    public double getKd() {
    	return -1;
    }

    //NEEDS primitive
    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	if(interactable instanceof Blood) {
    		if(this.iterations.contains(this.getIteration())) 
    			((Blood) interactable).setTxa(x,y,z);
    		return true;
    	}
    	
        return interactable.interact(this, x, y, z); 
    }

	@Override
	public String getName() {
		return NAME;
	}
	
	
	
}
