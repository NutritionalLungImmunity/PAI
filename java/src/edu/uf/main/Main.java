package edu.uf.main;

import edu.uf.Diffusion.Diffuse;
import edu.uf.Diffusion.FADIPeriodic;
import edu.uf.compartments.GridFactory;
import edu.uf.compartments.MacrophageRecruiter;
import edu.uf.compartments.NeutrophilRecruiter;
import edu.uf.compartments.Recruiter;
import edu.uf.compartments.Voxel;
import edu.uf.control.Exec;
import edu.uf.intracellularState.AspergillusIntracellularModel;
import edu.uf.main.initialize.Initialize;
import edu.uf.main.initialize.InitializeBaseModel;
import edu.uf.main.initialize.InitializeExample;
import edu.uf.main.initialize.InitializeTranexamicModel;
import edu.uf.main.print.PrintBaseModel;
import edu.uf.main.print.PrintExample;
import edu.uf.main.print.PrintHemeModel;
import edu.uf.main.print.PrintStat;
import edu.uf.main.run.Run;
import edu.uf.main.run.RunSingleThread;
import edu.uf.utils.Constants;

public class Main {
	
	private static void baseModel(String[] args) throws InterruptedException {
			
		Initialize initialize = new InitializeBaseModel();
		Run run = new RunSingleThread();
		PrintBaseModel stat = new PrintBaseModel();
		
		
		int xbin = 10;
		int ybin = 10;
		int zbin = 10;

        
        String[] input = new String[]{"0", "1920", "15", "640"};
        
        
        
        double f = 0.1;
        double pdeFactor = Constants.D/(30/Constants.TIME_STEP_SIZE); 
        double dt = 1;
        Diffuse diffusion = new FADIPeriodic(f, pdeFactor, dt);
        
        initialize.createPeriodicGrid(xbin, ybin, zbin);
        initialize.initializeMolecules(diffusion, false);
        initialize.initializePneumocytes(Integer.parseInt(input[3]));
        initialize.initializeMacrophage(Integer.parseInt(input[2]));
        initialize.initializeNeutrophils(0);
        initialize.infect(Integer.parseInt(input[1]), AspergillusIntracellularModel.RESTING_CONIDIA, Constants.CONIDIA_INIT_IRON, -1, false);
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
	}
	
	
	
	public static void main(String[] args) throws Exception {
		System.out.println("jISS");
		long tic = System.currentTimeMillis();
		Main.baseModel(args);
		long toc = System.currentTimeMillis();
		System.out.println("Total: " + (toc - tic));
		System.out.println("Voxel.Interact: " + Exec.interactTime);
		System.out.println("Exec.gc: " + Exec.gcTime);
		System.out.println("Voxel.next: " + Exec.nextTime);
		System.out.println("Molecule.degrade: " + Exec.degradeTime);
		System.out.println("Exec.next: " + RunSingleThread.eNextTime);
		System.out.println("Exec.recruit: " + RunSingleThread.recruitTime);
		System.out.println("Exec.diffusion: " + RunSingleThread.diffusionTime);
		
		
	}

}


//15310, 28325, 54318
