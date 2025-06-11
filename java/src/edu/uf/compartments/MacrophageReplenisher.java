package edu.uf.compartments;

import java.util.Map;

import edu.uf.interactable.Cell;
import edu.uf.interactable.Macrophage;
import edu.uf.intracellularState.IntracellularModelFactory;
import edu.uf.utils.Constants;

public class MacrophageReplenisher extends Recruiter {
	
	public Cell createCell() { 
        return new Macrophage(Constants.MA_INTERNAL_IRON, IntracellularModelFactory.createBooleanNetwork(IntracellularModelFactory.ASPERGILLUS_MACROPHAGE));//new Macrophage(Constants.MA_INTERNAL_IRON);
    }
    
    public int getQtty() {
    	return getQtty(0);
    }

    protected int getQtty(double chemokine) {
        double avg = Constants.MAX_MA - Macrophage.getTotalCells();
        if (avg > 0)
            return (int) avg; 
        else {
            return 0;
        }
    }

    /*public double chemoatract(Quadrant quadrant) {
    	int x = (int) (quadrant.xMax - quadrant.xMin) / 2;
    	int y = (int) (quadrant.yMax - quadrant.yMin) / 2;
    	int z = (int) (quadrant.zMax - quadrant.zMin) / 2;
        return Util.activationFunction(
        		quadrant.chemokines.get(Macrophage.getChemokine()).get(x, y, z), 
        		Constants.Kd_MIP1B, 
        		Constants.STD_UNIT_T, 
        		Constants.VOXEL_VOL,
        		Constants.REC_BIAS
        );
    }*/

    public boolean leave() {
        return true; 
    }

    public Cell getCell(Voxel voxel) {
        //if (voxel.getCells().size() == 0)
        //    return null;
        for(Map.Entry<Integer, Cell> entry : voxel.getCells().entrySet())
        	if (entry.getValue() instanceof Macrophage)
        		return entry.getValue();
        return null;
    }
}
