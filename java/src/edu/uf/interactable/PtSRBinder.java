package edu.uf.interactable;

import edu.uf.utils.Id;

/**
 * This class is a wildcard non-interactable Binder used to implicitly represent any molecule that 
 * binds Phosphatidil-Serine (PtSR). 
 * @author henriquedeassis
 *
 */
public class PtSRBinder implements Binder{
	private static int id;
	private static PtSRBinder binder;
	static {
		id = Id.getId();
	}
	
	public static PtSRBinder getBinder() {
		if(binder == null) {
			binder = new PtSRBinder();
		}
		return binder;
	}
	
	@Override
	public int getInteractionId() {
		return id;
	}
}
