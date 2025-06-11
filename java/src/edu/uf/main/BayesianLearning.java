/*package edu.uf.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import edu.uf.Diffusion.Diffuse;
import edu.uf.Diffusion.FADIPeriodic;
import edu.uf.compartments.Quadrant;
import edu.uf.compartments.Recruiter;
import edu.uf.compartments.Voxel;
import edu.uf.interactable.Afumigatus;
import edu.uf.interactable.invitrogrowth.InVitroRun;
import edu.uf.main.initialize.bayesianLearning.InitializeLearnCytokinesSecretion;
import edu.uf.main.print.BayesianLearning.PrintLearningCytokines;
import edu.uf.main.print.BayesianLearning.PrintNull;
import edu.uf.main.run.Run;
import edu.uf.main.run.RunSingleThread;
import edu.uf.utils.Constants;

public class BayesianLearning {
	
	private static void pilotLearningCytokinesSecretion(String[] args) throws InterruptedException {
		InitializeLearnCytokinesSecretion initialize = new InitializeLearnCytokinesSecretion();
		PrintLearningCytokines printStat = new PrintLearningCytokines();
		Run run = new RunSingleThread();
		
		//args[0] kind of cell
		//args[1] kind of ag
		//args[2] qtty of cell
		
		//args[3] CELL_IL6_QTTY [or NA]
		//args[4] CELL_IL10_QTTY [or NA]
		//args[5] CELL_MIP1B_QTTY [or NA]
		//args[6] CELL_MIP2_QTTY [or NA]
		//args[7] CELL_TNF_QTTY [or NA]
		//args[8] CELL_TGF_QTTY [or NA]
		
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        Constants.MA_HALF_LIFE = 0.0001375292; //seven days
        /*Constants.IL6_HALF_LIFE = 1;
        Constants.IL10_HALF_LIFE = 1;
        Constants.MIP1B_HALF_LIFE = 1;
        Constants.MIP2_HALF_LIFE = 1;
        Constants.TNF_HALF_LIFE = 1;
        Constants.TGF_HALF_LIFE = 1;*/
        /*Constants.TURNOVER_RATE = 0;
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE); 
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        
        
		Constants.MA_IL10_QTTY = Double.parseDouble(args[0]);
		Constants.MA_TNF_QTTY = Double.parseDouble(args[1]);
		
		Constants.TURNOVER_RATE = 0;
		
		initialize.setQuadrant(grid, xbin, ybin, zbin);
        

        Recruiter[] recruiters = new Recruiter[0];
		
		if(args[13].contentEquals("Aspergillus")){
			//System.out.println("pre-running ...");
			initialize.infect(Integer.parseInt(args[14]), grid, xbin, ybin, zbin, Integer.parseInt(args[15]), Constants.CONIDIA_INIT_IRON, -1, false);
			
			if(args[16].contentEquals("Germinate")) {
				int germinationIter = Integer.parseInt(args[17]);
				PrintNull printNull = new PrintNull();
				run.run(
						germinationIter,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
						xbin, 
						ybin, 
						zbin, 
						grid, 
	        			quadrants, 
	        			recruiters,
	        			false,
	        			null, //new File("/Users/henriquedeassis/Documents/Projects/COVID19/data/newBaseModel.tsv"),
	        			-1,
	        			printNull
				);
				Constants.ITER_TO_GROW = 1000000000;
				Constants.ITER_TO_GERMINATE = 1000000000;
				Constants.PR_ASPERGILLUS_CHANGE = 0;
			}
		}
		
		if(args[3].contentEquals("LPS")){
			initialize.initializeLPS(xbin, ybin, zbin, Double.parseDouble(args[4]));
		}
		
		if(args[5].contentEquals("GM_CSF")){
			//initialize.initializeGMCSF(xbin, ybin, zbin, Double.parseDouble(args[29]));
		}
		
		if(args[7].contentEquals("BGlucan")){
			initialize.initializeBGlucan(xbin, ybin, zbin, 1);
		}
		
		if(args[8].contentEquals("TNF")){
			initialize.initializeTNF(xbin, ybin, zbin, Double.parseDouble(args[9]));
		}
		
		if(args[10].contentEquals("SAMP")){
			initialize.initializeSAMP(xbin, ybin, zbin, Integer.parseInt(args[11]));
		}
        
        
		
		initialize.initializeMacrophage(grid, xbin, ybin, zbin, Integer.parseInt(args[2]));
		
		int iter = Integer.parseInt(args[12]);
		//System.out.println(args[40]);
		File file = new File(args[18]);

        run.run(
        		iter,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		file, //new File("/Users/henriquedeassis/Documents/Projects/COVID19/data/newBaseModel.tsv"),
        		-1,
        		printStat
        );
        
        printStat.close();
		
		/*System.out.println(
				Constants.MA_IL1_QTTY + " " + //0
				Constants.MA_IL6_QTTY + " " + //1
				Constants.MA_IL8_QTTY + " " + //2
				Constants.MA_IL10_QTTY + " " + //3
				Constants.MA_MIP1B_QTTY + " " + //4
				Constants.MA_MIP2_QTTY + " " + //5
				Constants.MA_TNF_QTTY + " " + //6
				Constants.MA_TGF_QTTY + " " + //7
				"1 " + //8
				Constants.Kd_IL1 + " " + //9
		        Constants.Kd_IL6 + " " + //10
		        Constants.Kd_IL8  + " " + //11
				Constants.Kd_IL10 + " " + //12
				Constants.Kd_MCP1 + " " + //13
				Constants.Kd_MIP2 + " " + //14
				Constants.Kd_TNF + " " + //15
				Constants.Kd_TGF + " " + //16
				Constants.Kd_SAMP + " " + //17
				Constants.D + " " + //18
				Constants.MA_MOVE_RATE_ACT + " " + //19
				Constants.PR_MA_PHAG + " " + //20
				Constants.PR_MA_HYPHAE + " " + //21
				Constants.PR_ASPERGILLUS_CHANGE + " " + //22
				Constants.Kd_LPS + " " + //23
				Constants.Kd_GM_CSF
				);*/
	/*}
	
	private static void learningCytokinesSecretion(String[] args) throws InterruptedException {
		InitializeLearnCytokinesSecretion initialize = new InitializeLearnCytokinesSecretion();
		PrintLearningCytokines printStat = new PrintLearningCytokines();
		Run run = new RunSingleThread();
		
		//args[0] kind of cell
		//args[1] kind of ag
		//args[2] qtty of cell
		
		//args[3] CELL_IL6_QTTY [or NA]
		//args[4] CELL_IL10_QTTY [or NA]
		//args[5] CELL_MIP1B_QTTY [or NA]
		//args[6] CELL_MIP2_QTTY [or NA]
		//args[7] CELL_TNF_QTTY [or NA]
		//args[8] CELL_TGF_QTTY [or NA]
		
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        Constants.MA_HALF_LIFE = 0.0001375292; //seven days
        /*Constants.IL6_HALF_LIFE = 1;
        Constants.IL10_HALF_LIFE = 1;
        Constants.MIP1B_HALF_LIFE = 1;
        Constants.MIP2_HALF_LIFE = 1;
        Constants.TNF_HALF_LIFE = 1;
        Constants.TGF_HALF_LIFE = 1;*/
       /* Constants.TURNOVER_RATE = 0;
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE); 
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        
        
        Constants.MA_IL1_QTTY = Double.parseDouble(args[0]);
        Constants.MA_IL6_QTTY = Double.parseDouble(args[1]);
        Constants.MA_IL8_QTTY = Double.parseDouble(args[2]);
		Constants.MA_IL10_QTTY = Double.parseDouble(args[3]);
		Constants.MA_MCP1_QTTY = Double.parseDouble(args[4]);
		Constants.MA_MIP2_QTTY = Double.parseDouble(args[5]);
		Constants.MA_TNF_QTTY = Double.parseDouble(args[6]);
		Constants.MA_TGF_QTTY = Double.parseDouble(args[7]);
		
		Constants.P_IL1_QTTY = Double.parseDouble(args[0]);
        Constants.P_IL6_QTTY = Double.parseDouble(args[1]);
        Constants.P_IL8_QTTY = Double.parseDouble(args[2]);
		Constants.P_MCP1_QTTY = Double.parseDouble(args[4]);
		Constants.P_MIP2_QTTY = Double.parseDouble(args[5]);
		Constants.P_TNF_QTTY = Double.parseDouble(args[6]);
		
		
		Constants.IL1_HALF_LIFE = Double.parseDouble(args[8]);
        Constants.IL6_HALF_LIFE = Double.parseDouble(args[8]);
        Constants.IL8_HALF_LIFE = Double.parseDouble(args[8]);
		Constants.IL10_HALF_LIFE = Double.parseDouble(args[8]);
		Constants.MCP1_HALF_LIFE = Double.parseDouble(args[8]);
		Constants.MIP2_HALF_LIFE = Double.parseDouble(args[8]);
		Constants.TNF_HALF_LIFE = Double.parseDouble(args[8]);
		Constants.TGF_HALF_LIFE = Double.parseDouble(args[8]);
		
		Constants.Kd_IL1 = Double.parseDouble(args[9]);
        Constants.Kd_IL6 = Double.parseDouble(args[10]);
        Constants.Kd_IL8 = Double.parseDouble(args[11]);
		Constants.Kd_IL10 = Double.parseDouble(args[12]);
		Constants.Kd_MCP1 = Double.parseDouble(args[13]);
		Constants.Kd_MIP2 = Double.parseDouble(args[14]);
		Constants.Kd_TNF = Double.parseDouble(args[15]);
		Constants.Kd_TGF = Double.parseDouble(args[16]);
		Constants.Kd_SAMP = Double.parseDouble(args[17]);
		
		Constants.D = Double.parseDouble(args[18]);
		
		Constants.MA_MOVE_RATE_ACT = Double.parseDouble(args[19]);
		Constants.MA_MOVE_RATE_REST = Double.parseDouble(args[19]);
		
		Constants.PR_MA_PHAG = Double.parseDouble(args[20]);
		Constants.PR_MA_HYPHAE = Double.parseDouble(args[21]);
		
		Constants.PR_ASPERGILLUS_CHANGE = 1.0;
		//Constants. Double.parseDouble(args[22]);
		
		Constants.Kd_LPS = Double.parseDouble(args[23]);
		Constants.Kd_GM_CSF = Double.parseDouble(args[24]);
		
		Constants.TURNOVER_RATE = 0;
		
		
		initialize.setQuadrant(grid, xbin, ybin, zbin);
        

        Recruiter[] recruiters = new Recruiter[0];
		
		if(args[36].contentEquals("Aspergillus")){
			//System.out.println("pre-running ...");
			initialize.infect(Integer.parseInt(args[37]), grid, xbin, ybin, zbin, Integer.parseInt(args[38]), Constants.CONIDIA_INIT_IRON, -1, false);
			
			if(args[39].contentEquals("Germinate")) {
				int germinationIter = Integer.parseInt(args[40]);
				PrintNull printNull = new PrintNull();
				run.run(
						germinationIter,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
						xbin, 
						ybin, 
						zbin, 
						grid, 
	        			quadrants, 
	        			recruiters,
	        			false,
	        			null, //new File("/Users/henriquedeassis/Documents/Projects/COVID19/data/newBaseModel.tsv"),
	        			-1,
	        			printNull
				);
				Constants.ITER_TO_GROW = 1000000000;
				Constants.ITER_TO_GERMINATE = 1000000000;
				Constants.PR_ASPERGILLUS_CHANGE = 0;
			}
		}
        
        
		
		initialize.initializeMacrophage(grid, xbin, ybin, zbin, Integer.parseInt(args[25]));
		
		
		if(args[26].contentEquals("LPS")){
			initialize.initializeLPS(xbin, ybin, zbin, Double.parseDouble(args[27]));
		}
		
		if(args[28].contentEquals("GM_CSF")){
			//initialize.initializeGMCSF(xbin, ybin, zbin, Double.parseDouble(args[29]));
		}
		
		if(args[30].contentEquals("BGlucan")){
			initialize.initializeBGlucan(xbin, ybin, zbin, 1);
		}
		
		if(args[31].contentEquals("TNF")){
			initialize.initializeTNF(xbin, ybin, zbin, Double.parseDouble(args[32]));
		}
		
		if(args[33].contentEquals("SAMP")){
			initialize.initializeSAMP(xbin, ybin, zbin, Integer.parseInt(args[34]));
		}
		
		
		
		
		
		
		int iter = Integer.parseInt(args[35]);
		//System.out.println(args[40]);
		File file = new File(args[41]);

        run.run(
        		iter,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		file, //new File("/Users/henriquedeassis/Documents/Projects/COVID19/data/newBaseModel.tsv"),
        		-1,
        		printStat
        );
        
        printStat.close();
		
		/*System.out.println(
				Constants.MA_IL1_QTTY + " " + //0
				Constants.MA_IL6_QTTY + " " + //1
				Constants.MA_IL8_QTTY + " " + //2
				Constants.MA_IL10_QTTY + " " + //3
				Constants.MA_MIP1B_QTTY + " " + //4
				Constants.MA_MIP2_QTTY + " " + //5
				Constants.MA_TNF_QTTY + " " + //6
				Constants.MA_TGF_QTTY + " " + //7
				"1 " + //8
				Constants.Kd_IL1 + " " + //9
		        Constants.Kd_IL6 + " " + //10
		        Constants.Kd_IL8  + " " + //11
				Constants.Kd_IL10 + " " + //12
				Constants.Kd_MCP1 + " " + //13
				Constants.Kd_MIP2 + " " + //14
				Constants.Kd_TNF + " " + //15
				Constants.Kd_TGF + " " + //16
				Constants.Kd_SAMP + " " + //17
				Constants.D + " " + //18
				Constants.MA_MOVE_RATE_ACT + " " + //19
				Constants.PR_MA_PHAG + " " + //20
				Constants.PR_MA_HYPHAE + " " + //21
				Constants.PR_ASPERGILLUS_CHANGE + " " + //22
				Constants.Kd_LPS + " " + //23
				Constants.Kd_GM_CSF
				);*/
		
	/*}
	
	
	private static void learningActivationTime(String[] args) throws InterruptedException {
		InitializeLearnCytokinesSecretion initialize = new InitializeLearnCytokinesSecretion();
		PrintLearningCytokines printStat = new PrintLearningCytokines();
		Run run = new RunSingleThread();
		
		//args[0] kind of cell
		//args[1] kind of ag
		//args[2] qtty of cell
		
		//args[3] CELL_IL6_QTTY [or NA]
		//args[4] CELL_IL10_QTTY [or NA]
		//args[5] CELL_MIP1B_QTTY [or NA]
		//args[6] CELL_MIP2_QTTY [or NA]
		//args[7] CELL_TNF_QTTY [or NA]
		//args[8] CELL_TGF_QTTY [or NA]
		
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        Constants.MA_HALF_LIFE = 0.0001375292; //seven days
        /*Constants.IL6_HALF_LIFE = 1;
        Constants.IL10_HALF_LIFE = 1;
        Constants.MIP1B_HALF_LIFE = 1;
        Constants.MIP2_HALF_LIFE = 1;
        Constants.TNF_HALF_LIFE = 1;
        Constants.TGF_HALF_LIFE = 1;*/
        /*Constants.TURNOVER_RATE = 0;
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE); 
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        
        
        Constants.MA_IL1_QTTY = Double.parseDouble(args[0]);
        Constants.MA_IL6_QTTY = Double.parseDouble(args[1]);
        Constants.MA_IL8_QTTY = Double.parseDouble(args[2]);
		Constants.MA_IL10_QTTY = Double.parseDouble(args[3]);
		Constants.MA_MCP1_QTTY = Double.parseDouble(args[4]);
		Constants.MA_MIP2_QTTY = Double.parseDouble(args[5]);
		Constants.MA_TNF_QTTY = Double.parseDouble(args[6]);
		Constants.MA_TGF_QTTY = Double.parseDouble(args[7]);
		
		Constants.P_IL1_QTTY = Double.parseDouble(args[0]);
        Constants.P_IL6_QTTY = Double.parseDouble(args[1]);
        Constants.P_IL8_QTTY = Double.parseDouble(args[2]);
		Constants.P_MCP1_QTTY = Double.parseDouble(args[4]);
		Constants.P_MIP2_QTTY = Double.parseDouble(args[5]);
		Constants.P_TNF_QTTY = Double.parseDouble(args[6]);
		
		
		Constants.IL1_HALF_LIFE = Double.parseDouble(args[8]);
        Constants.IL6_HALF_LIFE = Double.parseDouble(args[8]);
        Constants.IL8_HALF_LIFE = Double.parseDouble(args[8]);
		Constants.IL10_HALF_LIFE = Double.parseDouble(args[8]);
		Constants.MCP1_HALF_LIFE = Double.parseDouble(args[8]);
		Constants.MIP2_HALF_LIFE = Double.parseDouble(args[8]);
		Constants.TNF_HALF_LIFE = Double.parseDouble(args[8]);
		Constants.TGF_HALF_LIFE = Double.parseDouble(args[8]);
		
		Constants.Kd_IL1 = Double.parseDouble(args[9]);
        Constants.Kd_IL6 = Double.parseDouble(args[10]);
        Constants.Kd_IL8 = Double.parseDouble(args[11]);
		Constants.Kd_IL10 = Double.parseDouble(args[12]);
		Constants.Kd_MCP1 = Double.parseDouble(args[13]);
		Constants.Kd_MIP2 = Double.parseDouble(args[14]);
		Constants.Kd_TNF = Double.parseDouble(args[15]);
		Constants.Kd_TGF = Double.parseDouble(args[16]);
		Constants.Kd_SAMP = Double.parseDouble(args[17]);
		
		Constants.D = Double.parseDouble(args[18]);
		
		Constants.MA_MOVE_RATE_ACT = Double.parseDouble(args[19]);
		Constants.MA_MOVE_RATE_REST = Double.parseDouble(args[19]);
		
		Constants.PR_MA_PHAG = Double.parseDouble(args[20]);
		Constants.PR_MA_HYPHAE = Double.parseDouble(args[21]);
		
		Constants.PR_ASPERGILLUS_CHANGE = 1.0;
		Constants.INV_UNIT_T = Double.parseDouble(args[22]);
		
		Constants.Kd_LPS = Double.parseDouble(args[23]);
		Constants.Kd_BGLUCAN = Double.parseDouble(args[24]);
		//Constants.Kd_GM_CSF = Double.parseDouble(args[24]);
		
		Constants.TURNOVER_RATE = 0;
		
		
		initialize.setQuadrant(grid, xbin, ybin, zbin);
        

        Recruiter[] recruiters = new Recruiter[0];
		
		if(args[36].contentEquals("Aspergillus")){
			//System.out.println("pre-running ...");
			initialize.infect(Integer.parseInt(args[37]), grid, xbin, ybin, zbin, Integer.parseInt(args[38]), Constants.CONIDIA_INIT_IRON, -1, false);
			
			if(args[39].contentEquals("Germinate")) {
				int germinationIter = Integer.parseInt(args[40]);
				PrintNull printNull = new PrintNull();
				run.run(
						germinationIter,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
						xbin, 
						ybin, 
						zbin, 
						grid, 
	        			quadrants, 
	        			recruiters,
	        			false,
	        			null, //new File("/Users/henriquedeassis/Documents/Projects/COVID19/data/newBaseModel.tsv"),
	        			-1,
	        			printNull
				);
				Constants.ITER_TO_GROW = 1000000000;
				Constants.ITER_TO_GERMINATE = 1000000000;
				Constants.PR_ASPERGILLUS_CHANGE = 0;
			}
		}
        
        
		
		initialize.initializeMacrophage(grid, xbin, ybin, zbin, Integer.parseInt(args[25]));
		
		
		if(args[26].contentEquals("LPS")){
			initialize.initializeLPS(xbin, ybin, zbin, Double.parseDouble(args[27]));
		}else if (args[26].contentEquals("BGlucan")) {
			initialize.initializeBGlucan(xbin, ybin, zbin, Double.parseDouble(args[27]));
		}
		
		if(args[28].contentEquals("GM_CSF")){
			//initialize.initializeGMCSF(xbin, ybin, zbin, Double.parseDouble(args[29]));
		}
		
		//if(args[30].contentEquals("BGlucan")){
		//	initialize.initializeBGlucan(xbin, ybin, zbin, 1);
		//}
		
		if(args[31].contentEquals("TNF")){
			initialize.initializeTNF(xbin, ybin, zbin, Double.parseDouble(args[32]));
		}
		
		if(args[33].contentEquals("SAMP")){
			initialize.initializeSAMP(xbin, ybin, zbin, Integer.parseInt(args[34]));
			if(args[39].contentEquals("Preincubation")) {
				int preincubationIter = Integer.parseInt(args[40]);
				PrintNull printNull = new PrintNull();
				run.run(
						preincubationIter,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
						xbin, 
						ybin, 
						zbin, 
						grid, 
	        			quadrants, 
	        			recruiters,
	        			false,
	        			null, //new File("/Users/henriquedeassis/Documents/Projects/COVID19/data/newBaseModel.tsv"),
	        			-1,
	        			printNull
				);
				Constants.ITER_TO_GROW = 1000000000;
				Constants.ITER_TO_GERMINATE = 1000000000;
				Constants.PR_ASPERGILLUS_CHANGE = 0;
			}
			initialize.removeSAMP(xbin, ybin, zbin);
		}
		
		
		
		
		
		
		int iter = Integer.parseInt(args[35]);
		//System.out.println(args[40]);
		File file = new File(args[41]);

        run.run(
        		iter,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		file, //new File("/Users/henriquedeassis/Documents/Projects/COVID19/data/newBaseModel.tsv"),
        		-1,
        		printStat
        );
        
        printStat.close();
		
		/*System.out.println(
				Constants.MA_IL1_QTTY + " " + //0
				Constants.MA_IL6_QTTY + " " + //1
				Constants.MA_IL8_QTTY + " " + //2
				Constants.MA_IL10_QTTY + " " + //3
				Constants.MA_MIP1B_QTTY + " " + //4
				Constants.MA_MIP2_QTTY + " " + //5
				Constants.MA_TNF_QTTY + " " + //6
				Constants.MA_TGF_QTTY + " " + //7
				"1 " + //8
				Constants.Kd_IL1 + " " + //9
		        Constants.Kd_IL6 + " " + //10
		        Constants.Kd_IL8  + " " + //11
				Constants.Kd_IL10 + " " + //12
				Constants.Kd_MCP1 + " " + //13
				Constants.Kd_MIP2 + " " + //14
				Constants.Kd_TNF + " " + //15
				Constants.Kd_TGF + " " + //16
				Constants.Kd_SAMP + " " + //17
				Constants.D + " " + //18
				Constants.MA_MOVE_RATE_ACT + " " + //19
				Constants.PR_MA_PHAG + " " + //20
				Constants.PR_MA_HYPHAE + " " + //21
				Constants.PR_ASPERGILLUS_CHANGE + " " + //22
				Constants.Kd_LPS + " " + //23
				Constants.Kd_GM_CSF
				);*/
		
	/*}

	public static void main_legacy(String[] args) throws Exception {
		/*String[] str = new String[] {"4.27225e-18", "2.683149e-21", "7.655258e-18", "3.489064e-21",  "1.054377e-19", "5.374622e-19", "5.474817e-18",
				"3.204886e-18", "1", "8.906547e-09", "1.076802e-09", "1.452237e-09", "1.136358e-10", "4.249606e-09", "1.189199e-10",
				"9.502331e-10", "1.479079e-11", "0.3491431", "13.4107", "1.839809", "0.3896933", "1",  "0.01316449", "1.402887e-09", "1",
				"64", "LPS", "0", "GM_CSF", "0", "None", "TNF", "0", "SAMP", "0", "570", "None", "8", Afumigatus.RESTING_CONIDIA + "", "None", "0"
		};*/
		
		/*String[] str = new String[] {
				"4.27225e-18", //0
				"2.683149e-21", //1
				"7.655258e-18", //2
				"3.489064e-21", //3
				"1.054377e-19", //4
				"5.374622e-19", //5
				"5.474817e-18", //6
				"3.204886e-18", //7
				"1", //8
				"8.906547e-09", //9
				"1.076802e-09", //10
				"1.452237e-09", //11
				"1.136358e-10", //12
				"4.249606e-09", //13
				"1.189199e-10", //14
				
				"9.502331e-10", //15
				"1.479079e-11", //16
				"0.3491431", //17
				"13.4107", //18
				"1.839809", //19
				"0.3896933", //20
				"1", //21
				"0.01316449", //22
				"1.402887e-09", //23
				"1", //24
				"64", //25
				"LPS", //26
				"0", //27 1e-8
				"GM_CSF", //28
				"0", //29
				"None", //30 -- BGlucan
				"TNF", //31
				"0", //32
				"SAMP", //33
				"0", //34
				"570", //35
				"Aspergillus", //36
				"8", //37
				Afumigatus.RESTING_CONIDIA + "", //38
				"None", //39
				"0" //40
		};*/
		
		/*System.out.println("jISS Bayesian Learning");
		long tic = System.currentTimeMillis();
		//BayesianLearning.learningCytokinesSecretion(args);
		BayesianLearning.learningActivationTime(args);
		long toc = System.currentTimeMillis();
		System.out.println((toc - tic));
		//System.out.println(args[41] + " took " + (toc - tic));
		/*BayesianLearning.learningActivationTime(new String[] {
				Constants.MA_IL1_QTTY + "", //0
				Constants.MA_IL6_QTTY + "", //1
				Constants.MA_IL8_QTTY + "", //2
				Constants.MA_IL10_QTTY + "", //3
				Constants.MA_MIP1B_QTTY + "", //4
				Constants.MA_MIP2_QTTY + "", //5
				Constants.MA_TNF_QTTY + "", //6
				Constants.MA_TGF_QTTY + "", //7
				Constants.IL1_HALF_LIFE + "", //8
				Constants.Kd_IL1 + "", //9
		        Constants.Kd_IL6 + "", //10
		        Constants.Kd_IL8  + "", //11
				Constants.Kd_IL10 + "", //12
				Constants.Kd_MCP1 + "", //13
				Constants.Kd_MIP2 + "", //14
				Constants.Kd_TNF + "", //15
				Constants.Kd_TGF + "", //16
				Constants.Kd_SAMP + "", //17
				Constants.D + "", //18
				Constants.MA_MOVE_RATE_ACT + "", //19
				Constants.PR_MA_PHAG + "", //20
				Constants.PR_MA_HYPHAE + "", //21
				Constants.INV_UNIT_T + "", //22
				Constants.Kd_LPS + "", //23
				"1e-4", //24
				"160",	
				"BGlucan",	
				"6.4e-12",	
				"None",	
				"0",	
				"None",	
				"None",	
				"0",	
				"None",	
				"0",	
				"720",	
				"None",	
				"0",	
				"5",	
				"None",	
				"0"
		});*/
	//}
	
//}
