package edu.uf.intracellularState;

import java.util.List;
import java.util.Set;

public abstract class BooleanNetwork {

	private int bnIteration;
	protected int[] booleanNetwork;
	protected int[] inputs;
	
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
	
	public abstract void processBooleanNetwork();
	
	protected int e(int[] bn, int i) {
		if(i<0)return 0;
		return bn[i];
	}
	
	protected void r(int[] bn, int i) {
		if(i<0)return;
		bn[i] = 0;
	}
	
	protected int o(int[] bn, Set<Integer> idxs) {
		for(int i : idxs) 
			if(bn[i] > 0)return bn[i];
		return 0;
	}
	
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
