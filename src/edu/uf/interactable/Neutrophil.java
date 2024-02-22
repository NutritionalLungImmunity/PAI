package edu.uf.interactable;

import edu.uf.compartments.GridFactory;
import edu.uf.compartments.Voxel;
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.intracellularState.BooleanNetwork;
import edu.uf.intracellularState.NeutrophilStateModel;
import edu.uf.intracellularState.Phenotype;
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
    
    public static final int ACTIVE = Phenotype.createPhenotype();
    public static final int NETOTIC = Phenotype.createPhenotype();
    public static final int MIX_ACTIVE = Phenotype.createPhenotype();
    
    
    public boolean depleted = false;
    private boolean control;
    
    private static int interactionId = Id.getMoleculeId();

    public Neutrophil(double ironPool, BooleanNetwork network) {
    	super(ironPool, network);
        Neutrophil.totalCells = Neutrophil.totalCells + 1;
        this.setState(Neutrophil.FREE);
        Neutrophil.totalIron = Neutrophil.totalIron + ironPool;
        this.maxMoveStep = -1;
        this.setEngaged(false);
        this.control = true;
        this.netHalfLife = Constants.NET_HALF_LIFE;
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
    
    /*public boolean isDead() {
		return false;
	}*/

    public void updateStatus(int x, int y, int z) {
    	super.updateStatus(x, y, z);
    	if(!this.getClock().toc())return;
    	this.processBooleanNetwork();
    	
        if(this.getStatus() == Neutrophil.DEAD)
            return;
        
        
        if(this.hasPhenotype(NeutrophilStateModel.APOPTOTIC)) {
            this.die();
            for(InfectiousAgent entry : this.getPhagosome())
                entry.setState(Afumigatus.RELEASING);
        }
        if(this.hasPhenotype(NeutrophilStateModel.NETOTIC)) {
        	GridFactory.getGrid()[x][y][z].setExternalState(1);
        	/*if(this.clock.getCount() >= 48) {
        		this.netHalfLife = Constants.NET_HALF_LIFE*Constants.NET_COUNTER_INHIBITION;
        		System.out.println(this.netHalfLife + " " + Constants.NET_HALF_LIFE);
        	}*/
        	if(Rand.getRand().randunif() < this.netHalfLife) {
        		this.die();
        		GridFactory.getGrid()[x][y][z].setExternalState(0);
        		for(InfectiousAgent entry : this.getPhagosome())
        			entry.setState(Afumigatus.RELEASING);
        	}
        }
        
        
        //else if(Rand.getRand().randunif() < Constants.NEUTROPHIL_HALF_LIFE)
           // this.setStatus(Neutrophil.APOPTOTIC);
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
        	//System.out.println(this.hasPhenotype(NeutrophilStateModel.APOPTOTIC) + " " + this.hasPhenotype(NeutrophilStateModel.NETOTIC));
            if(!this.isDead() && !this.hasPhenotype(NeutrophilStateModel.NETOTIC)) {
                if(interac.getStatus() == Afumigatus.HYPHAE || interac.getStatus() == Afumigatus.GERM_TUBE) {
                    double pr = Constants.PR_N_HYPHAE;
                    //if(this.getExternalState() == 1)  pr *= Constants.NET_COUNTER_INHIBITION;
                    //System.out.println(Rand.getRand().randunif());
                    if (Rand.getRand().randunif() < pr) {
                        Afumigatus.intAspergillus(this, interac);
                        interac.setStatus(Afumigatus.DYING);
                        this.bind(interac, 4);
                        
                    }else {
                        this.setEngaged(true);
                        //interac.setEngaged(true);
                        
                    }
                    if (Rand.getRand().randunif() < Constants.PR_N_PHAG) {
                    	
                    	//Afumigatus.intAspergillus(this, interac);
                        //this.bind(interac, 4);
                        
                    }
                }else if (interac.getStatus() == Afumigatus.SWELLING_CONIDIA) {
                	if (Rand.getRand().randunif() < Constants.PR_N_PHAG) {
                		Afumigatus.intAspergillus(this, interac);
                		this.bind(interac, 4);
                	
                		//interac.setEngaged(true);
                	}else {
                		//interac.setEngaged(true);
                	}
                }
            }//if(this.hasPhenotype(NeutrophilStateModel.NETOTIC)) {
            //	interac.setEpithelialInhibition(interac.getEpithelialInhibition());
            	//interac.setNetGermBust(Constants.NET_COUNTER_INHIBITION);
            //}
            
            return true;
        }
        if(interactable instanceof Macrophage) {
            Macrophage interact = (Macrophage) interactable;
            //EukaryoteSignalingNetwork.SAMP_e = RECEPTOR_IDX;
        	if (this.hasPhenotype(NeutrophilStateModel.APOPTOTIC)){//(this.getStatus() == Neutrophil.APOPTOTIC){// and len(interactable.phagosome.agents) == 0:
        		interact.incIronPool(this.getIronPool());
                this.incIronPool(this.getIronPool());
                this.die();
                //interact.bind(RECEPTOR_IDX);
        	}
            return true;
        }
        if(interactable instanceof PneumocyteI) {
        	if(this.hasPhenotype(NeutrophilStateModel.NETOTIC)) {
        		PneumocyteI k = (PneumocyteI) interactable;
        		//k.setInjury(true);
        		//if(k.isInjury() || (control && Rand.getRand().randunif() < Constants.PR_NET_KILL_EPI)) {
        		if((control && Rand.getRand().randunif() < Constants.PR_NET_KILL_EPI)) {
        		//if(false) {
        			k.die();
        		}//else {
        			control = false;
        		//}
        	}
			return true;
		}
        if (interactable instanceof Iron) {
        	Iron interac = (Iron) interactable;
            if(this.hasPhenotype(NeutrophilStateModel.NETOTIC)){//# or this.status == Neutrophil.APOPTOTIC or this.status == Neutrophil.DEAD:
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
	public int getMaxCell() {
		return Constants.N_MAX_CONIDIA;
	}

}
