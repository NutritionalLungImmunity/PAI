package edu.uf.interactable.covid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.uf.interactable.Interactable;
import edu.uf.interactable.Molecule;
import edu.uf.intracellularState.BooleanNetwork;
import edu.uf.intracellularState.EukaryoteSignalingNetwork;
import edu.uf.intracellularState.Phenotypes;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;

public class Neutrophil extends edu.uf.interactable.Neutrophil{

	public static final int RECEPTOR_IDX_2 = Molecule.getReceptors();
	private boolean degranuled = false;
	
	public Neutrophil() {
		super(0.0);
	}
	
	public boolean isDeganuled() {
		return degranuled;
	}
	
	public void degranuled() {
		this.degranuled = true;
	}
	
	/*@Override
	protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
		if(interactable instanceof Pneumocyte) {
			Pneumocyte cell = (Pneumocyte) interactable;
			EukaryoteSignalingNetwork.ROS_e = Neutrophil.RECEPTOR_IDX_2;
			if(this.inPhenotype(Phenotypes.ACTIVE) && Rand.getRand().randunif() < Constants.PR_ROS) 
				cell.bind(Neutrophil.RECEPTOR_IDX_2);
			return true;
		}
		
		return super.templateInteract(interactable, x, y, z);
	}*/
	
	@Override
	protected BooleanNetwork createNewBooleanNetwork() {
		return new EukaryoteSignalingNetwork() {

			public static final int size = 3;
			public static final int NUM_PHENOTYPES = 2;
			
			public static final int TLR4 = 0;
			public static final int ROS = 1;
			public static final int CASP8 = 2;
			/*public static final int AKT = 3;
			public static final int SHIP = 4;
			public static final int APO = 5;
			public static final int NET = 6;*/
			
			
			{
				this.inputs = new int[NUM_RECEPTORS];
				this.booleanNetwork = new int[size];
			}
			
			
			
			@Override
			public void processBooleanNetwork() {
				if(Neutrophil.this.getClock().toc(BN_CLOCK, Constants.HALF_HOUR/Constants.TIME_STEP_SIZE)) { //convet minutes in iterations
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
									this.booleanNetwork[TLR4] = o(this.inputs, TLR4_o);
									break;
								case 1:
									this.booleanNetwork[ROS] = this.booleanNetwork[TLR4];//or(this.booleanNetwork[TLR4], this.booleanNetwork[ROS]);
									break;
								case 2:
									this.booleanNetwork[CASP8] = p(Constants.NEUTROPHIL_HALF_LIFE);//and(this.booleanNetwork[ROS], not(this.booleanNetwork[AKT], 1));
									break;
								case 3:
									//this.booleanNetwork[AKT] = or(this.booleanNetwork[TLR4], and(this.booleanNetwork[AKT], not(this.booleanNetwork[SHIP], 1)));
									//break;
								case 4:
									//this.booleanNetwork[SHIP] = this.booleanNetwork[ROS];
									//break;
								case 5:
									//int ii = p(Constants.NEUTROPHIL_HALF_LIFE);
									//System.out.println(this.booleanNetwork[CASP8] + " " + ii + " " + not(this.booleanNetwork[AKT], 1));
									//this.booleanNetwork[APO]= and(or(this.booleanNetwork[CASP8], p(Constants.NEUTROPHIL_HALF_LIFE)), not(this.booleanNetwork[AKT], 1));
									//break;
								case 6:
									//this.booleanNetwork[NET]=this.booleanNetwork[AKT];
									//break;
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
					
					Neutrophil.this.clearPhenotype();
					
					Collections.shuffle(array, new Random());
					for(int i : array) {
						switch(i) {
							case 0:
								if(this.booleanNetwork[CASP8] == 1)
									Neutrophil.this.addPhenotype(Phenotypes.APOPTOTIC);
								return;
							/*case 1:
								if(this.booleanNetwork[NET] == 1)
									Neutrophil.this.addPhenotype(Phenotypes.NECROTIC);
								return;*/
							case 1:
								if(this.booleanNetwork[ROS] == 1)
									Neutrophil.this.addPhenotype(Phenotypes.ACTIVE);
								return;
							default:
								System.err.println("No such phenotype " + i);
						}
					}
				}
			}
			
			private int p(double d) {
				return Rand.getRand().randunif() < d ? 1 : 0;
			}
		};
	}
}