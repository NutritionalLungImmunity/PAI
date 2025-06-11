package edu.uf.Diffusion;

public abstract class FADI implements Diffuse{

	protected double f;
	protected double pdeFactor;
	protected double deltaT;
	
	public FADI(double f, double pdeFactor, double deltaT) {
		this.f = f;
		this.pdeFactor = pdeFactor;
		this.deltaT = deltaT; 
	}	
	
}
