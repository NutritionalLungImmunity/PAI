package edu.uf.main.print;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map.Entry;

import edu.uf.compartments.Voxel;
import edu.uf.interactable.Heme;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.PneumocyteI;
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.intracellularState.NeutrophilStateModel;

public class PrintHemeModel extends PrintStat{
	
	int net;
	int PMixActive;
	int PActive;
	int MAResting;
	int MAMixActive;
	int MAActive;
	int MAInactive;
	
	public Voxel[][][] grid;

	@Override
	public void printStatistics(int k, File file){
		count();
		if(k%15 != 0)return;
		String str = k + "\t" + 
	              Afumigatus.getTotalCells() + "\t" +
	              Afumigatus.getTotalRestingConidia() + "\t" +
	              Afumigatus.getTotalSwellingConidia() + "\t" +
	              Afumigatus.getTotalGerminatingConidia() + "\t" +
	              Afumigatus.getTotalHyphae() + "\t" +
	              MIP2.getMolecule().getTotalMolecule(0) + "\t" +
	              Heme.getMolecule().getTotalMolecule(0)  + "\t" +
	              //DNAse.getMolecule().getTotalMolecule(0)  + "\t" +
	              //Erythrocyte.getTotalCells() + "\t" +
	              PneumocyteI.getTotalCells() + "\t" +
	              Macrophage.getTotalCells() + "\t" +
	              Neutrophil.getTotalCells() + "\t" + 
	              net;
		
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
		net = 0;
		PMixActive = 0;
		PActive = 0;
		MAResting = 0;
		MAMixActive = 0;
		MAActive = 0;
		MAInactive = 0;
		for(Voxel[][] VV : grid) {
			for(Voxel[] V : VV) {
				for(Voxel v : V) {
					for(Entry<Integer, Interactable> entry : v.getInteractables().entrySet()) {
						Interactable cell = entry.getValue();
						if(cell instanceof Neutrophil) {
							Neutrophil p = (Neutrophil) cell;
							if(p.hasPhenotype(NeutrophilStateModel.NETOTIC))net++;
							//else if(p.hasPhenotype(Pneumocyte.ACTIVE))PActive++;
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
	}
	

}


//307200

