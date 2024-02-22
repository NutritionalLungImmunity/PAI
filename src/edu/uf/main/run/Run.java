package edu.uf.main.run;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import edu.uf.compartments.Recruiter;
import edu.uf.main.print.PrintStat;

public interface Run {
	
	public static final List<Integer> L = Arrays.asList(0,1,2,3);
	
	public void run(
			int iterations, 
			int xbin, 
			int ybin, 
			int zbin, 
			Recruiter[] recruiters, 
			boolean printLattice, 
			File outputFile,
			int nthreads,
			PrintStat printStat
	) throws InterruptedException;
}
