package edu.uf.intracellularState;

public class MacrophageFactory {

	private static String model;
	public static final String FM_NETWORK = "FMacrophageBooleanNetwork";
	
	public static void setModel(String model) {
		MacrophageFactory.model = model;
	}
	
	public static BooleanNetwork createBooleanNetwork() {
		if(model == FM_NETWORK)
			return new FMacrophageBooleanNetwork();
		return null;
	}
	
}
