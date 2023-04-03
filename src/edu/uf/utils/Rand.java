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
	
	/*CHECK THIS FUNCTION */
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
	
	public double randunif() {
		return secRand.nextDouble();
	}
	
	public int randunif(int min, int max) {
		return secRand.nextInt(max - min) + min;
	}
	
}
