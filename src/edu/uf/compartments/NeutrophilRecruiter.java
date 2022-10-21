package edu.uf.compartments;

import edu.uf.interactable.Cell;
import edu.uf.interactable.Neutrophil;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.Macrophage;

public class NeutrophilRecruiter extends Recruiter{
	
    public Cell createCell() {
        return new Neutrophil(0); 
    }
    
    public int getQtty() {
    	return getQtty(MIP2.getMolecule().getTotalMolecule(0));
    }

    public int getQtty(Quadrant q) { 
    	return getQtty(q.chemokines.get(MIP2.getMolecule().getName()));
    }
    
    protected int getQtty(double chemokine) {
        double avg = Constants.RECRUITMENT_RATE * Constants.SPACE_VOL * chemokine *
              (1 - (Neutrophil.getTotalCells()) / Constants.MAX_N) / Constants.Kd_MIP2;
        if (avg > 0)
            return Rand.getRand().randpois(avg); 
        else {
            if (Neutrophil.getTotalCells() < Constants.MIN_MA) {
                return Rand.getRand().randpois(1); 
            }
            return 0;
        }
        
        /*if (avg > 0)
            return Rand.getRand().randpois(avg);
        else
            return 0;*/
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
