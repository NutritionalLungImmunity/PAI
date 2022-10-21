package edu.uf.compartments;

import java.util.HashMap;
import java.util.Map;

import edu.uf.interactable.Chemokine;
import edu.uf.interactable.Molecule;

public class Quadrant {
	
	//public Map<Object, Chemokine> chemokines;
	public Map<Object, Double> chemokines;
	public int xMin;
	public int yMin;
	public int zMin; 
	public int xMax;
	public int yMax;
	public int zMax;
	
	
	public Quadrant() {
        this.chemokines = new HashMap<>();
        this.xMin = Integer.MAX_VALUE;
        this.yMin = Integer.MAX_VALUE;
        this.zMin = Integer.MAX_VALUE;
        this.xMax = 0;
        this.yMax = 0;
        this.zMax = 0;
	}

    /*public void updateChemokines(Map<String, Molecule> molecules, int x, int y, int z) {
        for(Map.Entry<String, Molecule> entry : molecules.entrySet()) {
            if (entry.getValue() instanceof Chemokine) {
            	Chemokine chemokine = (Chemokine) entry.getValue();
            	if (!this.chemokines.containsKey(chemokine.getName()) || 
            			chemokine.values[0][x][y][z] > this.chemokines.get(chemokine.getName()).values[0][x][y][z])
                    this.chemokines.put(chemokine.getName(), chemokine);
            }
        }
    }*/
    
    public void updateChemokines(Map<String, Molecule> molecules, int x, int y, int z) {
        for(Map.Entry<String, Molecule> entry : molecules.entrySet()) {
            if (entry.getValue() instanceof Chemokine) {
            	Chemokine chemokine = (Chemokine) entry.getValue();
            	if(!this.chemokines.containsKey(chemokine.getName())){
            		this.chemokines.put(chemokine.getName(), chemokine.get(0, x, y, z));
            	}else {
            		Double value = this.chemokines.get(chemokine.getName());
            		value += chemokine.get(0, x, y, z);
            		this.chemokines.put(chemokine.getName(), value);
            	}
            }
        }
    } 
    
    public void reset() {
    	for(Map.Entry<Object, Double> entry : chemokines.entrySet()) {
    		entry.setValue(0.0);
    	}
    }

    public void setCoordinates(int x, int y, int z) {
        if(x < this.xMin)
            this.xMin = x;
        if(x > this.xMax)
            this.xMax = x;
        if(y < this.yMin)
            this.yMin = y;
        if(y > this.yMax)
            this.yMax = y;
        if(z < this.zMin)
            this.zMin = z;
        if(z > this.zMax)
            this.zMax = z;
    }
}
