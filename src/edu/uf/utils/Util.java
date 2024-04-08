package edu.uf.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.uf.compartments.Voxel;
import edu.uf.interactable.Cell;
import edu.uf.interactable.Chemokine;
import edu.uf.interactable.Molecule;

public class Util {
	
	public static double michaelianKinetics(
			double substrate1, 
			double substrate2, 
			double km, 
			double h) {
		return michaelianKinetics(substrate1, substrate2, km, h, 1, Constants.VOXEL_VOL);
	}
	
	public static double michaelianKinetics(
			double substrate1, 
			double substrate2, 
			double km, 
			double h, 
			double Kcat, 
			double v) {// ### CAUTION CHANGED!!!!
		
		substrate1 = substrate1 / v; //# transform into M
		substrate2 = substrate2 / v;
		double substrate = 0;
		double enzime = 0;
		
		if(substrate1 > substrate2) {
			substrate = substrate1;
			enzime = substrate2;
		}else {
			substrate = substrate2;
			enzime = substrate1;
		}
		
		return h * Kcat * enzime * substrate * v  / (substrate + km); //# (*v) transform back into mol
	}
	
	/**
	 * print system clock
	 * @return
	 */
	
	
	
	public static String printServerTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = new Date();
		return sdf.format(now);
	}
	
	public static boolean activationFunction(double x, double kd) {
		return activationFunction(x, kd, Constants.VOXEL_VOL);
	}
	
	public static int activationFunction5(double x, double kd) {
		double d = activationFunction(x, kd, Constants.VOXEL_VOL, 1.0);
		if(Rand.getRand().randunif() > d)return 0;
		if(d <= 0.35)
			return 1;
		if(d <= 0.69)
			return 2;
		if(d <= 0.90)
			return 3;
		else
			return 4;
	}
	
	public static boolean activationFunction(double x, double kd, double v) {
		return activationFunction(x, kd, v, 1.0) > Rand.getRand().randunif();
	}
	
	public static double activationFunction(double x, double kd, double v, double b) {
		return activationFunction(x, kd, v, b, 1);
	}
	
	public static double activationFunction(double x, double kd, double v, double b, double h) {
		x = x/v;
		return h * (1 - b*Math.exp(-(x/kd)));
	}
	
	public static boolean ctActivationFunction(double x, double kd, double dt) {
		return (1 - Math.exp(-(x*dt/kd))) > Rand.getRand().randunif();
	}
	
	public static double inactivationFunction(double x, double kd, double v, double b, double h) {
		x = x/v;
		return h * (1 - b + b * Math.exp(-(x/kd)));
	}

	public static double tranexamicActivation(double x, double kd, double v, double p0) {
		x = x/v;
		double b = (1.0 - p0)/p0;
		return p0 * (1 + b * ( 1 - Math.exp(-(x/kd))));
	}
	
	public static double turnoverRate(double x, double xSystem) {
		return Util.turnoverRate(
				x, xSystem, Constants.TURNOVER_RATE, Constants.REL_CYT_BIND_UNIT_T, Constants.VOXEL_VOL
		);
	}
	
    //public double turnover_rate(x, x_system=0, k=Constants.TURNOVER_RATE, t=Constants.REL_CYT_BIND_UNIT_T, v=Constants.VOXEL_VOL):
    public static double turnoverRate(double x, double xSystem, double k, double t, double v) {

        if (x == 0 && xSystem == 0) 
            return 0;
        x = x / v;  //# CONVERT MOL TO MOLAR
        xSystem = xSystem / v;
        double y = (x - xSystem)*Math.exp(-k*t) + xSystem; 
        return y / x;
    }
    
    
    
    public static double ironTfReaction(double iron, double Tf, double TfFe) {
        double totalBindingSite = 2*(Tf + TfFe); //# That is wright 2*(Tf + TfFe)!
        double totalIron = iron + TfFe; //# it does not count TfFe2
        if(totalIron <= 0 || totalBindingSite <= 0) 
            return 0.0;
        double relTotalIron = totalIron/totalBindingSite;
        relTotalIron = relTotalIron <= 1.0 ? relTotalIron : 1.0;
        double relTfFe = Constants.P1*relTotalIron*relTotalIron*relTotalIron + 
        		Constants.P2*relTotalIron*relTotalIron + Constants.P3*relTotalIron;
        relTfFe = relTfFe > 0.0 ? relTfFe : 0.0;
        return relTfFe;
    }
    
    public static Voxel findVoxel(double x, double y, double z, int xbin, int ybin, int zbin, Voxel[][][] grid) {
    	int xx = (int) x;
    	int yy = (int) y;
    	int zz = (int) z;
    	if (xx < 0 || yy < 0 || zz < 0)
    		return null; //CHECK THIS MAY PREVENT PERIODIC BOUNDARY???
        return grid[(xx%xbin)][(yy%ybin)][(zz%zbin)];
    }
    
    public static double getChemoattraction(Voxel voxel, Cell agent) {
    	if(agent.attractedBy() == null)
    		return Constants.DRIFT_BIAS;
    	
    	Chemokine chemokine = (Chemokine) voxel.getMolecules().get(agent.attractedBy());
    	return chemokine.chemoatract(voxel.getX(), voxel.getY(), voxel.getZ());
    }
    
    public static void calcDriftProbability(Voxel voxel, Cell agent) {
    	
        double chemoattraction = Util.getChemoattraction(voxel, agent);

        voxel.setP(chemoattraction);// + () (0.0 if len(this.aspergillus) > 0 else 0.0)


        double cumP = voxel.getP();
        for(Voxel v : voxel.getNeighbors()) {
            chemoattraction = Util.getChemoattraction(voxel, agent);
            //System.out.println("chemo: " + chemoattraction);
            v.setP(chemoattraction);//  + (0.0 if len(this.aspergillus) > 0 else 0.0))if v.tissue_type != Voxel.AIR else 0.0
            cumP = cumP + v.getP();
        }
        voxel.setP(voxel.getP() / cumP);
        for(Voxel v : voxel.getNeighbors())
            v.setP(v.getP() / cumP);
    }
    
    public static Voxel getVoxel(Voxel voxel, double P) {
    	//System.out.println("# " + P + " " + voxel.getP());
        double cumP = voxel.getP();
        if(P <= cumP) 
            return voxel;
        for(Voxel v : voxel.getNeighbors()) {
        	//System.out.println("-> " + v.p);
            cumP = cumP + v.getP();
            if(P <= cumP)
                return v; 
        }

        return null;
    }
    
    public static void printIntArray(int[] array) {
    	for(int i : array)
    		System.out.print(i + " ");
    	System.out.println();
    }
    
    
    public static boolean bind(Cell cell, Molecule molecule, int x, int y, int z, int w) {
    	cell.bind(molecule, Util.activationFunction5(molecule.get(w, x, y, z), molecule.getKd()));
    	return true;
    }
    
    public static boolean secrete(Cell cell, Molecule molecule, double qtty, int x, int y, int z, int w) {
    	if (cell.hasPhenotype(molecule.getPhenotype()))
    		molecule.inc(qtty, w, x, y, z);
    	return true;
    }
    
	
}
