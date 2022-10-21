package edu.uf.main.print;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map.Entry;

import edu.uf.compartments.Voxel;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.covid.Neutrophil;
import edu.uf.interactable.covid.Pneumocyte;
import edu.uf.interactable.covid.SarsCoV2;
import edu.uf.interactable.TNFa;
import edu.uf.interactable.covid.H2O2;
import edu.uf.intracellularState.Phenotypes;

public class PrintCitotoxicity extends PrintStat{

	@Override
	public void printStatistics(int k, File file) {
		if((k+1)%30!=0 && k != 1)return;
		count();
		
		String str = k + "\t" + 
	              //((int) (TNFa.getMolecule().getTotalMolecule(0) * 17000 * 1e3 * 1e12)) + "\t" +
	              SarsCoV2.getMolecule().getTotalMolecule(0)+ "\t" +
	              ((int) (H2O2.getMolecule().getTotalMolecule(0) * 1e3 * 1e12)) + "\t" +
	              //H2O2.getMolecule().getTotalMolecule(0) + "\t" + 
	              Pneumocyte.getTotalCells() + "\t" + 
	              Neutrophil.getTotalCells() + "\t" + PActive;// + "\t" + PActive + "\t" + PResting;
		if(file == null) {
			System.out.println(str);
		}else {
			try {
		
				if(getPrintWriter() == null) 
					setPrintWriter(new PrintWriter(file)); 
				getPrintWriter().println(str);
			}catch(FileNotFoundException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public Voxel[][][] grid;
	int PResting;
	int PActive;
	int neut;
	int dead;
	
	void count() {
		PResting = 0;
		neut = 0;
		PActive = 0;
		dead = 0;
		for(Voxel[][] VV : grid) {
			for(Voxel[] V : VV) {
				for(Voxel v : V) {
					for(Entry<Integer, Interactable> entry : v.getInteractables().entrySet()) {
						Interactable cell = entry.getValue();
						
						if(cell instanceof Neutrophil) {
							neut++;
							Neutrophil p = (Neutrophil) cell;
							if(p.inPhenotype(Phenotypes.INACTIVE))PResting++;
							if(p.inPhenotype(Phenotypes.ACTIVE))PActive++;
							if(p.inPhenotype(new int[] {Phenotypes.APOPTOTIC, Phenotypes.NECROTIC}))dead++;
						}
					}
				}
			}
		}
	}

}
