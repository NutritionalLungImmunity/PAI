package edu.uf.main.run;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.uf.compartments.Quadrant;
import edu.uf.compartments.Recruiter;
import edu.uf.compartments.Voxel;
import edu.uf.control.Exec;
import edu.uf.control.MultiThreadExec;
import edu.uf.interactable.Afumigatus;
import edu.uf.interactable.Cell;
import edu.uf.interactable.Heme;
import edu.uf.interactable.Hemopexin;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Pneumocyte;
import edu.uf.main.print.PrintStat;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;

public class RunSingleThread implements Run{

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
    	
        for (int k = 0; k < iterations; k++) {
            if (k != 0)
                Collections.shuffle(L);
            if (Afumigatus.getTotalCells() > 3e6)//1.1e7)
                return;
            for (int ii : L) {

                if (ii == 0) 
                    Exec.next(grid, xbin, ybin, zbin);
                else if (ii == 1)
                	Exec.recruit(recruiters, grid, quadrants);
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
