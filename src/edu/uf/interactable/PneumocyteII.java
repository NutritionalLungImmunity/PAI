package edu.uf.interactable;

import edu.uf.compartments.Voxel;
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.intracellularState.IntracellularModel;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;

public class PneumocyteII extends Cell {
    public static final String NAME = "Pneumocyte";

    private static int totalCells = 0;
    
    private int iteration;
    
    /*public static final int ACTIVE = Phenotype.createPhenotype();
    public static final int MIX_ACTIVE = Phenotype.createPhenotype();*/
    
    
    private static int interactionId = Id.getMoleculeId();

    public PneumocyteII(IntracellularModel network) {
        super(network);
        this.iteration = 0;
        PneumocyteII.totalCells = PneumocyteII.totalCells + 1;
    }
    
    public int getInteractionId() {
    	return interactionId;
    }
    
    public boolean isTime() {
		return this.getClock().toc();
	}

    public static int getTotalCells() {
		return totalCells;
	}

	public static void setTotalCells(int totalCells) {
		PneumocyteII.totalCells = totalCells;
	}

	public int getIteration() {
		return iteration;
	}

    
	public void die() {
		if(this.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) != Cell.DEAD) {
    		this.getBooleanNetwork().setState(IntracellularModel.LIFE_STATUS, Cell.DEAD);
            PneumocyteII.totalCells--;
        }
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Afumigatus) 
            return Interactions.typeIIPneumocyteAspergillus(this, (Afumigatus) interactable);
        
        if (interactable instanceof IL6) 
        	return Interactions.secrete(this, (IL6) interactable, Constants.MA_IL6_QTTY, x, y, z, 0); 
        
        if (interactable instanceof TNFa) {
        	Interactions.bind(this, (TNFa) interactable, x, y, z, 0);
            return Interactions.secrete(this, (TNFa) interactable, Constants.MA_TNF_QTTY, x, y, z, 0); 
        }
		
        return interactable.interact(this, x, y, z);
    }

    public void incIronPool(double qtty) {}

    /*public void updateStatus(int x, int y, int z) {
    	super.updateStatus(x, y, z);
    	if(!this.getClock().toc())return;
    	this.processBooleanNetwork();
    	if(this.getBooleanNetwork().hasPhenotype(IntracellularModel.APOPTOTIC))//I STILL DON'T KNOW IF THIS CODE WORKS
    		this.die();
    	//if(this.hasPhenotype(new int[] {Leukocyte.APOPTOTIC})) //I DON'T KNOW IF THIS CODE WORKS
			
    }*/
           
    /**
     * Disabled.
     */
    public void move(Voxel oldVoxel, int steps) {}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public int getMaxMoveSteps() {
		// TODO Auto-generated method stub
		return -1;
	}
}