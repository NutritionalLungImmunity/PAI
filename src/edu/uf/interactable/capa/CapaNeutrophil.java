package edu.uf.interactable.capa;

import java.util.ArrayList;
import java.util.List;

import edu.uf.interactable.Neutrophil;
import edu.uf.intracellularState.BooleanNetwork;
import edu.uf.intracellularState.EukaryoteSignalingNetwork;
import edu.uf.intracellularState.Phenotypes;
import edu.uf.utils.Constants;

public class CapaNeutrophil extends Neutrophil{

	int iteration = 0;
	
	public CapaNeutrophil(double ironPool) {
		super(ironPool);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	protected BooleanNetwork createNewBooleanNetwork() {
		EukaryoteSignalingNetwork network = new EukaryoteSignalingNetwork() {

			public static final int size = 2;
			//public static final int NUM_RECEPTORS = 12;
			
			public static final int ACTIVATING = 0;
			public static final int ACTIVE = 1;
			
			//public static final int FNP = 28; //IN THE OUTER CLASS!
			
			{
				this.inputs = new int[NUM_RECEPTORS];
				this.booleanNetwork = new int[size];
			}
			
			
			
			@Override
			public void processBooleanNetwork() {
				if(CapaNeutrophil.this.getClock().toc(BN_CLOCK, Constants.HALF_HOUR/Constants.TIME_STEP_SIZE)) { //convet minutes in iterations
					int k = 0;
					List<Integer> array = new ArrayList<>(size);
					for(int i = 0; i < size; i++)
						array.add(i);
					if(this.booleanNetwork[ACTIVATING] == 0 && this.booleanNetwork[ACTIVE] == 0) {
						this.booleanNetwork[ACTIVATING] = e(this.inputs, TNFa_e) | e(this.inputs, B_GLUC_e);
					}
					
					for(int i = 0; i < NUM_RECEPTORS; i++)
						this.inputs[i] = 0;
					
					if(this.booleanNetwork[ACTIVATING] == 1) {
						if(++CapaNeutrophil.this.iteration == 4) {
							this.booleanNetwork[ACTIVATING] = 0;
							this.booleanNetwork[ACTIVE] = 1;
							CapaNeutrophil.this.clearPhenotype();
							CapaNeutrophil.this.addPhenotype(Phenotypes.ACTIVE);
							CapaNeutrophil.this.iteration = 0;
						}
					}else if(this.booleanNetwork[ACTIVE] == 1) {
						if(++CapaNeutrophil.this.iteration == 12) {
							CapaNeutrophil.this.clearPhenotype();
							this.booleanNetwork[ACTIVE] = 0;
							CapaNeutrophil.this.iteration = 0;
						}
					}
					
				}
				
			}
			
		};
		
		return network;
	}

}
