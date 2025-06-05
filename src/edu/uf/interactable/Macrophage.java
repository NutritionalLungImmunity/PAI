package edu.uf.interactable;

import edu.uf.intracellularState.IntracellularModel;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;
import edu.uf.utils.Rand;


public class Macrophage extends Leukocyte{

    public static final String NAME = "Macrophage";

    private static String chemokine = null;
    
    private static int totalCells = 0;
    private static double totalIron = 0; 
    
    private int maxMoveStep;
    private boolean engaged;
    private static int interactionId = Id.getId();
    
    
    
	public Macrophage(double ironPool, IntracellularModel network) {
    	super(ironPool, network);
    	Macrophage.totalCells = Macrophage.totalCells + 1;
        this.maxMoveStep = -1; 
        Macrophage.totalIron = Macrophage.totalIron + ironPool;
    }
	
	public void setMaxMoveStep(int moveStep) {
		this.maxMoveStep = moveStep;
	}
	
	public int getInteractionId() {
		return interactionId;
	}
	
	public boolean isTime() {
		return this.getClock().toc();
	}
    
    public static String getChemokine() {
		return chemokine;
	}

	public static void setChemokine(String chemokine) {
		if(chemokine == null)
			Macrophage.chemokine = chemokine;
	}

	public static int getTotalCells() {
		return totalCells;
	}

	public static void setTotalCells(int totalCells) {
		Macrophage.totalCells = totalCells;
	}

	public static double getTotalIron() {
		return totalIron;
	}

	public static void setTotalIron(double totalIron) {
		Macrophage.totalIron = totalIron;
	}

	public boolean isEngaged() {
		return engaged;
	}

	public void setEngaged(boolean engaged) {
		this.engaged = engaged;
	}

    public int getMaxMoveSteps() { 
    	double r = 1.0;
    	//if(this.getExternalState() == 1)r = Constants.NET_COUNTER_INHIBITION;
        if(this.maxMoveStep == -1) {
        	//this.maxMoveStep = Rand.getRand().randunif() < Constants.MA_MOVE_RATE_ACT ? 1 : 0;
        	this.maxMoveStep = Rand.getRand().randpois(Constants.MA_MOVE_RATE_ACT*r);
        }
            //
        return this.maxMoveStep;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        return interactable.interact(this, x, y, z);
    }
        
    public void incIronPool(double qtty) {
        this.setIronPool(this.getIronPool() + qtty);
        Macrophage.totalIron = Macrophage.totalIron + qtty;
    }

    public void die() {
    	if(this.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) != IntracellularModel.DEAD) {
    		this.getBooleanNetwork().setState(IntracellularModel.LIFE_STATUS, IntracellularModel.DEAD);
            Macrophage.totalCells = Macrophage.totalCells - 1;
        }
    }

    public String attractedBy() {
        return Macrophage.chemokine;
    }

	@Override
	public int getMaxCell() {
		return Constants.MA_MAX_CONIDIA;
	}
	
	public String getName() {
    	return NAME;
    }
	
}
