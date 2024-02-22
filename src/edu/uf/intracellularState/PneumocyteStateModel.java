package edu.uf.intracellularState;

import edu.uf.interactable.IL1;
import edu.uf.interactable.PneumocyteII;
import edu.uf.interactable.TNFa;
import edu.uf.interactable.Afumigatus.Afumigatus;

public class PneumocyteStateModel extends BooleanNetwork {


	public static final int size = 2;
	
	public static final int MIX_ACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int ITER_REST = 12;
	private int iterations1 = 0;
	private int iterations2 = 0;
	
	public static final int N = 4;
	
	{
		this.inputs = new int[NUM_RECEPTORS];
		this.booleanNetwork = new int[size];
	}
	
	private int countMix() {
		if((this.booleanNetwork[MIX_ACTIVE]) == 0)
			return 0;
		if(iterations1++ >= ITER_REST) {
			iterations1 = 0;
			return 0;
		}
		return 1;
	}
	
	private int countActive() {
		if((this.booleanNetwork[ACTIVE]) == 0)
			return 0;
		//System.out.println(iterations2 + " " + ITER_REST);
		if(iterations2++ >= ITER_REST) {
			iterations2 = 0;
			return 0;
		}
		return 1;
	}
	
	
	@Override
	public void processBooleanNetwork(int... args) {
		
		//this.booleanNetwork[ACTIVE] =  or(input(Afumigatus.DEF_OBJ), N*countMix());//, (-this.booleanNetwork[ACTIVE] + N));
		//this.booleanNetwork[ACTIVE] = and((or(input(IL1.getMolecule()), input(TNFa.getMolecule()))), this.booleanNetwork[MIX_ACTIVE]);
		
		this.booleanNetwork[MIX_ACTIVE] =  and(or(input(Afumigatus.DEF_OBJ), N*countMix()), (-this.booleanNetwork[ACTIVE] + N));
		this.booleanNetwork[ACTIVE] = or(and((or(input(IL1.getMolecule()), input(TNFa.getMolecule()))), this.booleanNetwork[MIX_ACTIVE]), N*countActive());

		this.iterations1 = input(Afumigatus.DEF_OBJ) > 0 ? 0 : this.iterations1;
		this.iterations2 = input(Afumigatus.DEF_OBJ) > 0 | input(IL1.getMolecule()) > 0 | input(TNFa.getMolecule()) > 0 ? 0 : this.iterations2;
		
		for(int i = 0; i < NUM_RECEPTORS; i++)
			this.inputs[i] = 0;
		
		this.clearPhenotype();
		
		if(this.booleanNetwork[ACTIVE] > 0) {
			this.getPhenotype().put(PneumocyteII.ACTIVE, this.booleanNetwork[ACTIVE]);
			//System.out.println("active");
		} 
		if(this.booleanNetwork[MIX_ACTIVE] > 0) {
			this.getPhenotype().put(PneumocyteII.MIX_ACTIVE, this.booleanNetwork[MIX_ACTIVE]);
			//System.out.println("mix active");
		}
		
	}

}
