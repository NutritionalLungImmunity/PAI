package edu.uf.intracellularState;

import edu.uf.intracellularState.Klebsiella.DCKlebsiella;
import edu.uf.intracellularState.Klebsiella.GDTKlebsiella;
import edu.uf.intracellularState.Klebsiella.MacrophageKlebsiella;
import edu.uf.intracellularState.Klebsiella.NKKlebsiella;
import edu.uf.intracellularState.Klebsiella.PneumocyteIIKlebsiella;
import edu.uf.intracellularState.Klebsiella.PneumocyteIKlebsiella;

public class IntracellularModelFactory {

	public static final String ASPERGILLUS_MACROPHAGE = AspergillusMacrophage.name;
	public static final String NEUTROPHIL_STATE_MODEL = NeutrophilStateModel.name;
	public static final String NEUTROPHIL_STD_MODEL = NeutrophilNetwork.name;
	public static final String PNEUMOCYTE_STATE_MODEL = PneumocyteStateModel.name;
	public static final String GDT_KLEBSIELLA_MODEL= GDTKlebsiella.name;
	public static final String MACROPHAGE_KLEBSIELLA_MODEL= MacrophageKlebsiella.name;
	public static final String DC_KLEBSIELLA_MODEL= DCKlebsiella.name;
	public static final String PNEUMOCYTE_I_KLEBSIELLA_MODEL= PneumocyteIKlebsiella.name;
	public static final String PNEUMOCYTE_II_KLEBSIELLA_MODEL= PneumocyteIIKlebsiella.name;
	public static final String NK_KLEBSIELLA_MODEL= NKKlebsiella.name;
	
	public static IntracellularModel createBooleanNetwork(String network) {
		if(network.contentEquals(ASPERGILLUS_MACROPHAGE))
			return new AspergillusMacrophage();
		else if (network.contentEquals(NEUTROPHIL_STATE_MODEL))
			return new NeutrophilStateModel();
		else if(network.contentEquals(NEUTROPHIL_STD_MODEL))
			return new NeutrophilNetwork();
		else if(network.contentEquals(PNEUMOCYTE_STATE_MODEL))
			return new PneumocyteStateModel();
		else if(network.contentEquals(GDT_KLEBSIELLA_MODEL))
			return new GDTKlebsiella();
		else if(network.contentEquals(MACROPHAGE_KLEBSIELLA_MODEL))
			return new MacrophageKlebsiella();
		else if(network.contentEquals(DC_KLEBSIELLA_MODEL))
			return new DCKlebsiella();
		else if(network.contentEquals(PNEUMOCYTE_I_KLEBSIELLA_MODEL))
			return new PneumocyteIKlebsiella();
		else if(network.contentEquals(PNEUMOCYTE_II_KLEBSIELLA_MODEL))
			return new PneumocyteIIKlebsiella();
		else if(network.contentEquals(NK_KLEBSIELLA_MODEL))
			return new NKKlebsiella();
		return null;
	}
	
}
