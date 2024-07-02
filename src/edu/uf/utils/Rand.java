package edu.uf.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Rand {

	private static Rand rand;
	private SecureRandom secRand;
	
	//public static long seed = 0x23946298;
	
	private Rand() {
		this.secRand = new SecureRandom();
	}
	
	/**
	 * Get/create singleton Rand object.
	 * @return
	 */
	public static Rand getRand() {
		if(Rand.rand == null) {
			Rand.rand = new Rand();
		}
		return Rand.rand;
	}
	
	/**
	 * random number generator. usage: mean + std * randnorm()
	 * @return
	 */
	public double randnorm(){
		double u1, u2, v1=0, v2=0;
		double s=2;
		while(s>=1){
			u1=secRand.nextDouble();
			u2=secRand.nextDouble(); 
			v1=2.0*u1-1.0;
			v2=2.0*u2-1.0;
			s=v1*v1+v2*v2;
		};
		double x= v1*Math.sqrt((-2.0*Math.log(s))/s); 
		return x;
	}
	
	/**
	 * A Poisson distributed random number with mean lambda.
	 * @param lambda mean
	 * @return
	 */
	public int randpois(double lambda) {
		double L = Math.exp(-lambda);
	  	double p = 1.0;
	  	int k = 0;
	  	do {
	  		k++;
	  		p *= secRand.nextDouble();//Math.random();
	  	} while (p > L);

	  	return k - 1; 
	}
	
	/**
	 * Creates a sample, without replacement, of length "size" out of a list of length "max." 
	 * "size" cannot be larger than "max."
	 * <strong> Actually, I think this method only works if max==size. Currently, it is only really being 
	 * used this way. numSamples != -1 is never being used in the "Voxel" class. </strong>
	 * @param max
	 * @param size
	 * @return
	 */
	public int[] sample(int max, int size) {//int[] array) {
		if(max == 0 || size == 0) return new int[0];
		int[] array = new int[size];
 		List<Integer> indices = new ArrayList<>(size);
		for(int i = 0; i < size; i++)
			indices.add(i);
		
		int n = max;
		for(int i = 0; i < size; i++) {
			int k = secRand.nextInt(n--);
			int j = indices.remove(k);
			array[i] = j;
		}
		
		return array;
	}
	
	/**
	 * A uniformly distributed random number between 0 and 1.
	 * @return
	 */
	public double randunif() {
		return secRand.nextDouble();
	}
	
	/**
	 * A uniformly distributed random number between "min" and "max".
	 * @param min
	 * @param max
	 * @return
	 */
	public int randunif(int min, int max) {
		return secRand.nextInt(max - min) + min;
	}
	
}
