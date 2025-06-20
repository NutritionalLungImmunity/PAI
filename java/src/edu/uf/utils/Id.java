package edu.uf.utils;

public class Id {
	private static int ID = 0;
	
	/**
	 * Generates a unique, sequential, non-repeating ID.
	 *
	 * <p>This method returns a globally incrementing identifier that is independent of IDs generated by 
	 * {@code getMoleculeId()}. Each call to this method returns a unique value greater than the previous one.</p>
	 *
	 * @return a unique sequential ID
	 */
	public static int getId() {
		Id.ID = Id.ID + 1;
		return Id.ID; 
	}
}
