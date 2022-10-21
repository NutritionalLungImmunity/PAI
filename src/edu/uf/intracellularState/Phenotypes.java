package edu.uf.intracellularState;

public interface Phenotypes {

	public static final int RESTING = 0;
	public static final int ACTIVE = 1; // if macrophage: M1
	public static final int ALT_ACTIVE = 2; // if macrophage: M2a
	public static final int MIX_ACTIVE = 3; // if macrophage: M2b
	public static final int INACTIVE = 4; // if macrophage: M2c
	public static final int ANERGIC = 5;
	
	public static final int NFkB = 6;
	public static final int ANG_ACTIVE = 7;
	public static final int IRF3 = 8;
	public static final int APOPTOTIC = 9;
	public static final int NECROTIC = 10;
	
	public static final int STAT3 = 11;
	public static final int OPEN = 12;
	
	public static final int SECRETING = 12;
	public static final int FULL_ACTIVE = 13;
	
	public static final int IRF9 = 14;
	public static final int AKT = 15;
	
	
	
}
