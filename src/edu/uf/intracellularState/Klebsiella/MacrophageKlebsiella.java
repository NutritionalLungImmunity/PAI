package edu.uf.intracellularState.Klebsiella;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.uf.interactable.Hepcidin;
import edu.uf.interactable.IFN_I;
import edu.uf.interactable.IFN_III;
import edu.uf.interactable.IL1;
import edu.uf.interactable.IL10;
import edu.uf.interactable.IL23;
import edu.uf.interactable.IL6;
import edu.uf.interactable.TLRBinder;
import edu.uf.interactable.TNFa;
import edu.uf.interactable.invitro.GM_CSF;
import edu.uf.intracellularState.IntracellularModel;
import edu.uf.utils.Constants;

public class MacrophageKlebsiella extends IntracellularModel{

	public static final String name = "MacrophageKlebsiella";

	
	public static final int size = 14;
	//public static final int NUM_RECEPTORS = 12;
	
	public static final int IFNGR = 0;
	public static final int IFNB = 1;
	public static final int CSF2Ra = 2;
	public static final int IL1R = 3;
	public static final int IL1B = 4;
	public static final int TLR4 = 5;
	public static final int IL10R = 6;
	public static final int IL10_out = 7;
	public static final int STAT1 = 8;
	public static final int STAT3 = 9;
	public static final int STAT5 = 10;
	public static final int NFkB = 11;
	public static final int IRF3 = 12;
	public static final int IL12_out = 13;
	public static final int TNFR = 14;
	public static final int FPN = 15;
	
	//public static final int FNP = 28; //IN THE OUTER CLASS!
	
	{
		this.inputs = new int[NUM_RECEPTORS];
		this.booleanNetwork = new int[size];
	}
	
	private static final int N = 4;
	
	@Override
	public void processBooleanNetwork(int... args) {

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
						this.booleanNetwork[IFNGR] = max(this.booleanNetwork[IFNB], input(IFN_III.getMolecule()));// | e(this.inputs, IFNG_e);
						break;
					case 1:
						this.booleanNetwork[CSF2Ra] = input(GM_CSF.getMolecule());
						break;
					case 2:
						this.booleanNetwork[IL1R] = max(this.booleanNetwork[IL1B], input(IL1.getMolecule()));
						break;
					case 3:
						this.booleanNetwork[TLR4] = input(TLRBinder.getBinder());
						break;
					case 4:
						this.booleanNetwork[IL10R] = max(input(IL10.getMolecule()), this.booleanNetwork[IL10_out]);
						break;
					case 5:
						this.booleanNetwork[STAT1] = min(this.booleanNetwork[IFNGR], not(this.booleanNetwork[STAT3], N));
						break;
					case 6:
						this.booleanNetwork[STAT5] = min(this.booleanNetwork[CSF2Ra], not(this.booleanNetwork[STAT3], N));
						break;
					case 7:
						this.booleanNetwork[NFkB] = min(max(new int[] {
								this.booleanNetwork[IL1R], 
								this.booleanNetwork[TLR4],  
								this.booleanNetwork[TNFR]
						}), not(this.booleanNetwork[STAT3], N));
						break;
					case 8:
						this.booleanNetwork[IRF3] = this.booleanNetwork[TLR4];
						break;
					case 9:
						this.booleanNetwork[IL1B] = this.booleanNetwork[NFkB];
						break;
					case 10:
						this.booleanNetwork[IFNB] = this.booleanNetwork[IRF3];
						break;
					case 11:
						this.booleanNetwork[IL12_out] = max(new int[] {this.booleanNetwork[STAT1], this.booleanNetwork[STAT5], this.booleanNetwork[NFkB]});
						break;
					case 12: 
						this.booleanNetwork[IL10_out] = this.booleanNetwork[STAT3]; 
						break;
					case 13:
						this.booleanNetwork[TNFR] = input(TNFa.getMolecule());
						break;
					case 14:
						this.booleanNetwork[FPN] = (-input(Hepcidin.getMolecule()) + N);
						break;
					default:
						System.err.println("No such interaction " + i + "!");
						break;
				}
			}
		}
		
		for(int i = 0; i < NUM_RECEPTORS; i++)
			this.inputs[i] = 0;
		
		this.clearPhenotype();
		
		
		this.computePhenotype();
		
	}
	
	protected void computePhenotype() {
		if(this.booleanNetwork[NFkB] > 0) {
			this.getPhenotype().put(IL6.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[NFkB]}));
			this.getPhenotype().put(TNFa.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[NFkB]}));
			this.getPhenotype().put(IL23.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[NFkB]}));
			this.getPhenotype().put(IL1.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[NFkB]}));
		}
		if(this.booleanNetwork[IRF3] > 0) {
			this.getPhenotype().put(IFN_I.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[IRF3]}));
		}
		if(this.booleanNetwork[STAT3] > 0) {
			this.getPhenotype().put(IL10.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[STAT3]}));
		}
		if(this.booleanNetwork[STAT1] > 0) {
			this.getPhenotype().put(IL10.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[STAT1]}));
		}
		if(this.booleanNetwork[STAT5] > 0) {
			this.getPhenotype().put(IL10.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[STAT5]}));
		}
	}

	/**
	 * Disabled.
	 */
	@Override
	public void updateStatus(int id, int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}
	
}
