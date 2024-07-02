package edu.uf.intracellularState;

import java.util.HashMap;
import java.util.Map;

import edu.uf.interactable.Binder;
import edu.uf.interactable.Cell;
import edu.uf.interactable.Molecule;

public abstract class IntracellularModel{
	
	//public static final int INTERACTION_STATE = Phenotype.createPhenotype();
	public static final int STATUS = Phenotype.createPhenotype();
	public static final int LIFE_STATUS = Phenotype.createPhenotype();
	public static final int LOCATION = Phenotype.createPhenotype();
	
	private int bnIteration;
	protected int[] booleanNetwork;
	protected int[] inputs;
	private Map<Integer, Integer> phenotypes;
	private Map<Integer, Integer> states;
	public static final int NUM_RECEPTORS = 100;
	
	public IntracellularModel() {
		this.phenotypes = new HashMap<>();
		this.states = new HashMap<>();
		this.inputs = new int[NUM_RECEPTORS];
		this.states.put(LIFE_STATUS, Cell.ALIVE);
	}
	
	public int getBnIteration() {
		return bnIteration; 
	}
	
	/**
	 * Returns the array with the current state of the generalized-Boolean Network.
	 * @return
	 */
	public int[] getBooleanNetwork() {
		return booleanNetwork;
	}
	
	/**
	 * Returns the array containing a generalized-Boolean Network in a given state.
	 * @param booleanNetwork
	 */
	public void setBooleanNetwork(int[] booleanNetwork) {
		this.booleanNetwork = booleanNetwork;
	}

	public void setBnIteration(int bnIteration) {
		this.bnIteration = bnIteration;
	}
	
	/**
	 * This method is part of the bind family. (See also Cell.bind, IntracellularModel.input, 
	 * and Binder.getInteractionId()). This method solves the problem of an intracellular model 
	 * that is agnostic (as much as possible) to the external signals that activate it. The 
	 * "IntracellularModel" class of the Boolean Network is split into the receptors and the rest 
	 * of the Network. Each one has its own array. Let's call them input array and Network array. 
	 * This separation aims to decouple the internal dynamics from the external signals.
	 * <br/><br/>
	 * Each position in the input array corresponds to the receptor for a Binder (e.g., a molecule). 
	 * However, those positions are not fixed a-prior. Instead, during initialization, each Binder 
	 * class receives a unique sequential interactionId. This ID becomes the index in the input array, 
	 * and that position in the array becomes the receptor for that Binder. 
	 * <br/><br/>
	 * This method receives an index (that should come from Binder.getInteractionId()) and an activation 
	 * level and sets the input array at that position with that value. (I.e., input[index] = level;)
	 * @param idx index of the input array. (that should come from Binder.getInteractionId())
	 * @param level activation level. (Usually 1-4)
	 */
	public void activateReceptor(int idx, int level) {
		this.inputs[idx] = level;
	}
	
	/**
	 * Runs one iteration of the Boolean Network. This method is called by Cell.updateStatus. 
	 * Usually this method is called once every 15 iterations (see updateStatus implementation).
	 * @param args Optional numeric arguments that can be passed to the Network.
	 */
	public abstract void processBooleanNetwork(int... args);
	
	/**
	 * This method is called by Cell.updateStatus. Usually this method is called once every 15 iterations 
	 * (see updateStatus implementation). This method updates other internal statuses of the cell that are 
	 * not related to the Boolean Network (e.g., Afumigatus change from resting conidia to swelling conidia, 
	 * cells life status changing from apoptotic to dead, etc).
	 * @param cell The cell agent it operates over
	 * @param x
	 * @param y
	 * @param z
	 */
	public abstract void updateStatus(Cell cell, int x, int y, int z);
	
	/**
	 * This method is part of the phenotype family (see also IntracellularModel.getPhenotype, 
	 * IntracellularModel.getPhenotype(int), IntracellularModel.clearPhenotype, 
	 * IntracellularModel.hasPhenotype(Molecule),  IntracellularModel.hasPhenotype(int), 
	 * IntracellularModel.hasPhenotype(int[]),  IntracellularModel.addPhenotype(Integer, int), 
	 * IntracellularModel.addPhenotype(Integer), Phenotype.createPhenotype(), Molecule.getPhenotype, 
	 * and Molecule.setPhenotype). These methods try to solve the problem of an intracellular model 
	 * that is agnostic (as much as possible) to its outputs (e.g., the molecules it secretes). 
	 * The phenotypes partially solve this problem. Each molecule receives a unique sequential 
	 * numeric phenotype. <strong>After the intracellular models run, their states must be evaluated 
	 * by this method</strong>, and a series of phenotypes are assigned. Later methods such as 
	 * "IntracellularModel.hasPhenotype(Molecule mol)" will check if one of the phenotypes from 
	 * molecule "mol" is in the list of phenotypes assigned to that intracellular model. If it is, 
	 * that molecule can be secreted; if not, it can't.
	 * <br/><br/>
	 * Notice Phenotypes are maps containing the phenotype ID (described above) and the phenotype 
	 * activation level (usually 1-4).
	 */
	protected abstract void computePhenotype();
	
	/**
	 * Returns the phenotypes dictionary. The dictionary has the structure: {Integer: phenotypeID, Integer: activation-level}.
	 * <br/><br/>
	 * activation-level is usually a number between 1-4. Can be 0 for not active or -1 for not apply or "null."
	 * @return
	 */
	public Map<Integer, Integer> getPhenotype(){
		return this.phenotypes;
	}
	
	/**
	 * Returns the activation level associated with phenotype "phenotype" in the phenotypes dictionary.
	 * <br/><br/>
	 * The dictionary has the structure: {Integer: phenotypeID, Integer: activation-level}.
	 * <br/><br/>
	 * activation-level is usually a number between 1-4. Can be 0 for not active or -1 for not apply or "null."
	 * @param phenotype
	 * @return
	 */
	public int getPhenotype(int phenotype) {
		//System.out.println(this.phenotypes + " - " + phenotype);
		return this.phenotypes.get(phenotype);
	}
	
	/**
	 * This method is part of the phenotype family (see also IntracellularModel.getPhenotype, 
	 * IntracellularModel.getPhenotype(int), IntracellularModel.clearPhenotype, 
	 * IntracellularModel.hasPhenotype(Molecule),  IntracellularModel.hasPhenotype(int), 
	 * IntracellularModel.hasPhenotype(int[]),  IntracellularModel.addPhenotype(Integer, int), 
	 * IntracellularModel.addPhenotype(Integer), Phenotype.createPhenotype(), Molecule.getPhenotype, 
	 * and Molecule.setPhenotype). These methods try to solve the problem of an intracellular 
	 * model that is agnostic (as much as possible) to its outputs (e.g., the molecules it secretes).
	 * <br/><br/>
	 * This method clears the phenotype dictionary so that a new one can be computed in the next 
	 * interaction of the Intracellular model.
	 */
	public void clearPhenotype() {
		this.phenotypes.clear();
	}
	
	/**
	 * This method is part of the phenotype family (see also IntracellularModel.getPhenotype, 
	 * IntracellularModel.getPhenotype(int), IntracellularModel.clearPhenotype, 
	 * IntracellularModel.hasPhenotype(Molecule),  IntracellularModel.hasPhenotype(int), 
	 * IntracellularModel.hasPhenotype(int[]),  IntracellularModel.addPhenotype(Integer, int), 
	 * IntracellularModel.addPhenotype(Integer), Phenotype.createPhenotype(), Molecule.getPhenotype, 
	 * and Molecule.setPhenotype). These methods try to solve the problem of an intracellular model that 
	 * is agnostic (as much as possible) to its outputs (e.g., the molecules it secretes).
	 * The phenotypes partially solve this problem. Each molecule receives a unique sequential numeric 
	 * phenotype. After the intracellular models run, their states must be evaluated by the 
	 * "IntracellularModel.computePhenotype" method, and a series of phenotypes are assigned. 
	 * <strong> Subsequently, this method will check if one of the phenotypes from molecule "mol" 
	 * is in the list of phenotypes assigned to that intracellular model. If it is, that molecule 
	 * can be secreted; if not, it can't. </strong>
	 * <br/><br/>
	 * Notice Phenotypes are maps containing the phenotype ID (described above) and the phenotype activation level (usually 1-4).
	 * @param molecule
	 * @return
	 */
	public boolean hasPhenotype(Molecule molecule) {
		return this.phenotypes.containsKey(molecule.getPhenotype());
	}
	
	/**
	 * This method is part of the phenotype family (see also IntracellularModel.getPhenotype, 
	 * IntracellularModel.getPhenotype(int), IntracellularModel.clearPhenotype, 
	 * IntracellularModel.hasPhenotype(Molecule),  IntracellularModel.hasPhenotype(int), 
	 * IntracellularModel.hasPhenotype(int[]),  IntracellularModel.addPhenotype(Integer, int), 
	 * IntracellularModel.addPhenotype(Integer), Phenotype.createPhenotype(), Molecule.getPhenotype, 
	 * and Molecule.setPhenotype). These methods try to solve the problem of an intracellular model 
	 * that is agnostic (as much as possible) to its outputs (e.g., the molecules it secretes). 
	 * The phenotypes partially solve this problem. Each molecule receives a unique sequential numeric 
	 * phenotype. After the intracellular models run, their states must be evaluated by the 
	 * "IntracellularModel.computePhenotype" method, and a series of phenotypes are assigned. 
	 * Subsequently, the "IntracellularModel.hasPhenotype(Molecule mol)" will check if one of the 
	 * phenotypes from molecule "mol" is in the list of phenotypes assigned to that intracellular model. 
	 * If it is, that molecule can be secreted; if not, it can't.
	 * <br/><br/>
	 * <strong>This method is an alternative to "IntracellularModel.hasPhenotype(Molecule mol)."</strong>
	 * <br/><br/>
	 * Notice Phenotypes are maps containing the phenotype ID (described above) and the phenotype activation level (usually 1-4).
	 * @param phenotype
	 * @return
	 */
	public boolean hasPhenotype(int phenotype) {
		return this.phenotypes.containsKey(phenotype);
	}
	
	/**
	 * This method is part of the phenotype family (see also IntracellularModel.getPhenotype, 
	 * IntracellularModel.getPhenotype(int), IntracellularModel.clearPhenotype, 
	 * IntracellularModel.hasPhenotype(Molecule),  IntracellularModel.hasPhenotype(int), 
	 * IntracellularModel.hasPhenotype(int[]),  IntracellularModel.addPhenotype(Integer, int), 
	 * IntracellularModel.addPhenotype(Integer), Phenotype.createPhenotype(), Molecule.getPhenotype, 
	 * and Molecule.setPhenotype). These methods try to solve the problem of an intracellular model 
	 * that is agnostic (as much as possible) to its outputs (e.g., the molecules it secretes). 
	 * The phenotypes partially solve this problem. Each molecule receives a unique sequential numeric 
	 * phenotype. After the intracellular models run, their states must be evaluated by the 
	 * "IntracellularModel.computePhenotype" method, and a series of phenotypes are assigned. 
	 * Subsequently, the "IntracellularModel.hasPhenotype(Molecule mol)" will check if one of the 
	 * phenotypes from molecule "mol" is in the list of phenotypes assigned to that intracellular model. 
	 * If it is, that molecule can be secreted; if not, it can't.
	 * <br/><br/>
	 * <strong>This method is an alternative to "IntracellularModel.hasPhenotype(Molecule mol), and it 
	 * returns true if at least one of the phenotypes in the array "phenotypes" is in the phenotypes 
	 * dictionary."</strong>
	 * <br/><br/>
	 * Notice Phenotypes are maps containing the phenotype ID (described above) and the phenotype activation level (usually 1-4).
	 * @param phenotype
	 * @return
	 */
	public boolean hasPhenotype(int[] phenotype) {
		for(Integer p : phenotype)
			if(this.phenotypes.containsKey(p))
				return true;
		return false;
	}
	
	/**
	 * This method is part of the phenotype family (see also IntracellularModel.getPhenotype, 
	 * IntracellularModel.getPhenotype(int), IntracellularModel.clearPhenotype, 
	 * IntracellularModel.hasPhenotype(Molecule),  IntracellularModel.hasPhenotype(int), 
	 * IntracellularModel.hasPhenotype(int[]),  IntracellularModel.addPhenotype(Integer, int), 
	 * IntracellularModel.addPhenotype(Integer), Phenotype.createPhenotype(), Molecule.getPhenotype, 
	 * and Molecule.setPhenotype). These methods try to solve the problem of an intracellular model 
	 * that is agnostic (as much as possible) to its outputs (e.g., the molecules it secretes). 
	 * The phenotypes partially solve this problem. Each molecule receives a unique sequential 
	 * numeric phenotype. After the intracellular models run, their states must be evaluated by 
	 * the "IntracellularModel.computePhenotype" method, and a series of phenotypes are assigned. 
	 * Subsequently, the "IntracellularModel.hasPhenotype(Molecule mol)" will check if one of the 
	 * phenotypes from molecule "mol" is in the list of phenotypes assigned to that intracellular 
	 * model. If it is, that molecule can be secreted; if not, it can't.
	 * <br/><br/>
	 * <strong>This method is used by "IntracellularModel.computePhenotype" to assign (add) a phenotype 
	 * and its level of activation to the dictionary of phenotypes.</strong>
	 * <br/><br/>
	 * Notice Phenotypes are maps containing the phenotype ID (described above) and the phenotype activation level (usually 1-4).
	 * @param phenotype
	 * @param level
	 */
	public void addPhenotype(Integer phenotype, int level) {
		if(phenotype == null)return;
		this.phenotypes.put(phenotype, level);
	}
	
	/**
	 * This method is part of the phenotype family (see also IntracellularModel.getPhenotype, 
	 * IntracellularModel.getPhenotype(int), IntracellularModel.clearPhenotype, 
	 * IntracellularModel.hasPhenotype(Molecule),  IntracellularModel.hasPhenotype(int), 
	 * IntracellularModel.hasPhenotype(int[]),  IntracellularModel.addPhenotype(Integer, int), 
	 * IntracellularModel.addPhenotype(Integer), Phenotype.createPhenotype(), Molecule.getPhenotype, 
	 * and Molecule.setPhenotype). These methods try to solve the problem of an intracellular model 
	 * that is agnostic (as much as possible) to its outputs (e.g., the molecules it secretes). 
	 * The phenotypes partially solve this problem. Each molecule receives a unique sequential 
	 * numeric phenotype. After the intracellular models run, their states must be evaluated by 
	 * the "IntracellularModel.computePhenotype" method, and a series of phenotypes are assigned. 
	 * Subsequently, the "IntracellularModel.hasPhenotype(Molecule mol)" will check if one of the 
	 * phenotypes from molecule "mol" is in the list of phenotypes assigned to that intracellular 
	 * model. If it is, that molecule can be secreted; if not, it can't.
	 * <br/><br/>
	 * "IntracellularModel.computePhenotype" to assign (add) a phenotype to and a "null" (-1) level 
	 * of activation to the dictionary of phenotypes. This method can be used for phenotypes for which 
	 * the level of activation does not apply.</strong>
	 * <br/><br/>
	 * Notice Phenotypes are maps containing the phenotype ID (described above) and the phenotype activation 
	 * level (usually 1-4 but in this case -1 - I.e., "null").
	 * @param phenotype
	 * @param level
	 */
	public void addPhenotype(Integer phenotype) {
		this.addPhenotype(phenotype, -1);
	}
	
	/**
	 * This method is part of the state family (see also getState(int) and setState (Int, int)). 
	 * These methods attempt to set and retrieve states not related to the main intracellular model 
	 * (e.g., Boolean Network). There are several kinds of status, and each kind can have several states. 
	 * For example, calling the method "setStatus(LIFE_STATUS, NECROTIC)" will set the cell's life status to "NECROTIC."
	 * <br/><br/>
	 * <strong>This method returns the states dictionary. The states has the structure: {Integer: state-type, Integer: state-ID}.</strong>
	 * @return
	 */
	public Map<Integer, Integer> getState(){
		return this.states;
	}
	
	/**
	 * This method is part of the state family (see also getState(int) and setState (Int, int)). 
	 * These methods attempt to set and retrieve states not related to the main intracellular model 
	 * (e.g., Boolean Network). There are several kinds of status, and each kind can have several states. 
	 * For example, calling the method "setStatus(LIFE_STATUS, NECROTIC)" will set the cell's life status to "NECROTIC."
	 * <br/><br/>
	 * <strong>The states dictionary has the structure: {Integer: state-type, Integer: state-ID}.
	 * This method returns the status related to the state-kind "state." For example, calling 
	 * "getState(LIFE_STATUS)" will return the life status of the cell (e.g., DEAD, NECROTIC, APOTOTIC, etc).</strong>
	 * @return
	 */
	public int getState(int state) {
		return this.states.get(state);
	}
	
	/**
	 * This method is part of the state family (see also getState(int) and setState (Int, int)). 
	 * These methods attempt to set and retrieve states not related to the main intracellular model 
	 * (e.g., Boolean Network). There are several kinds of status, and each kind can have several states. 
	 * <strong>For example, calling this method as "setStatus(LIFE_STATUS, NECROTIC)" will set the cell's 
	 * life status to "NECROTIC."</strong>
	 * <br/><br/>
	 * The states-dictionary has the structure: {Integer: state-type, Integer: state-ID}.
	 * @return
	 */
	public void setState(Integer stateName, int stateValue) {
		if(stateName == null)return;
		this.states.put(stateName, stateValue);
	}
	
	/*public void removePhenotype(int phenotype) {
		this.phenotypes.remove(phenotype);
	}*/
	
	/**
	 * Returns dead if life status is "DEAD," or "DYING," or "APOPTOTIC," or "NECROTIC."
	 * @return
	 */
	public boolean isDead() {
		return  this.getState(LIFE_STATUS) == Cell.DEAD ||
				this.getState(LIFE_STATUS) == Cell.DYING || 
				this.getState(LIFE_STATUS) == Cell.APOPTOTIC || 
				this.getState(LIFE_STATUS) == Cell.NECROTIC;
	}
	/*protected int e(int[] bn, int i) {
		if(i<0)return 0;
		return bn[i];
	}*/
	
	/**
	 * This method is part of the bind family. (See also Cell.bind, IntracellularModel.input, 
	 * and Binder.getInteractionId()). This method solves the problem of an intracellular model 
	 * that is agnostic (as much as possible) to the external signals that activate it. The 
	 * "IntracellularModel" class of the Boolean Network is split into the receptors and the 
	 * rest of the Network. Each one has its own array. Let's call them input array and Network array. 
	 * This separation aims to decouple the internal dynamics from the external signals.
	 * <br/><br/>
	 * Each position in the input array corresponds to the receptor for a Binder (e.g., a molecule). 
	 * However, those positions are not fixed a-prior. Instead, during initialization, each Binder class 
	 * receives a unique sequential interactionId. This ID becomes the index in the input array, and that 
	 * position in the array becomes the receptor for that Binder. 
	 * <br/><br/>
	 * This method receives a binder calls Binder.getInteractionId() and retrieves the level of activation 
	 * of its receptor. It should be used inside the Boolean network or other intracellular models.
	 * @param i a Binder object
	 * @return
	 */
	protected int input(Binder i) {
		if(i == null)return 0;
		return inputs[i.getInteractionId()];
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
	 * This method is part of the generalized Boolean function families, which also include "min," "not," and their overloadings. 
	 * Computes the max of an int array. The max is the generalized "or" function.
	 * @param a
	 * @return
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
	 * This method is part of the generalized Boolean function families, which also include "min," "not," and their overloadings. 
	 * Computes the min of an int array. The min is the generalized "and" function.
	 * @param a
	 * @return
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
	 * This method is part of the generalized Boolean function families, which also include "min," "not," and their overloadings. 
	 * Computes the max between two integers. The max is the generalized "or" function.
	 * @param a
	 * @return
	 */
	protected int max(int i, int j) {
		return (i > j ? i : j);
	}
	
	/**
	 * This method is part of the generalized Boolean function families, which also include "min," "not," and their overloadings. 
	 * Computes the min between two integers. The min is the generalized "or" function.
	 * @param a
	 * @return
	 */
	protected int min(int i, int j) {
		return (i < j ? i : j);
	}
	
	/**
	 * This method is part of the generalized Boolean function families, which also include "min," "not," and their overloadings. 
	 * Computes the generalized not of integer "i." "k" is the domain. For binary, use k = 1.
	 * @param i
	 * @param k
	 * @return
	 */
	protected int not(int i, int k) {
		return -i + k; 
	}
	
}
