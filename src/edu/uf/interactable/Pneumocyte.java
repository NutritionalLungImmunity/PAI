package edu.uf.interactable;

import edu.uf.compartments.Voxel;
import edu.uf.intracellularState.BooleanNetwork;
import edu.uf.intracellularState.Phenotype;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;
import edu.uf.utils.Util;

public class Pneumocyte extends Cell {
    public static final String NAME = "Pneumocyte";

    private static int totalCells = 0;
    
    private int iteration;
    
    public static final int ACTIVE = Phenotype.createPhenotype();
    public static final int MIX_ACTIVE = Phenotype.createPhenotype();
    
    
    private static int interactionId = Id.getMoleculeId();

    public Pneumocyte() {
        super();
        this.iteration = 0;
        Pneumocyte.totalCells = Pneumocyte.totalCells + 1;
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
		Pneumocyte.totalCells = totalCells;
	}

	public int getIteration() {
		return iteration;
	}

    
    public void die() {
        if(this.getStatus() != Phagocyte.DEAD) {
            this.setStatus(Neutrophil.DEAD);  //##CAUTION!!!
            Pneumocyte.totalCells--;
        }
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Afumigatus) {
            Afumigatus interac = (Afumigatus) interactable;
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

    public void updateStatus() {
    	super.updateStatus();
    	if(!this.getClock().toc())return;
    	this.processBooleanNetwork();
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

	@Override
	protected BooleanNetwork createNewBooleanNetwork() {
		return new BooleanNetwork() {

			public static final int size = 2;
			
			public static final int MIX_ACTIVE = 0;
			public static final int ACTIVE = 1;
			public static final int ITER_REST = 12;
			private int iterations1 = 0;
			private int iterations2 = 0;
			
			public static final int N = 4;
			
			{
				this.inputs = new int[NUM_RECEPTORS];
				this.booleanNetwork = new int[size];
			}
			
			private int countMix() {
				if((this.booleanNetwork[MIX_ACTIVE]) == 0)
					return 0;
				if(iterations1++ >= ITER_REST) {
					iterations1 = 0;
					return 0;
				}
				return 1;
			}
			
			private int countActive() {
				if((this.booleanNetwork[ACTIVE]) == 0)
					return 0;
				if(iterations2++ >= ITER_REST) {
					iterations2 = 0;
					return 0;
				}
				return 1;
			}
			
			
			@Override
			public void processBooleanNetwork() {
				this.booleanNetwork[MIX_ACTIVE] =  and(or(input(Afumigatus.DEF_OBJ), N*countMix()), (-this.booleanNetwork[ACTIVE] + N));
				this.booleanNetwork[ACTIVE] = or(and((or(input(IL1.getMolecule()), input(TNFa.getMolecule()))), this.booleanNetwork[MIX_ACTIVE]), N*countActive());

				this.iterations1 = input(Afumigatus.DEF_OBJ) > 0 | input(IL1.getMolecule()) > 0 | input(TNFa.getMolecule()) > 0 ? 0 : this.iterations1;
				this.iterations2 = input(Afumigatus.DEF_OBJ) > 0 | input(IL1.getMolecule()) > 0 | input(TNFa.getMolecule()) > 0 ? 0 : this.iterations2;
				
				for(int i = 0; i < NUM_RECEPTORS; i++)
					this.inputs[i] = 0;
				
				this.clearPhenotype();
				
				if(this.booleanNetwork[ACTIVE] > 0) {
					this.getPhenotype().put(Pneumocyte.this.ACTIVE, this.booleanNetwork[ACTIVE]);
					//System.out.println("active");
				} 
				if(this.booleanNetwork[MIX_ACTIVE] > 0) {
					this.getPhenotype().put(Pneumocyte.this.MIX_ACTIVE, this.booleanNetwork[MIX_ACTIVE]);
					//System.out.println("mix active");
				}
				
			}
			
		};
	}
}