package edu.uf.interactable;

import java.util.HashMap;
import java.util.Map;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;

public abstract class Molecule extends Interactable {

	private int id;
	private int moleculeId;
	
	public static final int NUM_COMPARTMENTS = 2;
	
	private double[][][][] values;
	//private double[][] compartimentValues;
	private double[] totalMolecules;
	protected double[] totalMoleculesAux;
	private Diffuse diffuse;
	private int phenotype;
	//private List<Integer> phenotypes;
	protected static Map<String, Molecule> existingMolecules = new HashMap<>();
	
	//protected List<Integer> secretionPhenotypes = new ArrayList<>();
	
	protected Molecule(double[][][][] qttys, Diffuse diffuse, String name) {
		//countReceptors++;
		//this.compartimentValues = new double[qttys.length][NUM_COMPARTMENTS - 1];
        this.id = Id.getId();
        this.moleculeId = Id.getId();
        this.values = qttys ;
        this.totalMoleculesAux = new double[qttys.length];
        this.totalMolecules = new double[qttys.length];
        this.diffuse = diffuse;
        this.phenotype  = -1;
        existingMolecules.put(name, this);
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
	
	/**
	 * This method does not create a new molecule. It only returns molecules that were previously created or else null;
	 * @param name Molecule name
	 * @return A molecule Object or null if the molecule was not instanciated yet.
	 */
	public static final Molecule searchMolecule(String name) {
		return existingMolecules.get(name);
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
	 * Increments the quantity of a molecule in the voxel located at the specified (x, y, z) position 
	 * and in the given molecular state (identified by {@code index}) by the specified amount {@code qtty}.
	 * 
	 * <p>Certain molecules, such as siderophores, can exist in multiple states (e.g., free or bound to iron). 
	 * These states are represented as a fourth dimension in the internal molecule array and can be accessed 
	 * either by index or by state name.</p>
	 *
	 * @param qtty  the quantity to be added
	 * @param index the name of the molecular state (dimension)
	 * @param x     the x-coordinate in the grid
	 * @param y     the y-coordinate in the grid
	 * @param z     the z-coordinate in the grid
	 * @return the updated total quantity of the molecule at the specified position and state
	 */
	public double inc(double qtty, String index, int x, int y, int z) {
		return inc(qtty, this.getIndex(index), x, y, z);
	}
	
	/**
	 * Increments the quantity of a molecule in the voxel located at the specified (x, y, z) position 
	 * and in the specified molecular state (given by {@code index}) by the amount {@code qtty}.
	 * 
	 * <p>Certain molecules, such as siderophores, can exist in multiple states (e.g., free or bound to iron). 
	 * These molecular states are represented as a fourth dimension in the internal data structure and can be 
	 * accessed either by numerical index or by name.</p>
	 *
	 * @param qtty  the quantity to be added
	 * @param index the index of the molecular state (dimension)
	 * @param x     the x-coordinate in the grid
	 * @param y     the y-coordinate in the grid
	 * @param z     the z-coordinate in the grid
	 * @return the updated total quantity of the molecule at the specified position and state
	 */
    public double inc(double qtty, int index, int x, int y, int z) {
        this.values[index][x][y][z] = this.values[index][x][y][z] + qtty;
        return this.values[index][x][y][z];
    }
	
    /**
     * Decrements the quantity of a molecule in the voxel located at the specified (x, y, z) position 
     * and in the specified molecular state (given by {@code index}) by the amount {@code qtty}.
     * 
     * <p>If {@code qtty} exceeds the current quantity of the molecule at the specified state and position, 
     * the amount is reduced to zero. Negative values are not allowed—this method enforces a non-negative 
     * constraint on molecular levels.</p>
     * 
     * <p>Certain molecules, such as siderophores, can exist in multiple states (e.g., free or bound to iron). 
     * These states are represented as a fourth dimension in the internal data structure and can be accessed 
     * either by index or by name.</p>
     *
     * @param qtty  the quantity to be subtracted
     * @param index the index of the molecular state (dimension)
     * @param x     the x-coordinate in the grid
     * @param y     the y-coordinate in the grid
     * @param z     the z-coordinate in the grid
     * @return the updated total quantity of the molecule at the specified position and state
     */
	public double dec(double qtty, String index, int x, int y, int z) {
		return dec(qtty, this.getIndex(index), x, y, z);
	}

	/**
	 * Decrements the quantity of a molecule in the voxel located at the specified (x, y, z) position 
	 * and in the specified molecular state (identified by {@code index}) by the amount {@code qtty}.
	 * 
	 * <p>If {@code qtty} exceeds the current quantity of the molecule at the given state and position, 
	 * the value is clamped to zero. Negative molecule levels are not allowed-This method enforces a non-negative 
     * constraint on molecular levels</p>
	 * 
	 * <p>Certain molecules, such as siderophores, can exist in multiple states (e.g., free or bound to iron). 
	 * These states are represented as a fourth dimension in the internal data structure and can be accessed 
	 * either by numerical index or by name.</p>
	 *
	 * @param qtty  the quantity to be subtracted
	 * @param index the index of the molecular state (dimension)
	 * @param x     the x-coordinate in the grid
	 * @param y     the y-coordinate in the grid
	 * @param z     the z-coordinate in the grid
	 * @return the updated total quantity of the molecule at the specified position and state
	 */
    public double dec(double qtty, int index, int x, int y, int z) {
    	qtty = this.values[index][x][y][z] - qtty >= 0 ? qtty : 0;
        this.values[index][x][y][z] = this.values[index][x][y][z] - qtty;
        return this.values[index][x][y][z];
    }
	
    /**
     * Decreases the quantity of a molecule in the voxel located at the specified (x, y, z) position 
     * and in the given molecular state (identified by {@code index}) by the percentage {@code p}.
     *
     * <p>The parameter {@code p} must be a value between 0 and 1, representing the fraction of the 
     * molecule quantity to be subtracted. For example, {@code p = 0.2} will reduce the amount by 20%.</p>
     *
     * <p><strong>Warning:</strong> values of {@code p} greater than 1 will result in negative molecule 
     * concentrations, which may lead to invalid or undefined behavior.</p>
     *
     * <p>Certain molecules, such as siderophores, can exist in multiple states (e.g., free or bound to iron). 
     * These states are represented as a fourth dimension in the internal data structure and can be accessed 
     * either by index or by name.</p>
     *
     * @param p      the percentage to be subtracted (range: 0–1)
     * @param index  the index of the molecular state (dimension)
     * @param x      the x-coordinate in the grid
     * @param y      the y-coordinate in the grid
     * @param z      the z-coordinate in the grid
     * @return the updated total quantity of the molecule at the specified position and state
     */
	public double pdec(double p, String index, int x, int y, int z) {
		return pdec(p, this.getIndex(index), x, y, z);
	}

	/**
	 * Decreases the quantity of a molecule in the voxel located at the specified (x, y, z) position 
	 * and in the specified molecular state (given by {@code index}) by a percentage {@code p}.
	 * 
	 * <p>The parameter {@code p} must be a value between 0 and 1, representing the fraction of the 
	 * molecule to be subtracted. For example, {@code p = 0.25} will reduce the amount by 25%.</p>
	 * 
	 * <p><strong>Note:</strong> values of {@code p} greater than 1 will result in negative concentrations, 
	 * which may lead to invalid or undefined behavior.</p>
	 * 
	 * <p>Certain molecules, such as siderophores, can exist in multiple states (e.g., free or bound to iron). 
	 * These states are represented as a fourth dimension in the internal molecule data structure and can 
	 * be accessed by either index or name.</p>
	 *
	 * @param p      the percentage to be subtracted (range: 0–1)
	 * @param index  the index of the molecular state (dimension)
	 * @param x      the x-coordinate in the grid
	 * @param y      the y-coordinate in the grid
	 * @param z      the z-coordinate in the grid
	 * @return the updated total quantity of the molecule at the specified position and state
	 */
    public double pdec(double p, int index, int x, int y, int z) {
        double dec = this.values[index][x][y][z] * p;
        this.values[index][x][y][z] = this.values[index][x][y][z] - dec;
        return this.values[index][x][y][z];
    }
	
    /**
     * Increases the quantity of a molecule in the voxel located at the specified (x, y, z) position 
     * and in the specified molecular state (given by {@code index}) by a percentage {@code p}.
     *
     * <p>The parameter {@code p} represents the fractional amount to add relative to the current quantity. 
     * For example, {@code p = 1.0} adds 100% of the current amount, effectively doubling it.</p>
     *
     * <p>Certain molecules, such as siderophores, can exist in multiple states (e.g., free or bound to iron). 
     * These states are represented as a fourth dimension in the internal molecule data structure and can 
     * be accessed either by index or by name.</p>
     *
     * @param p      the percentage to be added (e.g., 1.0 = 100%)
     * @param index  the index of the molecular state (dimension)
     * @param x      the x-coordinate in the grid
     * @param y      the y-coordinate in the grid
     * @param z      the z-coordinate in the grid
     * @return the updated total quantity of the molecule at the specified position and state
     */
	public double pinc(double p, String index, int x, int y, int z) {
		return pinc(p, this.getIndex(index), x, y, z);
	}

	/**
	 * Increases the quantity of a molecule in the voxel located at the specified (x, y, z) position 
	 * and in the specified molecular state (identified by {@code index}) by a percentage {@code p}.
	 *
	 * <p>The parameter {@code p} represents the proportion to be added relative to the current amount. 
	 * For example, {@code p = 1.0} adds 100% of the current quantity, effectively doubling it.</p>
	 *
	 * <p>Certain molecules, such as siderophores, can exist in multiple states (e.g., free or bound to iron). 
	 * These states are represented as a fourth dimension in the internal data structure and can be accessed 
	 * either by index or by name.</p>
	 *
	 * @param p      the percentage to be added (e.g., 1.0 = 100%)
	 * @param index  the index of the molecular state (dimension)
	 * @param x      the x-coordinate in the grid
	 * @param y      the y-coordinate in the grid
	 * @param z      the z-coordinate in the grid
	 * @return the updated total quantity of the molecule at the specified position and state
	 */
    public double pinc(double p, int index, int x, int y, int z) {
        double inc = this.values[index][x][y][z] * p;
        this.values[index][x][y][z] = this.values[index][x][y][z] + inc;
        return this.values[index][x][y][z];
    }
	
    /**
     * Sets (overwrites) the quantity of a molecule in the voxel located at the specified (x, y, z) position, 
     * in the molecular state identified by {@code index}.
     *
     * <p>Certain molecules, such as siderophores, can exist in multiple states (e.g., free or bound to iron). 
     * These states are represented as a fourth dimension in the internal data structure and can be accessed 
     * either by index or by name.</p>
     *
     * @param qtty   the quantity to be set
     * @param index  the index of the molecular state (dimension)
     * @param x      the x-coordinate in the grid
     * @param y      the y-coordinate in the grid
     * @param z      the z-coordinate in the grid
     */
	public void set(double qtty, String index, int x, int y, int z) {
		set(qtty, this.getIndex(index), x, y, z);
	}

	/**
	 * Sets (overwrites) the quantity of a molecule in the voxel located at the specified (x, y, z) position, 
	 * in the molecular state identified by {@code index}.
	 * 
	 * <p>Certain molecules, such as siderophores, can exist in multiple states (e.g., free or bound to iron). 
	 * These states are represented as a fourth dimension in the internal data structure and can be accessed 
	 * either by index or by name.</p>
	 *
	 * @param qtty   the quantity to be set
	 * @param index  the index of the molecular state (dimension)
	 * @param x      the x-coordinate in the grid
	 * @param y      the y-coordinate in the grid
	 * @param z      the z-coordinate in the grid
	 */
    public void set(double qtty, int index, int x, int y, int z) {
        this.incTotalMolecule(index, - this.values[index][x][y][z]);
        this.incTotalMolecule(index, qtty);
        this.values[index][x][y][z] = qtty;
    }
	
    /**
     * Retrieves the quantity of a molecule in the voxel located at the specified (x, y, z) position, 
     * in the molecular state identified by {@code index}.
     *
     * <p>Certain molecules, such as siderophores, can exist in multiple states (e.g., free or bound to iron). 
     * These states are represented as a fourth dimension in the internal data structure and can be accessed 
     * either by index or by name.</p>
     *
     * @param index the index of the molecular state (dimension)
     * @param x     the x-coordinate in the grid
     * @param y     the y-coordinate in the grid
     * @param z     the z-coordinate in the grid
     * @return the quantity of the molecule at the specified position and state
     */
	public double get(String index, int x, int y, int z) {
		return get(this.getIndex(index), x, y, z);
	}

	/**
	 * Retrieves the quantity of a molecule in the voxel located at the specified (x, y, z) position 
	 * and in the molecular state identified by {@code index}.
	 * 
	 * <p>Certain molecules, such as siderophores, can exist in multiple states (e.g., free or bound to iron). 
	 * These states are represented as a fourth dimension in the internal data structure and can be accessed 
	 * either by index or by name.</p>
	 *
	 * @param index the index of the molecular state (dimension)
	 * @param x     the x-coordinate in the grid
	 * @param y     the y-coordinate in the grid
	 * @param z     the z-coordinate in the grid
	 * @return the quantity of the molecule at the specified position and state
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
     * Performs the default degradation of a molecule in the voxel located at the specified (x, y, z) position.
     * 
     * <p>This method is part of a group of standard operations invoked by {@code Voxel} objects, including 
     * {@code grow}, {@code move}, {@code kill}, {@code interact}, and {@code computeTotalMolecule}.</p>
     *
     * <p>The degradation reduces the quantity of the molecule in all its states (dimensions) by a fixed percentage: 
     * specifically, by {@code 1 - MCP1_HALF_LIFE}. By default, the half-life of MCP-1 is used as a standard degradation 
     * factor. This operation is applied across all molecular states (e.g., free and iron-bound forms for siderophores).</p>
     *
     * @param x the x-coordinate in the grid
     * @param y the y-coordinate in the grid
     * @param z the z-coordinate in the grid
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
     * Advances the diffusion partial differential equation (PDE) for the molecule by {@code TIME_STEP_SIZE} minutes, 
     * if the molecule is associated with a {@code PDE} object. If the molecule has no PDE defined, this method does nothing.
     * 
     * <p>The PDE is applied to all molecular states (e.g., free and iron-bound forms for siderophores). These states are 
     * represented as a fourth array dimension in the internal data structure.</p>
     *
     * <p>Certain molecules, such as siderophores, can exist in multiple states (e.g., free or bound to iron), 
     * and diffusion is computed independently for each state.</p>
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
     * Computes the total quantity of the molecule across all states and all voxels in the simulated space.
     * This method is primarily used for reporting global statistics.
     *
     * @param x the x-coordinate in the grid (unused in the global summation, may be reserved for future use)
     * @param y the y-coordinate in the grid (unused in the global summation, may be reserved for future use)
     * @param z the z-coordinate in the grid (unused in the global summation, may be reserved for future use)
     */
    public abstract void computeTotalMolecule(int x, int y, int z);

    /**
     * Converts the given molecular state name into its corresponding numerical index.
     *
     * <p>Certain molecules, such as siderophores, can exist in multiple states 
     * (e.g., free or bound to iron). These states are represented as a fourth dimension 
     * in the internal data structure and can be accessed either by index or by name.</p>
     *
     * <p>If the specified state name does not exist, this method returns {@code null}.</p>
     *
     * @param str the name of the molecular state (dimension)
     * @return the corresponding numerical index, or {@code null} if the name is not recognized
     */
    public abstract int getIndex(String str);
    
    /**
     * <strong> Currently not being used. Consider remove it. </strong>
     * @return
     */
    public abstract double getThreshold();
    
    /**
     * Returns the number of molecular states (dimensions) associated with the molecule.
     *
     * <p>Certain molecules, such as siderophores, can exist in multiple states 
     * (e.g., free or bound to iron). These states are represented as a fourth dimension 
     * in the internal data structure and can be accessed either by index or by name.</p>
     *
     * @return the number of dimensions (states) defined for the molecule
     */
    public abstract int getNumState();
    
    /**
     * Returns the dissociation constant (Kd) of the molecule, applicable only to molecules 
     * that bind to receptors or similar targets.
     *
     * <p>For molecules that do not participate in binding interactions, this method returns {@code -1}.</p>
     *
     * @return the dissociation constant (Kd) if applicable; {@code -1} otherwise
     */
    public abstract double getKd();
    
    /**
     * Resets the overall molecule summation used for global statistics.
     *
     * <p>This method is called by {@code Exec} to clear previously accumulated values 
     * so that new statistics can be computed in the next simulation iteration.</p>
     */
    public void resetCount() {
    	for(int i = 0; i < this.totalMolecules.length; i++) {
    		this.totalMolecules[i] =  this.totalMoleculesAux[i];
    		this.totalMoleculesAux[i] = 0;
    	}
    }
	
}
