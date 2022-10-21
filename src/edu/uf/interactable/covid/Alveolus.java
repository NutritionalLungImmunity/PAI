package edu.uf.interactable.covid;

import edu.uf.compartments.Voxel;
import edu.uf.interactable.Cell;
import edu.uf.interactable.Interactable;
import edu.uf.intracellularState.BooleanNetwork;

public class Alveolus extends Cell{
	
	public static final String NAME = "Alveolus";

	@Override
	protected BooleanNetwork createNewBooleanNetwork() {
		return null;
	}

	@Override
	public void updateStatus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void move(Voxel oldVoxel, int steps) {}

	@Override
	public void die() {}

	@Override
	public void incIronPool(double ironPool) {}

	@Override
	public int getMaxMoveSteps() {
		return 0;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
		// TODO Auto-generated method stub
		return false;
	}

}
