package edu.uf.interactable;

import edu.uf.compartments.Voxel;
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.intracellularState.BooleanNetwork;
import edu.uf.intracellularState.Phenotype;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;
import edu.uf.utils.Util;

public class PneumocyteII extends Cell {
    public static final String NAME = "Pneumocyte";

    private static int totalCells = 0;
    
    private int iteration;
    
    public static final int ACTIVE = Phenotype.createPhenotype();
    public static final int MIX_ACTIVE = Phenotype.createPhenotype();
    
    
    private static int interactionId = Id.getMoleculeId();

    public PneumocyteII(BooleanNetwork network) {
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
        if(this.getStatus() != Leukocyte.DEAD) {
            this.setStatus(Neutrophil.DEAD);  //##CAUTION!!!
            PneumocyteII.totalCells--;
        }
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Afumigatus) {
            Afumigatus interac = (Afumigatus) interactable;
            //interac.setEpithelialInhibition(1*Constants.ITER_TO_GROW);
        	if(!this.isDead()) 
                if(interac.getStatus() != Afumigatus.RESTING_CONIDIA) 
                    //if(!this.hasPhenotype(Macrophage.M1))  //REVIEW
                    	this.bind(interac, 4);
                        //if(Rand.getRand().randunif() < Constants.PR_P_INT) 
                        //	this.bind(Afumigatus.RECEPTOR_IDX);
            return true;
        }
        
        if (interactable instanceof IL6) { 
        	if(this.hasPhenotype(((IL6)interactable).getPhenotype())) 
            	((Molecule)interactable).inc(Constants.P_IL6_QTTY, 0, x, y, z);
            return true;
        }
        
        if (interactable instanceof TNFa) {
            Molecule interact = (Molecule) interactable;
            //System.out.println(interact.get(0, x, y, z) + " " + Util.activationFunction5(interact.get(0, x, y, z), Constants.Kd_TNF));
            this.bind(interactable, Util.activationFunction5(interact.get(0, x, y, z), Constants.Kd_TNF));
        	if(this.hasPhenotype(interact.getPhenotype())) 
            	interact.inc(Constants.P_TNF_QTTY, 0, x, y, z);
            return true;
        }
        
        /*if (interactable instanceof IL10) {
            Molecule interact = (Molecule) interactable;
            EukaryoteSignalingNetwork.IL10_e = IL10.MOL_IDX;
        	if (Util.activationFunction(interact.values[0][x][y][z], Constants.Kd_IL10, this.getClock())) 
        		this.bind(IL10.MOL_IDX);
            return true;
        }*/
        
        /*if (interactable instanceof TGFb) {
            Molecule interact = (Molecule) interactable;
            EukaryoteSignalingNetwork.TGFb_e = TGFb.MOL_IDX;
        	if (Util.activationFunction(interact.values[0][x][y][z], Constants.Kd_TGF, this.getClock())) 
        		this.bind(TGFb.MOL_IDX);
            return true;
        }*/
		
        return interactable.interact(this, x, y, z);
    }

    public void incIronPool(double qtty) {}

    public void updateStatus(int x, int y, int z) {
    	super.updateStatus(x, y, z);
    	if(!this.getClock().toc())return;
    	this.processBooleanNetwork();
    	if(this.hasPhenotype(new int[] {Leukocyte.APOPTOTIC})) //I DON'T KNOW IF THIS CODE WORKS
			this.die();
    }
            
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