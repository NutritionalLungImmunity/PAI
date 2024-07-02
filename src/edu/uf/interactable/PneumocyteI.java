package edu.uf.interactable;

import edu.uf.compartments.Voxel;
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.intracellularState.IntracellularModel;
import edu.uf.intracellularState.Phenotype;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Id;

public class PneumocyteI extends Cell{
	
	public static final String NAME = "TypeI_Pneumocyte";
	private static int totalCells = 0;
	private static int interactionId = Id.getMoleculeId();
	
	public static final int OPEN = Phenotype.createPhenotype();
	
	private boolean injury;

	public PneumocyteI(IntracellularModel model) {
		super(model);
		totalCells++;
		injury = false;
	}
	
	public static int getTotalCells() {
		return totalCells;
	}

    public int getInteractionId() {
    	return interactionId;
    }

    /**
     * Disabled.
     */
	@Override
	public void move(int x, int y, int z, int steps) {}

	@Override
	public void die() {
		if(this.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) != IntracellularModel.DEAD) {
    		this.getBooleanNetwork().setState(IntracellularModel.LIFE_STATUS, IntracellularModel.DEAD);
            PneumocyteI.totalCells--;
        }
    }
	
	public boolean isInjury() {
		return injury;
	}

	public void setInjury(boolean b) {
		this.injury = b;
	}
	
	@Override
	public void incIronPool(double ironPool) {}

	@Override
	public int getMaxMoveSteps() {
		return 0;
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
		return false;
	}
	
	/*public void updateStatus(int x, int y, int z) {
    	super.updateStatus(x, y, z);
    	injury = false;
    	if(this.getStatus() != null)this.getStatus().updateStatus(this, x, y, z);
    }*/

	@Override
	protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
		if(interactable instanceof Afumigatus) {
			injury = Interactions.typeIPneumocyteAspergillus(this, (Afumigatus) interactable, injury);
			return true;
		}
		
		return interactable.interact(this, x, y, z);
	}

}
