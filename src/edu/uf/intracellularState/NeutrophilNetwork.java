package edu.uf.intracellularState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.uf.interactable.IL1;
import edu.uf.interactable.Lactoferrin;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.TNFa;
import edu.uf.utils.Constants;

public class NeutrophilNetwork extends IntracellularModel{
	
	public static final String name = "NeutrophilNetwork";

	public static final int size = 7;
	//public static final int NUM_RECEPTORS = 4;
	
	public static final int IL1R = 0;
	public static final int IL1B = 1;
	public static final int NFkB = 2;
	public static final int ERK = 3;
	public static final int TNFR = 4;
	public static final int Dectin = 5;
	public static final int CXCL2R = 6;
	
	
	public static final int ACTIVE = Phenotype.createPhenotype();
    public static final int NETOTIC = Phenotype.createPhenotype();
    public static final int MIX_ACTIVE = Phenotype.createPhenotype();
    public static final int PYROPTOTIC = Phenotype.createPhenotype();
	
	{
		this.booleanNetwork = new int[size];
	}
	
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
						this.booleanNetwork[IL1R] = this.booleanNetwork[IL1B] | getInput(IL1.getMolecule());
						break;
					case 1:
						this.booleanNetwork[NFkB] = (this.booleanNetwork[IL1R] | this.booleanNetwork[TNFR] | this.booleanNetwork[CXCL2R]) ;
						break;
					case 2:
						this.booleanNetwork[ERK] = this.booleanNetwork[Dectin];
						break;
					case 3:
						this.booleanNetwork[IL1B] = this.booleanNetwork[NFkB];
						break;
					case 4:
						this.booleanNetwork[TNFR] = getInput(TNFa.getMolecule());
						break;
					case 5:
						this.booleanNetwork[Dectin] = 0;//e(B_GLUC);
						break;
					case 6:
						this.booleanNetwork[CXCL2R] = getInput(MIP2.getMolecule());
						break;
					default:
						System.err.println("No such interaction " + i + "!");
						break;
				}
			}
		}
		
		this.inputs.clear();
		this.clearPhenotype();
		this.computePhenotype();
		
	}
	
	protected void computePhenotype() {
		if(this.booleanNetwork[NFkB] > 0) {
			this.getPhenotype().put(ACTIVE, this.booleanNetwork[NFkB]);
			this.getPhenotype().put(Lactoferrin.getMolecule().getPhenotype(), this.booleanNetwork[NFkB]);
		} 
		if(this.booleanNetwork[ERK] == 1) {
			this.getPhenotype().put(MIX_ACTIVE, this.booleanNetwork[ERK]);
			this.getPhenotype().put(Lactoferrin.getMolecule().getPhenotype(), this.booleanNetwork[NFkB]);
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
