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
	 * Returns the singleton instance of the {@code Rand} class, creating it if it does not already exist.
	 *
	 * <p>This ensures that only one instance of the random number generator is used throughout the application, 
	 * following the Singleton design pattern.</p>
	 *
	 * @return the singleton {@code Rand} instance
	 */
	public static Rand getRand() {
		if(Rand.rand == null) {
			Rand.rand = new Rand();
		}
		return Rand.rand;
	}
	
	/**
	 * Generates a normally distributed random number with mean 0 and standard deviation 1 
	 * using the Box–Muller transform.
	 *
	 * <p>This method is typically used as:
	 * <pre>
	 * double sample = mean + std * randnorm();
	 * </pre>
	 * to generate samples from a normal distribution with arbitrary mean and standard deviation.</p>
	 *
	 * @return a standard normally distributed random value (mean = 0, std = 1)
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
	 * Generates a Poisson-distributed random integer with the specified mean {@code lambda}.
	 *
	 * <p>This method uses the classic Knuth algorithm to generate values from a Poisson distribution:
	 * a count of events occurring in a fixed interval given an average rate {@code lambda}.</p>
	 *
	 * @param lambda the mean (expected value) of the Poisson distribution
	 * @return a Poisson-distributed random integer
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
	 * Generates a random sample without replacement from a list of {@code max} elements.
	 *
	 * <p>This method returns an array of {@code size} unique integers sampled from the range [0, {@code max} - 1]. 
	 * Sampling is performed without replacement.</p>
	 *
	 * <p><strong>Note:</strong> Although the method signature supports {@code size < max}, the current implementation 
	 * only works correctly when {@code size == max}. As of now, this method is only used in that context 
	 * (e.g., within the {@code Voxel} class, where {@code numSamples != -1} is never triggered).</p>
	 *
	 * @param max  the size of the original list to sample from
	 * @param size the number of elements to sample (must be ≤ {@code max})
	 * @return an array containing {@code size} unique sampled indices
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
	 * Returns a uniformly distributed random number in the range [0.0, 1.0).
	 *
	 * <p>This method delegates to the underlying secure random number generator to produce a 
	 * double-precision floating-point value uniformly sampled from the open interval [0, 1).</p>
	 *
	 * @return a uniformly distributed random {@code double} between 0 (inclusive) and 1 (exclusive)
	 */
	public double randunif() {
		return secRand.nextDouble();
	}
	
	/**
	 * Returns a uniformly distributed random integer between {@code min} (inclusive) and {@code max} (exclusive).
	 *
	 * <p>This method generates an integer in the range [min, max) using the underlying secure random number generator.</p>
	 *
	 * @param min the lower bound (inclusive)
	 * @param max the upper bound (exclusive)
	 * @return a uniformly distributed random integer between {@code min} (inclusive) and {@code max} (exclusive)
	 */
	public int randunif(int min, int max) {
		return secRand.nextInt(max - min) + min;
	}
	
}
