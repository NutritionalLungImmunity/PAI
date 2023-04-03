package edu.uf.compartments;
import java.util.Map;

import java.util.*;

import edu.uf.interactable.Cell;
import edu.uf.interactable.InfectiousAgent;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Molecule;
import edu.uf.interactable.Phagocyte;
import edu.uf.interactable.covid.NK;
import edu.uf.interactable.covid.Neutrophil;
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
    private List<Voxel> neighbors;
    private Quadrant quadrant;
    private int numSamples;
    
    static {
    	molecules = new HashMap<>();
    	infectiousAgentMolecules = new HashMap<>();
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
        this.quadrant = null;
        this.numSamples = numSamples;
    }
    
    public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x; 
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public double getP() {
		return p;
	}

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

	public Quadrant getQuadrant() {
		return quadrant;
	}


    public void setQuadrant(Quadrant quadrant) {
        quadrant.setCoordinates(this.x, this.y, this.z);
        this.quadrant = quadrant;
    }

    //def set_type(this, type):
    //    this.type = type 

    /*public void setAgent(Interactable interactable) {
        if(interactable instanceof  Molecule) //isinstance(interactable, Molecule):
            this.molecules.put(interactable.getName(), (Molecule) interactable);
        else if(interactable instanceof Cell && !(interactable instanceof Liver)){// isinstance(interactable, Cell):
            this.cells.put(interactable.getId(), (Cell) interactable);
            if(interactable instanceof Afumigatus)//type(interactable) is Afumigatus:
                this.aspergillus.put(interactable.getId(), (Afumigatus) interactable);

        }else if(interactable instanceof Liver) //UNCOMENT WHEN ADD LIVER
            this.cells.put(interactable.getId(), (Cell) interactable);

        if(!interactables.containsKey(interactable.getName()))//   interactable.name not in this.interactables:
            this.interactables.put(interactable.getName(), new ArrayList<Interactable>());
        this.interactables.get(interactable.getName()).add(interactable);
    }*/
    
    public void setCell(Cell cell) {
    	if(cell instanceof InfectiousAgent) 
    		this.infectiousAgents.put(cell.getId(), (InfectiousAgent) cell);
    	else {
    		this.cells.put(cell.getId(), cell);
    	}
    	this.interactables.put(cell.getId(), cell);
    }
    
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
    	if(agent != null) this.interactables.put(agent.getId(), agent); //IS THIS NECESSARY!!??
    	return null;
    }
    
    public static void setMolecule(String molName, Molecule molecule) {
    	setMolecule(molName, molecule, false);
    }
    
    public static void setMolecule(String molName, Molecule molecule, boolean infectiousAgentMolecule) {
        molecules.put(molName, molecule);
        //interactables.put(molecule.getId(), molecule);
        if(infectiousAgentMolecule)
        	infectiousAgentMolecules.put(molName, molecule);
    }
    
    public static Molecule getMolecule(String molName) {
    	return molecules.get(molName);
    }

    public void setNeighbor(Voxel neighbor) {
        this.neighbors.add(neighbor);
    }

    public void next(int xbin, int ybin, int zbin, Voxel[][][] grid) {
        HashMap<Integer, Cell> listCell = (HashMap<Integer, Cell>) ((HashMap) this.cells).clone();
        Cell agent = null;
        for(Map.Entry<Integer, Cell> entry : listCell.entrySet()) {
        	agent = entry.getValue();
            this.update(agent);
            agent.move(this, 0);
            if(agent instanceof Phagocyte && !((Phagocyte) agent).getPhagosome().isEmpty())
            	this.grow(xbin, ybin, zbin, grid, (Phagocyte) agent);
        }
        HashMap<Integer, InfectiousAgent> listInf = (HashMap<Integer, InfectiousAgent>) ((HashMap) this.infectiousAgents).clone();
        InfectiousAgent infAgent = null;
        for(Map.Entry<Integer, InfectiousAgent> entry : listInf.entrySet()) {
        	infAgent = entry.getValue();
        	this.update(infAgent);
        	infAgent.move(this, 0);
        	infAgent.grow(xbin, ybin, zbin, grid);
        }
    }

    protected void update(Cell agent) {
    	agent.updateStatus();
    	if(agent instanceof Phagocyte){//isinstance(agent, Phagocyte):
    		Phagocyte phag = (Phagocyte) agent;
    		this.updatePhagosome(phag.getPhagosome());
    		phag.kill();
    	}
    }
    
    private void updatePhagosome(List<InfectiousAgent> agents) {
        for(InfectiousAgent agent : agents) {
        	agent.updateStatus(); //this.x, this.y, this.z 
        }
    }
    
    protected void grow(int xbin, int ybin, int zbin, Voxel[][][] grid, Phagocyte agent) {
    	for (InfectiousAgent phAgent : agent.getPhagosome())
            phAgent.grow(xbin, ybin, zbin, grid, agent);
    }
    
    public void degrade() {
        for(Map.Entry<String, Molecule> entry : this.molecules.entrySet()) {
        	entry.getValue().turnOver(this.x, this.y, this.z);
        	entry.getValue().computeTotalMolecule(this.x, this.y, this.z);
        }
    }
    
    public void interact() {
    	
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
                for(int i = 0; i < molSize; i++) 
                	//if(mols.get(i).getThreshold() != -1 && mols.get(i).values[0][this.x][this.y][this.z] > mols.get(i).getThreshold()) 
                		for(int j = i; j < molSize; j++) {
                			mols.get(i).interact(mols.get(j), this.x, this.y, this.z);
                			//System.out.println(mols.get(i) + "\t" + mols.get(j) + "\t" + i + "\t" + j);
                		}
                break;
            case 1:
            	for(int i : cellsIndices) {
            		if(!cells.get(i).isTime())continue;
            		for(int j : cellsIndices) {
            			//if(!cells.get(j).isTime())continue;
                        cells.get(i).interact(cells.get(j), this.x, this.y, this.z);
            		}
            	}
            	break;
            case 2:
            	for(int i : cellsIndices) {
            		if(!cells.get(i).isTime())continue;
            		for(int j = 0; j < molSize; j++) 
                        cells.get(i).interact(mols.get(j), this.x, this.y, this.z);
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
