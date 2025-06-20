package edu.uf.interactable;

import edu.uf.intracellularState.IntracellularModel;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;
import edu.uf.utils.Rand;

public class DeltaGammaT extends Cell{

	public static final String NAME = "DeltaGamma_T_lymphocytes";
	private static int totalCells = 0;
	private static int interactionId = Id.getId();

	public DeltaGammaT() {
		super(null);
		totalCells++;
	}
	
	public static int getTotalCells() {
		return totalCells;
	}

    public int getInteractionId() {
    	return interactionId;
    }

    /**
	 * Disabled. <strong> Review!</strong>
	 * @param b
	 */
	@Override
	public void move(int x, int y, int z, int steps) {}

	@Override
	public void die() {
		if(this.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) != IntracellularModel.DEAD) {
    		this.getBooleanNetwork().setState(IntracellularModel.LIFE_STATUS, IntracellularModel.DEAD);
            DeltaGammaT.totalCells = DeltaGammaT.totalCells - 1;
        }
    }
	
	public boolean isInjury() {
		return false;
	}

	/**
	 * Disabled.
	 * @param b
	 */
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
		return interactable.interact(this, x, y, z);
	}

}
