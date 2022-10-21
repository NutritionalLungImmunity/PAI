package edu.uf.main.initialize;

import java.util.List;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.Voxel;
import edu.uf.interactable.Pneumocyte;

public class InitializeBaseInvitroModel extends InitializeBaseModel{

	@Override
	public void initializeMolecules(Voxel[][][] grid, int xbin, int ybin, int zbin, Diffuse diffuse, boolean verbose) {
		// TODO Auto-generated method stub
		this.setSecretionPhenotypes();
	}

	@Override
	public void initializeLiver(Voxel[][][] grid, int xbin, int ybin, int zbin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Pneumocyte> initializePneumocytes(Voxel[][][] grid, int xbin, int ybin, int zbin, int numCells) {
		return null;
		
	}

	@Override
	public void initializeErytrocytes(Voxel[][][] grid, int xbin, int ybin, int zbin) {
		// TODO Auto-generated method stub
		
	}

}
