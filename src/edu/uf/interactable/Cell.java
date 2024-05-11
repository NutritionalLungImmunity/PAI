package edu.uf.interactable;

import edu.uf.compartments.Voxel;
import edu.uf.intracellularState.IntracellularModel;
import edu.uf.intracellularState.Phenotype;
import edu.uf.time.Clock;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;

public abstract class Cell extends Interactable{
	
	public static final int DEAD = Phenotype.createPhenotype();;
	public static final int ALIVE = Phenotype.createPhenotype();
	public static final int APOPTOTIC = Phenotype.createPhenotype();
	public static final int NECROTIC = Phenotype.createPhenotype();
	public static final int DYING = Phenotype.createPhenotype();
	
	private int id;
    
    private double ironPool;
    //private int status;
    private int state;
    private boolean engulfed;
    protected Clock clock;
    protected IntracellularModel intracellularModel;
    private int externalState;
    
    
    public Cell(IntracellularModel intracellularModel) {
    	this.intracellularModel = intracellularModel;
    	this.clock = new Clock((int) Constants.INV_UNIT_T);
    	this.id = Id.getId(); 
    	this.externalState = 0;
    }
    
    public void setExternalState(int state) {
    	this.externalState = state;
    }
    
    public int getExternalState() {
    	return this.externalState;
    }


	public double getIronPool() {
		return ironPool;
	}


	public void setIronPool(double ironPool) {
		this.ironPool = ironPool;
	}


	public boolean isEngulfed() {
		return engulfed;
	}


	public void setEngulfed(boolean engulfed) {
		this.engulfed = engulfed;
	}

	
	public int getId() {
    	return id;
    }


	public void setId(int id) {
		this.id = id;
	}
	
	public void bind(Binder iter, int level) {
		this.intracellularModel.activateReceptor(iter.getInteractionId(), level);
	}
	
	public Clock getClock() {
		return clock;
	}


	/*protected void processBooleanNetwork(int... args) {
		this.intracellularModel.processBooleanNetwork(args);
    }*/
	
	public boolean isDead() {
		return this.intracellularModel.isDead();
	}

    public void updateStatus(int x, int y, int z) {
    	this.clock.tic();
    	if(!this.getClock().toc())return;
    	this.intracellularModel.processBooleanNetwork();
    	this.intracellularModel.updateStatus(this, x, y, z);
    }
    
    public IntracellularModel getBooleanNetwork() {
    	return this.intracellularModel;
    }
    
    public boolean removeUponDeath() {
    	return true;
    }


    public abstract void move(Voxel oldVoxel, int steps);

    public abstract void die();
    
    public abstract void incIronPool(double ironPool);
    
    public abstract int getMaxMoveSteps();

    public String attractedBy() {
        return null;
    }
	
}
