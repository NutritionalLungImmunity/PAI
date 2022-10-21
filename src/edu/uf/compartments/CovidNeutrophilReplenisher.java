package edu.uf.compartments;

import edu.uf.interactable.Cell;
import edu.uf.interactable.covid.Neutrophil;
import edu.uf.utils.Constants;

public class CovidNeutrophilReplenisher extends Recruiter{
	public Cell createCell() {
        Neutrophil n = new Neutrophil();//new Neutrophil(0); 
        return n;
    }
    
    public int getQtty() {
    	return getQtty(0);
    }

    public int getQtty(Quadrant q) { 
    	return getQtty(0);
    }
    
    protected int getQtty(double chemokine) {
    	double avg =  Constants.MAX_N - Neutrophil.getTotalCells();
        if (avg > 0)
            return (int) avg;
        else
            return 0;
    }

    /*public double chemoatract(Quadrant quadrant) {
    	int x = (int) (quadrant.xMax - quadrant.xMin) / 2;
    	int y = (int) (quadrant.yMax - quadrant.yMin) / 2;
    	int z = (int) (quadrant.zMax - quadrant.zMin) / 2;
        return  Util.activationFunction(
        		quadrant.chemokines.get(Neutrophil.getChemokine()).get(x, y, z), 
        		Constants.Kd_MIP2, 
        		Constants.STD_UNIT_T,
        		Constants.VOXEL_VOL,
        		Constants.REC_BIAS 
        );
    }*/
    

    public boolean leave() {
        return false;
    }

    public Cell getCell(Voxel voxel) {
        return null;
    }
}
