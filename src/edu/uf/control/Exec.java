package edu.uf.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uf.compartments.GridFactory;
import edu.uf.compartments.Recruiter;
import edu.uf.compartments.Voxel;
import edu.uf.interactable.Molecule;
import edu.uf.interactable.Setter;
import edu.uf.intracellularState.IntracellularModel;
import edu.uf.interactable.Cell;
import edu.uf.interactable.InfectiousAgent;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Internalizable;
import edu.uf.interactable.Leukocyte;
import edu.uf.interactable.Liver;


public class Exec {
	
	
	public static long interactTime = 0;
	public static long gcTime = 0;
	public static long nextTime = 0;
	public static long degradeTime = 0;
	
	//protected static List<Molecule> diffusableMolecules;
	protected static List<Molecule> molecules; 

	
	static {
		//diffusableMolecules = new ArrayList<>(); 
		molecules = new ArrayList<>(); 
	}
	
	/**
	 * Runs a simulation iteration at the voxel level, excluding molecule diffusion, 
	 * cell recruitment, and molecule count reset—these are handled at the grid level.
	 * 
	 * This method iterates through the 3D array of voxels and, for each voxel, calls the 
	 * following methods:
	 * <ul>
	 *   <li>{@code Voxel.interact()}</li>
	 *   <li>{@code Voxel.next()}</li>
	 *   <li>{@code Voxel.degrade()}</li>
	 *   <li>{@code Exec.gc(Voxel)}</li>
	 * </ul>
	 * 
	 * The {@code Exec.gc()} method operates on a per-voxel basis and is thus considered 
	 * voxel-level as well. In contrast, diffusion, recruitment, and molecule count resets 
	 * operate over the entire 3D grid and are considered grid-level operations.
	 * 
	 * This method is called by {@code Run.run()}.
	 *
	 * @param xbin the size of the grid along the x-axis
	 * @param ybin the size of the grid along the y-axis
	 * @param zbin the size of the grid along the z-axis
	 */
    public static void next(int xbin, int ybin, int zbin) {
    	Voxel[][][] grid = GridFactory.getGrid();
    	//long start = 0;
    	//long end = 0;
    	for(int x = 0; x < xbin; x++)
    		for(int y = 0; y < ybin; y++)
    			for(int z = 0; z < zbin; z++) {
    				long start = System.currentTimeMillis();
                    grid[x][y][z].interact();  
                    long end = System.currentTimeMillis();
                    interactTime += (end - start);
                    start = System.currentTimeMillis();
                    Exec.gc(grid[x][y][z]);
                    end = System.currentTimeMillis();
                    gcTime += (end - start);
                    start = System.currentTimeMillis();
                    grid[x][y][z].next(x, y, z);
                    end = System.currentTimeMillis();
                    nextTime += (end - start);
                    start = System.currentTimeMillis();
                    grid[x][y][z].degrade();
                    end = System.currentTimeMillis();
                    degradeTime += (end - start);
    			}
    }
    
    /**
     * Iterates through the initialized recruiters and recruits cells accordingly. 
     * For example, if {@code MacrophageRecruiter} and {@code NeutrophilRecruiter} 
     * have been initialized, this method will recruit macrophages and neutrophils 
     * based on the logic defined in each recruiter.
     * 
     * If no recruiters have been initialized, this method does nothing.
     * 
     * This method is called by {@code Run.run()}.
     *
     * @param recruiters the list of initialized cell recruiters
     */
    public static void recruit(Recruiter[] recruiters) {
    	for (Recruiter recruiter : recruiters) 
            recruiter.recruit();
    }
    
    /**
     * Iterates through the list of molecules and calls the {@code diffusion.solve()} method 
     * on each one to simulate diffusion.
     * 
     * This method is called by {@code Run.run()}.
     */
    public static void diffusion() {
    	for(Molecule mol : Exec.molecules)  
    		mol.diffuse();    	
    }
    
    /**
     * Adds a molecule to the list of tracked molecules. 
     * This list is used by {@code Exec.diffusion} and {@code Exec.resetCount}.
     *
     * @param mol the {@code Molecule} to add
     */
    public static void setMolecule(Molecule mol) {
    	molecules.add(mol); 
    }

    /**
     * Iterates through the list of molecules and resets their counts for statistical purposes. 
     * Additionally, performs an ad hoc reset for the liver. 
     * 
     * If a molecule is an instance of {@code Setter}, the method {@code update()} is also called on it.
     * 
     * This method is called by {@code Run.run()}.
     */
    public static void resetCount() {
    	Liver.getLiver().reset();
        for (Molecule mol : molecules) {
        	mol.resetCount();
        	if(mol instanceof Setter) ((Setter) mol).update();
        }
    }

    /**
     * Iterates through the list of cells in a given {@code Voxel} and performs the following actions:
     * <ol>
     *   <li>If a cell's life status is {@code DEAD} and its {@code removeUponDeath()} method returns {@code true}, 
     *       the cell is removed from all simulator-managed lists.</li>
     *   <li>If a cell has an {@code isInternalizing()} method that returns {@code true}, the cell is removed 
     *       from the {@code Voxel}'s list, but remains in the phagocytosed cell list.</li>
     * </ol>
     * 
     * This method is called by {@code Exec.next()}.
     * <br/><br/>
     * <strong>Note:</strong> If a cell is removed from all lists, it no longer has any valid reference 
     * and will be automatically removed by Java's garbage collection system. This is not the case in 
     * the C++ implementation, where manual memory management is required.
     *
     * @param voxel the {@code Voxel} whose cell list is being processed
     */
    protected static void gc(Voxel voxel) {
        Map<Integer, Interactable> tmpAgents = (Map<Integer, Interactable>) ((HashMap)voxel.getInteractables()).clone();
        for(Map.Entry<Integer, Interactable> entry : tmpAgents.entrySet()) {
        	if(entry.getValue() instanceof Cell) {
        		Cell v = (Cell) entry.getValue();
        		//if isinstance(v, Cell):
        		if(v instanceof Liver)continue;//Dirty hack!
        		if (v.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) == IntracellularModel.DEAD && v.removeUponDeath()) {
        			//if(v instanceof Neutrophil)System.out.println(v.hasPhenotype(NeutrophilStateModel.NETOTIC));
        			if (v instanceof Leukocyte) {
        				List<InfectiousAgent> phagosome = ((Leukocyte)v).getPhagosome();
        				Exec.releasePhagosome(phagosome, voxel);
        			}
        			voxel.removeCell(entry.getKey());
        			Cell.remove(v.getId());
        		}else if(v instanceof Leukocyte) {
        			List<InfectiousAgent> phagosome = ((Leukocyte)v).getPhagosome();
        			List<InfectiousAgent> aux = (List<InfectiousAgent>) ((ArrayList<InfectiousAgent>)phagosome).clone();
        			for(InfectiousAgent c : aux) 
        				if (c.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) == IntracellularModel.DEAD) {
        					phagosome.remove(c);
        					Cell.remove(c.getId());
        				}
        			
        		}else if (v instanceof Internalizable) 
        			if (((Internalizable)v).isInternalizing())
        				voxel.removeCell(entry.getKey());
        	}
        }
    }
    
    private static void releasePhagosome(List<InfectiousAgent> phagosome, Voxel voxel) {
    	
    	List<InfectiousAgent> aux = (List<InfectiousAgent>) ((ArrayList<InfectiousAgent>)phagosome).clone();
        for(InfectiousAgent entry : aux)
        	if (entry.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) == IntracellularModel.DEAD) {
        		phagosome.remove(entry);
        		Cell.remove(entry.getId());
        	}else {
        		voxel.setCell(entry);
        	}
    }
}
