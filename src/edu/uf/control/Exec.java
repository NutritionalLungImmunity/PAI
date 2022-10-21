package edu.uf.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.Quadrant;
import edu.uf.compartments.Recruiter;
import edu.uf.compartments.Voxel;
import edu.uf.interactable.Molecule;
import edu.uf.interactable.Phagocyte;
import edu.uf.time.Clock;
import edu.uf.interactable.Afumigatus;
import edu.uf.interactable.Cell;
import edu.uf.interactable.InfectiousAgent;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Liver;


public class Exec {
	
	//protected static List<Molecule> diffusableMolecules;
	protected static List<Molecule> molecules; 

	
	static {
		//diffusableMolecules = new ArrayList<>(); 
		molecules = new ArrayList<>(); 
	}
	
    public static void next(Voxel[][][] grid, int xbin, int ybin, int zbin) {
    	Clock.updade();
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
    
    public static void recruit(Recruiter[] recruiters, Voxel[][][] grid, List<Quadrant> quadrants) {
    	for (Recruiter recruiter : recruiters) 
            recruiter.recruit(grid, quadrants);
    	for(Quadrant q : quadrants)
    		q.reset();
    }
    
    
    public static void diffusion() {
    	for(Molecule mol : Exec.molecules)  
    		mol.diffuse();    	
    }

    /*public static void diffusion(Diffuse[] diffusion, Voxel[][][] grid) {
        for(Map.Entry<String, Molecule> entry : grid[0][0][0].molecules.entrySet()) {
            Molecule molecules = entry.getValue();
        	for(int i = 0; i < molecules.getNumState(); i++) {
                if (!molecules.getName().contentEquals(Iron.NAME) && 
                		!molecules.getName().contentEquals(Hepcidin.NAME) && 
                		!molecules.getName().contentEquals(Transferrin.NAME))
                    diffusion[0].solver(grid, molecules.getName(), i);
                else if (molecules.getName().contentEquals(Transferrin.NAME))
                    diffusion[1].solver(grid, molecules.getName(), i);
        	}
        }
    }*/
    
    public static void setMolecule(Molecule mol) {
    	molecules.add(mol); 
    }

    public static void resetCount() {
    	Liver.getLiver().reset();
        for (Molecule mol : molecules)
        	mol.resetCount();
    }

    public static void gc(Voxel voxel) {
        Map<Integer, Interactable> tmpAgents = (Map<Integer, Interactable>) ((HashMap)voxel.getInteractables()).clone();
        for(Map.Entry<Integer, Interactable> entry : tmpAgents.entrySet()) {
        	if(entry.getValue() instanceof Cell) {
        		Cell v = (Cell) entry.getValue();
        		//if isinstance(v, Cell):
        		if (v.getStatus() == Cell.DEAD) {
        			if (v instanceof Phagocyte) {
        				List<InfectiousAgent> phagosome = ((Phagocyte)v).getPhagosome();
        				Exec.releasePhagosome(phagosome, voxel);
        			}
        			voxel.removeCell(entry.getKey());
        		}else if (v instanceof Afumigatus) 
        			if (((Afumigatus)v).isInternalizing())
        				voxel.removeCell(entry.getKey());
        	}
        }
    }

    private static void releasePhagosome(List<InfectiousAgent> phagosome, Voxel voxel) {
        for(InfectiousAgent entry : phagosome)
        	if (entry.getStatus() != Afumigatus.DEAD)
        		voxel.setCell(entry);
    }
}
