package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;

public abstract class Molecule extends Interactable {

	private int id;
	private int moleculeId;
	
	public static final int NUM_COMPARTMENTS = 2;
	
	private double[][][][] values;
	private double[][] compartimentValues;
	private double[] totalMolecules;
	protected double[] totalMoleculesAux;
	private Diffuse diffuse;
	private int phenotype;
	//private List<Integer> phenotypes;
	
	//protected List<Integer> secretionPhenotypes = new ArrayList<>();
	
	protected Molecule(double[][][][] qttys, Diffuse diffuse) {
		//countReceptors++;
		this.compartimentValues = new double[qttys.length][NUM_COMPARTMENTS - 1];
        this.id = Id.getId();
        this.moleculeId = Id.getMoleculeId();
        this.values = qttys ;
        this.totalMoleculesAux = new double[qttys.length];
        this.totalMolecules = new double[qttys.length];
        this.diffuse = diffuse;
        this.phenotype  = -1;
        /*this.phenotypes = new ArrayList<>();
        for(int i : phenotypes)
        	this.phenotypes.add(i);*/
        for(int i = 0; i < qttys.length; i++) {
        	for(double[][] matrix : qttys[i])
        		for(double[] array : matrix)
        			for(double d : array) 
        				this.incTotalMolecule(i, d);
        }
	}
	
	public int getInteractionId() {
		return moleculeId;
	}
	
	/**
	 * 
	 * @param phenotype
	 */
	public void setPhenotye(int phenotype) {
		this.phenotype = phenotype;
	}
	
	public int getPhenotype() {
		return this.phenotype;
	}
	
	/*public void addPhenotype(int phenotype) {
		this.secretionPhenotypes.add(phenotype);
	}*/
	
	/*public List<Integer> getPhenotype(){
		return phenotypes;
	}*/
	
	private void incTotalMolecule(int index, double inc) {
    	this.totalMolecules[index] = this.totalMolecules[index] + inc;
    }
	
	/**
	 * This method increments the quantity of molecule in the Voxel at position 
	 * (x, y, z) and at state "index" by the amount "qtty."
	 * <br/><br/>
     * Some molecules, such as siderophores, have more than one state 
     * (e.g., free/bound-to-iron). These states are accommodated into a 
     * fourth array dimension. These dimensions or states can be accessed 
     * by index or by their name.
	 * @param qtty quantity to be added
	 * @param index dimension (state) name
	 * @param x axis position in the grid
	 * @param y axis position in the grid
	 * @param z axis position in the grid
	 * @return
	 */
	public double inc(double qtty, String index, int x, int y, int z) {
		return inc(qtty, this.getIndex(index), x, y, z);
	}
	
	/**
	 * This method increments the quantity of molecule in the Voxel at position 
	 * (x, y, z) and at state "index" by the amount "qtty."
	 * <br/><br/>
     * Some molecules, such as siderophores, have more than one state 
     * (e.g., free/bound-to-iron). These states are accommodated into a 
     * fourth array dimension. These dimensions or states can be accessed 
     * by index or by their name.
	 * @param qtty quantity to be added
	 * @param index dimension (state)
	 * @param x axis position in the grid
	 * @param y axis position in the grid
	 * @param z axis position in the grid
	 * @return
	 */
    public double inc(double qtty, int index, int x, int y, int z) {
        this.values[index][x][y][z] = this.values[index][x][y][z] + qtty;
        return this.values[index][x][y][z];
    }
	
    /**
     * This method increments the quantity of molecule in the Voxel at position (x, y, z) 
     * and at state "index" by the amount "qtty." If the amount "qtty" is greater than the 
     * quantity of molecule at (index, x, y, z), the amount of molecule is decremented to 
     * zero. This function does not allows negative levels of molecule
	 * <br/><br/>
     * Some molecules, such as siderophores, have more than one state 
     * (e.g., free/bound-to-iron). These states are accommodated into a 
     * fourth array dimension. These dimensions or states can be accessed 
     * by index or by their name.
     * @param qtty quantity to be subtracted
     * @param index dimension (state)
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     * @return
     */
	public double dec(double qtty, String index, int x, int y, int z) {
		return dec(qtty, this.getIndex(index), x, y, z);
	}

	/**
	 * This method decrements the quantity of molecule in the Voxel at position (x, y, z) 
     * and at state "index" by the amount "qtty." If the amount "qtty" is greater than the 
     * quantity of molecule at (index, x, y, z), the amount of molecule is decremented to 
     * zero. This function does not allow negative levels of a molecule.
	 * <br/><br/>
     * Some molecules, such as siderophores, have more than one state 
     * (e.g., free/bound-to-iron). These states are accommodated into a 
     * fourth array dimension. These dimensions or states can be accessed 
     * by index or by their name.
	 * @param qtty quantity to be subtracted
	 * @param index dimension (state)
	 * @param x axis position in the grid
	 * @param y axis position in the grid
	 * @param z axis position in the grid
	 * @return
	 */
    public double dec(double qtty, int index, int x, int y, int z) {
    	qtty = this.values[index][x][y][z] - qtty >= 0 ? qtty : 0;
        this.values[index][x][y][z] = this.values[index][x][y][z] - qtty;
        return this.values[index][x][y][z];
    }
	
    /**
     * This method decrements the quantity of molecule in the Voxel at position (x, y, z) 
     * and at state "index" by the percentage "p." The percentage "p" must be a number 
     * between 0 and 1. 
     * <strong>"p" larger than one will lead to negative concentrations of the molecule.</strong>
     * <br/><br/>
     * Some molecules, such as siderophores, have more than one state (e.g., free/bound-to-iron). 
     * These states are accommodated into a fourth array dimension. These dimensions or states can 
     * be accessed by index or by their name.
     * @param p percentage to be subtracted [0-1]
     * @param index dimension (state)
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     * @return
     */
	public double pdec(double p, String index, int x, int y, int z) {
		return pdec(p, this.getIndex(index), x, y, z);
	}

	/**
	 * This method decrements the quantity of molecule in the Voxel at position (x, y, z) 
     * and at state "index" by the percentage "p." The percentage "p" must be a number 
     * between 0 and 1. 
     * <strong>"p" larger than one will lead to negative concentrations of the molecule.</strong>
     * <br/><br/>
     * Some molecules, such as siderophores, have more than one state (e.g., free/bound-to-iron). 
     * These states are accommodated into a fourth array dimension. These dimensions or states can 
     * be accessed by index or by their name.
	 * @param p percentage to be subtracted [0-1]
	 * @param index dimension (state)
	 * @param x axis position in the grid
	 * @param y axis position in the grid
	 * @param z axis position in the grid
	 * @return
	 */
    public double pdec(double p, int index, int x, int y, int z) {
        double dec = this.values[index][x][y][z] * p;
        this.values[index][x][y][z] = this.values[index][x][y][z] - dec;
        return this.values[index][x][y][z];
    }
	
    /**
     * This method decrements the quantity of molecule in the Voxel at position (x, y, z) 
     * and at state "index" by the percentage "p."
     * <br/><br/>
     * Some molecules, such as siderophores, have more than one state (e.g., free/bound-to-iron). 
     * These states are accommodated into a fourth array dimension. These dimensions or states can 
     * be accessed by index or by their name.
     * @param p percentage to be added. 1=100%
     * @param index dimension (state)
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     * @return
     */
	public double pinc(double p, String index, int x, int y, int z) {
		return pinc(p, this.getIndex(index), x, y, z);
	}

	/**
	 * This method decrements the quantity of molecule in the Voxel at position (x, y, z) 
     * and at state "index" by the percentage "p."
     * <br/><br/>
     * Some molecules, such as siderophores, have more than one state (e.g., free/bound-to-iron). 
     * These states are accommodated into a fourth array dimension. These dimensions or states can 
     * be accessed by index or by their name.
	 * @param p percentage ot be added. 1=100%
	 * @param index dimension (state)
	 * @param x axis position in the grid
	 * @param y axis position in the grid
	 * @param z axis position in the grid
	 * @return
	 */
    public double pinc(double p, int index, int x, int y, int z) {
        double inc = this.values[index][x][y][z] * p;
        this.values[index][x][y][z] = this.values[index][x][y][z] + inc;
        return this.values[index][x][y][z];
    }
	
    /**
     * Set (overwrite) the quantity of molecule in the Voxel at position (x, y, z) with state name "index."
     * <br/><br/>
     * Some molecules, such as siderophores, have more than one state (e.g., free/bound-to-iron). 
     * These states are accommodated into a fourth array dimension. These dimensions or states can 
     * be accessed by index or by their name.
     * @param qtty quantity to be sed
     * @param index dimension (state)
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     */
	public void set(double qtty, String index, int x, int y, int z) {
		set(qtty, this.getIndex(index), x, y, z);
	}

	/**
	 * Set (overwrite) the quantity of molecule in the Voxel at position (x, y, z) with state "index."
     * <br/><br/>
     * Some molecules, such as siderophores, have more than one state (e.g., free/bound-to-iron). 
     * These states are accommodated into a fourth array dimension. These dimensions or states can 
     * be accessed by index or by their name.
	 * @param qtty quantity to be set.
	 * @param index dimension (state)
	 * @param x axis position in the grid
	 * @param y axis position in the grid
	 * @param z axis position in the grid
	 */
    public void set(double qtty, int index, int x, int y, int z) {
        this.incTotalMolecule(index, - this.values[index][x][y][z]);
        this.incTotalMolecule(index, qtty);
        this.values[index][x][y][z] = qtty;
    }
	
    /**
     * Get the quantity of molecule in the Voxel at position (x, y, z) with state name "index."
     * <br/><br/>
     * Some molecules, such as siderophores, have more than one state (e.g., free/bound-to-iron). 
     * These states are accommodated into a fourth array dimension. These dimensions or states can 
     * be accessed by index or by their name.
     * @param index dimension (state)
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     * @return
     */
	public double get(String index, int x, int y, int z) {
		return get(this.getIndex(index), x, y, z);
	}

	/**
	 * Get the quantity of molecule in the Voxel at position (x, y, z) with state "index."
     * <br/><br/>
     * Some molecules, such as siderophores, have more than one state (e.g., free/bound-to-iron). 
     * These states are accommodated into a fourth array dimension. These dimensions or states can 
     * be accessed by index or by their name.
	 * @param index dimension (state)
	 * @param x axis position in the grid
	 * @param y axis position in the grid
	 * @param z axis position in the grid
	 * @return
	 */
    public double get(int index, int x, int y, int z) {
        return this.values[index][x][y][z];
    }
    
    /*public void turnOver(int x, int y, int z) {
    	for(int i = 0; i < this.values.length; i++) { 
    		double voxelConentration = this.get(i, x, y, z) / Constants.VOXEL_VOL;
    		double serumConcentration = this.get(i, 0) / Constants.SERUM_VOL;
    		double dmdt = Constants.TURNOVER_RATE * (voxelConentration - serumConcentration);
    		this.inc(dmdt, i, 0);
    		this.dec(dmdt, i, x, y, z);
    	}
    }*/
    
    /**
     * This is the defacto degrade method. This method is part of a group of methods 
     * that are called by the Voxel objects (see grow, move, kill, interact, and 
     * computeTotalMolecule). This method decreases the amount of molecule in the 
     * "Voxel" at position (x, y, z) by "1- MCP1_HALF_LIFE" percent. Currently, the 
     * half-life of MCP-1 is being used by default. This method decreases the amount 
     * of molecule in all dimensions (states) of the molecule. 
     * <br/><br/>
     * Some molecules, such as siderophores, have more than one state (e.g., free/bound-to-iron). 
     * These states are accommodated into a fourth array dimension.
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     */
    public void turnOver(int x, int y, int z) {
    	for(int i = 0; i < this.values.length; i++) { 
    		//this.pdec(1-Util.turnoverRate(this.get(i, x, y, z), 0), i, x, y, z);
    		this.pdec(1-Constants.MCP1_HALF_LIFE, i, x, y, z);
    		
    	}
    }
    
    public abstract void degrade();
    

    /**
     * <strong>This method is currently not being used. See "turnOver." 
     * Although this is not wrong this should change.</strong>
     * @param p
     * @param x
     */
    protected void degrade(double p, int x) {
        if(p < 0) {
            return;
        }
        for(int i = 0; i < this.values.length; i++) { 
            //this.pdec(1-p, i, x);
        }
    }
    
    public int getId() {
    	return id;
    }
    
    public double getTotalMolecule(int index) {
    	return this.totalMolecules[index];
    }
    
    /**
     * This method runs "TIME_STEP_SIZE" minutes of the diffuse PDE if the 
     * molecule has a PDE object or does nothing otherwise. It runs the PDE 
     * in all the dimensions (states) of the molecule. 
     * <br/><br/>
     * Some molecules, such as siderophores, have more than one state 
     * (e.g., free/bound-to-iron). These states are accommodated into a 
     * fourth array dimension.
     */
    public void diffuse() {
    	if(diffuse == null) return;
    	for(int i = 0; i < values.length; i++) {
    		diffuse.solver(values, i); 
    	}
    }
    
    /*public static int[] addSelfPhenotype(int[] phenotypes, final int  phenotype) {
    	int[] nphenotypes = null;
    	if(phenotypes == null) {
    		nphenotypes = new int[phenotypes.length + 1];
    		for(int i = 0; i < phenotypes.length; i++)
    			nphenotypes[i] = phenotypes[i];
    		nphenotypes[phenotypes.length] = phenotype;
    	}else 
    		nphenotypes = new int[] {phenotype};
    	return nphenotypes;
    }*/
            
    /**
     * Computes the summation of the quantities of molecules across the whole 
     * simulated space. This method is important to report statistics.
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     */
    public abstract void computeTotalMolecule(int x, int y, int z);

    /**
     * Converts the dimension (state) name into a numerical index. Some molecules,
     * It should return null if the name does not exist. 
     * such as siderophores, have more than one state (e.g., free/bound-to-iron). 
     * These states are accommodated into a fourth array dimension. These dimensions 
     * or states can be accessed by index or by their name.
     * @param str dimension (state) name.
     * @return
     */
    public abstract int getIndex(String str);
    
    /**
     * <strong> Currently not being used. Consider remove it. </strong>
     * @return
     */
    public abstract double getThreshold();
    
    /**
     * Returns the number of dimensions (states) of the molecule. Some molecules, such as 
     * siderophores, have more than one state (e.g., free/bound-to-iron). These states are 
     * accommodated into a fourth array dimension. These dimensions or states can be accessed 
     * by index or by their name.
     * @return
     */
    public abstract int getNumState();
    
    /**
     * Returns the Kd of Molecule binding only for molecules that bind to 
     * receptors or similar. Should return -1 for other cases.
     * @return
     */
    public abstract double getKd();
    
    /**
     * This method is called by "Exec." This method resets the overall molecule summation 
     * (statistics) so that new statistics can be calculated in the next iteration. 
     */
    public void resetCount() {
    	for(int i = 0; i < this.totalMolecules.length; i++) {
    		this.totalMolecules[i] =  this.totalMoleculesAux[i];
    		this.totalMoleculesAux[i] = 0;
    	}
    }
	
}
