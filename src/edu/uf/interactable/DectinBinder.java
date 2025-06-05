package edu.uf.interactable;

import edu.uf.utils.Id;

/**
 * This class is a wildcard non-interactable Binder used to implicitly represent any molecule that 
 * binds Dectin. 
 * @author henriquedeassis
 *
 */
public class DectinBinder implements Binder{
	private static int id;
	private static DectinBinder binder;
	static {
		id = Id.getId();
	}
	
	public static DectinBinder getBinder() {
		if(binder == null) {
			binder = new DectinBinder();
		}
		return binder;
	}
	
	@Override
	public int getInteractionId() {
		return id;
	}
}
