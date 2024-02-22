package edu.uf.intracellularState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.uf.interactable.Hepcidin;
import edu.uf.interactable.IL1;
import edu.uf.interactable.IL10;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.TGFb;
import edu.uf.interactable.TLRBinder;
import edu.uf.interactable.TNFa;
import edu.uf.interactable.Afumigatus.Afumigatus;
//import edu.uf.interactable.covid.SAMP;
import edu.uf.interactable.invitro.GM_CSF;
import edu.uf.utils.Constants;

public class FMacrophageBooleanNetwork extends BooleanNetwork{

	
	public static final int size = 29;
	//public static final int NUM_RECEPTORS = 12;
	
	public static final int IFNGR = 0;
	public static final int IFNB = 1;
	public static final int CSF2Ra = 2;
	public static final int IL1R = 3;
	public static final int IL1B = 4;
	public static final int TLR4 = 5;
	public static final int FCGR = 6;
	public static final int IL4Ra = 7;
	public static final int IL10R = 8;
	public static final int IL10_out = 9;
	public static final int STAT1 = 10;
	public static final int SOCS1 = 11;
	public static final int STAT3 = 12;
	public static final int STAT5 = 13;
	public static final int IRF4 = 14;
	public static final int NFkB = 15;
	public static final int PPARG = 16;
	public static final int KLF4 = 17;
	public static final int STAT6 = 18;
	public static final int JMJD3 = 19;
	public static final int IRF3 = 20;
	public static final int ERK = 21;
	public static final int IL12_out = 22;
	public static final int TNFR = 23;
	public static final int Dectin = 24;
	public static final int ALK5 = 25;
	public static final int SMAD2 = 26;
	public static final int PtSR = 27;
	public static final int FPN = 28;
	
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
						this.booleanNetwork[IFNGR] = this.booleanNetwork[IFNB];// | e(this.inputs, IFNG_e);
						break;
					case 1:
						this.booleanNetwork[CSF2Ra] = input(GM_CSF.getMolecule());
						break;
					case 2:
						this.booleanNetwork[IL1R] = or(this.booleanNetwork[IL1B], input(IL1.getMolecule()));
						break;
					case 3:
						/*this.booleanNetwork[TLR4] = (e(this.inputs, LPS_e) | (e(this.inputs, Heme_e) & 
								(-e(this.inputs, Hpx_e) + N)) | e(this.inputs, DAMP_e) | e(this.inputs, VIRUS_e)) & 
								(-this.booleanNetwork[FCGR] + N);*/
						/*this.booleanNetwork[TLR4] = and(and(input(TLRBinder.getBinder()),
								  not(input(Hemopexin.getMolecule()), N)), not(this.booleanNetwork[FCGR], N));*/
						this.booleanNetwork[TLR4] = and(input(TLRBinder.getBinder()), not(this.booleanNetwork[FCGR], N));
						break;
					case 4:
						this.booleanNetwork[FCGR] = 0;//(e(this.inputs, IC_e) & e(this.inputs, LPS_e)) | (e(this.inputs, IC_e) & e(IL1.getMolecule()));
						break;
					case 5:
						this.booleanNetwork[IL4Ra] = 0;//e(this.inputs, IL4_e);
						break;
					case 6:
						this.booleanNetwork[IL10R] = or(input(IL10.getMolecule()), this.booleanNetwork[IL10_out]);
						break;
					case 7:
						this.booleanNetwork[STAT1] = and(this.booleanNetwork[IFNGR], not(or(this.booleanNetwork[SOCS1], this.booleanNetwork[STAT3]), N));
						break;
					case 8:
						this.booleanNetwork[STAT5] = and(this.booleanNetwork[CSF2Ra], not(or(this.booleanNetwork[STAT3], this.booleanNetwork[IRF4]), N));
						break;
					case 9:
						this.booleanNetwork[NFkB] = and(or(new int[] {
								this.booleanNetwork[IL1R], 
								this.booleanNetwork[TLR4], 
								this.booleanNetwork[Dectin], 
								this.booleanNetwork[TNFR]
						}), not(or(new int[] {
								this.booleanNetwork[STAT3], 
								this.booleanNetwork[FCGR], 
								this.booleanNetwork[PPARG], 
								this.booleanNetwork[KLF4]}), N));
						break;
					case 10:
						this.booleanNetwork[PPARG] = this.booleanNetwork[IL4Ra];
						break;
					case 11:
						this.booleanNetwork[STAT6] = this.booleanNetwork[IL4Ra];
						break;
					case 12:
						this.booleanNetwork[JMJD3] = this.booleanNetwork[IL4Ra];
						break;
					case 13:
						this.booleanNetwork[STAT3] = and(or(this.booleanNetwork[IL10R], this.booleanNetwork[SMAD2]), not(
								or(this.booleanNetwork[FCGR], this.booleanNetwork[PPARG]), N));
						break;
					case 14:
						this.booleanNetwork[IRF3] = this.booleanNetwork[TLR4];
						break;
					case 15:
						this.booleanNetwork[ERK] = this.booleanNetwork[FCGR];// | this.booleanNetwork[Dectin];
						break;
					case 16:
						this.booleanNetwork[KLF4] = this.booleanNetwork[STAT6];
						break;
					case 17:
						this.booleanNetwork[IL1B] = this.booleanNetwork[NFkB];
						break;
					case 18:
						this.booleanNetwork[IFNB] = this.booleanNetwork[IRF3];
						break;
					case 19:
						this.booleanNetwork[IL12_out] = or(new int[] {this.booleanNetwork[STAT1], this.booleanNetwork[STAT5], this.booleanNetwork[NFkB]});
						break;
					case 20: 
						this.booleanNetwork[IL10_out] = or(new int[] {
								this.booleanNetwork[PPARG], 
								this.booleanNetwork[STAT6], 
								this.booleanNetwork[JMJD3], 
								this.booleanNetwork[STAT3], 
								this.booleanNetwork[ERK], 
								this.booleanNetwork[PtSR]
						});
						break;
					case 21:
						this.booleanNetwork[TNFR] = input(TNFa.getMolecule());
						break;
					case 22:
						this.booleanNetwork[Dectin] = input(Afumigatus.DEF_OBJ);//e(this.inputs, B_GLUC_e);
						break;
					case 23:
						this.booleanNetwork[SOCS1] = this.booleanNetwork[STAT6];
						break;
					case 24:
						this.booleanNetwork[IRF4] = this.booleanNetwork[JMJD3];
						break;
					case 25:
						this.booleanNetwork[ALK5] = input(TGFb.getMolecule());
						break;
					case 26:
						this.booleanNetwork[SMAD2] = this.booleanNetwork[ALK5];
						break;
					case 27:
						//this.booleanNetwork[PtSR] = input(SAMP.getMolecule());
						break;
					case 28:
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
		
		
		if(this.booleanNetwork[NFkB] > 0 || this.booleanNetwork[STAT1] > 0 || this.booleanNetwork[STAT5] > 0)
			this.getPhenotype().put(Macrophage.M1, this.or(new int[] {this.booleanNetwork[NFkB], this.booleanNetwork[STAT1], this.booleanNetwork[STAT5]}));
		if(this.booleanNetwork[STAT6] > 0)
			this.getPhenotype().put(Macrophage.M2A, this.booleanNetwork[STAT6]);
		if(this.booleanNetwork[ERK] > 0)
			this.getPhenotype().put(Macrophage.M2B, this.booleanNetwork[ERK]);
		if(this.booleanNetwork[STAT3] > 0)
			this.getPhenotype().put(Macrophage.M2C, this.booleanNetwork[STAT3]);
		//else
		//	Macrophage.this.addPhenotype(Phenotypes.RESTING);
			
		
	
		
	}

}
