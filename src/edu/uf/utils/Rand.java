package edu.uf.utils;

import java.security.SecureRandom;

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
	
	public double randunif() {
		return secRand.nextDouble();
	}
	
	public int randunif(int min, int max) {
		return secRand.nextInt(max - min) + min;
	}
	
}
