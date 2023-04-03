package edu.uf.interactable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.uf.intracellularState.BooleanNetwork;
import edu.uf.intracellularState.EukaryoteSignalingNetwork;
import edu.uf.intracellularState.Phenotypes;
import edu.uf.time.Clock;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class Macrophage extends Phagocyte{

    public static final String NAME = "Macrophage";

    private static String chemokine = null;
    
    private static int totalCells = 0;
    private static double totalIron = 0; 
    
    public static final int FPN = 28;
    
    private int maxMoveStep;
    private boolean engaged;

	public Macrophage(double ironPool) {
    	super(ironPool);
        Macrophage.totalCells = Macrophage.totalCells + 1;
        this.setState(Macrophage.FREE);
        this.maxMoveStep = -1; 
        this.engaged = false; //### CHANGE HERE!!!
        Macrophage.totalIron = Macrophage.totalIron + ironPool;
    }
	
	public boolean isTime() {
		return this.getClock().toc();
	}
    
    public static String getChemokine() {
		return chemokine;
	}

	public static void setChemokine(String chemokine) {
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
        if(this.maxMoveStep == -1) {
        	//this.maxMoveStep = Rand.getRand().randunif() < Constants.MA_MOVE_RATE_ACT ? 1 : 0;
        	this.maxMoveStep = Rand.getRand().randpois(Constants.MA_MOVE_RATE_ACT);
        }
            //
        return this.maxMoveStep;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        return interactable.interact(this, x, y, z);
    }


    public void updateStatus() {
    	super.updateStatus();
    	if(!this.getClock().toc())return;
    	
    	this.processBooleanNetwork();
    	
        if(this.getStatus() == Macrophage.DEAD)
            return;
        if(this.getStatus() == Macrophage.NECROTIC) {
            this.die();
            for(InfectiousAgent entry : this.getPhagosome())
            	entry.setState(Afumigatus.RELEASING);
        }else if(this.getPhagosome().size() > Constants.MA_MAX_CONIDIA)
            this.setStatus(Phagocyte.NECROTIC);
        
        if(Rand.getRand().randunif() < Constants.MA_HALF_LIFE && this.getPhagosome().size() == 0 &&
        		Macrophage.totalCells > Constants.MIN_MA) 
            this.die();
        //this.setMoveStep(0);
        this.maxMoveStep = -1;
    }
        

    public void incIronPool(double qtty) {
        this.setIronPool(this.getIronPool() + qtty);
        Macrophage.totalIron = Macrophage.totalIron + qtty;
    }

    public void die() {
        if(this.getStatus() != Macrophage.DEAD) {
            this.setStatus(Macrophage.DEAD);
            Macrophage.totalCells = Macrophage.totalCells - 1;
        }
    }

    public String attractedBy() {
        return Macrophage.chemokine;
    }

	@Override
	public int getMaxConidia() {
		return Constants.MA_MAX_CONIDIA;
	}
	
	public String getName() {
    	return NAME;
    }

	@Override
	protected BooleanNetwork createNewBooleanNetwork() {
		EukaryoteSignalingNetwork network = new EukaryoteSignalingNetwork() {

			public static final int size = 29;
			//public static final int NUM_RECEPTORS = 12;
			
			public static final int IFNGR = 0;
			public static final int IFNB = 1;
			public static final int CSF2Ra = 2;
			public static final int IL1R = 3;
			public static final int IL1B = 4;
			public static final int TLR4 = 5;
			public static final int FCGR = 6;
			public static final int IL4Ra = 7;
			public static final int IL10R = 8;
			public static final int IL10_out = 9;
			public static final int STAT1 = 10;
			public static final int SOCS1 = 11;
			public static final int STAT3 = 12;
			public static final int STAT5 = 13;
			public static final int IRF4 = 14;
			public static final int NFkB = 15;
			public static final int PPARG = 16;
			public static final int KLF4 = 17;
			public static final int STAT6 = 18;
			public static final int JMJD3 = 19;
			public static final int IRF3 = 20;
			public static final int ERK = 21;
			public static final int IL12_out = 22;
			public static final int TNFR = 23;
			public static final int Dectin = 24;
			public static final int ALK5 = 25;
			public static final int SMAD2 = 26;
			public static final int PtSR = 27;
			
			//public static final int FNP = 28; //IN THE OUTER CLASS!
			
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
								this.booleanNetwork[IFNGR] = this.booleanNetwork[IFNB] | e(this.inputs, IFNG_e);
								break;
							case 1:
								this.booleanNetwork[CSF2Ra] = e(this.inputs, GM_CSF_e);
								break;
							case 2:
								this.booleanNetwork[IL1R] = this.booleanNetwork[IL1B] | e(this.inputs, IL1B_e);
								break;
							case 3:
								/*this.booleanNetwork[TLR4] = (e(this.inputs, LPS_e) | (e(this.inputs, Heme_e) & 
										(-e(this.inputs, Hpx_e) + 1)) | e(this.inputs, DAMP_e) | e(this.inputs, VIRUS_e)) & 
										(-this.booleanNetwork[FCGR] + 1);*/
								this.booleanNetwork[TLR4] = (o(this.inputs, TLR4_o) | //e(this.inputs, LPS_e) | 
										(e(this.inputs, Heme_e) & (-e(this.inputs, Hpx_e) + 1))) & (-this.booleanNetwork[FCGR] + 1);
								break;
							case 4:
								this.booleanNetwork[FCGR] = (e(this.inputs, IC_e) & e(this.inputs, LPS_e)) | (e(this.inputs, IC_e) & e(this.inputs, IL1B_e));
								break;
							case 5:
								this.booleanNetwork[IL4Ra] = e(this.inputs, IL4_e);
								break;
							case 6:
								this.booleanNetwork[IL10R] = e(this.inputs, IL10_e) | this.booleanNetwork[IL10_out];
								break;
							case 7:
								this.booleanNetwork[STAT1] = this.booleanNetwork[IFNGR] & (-(this.booleanNetwork[SOCS1] | this.booleanNetwork[STAT3]) + 1);
								break;
							case 8:
								this.booleanNetwork[STAT5] = this.booleanNetwork[CSF2Ra] & (-(this.booleanNetwork[STAT3] | this.booleanNetwork[IRF4]) + 1);
								break;
							case 9:
								this.booleanNetwork[NFkB] = (this.booleanNetwork[IL1R] | this.booleanNetwork[TLR4] | 
										this.booleanNetwork[Dectin] | this.booleanNetwork[TNFR]) & (
										-(this.booleanNetwork[STAT3] | this.booleanNetwork[FCGR] | this.booleanNetwork[PPARG] | this.booleanNetwork[KLF4])
								+ 1);
								break;
							case 10:
								this.booleanNetwork[PPARG] = this.booleanNetwork[IL4Ra];
								break;
							case 11:
								this.booleanNetwork[STAT6] = this.booleanNetwork[IL4Ra];
								break;
							case 12:
								this.booleanNetwork[JMJD3] = this.booleanNetwork[IL4Ra];
								break;
							case 13:
								this.booleanNetwork[STAT3] = (this.booleanNetwork[IL10R] | this.booleanNetwork[SMAD2]) & (
										-(this.booleanNetwork[FCGR] | this.booleanNetwork[PPARG]) + 1
								);
								break;
							case 14:
								this.booleanNetwork[IRF3] = this.booleanNetwork[TLR4];
								break;
							case 15:
								this.booleanNetwork[ERK] = this.booleanNetwork[FCGR];// | this.booleanNetwork[Dectin];
								break;
							case 16:
								this.booleanNetwork[KLF4] = this.booleanNetwork[STAT6];
								break;
							case 17:
								this.booleanNetwork[IL1B] = this.booleanNetwork[NFkB];
								break;
							case 18:
								this.booleanNetwork[IFNB] = this.booleanNetwork[IRF3];
								break;
							case 19:
								this.booleanNetwork[IL12_out] = this.booleanNetwork[STAT1] | this.booleanNetwork[STAT5] | this.booleanNetwork[NFkB];
								break;
							case 20: 
								this.booleanNetwork[IL10_out] = this.booleanNetwork[PPARG] | this.booleanNetwork[STAT6] | this.booleanNetwork[JMJD3] | 
									this.booleanNetwork[STAT3] | this.booleanNetwork[ERK] | this.booleanNetwork[PtSR];
								break;
							case 21:
								this.booleanNetwork[TNFR] = e(this.inputs, TNFa_e);
								break;
							case 22:
								this.booleanNetwork[Dectin] = e(this.inputs, B_GLUC_e);
								break;
							case 23:
								this.booleanNetwork[SOCS1] = this.booleanNetwork[STAT6];
								break;
							case 24:
								this.booleanNetwork[IRF4] = this.booleanNetwork[JMJD3];
								break;
							case 25:
								this.booleanNetwork[ALK5] = e(this.inputs, TGFb_e);
								break;
							case 26:
								this.booleanNetwork[SMAD2] = this.booleanNetwork[ALK5];
								break;
							case 27:
								//this.booleanNetwork[PtSR] = e(this.inputs, SAMP_e);
								break;
							case 28:
								this.booleanNetwork[FPN] = (-e(this.inputs, Hep_e) + 1);
								break;
							default:
								System.err.println("No such interaction " + i + "!");
								break;
						}
					}
				}
				
				for(int i = 0; i < NUM_RECEPTORS; i++)
					this.inputs[i] = 0;
				
				Macrophage.this.clearPhenotype();
				
				if(this.booleanNetwork[NFkB] == 1 || this.booleanNetwork[STAT1] == 1 || this.booleanNetwork[STAT5] == 1)
					Macrophage.this.addPhenotype(Phenotypes.ACTIVE);
				else if(this.booleanNetwork[STAT6] == 1)
					Macrophage.this.addPhenotype(Phenotypes.ALT_ACTIVE);
				else if(this.booleanNetwork[ERK] == 1)
					Macrophage.this.addPhenotype(Phenotypes.MIX_ACTIVE);
				else if(this.booleanNetwork[STAT3] == 1)
					Macrophage.this.addPhenotype(Phenotypes.INACTIVE);
				else
					Macrophage.this.addPhenotype(Phenotypes.RESTING);
					
				
			}
			
		};
		
		return network;
	}
	
}
