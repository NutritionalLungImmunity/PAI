package edu.uf.compartments;
import java.util.Map;

import java.util.*;

import edu.uf.interactable.Cell;
import edu.uf.interactable.InfectiousAgent;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Leukocyte;
import edu.uf.interactable.Molecule;
import edu.uf.utils.Rand;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Voxel {
    public static final int AIR = 0;
    public static final int EPITHELIUM = 1;
    public static final int REGULAR_TISSUE = 2;
    public static final int BLOOD = 3;
    
    
    public List<String> threadNames = new ArrayList<>();
    
    
    private int x;
    private int y;
    private int z;
	private double p;
    private int tissueType;
    private Map<Integer, Interactable> interactables;
    private Map<Integer, Cell> cells;
    private Map<Integer, InfectiousAgent> infectiousAgents;
    private static Map<String, Molecule> molecules;
    private static Map<String, Molecule> infectiousAgentMolecules;
    private static Map<String, Molecule> moleculeInteractable;
    private List<Voxel> neighbors;
    private int numSamples;
    private int externalState;
    
    static {
    	molecules = new HashMap<>();
    	infectiousAgentMolecules = new HashMap<>();
    	moleculeInteractable = new HashMap<>();
    }

    public Voxel(int x, int y, int z, int numSamples) { 
        this.x = x;
        this.y = y;
        this.z = z;
        this.p = 0.0;
        this.tissueType = Voxel.EPITHELIUM;
        this.interactables = new HashMap<>();
        //this.molecules = new HashMap<>();
        this.cells = new HashMap<>();
        this.infectiousAgents = new HashMap<>();
        //this.infectiousAgentMolecules = new HashMap<>();
        this.neighbors = new ArrayList<>();
        this.numSamples = numSamples;
        this.externalState  = 0;
    }
    
    public void setExternalState(int state) {
    	this.externalState = state;
    }
    
    /**
     * Returns the x-coordinate of the voxel.
     *
     * @return the x-coordinate of the voxel
     */
    public int getX() {
		return x;
	}

    /**
     * Sets the x-coordinate of the voxel.
     *
     * @param x the x-coordinate to set
     */
	public void setX(int x) {
		this.x = x; 
	}

	/**
	 * Returns the y-coordinate of the voxel.
	 *
	 * @return the y-coordinate of the voxel
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y-coordinate of the voxel.
	 *
	 * @param y the y-coordinate to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Returns the z-coordinate of the voxel.
	 *
	 * @return the z-coordinate of the voxel
	 */
	public int getZ() {
		return z;
	}

	/**
	 * Sets the z-coordinate of the voxel.
	 *
	 * @param z the z-coordinate to set
	 */
	public void setZ(int z) {
		this.z = z;
	}

	/**
	 * Returns the voxel's weight used for chemoattraction. 
	 * The weight is proportional to the chemokine concentration. 
	 * This method is used by {@code Util.calcDriftProbability} and {@code Util.getVoxel}.
	 *
	 * @return the weight of the voxel
	 */
	public double getP() {
		return p;
	}

	/**
	 * Sets the voxel's weight used for chemoattraction. 
	 * The weight is proportional to the chemokine concentration. 
	 * This method is used by {@code Util.calcDriftProbability} and {@code Util.getVoxel}.
	 *
	 * @param p the weight to assign to the voxel
	 */
	public void setP(double p) {
		this.p = p;
	}
	
	public Map<Integer, InfectiousAgent> getInfectiousAgents() {
		return this.infectiousAgents;
	}

	public int getTissueType() {
		return tissueType;
	}

	public void setTissueType(int tissue_type) {
		this.tissueType = tissue_type;
	}

	public Map<Integer, Interactable> getInteractables() {
		return interactables;
	}

	public Map<Integer, Cell> getCells() {
		return cells;
	}

	public synchronized static Map<String, Molecule> getMolecules() {
		return molecules;
	}

	public List<Voxel> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(List<Voxel> neighbors) {
		this.neighbors = neighbors;
	}
    
	/**
	 * Adds a cell to the appropriate collections within the voxel.
	 *
	 * @param cell the cell to add
	 */
    public void setCell(Cell cell) {
    	if(cell instanceof InfectiousAgent) 
    		this.infectiousAgents.put(cell.getId(), (InfectiousAgent) cell);
    	else {
    		this.cells.put(cell.getId(), cell);
    	}
    	this.interactables.put(cell.getId(), cell);
    }
    
    /**
     * Removes the cell with the given ID from all collections within the voxel and returns it.
     *
     * @param id the ID of the cell to remove
     * @return the removed cell, or {@code null} if not found
     */
    public Cell removeCell(int id) {
    	Interactable agent = this.interactables.remove(id);
    	if(agent != null && agent instanceof Cell) {
    		if(agent instanceof InfectiousAgent) {
    			this.infectiousAgents.remove(id);
    		}
    		else
    			this.cells.remove(id);
    		return (Cell) agent;
    	}
    	if(agent != null) this.interactables.put(agent.getId(), agent); // Restore if not a Cell
    	return null;
    }
    
    /**
     * Adds the given {@code molecule} to the {@code molecules} map with the specified name {@code molName}. 
     * The {@code molecules} map has the structure: {@code Map<String, Molecule>} 
     * where the key is the molecule name and the value is the molecule object.
     *
     * @param molName the name of the molecule
     * @param molecule the {@code Molecule} object to add
     */
    public static void setMolecule(String molName, Molecule molecule) {
    	setMolecule(molName, molecule, false, false);
    }
    
    /**
     * Adds the given {@code molecule} to the {@code molecules} map with the specified name {@code molName}. 
     * The {@code molecules} map has the structure {@code Map<String, Molecule>}, where the key is the molecule name 
     * and the value is the {@code Molecule} object.
     * 
     * If {@code infectiousAgentMolecule} is {@code true}, the molecule is also added to the {@code infectiousAgentMolecules} map.
     * If {@code moleculeInteractable} is {@code true}, the molecule is also added to the {@code moleculeInteractable} map.
     * 
     * The {@code infectiousAgentMolecules} and {@code moleculeInteractable} maps share the same structure as {@code molecules}. 
     * These maps are used to handle interactions between infectious agents (e.g., *Aspergillus*, *Klebsiella*) and molecules 
     * that interact with them (e.g., siderophores), as well as interactions between molecules themselves.
     * These separate collections are maintained for optimization purposes.
     *
     * @param molName the name of the molecule
     * @param molecule the {@code Molecule} object to add
     * @param infectiousAgentMolecule whether the molecule interacts with infectious agents
     * @param moleculeInteractable whether the molecule can interact with other molecules
     */
    public static void setMolecule(String molName, Molecule molecule, boolean infectiousAgentMolecule, boolean moleculeInteractable) {
        molecules.put(molName, molecule);
        //interactables.put(molecule.getId(), molecule);
        if(infectiousAgentMolecule)
        	infectiousAgentMolecules.put(molName, molecule);
        if(moleculeInteractable)
        	Voxel.moleculeInteractable.put(molName, molecule);
    }
    
    /**
     * Retrieves a {@code Molecule} object by its name.
     *
     * @param molName the name of the molecule
     * @return the corresponding {@code Molecule} object, or {@code null} if not found
     */
    public static Molecule getMolecule(String molName) {
    	return molecules.get(molName);
    }

    /**
     * Adds the given voxel to the list of neighbors of this voxel. 
     * This method is used by {@code GridFactory}.
     *
     * @param neighbor the neighboring voxel to add
     */
    public void setNeighbor(Voxel neighbor) {
        this.neighbors.add(neighbor);
    }

    /**
     * Iterates through the list of cells in the voxel at the given coordinates and performs updates:
     * <ul>
     *   <li>Calls {@code updateStatus()} and {@code move()} on all cells.</li>
     *   <li>If the cell is a leukocyte, also calls {@code kill()}.</li>
     *   <li>If the cell is an infectious agent (e.g., <i>Aspergillus</i>, <i>Klebsiella</i>), also calls {@code grow()}.</li>
     *   <li>Additionally, iterates through the list of infectious agents inside leukocytes,
     *       calling {@code grow()} and {@code updateStatus()} on them as well.</li>
     * </ul>
     *
     * @param x the x-coordinate of the voxel
     * @param y the y-coordinate of the voxel
     * @param z the z-coordinate of the voxel
     */
    public void next(int x, int y, int z) {
    	int xbin = GridFactory.getXbin();
    	int ybin = GridFactory.getYbin();
    	int zbin = GridFactory.getZbin();
        HashMap<Integer, Cell> listCell = (HashMap<Integer, Cell>) ((HashMap) this.cells).clone();
        Cell agent = null;
        for(Map.Entry<Integer, Cell> entry : listCell.entrySet()) {
        	agent = entry.getValue();
            this.update(agent, x, y, z);
            agent.move(this.getX(), this.getY(), this.getZ(), 0);
            if(agent instanceof Leukocyte && !((Leukocyte) agent).getPhagosome().isEmpty())
            	this.grow(x, y, z, xbin, ybin, zbin, (Leukocyte) agent);
        }
        HashMap<Integer, InfectiousAgent> listInf = (HashMap<Integer, InfectiousAgent>) ((HashMap) this.infectiousAgents).clone();
        InfectiousAgent infAgent = null;
        for(Map.Entry<Integer, InfectiousAgent> entry : listInf.entrySet()) {
        	infAgent = entry.getValue();
        	this.update(infAgent, x, y, z);
        	infAgent.move(this.getX(), this.getY(), this.getZ(), 0);
        	infAgent.grow(x, y, z, xbin, ybin, zbin);
        }
    }

    /**
     * Calls {@code updateStatus()} on the given cell. 
     * If the cell is a leukocyte, iterates through its phagosome contents, 
     * updates the status of each internalized infectious agent by calling {@code updateStatus()}, 
     * and attempts to kill the agent using the leukocyte's {@code kill()} method.
     *
     * @param agent the cell to update
     * @param x the x-coordinate of the voxel containing the cell
     * @param y the y-coordinate of the voxel containing the cell
     * @param z the z-coordinate of the voxel containing the cell
     */
    protected void update(Cell agent, int x, int y, int z) {
    	agent.updateStatus(x, y, z);
    	if(agent instanceof Leukocyte){//isinstance(agent, Phagocyte):
    		Leukocyte phag = (Leukocyte) agent;
    		this.updatePhagosome(phag.getPhagosome(), x, y, z);
    		phag.kill();
    	}
    }
    
    private void updatePhagosome(List<InfectiousAgent> agents, int x, int y, int z) {
        for(InfectiousAgent agent : agents) {
        	agent.updateStatus(x, y, z); //this.x, this.y, this.z 
        }
    }
    
    /**
     * Handles the growth of an infectious agent inside a leukocyte.
     *
     * @param x the x-coordinate of the voxel
     * @param y the y-coordinate of the voxel
     * @param z the z-coordinate of the voxel
     * @param xbin the number of bins along the x-axis in the voxel grid
     * @param ybin the number of bins along the y-axis in the voxel grid
     * @param zbin the number of bins along the z-axis in the voxel grid
     * @param agent the infectious agent contained within the leukocyte
     */
    protected void grow(int x, int y, int z, int xbin, int ybin, int zbin, Leukocyte agent) {
    	for (InfectiousAgent phAgent : agent.getPhagosome())
            phAgent.grow(x, y, z, xbin, ybin, zbin, agent);
    }
    
    /**
     * Calls {@code turnOver()} and {@code computeTotalMolecule()} on each molecule 
     * in the voxel's molecule list.
     */
    public void degrade() {
        for(Map.Entry<String, Molecule> entry : this.molecules.entrySet()) {
        	entry.getValue().turnOver(this.x, this.y, this.z);
        	entry.getValue().computeTotalMolecule(this.x, this.y, this.z);
        }
    }
    
    /**
     * Shuffles the lists of {@code Interactable} agents (e.g., cells and molecules), 
     * then performs a nested loop over them, calling the {@code interact()} method on each agent.
     */
    public void interact() {
    	
        List<Cell> cells = (List<Cell>) this.toList(this.cells);
        List<Molecule> mols = (List<Molecule>) this.toList(molecules);
        List<InfectiousAgent> infectiousAgents = (List<InfectiousAgent>) this.toList(this.infectiousAgents);
        List<Molecule> infectiousMolecules = (List<Molecule>) this.toList(this.infectiousAgentMolecules);
        List<Molecule> moleculeInteractable = (List<Molecule>) this.toList(this.moleculeInteractable);
        
        int size = cells.size();
        int[] cellsIndices = null;
        int[] infIndices = new int[infectiousAgents.size()];
        for(int i = 0; i < infectiousAgents.size(); i++)
        	infIndices[i] = i;
        //if(size > 2) {
        	cellsIndices = Rand.getRand().sample(cells.size(), numSamples == -1 ? cells.size() : numSamples);
        	if(numSamples != -1 ) infIndices = Rand.getRand().sample(infectiousMolecules.size(), numSamples);
        	//Collections.shuffle(cells, new Random());
        	Collections.shuffle(mols, new Random());
        	Collections.shuffle(infectiousMolecules, new Random());
       // }	
            
        int molSize = mols.size();
        int cellSize = cells.size();
        int infAgSize = infectiousAgents.size();
        int infMolSize = infectiousMolecules.size();
        int molMolSize = moleculeInteractable.size();
        
        List<Integer> l = new ArrayList<>();
        l.add(0);
        l.add(1); 
        l.add(2);
        l.add(3);
        l.add(4);
        Collections.shuffle(l, new Random());
        for(Integer k : l) {
            switch(k) {
            case 0:
                for(int i = 0; i < molMolSize; i++) 
                	//if(mols.get(i).getThreshold() != -1 && mols.get(i).values[0][this.x][this.y][this.z] > mols.get(i).getThreshold()) 
                		for(int j = 0 ; j < molMolSize; j++) {
                			moleculeInteractable.get(i).interact(moleculeInteractable.get(j), this.x, this.y, this.z);
                			//System.out.println(mols.get(i) + "\t" + mols.get(j) + "\t" + i + "\t" + j);
                		}
                break;
            case 1:
            	for(int i : cellsIndices) {
            		if(!cells.get(i).isTime())continue;
            		cells.get(i).setExternalState(externalState);
            		for(int j : cellsIndices) {
            			//if(!cells.get(j).isTime())continue;
            			cells.get(j).setExternalState(externalState);
                        cells.get(i).interact(cells.get(j), this.x, this.y, this.z);
            		}
            	}
            	break;
            case 2:
            	for(int i : cellsIndices) {
            		if(!cells.get(i).isTime())continue;
            		for(int j = 0; j < molSize; j++) {
            			//if(mols.get(j) instanceof Transferrin)System.out.println(cells.get(i) + " "  + mols.get(j));
            			//if(cells.get(i) instanceof Liver && mols.get(j) instanceof Transferrin)System.out.println("BLE");
            			cells.get(i).interact(mols.get(j), this.x, this.y, this.z);
            		}
            	}
            	break;
            case 3:
            	for(int i : cellsIndices) {
            		if(!cells.get(i).isTime())continue;
            		for(int j : infIndices) {
            			//if(!infectiousAgents.get(j).isTime())continue;
            			cells.get(i).interact(infectiousAgents.get(j), this.x, this.y, this.z);
            		}
            	}
            	break;
            case 4:
            	for(int i = 0; i < infMolSize; i++) 
            		for(int j : infIndices)
            			infectiousMolecules.get(i).interact(infectiousAgents.get(j), this.x, this.y, this.z);
            	break;
            default:
            	System.err.println("No such interaction: " + k + "!");
            	System.exit(1);
            }
            	
        }
    }
    
    /**
     * <strong>Not used</strong>
     */
    public void interact_legacy() {
    	
        List<Cell> cells = (List<Cell>) this.toList(this.cells);
        List<Molecule> mols = (List<Molecule>) this.toList(molecules);
        List<InfectiousAgent> infectiousAgents = (List<InfectiousAgent>) this.toList(this.infectiousAgents);
        List<Molecule> infectiousMolecules = (List<Molecule>) this.toList(this.infectiousAgentMolecules);
        
        int size = cells.size();
        int[] cellsIndices = null;
        int[] infIndices = new int[infectiousAgents.size()];
        for(int i = 0; i < infectiousAgents.size(); i++)
        	infIndices[i] = i;
        //if(size > 2) {
        	cellsIndices = Rand.getRand().sample(cells.size(), numSamples == -1 ? cells.size() : numSamples);
        	if(numSamples != -1 ) infIndices = Rand.getRand().sample(infectiousMolecules.size(), numSamples);
        	//Collections.shuffle(cells, new Random());
        	Collections.shuffle(mols, new Random());
        	Collections.shuffle(infectiousMolecules, new Random());
       // }	
            
        int molSize = mols.size();
        int cellSize = cells.size();
        int infAgSize = infectiousAgents.size();
        int infMolSize = infectiousMolecules.size();
        
        List<Integer> l = new ArrayList<>();
        l.add(0);
        l.add(1); 
        l.add(2);
        l.add(3);
        l.add(4);
        Collections.shuffle(l, new Random());
        for(Integer k : l) {
            switch(k) {
            case 0:
                for(int i = 0; i < molSize; i++) {
                	//if(mols.get(i).getThreshold() != -1 && mols.get(i).values[0][this.x][this.y][this.z] > mols.get(i).getThreshold()) 
                	if(infIndices.length <= 0)continue;
        			int j = Rand.getRand().randunif(0, molSize);
                		//for(int j = i; j < molSize; j++) {
                			mols.get(i).interact(mols.get(j), this.x, this.y, this.z);
                			//System.out.println(mols.get(i) + "\t" + mols.get(j) + "\t" + i + "\t" + j);
                		//}
                }
                break;
            case 1:
            	for(int i : cellsIndices) {
            		if(!cells.get(i).isTime())continue;
            		if(cellsIndices.length <= 0)continue;
            		int j = cellsIndices[Rand.getRand().randunif(0, cellsIndices.length)];
            		//for(int j : cellsIndices) {
            			//if(!cells.get(j).isTime())continue;
                        cells.get(i).interact(cells.get(j), this.x, this.y, this.z);
            		//}
            	}
            	break;
            case 2:
            	for(int i : cellsIndices) {
            		if(!cells.get(i).isTime())continue;
            		if(infIndices.length <= 0)continue;
        			int j = Rand.getRand().randunif(0, molSize);
            		//for(int j = 0; j < molSize; j++) 
            			cells.get(i).interact(mols.get(j), this.x, this.y, this.z);
            	}
            	break;
            case 3:
            	for(int i : cellsIndices) {
            		if(!cells.get(i).isTime())continue;
            		if(infIndices.length <= 0)continue;
            		int j = infIndices[Rand.getRand().randunif(0, infIndices.length)];
            		//for(int j : infIndices) {
            			//if(!infectiousAgents.get(j).isTime())continue;
            			cells.get(i).interact(infectiousAgents.get(j), this.x, this.y, this.z);
            		//}
            	}
            	break;
            case 4:
            	for(int i = 0; i < infMolSize; i++) {
            		if(infIndices.length <= 0)continue;
        			int j = infIndices[Rand.getRand().randunif(0, infIndices.length)];
            		//for(int j : infIndices)
            			infectiousMolecules.get(i).interact(infectiousAgents.get(j), this.x, this.y, this.z);
            	}
            	break;
            default:
            	System.err.println("No such interaction: " + k + "!");
            	System.exit(1);
            }
            	
        }
    }
    
    private List<?> toList(Map<?,?> map){
    	List<Object> list = new ArrayList<>(map.size());
    	for(Object o : map.values()) {
    		list.add(o);
    	}
    	return list;
    }
    
    
    public String toString() {
    	return x + " " + y + " " + z;
    }
    
/*

    def _get_voxel_(this, P):
        p=0
        vx = None
        for v in this.neighbors:
            if v.p > p:
                p = v.p
                vx = v
        return vx
        */
}
