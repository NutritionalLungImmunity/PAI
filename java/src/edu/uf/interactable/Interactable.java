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
	 * Handles the interaction between this agent and another {@link Interactable} within the simulation grid
	 * at the specified (x, y, z) location. This method is part of a two-method system, working recursively
	 * in tandem with {@code templateInteract}.
	 * 
	 * <p>The method first checks whether the type of the given interactable is listed in this object's local 
	 * "negative list" — a list of interactable types that this object does not interact with. If the type is 
	 * present, the method exits without performing any action.</p>
	 * 
	 * <p>If the recursive call counter is greater than zero, the interactable’s type is added to the negative list 
	 * (to prevent future unnecessary interaction attempts), and the method returns.</p>
	 * 
	 * <p>If neither condition applies, the recursive call counter is incremented, and {@code templateInteract} is invoked
	 * to perform the actual interaction logic. Arguments {@code x}, {@code y}, and {@code z} are auxiliary spatial 
	 * parameters used in location-dependent interactions (e.g., with molecules).</p>
	 * 
	 * <p>This method is symmetric, meaning the interaction is bidirectional:
	 * {@code a.interact(b, x, y, z)} is equivalent to {@code b.interact(a, x, y, z)}.</p>
	 * 
	 * <p>The local negative list is specific to each interactable type. For instance, if macrophages do not interact 
	 * with type II pneumocytes, the macrophage class will list type II pneumocytes in its negative list.</p>
	 * 
	 * @param interactable the {@link Interactable} object to attempt interaction with
	 * @param x the x-coordinate in the simulation grid
	 * @param y the y-coordinate in the simulation grid
	 * @param z the z-coordinate in the simulation grid
	 * @return {@code true} if the interaction was performed; {@code false} otherwise
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
	 * Returns the name of the object type (i.e., class). This method is intended to be implemented 
	 * by each subclass to return a static string identifying its type.
	 * 
	 * <p>This method is primarily used by the {@code interact} mechanism to determine whether 
	 * an object’s type should be added to the local negative list — a list of object types 
	 * that do not interact with the current object.</p>
	 *
	 * @return the name of the object's class or type
	 */
	public  abstract String getName();
	
	/**
	 * Returns a unique identifier specific to this object instance.
	 *
	 * @return the object-specific ID
	 */
	public abstract int getId();
	
	/**
	 * Defines the specific interaction behavior between this agent and another {@link Interactable}
	 * at the given (x, y, z) location. This method works in tandem with the {@code interact} method 
	 * as part of a recursive interaction system.
	 * 
	 * <p>If the {@code interactable} object passes the checks in {@code interact}, then 
	 * {@code interact} invokes {@code templateInteract}. Two outcomes are possible:</p>
	 *
	 * <ol>
	 *   <li>
	 *     {@code templateInteract} contains logic to handle the interaction between this object and 
	 *     the given {@code interactable} type. In this case, the interaction is processed, and the 
	 *     method returns.
	 *   </li>
	 *   <li>
	 *     {@code templateInteract} does not handle the interaction (e.g., the type is not supported 
	 *     directly). In this case, it calls {@code interactable.interact(this, x, y, z)} to give the 
	 *     other object a chance to handle the interaction.
	 *   </li>
	 * </ol>
	 *
	 * <p>If that recursive call leads to another {@code templateInteract} that also falls into 
	 * case (2), then a full recursion occurs. However, the recursive call counter in {@code interact} 
	 * ensures termination by adding the calling object’s type to the local negative list 
	 * (preventing further attempts) and returning.</p>
	 *
	 * @param interactable the other {@link Interactable} involved in the interaction
	 * @param x the x-coordinate in the simulation grid
	 * @param y the y-coordinate in the simulation grid
	 * @param z the z-coordinate in the simulation grid
	 * @return {@code true} if the interaction was handled in this call; {@code false} otherwise
	 */
	protected abstract boolean templateInteract(Interactable interactable, int x, int y, int z);
	
	
	
}
