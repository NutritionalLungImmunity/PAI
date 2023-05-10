package edu.uf.interactable.covid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import edu.uf.compartments.Voxel;
import edu.uf.interactable.Cell;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Molecule;
import edu.uf.interactable.Pneumocyte;
import edu.uf.interactable.TNFa;
import edu.uf.intracellularState.BooleanNetwork;
import edu.uf.intracellularState.Phenotype;
import edu.uf.time.Clock;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;
import edu.uf.utils.Util;

public class EndothelialCells extends Cell{
	
	public static final String NAME = "EC";
	
	private static int totalCells = 0;
	private static int interactionId = Id.getMoleculeId();
	
	private Set<EndothelialCells> neighbors = new HashSet<>();
	
	public static final int STAT3  = Phenotype.createPhenotype();
	public static final int NFKB = Phenotype.createPhenotype();
	public static final int OPEN = Phenotype.createPhenotype();
	
	public EndothelialCells() {
		totalCells++;
	}
	
	public boolean isTime() {
		return this.getClock().toc();
	}
	
	public static int getTotalCells() {
		return totalCells;
	}

	@Override
	protected BooleanNetwork createNewBooleanNetwork() {
		return new BooleanNetwork() {

			public static final int size = 9;
			public static final int NUM_PHENOTYPES = 4;
			
			public static final int sIL6R = 0;
			public static final int STAT3 = 1;
			public static final int VEGFR2 = 2;
			public static final int AKT = 3;
			public static final int VEGFA = 4;
			public static final int TNF = 5;
			public static final int TNFR = 6;
			public static final int NFkB = 7;
			public static final int ICAM = 8;
			
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
								this.booleanNetwork[sIL6R] = input(IL6Complex.getMolecule());
								break;
							case 1:
								this.booleanNetwork[STAT3] = or(this.booleanNetwork[sIL6R], this.booleanNetwork[AKT]);
								break;
							case 2:
								this.booleanNetwork[VEGFR2] = this.booleanNetwork[VEGFA];
								break;
							case 3:
								this.booleanNetwork[AKT] = this.booleanNetwork[VEGFR2];
								break;
							case 4:
								this.booleanNetwork[VEGFA] = input(VEGF.getMolecule());
								break;
							case 5:
								this.booleanNetwork[TNF] = input(TNFa.getMolecule());
								break;
							case 6:
								this.booleanNetwork[TNFR] = this.booleanNetwork[TNF];
								break;
							case 7:
								this.booleanNetwork[NFkB] = this.booleanNetwork[TNFR];
								break;
							case 8:
								this.booleanNetwork[ICAM] = this.booleanNetwork[NFkB];
								break;
							default:
								System.err.println("No such interaction " + i + "!");
								break;
						}
					}
				}
				
				array = new ArrayList<>();
				
				for(int i = 0; i < NUM_RECEPTORS; i++)
					this.inputs[i] = 0;
				
				for(int i = 0; i < NUM_PHENOTYPES; i++)
					array.add(i);
				
				this.clearPhenotype();
				
				if(this.booleanNetwork[STAT3] == 1)
					this.getPhenotype().put(EndothelialCells.this.STAT3, this.booleanNetwork[STAT3]);
				if(this.booleanNetwork[NFkB] == 1)
					this.getPhenotype().put(EndothelialCells.this.NFKB, this.booleanNetwork[NFkB]);
				if(this.booleanNetwork[AKT] == 1)
					this.getPhenotype().put(EndothelialCells.this.OPEN, this.booleanNetwork[AKT]);
					
			}
		};
	}

	@Override
	public void updateStatus() {
		super.updateStatus();
    	if(!this.getClock().toc())return;
		this.processBooleanNetwork();
		
	}

	@Override
	public void move(Voxel oldVoxel, int steps) {}

	@Override
	public void die() {
		if(this.getStatus() != EndothelialCells.DEAD) {
			this.setStatus(EndothelialCells.DEAD);
			EndothelialCells.totalCells--;
		}
	}

	@Override
	public void incIronPool(double ironPool) {}

	@Override
	public int getMaxMoveSteps() {
		return 0;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
		if(interactable instanceof TNFa) {
			Molecule mol = (Molecule) interactable;
			this.bind(mol, Util.activationFunction5(mol.get(0, x, y, z), Constants.Kd_TNF));
	        return true;
		}
		if(interactable instanceof EndothelialCells) {
			if(!neighbors.contains(interactable)) {
				neighbors.add(((EndothelialCells)interactable));
			}
			
			return true;
		}
		return interactable.interact(this, x, y, z);
	}

	@Override
	public int getInteractionId() {
		return interactionId;
	}

}
