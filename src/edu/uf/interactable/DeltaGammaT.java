package edu.uf.interactable;

import edu.uf.compartments.Voxel;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;
import edu.uf.utils.Rand;

public class DeltaGammaT extends Cell{

	public static final String NAME = "DeltaGamma_T_lymphocytes";
	private static int totalCells = 0;
	private static int interactionId = Id.getMoleculeId();

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

	@Override
	public void move(Voxel oldVoxel, int steps) {}

	@Override
	public void die() {
		if(this.getStatus() != Leukocyte.DEAD) {
            this.setStatus(Neutrophil.DEAD);  //##CAUTION!!!
            DeltaGammaT.totalCells--;
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
	
	public void updateStatus(int x, int y, int z) {
    	super.updateStatus(x, y, z);
    	if(!this.getClock().toc())return;
    	this.processBooleanNetwork();
    }

	@Override
	protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
		return interactable.interact(this, x, y, z);
	}

}
