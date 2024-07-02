package edu.uf.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.uf.compartments.Voxel;
import edu.uf.interactable.Cell;
import edu.uf.interactable.Chemokine;
import edu.uf.interactable.Molecule;

public class Util {
	
	/**
	 * Computes the Michaelian kinetics given two reactants, "substrate1" and "substrate2" assuming Kcat = 1 and reaction volume  = 6.4e-11  (Voxel volume). 
	 * This method treats the less concentrated as the enzyme and the more concentrated as the substrate.
	 * <br/><br/>
	 * v = (substrate1-concentration * substrate2-concentration) / (km + [more-concentrated-substrate])
	 * <br/><br/>
	 * substrate-concentration = substrate/6.4e-11 (For example, substrate1-concentration = substrate1/6.4e-11)
	 * @param substrate1 a reactant
	 * @param substrate2 another reactant
	 * @param km Michaelian-constant
	 * @param h integration factor
	 * @return
	 */
	public static double michaelianKinetics(
			double substrate1, 
			double substrate2, 
			double km, 
			double h) {
		return michaelianKinetics(substrate1, substrate2, km, h, 1, Constants.VOXEL_VOL);
	}
	
	/**
	 * Computes the Michaelian kinetics given two reactants, "substrate1" and "substrate2." 
	 * This method treats the less concentrated as the enzyme and the more concentrated as the substrate.
	 * <br/><br/>
	 * v = (Kcat * substrate1-concentration * substrate2-concentration) / (km + [more-concentrated-substrate])
	 * <br/><br/>
	 * substrate-concentration = substrate/v (For example, substrate1-concentration = substrate1/v)
	 * @param substrate1 a reactant
	 * @param substrate2 another reactant
	 * @param km Michaelian-constant
	 * @param h integration factor
	 * @param Kcat Catalitic-constant
	 * @param v space volume (Usually the voxel volume - 6.4e-11 L)
	 * @return
	 */
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
	
	/**
	 * Computes the function: p = 1 - exp(-(x/6.4e-11)/kd)
	 * Where "p" is the probability of returning "true."
	 * @param x
	 * @param kd
	 * @return
	 */
	public static boolean activationFunction(double x, double kd) {
		return activationFunction(x, kd, Constants.VOXEL_VOL);
	}
	
	/**
	 * Computes the function:
	 * <pre>
	 * 	    | 1 if f(x) <= 0.35
	 *	    | 2 if f(x) >  0.35 & f(x) <= 0.69
	 *	y = | 3 if f(x) >  0.69 & f(x) <= 0.90
	 *	    | 4 if f(x) >  0.90
	 *	    | 0 otherwise
	 * </pre>
	 * f(x) = 1 - exp(-(x/6.4e-11)/kd)
	 * @param x quantity of activation molecule
	 * @param kd of the activation molecule
	 * @return
	 */
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
	
	/**
	 * Computes the function: p = 1 - exp(-(x/v)/kd)
	 * Where "p" is the probability of returning "true."
	 * @param x
	 * @param kd
	 * @param v
	 * @return
	 */
	public static boolean activationFunction(double x, double kd, double v) {
		return activationFunction(x, kd, v, 1.0) > Rand.getRand().randunif();
	}
	
	/**
	 * Computes the function: y = 1 - b*exp(-(x/v)/kd)
	 * @param x
	 * @param kd
	 * @param v
	 * @param b
	 * @return
	 */
	public static double activationFunction(double x, double kd, double v, double b) {
		return activationFunction(x, kd, v, b, 1);
	}
	
	/**
	 * Computes the function: y = h * (1 - b*exp(-(x/v)/kd))
	 * @deprecated: this function  should not be used unless h = 1.
	 * @param x
	 * @param kd
	 * @param v
	 * @param b
	 * @param h integration factor
	 * @return
	 */
	public static double activationFunction(double x, double kd, double v, double b, double h) {
		x = x/v;
		return h * (1 - b*Math.exp(-(x/kd)));
	}
	 /**
	  * computes the function: p = 1 - exp(-x*dt/kd).
	  * Where "p" is the probability of returning "true."
	  * <strong> x is the concentration. Not quantity. 
	  * <br/><br/>
	  * Not used!</strong>
	  * @param x concentration
	  * @param kd
	  * @param dt time interval (probably usually 2 min).
	  * @return
	  */
	public static boolean ctActivationFunction(double x, double kd, double dt) {
		return (1 - Math.exp(-(x*dt/kd))) > Rand.getRand().randunif();
	}
	
	/**
	 * compute the function: y = h * (1 - b + b * exp(-(x/v)/kd))
	 * <br/><br/>
	 * <strong>Not used!</strong>
	 * @deprecated: this function should not be used unless h = 1;
	 * @param x
	 * @param kd
	 * @param v
	 * @param b
	 * @param h integration factor
	 * @return
	 */
	public static double inactivationFunction1(double x, double kd, double v, double b, double h) {
		x = x/v;
		return h * (1 - b + b * Math.exp(-(x/kd)));
	}

	/**
	 * computes the function: y  = p0 * (1 + b * (1 - exp(-(x/v)/kd)))
	 * where: b = (1.0 - p0)/p0
	 * <br/><br/>
	 * <strong>Not used!</strong>
	 * @param x
	 * @param kd
	 * @param v
	 * @param p0
	 * @return
	 */
	public static double tranexamicActivation(double x, double kd, double v, double p0) {
		x = x/v;
		double b = (1.0 - p0)/p0;
		return p0 * (1 + b * ( 1 - Math.exp(-(x/kd))));
	}
	
	/**
	 * if x and xSystem not equal zero, computes the function: y = [(x - xSystem) * exp(TURNOVER_RATE * REL_CYT_BIND_UNIT_T) + xSystem]/x
	 * Where: xSystem = xSystem/VOXEL_VOL and x = x/VOXEL_VOL
	 * <br/><br/>
	 * <strong>Only used in Liver. Not used!</strong>
	 * @param x
	 * @param xSystem
	 * @return
	 */
	public static double turnoverRate(double x, double xSystem) {
		return Util.turnoverRate(
				x, xSystem, Constants.TURNOVER_RATE, Constants.REL_CYT_BIND_UNIT_T, Constants.VOXEL_VOL
		);
	}
	
	/**
	 * if x and xSystem not equal zero, computes the function: y = [(x - xSystem) * exp(k * t) + xSystem]/x
	 * Where: xSystem = xSystem/v and x = x/v
	 * <br/><br/>
	 * <strong>Only used in Liver. Not used!</strong>
	 * @param x
	 * @param xSystem
	 * @return
	 */
    public static double turnoverRate(double x, double xSystem, double k, double t, double v) {

        if (x == 0 && xSystem == 0) 
            return 0;
        x = x / v;  //# CONVERT MOL TO MOLAR
        xSystem = xSystem / v;
        double y = (x - xSystem)*Math.exp(-k*t) + xSystem; 
        return y / x;
    }
    
    
    /**
     * Given an initial amount of Fe, Apo-Tf, and TfFe computes the final relative amount of TfFe, 
     * supposing that all the Fe, up to the maximum Transferrin (Tf) capacity, will be chelated. 
     * This method uses a 3rd-degree polynomial as a surrogate model.  
     * @param iron
     * @param Tf
     * @param TfFe
     * @return
     */
    public static double ironTfReaction(double iron, double Tf, double TfFe) {
        double totalBindingSite = 2*(Tf + TfFe); //# That is right 2*(Tf + TfFe)!
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
    
    /**
     * Given approximate coordinates, find a voxel where those coordinates belong. 
     * <strong>WARNING: This method may not work and even prevent some features of 
     * periodic boundary conditions.</strong>
     * @param x
     * @param y
     * @param z
     * @param xbin
     * @param ybin
     * @param zbin
     * @param grid
     * @return
     */
    public static Voxel findVoxel(double x, double y, double z, int xbin, int ybin, int zbin, Voxel[][][] grid) {
    	int xx = (int) x;
    	int yy = (int) y;
    	int zz = (int) z;
    	if (xx < 0 || yy < 0 || zz < 0)
    		return null; //CHECK THIS MAY PREVENT PERIODIC BOUNDARY???
        return grid[(xx%xbin)][(yy%ybin)][(zz%zbin)];
    }
    
    /**
     * Select the chemokine that attracts cell "cell" and compute its 
     * chemoattractive power given its concentration. 
     * @param voxel
     * @param agent
     * @return
     */
    public static double getChemoattraction(Voxel voxel, Cell agent) {
    	if(agent.attractedBy() == null)
    		return Constants.DRIFT_BIAS;
    	
    	Chemokine chemokine = (Chemokine) voxel.getMolecules().get(agent.attractedBy());
    	return chemokine.chemoatract(voxel.getX(), voxel.getY(), voxel.getZ());
    }
    
    /**
     * Computes the weights of the voxels in the neighborhood of the Voxel 
     * in which the cell "agent" is currently in. The weights are proportional 
     * to the concentration of chemokine in each voxel. 
     * @param voxel
     * @param agent
     */
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
    
    /**
     * Select a voxel in the neighborhood of the Voxel in which the cell "agent" 
     * is currently in. The probability of a voxel being selected is proportional 
     * to its weight. The weight must have been computed with "calcDriftProbability" 
     * and is proportional to the concentration of chemokine in the voxel. 
     * This method works as a roulette. "P" is a random number between 
     * zero and sum(weight-in-the-neighbohood). The method iterates through the voxels 
     * in the neighborhood and sums their weights into a variable "cumP" as soon as 
     * "cumP" becomes larger or equal to "P" that voxel is selected. 
     * @param voxel the current Voxel. The current Voxel is also considered a neighbor of itself in this method.
     * @param P a random number between zero and sum(weight-in-the-neighbohood) for the roulette.
     * @return
     */
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
    
	
}
