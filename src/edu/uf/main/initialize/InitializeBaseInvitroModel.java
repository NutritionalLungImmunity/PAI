package edu.uf.main.initialize;

import java.util.List;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.Voxel;
import edu.uf.interactable.PneumocyteII;

public class InitializeBaseInvitroModel extends InitializeBaseModel{

	@Override
	public void initializeMolecules( Diffuse diffuse, boolean verbose) {
		// TODO Auto-generated method stub
		//this.setSecretionPhenotypes();
	}

	@Override
	public void initializeLiver() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<PneumocyteII> initializePneumocytes(int numCells) {
		return null;
		
	}

}
