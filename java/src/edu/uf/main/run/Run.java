package edu.uf.main.run;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import edu.uf.compartments.Recruiter;
import edu.uf.main.print.PrintStat;

public interface Run {
	
	public static final List<Integer> L = Arrays.asList(0,1,2,3);
	
	/**
	 * Runs the simulation for a specified number of iterations.
	 *
	 * <p>Before calling this method, the simulator must be properly initialized, including the creation 
	 * of the grid, cells, molecules, recruiters, and the statistics output handler.</p>
	 *
	 * @param iterations   the number of iterations to run the simulation
	 * @param xbin         x-axis length
	 * @param ybin         y-axis length
	 * @param zbin         z-axis length
	 * @param recruiters   an array of recruiter objects (e.g., {@code MacrophageRecruiter}, {@code NeutrophilRecruiter}, etc.)
	 * @param printLattice whether to print the lattice at each iteration
	 * @param outputFile   optional file for outputting statistics; if {@code null}, statistics will be printed to the screen
	 * @param nthreads     number of threads to use (only applicable in {@code RunMultiThread}; use {@code -1} otherwise)
	 * @param printStat    the class responsible for formatting and outputting statistics
	 * @throws InterruptedException if the simulation is interrupted during execution
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
