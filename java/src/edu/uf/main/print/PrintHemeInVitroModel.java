/*package edu.uf.main.print;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import edu.uf.interactable.Afumigatus;
import edu.uf.interactable.Heme;
import edu.uf.interactable.TAFC;
import edu.uf.interactable.invitro.FreeIron;
import edu.uf.interactable.invitro.Glutamate;
import edu.uf.interactable.invitro.InVitroHeme;
import edu.uf.interactable.invitro.TinProtoporphyrin;

public class PrintHemeInVitroModel extends PrintBaseInVitro{

	@Override
	public void printStatistics(int k, File file){
    	
		String str = k + "\t" + 
	              Afumigatus.getTotalCells() + "\t" +
	              Afumigatus.getTotalRestingConidia() + "\t" +
	              Afumigatus.getTotalSwellingConidia() + "\t" +
	              Afumigatus.getTotalGerminatingConidia() + "\t" +
	              Afumigatus.getTotalHyphae() + "\t" +
	              FreeIron.getMolecule().getTotalMolecule(0) + "\t" +
	              (TAFC.getMolecule().getTotalMolecule(0) + TAFC.getMolecule().getTotalMolecule(1)) + "\t" +
	              TAFC.getMolecule().getTotalMolecule(0) + "\t" +
	              TAFC.getMolecule().getTotalMolecule(1) + "\t" +
	              InVitroHeme.getMolecule().getTotalMolecule(0) + "\t" +
	              TinProtoporphyrin.getMolecule().getTotalMolecule(0) + "\t" +
	              Glutamate.getMolecule().getTotalMolecule(0);
		
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
	
}*/
