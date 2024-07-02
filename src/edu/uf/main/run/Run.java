package edu.uf.main.run;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import edu.uf.compartments.Recruiter;
import edu.uf.main.print.PrintStat;

public interface Run {
	
	public static final List<Integer> L = Arrays.asList(0,1,2,3);
	
	/**
	 * Runs the simulation. The simulator must have been initialized. 
	 * The grid must have been created, and the cells, the molecules, 
	 * the recruiters, and the class-to-print statistics must have been 
	 * initialized. 
	 * @param iterations number of iterations to run the simulation.
	 * @param xbin 
	 * @param ybin
	 * @param zbin
	 * @param recruiters array of recruiters (e.g., MacrophageRecruiter, NeutrophilRecruiter, etc)
	 * @param printLattice (Boolean) if true prints the lattice.
	 * @param outputFile (Optional) can be null. If provided prints the statistics into a file. If not prints into the screen.
	 * @param nthreads Only for RunMultiThread, otherwise use -1. Number of threads to run the code.
	 * @param printStat class handling the statistics output format.
	 * @throws InterruptedException
	 */
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
