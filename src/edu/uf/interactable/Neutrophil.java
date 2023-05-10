package edu.uf.interactable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.uf.intracellularState.BooleanNetwork;
import edu.uf.intracellularState.Phenotype;
import edu.uf.time.Clock;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;
import edu.uf.utils.Rand;

public class Neutrophil extends Phagocyte{
    public static final String NAME = "Neutrophils";


    private static String chemokine;
    private static int totalCells = 0;
    private static double totalIron = 0;
    private int maxMoveStep;
    private boolean degranulated = false;
    
    public static final int ACTIVE = Phenotype.createPhenotype();
    public static final int MIX_ACTIVE = Phenotype.createPhenotype();
    
    
    public boolean depleted = false;
    
    private static int interactionId = Id.getMoleculeId();

    public Neutrophil(double ironPool) {
    	super(ironPool);
        Neutrophil.totalCells = Neutrophil.totalCells + 1;
        this.setState(Neutrophil.FREE);
        Neutrophil.totalIron = Neutrophil.totalIron + ironPool;
        this.maxMoveStep = -1;
        this.setEngaged(false);
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
        if(this.maxMoveStep == -1)
            this.maxMoveStep = Rand.getRand().randpois(Constants.MA_MOVE_RATE_REST);
        return this.maxMoveStep;
    }

    public void updateStatus() {
    	super.updateStatus();
    	if(!this.getClock().toc())return;
    	this.processBooleanNetwork();
    	
        if(this.getStatus() == Neutrophil.DEAD)
            return;
        
        if(this.getStatus() == Neutrophil.NECROTIC || this.getStatus() == Neutrophil.APOPTOTIC) {
            this.die();
            for(InfectiousAgent entry : this.getPhagosome())
                entry.setState(Afumigatus.RELEASING);
        }else if(Rand.getRand().randunif() < Constants.NEUTROPHIL_HALF_LIFE)
            this.setStatus(Neutrophil.APOPTOTIC);
        //this.setMoveStep(0);
        this.maxMoveStep = -1;
        this.setEngaged(false);
        
    }

    public void incIronPool(double qtty) {
        this.setIronPool(this.getIronPool() + qtty);
        Neutrophil.totalIron += qtty;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if(interactable instanceof Afumigatus) {
            Afumigatus interac = (Afumigatus) interactable;
        	if(this.isEngaged())
                return true;
            if(!this.isDead()) {
                if(interac.getStatus() == Afumigatus.HYPHAE || interac.getStatus() == Afumigatus.GERM_TUBE) {
                    double pr = Constants.PR_N_HYPHAE;
                    //System.out.println(Rand.getRand().randunif());
                    if (Rand.getRand().randunif() < pr) {
                        Phagocyte.intAspergillus(this, interac);
                        interac.setStatus(Afumigatus.DYING);
                        this.bind(interac, 4);
                    }else
                        this.setEngaged(true);
                }else if (interac.getStatus() == Afumigatus.SWELLING_CONIDIA) {
                    if (Rand.getRand().randunif() < Constants.PR_N_PHAG) {
                        Phagocyte.intAspergillus(this, interac);
                        this.bind(interac, 4);
                    }
                }
            }
            
            return true;
        }
        if(interactable instanceof Macrophage) {
            Macrophage interact = (Macrophage) interactable;
            //EukaryoteSignalingNetwork.SAMP_e = RECEPTOR_IDX;
        	if (this.getStatus() == Neutrophil.APOPTOTIC){// and len(interactable.phagosome.agents) == 0:
        		interact.incIronPool(this.getIronPool());
                this.incIronPool(this.getIronPool());
                this.die();
                //interact.bind(RECEPTOR_IDX);
        	}
            return true;
        }
        if (interactable instanceof Iron) {
        	Iron interac = (Iron) interactable;
            if(this.getStatus() == Neutrophil.NECROTIC){//# or this.status == Neutrophil.APOPTOTIC or this.status == Neutrophil.DEAD:
            	interac.inc(this.getIronPool(), 0, x, y, z);
                this.incIronPool(-this.getIronPool());
            }
            return false;
        }
        
        return interactable.interact(this, x, y, z);
    }

    public void die() {
        if(this.getStatus() != Neutrophil.DEAD) { 
            this.setStatus(Neutrophil.DEAD);
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
	public int getMaxConidia() {
		return Constants.N_MAX_CONIDIA;
	}

	@Override
	protected BooleanNetwork createNewBooleanNetwork() {
		return new BooleanNetwork() {

			public static final int size = 7;
			//public static final int NUM_RECEPTORS = 4;
			
			public static final int IL1R = 0;
			public static final int IL1B = 1;
			public static final int NFkB = 2;
			public static final int ERK = 3;
			public static final int TNFR = 4;
			public static final int Dectin = 5;
			public static final int CXCL2R = 6;
			
			{
				this.inputs = new int[NUM_RECEPTORS];
				this.booleanNetwork = new int[size];
			}
			
			
			
			@Override
			public void processBooleanNetwork() {
				int k = 0;
				List<Integer> array = new ArrayList<>(size);
				for(int i = 0; i < size; i++)
					array.add(i);
				while(true) {
					if(k++ > Constants.MAX_BN_ITERATIONS)break;
					Collections.shuffle(array, new Random());
					for(int i : array) {
						switch(i) {
							case 0:
								this.booleanNetwork[IL1R] = this.booleanNetwork[IL1B] | input(IL1.getMolecule());
								break;
							case 1:
								this.booleanNetwork[NFkB] = (this.booleanNetwork[IL1R] | this.booleanNetwork[TNFR] | this.booleanNetwork[CXCL2R]) ;
								break;
							case 2:
								this.booleanNetwork[ERK] = this.booleanNetwork[Dectin];
								break;
							case 3:
								this.booleanNetwork[IL1B] = this.booleanNetwork[NFkB];
								break;
							case 4:
								this.booleanNetwork[TNFR] = input(TNFa.getMolecule());
								break;
							case 5:
								this.booleanNetwork[Dectin] = 0;//e(B_GLUC);
								break;
							case 6:
								this.booleanNetwork[CXCL2R] = input(MIP2.getMolecule());
								break;
							default:
								System.err.println("No such interaction " + i + "!");
								break;
						}
					}
				}
				
				for(int i = 0; i < NUM_RECEPTORS; i++)
					this.inputs[i] = 0;
				
				this.clearPhenotype();
				
				if(this.booleanNetwork[NFkB] > 0) {
					this.getPhenotype().put(Neutrophil.this.ACTIVE, this.booleanNetwork[NFkB]);
				} 
				if(this.booleanNetwork[ERK] == 1) {
					this.getPhenotype().put(Neutrophil.this.MIX_ACTIVE, this.booleanNetwork[ERK]);
				}
					
				
			}
			
		};
	}
}
