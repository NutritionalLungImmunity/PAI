package edu.uf.main;

import java.io.File;

import edu.uf.Diffusion.Diffuse;
import edu.uf.Diffusion.FADIPeriodic;
import edu.uf.compartments.GridFactory;
import edu.uf.compartments.MacrophageRecruiter;
import edu.uf.compartments.NeutrophilRecruiter;
import edu.uf.compartments.Recruiter;
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.main.initialize.Initialize;
import edu.uf.main.initialize.InitializeBaseModel;
import edu.uf.main.initialize.InitializeTranexamicModel;
import edu.uf.main.print.PrintHemeModel;
import edu.uf.main.run.Run;
import edu.uf.main.run.RunSingleThread;
import edu.uf.utils.Constants;

public class Main {
	
	private static void baseModel(String[] args) throws InterruptedException {

		
		Constants.PR_NET_KILL_EPI *= 0.25;//Double.parseDouble(args[0]);//(0.1*Double.parseDouble(args[0]));
		Constants.NET_COUNTER_INHIBITION = 0.0;//Double.parseDouble(args[0]);
		Constants.HEME_QTTY *= 10;//Double.parseDouble(args[0]);
		Constants.HEME_UP  *= 1.0;//0.75;//Double.parseDouble(args[1]);
		
		int i = Integer.parseInt(args[0]);
		
		String filename = "HemeNET025_";// + args[0] + "_";

		filename += i + ".tsv";
			
		Initialize initialize = new InitializeBaseModel();
		Run run = new RunSingleThread();
		PrintHemeModel stat = new PrintHemeModel();
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
        
        //int pne = (int) (xbin * ybin * zbin  * 0.64);
        
        String[] input = new String[]{"0", "1920", "15", "640"};
        
        //Constants.Kd_Granule = 1e12;
        
        /*Constants.ITER_TO_GROW = 328/2;//Integer.parseInt(args[0]);  //REF 328
        Constants.PR_N_HYPHAE = 0;//Double.parseDouble(args[1]) * Constants.PR_N_HYPHAE ;
        Constants.PR_MA_HYPHAE = Constants.PR_MA_HYPHAE/4;//Double.parseDouble(args[1]) * Constants.PR_MA_HYPHAE;
        Constants.GRANULE_QTTY = 1;//Double.parseDouble(args[2]);
        Constants.Kd_Granule = 0.1 * 1e12;//10 * 1e12; 3.98 //REF:10
        //Constants.Kd_Granule = Double.parseDouble(args[0]) * 1e12;
        Constants.Granule_HALF_LIFE = -1+Math.log(0.5)/20;//1+Math.log(0.5)/20.0;// //harmonic avg of the best //REF:20
        Constants.MAX_MA = 660;//Integer.parseInt(args[3]);
        Constants.MAX_N = 660;//Integer.parseInt(args[4]);
        
        Constants.PR_DEPLETION = 0.0;*/
        
        //Constants.P_TNF_QTTY = 1*Constants.P_TNF_QTTY;
        //Constants.MIN_MA = 15;
        
        //heme diffusion is off!!!
        
        //Constants.PR_ASPERGILLUS_CHANGE = 0.1732868;
        Constants.MAX_N = 522*1;//0.75;//*0.75;
        //Constants.MAX_MA = 0;//0.75;//*0.75;
        Constants.RECRUITMENT_RATE_N *= 1.0;//0.25;//0.125;//0.25;
        Constants.NEUTROPHIL_HALF_LIFE = 0.05776227;
        Constants.NET_HALF_LIFE = 0.007701635;
        Constants.DNAse_KCAT = 0.007701635 * 10.0;
        
        Constants.DNAse_HALF_LIFE = 0.999;
        
        //Constants.ITER_TO_GROW =  30;
        
        Constants.PR_ASP_KILL_EPI *= 1.0;
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE); 
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        /*Constants.P_TNF_QTTY = Constants.P_TNF_QTTY/10.0;
        Constants.P_MIP2_QTTY = Constants.P_MIP2_QTTY/10.0;
        Constants.P_IL6_QTTY = Constants.P_IL6_QTTY/10.0;
        Constants.P_MIP1B_QTTY = Constants.P_MIP1B_QTTY/10.0;
        
        Constants.MA_TNF_QTTY = Constants.MA_TNF_QTTY/10.0;
        Constants.MA_MIP2_QTTY = Constants.MA_MIP2_QTTY/10.0;
        Constants.MA_IL6_QTTY = Constants.MA_IL6_QTTY/10.0;
        Constants.MA_MIP1B_QTTY = Constants.MA_MIP1B_QTTY/10.0;*/
        
        initialize.createPeriodicGrid(xbin, ybin, zbin);
        initialize.initializeMolecules(diffusion, false);
        initialize.initializePneumocytes(Integer.parseInt(input[3]));
        //initialize.initializeLiver(grid, xbin, ybin, zbin);
        initialize.initializeMacrophage(Integer.parseInt(input[2]));
        initialize.initializeNeutrophils(0);
        initialize.initializeTypeIPneumocytes(Integer.parseInt(input[3])/2);
        initialize.initializeBlood();
        initialize.infect(Integer.parseInt(input[1]), Afumigatus.RESTING_CONIDIA, Constants.CONIDIA_INIT_IRON, -1, false);
        stat.grid = GridFactory.getGrid();

        Recruiter[] recruiters = new Recruiter[2];
        recruiters[0] = new MacrophageRecruiter();
        recruiters[1] = new NeutrophilRecruiter();
        //recruiters[0] = new MacrophageReplenisher();
        //recruiters[1] = new NeutrophilReplenisher();
        
        run.run(
        		1366,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin,  
        		recruiters,
        		false,
        		new File(filename),//new File("/Users/henriquedeassis/Documents/Projects/Afumigatus/data/ganlin/" + filename),
        		-1,
        		stat
        );
        
        stat.close();
        //System.out.println((toc - tic));
	}
	
	
	
	private static void tranexamicAcidModel(String[] args) throws InterruptedException {

		
		Constants.PR_NET_KILL_EPI *= 0.25;//Double.parseDouble(args[0]);//(0.1*Double.parseDouble(args[0]));
		Constants.NET_COUNTER_INHIBITION = 0.0;//Double.parseDouble(args[0]);
		Constants.HEME_QTTY *= 10;//Double.parseDouble(args[0]);
		Constants.HEME_UP  *= 1.0;//0.75;//Double.parseDouble(args[1]);
		
		int i = 0;//Integer.parseInt(args[0]);
		
		String filename = "HemeNET025_";// + args[0] + "_";

		filename += i + ".tsv";
			
		Initialize initialize = new InitializeTranexamicModel();
		Run run = new RunSingleThread();
		PrintHemeModel stat = new PrintHemeModel();
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
        
        //int pne = (int) (xbin * ybin * zbin  * 0.64);
        
        String[] input = new String[]{"0", "1920", "15", "640"};
        
        Constants.MAX_N = 522*1;//0.75;//*0.75;
        //Constants.MAX_MA = 0;//0.75;//*0.75;
        Constants.RECRUITMENT_RATE_N *= 1.0;//0.25;//0.125;//0.25;
        Constants.NEUTROPHIL_HALF_LIFE = 0.05776227;
        Constants.NET_HALF_LIFE = 0.007701635;
        Constants.DNAse_KCAT = 0.007701635 * 10.0;
        //Constants.PR_COAGULUM_BREAK = 0;
        
        Constants.DNAse_HALF_LIFE = 0.999;
        
        Constants.PR_ASP_KILL_EPI *= 1.0;
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE); 
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        initialize.createPeriodicGrid(xbin, ybin, zbin);
        initialize.initializeMolecules(diffusion, false);
        initialize.initializePneumocytes(Integer.parseInt(input[3]));
        initialize.initializeMacrophage(Integer.parseInt(input[2]));
        initialize.initializeNeutrophils(0);
        initialize.initializeTypeIPneumocytes(Integer.parseInt(input[3])/2);
        initialize.initializeBlood();
        ((InitializeTranexamicModel)initialize).initializeTranexamicAcid(new int[] {360});
        initialize.infect(Integer.parseInt(input[1]), Afumigatus.RESTING_CONIDIA, Constants.CONIDIA_INIT_IRON, -1, false);
        stat.grid = GridFactory.getGrid();

        Recruiter[] recruiters = new Recruiter[2];
        recruiters[0] = new MacrophageRecruiter();
        recruiters[1] = new NeutrophilRecruiter();
        
        run.run(
        		2160,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		recruiters,
        		false,
        		null, //new File(filename),//new File("/Users/henriquedeassis/Documents/Projects/Afumigatus/data/ganlin/" + filename),
        		-1,
        		stat
        );
        
        stat.close();
        //System.out.println((toc - tic));
	}
	
	
	
	
	
	/*private static void baseHemorrhage(String[] args) throws InterruptedException {
		Initialize initialize = new InitializeHemorrhageModel();
		Run run = new RunSingleThread();
		PrintHemorrhageModel stat = new PrintHemorrhageModel();
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        //int pne = (int) (xbin * ybin * zbin  * 0.64);
        
        String[] input = new String[]{"0", "1920", "15", "640"};
        
        //Constants.Kd_Granule = 1e12;
        
        /*Constants.ITER_TO_GROW = 328/2;//Integer.parseInt(args[0]);  //REF 328
        Constants.PR_N_HYPHAE = 0;//Double.parseDouble(args[1]) * Constants.PR_N_HYPHAE ;
        Constants.PR_MA_HYPHAE = Constants.PR_MA_HYPHAE/4;//Double.parseDouble(args[1]) * Constants.PR_MA_HYPHAE;
        Constants.GRANULE_QTTY = 1;//Double.parseDouble(args[2]);
        Constants.Kd_Granule = 0.1 * 1e12;//10 * 1e12; 3.98 //REF:10
        //Constants.Kd_Granule = Double.parseDouble(args[0]) * 1e12;
        Constants.Granule_HALF_LIFE = -1+Math.log(0.5)/20;//1+Math.log(0.5)/20.0;// //harmonic avg of the best //REF:20
        Constants.MAX_MA = 660;//Integer.parseInt(args[3]);
        Constants.MAX_N = 660;//Integer.parseInt(args[4]);
        
        Constants.PR_DEPLETION = 0.0;*/
        
        //Constants.P_TNF_QTTY = 1*Constants.P_TNF_QTTY;
        //Constants.MIN_MA = 15;
        
        /*double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE); 
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        /*Constants.P_TNF_QTTY = Constants.P_TNF_QTTY/10.0;
        Constants.P_MIP2_QTTY = Constants.P_MIP2_QTTY/10.0;
        Constants.P_IL6_QTTY = Constants.P_IL6_QTTY/10.0;
        Constants.P_MIP1B_QTTY = Constants.P_MIP1B_QTTY/10.0;
        
        Constants.MA_TNF_QTTY = Constants.MA_TNF_QTTY/10.0;
        Constants.MA_MIP2_QTTY = Constants.MA_MIP2_QTTY/10.0;
        Constants.MA_IL6_QTTY = Constants.MA_IL6_QTTY/10.0;
        Constants.MA_MIP1B_QTTY = Constants.MA_MIP1B_QTTY/10.0;*/
        
        /*Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(xquadrant, yquadrant, zquadrant);
        initialize.initializePneumocytes(Integer.parseInt(input[3]));
        initialize.initializeErytrocytes();
        initialize.initializeMolecules(diffusion, false);
        //initialize.initializeLiver(grid, xbin, ybin, zbin);
        initialize.initializeMacrophage(Integer.parseInt(input[2]));
        initialize.initializeNeutrophils(0);
        initialize.infect(Integer.parseInt(input[1]), Afumigatus.RESTING_CONIDIA, Constants.CONIDIA_INIT_IRON, -1, false);
        initialize.setQuadrant();
        stat.grid = grid;

        Recruiter[] recruiters = new Recruiter[2];
        recruiters[0] = new MacrophageRecruiter();
        recruiters[1] = new NeutrophilRecruiter();
        //recruiters[0] = new MacrophageReplenisher();
        //recruiters[1] = new NeutrophilReplenisher();
        
        run.run(
        		2160,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin,  
        		quadrants, 
        		recruiters,
        		false,
        		new File("/Users/henriquedeassis/Documents/Projects/COVID19/data/newBaseModel.tsv"),
        		-1,
        		stat
        );
        
        stat.close();
        //System.out.println((toc - tic));
	}
	
	
	/*private static void baseHemorrhage2(String[] args) throws InterruptedException {
		Initialize initialize = new InitializeHemorrhageModel();
		Run run = new RunSingleThread();
		PrintBaseModel stat = new PrintBaseModel();
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        //int pne = (int) (xbin * ybin * zbin  * 0.64);
        
        String[] input = new String[]{"0", "1920", "15", "640"};
        
        //Constants.Kd_Granule = 1e12;
        
        /*Constants.ITER_TO_GROW = 328/2;//Integer.parseInt(args[0]);  //REF 328
        Constants.PR_N_HYPHAE = 0;//Double.parseDouble(args[1]) * Constants.PR_N_HYPHAE ;
        Constants.PR_MA_HYPHAE = Constants.PR_MA_HYPHAE/4;//Double.parseDouble(args[1]) * Constants.PR_MA_HYPHAE;
        Constants.GRANULE_QTTY = 1;//Double.parseDouble(args[2]);
        Constants.Kd_Granule = 0.1 * 1e12;//10 * 1e12; 3.98 //REF:10
        //Constants.Kd_Granule = Double.parseDouble(args[0]) * 1e12;
        Constants.Granule_HALF_LIFE = -1+Math.log(0.5)/20;//1+Math.log(0.5)/20.0;// //harmonic avg of the best //REF:20
        Constants.MAX_MA = 660;//Integer.parseInt(args[3]);
        Constants.MAX_N = 660;//Integer.parseInt(args[4]);
        
        Constants.PR_DEPLETION = 0.0;*/
        
        //Constants.P_TNF_QTTY = 1*Constants.P_TNF_QTTY;
        //Constants.MIN_MA = 15;
        
       /* double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE); 
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        /*Constants.P_TNF_QTTY = Constants.P_TNF_QTTY/10.0;
        Constants.P_MIP2_QTTY = Constants.P_MIP2_QTTY/10.0;
        Constants.P_IL6_QTTY = Constants.P_IL6_QTTY/10.0;
        Constants.P_MIP1B_QTTY = Constants.P_MIP1B_QTTY/10.0;
        
        Constants.MA_TNF_QTTY = Constants.MA_TNF_QTTY/10.0;
        Constants.MA_MIP2_QTTY = Constants.MA_MIP2_QTTY/10.0;
        Constants.MA_IL6_QTTY = Constants.MA_IL6_QTTY/10.0;
        Constants.MA_MIP1B_QTTY = Constants.MA_MIP1B_QTTY/10.0;*/
        
       /* Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.initializePneumocytes(grid, xbin, ybin, zbin, Integer.parseInt(input[3]));
        //initialize.initializeLiver(grid, xbin, ybin, zbin);
        initialize.initializeMacrophage(grid, xbin, ybin, zbin, Integer.parseInt(input[2]));
        initialize.initializeNeutrophils(grid, xbin, ybin, zbin, 0);
        initialize.infect(Integer.parseInt(input[1]), grid, xbin, ybin, zbin, Afumigatus.RESTING_CONIDIA, Constants.CONIDIA_INIT_IRON, -1, false);
        initialize.setQuadrant(grid, xbin, ybin, zbin);
        stat.grid = grid;

        Recruiter[] recruiters = new Recruiter[2];
        recruiters[0] = new MacrophageRecruiter();
        recruiters[1] = new NeutrophilRecruiter();
        //recruiters[0] = new MacrophageReplenisher();
        //recruiters[1] = new NeutrophilReplenisher();
        
        run.run(
        		2160,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		null, //new File("/Users/henriquedeassis/Documents/Projects/COVID19/data/newBaseModel.tsv"),
        		-1,
        		stat
        );
        
        stat.close();
        //System.out.println((toc - tic));
	}
	
	
	
	private static void test(String[] args) throws InterruptedException {
		Initialize initialize = new InitializeBaseModel();
		Run run = new RunSingleThread();
		PrintBaseModel stat = new PrintBaseModel();
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        //int pne = (int) (xbin * ybin * zbin  * 0.64);
        
        String[] input = new String[]{"0", "1920", "15", "640"};
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE); 
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        //List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        //initialize.initializePneumocytes(grid, xbin, ybin, zbin, Integer.parseInt(input[3]));
        initialize.initializeNeutrophils(grid, xbin, ybin, zbin, 100);
        //initialize.initializeMacrophage(grid, xbin, ybin, zbin, Integer.parseInt(input[2]));
        //initialize.infect(Integer.parseInt(input[1]), grid, xbin, ybin, zbin, Afumigatus.RESTING_CONIDIA, Constants.CONIDIA_INIT_IRON, -1, false);
        //initialize.setQuadrant(grid, xbin, ybin, zbin);
        stat.grid = grid;
        
        for(int i = 0; i < 1440; i++) {
        	for(int x = 0; x < xbin; x++)
        		for(int y = 0; y < ybin; y++)
        			for(int z = 0; z < zbin; z++) {
        				Exec.gc(grid[x][y][z]);
                        grid[x][y][z].next(xbin, ybin, zbin, grid);
        			}
        	stat.printStatistics(i, null);
        }

	}
	
	
	private static void invitroMacrophages(String[] args) throws InterruptedException {
		Initialize initialize = new InitializeInvitromacrophages();
		Run run = new RunSingleThread();
		PrintBaseModel stat = new PrintBaseModel();
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
		
		String[] input = new String[]{"0", "0", "100", "0"};
		
		Constants.MIN_MA = 100;
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE); 
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.initializeMacrophage(grid, xbin, ybin, zbin, Integer.parseInt(input[2]));
        initialize.setQuadrant(grid, xbin, ybin, zbin);

        Recruiter[] recruiters = new Recruiter[1];
        recruiters[0] = new MacrophageReplenisher();
        
        run.run(
        		2160,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		new File("/Users/henriquedeassis/Documents/Projects/COVID19/data/macrophagesInvitro.tsv"),
        		-1,
        		stat
        );
        
        stat.close();
	}
	
	
	private static void baseInvitroModel(String[] args) throws InterruptedException {
		Initialize initialize = new InitializeBaseInvitroModel();
		Run run = new RunSingleThread();
		PrintStat stat = new PrintBaseInVitro();
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        //int pne = (int) (xbin * ybin * zbin  * 0.64);
        
        String[] input = new String[]{args[0], args[1], args[2], args[3]};
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        List<Macrophage> macrophage = initialize.initializeMacrophage(grid, xbin, ybin, zbin, (int) (Integer.parseInt(input[0]) * Double.parseDouble(input[1])));
        List<Afumigatus> afumigatus = initialize.infect(Integer.parseInt(input[0]), grid, xbin, ybin, zbin, Afumigatus.SWELLING_CONIDIA, 0,  -1, false);
        initialize.setQuadrant(grid, xbin, ybin, zbin);
        Constants.MIN_MA = Integer.parseInt(input[0]) * Double.parseDouble(input[1]);
        Constants.MA_MOVE_RATE_ACT = Constants.MA_MOVE_RATE_ACT * Double.parseDouble(input[2]);
        Constants.MA_MOVE_RATE_REST = Constants.MA_MOVE_RATE_ACT;
        Constants.PR_MA_PHAG = Constants.PR_MA_PHAG * Double.parseDouble(input[3]);

        Recruiter[] recruiters = new Recruiter[0];
        
        System.out.println(input[0] + " " + input[1] + " " + input[2] + " " + input[3]);
        
        File file = new File("/Users/henriquedeassis/Documents/Projects/Afumigatus/data/phagocytosis_1_" + args[1] + "_" + args[2] + "_" + args[3] + ".tsv");
        
        run.run(
        		60,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		file,
        		-1,
        		stat
        );
        
        /*for(Macrophage m : macrophage)
        	m.die();
        for(Afumigatus m : afumigatus)
        	m.die();*/
        
        
    /*    stat.close();
        //System.out.println((toc - tic));
	}
	
	private static void hemorrhateModel(String[] args) throws InterruptedException {
		Initialize initialize = new InitializeHemorrhageModel();
		Run run = new RunSingleThread();
		PrintStat stat = new PrintHemorrhageModel();
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        //int pne = (int) (xbin * ybin * zbin  * 0.64);
        
        String[] input = new String[]{"0", "1920", "15", "640"};
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE);
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        /*Constants.CONIDIA_INIT_IRON *= Double.parseDouble(args[0]);
        Constants.L_HPX_QTTY = 0;
        Constants.L_REST_HPX_QTTY = 0;
        Constants.DEFAULT_HPX_CONCENTRATION = 0;
        Constants.HEME_TURNOVER_RATE = 0;*/
        //Constants.HEME_UP = 0;//Constants.HEME_UP*0.8;
        //Constants.Kd_HPX=1; //7.120429e-13
        
      /*  Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.initializePneumocytes(grid, xbin, ybin, zbin, Integer.parseInt(input[3]));
        initialize.initializeLiver(grid, xbin, ybin, zbin);
        initialize.initializeMacrophage(grid, xbin, ybin, zbin, Integer.parseInt(input[2]));
        //initialize.initializeErytrocytes(grid, xbin, ybin, zbin);
        initialize.infect(Integer.parseInt(input[1]), grid, xbin, ybin, zbin, Afumigatus.RESTING_CONIDIA, Constants.CONIDIA_INIT_IRON, -1, false);
        initialize.setQuadrant(grid, xbin, ybin, zbin);


        Recruiter[] recruiters = new Recruiter[2];
        recruiters[0] = new MacrophageRecruiter();
        recruiters[1] = new NeutrophilRecruiter();
        
        
        
        File file = new File("/Users/henriquedeassis/Documents/Projects/Afumigatus/data/tmp.tsv");
        
        run.run(
        		2160,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		null,
        		-1,
        		stat
        );
        
        stat.close();
        //System.out.println((toc - tic));
	}
	
	private static void runExperiment081321() throws Exception {
		FileReader fr = new FileReader("/Users/henriquedeassis/Documents/Projects/Afumigatus/data/mat081321.csv");
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		String[] sp = null;
	
		while((line = br.readLine())!=null) {
			sp = line.split(",");
			//System.out.println("64 " + sp[0] + " " + sp[1] + " " + sp[2]);
			Main.baseInvitroModel(new String[]{"64", sp[0], sp[1], sp[2]});
		}
		
		br.close();
	}
	
	
	private static void runSA092221(String[] args)  throws Exception{
		Initialize initialize = new InitializeHemorrhageModel();
		Run run = new RunSingleThread();
		PrintStat stat = new PrintHemorrhageModel();
		
		
		Constants.HEME_SYSTEM_CONCENTRATION = Double.parseDouble(args[0]);
		Constants.HEME_TURNOVER_RATE = Double.parseDouble(args[1]);
		Constants.Kd_HPX = Double.parseDouble(args[2]);
		Constants.Kd_Heme = Double.parseDouble(args[3]);
		Constants.KM_HPX = Double.parseDouble(args[4]);
		Constants.KCAT_HPX = Double.parseDouble(args[5]);
		Constants.HEME_UP = Double.parseDouble(args[6]); 
		Constants.DEFAULT_HPX_CONCENTRATION = Double.parseDouble(args[7]);
		Constants.L_HPX_QTTY = Double.parseDouble(args[8]);
		Constants.L_REST_HPX_QTTY = Double.parseDouble(args[9]);
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        //int pne = (int) (xbin * ybin * zbin  * 0.64);
        
        String[] input = new String[]{"0", "1920", "15", "640"};
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE);
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.initializePneumocytes(grid, xbin, ybin, zbin, Integer.parseInt(input[3]));
        initialize.initializeLiver(grid, xbin, ybin, zbin);
        initialize.initializeMacrophage(grid, xbin, ybin, zbin, Integer.parseInt(input[2]));
        //initialize.initializeErytrocytes(grid, xbin, ybin, zbin);
        initialize.infect(Integer.parseInt(input[1]), grid, xbin, ybin, zbin, Afumigatus.RESTING_CONIDIA, Constants.CONIDIA_INIT_IRON, -1, false);
        initialize.setQuadrant(grid, xbin, ybin, zbin);


        Recruiter[] recruiters = new Recruiter[2];
        recruiters[0] = new MacrophageRecruiter();
        recruiters[1] = new NeutrophilRecruiter();
        
        
        File file = new File("/Users/henriquedeassis/Documents/Projects/Afumigatus/data/hemorrhage/outWT_" + args[10] + ".tsv");
        
        run.run(
        		1080,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		file,
        		-1,
        		stat
        );
        
        stat.close();
	}
	
	/*private static void runHemeInVitro101221()  throws Exception{
		Initialize initialize = new InitializeHemeInVitroModel();
		Run run = new RunSingleThread();
		PrintStat stat = new PrintHemeInVitroModel();
		
		
		Constants.TURNOVER_RATE = 0;
		
		Constants.Kd_GROW = Constants.Kd_LIP / 2.0;
		
		Constants.KM_IRON = 1e-10;
		Constants.KCAT_IRON = 30;
		Constants.MIN_FREE_IRON = Constants.KM_IRON*Constants.VOXEL_VOL*10;//L*0.0001;
		Constants.INIT_GLU  = 0.02*Constants.VOXEL_VOL;
		Constants.INIT_IRON = Constants.MIN_FREE_IRON;//1e-5*Constants.VOXEL_VOL;
		//Constants.MIN_FREE_IRON = 0;
		
		Constants.Kd_GLU = 1;//5e1*Constants.VOXEL_VOL;
		
		Constants.INIT_HEME = 0;
		Constants.INIT_TIN_PROTOPORPHYRIN = 0;
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        //int pne = (int) (xbin * ybin * zbin  * 0.64);
        
        String[] input = new String[]{"0", "100"};//, "15", "640"};
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE);
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.infect(Integer.parseInt(input[1]), grid, xbin, ybin, zbin, Afumigatus.RESTING_CONIDIA, Constants.CONIDIA_INIT_IRON, -1, false);
        initialize.setQuadrant(grid, xbin, ybin, zbin);


        Recruiter[] recruiters = new Recruiter[0];
        
        
        //ile file = new File("/Users/henriquedeassis/Documents/Projects/Afumigatus/data/hemorrhage/outWT_" + args[10] + ".tsv");
        
        run.run(
        		1440,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		null,
        		-1,
        		stat
        );
        
        stat.close();
	}*/
	
	
	
	/*private static void runExperiment101221()  throws Exception{
		Initialize initialize = new InitializeBaseModel();
		Run run = new RunSingleThread();
		PrintStat stat = new PrintBaseModel();
		
		
		/*Initialize initialize = new InitializeHemorrhageModel();
		Run run = new RunSingleThread();
		PrintStat stat = new PrintHemorrhageModel();*/
		
		/*Constants.Kd_GROW = Constants.Kd_LIP / 10.0;
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        //int pne = (int) (xbin * ybin * zbin  * 0.64);
        
        String[] input = new String[]{"0", "1920", "15", "640"};
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE);
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.infect(Integer.parseInt(input[1]), grid, xbin, ybin, zbin, Afumigatus.RESTING_CONIDIA, Constants.CONIDIA_INIT_IRON, -1, false);
        initialize.setQuadrant(grid, xbin, ybin, zbin);
        initialize.initializePneumocytes(grid, xbin, ybin, zbin, Integer.parseInt(input[3]));
        initialize.initializeLiver(grid, xbin, ybin, zbin);
        initialize.initializeMacrophage(grid, xbin, ybin, zbin, Integer.parseInt(input[2]));
        initialize.initializeNeutrophils(grid, xbin, ybin, zbin, Integer.parseInt(input[0]));
        
        


        Recruiter[] recruiters = new Recruiter[1];
        recruiters[0] = new MacrophageRecruiter();
        //recruiters[1] = new NeutrophilRecruiter();
        
        
        //ile file = new File("/Users/henriquedeassis/Documents/Projects/Afumigatus/data/hemorrhage/outWT_" + args[10] + ".tsv");
        
        run.run(
        		2160,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		null,
        		-1,
        		stat
        );
        
        stat.close();
	}
	
	
	/*private static void runDummyCovid(String[] args) throws InterruptedException {
		Initialize initialize = new InitializeDummyCovidModel();
		Run run = new RunSingleThread();
		PrintStat stat = new PrintDummyCovid();
		
		
		int xbin = 30;
		int ybin = 30;
		int zbin = 30;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE);
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        int nMacrophage = 15;
        int nPnemocytes = 1000;
        double infectionLevel = Double.parseDouble(args[0]);
        Constants.MAX_MA *= 9;
        Constants.MAX_N *= 9;
        Constants.MAX_NK *= 9;
        Constants.DEFAULT_VIRAL_REPLICATION_RATE = Double.parseDouble(args[1]);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.initializePneumocytes(grid, xbin, ybin, zbin, nPnemocytes);
        initialize.initializeMacrophage(grid, xbin, ybin, zbin, nMacrophage);
        ((InitializeDummyCovidModel) initialize).infect(infectionLevel, grid, 5, 5, 5);
        initialize.setQuadrant(grid, xbin, ybin, zbin);


        Recruiter[] recruiters = new Recruiter[3];
        recruiters[0] = new MacrophageRecruiter();
        recruiters[1] = new NeutrophilRecruiter();
        recruiters[2] = new NKRecruiter();
        
        run.run(
        		720,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		null,
        		-1,
        		stat
        );
        
        stat.close();
        //System.out.println((toc - tic));
	}*/
	
	/*private static void learnReplicationRate(String[] args) throws Exception{
		InitializeCovidModel initialize = new InitializeCovidModel();
		Run run = new RunSingleThread();
		PrintLearnTCID50 stat = new PrintLearnTCID50();
		PrintNull statNull = new PrintNull();
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        String[] input = new String[]{"0", "0", "234", "80"};
        Constants.MIN_MA = 0;
        
        Constants.TURNOVER_RATE = 0;
        Constants.MAX_VIRAL_LOAD = Double.parseDouble(args[0]);//7.2e-4;//2.02e-04;
        Constants.SarsCoV2_REP_RATE = 1 + Double.parseDouble(args[1]);
        Constants.SarsCoV2_HALF_LIFE = 0.9909981;
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE); 
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeCovid(grid, xbin, ybin, zbin, diffusion, 6.644518e-26);
        initialize.initializePneumocytes(grid, xbin, ybin, zbin, Integer.parseInt(input[3]));
        //initialize.initializeMacrophage(grid, xbin, ybin, zbin, Integer.parseInt(input[2]));
        //initialize.initializeEndothelialCells(Integer.parseInt(input[3]), grid, xbin, ybin, zbin);
        initialize.setQuadrant(grid, xbin, ybin, zbin);
        stat.grid = grid;
        

        Recruiter[] recruiters = new Recruiter[0];
        //recruiters[0] = new MacrophageMCP1Recruiter();
        //recruiters[1] = new NeutrophilRecruiter();
        //recruiters[0] = new MacrophageReplenisher();
        //recruiters[1] = new NeutrophilReplenisher();
        
        run.run(
        		30,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		null,
        		-1,
        		statNull
        );
        
        Molecule virus = SarsCoV2.getMolecule();
        
        for(int i = 0; i < xbin; i++) 
			for(int j = 0; j < ybin; j++) 
				for(int k = 0; k < zbin; k++) 
					virus.set(0, 0, i, j, k);
				
        
        run.run(
        		2160,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		new File("/Users/henriquedeassis/Documents/Projects/COVID19/data/learnGrowth_" + Integer.parseInt(args[2]) + ".tsv"), 
        		-1,
        		stat
        );
        
        stat.close();
	}
	
	
	private static void baseCovidModel(String[] args)  throws Exception{
		InitializeCovidModel initialize = new InitializeCovidModel();
		Run run = new RunSingleThread();
		PrintCovid stat = new PrintCovid();
		
		
		int xbin = 25;
		int ybin = 25;
		int zbin = 25;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        
        //IFN		IL6		IL10		MCP1	TNF
        //17921, 103231		316227		95480	885
        
        /*Constants.MA_TNF_QTTY = Double.parseDouble(args[0])*Constants.MA_TNF_QTTY;
        Constants.P_TNF_QTTY = Double.parseDouble(args[0])*Constants.P_TNF_QTTY;
        Constants.MA_IFN_QTTY = Double.parseDouble(args[1])*Constants.MA_IFN_QTTY;
        Constants.MA_TGF_QTTY = Double.parseDouble(args[2])*Constants.MA_TGF_QTTY;*/
        //int maNum = (int) (Double.parseDouble(args[0])*110);
        /*int nNum = (int) (Double.parseDouble(args[0])*400);
        int nkNum = (int) (Double.parseDouble(args[1])*250);
        Constants.Kd_SarsCoV2 = Double.parseDouble(args[2]) * Constants.Kd_SarsCoV2;
        Constants.LAC_QTTY = 1*Constants.LAC_QTTY;
        
        
        String[] input = new String[]{"0", nkNum + "", "80" + "", nNum + "", "10000"};
        Constants.MIN_MA = 80;
        Constants.MIN_NK = nkNum;
        
        
     
        
        
        
        
        
        Constants.MAX_MA = 80;
        Constants.MAX_N = nNum;
        Constants.MAX_NK = nkNum;
        Constants.SPACE_VOL = Constants.VOXEL_VOL * xbin * ybin * zbin;
        //Constants.RECRUITMENT_RATE = 0.0005*Constants.RECRUITMENT_RATE;
        //Constants.DEFENSIN_Kd = Constants.DEFENSIN_Kd;
        //Constants.D
        
        
        Constants.SarsCoV2_HALF_LIFE = 1;
        Constants.SarsCoV2_REP_RATE = 0.005311*2.0;//*1.9;//  0.009210062*2;
        Constants.MAX_VIRAL_LOAD = 2.382849e-21;//9.531395e-19;
        Constants.SarsCoV2_UPTAKE_QTTY = 1.66113e-24*1.0;
        
        Constants.TURNOVER_RATE = 0;//1.0 - Constants.TURNOVER_RATE;
        Constants.MAX_BN_ITERATIONS = 20;
        //Constants.MA_MOVE_RATE_ACT = 10*Constants.MA_MOVE_RATE_ACT;
        //Constants.PR_INF_DIE = 2.02e-04;
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE); 
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.initializePneumocytes(grid, xbin, ybin, zbin, Integer.parseInt(input[4]));
        initialize.initializeDC(grid, xbin, ybin, zbin, Integer.parseInt(input[2]));
        initialize.initializeNeutrophils(grid, xbin, ybin, zbin, Integer.parseInt(input[3]));
        initialize.initializeNK(grid, xbin, ybin, zbin, Integer.parseInt(input[1]));
        //initialize.initializeEndothelialCells(Integer.parseInt(input[3]), grid, xbin, ybin, zbin);
        initialize.setQuadrant(grid, xbin, ybin, zbin);
        //initialize.initializeCovid(grid, xbin, ybin, zbin, diffusion, 1.66113e-23);
        initialize.covidInfec(grid, xbin, ybin, zbin, diffusion, 10);
        stat.grid = grid;
        

        Recruiter[] recruiters = new Recruiter[2];
        recruiters[0] = new RecruitDC();
        recruiters[1] = new CovidNeutrophilReplenisher();
        //recruiters[2] = new NKRecruiter();
        //recruiters[0] = new MacrophageReplenisher();
        //recruiters[1] = new NeutrophilReplenisher();
        
        run.run(
        		2160,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		new File(args[3]), //new File("/Users/henriquedeassis/Documents/Projects/COVID19/data/covidModel.tsv"),
        		-1,
        		stat
        );
        
        stat.close();
	}
	
	
	private static void saCovidModel(String[] args)  throws Exception{
		InitializeCovidModel initialize = new InitializeCovidModel();
		Run run = new RunSingleThread();
		PrintCovid stat = new PrintCovid();
		
		
		int xbin = 25;
		int ybin = 25;
		int zbin = 25;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        
        //IFN		IL6		IL10		MCP1	TNF
        //17921, 103231		316227		95480	885
        
        /*Constants.MA_TNF_QTTY = Double.parseDouble(args[0])*Constants.MA_TNF_QTTY;
        Constants.P_TNF_QTTY = Double.parseDouble(args[0])*Constants.P_TNF_QTTY;
        Constants.MA_IFN_QTTY = Double.parseDouble(args[1])*Constants.MA_IFN_QTTY;
        Constants.MA_TGF_QTTY = Double.parseDouble(args[2])*Constants.MA_TGF_QTTY;*/
        //int maNum = (int) (Double.parseDouble(args[0])*110);
        
       
        
        
     
        
        /*Constants.SarsCoV2_REP_RATE = 0.005311*2.0 * Double.parseDouble(args[0]); 
        Constants.MAX_VIRAL_LOAD = 2.382849e-21 * Double.parseDouble(args[1]);
        Constants.SarsCoV2_UPTAKE_QTTY = 1.66113e-24;
        Constants.MCP1_HALF_LIFE = 1.0 + Math.log(0.5)/(30.0*Double.parseDouble(args[2]));
        Constants.MA_IFN_QTTY =  4.983902e-22 * Double.parseDouble(args[3]);
        Constants.DC_IFN_QTTY = 4.983902e-22 * Double.parseDouble(args[3]);
        Constants.Kd_IFNG = 3.0e-9;// * Double.parseDouble(args[4]); 
        Constants.Kd_SarsCoV2 = 1.58e-9 * Double.parseDouble(args[4]); 
        int nkNum = (int) (Double.parseDouble(args[5])*250);
        Constants.MAX_NK = nkNum; 
        Constants.MIN_NK = nkNum; 
        Constants.LAC_QTTY = 1.847e-17 / Double.parseDouble(args[6]);
        Constants.Kd_H2O2 = 125e-6;// * Double.parseDouble(args[8]); 
        Constants.H2O2_QTTY = 2.38e-15 * Double.parseDouble(args[7]); 
        Constants.H2O2_HALF_LIFE = 1.0+Math.log(0.5)/(7.5*Double.parseDouble(args[8])); 
        Constants.VIRAL_LAC_Kd = 2.980226e-07;// * Double.parseDouble(args[11]); 
        Constants.D = 16 * Double.parseDouble(args[9]);
        Constants.MA_MOVE_RATE_ACT = Constants.MA_MOVE_RATE_ACT * Double.parseDouble(args[10]);
        Constants.MA_MOVE_RATE_REST = Constants.MA_MOVE_RATE_ACT * Double.parseDouble(args[10]);
        Constants.PR_NK_KILL = Math.exp(-1);//Double.parseDouble(args[14]);
        Constants.NEUTROPHIL_HALF_LIFE = -Math.log(0.5)/(180.0 * Double.parseDouble(args[11]));
        int nNum = (int) (Double.parseDouble(args[12])*400);
        Constants.MAX_N = nNum;
        Constants.MAX_MA = 80 * Double.parseDouble(args[13]);
        Constants.MIN_MA = 80 * Double.parseDouble(args[13]);

        String[] input = new String[]{"0", nkNum + "", "80" + "", nNum + "", "10000"};
        
        Constants.SPACE_VOL = Constants.VOXEL_VOL * xbin * ybin * zbin;
        //Constants.RECRUITMENT_RATE = 0.0005*Constants.RECRUITMENT_RATE;
        //Constants.DEFENSIN_Kd = Constants.DEFENSIN_Kd;
        //Constants.D
        
        
        Constants.SarsCoV2_HALF_LIFE = 1;
        
        Constants.TURNOVER_RATE = 0;//1.0 - Constants.TURNOVER_RATE;
        Constants.MAX_BN_ITERATIONS = 20;
        //Constants.MA_MOVE_RATE_ACT = 10*Constants.MA_MOVE_RATE_ACT;
        //Constants.PR_INF_DIE = 2.02e-04;
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE); 
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.initializePneumocytes(grid, xbin, ybin, zbin, Integer.parseInt(input[4]));
        initialize.initializeDC(grid, xbin, ybin, zbin, Integer.parseInt(input[2]));
        initialize.initializeNeutrophils(grid, xbin, ybin, zbin, Integer.parseInt(input[3]));
        initialize.initializeNK(grid, xbin, ybin, zbin, Integer.parseInt(input[1]));
        //initialize.initializeEndothelialCells(Integer.parseInt(input[3]), grid, xbin, ybin, zbin);
        initialize.setQuadrant(grid, xbin, ybin, zbin);
        //initialize.initializeCovid(grid, xbin, ybin, zbin, diffusion, 1.66113e-23);
        initialize.covidInfec(grid, xbin, ybin, zbin, diffusion, 10);
        stat.grid = grid;
        

        Recruiter[] recruiters = new Recruiter[2];
        recruiters[0] = new RecruitDC();
        recruiters[1] = new CovidNeutrophilReplenisher();
        //recruiters[2] = new NKRecruiter();
        //recruiters[0] = new MacrophageReplenisher();
        //recruiters[1] = new NeutrophilReplenisher();
        
        run.run(
        		2160,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		new File(args[14]), //new File("/Users/henriquedeassis/Documents/Projects/COVID19/data/covidModel.tsv"),
        		-1,
        		stat
        );
        
        stat.close();
	}
	
	private static void covidFit(String[] args)  throws Exception{
		InitializeCovidModel initialize = new InitializeCovidModel();
		Run run = new RunSingleThread();
		PrintCovid stat = new PrintCovid();
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        
        //IFN		IL6		IL10		MCP1	TNF
        //17921, 103231		316227		95480	885
        
        /*Constants.MA_TNF_QTTY = Double.parseDouble(args[0])*Constants.MA_TNF_QTTY;
        Constants.P_TNF_QTTY = Double.parseDouble(args[0])*Constants.P_TNF_QTTY;
        Constants.MA_IFN_QTTY = Double.parseDouble(args[1])*Constants.MA_IFN_QTTY;
        Constants.MA_TGF_QTTY = Double.parseDouble(args[2])*Constants.MA_TGF_QTTY;*/
        //int maNum = (int) (Double.parseDouble(args[0])*110);
        
        
        /*String[] input = new String[]{"0", "", "80" + "", "", "100"};
        Constants.MIN_MA = 80;
        Constants.MIN_NK = -1;
        
     
        
        
        
        
        
        Constants.MAX_MA = 80;
        Constants.MAX_N = -1;
        Constants.MAX_NK = -1;
        Constants.SPACE_VOL = Constants.VOXEL_VOL * xbin * ybin * zbin;
        //Constants.RECRUITMENT_RATE = 0.0005*Constants.RECRUITMENT_RATE;
        //Constants.DEFENSIN_Kd = Constants.DEFENSIN_Kd;
        //Constants.D
        
        
        Constants.SarsCoV2_HALF_LIFE = 1;
        Constants.SarsCoV2_REP_RATE = 0.005311*2.0;//*1.9;//  0.009210062*2;
        Constants.MAX_VIRAL_LOAD = 2.382849e-21;//9.531395e-19;
        Constants.SarsCoV2_UPTAKE_QTTY = 1.66113e-24;
        
        Constants.TURNOVER_RATE = 0;//1.0 - Constants.TURNOVER_RATE;
        Constants.MAX_BN_ITERATIONS = 20;
        //Constants.MA_MOVE_RATE_ACT = 10*Constants.MA_MOVE_RATE_ACT;
        //Constants.PR_INF_DIE = 2.02e-04;
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE); 
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.initializePneumocytes(grid, xbin, ybin, zbin, Integer.parseInt(input[4]));
        //initialize.initializeEndothelialCells(Integer.parseInt(input[3]), grid, xbin, ybin, zbin);
        initialize.setQuadrant(grid, xbin, ybin, zbin);
        //initialize.initializeCovid(grid, xbin, ybin, zbin, diffusion, 1.66113e-23);
        initialize.covidInfec(grid, xbin, ybin, zbin, diffusion, 50);
        stat.grid = grid;
        

        Recruiter[] recruiters = new Recruiter[2];
        recruiters[0] = new RecruitDC();
        recruiters[1] = new CovidNeutrophilReplenisher();
        //recruiters[2] = new NKRecruiter();
        //recruiters[0] = new MacrophageReplenisher();
        //recruiters[1] = new NeutrophilReplenisher();
        
        run.run(
        		2160,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		null,//new File(args[3]), //new File("/Users/henriquedeassis/Documents/Projects/COVID19/data/covidModel.tsv"),
        		-1,
        		stat
        );
        
        stat.close();
	}
	
	private static void covidPilot(String[] args)  throws Exception{
		InitializeCovidModel initialize = new InitializeCovidModel();
		Run run = new RunSingleThread();
		PrintCovid stat = new PrintCovid();
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        
        //IFN		IL6		IL10		MCP1	TNF
        //17921, 103231		316227		95480	885
        
        /*Constants.MA_TNF_QTTY = Double.parseDouble(args[0])*Constants.MA_TNF_QTTY;
        Constants.P_TNF_QTTY = Double.parseDouble(args[0])*Constants.P_TNF_QTTY;
        Constants.MA_IFN_QTTY = Double.parseDouble(args[1])*Constants.MA_IFN_QTTY;
        Constants.MA_TGF_QTTY = Double.parseDouble(args[2])*Constants.MA_TGF_QTTY;*/
        //int maNum = (int) (Double.parseDouble(args[0])*110);
        /*int nNum = (int) (Double.parseDouble(args[0])*60);
        int nkNum = (int) (Double.parseDouble(args[1])*12.5);
        Constants.Kd_SarsCoV2 = Double.parseDouble(args[2]) * Constants.Kd_SarsCoV2;
        
        
        String[] input = new String[]{"0", nkNum + "", "80" + "", nNum + "", "640"};
        Constants.MIN_MA = 80;
        Constants.MIN_NK = nkNum;
        
     
        
        
        
        
        
        Constants.MAX_MA = 80;
        Constants.MAX_N = nNum;
        Constants.MAX_NK = nkNum;
        Constants.SPACE_VOL = Constants.VOXEL_VOL * xbin * ybin * zbin;
        //Constants.RECRUITMENT_RATE = 0.0005*Constants.RECRUITMENT_RATE;
        //Constants.DEFENSIN_Kd = Constants.DEFENSIN_Kd;
        //Constants.D
        
        
        Constants.SarsCoV2_HALF_LIFE = 1;
        Constants.SarsCoV2_REP_RATE = 0.005311*2.0;//*1.9;//  0.009210062*2;
        Constants.MAX_VIRAL_LOAD = 2.382849e-21;//9.531395e-19;
        Constants.SarsCoV2_UPTAKE_QTTY = 1.66113e-24*1.5;
        
        Constants.TURNOVER_RATE = 0;//1.0 - Constants.TURNOVER_RATE;
        Constants.MAX_BN_ITERATIONS = 20;
        //Constants.MA_MOVE_RATE_ACT = 10*Constants.MA_MOVE_RATE_ACT;
        //Constants.PR_INF_DIE = 2.02e-04;
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE); 
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.initializePneumocytes(grid, xbin, ybin, zbin, Integer.parseInt(input[4]));
        initialize.initializeDC(grid, xbin, ybin, zbin, Integer.parseInt(input[2]));
        initialize.initializeNeutrophils(grid, xbin, ybin, zbin, Integer.parseInt(input[3]));
        initialize.initializeNK(grid, xbin, ybin, zbin, Integer.parseInt(input[1]));
        //initialize.initializeEndothelialCells(Integer.parseInt(input[3]), grid, xbin, ybin, zbin);
        initialize.setQuadrant(grid, xbin, ybin, zbin);
        //initialize.initializeCovid(grid, xbin, ybin, zbin, diffusion, 1.66113e-23);
        initialize.covidInfec(grid, xbin, ybin, zbin, diffusion, 5);
        stat.grid = grid;
        

        Recruiter[] recruiters = new Recruiter[2];
        recruiters[0] = new RecruitDC();
        recruiters[1] = new CovidNeutrophilReplenisher();
        //recruiters[2] = new NKRecruiter();
        //recruiters[0] = new MacrophageReplenisher();
        //recruiters[1] = new NeutrophilReplenisher();
        
        run.run(
        		2160,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		new File("/Users/henriquedeassis/Documents/Projects/COVID19/data/" + args[3]), //new File("/Users/henriquedeassis/Documents/Projects/COVID19/data/covidModel.tsv"),
        		-1,
        		stat
        );
        
        stat.close();
	}
	
	
	
	private static void neutrophilsCytotoxicity(String[] args)  throws Exception{
		InitializeCytotoxicity initialize = new InitializeCytotoxicity();
		Run run = new RunSingleThread();
		PrintCitotoxicity stat = new PrintCitotoxicity();
		
		
		int xbin = 25;
		int ybin = 25;
		int zbin = 25;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        
        String[] input = new String[]{"0", "0", "667", "67"};

        
        Constants.SPACE_VOL = Constants.VOXEL_VOL * xbin * ybin * zbin;

        
        
        Constants.SarsCoV2_HALF_LIFE = 1;
        
        Constants.TURNOVER_RATE = 0;//1.0 - Constants.TURNOVER_RATE;

        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE); 
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.initializePneumocytes(grid, xbin, ybin, zbin, Integer.parseInt(input[3]));
        initialize.initializeNeutrophils(grid, xbin, ybin, zbin, Integer.parseInt(input[2]));
        initialize.setQuadrant(grid, xbin, ybin, zbin);
        stat.grid = grid;
        

        Recruiter[] recruiters = new Recruiter[0];
        
        run.run(
        		480,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		null, //new File("/Users/henriquedeassis/Documents/Projects/COVID19/data/" + args[3]), //new File("/Users/henriquedeassis/Documents/Projects/COVID19/data/covidModel.tsv"),
        		-1,
        		stat
        );
        
        stat.close();
	}
	
	
	private static void baseCoinjury(String[] args) throws InterruptedException {
		InitializeCoinjury initialize = new InitializeCoinjury();
		Run run = new RunSingleThread();
		PrintCoinjury stat = new PrintCoinjury();
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;
		int xquadrant = 3;
        int yquadrant = 3;
        int zquadrant = 3;
        
        //int pne = (int) (xbin * ybin * zbin  * 0.64);
        
        String[] input = new String[]{"0", "20", args[3], "640", "0"};
        //Constants.MAX_N = Constants.MAX_N;
        //Constants.MAX_MA = Constants.MAX_MA;
        
        //Constants.Kd_Granule = 2e12;
        
        //Constants.ITER_TO_GROW_FAST = Integer.parseInt(args[0]);
        Constants.ITER_TO_GROW = Integer.parseInt(args[0]);
        
        //Constants.Kd_Granule = Double.parseDouble(args[1]) * 2e12;
        
        Constants.PR_N_HYPHAE = Double.parseDouble(args[1])*Constants.PR_N_HYPHAE;
        Constants.PR_MA_HYPHAE = Double.parseDouble(args[2])*Constants.PR_MA_HYPHAE;
        //Constants.MA_MOVE_RATE_ACT = Constants.MA_MOVE_RATE_ACT*10;
        //Constants.MA_MOVE_RATE_REST = Constants.MA_MOVE_RATE_ACT*10;
        
        //Constants.NEUTROPHIL_HALF_LIFE =  - Math.log(0.5) / (Double.parseDouble(args[3]) * (Constants.HOUR/((double) Constants.TIME_STEP_SIZE)));
        
        //Constants.Granule_HALF_LIFE = - Math.log(0.5) / (Double.parseDouble(args[4]) * (Constants.HOUR/((double) Constants.TIME_STEP_SIZE)));
        
        Constants.MAX_MA = Integer.parseInt(args[3]);
        Constants.MAX_N = Integer.parseInt(args[4]);
        
        
        
        
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE);
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        Voxel[][][] grid = initialize.createPeriodicGrid(xbin, ybin, zbin);
        List<Quadrant> quadrants = initialize.createQuadrant(grid, xbin, ybin, zbin, xquadrant, yquadrant, zquadrant);
        initialize.initializeMolecules(grid, xbin, ybin, zbin, diffusion, false);
        initialize.initializePneumocytes(grid, xbin, ybin, zbin, Integer.parseInt(input[3]));
        //initialize.initializeLiver(grid, xbin, ybin, zbin);
        initialize.initializeMacrophage(grid, xbin, ybin, zbin, Integer.parseInt(input[2]));
        initialize.initializeNeutrophils(grid, xbin, ybin, zbin, Integer.parseInt(input[4]));
        initialize.infect(Integer.parseInt(input[1]), grid, xbin, ybin, zbin, Afumigatus.HYPHAE, Constants.CONIDIA_INIT_IRON, -1, false);
        initialize.setQuadrant(grid, xbin, ybin, zbin);
        //stat.setGrid(grid);


        Recruiter[] recruiters = new Recruiter[2];
        recruiters[0] = new MacrophageReplenisher();
        recruiters[1] = new NeutrophilReplenisher();
        
        
        
        run.run( 
        		720,//(int) 72*2*(30/Constants.TIME_STEP_SIZE),  
        		xbin, 
        		ybin, 
        		zbin, 
        		grid, 
        		quadrants, 
        		recruiters,
        		false,
        		null, //new File("/Users/henriquedeassis/eclipse-workspace/jISS_legacy/sample/out_" + args[5]), //new File("/Users/henriquedeassis/Documents/Projects/COVID19/data/baseClock.tsv"),
        		-1,
        		stat
        );
        
        stat.close();

        //System.out.println((toc - tic));
	}
	
	*/
	
	
	//THIS EXPERIMEENT IS NOT REPRODUCING OLD RESULT BECAUSE THERE IS NO TURN_OVER_RATE. WHICH TURN OUT TO BE MORE IMPORTANT THAN HALF LIFE

	public static void main(String[] args) throws Exception {
		//Turnover and degrade changed: Turnover now is automatic. In the future there should be turnover and then degrade in the serum.
		//Current Turnover rate is wrong. Should it be (1-Turnover_rate)?
		System.out.println("jISS");
		//Main.baseInvitroModel(new String[]{"64", args[0], args[1], args[2]});
		//Main.runHemeInVitro101221();
		///Main.runExperiment101221();
		//Main.runSA092221(args);
		//System.out.println("hello word!");
		//Main.runDummyCovid(args);
		//args = new String[] {"1", "1", "1"};
		//args = new String[] {"1.62489565399917", "3.79496365105168", "0.361375652696199", "0.958874000197507", "1.10247187374883", "0.70986409581613", "3.38225794654421", "1.27171492281", "1.80649492166268", "2.25767252319005", "0.440499686750385", "2.50927171531779", "3.26703388702485", "0.718551707413203", "1.72616948763091", "3.0497288483525", "1.53019227580619", "0.392280692899905", "1.80643602247491"};
		long tic = System.currentTimeMillis();
		//Main.baseCoinjury(new String[]{"29",   "1",   "1", "0", "488"});
		//Main.baseModel(args);
		Main.tranexamicAcidModel(args);
		long toc = System.currentTimeMillis();
		System.out.println((toc - tic));
	}

}


//15310, 28325, 54318