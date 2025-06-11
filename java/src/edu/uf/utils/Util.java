package edu.uf.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.uf.compartments.Voxel;
import edu.uf.interactable.Cell;
import edu.uf.interactable.Chemokine;

public class Util {
	
	/**
	 * Computes the reaction rate using Michaelis-Menten kinetics for two reactants, {@code substrate1} and {@code substrate2}, 
	 * assuming {@code Kcat = 1} and a fixed reaction volume of {@code 6.4e-11} (i.e., the volume of a voxel).
	 *
	 * <p>The method treats the reactant with the lower concentration as the enzyme and the one with the higher concentration 
	 * as the substrate.</p>
	 *
	 * <p>The rate is computed as:</p>
	 * <pre>
	 * v = ([S1] * [S2]) / (km + [substrate])
	 * </pre>
	 * where [S1] and [S2] are the concentrations of {@code substrate1} and {@code substrate2}, computed as:
	 * <pre>
	 * Concentrations are computed as:
	 * [S1] = [S1] / 6.4e-11
	 * [S2] = [S2] / 6.4e-11
	 * </pre>
	 *
	 * @param substrate1 the first reactant
	 * @param substrate2 the second reactant
	 * @param km the Michaelis constant
	 * @param h the integration factor
	 * @return the computed reaction rate
	 */
	public static double michaelianKinetics(
			double substrate1, 
			double substrate2, 
			double km, 
			double h) {
		return michaelianKinetics(substrate1, substrate2, km, h, 1, Constants.VOXEL_VOL);
	}
	
	/**
	 * Computes the reaction rate using Michaelis-Menten kinetics for two reactants, {@code substrate1} and {@code substrate2}.
	 *
	 * <p>The method assumes one reactant acts as the enzyme (the less concentrated one) and the other as the substrate 
	 * (the more concentrated one). Concentrations are derived from reactant amounts using the specified volume {@code v}.</p>
	 *
	 * <p>The reaction rate is calculated using the following equation:</p>
	 * <pre>
	 * v = (Kcat * [S1] * [S2]) / (km + [substrate])
	 * </pre>
	 * where [S1] and [S2] are the concentrations of {@code substrate1} and {@code substrate2}, computed as:
	 * <pre>
	 * * Concentrations are computed as:
	 * [S1] = [S1] / v
	 * [S2] = [S2] / v
	 * </pre>
	 *
	 * @param substrate1 the first reactant
	 * @param substrate2 the second reactant
	 * @param km the Michaelis constant
	 * @param h the integration factor
	 * @param Kcat the catalytic constant
	 * @param v the reaction volume (typically the voxel volume, e.g., 6.4e-11 L)
	 * @return the computed reaction rate
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
	 * Computes the probability {@code p} using the function:
	 * <pre>
	 * p = 1 - exp(-(x / 6.4e-11) / kd)
	 * </pre>
	 * where {@code p} represents the probability of returning {@code true}.
	 *
	 * @param x  the molecule quantity or binding input
	 * @param kd the dissociation constant
	 * @return the computed probability {@code p}
	 */
	public static boolean activationFunction(double x, double kd) {
		return activationFunction(x, kd, Constants.VOXEL_VOL);
	}
	
	/**
	 * Computes a discrete activation level {@code y} based on the activation function:
	 * 
	 * <pre>
	 * 		f(x) = 1 - exp(-(x / 6.4e-11) / kd)
	 * 
	 * 	    | 1 if f(x) <= 0.35
	 *	    | 2 if f(x) >  0.35 & f(x) <= 0.69
	 *	y = | 3 if f(x) >  0.69 & f(x) <= 0.90
	 *	    | 4 if f(x) >  0.90
	 *	    | 0 otherwise
	 * </pre>
	 *
	 * <p>This method maps the continuous output of {@code f(x)} into a discrete range [0–4] to represent
	 * graded activation levels.</p>
	 *
	 * @param x  the quantity of the activating molecule
	 * @param kd the dissociation constant of the activating molecule
	 * @return an integer between 0 and 4 representing the activation level
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
	 * Computes the probability {@code p} using the function:
	 * <pre>
	 * p = 1 - exp(-(x / v) / kd)
	 * </pre>
	 * where {@code p} represents the probability of returning {@code true}.
	 *
	 * @param x  the quantity of the activating molecule
	 * @param kd the dissociation constant
	 * @param v  the volume in which the molecule is distributed (e.g., voxel volume)
	 * @return the computed probability {@code p}
	 */
	public static boolean activationFunction(double x, double kd, double v) {
		return activationFunction(x, kd, v, 1.0) > Rand.getRand().randunif();
	}
	
	/**
	 * Computes the function:
	 * <pre>
	 * y = 1 - b * exp(-(x / v) / kd)
	 * </pre>
	 * where {@code y} represents a scaled activation or probability-like output.
	 *
	 * @param x  the quantity of the activating molecule
	 * @param kd the dissociation constant
	 * @param v  the volume in which the molecule is distributed (e.g., voxel volume)
	 * @param b  a scaling factor applied to the exponential term
	 * @return the computed value {@code y}
	 */
	public static double activationFunction(double x, double kd, double v, double b) {
		return activationFunction(x, kd, v, b, 1);
	}
	
	/**
	 * Computes the function:
	 * <pre>
	 * y = h * (1 - b * exp(-(x / v) / kd))
	 * </pre>
	 * 
	 * <p>This function models a scaled response or activation signal based on Michaelis-Menten-like kinetics, 
	 * with an additional scaling factor {@code h}.</p>
	 *
	 * @deprecated This method should not be used unless {@code h = 1}, as the integration factor may introduce unintended behavior.
	 *
	 * @param x  the quantity of the activating molecule
	 * @param kd the dissociation constant
	 * @param v  the volume in which the molecule is distributed (e.g., voxel volume)
	 * @param b  a scaling factor applied to the exponential term
	 * @param h  the integration factor
	 * @return the computed value {@code y}
	 */
	public static double activationFunction(double x, double kd, double v, double b, double h) {
		x = x/v;
		return h * (1 - b*Math.exp(-(x/kd)));
	}
	
	/**
	 * Computes the probability {@code p} using the function:
	 * <pre>
	 * p = 1 - exp(-x * dt / kd)
	 * </pre>
	 * where {@code p} represents the probability of returning {@code true}.
	 *
	 * <p><strong>Note:</strong> {@code x} represents the concentration (not quantity), and {@code dt} is the time interval 
	 * (typically around 2 minutes).</p>
	 *
	 * <p><strong>Warning: This method is currently not used.</strong></p>
	 *
	 * @param x  the concentration of the activating molecule
	 * @param kd the dissociation constant
	 * @param dt the time interval over which the probability is evaluated
	 * @return the computed probability {@code p}
	 */
	public static boolean ctActivationFunction(double x, double kd, double dt) {
		return (1 - Math.exp(-(x*dt/kd))) > Rand.getRand().randunif();
	}
	
	/**
	 * Computes the function:
	 * <pre>
	 * y = h * (1 - b + b * exp(-(x / v) / kd))
	 * </pre>
	 * 
	 * <p>This function represents a scaled response that incorporates both decay and amplification terms.</p>
	 *
	 * <p><strong>Not used!</strong></p>
	 *
	 * @deprecated This function should not be used unless {@code h = 1}, as the integration factor may produce unintended results.
	 *
	 * @param x  the quantity of the activating molecule
	 * @param kd the dissociation constant
	 * @param v  the reaction volume (e.g., voxel volume)
	 * @param b  a scaling factor applied to the exponential term
	 * @param h  the integration factor
	 * @return the computed value {@code y}
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
     * Estimates the final relative amount of transferrin-bound iron (TfFe) given initial amounts of free iron, 
     * apo-transferrin (Tf), and existing TfFe, assuming that all available iron (up to the transferrin binding capacity) 
     * will be chelated.
     *
     * <p>This method uses a third-degree polynomial surrogate model to approximate the outcome.</p>
     *
     * @param iron the initial amount of free iron
     * @param Tf   the initial amount of apo-transferrin (unbound transferrin)
     * @param TfFe the initial amount of iron-bound transferrin
     * @return the final relative amount of TfFe after chelation
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
     * Attempts to locate the voxel corresponding to the given approximate spatial coordinates.
     *
     * <p>This method uses the provided coordinates and binning resolution to identify the voxel 
     * in the 3D {@code grid} that contains the point ({@code x}, {@code y}, {@code z}).</p>
     *
     * <p><strong>WARNING:</strong> This method may be unreliable and could interfere with features 
     * that rely on periodic boundary conditions.</p>
     *
     * @param x     the x-coordinate
     * @param y     the y-coordinate
     * @param z     the z-coordinate
     * @param xbin  the bin size along the x-axis
     * @param ybin  the bin size along the y-axis
     * @param zbin  the bin size along the z-axis
     * @param grid  the 3D voxel grid
     * @return the voxel corresponding to the given coordinates, or {@code null} if not found
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
     * Selects the chemokine that attracts the given {@code agent} and computes its chemoattractive 
     * strength based on the chemokine's concentration in the specified {@code voxel}.
     *
     * @param voxel the voxel in which the chemokine concentration is measured
     * @param agent the cell or agent being evaluated for chemoattraction
     * @return the computed chemoattractive power for the agent in the given voxel
     */
    public static double getChemoattraction(Voxel voxel, Cell agent) {
    	if(agent.attractedBy() == null)
    		return Constants.DRIFT_BIAS;
    	
    	Chemokine chemokine = (Chemokine) voxel.getMolecules().get(agent.attractedBy());
    	return chemokine.chemoatract(voxel.getX(), voxel.getY(), voxel.getZ());
    }
    
    /**
     * Computes the weights of neighboring voxels based on the concentration of the chemokine 
     * that attracts the given {@code agent}.
     *
     * <p>The weights are assigned proportionally to the chemokine concentration in each neighboring voxel 
     * relative to the voxel in which the {@code agent} currently resides. These weights are typically used 
     * to bias movement or behavioral decisions based on chemotactic gradients.</p>
     *
     * @param voxel the current voxel occupied by the agent
     * @param agent the cell or agent influenced by chemokine concentration
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
     * Selects a voxel from the neighborhood of the given {@code voxel} based on chemokine-driven weights.
     *
     * <p>The probability of selecting each neighboring voxel (including the current one) is proportional 
     * to its chemokine-derived weight, which must have been previously computed using {@code calcDriftProbability}.</p>
     *
     * <p>This method implements a weighted roulette-wheel selection:  
     * a random number {@code P} is drawn from the interval {@code [0, totalWeight]}, where {@code totalWeight} is 
     * the sum of weights in the neighborhood. The method iterates over the neighboring voxels, 
     * accumulating weights into {@code cumP}, and selects the first voxel for which {@code cumP >= P}.</p>
     *
     * @param voxel the current voxel (also included as part of its own neighborhood)
     * @param P     a random number in the range [0, totalWeight] used for roulette selection
     * @return the selected voxel based on weighted probability
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
