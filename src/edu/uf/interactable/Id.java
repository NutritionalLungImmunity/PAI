package edu.uf.interactable;

public class Id {
	private static int ID = 0;
	
	public static int getId() {
		Id.ID = Id.ID + 1;
		return Id.ID; 
	}
		        		
}
