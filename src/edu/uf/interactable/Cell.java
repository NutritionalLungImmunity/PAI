package edu.uf.interactable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.uf.compartments.Voxel;
import edu.uf.intracellularState.BooleanNetwork;
import edu.uf.intracellularState.EukaryoteSignalingNetwork;
import edu.uf.intracellularState.Phenotypes;
import edu.uf.time.Clock;

public abstract class Cell extends Interactable{
	
	private int id;
	
	public static final int IT_CLOCK = 0;
	public static final int ST_CLOCK = 1;
	public static final int BN_CLOCK = 2;
	
	//private int phenotype = Phenotypes.RESTING;
	private List<Integer> phenotypes = new ArrayList<>(10);
	
	public static final int ALIVE = 0;
	public static final int APOPTOTIC = 1;
	public static final int NECROTIC = 2;
	public static final int DYING = 3;
	public static final int DEAD = 4;
	
    
    private double ironPool;
    private int status;
    private int state;
    private boolean engulfed;
    protected BooleanNetwork booleanNetwork;
    protected Clock clock;
    
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
	
	public void addPhenotype(int phenotype) {
		this.phenotypes.add(phenotype);
	}
	
	public void clearPhenotype() {
		this.phenotypes.clear();
	}
	
	public boolean inPhenotype(int phenotype) {
		for(Integer i : phenotypes) 
			if(phenotype == i)return true;
		return false;
	}
	
	public boolean inPhenotype(List<Integer> phenotype) {
		for(Integer i : phenotypes) 
			for(int j : phenotype)
				if(j == i)return true;
		return false;
	}
	
	public boolean inPhenotype(int[] phenotype) {
		for(Integer i : phenotypes) 
			for(int j : phenotype)
				if(j == i)return true;
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


	public Cell() {
		this.id = Id.getId(); 
	}
	
	public void bind(int molIdx) {
		createBooleanNetwork().activateReceptor(molIdx, 1);
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

    public abstract void updateStatus();


    public abstract void move(Voxel oldVoxel, int steps);

    public abstract void die();
    
    public abstract void incIronPool(double ironPool);
    
    public abstract int getMaxMoveSteps();

    public String attractedBy() {
        return null;
    }
	
}
