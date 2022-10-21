package edu.uf.intracellularState;

import java.util.HashSet;
import java.util.Set;

public abstract class EukaryoteSignalingNetwork extends BooleanNetwork{

	public static final int NUM_RECEPTORS = 33;
	
	public static int GM_CSF_e = -1;
	public static int LPS_e = -1;
	public static int IC_e = -1;
	public static int IL4_e = -1;
	public static int IFNG_e = -1;
	public static int IL1B_e = -1;
	public static int IL10_e = -1;
	public static int TNFa_e = -1;
	public static int B_GLUC_e = -1;
	public static int TGFb_e = -1;
	public static int VEGF_e = -1;
	public static int PDGFB_e = -1;
	public static int BMP2_e = -1;
	public static int BMP9_e = -1;
	public static int Hypoxia_e = -1;
	public static int Shear_Stress_e = -1;
	public static int IL6_e = -1;
	public static int sIL6R_e = -1;
	public static int AngII_e = -1;
	public static int SAMP_e = -1;
	public static int DAMP_e = -1;
	public static int ITGB_e = -1;
	public static int SARS_CoV2_e = -1;
	public static int MIP2_e = -1;
	public static int KC_e = -1;
	public static int Hep_e = -1;
	public static int Heme_e = -1;
	public static int Hpx_e = -1;
	public static int ROS_e = -1;
	public static int VIRUS_e = -1;
	public static int INF_CELL_e = -1;
	public static int IFN_e = -1;
	public static Set<Integer> TLR4_o = new HashSet<>();
	
	
}
