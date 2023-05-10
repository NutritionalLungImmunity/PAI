package edu.uf.interactable.capa;

import java.util.ArrayList;
import java.util.List;

import edu.uf.interactable.Pneumocyte;
import edu.uf.intracellularState.BooleanNetwork;
import edu.uf.intracellularState.EukaryoteSignalingNetwork;
import edu.uf.intracellularState.Phenotypes;
import edu.uf.utils.Constants;

public class CapaPneumocyte extends Pneumocyte{

	int iteration = 0;
	
	@Override
	protected BooleanNetwork createNewBooleanNetwork() {
		EukaryoteSignalingNetwork network = new EukaryoteSignalingNetwork() {

			public static final int size = 3;
			//public static final int NUM_RECEPTORS = 12;
			
			public static final int ACTIVATING = 0;
			public static final int MIX_ACTIVE = 1;
			public static final int TNF = 2;
			
			//public static final int FNP = 28; //IN THE OUTER CLASS!
			
			{
				this.inputs = new int[NUM_RECEPTORS];
				this.booleanNetwork = new int[size];
			}
			
			
			
			@Override
			public void processBooleanNetwork() {
				if(CapaPneumocyte.this.getClock().toc(BN_CLOCK, Constants.HALF_HOUR/Constants.TIME_STEP_SIZE)) { //convet minutes in iterations
					int k = 0;
					List<Integer> array = new ArrayList<>(size);
					for(int i = 0; i < size; i++)
						array.add(i);
					if(this.booleanNetwork[ACTIVATING] == 0 && this.booleanNetwork[MIX_ACTIVE] == 0) {
						this.booleanNetwork[ACTIVATING] = e(this.inputs, B_GLUC_e);
					}
					this.booleanNetwork[TNF] = this.booleanNetwork[MIX_ACTIVE] & e(this.inputs, TNFa_e);
					
					for(int i = 0; i < NUM_RECEPTORS; i++)
						this.inputs[i] = 0;
					
					if(this.booleanNetwork[ACTIVATING] == 1) {
						if(++CapaPneumocyte.this.iteration == 4) {
							this.booleanNetwork[ACTIVATING] = 0;
							this.booleanNetwork[MIX_ACTIVE] = 1;
							CapaPneumocyte.this.clearPhenotype();
							CapaPneumocyte.this.addPhenotype(Phenotypes.MIX_ACTIVE);
							CapaPneumocyte.this.iteration = 0;
						}
					}else if(this.booleanNetwork[MIX_ACTIVE] == 1) {
						if(++CapaPneumocyte.this.iteration == 12) {
							CapaPneumocyte.this.clearPhenotype();
							this.booleanNetwork[MIX_ACTIVE] = 0;
							CapaPneumocyte.this.iteration = 0;
						}
						if(this.booleanNetwork[TNF] == 1) {
							CapaPneumocyte.this.addPhenotype(Phenotypes.ACTIVE);
							CapaPneumocyte.this.iteration = 0;
						}
					}
					
				}
				
			}
			
		};
		
		return network;
	}
	
}
