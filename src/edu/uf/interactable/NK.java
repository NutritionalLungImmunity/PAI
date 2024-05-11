package edu.uf.interactable;

import edu.uf.compartments.Voxel;
import edu.uf.intracellularState.IntracellularModel;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;
import edu.uf.utils.Rand;

public class NK extends Cell{

	public static final String NAME = "NK";
	private static int totalCells = 0;
	private static int interactionId = Id.getMoleculeId();

	public NK(IntracellularModel model) {
		super(model);
		totalCells++;
	}
	
	public static int getTotalCells() {
		return totalCells;
	}

    public int getInteractionId() {
    	return interactionId;
    }

	@Override
	public void move(Voxel oldVoxel, int steps) {}

	@Override
	public void die() {
		if(this.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) != Cell.DEAD) {
    		this.getBooleanNetwork().setState(IntracellularModel.LIFE_STATUS, Cell.DEAD);
            NK.totalCells--;
        }
    }
	
	public boolean isInjury() {
		return false;
	}

	public void setInjury(boolean b) {}
	
	@Override
	public void incIronPool(double ironPool) {}

	@Override
	public int getMaxMoveSteps() {
		return Rand.getRand().randpois(Constants.MA_MOVE_RATE_ACT);
	}

	@Override
	public boolean isTime() {
		return this.getClock().toc();
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public boolean removeUponDeath() {
		return true;
	}

	@Override
	protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
		if (interactable instanceof IFN_I) 
			return Interactions.bind(this, (IFN_I) interactable, x, y, z, 0);
        
		return interactable.interact(this, x, y, z);
	}

}
