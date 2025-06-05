package edu.uf.main.run;

import java.io.File;
import java.util.Collections;

import edu.uf.compartments.Recruiter;
import edu.uf.control.Exec;
import edu.uf.control.MultiThreadExec;
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.main.print.PrintStat;

public class RunSingleThread implements Run{
	
	public static long eNextTime = 0;
	public static long recruitTime = 0;
	public static long diffusionTime = 0;

	@Override
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
	) throws InterruptedException {
    	
        for (int k = 0; k < iterations; k++) {
            if (k != 0)
                Collections.shuffle(L);
            if (Afumigatus.getTotalCells0() > 3e5)//1.1e7)
                return;
            for (int ii : L) {

                if (ii == 0) {
                	long start = System.currentTimeMillis();
                    Exec.next(xbin, ybin, zbin);
                    long end = System.currentTimeMillis();
                    eNextTime += (end - start);
                }else if (ii == 1) {
                	long start = System.currentTimeMillis();
                	Exec.recruit(recruiters);
                	long end = System.currentTimeMillis();
                	recruitTime += (end - start);
                }else if (ii == 2) {
                	long start = System.currentTimeMillis();
                	Exec.diffusion();
                	long end = System.currentTimeMillis();
                	diffusionTime += (end - start);
                }
            }
            
            /*if(k==360) {
            	for(int x = 0; x < xbin; x++) 
                	for(int y = 0; y < ybin; y++)
                		for(int z = 0; z < zbin; z++) {
                			//Hemopexin.getMolecule().values[0][x][y][z] = 1e5*Constants.DEFAULT_HPX_CONCENTRATION;
                			Heme.getMolecule().values[0][x][y][z] = 1*Constants.VOXEL_VOL;
                		}
            }*/

            printStat.printStatistics(k, outputFile);
            MultiThreadExec.resetCount();
        }
    }

}
