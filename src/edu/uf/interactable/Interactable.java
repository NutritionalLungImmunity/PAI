package edu.uf.interactable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Interactable implements Binder{
	
	private Set<String> negativeInteractList= new HashSet<>();
	private int callCounter = 0;
	//private Set<Molecule> interactingMolecules = null;
	private List<Interactable> removeList = new ArrayList<>();
	
	public abstract boolean isTime();
	

	/**
	 * This method is part of a duo that works in tandem and recursively 
	 * (see the "templateInteract" method). The "interact" method checks if the 
	 * interactable object type is in the local negative list. I.e., the local list 
	 * of objects whose interactions are refused. If it is, the method does nothing 
	 * and returns. This method also keeps track of recursive calls, and if the recursive 
	 * call counter is greater than zero, it adds the interactable object type to the 
	 * local negative list and returns. If none of the previous two conditions are met, 
	 * this method will increment the recursive call counter and call the "templateInteract" 
	 * method.  
	 * <br/><br/>
	 * The negative list is local and contains all the object types (I.e., classes) 
	 * that the current object type does not interact with. For example, if a macrophage 
	 * does not interact with a type II pneumocyte, the negative lists of macrophages 
	 * will contain type II pneumocytes.
	 * <br/><br/>
	 * The templateInteract method will provide code to interact the interactable 
	 * object with the current object. Arguments x, y, and z are auxiliary values 
	 * for interactions involving molecules or other spacial objects.
	 * @param interactable
	 * @param x axis position in the grid
	 * @param y axis position in the grid
	 * @param z axis position in the grid
	 * @return
	 */
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
	
	/**
	 * This method gives names to object types (I.e., class). The intended implementation 
	 * of this method is to return a static string. This method is important for the 
	 * "interact" method to determine object types that should be included in the local 
	 * negative lists. I.e., local lists of objects that do not interact. 
	 * @return the name of a class
	 */
	public  abstract String getName();
	
	/**
	 * This method returns an object-specific ID.
	 * @return an object-specific ID.
	 */
	public abstract int getId();
	
	/**
	 * This method is part of a duo that works in tandem and recursively 
	 * (see the "interact" method). If the interactable object passes the 
	 * tests, the "interact" method calls the "templateInteract." Then, one 
	 * of two things may happen: (1) the "templateInteract" method may contain 
	 * code to handle the interaction between the current object and objects of 
	 * the interactable type. In that case, the interaction is handled, and the 
	 * "templateInteract" returns. (2) The "templateInteract" does not have code 
	 * to handle the interaction. In that case, the "templateInteract" will call 
	 * the "interactable.interact" method, passing the current object as an 
	 * argument and the whole process repeats. Afterward, there is a full recursion 
	 * if the "interactable.templateInteract" method falls into option number (2). 
	 * However, the "interact" recursive call counter will place the interactable 
	 * object type into the local negative list and return.
	 * @param interactable
	 * @param x axis position in the grid
	 * @param y axis position in the grid
	 * @param z axis position in the grid
	 * @return
	 */
	protected abstract boolean templateInteract(Interactable interactable, int x, int y, int z);
	
	
	
}
