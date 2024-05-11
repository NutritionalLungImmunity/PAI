package edu.uf.interactable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Interactable implements Binder{
	
	private Set<String> negativeInteractList= new HashSet<>();
	private int callCounter = 0;
	private Set<Molecule> interactingMolecules = null;
	private List<Interactable> removeList = new ArrayList<>();
	
	public abstract boolean isTime();
	

	public boolean interact(Interactable interactable, int x, int y, int z) {
        if(this.negativeInteractList.contains(interactable.getName())) {
        	return false; 
        }
        if(interactable.callCounter > 0) {
			this.negativeInteractList.add(interactable.getName());
			this.removeList.add(interactable);
			interactable.callCounter = 0;
			this.callCounter = 0;
			return false;
		}
        interactable.callCounter++;
        boolean r = this.templateInteract(interactable, x, y, z);
        interactable.callCounter = 0;
        this.callCounter = 0;
        return r;
	}
	
	public  abstract String getName();
	public abstract int getId();
	protected abstract boolean templateInteract(Interactable interactable, int x, int y, int z);
	
	
	
}
