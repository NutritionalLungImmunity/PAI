package edu.uf.interactable;

import edu.uf.compartments.Voxel;
import edu.uf.intracellularState.BooleanNetwork;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;
import edu.uf.utils.Rand;

public class Erythrocyte extends Cell{
	
	public static final String NAME = "Erythrocyte";
	private boolean hemorrhage;
	
	private static int totalCells = 0;
	
	
	private int restingRBC;
	private int burstRBC;
	
	public static final int RESTING = 0;
	public static final int HEMORRHAGE = 1;
	public static final int BURSTING = 2;
	public static final int DEAD = 3;
	
	private static int interactionId = Id.getMoleculeId();
	
	public Erythrocyte(int rbc) {
		super();
		this.restingRBC = rbc;
		this.hemorrhage = false; 
		totalCells += rbc;
	}
	
	public int getInteractionId() {
    	return interactionId;
    }
	
	public static int getTotalCells() {
		return totalCells;
	}
	
	public int getBurst() {
		return this.burstRBC;
	}
	
	public int getResting() {
		return this.restingRBC;
	}
	
	public void setBurst(int burst) {
		this.burstRBC = burst;
	}
	
	public  void setHemorrhage(boolean hemorrhage) {
		this.hemorrhage = hemorrhage;
	}

	@Override
	protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
		if(interactable instanceof Afumigatus) {
			if(((Afumigatus) interactable).getStatus() == Afumigatus.HYPHAE) 
				this.hemorrhage = true;
			return true;
		}
		return interactable.interact(this, x, y, z);
	}

	@Override
	public void processBooleanNetwork() {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateStatus() {
		if(this.hemorrhage) {
			//turnover = turnover_rate;
			double burst = Rand.getRand().randpois(Constants.HEMOLYSIS_RATE * this.restingRBC);
			//this.restingRBC -= burst; //+turnover;
			this.burstRBC += burst; // -turnover;
			//totalCells -= burst;
		}
	}

	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void move(Voxel oldVoxel, int steps) {
		// TODO Auto-generated method stub
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub
	}

	@Override
	public void incIronPool(double ironPool) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public int getMaxMoveSteps() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	protected BooleanNetwork createNewBooleanNetwork() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean isTime() {
		return true;
	}

}
