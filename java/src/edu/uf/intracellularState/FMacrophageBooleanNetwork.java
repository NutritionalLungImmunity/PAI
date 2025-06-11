package edu.uf.intracellularState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.uf.interactable.Hepcidin;
import edu.uf.interactable.IFN_I;
import edu.uf.interactable.IFN_II;
import edu.uf.interactable.IL1;
import edu.uf.interactable.IL10;
import edu.uf.interactable.IL4;
import edu.uf.interactable.Molecule;
import edu.uf.interactable.PtSRBinder;
import edu.uf.interactable.TGFb;
import edu.uf.interactable.TLRBinder;
import edu.uf.interactable.TNFa;
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.interactable.invitro.BGlucan;
//import edu.uf.interactable.covid.SAMP;
import edu.uf.interactable.invitro.GM_CSF;
import edu.uf.utils.Constants;

public abstract class FMacrophageBooleanNetwork extends IntracellularModel{
	
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
	
	
	/*public static final int M1  = Phenotype.createPhenotype();
	public static final int M2A = Phenotype.createPhenotype();
	public static final int M2B = Phenotype.createPhenotype();
	public static final int M2C = Phenotype.createPhenotype();*/
	
	//public static final int FNP = 28; //IN THE OUTER CLASS!
	
	{
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
						this.booleanNetwork[IFNGR] = max(this.booleanNetwork[IFNB], getInput(Molecule.searchMolecule(IFN_II.NAME)));
						break;
					case 1:
						this.booleanNetwork[CSF2Ra] = getInput(Molecule.searchMolecule("GM_CSF"));
						break;
					case 2:
						this.booleanNetwork[IL1R] = max(this.booleanNetwork[IL1B], getInput(Molecule.searchMolecule(IL1.NAME)));
						break;
					case 3:
						this.booleanNetwork[TLR4] = min(getInput(TLRBinder.getBinder()), not(this.booleanNetwork[FCGR], N));
						break;
					case 4:
						this.booleanNetwork[FCGR] = max(
								min(getInput(Molecule.searchMolecule("IC")), getInput(TLRBinder.getBinder())),
								min(getInput(Molecule.searchMolecule("IC")), getInput(Molecule.searchMolecule(IL1.NAME)))
						);
						break;
					case 5:
						this.booleanNetwork[IL4Ra] = getInput(Molecule.searchMolecule(IL4.NAME));
						break;
					case 6:
						this.booleanNetwork[IL10R] = max(getInput(Molecule.searchMolecule(IL10.NAME)), this.booleanNetwork[IL10_out]);
						break;
					case 7:
						this.booleanNetwork[STAT1] = min(this.booleanNetwork[IFNGR], not(max(this.booleanNetwork[SOCS1], this.booleanNetwork[STAT3]), N));
						break;
					case 8:
						this.booleanNetwork[STAT5] = min(this.booleanNetwork[CSF2Ra], not(max(this.booleanNetwork[STAT3], this.booleanNetwork[IRF4]), N));
						break;
					case 9:
						this.booleanNetwork[NFkB] = min(max(new int[] {
								this.booleanNetwork[IL1R], 
								this.booleanNetwork[TLR4], 
								this.booleanNetwork[Dectin], 
								this.booleanNetwork[TNFR]
						}), not(max(new int[] {
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
						this.booleanNetwork[STAT3] = min(max(this.booleanNetwork[IL10R], this.booleanNetwork[SMAD2]), not(
								max(this.booleanNetwork[FCGR], this.booleanNetwork[PPARG]), N));
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
						this.booleanNetwork[IL12_out] = max(new int[] {this.booleanNetwork[STAT1], this.booleanNetwork[STAT5], this.booleanNetwork[NFkB]});
						break;
					case 20: 
						this.booleanNetwork[IL10_out] = max(new int[] {
								this.booleanNetwork[PPARG], 
								this.booleanNetwork[STAT6], 
								this.booleanNetwork[JMJD3], 
								this.booleanNetwork[STAT3], 
								this.booleanNetwork[ERK], 
								this.booleanNetwork[PtSR]
						});
						break;
					case 21:
						this.booleanNetwork[TNFR] = getInput(Molecule.searchMolecule(TNFa.NAME));
						break;
					case 22:
						this.booleanNetwork[Dectin] = max(getInput(Afumigatus.DEF_OBJ), getInput(Molecule.searchMolecule(BGlucan.NAME)));
						break;
					case 23:
						this.booleanNetwork[SOCS1] = this.booleanNetwork[STAT6];
						break;
					case 24:
						this.booleanNetwork[IRF4] = this.booleanNetwork[JMJD3];
						break;
					case 25:
						this.booleanNetwork[ALK5] = getInput(Molecule.searchMolecule(TGFb.NAME));
						break;
					case 26:
						this.booleanNetwork[SMAD2] = this.booleanNetwork[ALK5];
						break;
					case 27:
						this.booleanNetwork[PtSR] = getInput(PtSRBinder.getBinder());
						break;
					case 28:
						//this.booleanNetwork[FPN] = (-getInput(Hepcidin.getMolecule()) + N);
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
		
		
		/*if(this.booleanNetwork[NFkB] > 0 || this.booleanNetwork[STAT1] > 0 || this.booleanNetwork[STAT5] > 0)
			this.getPhenotype().put(M1, this.or(new int[] {this.booleanNetwork[NFkB], this.booleanNetwork[STAT1], this.booleanNetwork[STAT5]}));
		if(this.booleanNetwork[STAT6] > 0)
			this.getPhenotype().put(M2A, this.booleanNetwork[STAT6]);
		if(this.booleanNetwork[ERK] > 0)
			this.getPhenotype().put(M2B, this.booleanNetwork[ERK]);
		if(this.booleanNetwork[STAT3] > 0)
			this.getPhenotype().put(M2C, this.booleanNetwork[STAT3]);
		*/
		//else
		//	Macrophage.this.addPhenotype(Phenotypes.RESTING);
			
		
	
		
	}

}
