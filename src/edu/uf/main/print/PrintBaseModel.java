package edu.uf.main.print;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map.Entry;

import edu.uf.compartments.Voxel;
import edu.uf.interactable.IL10;
import edu.uf.interactable.IL6;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.PneumocyteII;
import edu.uf.interactable.TGFb;
import edu.uf.interactable.TNFa;
import edu.uf.interactable.Transferrin;
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.interactable.Afumigatus.TAFC;
import edu.uf.intracellularState.AspergillusIntracellularModel;
import edu.uf.intracellularState.PneumocyteStateModel;

public class PrintBaseModel extends PrintStat{
	
	int resting;
	int swelling;
	int germ;
	int hyphae;
	int MAMixActive;
	int MAActive;
	int MAInactive;
	
	public Voxel[][][] grid;

	@Override
	public void printStatistics(int k, File file){
		//if(k%30 == 0)count();
		if(k%15 != 0)return;
		String str = k + "\t" + 
	              Afumigatus.getTotalCells0() + "\t" +
	              Afumigatus.getTotalRestingConidia() + "\t" +
	              Afumigatus.getTotalSwellingConidia() + "\t" +
	              Afumigatus.getTotalGerminatingConidia() + "\t" +
	              Afumigatus.getTotalHyphae() + "\t" +
	              //PResting + "\t" +
	              //PMixActive + "\t" +
	              //PActive + "\t" +
	              (TAFC.getMolecule().getTotalMolecule(0) + TAFC.getMolecule().getTotalMolecule(1)) + "\t" +
	              TAFC.getMolecule().getTotalMolecule(0) + "\t" +
	              TAFC.getMolecule().getTotalMolecule(1) + "\t" +
	              /*Lactoferrin.getMolecule().getTotalMolecule(0) + "\t" +
	              Lactoferrin.getMolecule().getTotalMolecule(1) + "\t" +
	              Lactoferrin.getMolecule().getTotalMolecule(2) + "\t" +*/
	              (Transferrin.getMolecule().getTotalMolecule(0) + Transferrin.getMolecule().getTotalMolecule(1) + Transferrin.getMolecule().getTotalMolecule(2)) + "\t" +
	              Transferrin.getMolecule().getTotalMolecule(0) + "\t" +
	              Transferrin.getMolecule().getTotalMolecule(1) + "\t" +
	              Transferrin.getMolecule().getTotalMolecule(2) + "\t" +
	              //Hepcidin.getMolecule().getTotalMolecule(0) + "\t" +
	              //TGFb.getMolecule().getTotalMolecule(0) + "\t" +
	              //IL6.getMolecule().getTotalMolecule(0) + "\t" +
	              //IL10.getMolecule().getTotalMolecule(0) + "\t" +
	              TNFa.getMolecule().getTotalMolecule(0) + "\t" +
	              //MIP1B.getMolecule().getTotalMolecule(0) + "\t" +
	              MIP2.getMolecule().getTotalMolecule(0) + "\t" +
	              //Erythrocyte.getTotalCells() + "\t" + 
	              /*MAResting + "\t" +
	              MAMixActive + "\t" +
	              MAActive + "\t" +
	              MAInactive + "\t" +*/
	              Macrophage.getTotalCells() + "\t" +
	              Neutrophil.getTotalCells();
		
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
	
	void count() {
		resting = 0;
		swelling = 0;
		germ = 0;
		hyphae = 0;
		MAMixActive = 0;
		MAActive = 0;
		MAInactive = 0;
		for(Voxel[][] VV : grid) {
			for(Voxel[] V : VV) {
				for(Voxel v : V) {
					for(Entry<Integer, Interactable> entry : v.getInteractables().entrySet()) {
						Interactable cell = entry.getValue();
						if(cell instanceof Afumigatus) {
							Afumigatus p = (Afumigatus) cell;
							/*if(p.getBooleanNetwork().hasPhenotype(AspergillusIntracellularModel.RESTING_CONIDIA))resting++;
							if(p.getBooleanNetwork().hasPhenotype(AspergillusIntracellularModel.SWELLING_CONIDIA))swelling++;
							if(p.getBooleanNetwork().hasPhenotype(AspergillusIntracellularModel.GERM_TUBE))germ++;
							if(p.getBooleanNetwork().hasPhenotype(AspergillusIntracellularModel.HYPHAE))hyphae++;*/
							
							//if(p.getBooleanNetwork().hasPhenotype(PneumocyteStateModel.MIX_ACTIVE))PMixActive++;
							//else if(p.getBooleanNetwork().hasPhenotype(PneumocyteStateModel.ACTIVE))PActive++;
						}/*else if(cell instanceof Macrophage) {
							Macrophage p = (Macrophage) cell;
							if(p.getPhenotype() == Phenotypes.RESTING)MAResting++;
							else if(p.getPhenotype() == Phenotypes.MIX_ACTIVE)MAMixActive++;
							else if(p.getPhenotype() == Phenotypes.ACTIVE)MAActive++;
							else if(p.getPhenotype() == Phenotypes.INACTIVE)MAInactive++;
						}*/
					}
				}
			}
		}
		System.out.println(">>> " + resting  + " " + swelling + " " + germ + " " + hyphae);
	}
	

}


//307200
