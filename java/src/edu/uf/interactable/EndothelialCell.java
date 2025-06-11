package edu.uf.interactable;

import edu.uf.intracellularState.IntracellularModel;
import edu.uf.intracellularState.NeutrophilStateModel;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;
import edu.uf.utils.Rand;

public class EndothelialCell extends Cell{

    public static final String NAME = "EC";

    private static int totalCells = 0;
    
    
    
    private static int interactionId = Id.getId();

    public EndothelialCell(IntracellularModel network) {
        super(network);
        EndothelialCell.totalCells++;
    }

    
	public static void setTotalCells(int totalCells) {
		EndothelialCell.totalCells = totalCells;
	}

    
	
	
	
	
	public void die() {
		if(this.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) != IntracellularModel.DEAD) {
    		this.getBooleanNetwork().setState(IntracellularModel.LIFE_STATUS, IntracellularModel.DEAD);
    		EndothelialCell.totalCells--;
        }
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof PneumocyteI) {
        	Cell cell = (Cell) interactable;
        	if(cell.isDead()) 
        		this.bind(TLRBinder.getBinder(), 4);
            return true;
        }
        if(interactable instanceof MIP2) {
        	Molecule mol = (Molecule) interactable;
        	if(this.getBooleanNetwork().hasPhenotype(mol)) 
        		mol.inc(Constants.MA_MIP2_QTTY, 0, x, y, z);
        	return true;
        }
        if(interactable instanceof IL4) {
        	Molecule mol = (Molecule) interactable;
        	if(this.getBooleanNetwork().hasPhenotype(mol)) 
        		mol.inc(Constants.IL4_QTTY, 0, x, y, z);
        	return true;
        }
        if(interactable instanceof Neutrophil) {
        	Neutrophil neutr = (Neutrophil) interactable;
        	if(neutr.getBooleanNetwork().hasPhenotype(NeutrophilStateModel.NETOTIC) && Rand.getRand().randunif() < Constants.PR_NET_KILL_EPI*0.01) 
        		this.getBooleanNetwork().setState(IntracellularModel.LIFE_STATUS, IntracellularModel.APOPTOTIC);
        	return true;
        }
		
        return interactable.interact(this, x, y, z);
    }

    public void incIronPool(double qtty) {}
    
    public void move(int x, int y, int z, int steps) {}

    public int getInteractionId() {
    	return interactionId;
    }
    
    public boolean isTime() {
		return this.getClock().toc();
	}

    public static int getTotalCells() {
		return totalCells;
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
	
}
