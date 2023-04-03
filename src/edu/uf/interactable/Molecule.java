package edu.uf.interactable;

import java.util.ArrayList;
import java.util.List;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;

public abstract class Molecule extends Interactable{

	private int id;
	
	public static final int NUM_COMPARTMENTS = 2;
	
	private double[][][][] values;
	private double[][] compartimentValues;
	private double[] totalMolecules;
	protected double[] totalMoleculesAux;
	private Diffuse diffuse;
	
	private static int countReceptors;
	
	protected List<Integer> secretionPhenotypes = new ArrayList<>();
	
	protected Molecule(double[][][][] qttys, Diffuse diffuse) {
		//countReceptors++;
		this.compartimentValues = new double[qttys.length][NUM_COMPARTMENTS - 1];
        this.id = Id.getId();
        this.values = qttys ;
        this.totalMoleculesAux = new double[qttys.length];
        this.totalMolecules = new double[qttys.length];
        this.diffuse = diffuse;
        for(int i = 0; i < qttys.length; i++) {
        	for(double[][] matrix : qttys[i])
        		for(double[] array : matrix)
        			for(double d : array) 
        				this.incTotalMolecule(i, d);
        }
	}
	
	public static int getNumReceptors() {
		return Molecule.countReceptors;
	}
	
	public static int getReceptors() {
		return ++countReceptors;
	}
	
	public void addPhenotype(int phenotype) {
		this.secretionPhenotypes.add(phenotype);
	}
	
	public List<Integer> getSecretionPhenotype(){
		return secretionPhenotypes;
	}
	
	private void incTotalMolecule(int index, double inc) {
    	this.totalMolecules[index] = this.totalMolecules[index] + inc;
    }
	
	public double inc(double qtty, String index, int x, int y, int z) {
		return inc(qtty, this.getIndex(index), x, y, z);
	}
	
	public double inc(double qtty, int index, int x) {
		this.compartimentValues[index][x] = this.compartimentValues[index][x] + qtty;
        return this.compartimentValues[index][x];
	}
	
    public double inc(double qtty, int index, int x, int y, int z) {
    	if(y < 0 || z < 0) {
    		return inc(qtty, index, x);
    	}
        this.values[index][x][y][z] = this.values[index][x][y][z] + qtty;
        return this.values[index][x][y][z];
    }
    
    public double dec(double qtty, int index, int x) {
    	qtty = this.compartimentValues[index][x] - qtty >= 0 ? qtty : 0;
        this.compartimentValues[index][x] = this.compartimentValues[index][x] - qtty;
        return this.compartimentValues[index][x];
	}
	
	public double dec(double qtty, String index, int x, int y, int z) {
		return dec(qtty, this.getIndex(index), x, y, z);
	}

    public double dec(double qtty, int index, int x, int y, int z) {
    	if(y < 0 || z < 0) {
    		return dec(qtty, index, x);
    	}
    	qtty = this.values[index][x][y][z] - qtty >= 0 ? qtty : 0;
        this.values[index][x][y][z] = this.values[index][x][y][z] - qtty;
        return this.values[index][x][y][z];
    }
	
	public double pdec(double p, String index, int x, int y, int z) {
		return pdec(p, this.getIndex(index), x, y, z);
	}
	
	public double pdec(double p, int index, int x) {
		double dec = this.compartimentValues[index][x] * p;
        this.compartimentValues[index][x] = this.compartimentValues[index][x] - dec;
        return this.compartimentValues[index][x];
	}

    public double pdec(double p, int index, int x, int y, int z) {
    	if(y < 0 || z < 0) {
    		return pdec(p, index, x);
    	}
        double dec = this.values[index][x][y][z] * p;
        this.values[index][x][y][z] = this.values[index][x][y][z] - dec;
        return this.values[index][x][y][z];
    }
	
	public double pinc(double p, String index, int x, int y, int z) {
		return pinc(p, this.getIndex(index), x, y, z);
	}
	
	public double pinc(double p, int index, int x) {
		double dec = this.compartimentValues[index][x] * p;
        this.compartimentValues[index][x] = this.compartimentValues[index][x] + dec;
        return this.compartimentValues[index][x];
	}

    public double pinc(double p, int index, int x, int y, int z) {
    	if(y < 0 || z < 0) {
    		return pinc(p, index, x);
    	}
        double inc = this.values[index][x][y][z] * p;
        this.values[index][x][y][z] = this.values[index][x][y][z] + inc;
        return this.values[index][x][y][z];
    }
	
	public void set(double qtty, String index, int x, int y, int z) {
		set(qtty, this.getIndex(index), x, y, z);
	}
	
	public void set(double qtty, int index, int x) {
		this.compartimentValues[index][x] = qtty;
	}

    public void set(double qtty, int index, int x, int y, int z) {
		if(y < 0 || z < 0) {
    		set(qtty, index, x);
    		return;
    	}
        this.incTotalMolecule(index, - this.values[index][x][y][z]);
        this.incTotalMolecule(index, qtty);
        this.values[index][x][y][z] = qtty;
    }
	
	public double get(String index, int x, int y, int z) {
		return get(this.getIndex(index), x, y, z);
	}
	
	public double get(int index, int x) {
		return this.compartimentValues[index][x];
	}

    public double get(int index, int x, int y, int z) {
    	if(y < 0 || z < 0) {
    		return get(index, x);
    	}
        return this.values[index][x][y][z];
    }
    
    /*public void turnOver(int x, int y, int z) {
    	for(int i = 0; i < this.values.length; i++) { 
    		double voxelConentration = this.get(i, x, y, z) / Constants.VOXEL_VOL;
    		double serumConcentration = this.get(i, 0) / Constants.SERUM_VOL;
    		double dmdt = Constants.TURNOVER_RATE * (voxelConentration - serumConcentration);
    		this.inc(dmdt, i, 0);
    		this.dec(dmdt, i, x, y, z);
    	}
    }*/
    
    public void turnOver(int x, int y, int z) {
    	for(int i = 0; i < this.values.length; i++) { 
    		this.pdec(1-Constants.MCP1_HALF_LIFE, i, x, y, z);
    	}
    }
    
    public abstract void degrade();
    

    protected void degrade(double p, int x) {
        if(p < 0) {
            return;
        }
        for(int i = 0; i < this.values.length; i++) { 
            this.pdec(1-p, i, x);
        }
    }
    
    public int getId() {
    	return id;
    }
    
    public double getTotalMolecule(int index) {
    	return this.totalMolecules[index];
    }
    
    public void diffuse() {
    	if(diffuse == null) return;
    	for(int i = 0; i < values.length; i++) {
    		diffuse.solver(values, i); 
    	}
    }
            
    public abstract void computeTotalMolecule(int x, int y, int z);

    public abstract int getIndex(String str);
    
    public abstract double getThreshold();
    
    public abstract int getNumState();
    
    public void resetCount() {
    	for(int i = 0; i < this.totalMolecules.length; i++) {
    		this.totalMolecules[i] =  this.totalMoleculesAux[i];
    		this.totalMoleculesAux[i] = 0;
    	}
    }
	
}
