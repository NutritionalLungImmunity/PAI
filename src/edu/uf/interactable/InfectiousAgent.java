package edu.uf.interactable;

import edu.uf.compartments.Voxel;
import edu.uf.intracellularState.BooleanNetwork;

public abstract class InfectiousAgent extends Cell{
    
	public InfectiousAgent(BooleanNetwork booleanNetwork) {
		super(booleanNetwork);
	}

	public void grow(int x, int y, int z, int xbin, int ybin, int zbin) {
		grow(x, y, z, xbin, ybin, zbin, null);
	}
	
    public abstract void grow(int x, int y, int z, int xbin, int ybin, int zbin, Leukocyte phagocyte);
	
}
