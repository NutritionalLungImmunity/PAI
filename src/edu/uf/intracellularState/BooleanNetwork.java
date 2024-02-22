package edu.uf.intracellularState;

import java.util.HashMap;
import java.util.Map;

import edu.uf.interactable.Binder;

public abstract class BooleanNetwork {

	private int bnIteration;
	protected int[] booleanNetwork;
	protected int[] inputs;
	private Map<Integer, Integer> phenotypes;
	public static final int NUM_RECEPTORS = 100;
	
	public BooleanNetwork() {
		this.phenotypes = new HashMap<>();
		this.inputs = new int[NUM_RECEPTORS];
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
	
	public Map<Integer, Integer> getPhenotype(){
		return this.phenotypes;
	}
	
	public void clearPhenotype() {
		this.phenotypes.clear();
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
	
	protected int or(int[] a) {
		int max = 0;
		for(int i : a) {
			if(i > max)
				max = i;
		}
		return max;
	}
	
	protected int and(int[] a) {
		int min = 0;
		for(int i : a) {
			if(i < min)
				min = i;
		}
		return min;
	}
	
	protected int or(int i, int j) {
		return (i > j ? i : j);
	}
	
	protected int and(int i, int j) {
		return (i < j ? i : j);
	}
	
	protected int not(int i, int k) {
		return -i + k; 
	}
	
}
