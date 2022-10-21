package edu.uf.main.print;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map.Entry;

import edu.uf.compartments.Voxel;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.covid.Pneumocyte;
import edu.uf.interactable.covid.SarsCoV2;

public class PrintLearnTCID50  extends PrintStat{

	@Override
	public void printStatistics(int k, File file) {
		String infected = countInfected();
		String str = k + "\t" + 
				  SarsCoV2.getMolecule().getTotalMolecule(0) + "\t" +
	              Pneumocyte.getTotalCells() + "\t" + 
				  infected;
		
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
	
	private String countInfected() {
		int count = 0;
		double load = 0;
		for(Voxel[][] VV : grid) {
			for(Voxel[] V : VV) {
				for(Voxel v : V) {
					for(Entry<Integer, Interactable> entry : v.getInteractables().entrySet()) {
						Interactable cell = entry.getValue();
						if(cell instanceof Pneumocyte) {
							if(((Pneumocyte) cell).getViralLoad() > 0) {
								count++;
								load += ((Pneumocyte) cell).getViralLoad();
							}
						}
					}
				}
			}
		}
		return load + "\t" + count + "";
	}
	
}
