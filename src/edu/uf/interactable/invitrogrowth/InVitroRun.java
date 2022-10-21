package edu.uf.interactable.invitrogrowth;

import edu.uf.time.Clock;
import edu.uf.utils.Rand;

public class InVitroRun {

	public static void run(int iterations, int k, int numAfumigatus, double sqrt3_vol, int growthIteration, String file) {
		initialize(sqrt3_vol, numAfumigatus, growthIteration);
		
		for(int i = 0; i < iterations; i++) {
			long tic = System.currentTimeMillis();
			Field.getField().next(i, k);
			long toc = System.currentTimeMillis();
			System.out.println(i + "\t" + Afumigatus.getCellCount() + "\t" + Field.objects + "\t" + (toc - tic));
			Clock.updade();
		}
		
	}
	
	private static void initialize(double sqrt3_vol, int numAfumigatus, int growthIteration) {
		int xmin = (int) (Field.X_BIN/2 - sqrt3_vol/2);
		int xmax = (int) (Field.X_BIN/2 + sqrt3_vol/2);
		int ymin = (int) (Field.Y_BIN/2 - sqrt3_vol/2);
		int ymax = (int) (Field.Y_BIN/2 + sqrt3_vol/2);
		int zmin = (int) (Field.Z_BIN/2 - sqrt3_vol/2);
		int zmax = (int) (Field.Z_BIN/2 + sqrt3_vol/2);
		for(int i = 0; i < numAfumigatus; i++) {
			int x = Rand.getRand().randunif(xmin, xmax);
			int y = Rand.getRand().randunif(ymin, ymax);
			int z = Rand.getRand().randunif(zmin, zmax);
			
			Field.getField().setCell(new Afumigatus(x, y, z, x, y, z, Rand.getRand().randunif(), Rand.getRand().randunif(), Rand.getRand().randunif(), 
					growthIteration, 0.0, Afumigatus.RESTING_CONIDIA, 0, true));
		}
	}
	
}
