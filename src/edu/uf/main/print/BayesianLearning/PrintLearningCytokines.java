package edu.uf.main.print.BayesianLearning;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import edu.uf.interactable.Afumigatus;
import edu.uf.interactable.IL1;
import edu.uf.interactable.IL10;
import edu.uf.interactable.IL6;
import edu.uf.interactable.MCP1;
import edu.uf.interactable.MIP1B;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.Pneumocyte;
import edu.uf.interactable.TGFb;
import edu.uf.interactable.TNFa;
import edu.uf.interactable.covid.SAMP;
import edu.uf.interactable.invitro.BGlucan;
import edu.uf.interactable.invitro.GM_CSF;
import edu.uf.interactable.invitro.IL8;
import edu.uf.interactable.invitro.LPS;
import edu.uf.main.print.PrintStat;

public class PrintLearningCytokines extends PrintStat{

	@Override
	public void printStatistics(int k, File file) {
		String str = k + "\t" + 
				  BGlucan.getMolecule().getTotalMolecule(0) + "\t" +
	              LPS.getMolecule().getTotalMolecule(0) + "\t" +
	              SAMP.getMolecule().getTotalMolecule(0) + "\t" +
	              TGFb.getMolecule().getTotalMolecule(0) + "\t" +
	              IL1.getMolecule().getTotalMolecule(0) + "\t" +
	              IL6.getMolecule().getTotalMolecule(0) + "\t" +
	              IL8.getMolecule().getTotalMolecule(0) + "\t" +
	              IL10.getMolecule().getTotalMolecule(0) + "\t" +
	              MCP1.getMolecule().getTotalMolecule(0) + "\t" +
	              MIP2.getMolecule().getTotalMolecule(0) + "\t" +
	              TNFa.getMolecule().getTotalMolecule(0) + "\t" +
	              Macrophage.getTotalCells() + "\t" +
	              Afumigatus.getTotalCells();
		
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

}
