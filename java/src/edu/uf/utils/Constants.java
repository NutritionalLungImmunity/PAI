package edu.uf.utils;

public class Constants {
	
	public static double D = 16;
	
	public static int TIME_STEP_SIZE = 2; // minutes
	public static int HALF_HOUR = 30; // minutes
	public static int CYT_BIND_T = 35; // minutes
	public static int HOUR = 60; // minutes
	
	public static double UNIT_T = TIME_STEP_SIZE/((double) HALF_HOUR);
	public static double STD_UNIT_T = TIME_STEP_SIZE/((double) HOUR);
	public static double REL_CYT_BIND_UNIT_T = TIME_STEP_SIZE/((double) CYT_BIND_T);
	public static double INV_UNIT_T = HALF_HOUR/((double)TIME_STEP_SIZE);
	
	public static int ITER_TO_SWELLING = 8;//118;
	public static double PR_ASPERGILLUS_CHANGE = 0.05776227;//0.003850817669777474;
	public static int ITER_TO_GERMINATE = 1;//58;
	
	public static int ITER_TO_CHANGE_STATE = 60;
	public static int ITER_TO_REST = 30*6;
	public static int ITER_TO_GROW = 30 ; //40 um every 58 min  --  https://doi.org/10.1128/msphere.00076-23
	
	public static double VOXEL_VOL = 6.4e-11; // L
	public static double VOXEL_LEN = 40; //um
	public static double HYPHAE_VOL = 1.056832e-12; // L
	public static double CONIDIA_VOL = 4.844e-14; // L
	public static double MA_VOL = 4.849048e-12; // L
	public static double SPACE_VOL = 6.4e-8; // L
	public static double SERUM_VOL = 3e-3; // L (KIND OF DUMMY) 
	
	public static double PR_BRANCH = 0.333; //  https://doi.org/10.1128/msphere.00076-23
	
	public static double TURNOVER_RATE = 0.9878452295470697;
	public static double LAC_QTTY = 5.357143e-18;//1.847e-17 * 15;// single shot! (https://doi.org/10.1038/s41598-019-49419-z) 4.3680e-17 * STD_UNIT_T;
	
	public static double TAFC_UP = 0.01245766;// -- from the same paper of Kd_LIP;  (1e-12/VOXEL_VOL) * STD_UNIT_T * 15;
	
	public static double TAFC_QTTY = 1.0000e-15 * STD_UNIT_T;
	public static double HEMOLYSIN_QTTY = 1.386667e-10 * STD_UNIT_T * 15;
	public static double MA_IL6_QTTY = 1.4615e-20 * STD_UNIT_T * 15;
	public static double MA_IL8_QTTY = 5.072776e-19 * STD_UNIT_T * 15;
	public static double MA_MCP1_QTTY = 1.757245e-20 * STD_UNIT_T * 15;
	public static double MA_MIP1B_QTTY = 1.7896e-20 * STD_UNIT_T * 15;
	public static double MA_MIP2_QTTY = 1.1061e-19 * STD_UNIT_T * 15;
	public static double MA_IL10_QTTY = 6.9735e-22 * STD_UNIT_T * 15;
	public static double MA_TNF_QTTY = 3.2179e-20 * STD_UNIT_T * 15;
	public static double MA_TGF_QTTY = 1.0119e-21 * STD_UNIT_T * 15;
	public static double MA_IL1_QTTY = 5.645925e-21 * STD_UNIT_T * 15;
	public static double GRANULE_QTTY = 1; //AU

	public static double N_IL6_QTTY = 0.005875191*MA_IL6_QTTY;
	public static double N_IL8_QTTY = 0.005875191*MA_IL8_QTTY;
	public static double N_MIP2_QTTY = 0.005875191*MA_MIP2_QTTY;
	public static double N_TNF_QTTY = 0.005875191*MA_TNF_QTTY;
	public static double N_IL1_QTTY = 0.005875191*MA_IL1_QTTY;

	public static double P_IL6_QTTY = MA_IL6_QTTY;
	public static double P_IL8_QTTY = MA_IL8_QTTY;
	public static double P_MCP1_QTTY = MA_MCP1_QTTY;
	public static double P_MIP1B_QTTY = MA_MIP1B_QTTY;
	public static double P_MIP2_QTTY = MA_MIP2_QTTY;
	public static double P_TNF_QTTY = MA_TNF_QTTY;
	public static double P_IL1_QTTY = MA_IL1_QTTY;
	
	public static double L_HEP_QTTY = 6.9e-8;//3e-7;//1.945e-10*2.5;//2.3e-8;
	
	
	//degradation rate: [Cytokine] = HALF_LIFE * [Cytokine]; HALF_LIFE=0.9768950939813351 -> 1h half-life
	public static double IL6_HALF_LIFE = 0.9451153*0.9768950939813351;
	public static double MIP1B_HALF_LIFE = 0.9451153*0.9768950939813351;
	public static double MIP2_HALF_LIFE = 0.9451153*0.9768950939813351;
	public static double IL10_HALF_LIFE = 0.9451153*0.9768950939813351;
	public static double IL8_HALF_LIFE = 0.9451153*0.9768950939813351;
	public static double MCP1_HALF_LIFE = 0.9451153*0.9768950939813351;
	public static double TNF_HALF_LIFE = 0.9451153*0.9768950939813351;
	public static double TGF_HALF_LIFE = 0.9451153*0.9768950939813351;
	public static double IL1_HALF_LIFE = 0.9451153*0.9768950939813351;
    public static double HEP_HALF_LIFE = 0.9451153*0.9768950939813351;
    public static double GM_CSF_HALF_LIFE = 0.9451153*0.9768950939813351;
    
	public static double ANTI_TNF_HALF_LIFE = 0.9998074591165111; // five days -- 10.1002/eji.1830180221
	public static double Granule_HALF_LIFE = 0.3068528; //1+log(0.5)
	
	
	public static double Kd_IL6 = 3.3e-10;
    public static double Kd_IL8 = 1.045e-9;
    public static double Kd_MCP1 = 5.686549e-10;
    public static double Kd_MIP1B = 1.8e-10;
    public static double Kd_MIP2 = 9.166739837e-11;
    public static double Kd_IL10 = 1.4e-10;
    public static double Kd_TNF = 3.26e-10;
    public static double Kd_TGF = 2.65e-11;
    public static double Kd_KC = 1.0e-9; //doi/10.1074/jbc.M112.443762
    public static double Kd_Hep = 8.55e-07; // REF 223
    public static double Kd_LIP = 7.90456448805514E-05; //2.762975e-05
    public static double Kd_HEMO = 19.987634208144584;//185.18518518518516/2 // Arbitrary Unitis x h^-1 10.1016/j.ijmm.2011.04.016
    public static double Kd_MA_IRON = 0.0020799959084752307;
    public static double Kd_Granule = 1; //AU
    public static double Kd_IL1 = 3.9e-9; //
    public static double Kd_TfR2 = 2.7e-8;
    public static double Kd_LPS = 3e-9; //~3nM
    public static double Kd_GM_CSF = 6e-11; //20-100pM
    public static double Kd_BGLUCAN = 1.0;
	
	
	public static double MA_IRON_IMPORT_RATE = 5.3333e-12/VOXEL_VOL * 15;
	public static double MA_IRON_EXPORT_RATE = 1367.3051298168639/VOXEL_VOL * 15;

	public static double DRIFT_BIAS = 1e-100;
	public static double PR_MOVE_ASP = 0.75; // DUMMY
	
	public static double IL13_Kd = -1;
	public static double IL13_HALF_LIFE = -1;
	
	
	//Average number of displaced voxels
	public static double MA_MOVE_RATE_REST = 1.44*TIME_STEP_SIZE/VOXEL_LEN * 15;
	public static double MA_MOVE_RATE_ACT = 1.44*TIME_STEP_SIZE/VOXEL_LEN * 15; //for NK 0.6-0.7 um/min (https://doi.org/10.3389/fimmu.2014.00080)
	
	
	public static double REC_BIAS = 0.9995; // DUMMY VALUE CREATED TO AVOID INFINTY LOOP!
	
	
	public static double MAX_N = 522;//*3;
	public static double MAX_MA = 209*3;
	public static double MIN_MA = 15;

	public static double PR_MA_PHAG = 0.9054552746554831;
	public static double PR_N_PHAG = 0.1472550818938948;
	public static double PR_N_HYPHAE = 0.2270815966867843;
	public static double PR_MA_HYPHAE = 0.09851340404770559;
	public static double PR_P_INT = 0.04489851158453906;
	
	
	public static int MA_MAX_CONIDIA = 18;
	public static int N_MAX_CONIDIA = 3;
	
	
	public static double PR_KILL = 0.012792139405522474 * 15;

	public static double K_M_TF_TAFC = 2.514985e-3;
	public static double K_M_TF_LAC = 2.5052031141601793e-3;


	public static double MA_INTERNAL_IRON = 1.0086e-14; // mols
	public static double CONIDIA_INIT_IRON = Kd_LIP * CONIDIA_VOL; // mols

	//probability leukocytes die in one time-step (2 min): 6 and 24 hours half-life respectively
	public static double NEUTROPHIL_HALF_LIFE = 0.05776227;//0.003850817669777474;
	public static double MA_HALF_LIFE = 0.01444057;//0.0009627044174443685;
	
	
	/* 
	 * These are phenomenological parameters of a polynome used to approximate an ODE.
     * That ODE computes the relative amounts of TfFe and TfFe2 after the reaction of Transferrin (Tf) and Iron (Fe).
     * That ODE is dimension-free and assume that both binding sites of Tf has the same afinity and that there is no cooperativity.
     * That is a simplification.
     */
	public static double P1 =  0.2734;
	public static double P2 = -1.1292;
	public static double P3 =  0.8552;
	
	
	public static double IL6_THRESHOLD = 1.372243e-10; // mols
	public static double HEP_INTERCEPT = -0.3141;
	public static double HEP_SLOPE = 0.7793;
	
	
	public static double TF_INTERCEPT = -1.194e-05;
	public static double TF_SLOPE = -5.523e-06;
	public static double THRESHOLD_LOG_HEP = -8;
	public static double THRESHOLD_HEP = Math.pow(10, THRESHOLD_LOG_HEP);

	public static double DEFAULT_APOTF_REL_CONCENTRATION = 0.4; //40%
	public static double DEFAULT_TFFE_REL_CONCENTRATION = 0.1657; //16.57%
	public static double DEFAULT_TFFE2_REL_CONCENTRATION = 0.4343; //43.43%

	public static double SCALING = 1;
	
	public static double DEFAULT_TF_CONCENTRATION = (TF_INTERCEPT + TF_SLOPE * THRESHOLD_LOG_HEP) * VOXEL_VOL * SCALING;
	public static double DEFAULT_APOTF_CONCENTRATION = DEFAULT_APOTF_REL_CONCENTRATION * DEFAULT_TF_CONCENTRATION;
	public static double DEFAULT_TFFE_CONCENTRATION = DEFAULT_TFFE_REL_CONCENTRATION * DEFAULT_TF_CONCENTRATION;
	public static double DEFAULT_TFFE2_CONCENTRATION = DEFAULT_TFFE2_REL_CONCENTRATION * DEFAULT_TF_CONCENTRATION;

	public static double RECRUITMENT_RATE_N =  4.882812e+14;
	public static double RECRUITMENT_RATE_MA = 4.882812e+14; //arbitrary unity
	
	
	
	
	
	public static int MAX_BN_ITERATIONS = 20;
	
	
	
	
	
	public static int ANTI_TNFA_REACT_TIME_UNIT = 120; //sec
    public static double K_M_ANTI_TNFA = 6.9737e-07;  // (M^-1.sec^-1)  http://www.jimmunol.org/content/162/10/6040.full//ref-list-1
    public static double ANTI_TNFA_SYSTEM_CONCENTRATION_REF = 2e-8*VOXEL_VOL;
    public static double ANTI_TNFA_SYSTEM_CONCENTRATION = ANTI_TNFA_SYSTEM_CONCENTRATION_REF; // https://doi.org/10.1016/0022-1759(95)00278-2


    public static double HEMOGLOBIN_UPTAKE_RATE = (1e-13/VOXEL_VOL) * UNIT_T; // TAFC_UP
    public static double ERYTROCYTE_HEMOGLOBIN_CONCENTRATION = 4.6875e-16; //mol
    public static int MAX_ERYTHROCYTE_VOXEL =  239;//180; 
    public static int ERYTHROCYTE_TURNOVER_RATE = 239;// RBC/time-step //ON THE AMOUNT OF BLOOD IN THE LUNGS. By YAS KUNO, 1917
    

    public static double ESTB_KM = 4e-4; //10.1128/EC.00066-07 
    public static double ESTB_HALF_LIFE = 1+Math.log(0.5)/(24.0 * HOUR/((double) TIME_STEP_SIZE));//2.9 - Advances in Enzymology and related Areas of Molecular Biology, Alton Meister, v39, 1973, p227
    public static double ESTB_SYSTEM_CONCENTRATION = 0;
    public static double ESTB_KCAT = 3096; // 10.1128/AEM.65.8.3470-3472.1999 (GENERIC FUNGI ESTERASE)

    public static double HEMOPEXIN_SYSTEM_CONCENTRATION = 0; // NOT CORRECT
    public static double HEMOPEXIN_SYSTEM_CONCENTRATION_REF = 3.1e-7 * VOXEL_VOL; // (Luis SV and 10.3181/00379727-75-18083)
    public static double HEMOPEXIN_KM = 1e-9; // Kd < 1pM -- 10.1006/abbi.1993.1014 (Kd is not Km!!!)
    public static double HEMOPEXIN_KCAT = 100;
    
    
    /* ********* NEW PARAMETERS ********** */
	
	
    
    public static double HEME_SYSTEM_CONCENTRATION = 9.934e-5 * VOXEL_VOL; //from Borna's data
    public static double HEME_TURNOVER_RATE = 3e-2; //~fit to Borna's data
    
    public static double HEMOLYSIS_RATE = 0.1674/30*2.64; //first approx. -- Use Borna's data to fit
    public static double Kd_HP = 1;//3.657392737e-6; //10.1111/j.1365-2567.2004.02071.x
    public static double Kd_HPX = 9.74317e-7; // https://doi.org/10.1189/jlb.1208742
    
    public static double Kd_Heme = 3e-5;//9.74317e-5; //DOI:10.1074/jbc.M610737200
    
    public static double KM_HP = 2e-4; // same as PHX (DUMMY)
    public static double KM_HPX = 2e-4; //??? (it was abs) https://doi.org/10.1101/2020.04.16.044321;
    public static double KCAT_HPX = 120;
    
    public static double K_HB = 2*0.085976/30.0; //step^-1 Borna's data Lung Heme/hemoglobin Figure
    
    public static double HEME_UP = 0.0015625; //https://doi.org/10.1128/ec.00414-07 ---10.1099/mic.0.26108-0 (Haemin C. albicans - Calculate based on 40 min delta) per min therefore timse 2
    
    public static double DEFAULT_HPX_CONCENTRATION = 6.24e-16; //BORNA UNPUBLISHED RESULTS HPX/BAL (ALREADY MUL BY VOXEL VOL)
    public static double DEFAULT_HP_CONCENTRATION = 0.015*6.24e-16;  //(ALREADY MUL BY VOXEL VOL)
    
    
    public static double L_HP_QTTY = 10*4e-8;//1.2e-5;//3.443492e-05; //ROUGH APPROX based on V.max. DOI:10.1016/j.endinu.2018.07.008
    public static double L_HPX_QTTY = 10*9.74e-6*3;//1.8706e-14;//1.948634e-05; //ROUGH APPROX based on normal levels of hpx in mice: DOI:10.1016/0192-0561(84)90022-5
    public static double L_REST_HPX_QTTY = 9.74e-6*3; //BORNA UNPUBLISHED RESULTS HPX/BAL
    public static double L_REST_HP_QTTY = 4e-8; //DOI:10.17221/8770-VETMED corrected by BORNA UNPUBLISH. 
    
    
    
    
    public static double REDUCTIVE_IRON_ASSIMILATION_RATE = 5.208333e-08; //TAFC/10000 //DUMMY
    public static double KM_IRON = 1.428571e-05; //10.1128/jb.169.8.3664-3668.1987
    public static double KCAT_IRON = 1; //10.1128/jb.169.8.3664-3668.1987
    public static double MIN_FREE_IRON = 1e-9*VOXEL_VOL; //1e-17???
    public static double GLU_UPTAKE_RATE = 5.208333e-07; //TAFC/1000 //DUMMY
    public static double Kd_GLU = 5e-3*VOXEL_VOL; //DUMMY
    
    
    public static double INIT_GLU = 1e-5*VOXEL_VOL; //DUMMY
    public static double INIT_TIN_PROTOPORPHYRIN = 1e-5*VOXEL_VOL; //DUMMY
    public static double INIT_HEME = 0;
    public static double INIT_IRON = MIN_FREE_IRON;
    
    public static double Kd_GROW = Kd_LIP / 10.0;
    
    
    
    
    /** COVID-19 PARAMETERS**/
    
    public static double SarsCoV2_HALF_LIFE = 0.9959768; //(t1/2 influenza 10.1128/JVI.01623-05)      (0.99792 //https://doi.org/10.1098/rsos.210787)
    public static double SarsCoV2_REP_RATE = 0.009210062*2.5 * 15; //https://doi.org/10.1038/s41586-020-2708-8
    public static double MAX_VIRAL_LOAD = 3.824892e-20;//1e4*log(10) molecules (10-100 initial number https://doi.org/10.1073/pnas.2024815118)
    public static double SarsCoV2_UPTAKE_QTTY = 1.66113e-21 * 15;//a thousand molecules
    public static double DAMP_HALF_LIFE = 0.9768950939813351;
    public static double SAMP_HALF_LIFE = 0.9768950939813351;
    public static double VEGF_HALF_LIFE = 0.9768950939813351;
    public static double IFN1_HALF_LIFE = 0.9768950939813351;
    public static double IFN_I_HALF_LIFE = 0.9768950939813351;
    public static double MA_IFN_QTTY =  4.983902e-22 * 15;//  1.495171e-21; // preliminary -- PCA with 8 eigen values and TNF alone. 
    public static double DC_IFN_QTTY = 4.983902e-22 * 15;//https://doi.org/10.1182/blood-2006-05-023770     1.495171e-21; //5.922133e-19;
    public static double VEGF_QTTY = 5.280638e-22 * 15; //10.3389/fonc.2013.00196
    public static double Kd_IFNG = 3.0e-9; //median Ifnar2-EC (https://doi.org/10.1016/j.jmb.2006.11.053)
    public static double Kd_SAMP = 1; // 1 AU of SAMP has Kd of 1 by definition
    public static double Kd_DAMP = 1; // 1 AU of DAMP has Kd of 1 by definition
    public static double Kd_SarsCoV2 = 1.58e-9; //10.1038/s41467-021-21118-2
    public static double Kd_sIL6R = 4.22e-7; //ROUGH APROXX (https://doi.org/10.1186/s12860-020-00317-7) 
    public static double Kd_VEGF = 5.75e-10;
    public static double PR_ROS = Math.exp(-1);
    public static double sIL6R_QTTY = MA_IL6_QTTY;
    public static double DAMP_QTTY = 1; //Based on Cyt_QTTY/Kd_Cyt average.
    public static double SAMP_QTTY = 1; //Based on Cyt_QTTY/Kd_Cyt average.
    //public static double PR_INF_DIE = 2.02e-07; //X https://doi.org/10.1098/rsos.210787 -- 1-exp(-1.01e-7*2)
    public static double PR_NK_KILL = Math.exp(-1);//0.07675284; //DUMMY
    public static double MAX_NK = 160; //4% of 4e6 in 1 mL
    public static double NK_HALF_LIFE = 0.002062938; //0.0001375292; //~7 days (10.1111/j.1365-2567.2007.02573.x)
    public static double MIN_NK = 250; //https://doi.org/10.7554/eLife.74623
    public static double N_DEFENSIN_QTTY = 2.834008e-16 * 15;// reference 81 -- poor estimate
    public static double DEFENSIN_Kd = 4.547e-7;//https://doi.org/10.4049/jimmunol.0804049  //1.46e-7;//10.1016/j.jmb.2021.167225
    public static double DEFENSIN_RESTING_CONCENTRATION = 7.142857e-09;// * SERUM_VOL; //PMID: 8340706
    public static double Kd_H2O2 = 125e-6; //DOI:10.1167/ iovs.15-19039
    public static double H2O2_QTTY = 2.38e-15 * 15; //DOI:10.1016/0014-5793(94)80241-6
    public static double H2O2_HALF_LIFE = 1.0+Math.log(0.5)/7.5; // DOI:10.1159/000276558
    public static double VIRAL_LAC_Kd = 2.980226e-07; //https://doi.org/10.1086/343809
    
    //public static double ALBUMIN_MAX_GROWTH = 0.5;
    //public static double ALBUMIN_Kd = 0.01;
    public static double Kd_NET_PNEU = 3.2425e10;
    public static double NET_HALF_LIFE = 0.007701635; //10.1073/pnas.0909927107
    public static double TRANEXAMIC_ACID_Kd = 0.001;
    public static double TRANEXAMIC_ACID_HALF_LIFE = 0.9964454; //2-11 hours //https://www.ncbi.nlm.nih.gov/books/NBK532909/#:~:text=The%20half%2Dlife%20of%20TXA,hours%20after%20the%20initial%20dose.
    //public static double COAGULATION_RATE_RESTING_LEVEL = 0.5;
    public static double PR_NEUT_APOPT = 0.7;
    public static double HPX_INIT_QTTY = 6.24e-16; //BORNA UNPUBLISHED RESULTS HPX/BAL (ALREADY MUL BY VOXEL VOL);
    //public static double PR_COAGULATION = 1; //13 second to start!!!
    public static double PR_COAGULUM_BREAK = 0.5;
    public static double GROWTH_RATE_EPI = 120; //~25% (inverse) -- https://doi.org/10.1038/s41598-018-33902-0
    public static double GROWTH_RATE_FREE = 30; // https://doi.org/10.1128/msphere.00076-23, https://doi.org/10.3389/fmicb.2019.01919
    public static double GERM_RATE_EPI = 0.05776227;
    public static double GERM_RATE_FREE = 0.1732868; // https://doi.org/10.1128/jcm.39.2.478-484.2001
    
    
    public static double NITROGEN_THRESHOLD = 1.2e-9; // g * Q^-1 * septae^-1 -- 10.3390/su10093296; 10.17226/1349 (chapter 6)
    public static double PROTEASE_QTTY = -1;
    public static double PROTEASE_Kcat_KM = -1;
    public static double Kd_PROTEASE = -1;
    
    public static double GLUTAMINE_PER_MUCIN;
    public static double GLUTAMINE_PER_ALBUMIN = 20; //HUMAN
    public static double GLUTAMINE_UP_RATE = -1;
    public static double GLUTAMIN_INIT_QTTY = 4.16e-14;//10.3390/nu10111564
    
    public static double Kd_NET_KLEB = -1;
    public static double NET_ENTRAPMENT_THRESHOLD = 0.25; //2 half-lives? (no ref.)
    
    public static double ALBUMIN_INIT_QTTY = 3.92e-14;
    public static double MUCIN_INIT_QTTY = 1;
    
    public static double INTERNAL_IRON_KM = 4.742739e-05; //0.6 * Kd_LIP -- fit to kd LIP
    public static double INTERNAL_HEME_KM = 9.790121e-07; //Angelica's paper
    public static double HEME_INIT_QTTY = 1.034651e-18; //INTERNAL_HEME_KM * HYPHAE_VOL
    public static double HEME_QTTY = 1.034651e-18*200; //Angelica's paper (Fig 5C)
    
    public static double PR_NET_KILL_EPI  = 0.0512749; //10.1371/journal.pone.0032366 (Fig 2B)
    
    public static double PR_ASP_KILL_EPI = 0.02508277; //10.1371/journal.pone.0036952 -- 1:1 cell/C. albicans -> 30 cytotox in 24h
    
    public static double NET_COUNTER_INHIBITION = 0.5;
    
    public static double DNAse_KCAT = 0.007701635; //same as NET_HALF_LIFE
    public static double DNAse_HALF_LIFE = 0.9451153*0.9768950939813351;;
    
    
    public static double TRANEXAMIC_ACID_THRESHOLD = 6.369427e-07; //~hundred times less than the dose given in this paper https://doi.org/10.1016/j.jss.2017.03.031 (10 mg/kg)
    public static double TRANEXAMIC_ACID_LIFE_SPAN = 12*30; //2-11 hours //https://www.ncbi.nlm.nih.gov/books/NBK532909/#:~:text=The%20half%2Dlife%20of%20TXA,hours%20after%20the%20initial%20dose.
    
    
    
    public static double IL17_QTTY = -1;
    public static double DC_HALF_LIFE = -1;
    public static double MA_IFN_I_QTTY = -1;
    public static double IFN_II_QTTY = -1;
    public static double MA_IFN_III_QTTY = -1;
    public static double MA_IL23_QTTY = -1;
    public static double Kd_IFN_LBD = -1;
    public static double Kd_IL17 = -1;
    public static double Kd_IFN_I = -1;
    public static double Kd_IFNg = -1;
    public static double Kd_IL23 = -1;
    public static double LPC2_QTTY = -1;
    
    
    public static double Kd_Adenosine = -1;
    public static double b_DEFENSIN_QTTY = -1;
    public static double Kd_b_DEFENSIN = -1;
    public static double K_M_TF_YER = -1;
    public static double YER_QTTY = -1;
    public static double YER_UP_RATE = -1;
    public static double K_M_TF_AERO = -1;
    public static double AERO_QTTY = -1;
    public static double AERO_UP_RATE = -1;
    public static double K_M_TF_SAL = -1;
    public static double SAL_QTTY = -1;
    public static double SAL_UP_RATE = -1;
    public static double K_M_TF_ENT = -1;
    public static double ENT_QTTY = -1;
    public static double ENT_UP_RATE = -1;
    public static double K_M_LPC2_ENT = -1;
    
    public static double  Kd_IL4  = 3.26e-10; //THIS VALUE IS DUMMY (come from TNF)
    public static double IL4_QTTY = 3.2179e-20 * STD_UNIT_T * 15;  //THIS VALUE IS DUMMY (come from TNF)
    
    
    
    /*
     * CHANGES FROM GANLIN MODEL
     * TAFC * 15
     * REC_RATE * 2
     * MAX_MA * 3
     * CYT_HALF_LIFE -- ON PAPER
     */
    
    /*public static double PR_INT_NK_P = 0.05; //DUMMY
    public static double IFN1_QTTY = 4.95e-19; //DUMMY: BASED ON Kd
    public static double IFN1_INHIBITION = 0.8;//DUMMY
    public static double Kd_IFN1 = 3.34e-9; //https://doi.org/10.1074/jbc.M116.773788 //CHECK BETTER
    public static double DEFAULT_VIRAL_REPLICATION_RATE = 1.01;//DUMMY
    public static double IFN1_HALF_LIFE = 1+Math.log(0.5)/(1 * HOUR/((double) TIME_STEP_SIZE));//1
    public static double MAX_VIRAL_LOAD = 1e-10;//DUMMY
    public static double VIRAL_UPTAKE_RATE = 0.01;//DUMMY
    public static double Kd_Covid = 1e-20;//DUMMY
    public static double MAX_NK = 21; //DUMMY;
    
    public static double PR_DEPLETION = 0.0;*/
    
}
	
