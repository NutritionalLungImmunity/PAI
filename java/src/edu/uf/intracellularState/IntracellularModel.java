package edu.uf.intracellularState;

import java.util.HashMap;
import java.util.Map;

import edu.uf.interactable.Binder;
import edu.uf.interactable.Molecule;

public abstract class IntracellularModel{
	
	//public static final int INTERACTION_STATE = Phenotype.createPhenotype();
	public static final int STATUS = Phenotype.createPhenotype();
	public static final int LIFE_STATUS = Phenotype.createPhenotype();
	public static final int LOCATION = Phenotype.createPhenotype();
	
	public static final int DEAD = Phenotype.createPhenotype();;
	public static final int ALIVE = Phenotype.createPhenotype();
	public static final int APOPTOTIC = Phenotype.createPhenotype();
	public static final int NECROTIC = Phenotype.createPhenotype();
	public static final int DYING = Phenotype.createPhenotype();
	public static final int NETOTIC = Phenotype.createPhenotype();
	
	private int bnIteration;
	protected int[] booleanNetwork;
	protected Map<Integer, Integer> inputs;
	private Map<Integer, Integer> phenotypes;
	private Map<Integer, Integer> states;
	//public static final int NUM_RECEPTORS = 100;
	
	public IntracellularModel() {
		this.phenotypes = new HashMap<>();
		this.states = new HashMap<>();
		this.states.put(LIFE_STATUS, ALIVE);
		this.inputs = new HashMap<>();
	}
	
	public int getBnIteration() {
		return bnIteration; 
	}
	
	/**
	 * Returns the array representing the current state of the generalized Boolean network.
	 *
	 * @return an {@code int[]} containing the current values of all nodes in the network
	 */
	public int[] getBooleanNetwork() {
		return booleanNetwork;
	}
	
	/**
	 * Sets the state of the generalized Boolean network using the provided array.
	 *
	 * @param booleanNetwork an {@code int[]} representing the desired state of all nodes in the network
	 */
	public void setBooleanNetwork(int[] booleanNetwork) {
		this.booleanNetwork = booleanNetwork;
	}

	public void setBnIteration(int bnIteration) {
		this.bnIteration = bnIteration;
	}
	
	/**
	 * Sets the activation level of a specific input (receptor) in the input array of the intracellular model.
	 *
	 * <p>This method is part of the "bind" family of methods (see also: {@code Cell.bind}, 
	 * {@code IntracellularModel.input}, and {@code Binder.getInteractionId()}). It addresses the need for an 
	 * intracellular model—such as a Boolean network—to remain as agnostic as possible to the specific external 
	 * signals that activate it.</p>
	 *
	 * <p>In this model, the {@link IntracellularModel} separates the network into two distinct parts:
	 * <ul>
	 *   <li>An <strong>input array</strong> representing receptor activations (external signals)</li>
	 *   <li>A <strong>network array</strong> representing the internal signaling state</li>
	 * </ul>
	 * This separation enables a decoupling of external input handling from internal dynamics.</p>
	 *
	 * <p>Each entry in the input array corresponds to a receptor for a specific {@link Binder} (e.g., a molecule). 
	 * During initialization, each {@code Binder} is assigned a unique, sequential {@code interactionId}, which 
	 * serves as its index in the input array.</p>
	 *
	 * <p>This method accepts such an index—usually obtained via {@code Binder.getInteractionId()}—and an activation 
	 * level (typically in the range 1–4), and assigns that level to the corresponding position in the input array 
	 * (i.e., {@code input[idx] = level}).</p>
	 *
	 * @param idx   the index in the input array, typically from {@code Binder.getInteractionId()}
	 * @param level the activation level to assign (usually in the range 1 to 4)
	 */
	public void activateReceptor(int idx, int level) {
		this.inputs.put(idx, level);
	}
	
	/**
	 * Executes one iteration (or update cycle) of the Boolean network.
	 *
	 * <p>This method is typically called by {@code Cell.updateStatus}, which usually triggers it 
	 * once every 15 simulation iterations (see the implementation of {@code updateStatus} for details).</p>
	 *
	 * @param args optional numeric arguments that can be passed to the network for use during the update
	 */
	public abstract void processBooleanNetwork(int... args);
	
	/**
	 * Updates internal cellular states unrelated to the Boolean network.
	 *
	 * <p>This method is typically invoked by {@code Cell.updateStatus}, usually once every 15 simulation 
	 * iterations (see the implementation of {@code updateStatus} for details).</p>
	 *
	 * <p>It handles transitions in internal states such as morphological or life cycle changes. 
	 * Examples include the transformation of {@code Aspergillus fumigatus} from resting conidia to 
	 * swelling conidia, or the transition of a cell from apoptotic to dead.</p>
	 *
	 * @param id the unique identifier of the cell agent to update
	 * @param x  the x-coordinate in the grid
	 * @param y  the y-coordinate in the grid
	 * @param z  the z-coordinate in the grid
	 */
	public abstract void updateStatus(int id, int x, int y, int z);
	
	/**
	 * Evaluates the internal state of the intracellular model and assigns phenotypes accordingly.
	 *
	 * <p>This method is part of the "phenotype family" of methods, which includes:
	 * {@code IntracellularModel.getPhenotype}, {@code IntracellularModel.getPhenotype(int)},
	 * {@code IntracellularModel.clearPhenotype}, {@code IntracellularModel.hasPhenotype(Molecule)},
	 * {@code IntracellularModel.hasPhenotype(int)}, {@code IntracellularModel.hasPhenotype(int[])},
	 * {@code IntracellularModel.addPhenotype(Integer, int)}, {@code IntracellularModel.addPhenotype(Integer)},
	 * {@code Phenotype.createPhenotype()}, {@code Molecule.getPhenotype}, and {@code Molecule.setPhenotype}.</p>
	 *
	 * <p>These methods aim to decouple the intracellular model from knowledge of its outputs 
	 * (e.g., the molecules it secretes), supporting a more modular and agnostic design.</p>
	 *
	 * <p>Each molecule is assigned a unique sequential numeric phenotype ID. After the Boolean network 
	 * or other internal logic has been updated, <strong>this method must be called to evaluate the resulting 
	 * state and assign the appropriate phenotypes</strong>. These assignments determine which molecules 
	 * the cell is allowed to secrete.</p>
	 *
	 * <p>Later checks, such as {@code IntracellularModel.hasPhenotype(Molecule mol)}, will verify whether 
	 * the phenotypes associated with the molecule are present in the intracellular model. If so, secretion 
	 * is permitted; otherwise, it is not.</p>
	 *
	 * <p>Phenotypes are internally represented as maps, where each entry consists of a phenotype ID and an 
	 * associated activation level (typically in the range of 1–4).</p>
	 */
	protected abstract void computePhenotype();
	
	/**
	 * Returns the map of assigned phenotypes for this intracellular model.
	 *
	 * <p>The map has the structure {@code {Integer phenotypeID → Integer activationLevel}}.</p>
	 *
	 * <p>The {@code activationLevel} typically ranges from 1 to 4. A value of 0 indicates an inactive phenotype, 
	 * while -1 may be used to represent "not applicable" or a null-like state.</p>
	 *
	 * @return a map of phenotype IDs to their corresponding activation levels
	 */
	public Map<Integer, Integer> getPhenotype(){
		return this.phenotypes;
	}
	
	/**
	 * Returns the activation level associated with the given phenotype ID from the phenotypes map.
	 *
	 * <p>The phenotypes map has the structure {@code {Integer phenotypeID → Integer activationLevel}}.</p>
	 *
	 * <p>The {@code activationLevel} is typically a value between 1 and 4. A value of 0 indicates an inactive phenotype, 
	 * while -1 may represent "not applicable" or an undefined state.</p>
	 *
	 * @param phenotype the ID of the phenotype to look up
	 * @return the activation level associated with the given phenotype ID
	 */
	public int getPhenotype(int phenotype) {
		//System.out.println(this.phenotypes + " - " + phenotype);
		return this.phenotypes.get(phenotype);
	}
	
	/**
	 * Clears the phenotype dictionary so that a new set of phenotypes can be computed in the next 
	 * update cycle of the intracellular model.
	 *
	 * <p>This method is part of the "phenotype family" of methods, which includes:
	 * {@code IntracellularModel.getPhenotype}, {@code IntracellularModel.getPhenotype(int)},
	 * {@code IntracellularModel.clearPhenotype}, {@code IntracellularModel.hasPhenotype(Molecule)},
	 * {@code IntracellularModel.hasPhenotype(int)}, {@code IntracellularModel.hasPhenotype(int[])},
	 * {@code IntracellularModel.addPhenotype(Integer, int)}, {@code IntracellularModel.addPhenotype(Integer)},
	 * {@code Phenotype.createPhenotype()}, {@code Molecule.getPhenotype}, and {@code Molecule.setPhenotype}.</p>
	 *
	 * <p>These methods are designed to make the intracellular model agnostic—where possible—to its outputs, 
	 * such as the specific molecules it may secrete. Clearing the phenotype dictionary ensures that stale data 
	 * does not persist across iterations.</p>
	 */
	public void clearPhenotype() {
		this.phenotypes.clear();
	}
	
	/**
	 * Checks whether the intracellular model has any of the phenotypes associated with the given {@link Molecule}.
	 *
	 * <p>This method is part of the "phenotype family" of methods, which includes:
	 * {@code IntracellularModel.getPhenotype}, {@code IntracellularModel.getPhenotype(int)},
	 * {@code IntracellularModel.clearPhenotype}, {@code IntracellularModel.hasPhenotype(Molecule)},
	 * {@code IntracellularModel.hasPhenotype(int)}, {@code IntracellularModel.hasPhenotype(int[])},
	 * {@code IntracellularModel.addPhenotype(Integer, int)}, {@code IntracellularModel.addPhenotype(Integer)},
	 * {@code Phenotype.createPhenotype()}, {@code Molecule.getPhenotype}, and {@code Molecule.setPhenotype}.</p>
	 *
	 * <p>These methods aim to support intracellular models that are as agnostic as possible to their downstream outputs 
	 * (e.g., secreted molecules). Phenotypes help bridge this abstraction by providing a numeric interface between 
	 * intracellular state and external behavior.</p>
	 *
	 * <p>Each molecule is assigned a unique, sequential numeric phenotype ID. After the intracellular model runs, 
	 * its internal state should be evaluated using {@code IntracellularModel.computePhenotype()}, which assigns 
	 * a set of phenotypes to the model. <strong>This method then checks whether the phenotype(s) assigned to the 
	 * given {@code molecule} are present in the intracellular model. If so, the molecule is permitted to be secreted; 
	 * otherwise, it is not.</strong></p>
	 *
	 * <p>Internally, phenotypes are stored as a map of phenotype IDs to activation levels (typically 1–4).</p>
	 *
	 * @param molecule the {@link Molecule} to check against the model’s assigned phenotypes
	 * @return {@code true} if the model has a matching phenotype for the given molecule; {@code false} otherwise
	 */
	public boolean hasPhenotype(Molecule molecule) {
		return this.phenotypes.containsKey(molecule.getPhenotype());
	}
	
	/**
	 * Checks whether the intracellular model has the specified phenotype ID assigned.
	 *
	 * <p>This method is part of the "phenotype family" of methods, which includes:
	 * {@code IntracellularModel.getPhenotype}, {@code IntracellularModel.getPhenotype(int)},
	 * {@code IntracellularModel.clearPhenotype}, {@code IntracellularModel.hasPhenotype(Molecule)},
	 * {@code IntracellularModel.hasPhenotype(int)}, {@code IntracellularModel.hasPhenotype(int[])},
	 * {@code IntracellularModel.addPhenotype(Integer, int)}, {@code IntracellularModel.addPhenotype(Integer)},
	 * {@code Phenotype.createPhenotype()}, {@code Molecule.getPhenotype}, and {@code Molecule.setPhenotype}.</p>
	 *
	 * <p>These methods aim to make intracellular models as agnostic as possible to their outputs 
	 * (e.g., the molecules they secrete). Phenotypes provide an abstraction layer between intracellular 
	 * state and secretion logic. Each molecule is assigned a unique, sequential numeric phenotype ID.</p>
	 *
	 * <p>After the intracellular model has been evaluated using {@code IntracellularModel.computePhenotype()}, 
	 * a set of phenotypes is assigned to the model. These phenotypes can then be queried using methods like 
	 * {@code IntracellularModel.hasPhenotype(Molecule)} to determine if a molecule may be secreted based on 
	 * the current cellular state.</p>
	 *
	 * <p><strong>This method provides a direct alternative to 
	 * {@code IntracellularModel.hasPhenotype(Molecule mol)} by allowing queries using a phenotype ID.</strong></p>
	 *
	 * <p>Phenotypes are stored as a map from phenotype IDs to activation levels, typically in the range 1–4.</p>
	 *
	 * @param phenotype the numeric ID of the phenotype to check
	 * @return {@code true} if the intracellular model has the given phenotype; {@code false} otherwise
	 */
	public boolean hasPhenotype(int phenotype) {
		return this.phenotypes.containsKey(phenotype);
	}
	
	/**
	 * Checks whether the intracellular model has at least one of the specified phenotype IDs.
	 *
	 * <p>This method is part of the "phenotype family" of methods, which includes:
	 * {@code IntracellularModel.getPhenotype}, {@code IntracellularModel.getPhenotype(int)},
	 * {@code IntracellularModel.clearPhenotype}, {@code IntracellularModel.hasPhenotype(Molecule)},
	 * {@code IntracellularModel.hasPhenotype(int)}, {@code IntracellularModel.hasPhenotype(int[])},
	 * {@code IntracellularModel.addPhenotype(Integer, int)}, {@code IntracellularModel.addPhenotype(Integer)},
	 * {@code Phenotype.createPhenotype()}, {@code Molecule.getPhenotype}, and {@code Molecule.setPhenotype}.</p>
	 *
	 * <p>These methods are designed to allow intracellular models to remain agnostic, as much as possible, 
	 * to their downstream outputs (e.g., the specific molecules they secrete). Phenotypes act as an abstraction 
	 * layer, with each molecule assigned a unique, sequential numeric phenotype ID.</p>
	 *
	 * <p>After the intracellular model completes its update cycle, its internal state should be evaluated 
	 * using {@code IntracellularModel.computePhenotype()}, which assigns relevant phenotypes to the model. 
	 * These phenotypes are then used to determine secretion behavior through lookup methods like this one.</p>
	 *
	 * <p><strong>This method serves as an alternative to 
	 * {@code IntracellularModel.hasPhenotype(Molecule mol)} and returns {@code true} if at least one of 
	 * the phenotype IDs in the provided array is present in the model’s phenotype map.</strong></p>
	 *
	 * <p>Phenotypes are stored as a map where each entry consists of a phenotype ID and its activation level 
	 * (typically ranging from 1 to 4).</p>
	 *
	 * @param phenotype an array of phenotype IDs to check
	 * @return {@code true} if at least one of the specified phenotype IDs is assigned to the model; {@code false} otherwise
	 */
	public boolean hasPhenotype(int[] phenotype) {
		for(Integer p : phenotype)
			if(this.phenotypes.containsKey(p))
				return true;
		return false;
	}
	
	/**
	 * Adds a phenotype to the intracellular model's phenotype map, along with its activation level.
	 *
	 * <p>This method is part of the "phenotype family" of methods, which includes:
	 * {@code IntracellularModel.getPhenotype}, {@code IntracellularModel.getPhenotype(int)},
	 * {@code IntracellularModel.clearPhenotype}, {@code IntracellularModel.hasPhenotype(Molecule)},
	 * {@code IntracellularModel.hasPhenotype(int)}, {@code IntracellularModel.hasPhenotype(int[])},
	 * {@code IntracellularModel.addPhenotype(Integer, int)}, {@code IntracellularModel.addPhenotype(Integer)},
	 * {@code Phenotype.createPhenotype()}, {@code Molecule.getPhenotype}, and {@code Molecule.setPhenotype}.</p>
	 *
	 * <p>These methods aim to support intracellular models that are agnostic to their outputs, 
	 * such as the molecules they secrete. Each molecule is assigned a unique, sequential phenotype ID, 
	 * and phenotypes serve as an abstraction between internal state and external behavior.</p>
	 *
	 * <p><strong>This method is used by {@code IntracellularModel.computePhenotype} to assign a phenotype 
	 * and its activation level to the model's phenotype map.</strong></p>
	 *
	 * <p>Phenotypes are stored as a map of phenotype IDs to activation levels, typically in the range 1–4.</p>
	 *
	 * @param phenotype the phenotype ID to add
	 * @param level     the activation level of the phenotype (usually between 1 and 4)
	 */
	public void addPhenotype(Integer phenotype, int level) {
		if(phenotype == null)return;
		this.phenotypes.put(phenotype, level);
	}
	
	/**
	 * Adds a phenotype to the intracellular model's phenotype map with a {@code null}-equivalent activation level.
	 *
	 * <p>This method is part of the "phenotype family" of methods, which includes:
	 * {@code IntracellularModel.getPhenotype}, {@code IntracellularModel.getPhenotype(int)},
	 * {@code IntracellularModel.clearPhenotype}, {@code IntracellularModel.hasPhenotype(Molecule)},
	 * {@code IntracellularModel.hasPhenotype(int)}, {@code IntracellularModel.hasPhenotype(int[])},
	 * {@code IntracellularModel.addPhenotype(Integer, int)}, {@code IntracellularModel.addPhenotype(Integer)},
	 * {@code Phenotype.createPhenotype()}, {@code Molecule.getPhenotype}, and {@code Molecule.setPhenotype}.</p>
	 *
	 * <p>These methods are designed to support intracellular models that remain agnostic to their outputs 
	 * (e.g., the molecules they secrete). Phenotypes act as an abstraction layer, with each molecule receiving 
	 * a unique sequential numeric phenotype ID.</p>
	 *
	 * <p><strong>This method is used by {@code IntracellularModel.computePhenotype} to assign a phenotype 
	 * with a "null" activation level—represented by {@code -1}—for cases where an activation level does not apply.</strong></p>
	 *
	 * <p>Phenotypes are stored as a map where keys are phenotype IDs and values are activation levels. 
	 * Activation levels usually range from 1 to 4, but in this case, {@code -1} is used to indicate an undefined or non-applicable level.</p>
	 *
	 * @param phenotype the phenotype ID to add
	 */
	public void addPhenotype(Integer phenotype) {
		this.addPhenotype(phenotype, -1);
	}
	
	/**
	 * Returns the internal states dictionary for the cell.
	 *
	 * <p>This method is part of the "state family" of methods (see also {@code getState(int)} and {@code setState(int, int)}). 
	 * These methods are used to manage auxiliary states that are not directly related to the main intracellular model 
	 * (e.g., the Boolean network).</p>
	 *
	 * <p>There are multiple categories of states (state types), and each type can have multiple possible values (state IDs). 
	 * For example, calling {@code setState(LIFE_STATUS, NECROTIC)} sets the cell's life status to {@code NECROTIC}.</p>
	 *
	 * <p><strong>This method returns the entire state dictionary, which has the structure:
	 * {@code {Integer stateType → Integer stateId}}.</strong></p>
	 *
	 * @return a map representing the current set of auxiliary states
	 */
	public Map<Integer, Integer> getState(){
		return this.states;
	}
	
	/**
	 * Returns the current state ID associated with the specified state type.
	 *
	 * <p>This method is part of the "state family" of methods (see also {@code getState(int)} and {@code setState(int, int)}). 
	 * These methods manage auxiliary states that are not part of the main intracellular model (e.g., the Boolean network).</p>
	 *
	 * <p>There are multiple kinds of states (state types), each with several possible values (state IDs). 
	 * For example, calling {@code setState(LIFE_STATUS, NECROTIC)} sets the cell's life status to {@code NECROTIC}.</p>
	 *
	 * <p><strong>The states dictionary has the structure: {@code {Integer stateType → Integer stateId}}. 
	 * This method retrieves the state ID corresponding to the given {@code stateType}. 
	 * For instance, calling {@code getState(LIFE_STATUS)} returns the cell's life status 
	 * (e.g., {@code DEAD}, {@code NECROTIC}, {@code APOPTOTIC}, etc.).</strong></p>
	 *
	 * @param state the state type to retrieve (e.g., {@code LIFE_STATUS})
	 * @return the current state ID associated with the specified state type
	 */
	public int getState(int state) {
		return this.states.get(state);
	}
	
	/**
	 * Sets the state ID associated with a specific state type in the cell's internal state dictionary.
	 *
	 * <p>This method is part of the "state family" of methods (see also {@code getState(int)} and {@code setState(int, int)}). 
	 * These methods manage auxiliary states that are not part of the primary intracellular model 
	 * (e.g., the Boolean network).</p>
	 *
	 * <p>There are various categories of states (state types), each of which can hold several possible values 
	 * (state IDs). <strong>For example, calling this method as {@code setState(LIFE_STATUS, NECROTIC)} 
	 * will set the cell's life status to {@code NECROTIC}.</strong></p>
	 *
	 * <p>The internal state dictionary has the structure: {@code {Integer stateType → Integer stateId}}.</p>
	 *
	 * @param stateType the type of state to be updated (e.g., {@code LIFE_STATUS})
	 * @param stateId   the ID representing the new state value (e.g., {@code NECROTIC})
	 */
	public void setState(Integer stateName, int stateValue) {
		if(stateName == null)return;
		this.states.put(stateName, stateValue);
	}
	
	/**
	 * Returns {@code true} if the cell's life status is considered dead.
	 *
	 * <p>This includes the following life states: {@code DEAD}, {@code DYING}, {@code APOPTOTIC}, or {@code NECROTIC}.</p>
	 *
	 * @return {@code true} if the life status indicates a dead or dying state; {@code false} otherwise
	 */
	public boolean isDead() {
		return  this.getState(LIFE_STATUS) == DEAD ||
				this.getState(LIFE_STATUS) == DYING || 
				this.getState(LIFE_STATUS) == APOPTOTIC || 
				this.getState(LIFE_STATUS) == NECROTIC;
	}
	/*protected int e(int[] bn, int i) {
		if(i<0)return 0;
		return bn[i];
	}*/
	
	/**
	 * Retrieves the activation level of the receptor associated with the given {@link Binder} object.
	 *
	 * <p>This method is part of the "bind" family of methods (see also: {@code Cell.bind}, 
	 * {@code IntracellularModel.input}, and {@code Binder.getInteractionId()}). It supports the design 
	 * goal of keeping the intracellular model as agnostic as possible to external activating signals.</p>
	 *
	 * <p>In the Boolean network implementation of {@code IntracellularModel}, the system is conceptually 
	 * divided into two parts: a receptor input array and the internal network array. This separation allows 
	 * external signals to be processed independently from the internal logic.</p>
	 *
	 * <p>Each {@code Binder} (e.g., a molecule) is assigned a unique, sequential {@code interactionId} 
	 * during initialization. This ID serves as the index in the input array, effectively mapping each 
	 * {@code Binder} to a specific receptor slot.</p>
	 *
	 * <p>This method uses the binder’s {@code getInteractionId()} to look up and return the activation level 
	 * of the corresponding receptor in the input array. It is intended for use within Boolean networks or 
	 * other intracellular model logic.</p>
	 *
	 * @param i a {@link Binder} object
	 * @return the activation level of the receptor corresponding to the binder's interaction ID
	 */
	protected int getInput(Binder i) {
		if(i == null)return 0;
		Integer j = null;
		if((j = inputs.get(i.getInteractionId())) == null)return 0;
		return j;
	}
	
	/*protected void r(int[] bn, int i) {
		if(i<0)return;
		bn[i] = 0;
	}
	
	protected int o(int[] bn, Set<Integer> idxs) {
		for(int i : idxs) 
			if(bn[i] > 0)return bn[i];
		return 0;
	}*/
	
	/**
	 * Computes the maximum value in the given integer array.
	 *
	 * <p>This method is part of the generalized Boolean function family, which also includes 
	 * {@code min}, {@code not}, and their respective overloads. In the context of generalized 
	 * Boolean logic, {@code max} serves as the equivalent of the logical "OR" operation.</p>
	 *
	 * @param a the array of integers to evaluate
	 * @return the maximum value found in the array
	 */
	protected int max(int[] a) {
		int max = 0;
		for(int i : a) {
			if(i > max)
				max = i;
		}
		return max;
	}
	
	/**
	 * Computes the minimum value in the given integer array.
	 *
	 * <p>This method is part of the generalized Boolean function family, which also includes 
	 * {@code max}, {@code not}, and their respective overloads. In the context of generalized 
	 * Boolean logic, {@code min} serves as the equivalent of the logical "AND" operation.</p>
	 *
	 * @param a the array of integers to evaluate
	 * @return the minimum value found in the array
	 */
	protected int min(int[] a) {
		int min = 0;
		for(int i : a) {
			if(i < min)
				min = i;
		}
		return min;
	}
	
	/**
	 * Computes the maximum of two integer values.
	 *
	 * <p>This method is part of the generalized Boolean function family, which also includes 
	 * {@code min}, {@code not}, and their overloads. In the context of generalized Boolean logic, 
	 * {@code max} serves as the equivalent of the logical "OR" operation.</p>
	 *
	 * @param a the first integer
	 * @param b the second integer
	 * @return the greater of {@code a} and {@code b}
	 */
	protected int max(int i, int j) {
		return (i > j ? i : j);
	}
	
	/**
	 * Computes the minimum of two integer values.
	 *
	 * <p>This method is part of the generalized Boolean function family, which also includes 
	 * {@code max}, {@code not}, and their overloads. In the context of generalized Boolean logic, 
	 * {@code min} serves as the equivalent of the logical "AND" operation.</p>
	 *
	 * @param a the first integer
	 * @param b the second integer
	 * @return the smaller of {@code a} and {@code b}
	 */
	protected int min(int i, int j) {
		return (i < j ? i : j);
	}
	
	/**
	 * Computes the generalized NOT of the given integer {@code i}, based on the specified domain {@code k}.
	 *
	 * <p>This method is part of the generalized Boolean function family, which also includes 
	 * {@code min}, {@code max}, and their overloads. The generalized NOT operation is defined 
	 * over a multi-valued logic domain.</p>
	 *
	 * <p>For binary logic, use {@code k = 1}, in which case this behaves like a standard logical NOT. 
	 * For multi-valued logic, the result is computed as {@code k - i}.</p>
	 *
	 * @param i the input value
	 * @param k the domain size (e.g., {@code k = 1} for binary logic)
	 * @return the result of the generalized NOT operation
	 */
	protected int not(int i, int k) {
		return -i + k; 
	}
	
}
