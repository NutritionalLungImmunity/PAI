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
	
	public int[] getBooleanNetwork() {
		return booleanNetwork;
	}
	
	public void setBooleanNetwork(int[] booleanNetwork) {
		this.booleanNetwork = booleanNetwork;
	}

	public void setBnIteration(int bnIteration) {
		this.bnIteration = bnIteration;
	}
	
	public void activateReceptor(int idx, int level) {
		this.inputs[idx] = level;
	}
	
	public abstract void processBooleanNetwork(int... args);
	
	public abstract void updateStatus(Cell cell, int x, int y, int z);
	
	protected abstract void computePhenotype();
	
	public Map<Integer, Integer> getPhenotype(){
		return this.phenotypes;
	}
	
	public int getPhenotype(int phenotype) {
		//System.out.println(this.phenotypes + " - " + phenotype);
		return this.phenotypes.get(phenotype);
	}
	
	public void clearPhenotype() {
		this.phenotypes.clear();
	}
	
	public boolean hasPhenotype(Molecule molecule) {
		return this.phenotypes.containsKey(molecule.getPhenotype());
	}
	
	public boolean hasPhenotype(int phenotype) {
		return this.phenotypes.containsKey(phenotype);
	}
	
	public boolean hasPhenotype(int[] phenotype) {
		for(Integer p : phenotype)
			if(this.phenotypes.containsKey(p))
				return true;
		return false;
	}
	
	public void addPhenotype(Integer phenotype, int level) {
		if(phenotype == null)return;
		this.phenotypes.put(phenotype, level);
	}
	
	public void addPhenotype(Integer phenotype) {
		this.addPhenotype(phenotype, -1);
	}
	
	public Map<Integer, Integer> getState(){
		return this.states;
	}
	
	public int getState(int state) {
		return this.states.get(state);
	}
	
	public void setState(Integer stateName, int stateValue) {
		if(stateName == null)return;
		this.states.put(stateName, stateValue);
	}
	
	/*public void removePhenotype(int phenotype) {
		this.phenotypes.remove(phenotype);
	}*/
	
	
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
	
	protected int max(int[] a) {
		int max = 0;
		for(int i : a) {
			if(i > max)
				max = i;
		}
		return max;
	}
	
	protected int min(int[] a) {
		int min = 0;
		for(int i : a) {
			if(i < min)
				min = i;
		}
		return min;
	}
	
	protected int max(int i, int j) {
		return (i > j ? i : j);
	}
	
	protected int min(int i, int j) {
		return (i < j ? i : j);
	}
	
	protected int not(int i, int k) {
		return -i + k; 
	}
	
}
