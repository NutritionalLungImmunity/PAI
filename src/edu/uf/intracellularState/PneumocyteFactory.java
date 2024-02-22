package edu.uf.intracellularState;

public class PneumocyteFactory {

	private static String model;
	
	public static final String STATE_MODEL = "PneumocyteStateModel";
	
	public static void setModel(String model) {
		PneumocyteFactory.model = model;
	}
	
	public static BooleanNetwork createBooleanNetwork() {
		if(model == STATE_MODEL)
			return new PneumocyteStateModel();
		return null;
	}
	
}