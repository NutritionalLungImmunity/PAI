package edu.uf.interactable;

import edu.uf.intracellularState.IntracellularModel;

public abstract class InfectiousAgent extends Cell{
    
	public InfectiousAgent(IntracellularModel booleanNetwork) {
		super(booleanNetwork);
	}

	public void grow(int x, int y, int z, int xbin, int ybin, int zbin) {
		grow(x, y, z, xbin, ybin, zbin, null);
	}
	
    public abstract void grow(int x, int y, int z, int xbin, int ybin, int zbin, Leukocyte phagocyte);
    
    public abstract boolean hasSiderophore(Siderophore siderophore);
    
    public abstract boolean isSecretingSiderophore(Siderophore mol);
    
    public abstract boolean isUptakingSiderophore(Siderophore mol);
    
    //public abstract void secreteSiderophore(Siderophore mol, int x, int y, int z);
	
}
