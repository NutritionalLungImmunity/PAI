package edu.uf.compartments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uf.control.Exec;
import edu.uf.interactable.Afumigatus;
import edu.uf.interactable.Cell;
import edu.uf.interactable.InfectiousAgent;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Liver;
import edu.uf.interactable.Molecule;
import edu.uf.interactable.Phagocyte;
import edu.uf.time.Clock;

public class Lung implements Compartment{

	private static Lung lung = null;
	
	protected List<Molecule> molecules = new ArrayList<>();
	private Voxel[][][] grid;
	private int xbin;
	private int ybin;
	private int zbin;
	
	private Lung(Voxel[][][] grid, int xbin, int ybin, int zbin) {
		this.grid = grid;
		this.xbin = xbin;
		this.ybin = ybin;
		this.zbin = zbin;
	}
	
	
	
	public static Lung getLung(Voxel[][][] grid, int xbin, int ybin, int zbin) {
		if(lung == null) {
			lung = new Lung(grid, xbin, ybin, zbin);
		}
		return lung;
	}
	
	public static Lung getLung() {
		return lung;
	}
	
	@Override
	public void next() {
    	for(int x = 0; x < xbin; x++)
    		for(int y = 0; y < ybin; y++)
    			for(int z = 0; z < zbin; z++) {
                    grid[x][y][z].interact();  
                    Exec.gc(grid[x][y][z]);
                    grid[x][y][z].next(xbin, ybin, zbin, grid);
                    if(grid[x][y][z].getQuadrant() != null)
                    	grid[x][y][z].getQuadrant().updateChemokines(grid[x][y][z].getMolecules(), x, y, z);
                    grid[x][y][z].degrade();
    			}
    }
    
    public void recruit(Recruiter[] recruiters, Voxel[][][] grid, List<Quadrant> quadrants) {
    	for (Recruiter recruiter : recruiters) 
            recruiter.recruit(grid, quadrants);
    	for(Quadrant q : quadrants)
    		q.reset();
    }
    
    
    public void diffusion() {
    	for(Molecule mol : this.molecules)  
    		mol.diffuse();    	
    }
    
    public void setMolecule(Molecule mol) {
    	molecules.add(mol); 
    }

    public void resetCount() {
    	Liver.getLiver().reset();
        for (Molecule mol : molecules)
        	mol.resetCount();
    }

    public void gc(Voxel voxel) {
        Map<Integer, Interactable> tmpAgents = (Map<Integer, Interactable>) ((HashMap)voxel.getInteractables()).clone();
        for(Map.Entry<Integer, Interactable> entry : tmpAgents.entrySet()) {
        	if(entry.getValue() instanceof Cell) {
        		Cell v = (Cell) entry.getValue();
        		//if isinstance(v, Cell):
        		if (v.getStatus() == Cell.DEAD) {
        			if (v instanceof Phagocyte) {
        				List<InfectiousAgent> phagosome = ((Phagocyte)v).getPhagosome();
        				this.releasePhagosome(phagosome, voxel);
        			}
        			voxel.removeCell(entry.getKey());
        		}else if (v instanceof Afumigatus) 
        			if (((Afumigatus)v).isInternalizing())
        				voxel.removeCell(entry.getKey());
        	}
        }
    }

    private void releasePhagosome(List<InfectiousAgent> phagosome, Voxel voxel) {
        for(InfectiousAgent entry : phagosome)
        	if (entry.getStatus() != Afumigatus.DEAD)
        		voxel.setCell(entry);
    }
}
