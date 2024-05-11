package edu.uf.main.run;

import java.io.File;
import java.util.Collections;
import java.util.List;

import edu.uf.compartments.Recruiter;
import edu.uf.control.Exec;
import edu.uf.control.MultiThreadExec;
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.main.print.PrintStat;

public class RunSingleThread implements Run{

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

                if (ii == 0) 
                    Exec.next(xbin, ybin, zbin);
                else if (ii == 1)
                	Exec.recruit(recruiters);
                else if (ii == 2) 
                	Exec.diffusion();
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
