package edu.uf.interactable;

import java.util.List;

import edu.uf.compartments.Voxel;
import edu.uf.intracellularState.BooleanNetwork;
import edu.uf.time.Clock;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;

public abstract class Cell extends Interactable{
	
	private int id;
	
	//private int phenotype = Phenotypes.RESTING;
	//private List<Integer> phenotypes = new ArrayList<>(10);
	
	public static final int ALIVE = 0;
	public static final int APOPTOTIC = 1;
	public static final int NECROTIC = 2;
	public static final int DYING = 3;
	public static final int DEAD = 4;
	
    
    private double ironPool;
    private int status;
    private int state;
    private boolean engulfed;
    protected Clock clock;
    protected BooleanNetwork booleanNetwork;
    
    
    public Cell() {
    	this.clock = new Clock((int) Constants.INV_UNIT_T);
    	this.id = Id.getId(); 
    }
    
    public final BooleanNetwork createBooleanNetwork() {
    	if(booleanNetwork == null) {
    		booleanNetwork = createNewBooleanNetwork();
    	}
    	return booleanNetwork;
    }
    
    protected abstract BooleanNetwork createNewBooleanNetwork();


	public double getIronPool() {
		return ironPool;
	}


	public void setIronPool(double ironPool) {
		this.ironPool = ironPool;
	}


	public int getState() {
		return state;
	}


	public void setState(int state) {
		this.state = state;
	}
	
	/*public void addPhenotype(int phenotype) {
		this.phenotypes.add(phenotype);
	}
	
	public void clearPhenotype() {
		this.phenotypes.clear();
	}*/
	
	public boolean hasPhenotype(int phenotype) {
		return createBooleanNetwork().getPhenotype().containsKey(phenotype);
	}
	
	public boolean hasPhenotype(List<Integer> phenotype) {
		for(Integer p : phenotype)
			if(createBooleanNetwork().getPhenotype().containsKey(p))
				return true;
		return false;
	}
	
	public boolean hasPhenotype(int[] phenotype) {
		for(Integer p : phenotype)
			if(createBooleanNetwork().getPhenotype().containsKey(p))
				return true;
		return false;
	}
	
	/*public void setPhenotype(int phenotype) {
		this.phenotype = phenotype;
	}
	
	public int getPhenotype() {
		return this.phenotype;
	}*/


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
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
	
	public void bind(Interactable iter, int level) {
		createBooleanNetwork().activateReceptor(iter.getInteractionId(), level);
	}
	
	public Clock getClock() {
		return clock;
	}


	protected void processBooleanNetwork() {
		this.createBooleanNetwork().processBooleanNetwork();
    }
	
	public boolean isDead() {
		return status == DEAD || status == DYING || status == APOPTOTIC || status == NECROTIC;
	}

    public void updateStatus() {
    	this.clock.tic();
    }


    public abstract void move(Voxel oldVoxel, int steps);

    public abstract void die();
    
    public abstract void incIronPool(double ironPool);
    
    public abstract int getMaxMoveSteps();

    public String attractedBy() {
        return null;
    }
	
}
