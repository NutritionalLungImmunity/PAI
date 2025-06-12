package edu.uf.intracellularState;

public class IntracellularModelFactory {

	public static final String ASPERGILLUS_MACROPHAGE = AspergillusMacrophage.name;
	public static final String NEUTROPHIL_STATE_MODEL = NeutrophilStateModel.name;
	public static final String NEUTROPHIL_STD_MODEL = NeutrophilNetwork.name;
	public static final String PNEUMOCYTE_STATE_MODEL = PneumocyteStateModel.name;
	
	/**
	 * Creates and returns an {@link IntracellularModel} object based on the specified implementation name.
	 *
	 * <p>The {@code network} parameter identifies the desired implementation. Valid values are defined 
	 * as constants within this class.</p>
	 *
	 * @param network the name of the intracellular model implementation to instantiate
	 * @return an {@link IntracellularModel} corresponding to the given name if valid; {@code null} otherwise
	 */
	public static IntracellularModel createBooleanNetwork(String network) {
		if(network.contentEquals(ASPERGILLUS_MACROPHAGE))
			return new AspergillusMacrophage();
		else if (network.contentEquals(NEUTROPHIL_STATE_MODEL))
			return new NeutrophilStateModel();
		else if(network.contentEquals(NEUTROPHIL_STD_MODEL))
			return new NeutrophilNetwork();
		else if(network.contentEquals(PNEUMOCYTE_STATE_MODEL))
			return new PneumocyteStateModel();
		return null;
	}
	
}
