package edu.uf.interactable;

import edu.uf.utils.Id;

/**
 * This class is a wildcard non-interactable Binder used to implicitly represent any molecule that 
 * binds TLR, especially TLR4 (e.g., LPS - although currently there is an LPS Molecule class). 
 * @author henriquedeassis
 *
 */
public class TLRBinder implements Binder{
	private static int id;
	private static TLRBinder binder;
	static {
		id = Id.getMoleculeId();
	}
	
	public static TLRBinder getBinder() {
		if(binder == null) {
			binder = new TLRBinder();
		}
		return binder;
	}
	
	@Override
	public int getInteractionId() {
		return id;
	}
}
