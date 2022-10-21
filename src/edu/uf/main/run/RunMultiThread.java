package edu.uf.main.run;

import java.io.File;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import edu.uf.compartments.Quadrant;
import edu.uf.compartments.Recruiter;
import edu.uf.compartments.Voxel;
import edu.uf.control.MultiThreadExec;
import edu.uf.control.MultiThreadExecDiffusion;
import edu.uf.main.print.PrintStat;

public class RunMultiThread implements Run{

	@Override
	public void run(
			int iterations, 
			int xbin, 
			int ybin, 
			int zbin, 
			Voxel[][][] grid, 
			List<Quadrant> quadrants,
			Recruiter[] recruiters, 
			boolean printLattice, 
			File outputFile, 
			int nthreads,
			PrintStat printStat
	) throws InterruptedException {
    	
    	
    	MultiThreadExec.setGrid(grid);
    	Thread[] threads = new Thread[nthreads];
    	
    	PrintWriter pw = null;
    	
        for (int k = 0; k < iterations; k++) {
        	Collections.shuffle(L);
        	
        	for(int ii : L) {
        		if(ii == 0) {
        			for(int t = 0; t < nthreads; t++) 
        				threads[t] = new Thread(new MultiThreadExec("Thread-" + t));
        			for(int t = 0; t < nthreads; t++) 
        				threads[t].start();
        			for(int t = 0; t < nthreads; t++) 
        				threads[t].join();
        		}
        		if(ii == 1) { 
        			MultiThreadExec.singleThreadNext();
        		}
        		if(ii == 2) { 
        			for(int t = 0; t < nthreads; t++) 
        				threads[t] = new Thread(new MultiThreadExecDiffusion("diffusion-Thread-" + t));
        			for(int t = 0; t < nthreads; t++) 
        				threads[t].start();
        			for(int t = 0; t < nthreads; t++) 
        				threads[t].join();
        		}
        		if(ii==3) 
        			MultiThreadExec.recruit(recruiters, grid, quadrants);
        	}

        	printStat.printStatistics(k, outputFile);
            MultiThreadExec.resetCount();
            MultiThreadExec.reset();
            MultiThreadExecDiffusion.reset();
            
        }
        
        
        
        
        
    }
	
}
