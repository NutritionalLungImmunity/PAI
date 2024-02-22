package edu.uf.intracellularState;

public class NeutrophilFactory {

	private static String model;
	public static final String N_NETWORK = "NeutrophilNetwork";
	public static final String STATE_MODEL = "NeutrophilStateModel";
	
	public static void setModel(String model) {
		NeutrophilFactory.model = model;
	}
	
	public static BooleanNetwork createBooleanNetwork() {
		if(model == N_NETWORK)
			return new NeutrophilNetwork();
		else if(model == STATE_MODEL)
			return new NeutrophilStateModel();
		return null;
	}
	
}