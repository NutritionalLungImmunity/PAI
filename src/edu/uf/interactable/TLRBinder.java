package edu.uf.interactable;

import edu.uf.utils.Id;

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
