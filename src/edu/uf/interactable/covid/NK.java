package edu.uf.interactable.covid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.uf.compartments.Voxel;
import edu.uf.interactable.Cell;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Molecule;
import edu.uf.interactable.Phagocyte;
import edu.uf.interactable.TNFa;
import edu.uf.intracellularState.BooleanNetwork;
import edu.uf.intracellularState.EukaryoteSignalingNetwork;
import edu.uf.intracellularState.Phenotypes;
import edu.uf.time.Clock;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class NK extends Phagocyte{
	
	public static final String NAME = "NK";

    private static String chemokine = MIP2.NAME;
    
    private static int totalCells = 0;
    
    private int maxMoveStep;
    private boolean engaged = false;
    
    public NK() {
    	NK.totalCells = NK.totalCells + 1;
    }
    
    public boolean isTime() {
		return this.getClock().toc();
	}
    
    public static int getTotalCells() {
		return totalCells;
	}

	@Override
	protected BooleanNetwork createNewBooleanNetwork() {
		return new EukaryoteSignalingNetwork() {
			
			static final int size = 10;
			static final int NUM_PHENOTYPES = 3;
			
			private static final int NKG2D = 0;
			private static final int ERK = 1;
			private static final int NFAT = 2;
			private static final int IFNg = 3;
			private static final int IFNR = 4;
			private static final int IFNGR = 5;
			private static final int TRAIL = 6;
			private static final int GRAN = 7;
			private static final int TNFR = 8;
			private static final int IL6R = 9;
			private static final int TGFR = 10;
			
			{
				this.inputs = new int[NUM_RECEPTORS];
				this.booleanNetwork = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
			}
			

			@Override
			public void processBooleanNetwork() {
				int k = 0;
				List<Integer> array = new ArrayList<>(size);
				for(int i = 0; i < size; i++)
					array.add(i);
				//System.out.println();
				while(true) {
					if(k++ > Constants.MAX_BN_ITERATIONS)break;
					Collections.shuffle(array, new Random());
					for(int i : array) {
						switch (i) {
						case 0:
							this.booleanNetwork[NKG2D] = and(e(this.inputs, INF_CELL_e), not(this.booleanNetwork[TGFR], 1));
							break;
						case 1:
							this.booleanNetwork[ERK] = this.booleanNetwork[NKG2D];
							break;
						case 2:
							this.booleanNetwork[NFAT] = this.booleanNetwork[NKG2D];
							break;
						case 3:
							this.booleanNetwork[IFNg] = and(this.booleanNetwork[NFAT], not(this.booleanNetwork[TGFR], 1));
							break;
						case 4:
							this.booleanNetwork[IFNGR] = this.booleanNetwork[IFNg];
							break;
						case 5:
							this.booleanNetwork[TRAIL] = this.booleanNetwork[IFNGR];
							break;
						case 6:
							this.booleanNetwork[GRAN] = and(this.booleanNetwork[ERK], not(this.booleanNetwork[TGFR], 1));
							break;
						case 7:
							this.booleanNetwork[IFNR] = e(this.inputs, IFN_e);
							break;
						case 8:
							this.booleanNetwork[TNFR] = e(this.inputs, TNFa_e);
							break;
						case 9:
							this.booleanNetwork[IL6R] = e(this.inputs, IL6_e);
							break;
						case 10:
							this.booleanNetwork[TGFR] = e(this.inputs, TGFb_e);
							break;
						default:
							System.err.println("No such rule " + i);
						}
					}
				}
				
				array = new ArrayList<>();
				
				for(int i = 0; i < NUM_RECEPTORS; i++) 
					this.inputs[i] = 0;
				
				for(int i = 0; i < NUM_PHENOTYPES; i++)
					array.add(i);
				
				NK.this.clearPhenotype();
				
				if(this.booleanNetwork[GRAN] == 1 || this.booleanNetwork[TRAIL] == 1)
					NK.this.addPhenotype(Phenotypes.ACTIVE);
				if(this.booleanNetwork[IFNg] == 1)
					NK.this.addPhenotype(Phenotypes.SECRETING);
				
			}
			
		};
	}

	@Override
	public void updateStatus() {
		super.updateStatus();
    	if(!this.getClock().toc())return;
		this.processBooleanNetwork();
		engaged = false;
		maxMoveStep = -1;
		if(Rand.getRand().randunif() < Constants.NK_HALF_LIFE)
			this.setStatus(Cell.APOPTOTIC);
	}

	@Override
	public void move(Voxel oldVoxel, int steps) {
		if(engaged)return;
        if(steps < this.getMaxMoveSteps()) {
        	
        	Util.calcDriftProbability(oldVoxel, this);
            Voxel newVoxel = Util.getVoxel(oldVoxel, Rand.getRand().randunif());
        	
            oldVoxel.removeCell(this.getId());
            //System.out.println(oldVoxel + " " + newVoxel);
            newVoxel.setCell(this);
            steps = steps + 1;
            move(newVoxel, steps);
        }
    }

	@Override
	public void die() {
        if(this.getStatus() != Macrophage.DEAD) {
            this.setStatus(Macrophage.DEAD);
            NK.totalCells = NK.totalCells - 1;
        }
    }

	@Override
	public void incIronPool(double ironPool) {}

	@Override
	public int getMaxMoveSteps() { 
        if(this.maxMoveStep == -1) {
        	//this.maxMoveStep = Rand.getRand().randunif() < Constants.MA_MOVE_RATE_ACT ? 1 : 0;
        	this.maxMoveStep = Rand.getRand().randpois(Constants.MA_MOVE_RATE_ACT);
        }
            //
        return this.maxMoveStep;
    }

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
		if(interactable instanceof Pneumocyte) {
			Pneumocyte cell = (Pneumocyte) interactable;
			if(cell.getViralLoad() > 0) {
				EukaryoteSignalingNetwork.INF_CELL_e = Pneumocyte.RECEPTOR_IDX;
				this.bind(Pneumocyte.RECEPTOR_IDX);
				engaged = true;
			
				EukaryoteSignalingNetwork.IFNG_e = IFN1.MOL_IDX;
				if(this.inPhenotype(new int[] {Phenotypes.SECRETING, Phenotypes.FULL_ACTIVE})) {
					cell.bind(IFN1.MOL_IDX); //simulate NK IFNg secretion
				}
				if(this.inPhenotype(new int[] {Phenotypes.ACTIVE, Phenotypes.FULL_ACTIVE}))
					if(Rand.getRand().randunif() < Constants.PR_NK_KILL) {
						cell.addPhenotype(Phenotypes.APOPTOTIC);
						cell.clearViralLoad();
					}
			}
			return true;
		}
		if(interactable instanceof IFN1) {
			 Molecule interact = (Molecule) interactable;
	         EukaryoteSignalingNetwork.IFN_e = IFN1.MOL_IDX;
	         if (Util.activationFunction(interact.get(0, x, y, z), Constants.Kd_IFNG)) 
	        	this.bind(IFN1.MOL_IDX);
	         return true;
		}
		if(interactable instanceof TNFa) {
			 Molecule interact = (Molecule) interactable;
	         EukaryoteSignalingNetwork.TNFa_e = TNFa.MOL_IDX;
	        if (Util.activationFunction(interact.get(0, x, y, z), Constants.Kd_TNF)) 
	        	this.bind(TNFa.MOL_IDX);
	        return true;
		}
		if(interactable instanceof IL6Complex) {
			 Molecule interact = (Molecule) interactable;
	         EukaryoteSignalingNetwork.IL6_e = IL6Complex.MOL_IDX;
	        if (Util.activationFunction(interact.get(0, x, y, z), Constants.Kd_IL6)) 
	        	this.bind(IL6Complex.MOL_IDX);
	        return true;
		}
		return false;
	}
	
	public String attractedBy() {
        return NK.chemokine;
    }

	@Override
	public int getMaxConidia() {
		// TODO Auto-generated method stub
		return 0;
	}

}
