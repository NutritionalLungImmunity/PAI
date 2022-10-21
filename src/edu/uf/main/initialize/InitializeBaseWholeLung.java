package edu.uf.main.initialize;

import java.util.ArrayList;
import java.util.List;

import edu.uf.compartments.Voxel;
import edu.uf.interactable.Afumigatus;
import edu.uf.utils.LinAlg;
import edu.uf.utils.Rand;

public class InitializeBaseWholeLung extends InitializeBaseModel{

	public List<Afumigatus> infect(int numAspergillus, Voxel[][][] grid, int xbin, int ybin, int zbin, int status, double initIron, double sigma, boolean verbose) {
    	if(verbose) System.out.println("infecting ...");
    	
    	List<Afumigatus> list = new ArrayList<>();
    	double[][][] w = new double[xbin][ybin][zbin];
    	double sumW = 0;
    	
    	double sigma2 = sigma*sigma;
    	double d = 0;
    	int[] p1 = new int[3];
    	int[] p2 = new int[3];
    	
    	while(d < sigma) {
    		p1[0] = Rand.getRand().randunif(0, xbin);
    		p1[1] = Rand.getRand().randunif(0, ybin);
    		p1[2] = Rand.getRand().randunif(0, zbin);
    		
    		p2[0] = Rand.getRand().randunif(0, xbin);
    		p2[1] = Rand.getRand().randunif(0, ybin);
    		p2[2] = Rand.getRand().randunif(0, zbin);
    		
    		d = LinAlg.euclidianDistance(p1, p2);
    	}
    	
    	if(verbose)System.out.println("distance between intection sites: " + d);
    	int[] v = new int[3];
    	double d1 = 0;
    	double d2 = 0;
    	
    	for(int x = 0; x < xbin; x++) {
        	for(int y = 0; y < ybin; y++) {
        		for(int z = 0; z < zbin; z++) {
        			v[0] = x;
        			v[1] = y;
        			v[2] = z;
        			
        			d1 = LinAlg.euclidianDistance(v, p1);
        			d2 = LinAlg.euclidianDistance(v, p2);
        			
        			if(d1 < d2) {
        				if(d1 < sigma) {  //threshold = sigma
        					w[x][y][z] = (1/(sigma*Math.sqrt(2*Math.PI)))*Math.exp(-(0.5*d1*d1)/sigma2);
        					sumW += w[x][y][z];
        				}
        			}else {
        				if(d2 < sigma) {  //threshold = sigma
        					w[x][y][z] = (1/(sigma*Math.sqrt(2*Math.PI)))*Math.exp(-(0.5*d2*d2)/sigma2);
        					sumW += w[x][y][z];
        				}
        			}
        		}
        	}
    	}
    	if(verbose)System.out.println("sumW = " + sumW);
    	
    	int count = 0;
    	int totalInfection = 0;
    	Afumigatus a = null;
    	for(int x = 0; x < xbin; x++) {
        	for(int y = 0; y < ybin; y++) {
        		for(int z = 0; z < zbin; z++) {
        			count = Rand.getRand().randpois(numAspergillus*w[x][y][z]/sumW);
        			if(count > 0) {
        				for(int i = 0; i < count; i++) {
        					a = new Afumigatus(
        		            		x, y, z, x, y, z, random(), random(), random(), 
        		            		0,initIron,0, 0, true
        		            );
        					a.setStatus(status);
        					list.add(a);
        		            grid[x][y][z].setCell(a);
        		            totalInfection++;
        							
        				}
        			}
        		}
        	}
    	}
    	
    	if(verbose)System.out.println("Whole lung infected with " + totalInfection + " conidia!");
    	return list;
    }
	
}
