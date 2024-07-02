package edu.uf.interactable;

import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.intracellularState.IntracellularModel;
import edu.uf.intracellularState.NeutrophilStateModel;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;
import edu.uf.utils.Rand; 

public class Neutrophil extends Leukocyte{
    public static final String NAME = "Neutrophils";


    private static String chemokine;
    private static int totalCells = 0;
    private static double totalIron = 0;
    private int maxMoveStep;
    private boolean degranulated = false;
    private double netHalfLife;
    
    
    public boolean depleted = false;
    private boolean control;
    
    private static int interactionId = Id.getMoleculeId();

    public Neutrophil(double ironPool, IntracellularModel network) {
    	super(ironPool, network);
        Neutrophil.totalCells = Neutrophil.totalCells + 1;
        Neutrophil.totalIron = Neutrophil.totalIron + ironPool;
        this.maxMoveStep = -1;
        this.setEngaged(false);
        this.control = true;
        this.netHalfLife = Constants.NET_HALF_LIFE;
    } 
    
    public void setMaxMoveStep(int moveStep) {
		this.maxMoveStep = moveStep;
	}
    
    public double getNetHalfLife() {
    	return this.netHalfLife;
    }
    
    public void setNETHalfLife(double halfLife) {
    	this.netHalfLife = halfLife;
    }
    
    public int getInteractionId() {
    	return interactionId;
    }
    
    public boolean isTime() {
		return this.getClock().toc();
	}
    
    public boolean hasDegranulated() {
    	return this.degranulated;
    }
    
    public void degranulate() {
    	this.degranulated = true;
    }
    
    public static String getChemokine() {
		return chemokine;
	}

	public static void setChemokine(String chemokine) {
		Neutrophil.chemokine = chemokine;
	}

	public static int getTotalCells() {
		return totalCells;
	}

	public static void setTotalCells(int totalCells) {
		Neutrophil.totalCells = totalCells;
	}

	public static double getTotalIron() {
		return totalIron;
	}

	public static void setTotalIron(double totalIron) {
		Neutrophil.totalIron = totalIron;
	}

    public int getMaxMoveSteps(){// ##REVIEW
    	double r = 1.0;
    	//if(this.getExternalState() == 1)r = Constants.NET_COUNTER_INHIBITION;
        if(this.maxMoveStep == -1)
            this.maxMoveStep = Rand.getRand().randpois(Constants.MA_MOVE_RATE_REST*r);
        return this.maxMoveStep;
    }

    public void incIronPool(double qtty) {
        this.setIronPool(this.getIronPool() + qtty);
        Neutrophil.totalIron += qtty;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if(interactable instanceof Afumigatus) 
        	return Interactions.neutrophilAspergillu(this, (Afumigatus) interactable);
        
        if(interactable instanceof Macrophage) 
        	return Interactions.macrophagePhagApoptoticNeutrophilS(this, (Macrophage) interactable);

        if(interactable instanceof PneumocyteI) {
        	control = Interactions.typeIPneumocyteNET(this, (PneumocyteI) interactable, control);
        	return true;
		}
        if (interactable instanceof Iron) 
        	return Interactions.releaseIron(this, (Iron) interactable, NeutrophilStateModel.NETOTIC, x, y, z);
        
        return interactable.interact(this, x, y, z);
    }

    public void die() {
    	if(this.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) != IntracellularModel.DEAD) {
    		this.getBooleanNetwork().setState(IntracellularModel.LIFE_STATUS, IntracellularModel.DEAD);
            Neutrophil.totalCells = Neutrophil.totalCells - 1;
        }
    }

    public String attractedBy() {
        return Neutrophil.chemokine;
    }

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public int getMaxCell() {
		return Constants.N_MAX_CONIDIA;
	}

}
