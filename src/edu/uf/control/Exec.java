package edu.uf.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uf.compartments.GridFactory;
import edu.uf.compartments.Recruiter;
import edu.uf.compartments.Voxel;
import edu.uf.interactable.Molecule;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.Setter;
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.intracellularState.IntracellularModel;
import edu.uf.intracellularState.NeutrophilStateModel;
import edu.uf.interactable.Cell;
import edu.uf.interactable.InfectiousAgent;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Internalizable;
import edu.uf.interactable.Leukocyte;
import edu.uf.interactable.Liver;


public class Exec {
	
	//protected static List<Molecule> diffusableMolecules;
	protected static List<Molecule> molecules; 

	
	static {
		//diffusableMolecules = new ArrayList<>(); 
		molecules = new ArrayList<>(); 
	}
	
	/**
	 * This method runs a simulation iteration, except for molecule diffusion, cell recruitment, 
	 * and molecule reset count. This method runs everything at the "Voxel" level. That is, it 
	 * iterates through the 3D array of voxels and calls the "Voxel" methods "interact," "next," 
	 * "degrade," and the "Exec" method "gc." The "Exec.gc" method receives a "Voxel" as an argument. 
	 * Therefore, it is also at the voxel level (I.e., it operates at the voxel. Diffusion, recruitment, 
	 * and molecule counts are at the grid level. Those are operations that take the whole 3D grid.
	 * This method is called by "Run.run."
	 * @param xbin size of the grid on the x-axis
	 * @param ybin size of the grid on the x-axis
	 * @param zbin size of the grid on the x-axis
	 */
    public static void next(int xbin, int ybin, int zbin) {
    	Voxel[][][] grid = GridFactory.getGrid();
    	for(int x = 0; x < xbin; x++)
    		for(int y = 0; y < ybin; y++)
    			for(int z = 0; z < zbin; z++) {
                    grid[x][y][z].interact();  
                    Exec.gc(grid[x][y][z]);
                    grid[x][y][z].next(x, y, z);
                    grid[x][y][z].degrade();
    			}
    }
    
    /**
     * This method iterates through the recruiters that were initialized and 
     * recruits cells. For example, if we initialize "MacrophageRecruiter" 
     * and "NeutrophilRecruiter," this method will recruit macrophages and 
     * neutrophils according to the algorithms of these two recruiters. 
     * If no recruiters were initialized, this method will do nothing.
     * This method is called by "Run.run."
     * @param recruiters
     */
    public static void recruit(Recruiter[] recruiters) {
    	Voxel[][][] grid = GridFactory.getGrid();
    	for (Recruiter recruiter : recruiters) 
            recruiter.recruit();
    }
    
    /**
     * This method will iterate through the list of molecules and call the "diffusion.solve" method.
     * This method is called by "Run.run."
     */
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
    
    /**
     * Add a molecule to the list. This list is used by "Exec.diffusion" and "Exec.resetCount."
     * @param mol
     */
    public static void setMolecule(Molecule mol) {
    	molecules.add(mol); 
    }

    /**
     * This method will iterate through the list of molecules and reset their counts for 
     * the purpose of statistics. It will also do the same ad-hoc for Liver. If the molecule 
     * is a Setter, it will run "update." This method is called by "Run.run."
     */
    public static void resetCount() {
    	Liver.getLiver().reset();
        for (Molecule mol : molecules) {
        	mol.resetCount();
        	if(mol instanceof Setter) ((Setter) mol).update();
        }
    }

    /**
     * This method iterates through the list of cells in a "Voxel" and does two things. 
     * (1) if the cell life status is "DEAD" and the cell method "removeUponDeath" returns 
     * true, this method removes this cell from all the simulator lists. (2) If the cell 
     * has an "isInternalizing" method and it returns true, this method removes the cell 
     * from the "Voxel" list. However, the cell will still be in the phagocytosed cell list. 
     * This method is called by "Exec.next."
     * <br/><br/>
     * <strong>Note: If the cell is removed from all lists, it no longer has any valid reference, 
     * and the Java garbage collection system will remove that object automatically. That is not 
     * true for C++ implementation.</strong>
     * @param voxel
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
        			Cell.rm(v.getId());
        		}else if (v instanceof Internalizable) 
        			if (((Internalizable)v).isInternalizing())
        				voxel.removeCell(entry.getKey());
        	}
        }
    }
    
    private static void releasePhagosome(List<InfectiousAgent> phagosome, Voxel voxel) {
        for(InfectiousAgent entry : phagosome)
        	if (entry.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) == IntracellularModel.DEAD)
        		voxel.setCell(entry);
    }
}
