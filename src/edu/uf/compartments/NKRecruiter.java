package edu.uf.compartments;

import java.util.Map;

import edu.uf.interactable.Cell;
import edu.uf.interactable.MIP1B;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.covid.NK;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;

public class NKRecruiter extends Recruiter{

    public Cell createCell() { 
        return new NK();
    }
    
    public int getQtty() {
    	return getQtty(MIP2.getMolecule().getTotalMolecule(0));
    }
    
    public int getQtty(Quadrant q) { 
    	return getQtty(q.chemokines.get(MIP2.getMolecule().getName()));
    }

    protected int getQtty(double chemokine) {
    	//System.out.println(MIP1B.getMolecule().getTotalMolecule(0));
        double avg = Constants.SPACE_VOL * Constants.RECRUITMENT_RATE * chemokine *
              (1 - (NK.getTotalCells()) / Constants.MAX_NK) / Constants.Kd_MIP2;
        if (avg > 0)
            return Rand.getRand().randpois(avg); 
        else {
            if (NK.getTotalCells() < Constants.MIN_NK) 
                return Rand.getRand().randpois(1); 
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
        if (voxel.getCells().size() == 0)
            return null;
        for(Map.Entry<Integer, Cell> entry : voxel.getCells().entrySet())
        	if (entry.getValue() instanceof NK)
        		return entry.getValue();
        return null;
    }

}
