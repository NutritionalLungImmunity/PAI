package edu.uf.interactable;

import edu.uf.compartments.Voxel;
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;
import edu.uf.utils.Rand;

public class PneumocyteI extends Cell{
	
	public static final String NAME = "TypeI_Pneumocyte";
	private static int totalCells = 0;
	private static int interactionId = Id.getMoleculeId();
	
	private boolean injury;

	public PneumocyteI() {
		super(null);
		totalCells++;
		injury = false;
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
	
	public void updateStatus(int x, int y, int z) {
    	super.updateStatus(x, y, z);
    	injury = false;
    }

	@Override
	protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
		if(interactable instanceof Afumigatus) {
			if(this.isDead())return true;
			Afumigatus a = (Afumigatus) interactable;
			if(this.isDead())a.setEpithelialInhibition(1);
			if(a.getStatus() == Afumigatus.HYPHAE) {
					//if(injury || (a.getAspEpiInt() && Rand.getRand().randunif() < Constants.PR_ASP_KILL_EPI)) //*0.5
					if((a.getAspEpiInt() && Rand.getRand().randunif() < Constants.PR_ASP_KILL_EPI)) //*0.5
						this.die();
					else
						injury = true;
				a.setAspEpiInt(false);
				
			}
			
			//a.set
			//if(a.getStatus() == Afumigatus.HYPHAE)this.die();
			return true;
		}
		return false;
	}

}
