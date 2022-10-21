package edu.uf.interactable;

import edu.uf.compartments.Voxel;

public abstract class InfectiousAgent extends Cell{
    
	public void grow(int xbin, int ybin, int zbin, Voxel[][][] grid) {
		grow(xbin, ybin, zbin, grid, null);
	}
	
    public abstract void grow(int xbin, int ybin, int zbin, Voxel[][][] grid, Phagocyte phagocyte);
	
}
