package edu.uf.interactable;

import edu.uf.intracellularState.IntracellularModel;

public abstract class PositionalInfectiousAgent extends InfectiousAgent implements PositionalAgent{
	
	
	private double x;
    private double y;
    private double z;
    
    public PositionalInfectiousAgent(double x, double y, double z, IntracellularModel network) {
    	super(network);
    	this.x = x;
    	this.y = y;
    	this.z = z;
    }

	public double getX() {
		return x;
	}


	public void setX(double x) {
		this.x = x; 
	}


	public double getY() {
		return y;
	}


	public void setY(double y) {
		this.y = y;
	}


	public double getZ() {
		return z;
	}


	public void setZ(double z) {
		this.z = z;
	}
	
}
