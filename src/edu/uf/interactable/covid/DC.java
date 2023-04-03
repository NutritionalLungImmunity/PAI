package edu.uf.interactable.covid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.uf.interactable.Interactable;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.Molecule;
import edu.uf.interactable.Phagocyte;
import edu.uf.intracellularState.BooleanNetwork;
import edu.uf.intracellularState.EukaryoteSignalingNetwork;
import edu.uf.intracellularState.Phenotypes;
import edu.uf.time.Clock;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class DC extends Phagocyte{
    public static final String NAME = "DC";

    private static String chemokine = MIP2.NAME;
    
    private static int totalCells = 0;
    
    private int maxMoveStep;

	public DC() {
    	DC.totalCells = DC.totalCells + 1;
        this.maxMoveStep = -1; 
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

    public int getMaxMoveSteps() { 
        if(this.maxMoveStep == -1) {
        	//this.maxMoveStep = Rand.getRand().randunif() < Constants.MA_MOVE_RATE_ACT ? 1 : 0;
        	this.maxMoveStep = Rand.getRand().randpois(Constants.MA_MOVE_RATE_ACT);
        }
            //
        return this.maxMoveStep;
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	EukaryoteSignalingNetwork.IFNG_e = IFN1.MOL_IDX;
    	if(interactable instanceof IFN1) {
    		Molecule mol = (Molecule) interactable;
	        if (this.inPhenotype(mol.getSecretionPhenotype()))//# and interactable.state == Neutrophil.INTERACTING:
	        	mol.inc(Constants.DC_IFN_QTTY, 0, x, y, z);
    		return true;
    	}
    	EukaryoteSignalingNetwork.TLR4_o.add(SarsCoV2.MOL_IDX);
    	if(interactable instanceof SarsCoV2) {
    		Molecule mol = (Molecule) interactable;
    		//mol.pdec(1-Constants.SarsCoV2_HALF_LIFE, 0, x, y, z);
	        if (Util.activationFunction(mol.get(0, x, y, z)*10000, Constants.Kd_SarsCoV2)) {//viral particle per infectious unity
	        	this.bind(SarsCoV2.MOL_IDX);
	        	/*double qtty = Constants.SarsCoV2_UPTAKE_QTTY > this.get(0, x, y, z) ? this.get(0, x, y, z) : Constants.SarsCoV2_UPTAKE_QTTY;
	        	double q = this.get(0, x, y, z);
	        	this.dec(qtty, 0, x, y, z);*/
	        }
    		return true;
    	}
        return interactable.interact(this, x, y, z);
    }


    public void updateStatus() {
    	super.updateStatus();
    	if(!this.getClock().toc())return;
    	this.processBooleanNetwork();
        this.maxMoveStep = -1;
    }

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
	public int getMaxConidia() {
		return Constants.MA_MAX_CONIDIA;
	}
	
	public String getName() {
    	return NAME;
    }

	@Override
	protected BooleanNetwork createNewBooleanNetwork() {
		EukaryoteSignalingNetwork network = new EukaryoteSignalingNetwork() {

			public static final int size = 5;
			//public static final int NUM_RECEPTORS = 12;
			
			public static final int IFNGR = 0;
			public static final int IFNB = 1;
			public static final int TLR4 = 2;
			public static final int STAT1 = 3;
			public static final int IRF3 = 4;
			
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
								this.booleanNetwork[TLR4] = o(this.inputs, TLR4_o);
								break;
							case 2:
								this.booleanNetwork[STAT1] = this.booleanNetwork[IFNGR];
								break;
							case 3:
								this.booleanNetwork[IRF3] = this.booleanNetwork[TLR4];
								break;
							case 4:
								this.booleanNetwork[IFNB] = this.booleanNetwork[IRF3] | this.booleanNetwork[STAT1];
								break;
							default:
								System.err.println("No such interaction " + i + "!");
								break;
						}
					}
				}
				
				for(int i = 0; i < NUM_RECEPTORS; i++)
					this.inputs[i] = 0;
				
				DC.this.clearPhenotype();
				
				if(this.booleanNetwork[STAT1] == 1 || this.booleanNetwork[IRF3] == 1)
					DC.this.addPhenotype(Phenotypes.ACTIVE);
				else
					DC.this.addPhenotype(Phenotypes.RESTING);
					
				
			}
			
		};
		
		return network;
	}

	@Override
	public void incIronPool(double ironPool) {
		// TODO Auto-generated method stub
		
	}
}
