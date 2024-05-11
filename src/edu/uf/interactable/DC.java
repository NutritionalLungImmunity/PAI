package edu.uf.interactable;

import edu.uf.interactable.klebsiela.Klebsiella;
import edu.uf.intracellularState.IntracellularModel;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class DC extends Leukocyte{

    public static final String NAME = "Macrophage";

    private static String chemokine = null;
    
    private static int totalCells = 0;
    private static double totalIron = 0; 
    
    private int maxMoveStep;
    private boolean engaged;
    private static int interactionId = Id.getMoleculeId();
    
    
	public DC(IntracellularModel network) {
    	super(0.0, network);
    	DC.totalCells = DC.totalCells + 1;
        this.maxMoveStep = -1; 
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
		DC.chemokine = chemokine;
	}

	public static int getTotalCells() {
		return totalCells;
	}

	public static void setTotalCells(int totalCells) {
		DC.totalCells = totalCells;
	}

	public static double getTotalIron() {
		return totalIron;
	}

	public static void setTotalIron(double totalIron) {	}

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
    	if(interactable instanceof Klebsiella) 
    		return Interactions.intKlebsiella(this, (Klebsiella) interactable);
    	
    	if(interactable instanceof IFN_I) 
    		return Interactions.secrete(this, (IFN_I) interactable, Constants.MA_IFN_I_QTTY, x, y, z, 0);
    	
    	if(interactable instanceof IFN_III) 
    		return Interactions.secrete(this, (IFN_III) interactable, Constants.MA_IFN_III_QTTY, x, y, z, 0);
    	
    	if(interactable instanceof IL23) 
    		return Interactions.secrete(this, (IL23) interactable, Constants.MA_IL23_QTTY, x, y, z, 0);
    	
        return interactable.interact(this, x, y, z);
    }
        

    public void incIronPool(double qtty) {}

    public void die() {
    	if(this.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) != Cell.DEAD) {
    		this.getBooleanNetwork().setState(IntracellularModel.LIFE_STATUS, Cell.DEAD);
            DC.totalCells = DC.totalCells - 1;
        }
    }

    public String attractedBy() {
        return DC.chemokine;
    }

	@Override
	public int getMaxCell() {
		return Constants.MA_MAX_CONIDIA;
	}
	
	public String getName() {
    	return NAME;
    }
	
}
