package edu.uf.interactable.capa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.uf.interactable.Macrophage;
import edu.uf.intracellularState.BooleanNetwork;
import edu.uf.intracellularState.EukaryoteSignalingNetwork;
import edu.uf.intracellularState.Phenotypes;
import edu.uf.utils.Constants;

public class CapaMacrophage extends Macrophage{
	
	int iteration = 0;

	public CapaMacrophage(double ironPool) {
		super(ironPool);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected BooleanNetwork createNewBooleanNetwork() {
		EukaryoteSignalingNetwork network = new EukaryoteSignalingNetwork() {

			public static final int size = 5;
			//public static final int NUM_RECEPTORS = 12;
			
			public static final int ACTIVATING = 0;
			public static final int INACTIVATING = 1;
			public static final int MIX_ACTIVE = 2;
			public static final int INACTIVE = 3;
			public static final int TNF = 4;
			
			//public static final int FNP = 28; //IN THE OUTER CLASS!
			
			{
				this.inputs = new int[NUM_RECEPTORS];
				this.booleanNetwork = new int[size];
			}
			
			
			
			@Override
			public void processBooleanNetwork() {
				if(CapaMacrophage.this.getClock().toc(BN_CLOCK, Constants.HALF_HOUR/Constants.TIME_STEP_SIZE)) { //convet minutes in iterations
					int k = 0;
					List<Integer> array = new ArrayList<>(size);
					for(int i = 0; i < size; i++)
						array.add(i);
					if(this.booleanNetwork[ACTIVATING] == 0 && this.booleanNetwork[INACTIVATING] == 0 && this.booleanNetwork[INACTIVE] == 0 && this.booleanNetwork[MIX_ACTIVE] == 0) {
						this.booleanNetwork[ACTIVATING] = (e(this.inputs, TNFa_e) | e(this.inputs, B_GLUC_e)) & (this.booleanNetwork[INACTIVATING] + 1);
					}
					if(this.booleanNetwork[INACTIVATING] == 0 && this.booleanNetwork[INACTIVE] == 0) {
						this.booleanNetwork[INACTIVATING] = e(this.inputs, IL10_e) | e(this.inputs, TGFb_e) | e(this.inputs, SAMP_e);
					}
					this.booleanNetwork[TNF] = this.booleanNetwork[MIX_ACTIVE] & e(this.inputs, TNFa_e);
					
					for(int i = 0; i < NUM_RECEPTORS; i++)
						this.inputs[i] = 0;
					
					
					if(this.booleanNetwork[INACTIVATING] == 1) {
						if(++CapaMacrophage.this.iteration == 4) {
							this.booleanNetwork[INACTIVATING] = 0;
							this.booleanNetwork[INACTIVE] = 0;
							CapaMacrophage.this.clearPhenotype();
							CapaMacrophage.this.addPhenotype(Phenotypes.INACTIVE);
							CapaMacrophage.this.iteration = 1;
						}
					}else if(this.booleanNetwork[ACTIVATING] == 1) {
						if(++CapaMacrophage.this.iteration == 4) {
							this.booleanNetwork[ACTIVATING] = 0;
							this.booleanNetwork[MIX_ACTIVE] = 0;
							CapaMacrophage.this.clearPhenotype();
							CapaMacrophage.this.addPhenotype(Phenotypes.MIX_ACTIVE);
							CapaMacrophage.this.iteration = 1;
						}
						if(this.booleanNetwork[TNF] == 1) {
							CapaMacrophage.this.addPhenotype(Phenotypes.ACTIVE);
							CapaMacrophage.this.iteration = 0;
						}
					}else if(this.booleanNetwork[INACTIVE] == 1 || this.booleanNetwork[MIX_ACTIVE] == 1) {
						if(++CapaMacrophage.this.iteration == 12) {
							CapaMacrophage.this.clearPhenotype();
							this.booleanNetwork[MIX_ACTIVE] = 0;
							this.booleanNetwork[INACTIVE] = 0;
							CapaMacrophage.this.iteration = 0;
						}
					}
					
				}
				
			}
			
		};
		
		return network;
	}

}
