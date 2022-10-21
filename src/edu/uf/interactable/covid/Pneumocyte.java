package edu.uf.interactable.covid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.uf.interactable.Afumigatus;
import edu.uf.interactable.IL6;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Molecule;
import edu.uf.interactable.TGFb;
import edu.uf.interactable.TNFa;
import edu.uf.intracellularState.BooleanNetwork;
import edu.uf.intracellularState.EukaryoteSignalingNetwork;
import edu.uf.intracellularState.Phenotypes;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class Pneumocyte extends edu.uf.interactable.Pneumocyte{
	
	private double viralLoad;
	
	public Pneumocyte() {
		super();
		this.createBooleanNetwork();
	}
	
	public void incViralLoad(double virus) {
		this.viralLoad += virus;
		((SarsCoV2) SarsCoV2.getMolecule()).incInternalLoad(virus);
	}
	
	public double getViralLoad() {
		return this.viralLoad;
	}
	
	public void clearViralLoad() {
		((SarsCoV2) SarsCoV2.getMolecule()).incInternalLoad(-viralLoad);
		this.viralLoad = 0;
	}
	
	@Override
	protected BooleanNetwork createNewBooleanNetwork() {
		return new EukaryoteSignalingNetwork() {

			static final int size = 9;
			static final int NUM_PHENOTYPES = 3;
			
			static final int VIRUS = 0;
			static final int VIRAL_REP = 1;
			static final int IRF3 = 2;
			static final int STAT1 = 3;
			static final int IRF9 = 4;
			static final int TLR4 = 5;
			static final int RIG1 = 6;
			static final int IFNR = 7;
			static final int IFN = 8;
			
			{
				this.inputs = new int[NUM_RECEPTORS];
				this.booleanNetwork = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
			}
			
			@Override
			public void processBooleanNetwork() {
				if(Pneumocyte.this.getClock().toc(BN_CLOCK, Constants.HALF_HOUR/Constants.TIME_STEP_SIZE)) { //convet minutes in iterations
					int k = 0;
					List<Integer> array = new ArrayList<>(size);
					for(int i = 0; i < size; i++)
						array.add(i);
					//System.out.println();
					//Pneumocyte.count = 0;
					while(true) {
						if(k++ > Constants.MAX_BN_ITERATIONS)break;
						Collections.shuffle(array, new Random());
						//if(this.booleanNetwork[VIRUS] == 1)Util.printIntArray(booleanNetwork);
						//int virus_in = Util.activationFunction(Pneumocyte.this.viralLoad, Constants.Kd_SarsCoV2, Constants.MA_VOL, 1.0) > 
						//	Rand.getRand().randunif() ? 1 : 0;
						for(int i : array) {
							switch (i) {
							case 0:
								this.booleanNetwork[VIRUS] = or(e(this.inputs, VIRUS_e), this.booleanNetwork[VIRUS]);
								break;
							case 1:
								this.booleanNetwork[VIRAL_REP] = and(this.booleanNetwork[VIRUS], not(this.booleanNetwork[IRF9], 1));
								break;
							case 2:
								this.booleanNetwork[IRF3] = this.booleanNetwork[TLR4];//or(this.booleanNetwork[RIG1], this.booleanNetwork[TLR4]);
								break;
							case 3:
								this.booleanNetwork[STAT1] = this.booleanNetwork[IFNR];
								break;
							case 4:
								this.booleanNetwork[IRF9] = this.booleanNetwork[STAT1];
								break;
							case 5:
								this.booleanNetwork[TLR4] = o(this.inputs, TLR4_o);
								break;
							case 6:
								this.booleanNetwork[RIG1] = this.booleanNetwork[VIRAL_REP];
								break;
							case 7:
								this.booleanNetwork[IFNR] = this.booleanNetwork[IFN];
								break;	
							case 8:
								this.booleanNetwork[IFN] = or(this.booleanNetwork[IRF3], e(this.inputs, IFNG_e));
								//r(this.inputs, IFNG_e);
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
					
					Pneumocyte.this.clearPhenotype();
					
					if(this.booleanNetwork[IRF9] == 1)
						Pneumocyte.this.addPhenotype(Phenotypes.IRF9);
					if(this.booleanNetwork[IRF3] == 1)
						Pneumocyte.this.addPhenotype(Phenotypes.IRF3);
				}
			}
		};
	}
	
	protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof TGFb) {
            Molecule interact = (Molecule) interactable;
        	if(this.inPhenotype(interact.getSecretionPhenotype())) 
            	interact.inc(Constants.MA_TGF_QTTY, 0, x, y, z);
            return true;
        }
		
        return super.templateInteract(interactable, x, y, z);
    }
	
	public void updateStatus() {
		if(this.getStatus() == DEAD)return;
		this.processBooleanNetwork();
		if(!this.inPhenotype(Phenotypes.IRF9) && this.viralLoad > 0) {
			double qtty = this.viralLoad*Constants.SarsCoV2_REP_RATE;
			this.viralLoad += qtty;
			((SarsCoV2) SarsCoV2.getMolecule()).incInternalLoad(qtty);
		}
		
		//if(this.viralLoad > Constants.MAX_VIRAL_LOAD)
		if(viralLoad > Constants.MAX_VIRAL_LOAD)
			Pneumocyte.this.addPhenotype(Phenotypes.NECROTIC);
		
		//if(this.getPhenotype() != 0)System.out.println(this.getPhenotype());
		if(this.inPhenotype(new int[] {Phenotypes.APOPTOTIC, Phenotypes.NECROTIC})) 
			this.die();
	}
}

