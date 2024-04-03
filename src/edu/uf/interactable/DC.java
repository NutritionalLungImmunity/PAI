package edu.uf.interactable;

import edu.uf.interactable.klebsiela.Klebsiella;
import edu.uf.intracellularState.BooleanNetwork;
import edu.uf.intracellularState.Phenotype;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;
import edu.uf.utils.Rand;

public class DC extends Leukocyte{

    public static final String NAME = "Macrophage";

    private static String chemokine = null;
    
    private static int totalCells = 0;
    private static double totalIron = 0; 
    
    public static final int M1  = Phenotype.createPhenotype();
	public static final int M2A = Phenotype.createPhenotype();
	public static final int M2B = Phenotype.createPhenotype();
	public static final int M2C = Phenotype.createPhenotype();
    
    private int maxMoveStep;
    private boolean engaged;
    private static int interactionId = Id.getMoleculeId();
    
    
	public DC(BooleanNetwork network) {
    	super(0.0, network);
    	DC.totalCells = DC.totalCells + 1;
        this.setState(Macrophage.FREE);
        this.maxMoveStep = -1; 
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
    	if(interactable instanceof Klebsiella) {
    		Klebsiella.intKlebsiela(this, (Klebsiella) interactable);
    		return true;
    	}
    	if(interactable instanceof IFN_I) {
    		IFN_I mol = (IFN_I) interactable;
    		if (this.hasPhenotype(mol.getPhenotype()))
    			mol.inc(Constants.MA_IFN_I_QTTY, 0, x, y, z);
    		return true;
    	}
    	if(interactable instanceof IFN_III) {
    		IFN_III mol = (IFN_III) interactable;
    		if (this.hasPhenotype(mol.getPhenotype()))
    			mol.inc(Constants.MA_IFN_III_QTTY, 0, x, y, z);
    		return true;
    	}
    	if(interactable instanceof IL23) {
    		IL23 mol = (IL23) interactable;
    		if (this.hasPhenotype(mol.getPhenotype()))
    			mol.inc(Constants.MA_IL23_QTTY, 0, x, y, z);
    		return true;
    	}
    	
    	
        return interactable.interact(this, x, y, z);
    }


    public void updateStatus(int x, int y, int z) {
    	super.updateStatus(x, y, z);
    	if(!this.getClock().toc())return;
    	
    	this.processBooleanNetwork();
    	
        if(this.getStatus() == DC.DEAD)
            return;
        if(this.getStatus() == DC.NECROTIC) {
            this.die();
            //for(InfectiousAgent entry : this.getPhagosome()) //WARNING-COMMENT
            //	entry.setState(Afumigatus.RELEASING);
        }else if(this.getPhagosome().size() > Constants.MA_MAX_CONIDIA)
            this.setStatus(Leukocyte.NECROTIC);
        
        if(Rand.getRand().randunif() < Constants.DC_HALF_LIFE && this.getPhagosome().size() == 0 &&
        		DC.totalCells > Constants.MIN_MA) 
            this.die();
        //this.setMoveStep(0);
        this.maxMoveStep = -1;
    }
        

    public void incIronPool(double qtty) {}

    public void die() {
        if(this.getStatus() != DC.DEAD) {
            this.setStatus(DC.DEAD);
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
