package edu.uf.interactable;


public abstract class Setter extends Molecule{
	 
	private int iteration;
    
    protected Setter() {
		super(new double[][][][] {}, null, new int[] {});
	}
	
	private void incTotalMolecule(int index, double inc) {}
	
	public double inc(double qtty, String index, int x, int y, int z) {
		return 0;
	}
	
	public double inc(double qtty, int index, int x) {
        return 0;
	}
	
    public double inc(double qtty, int index, int x, int y, int z) {
        return 0;
    }
    
    public double dec(double qtty, int index, int x) {
        return 0;
	}
	
	public double dec(double qtty, String index, int x, int y, int z) {
		return -1;
	}

    public double dec(double qtty, int index, int x, int y, int z) {
        return 0;
    }
	
	public double pdec(double p, String index, int x, int y, int z) {
		return 0;
	}
	
	public double pdec(double p, int index, int x) {
        return 0;
	}

    public double pdec(double p, int index, int x, int y, int z) {
        return 0;
    }
	
	public double pinc(double p, String index, int x, int y, int z) {
		return 0;
	}
	
	public double pinc(double p, int index, int x) {
        return 0;
	}

    public double pinc(double p, int index, int x, int y, int z) {
        return 0;
    }
	
	public void set(double qtty, String index, int x, int y, int z) {}
	
	public void set(double qtty, int index, int x) {}

    public void set(double qtty, int index, int x, int y, int z) {}
	
	public double get(String index, int x, int y, int z) {
		return 0;
	}
	
	public double get(int index, int x) {
		return 0;
	}

    public double get(int index, int x, int y, int z) {
        return 0;
    }
    
    
    
    public void update() {
    	this.iteration++;
    }
    
    public int getIteration() {
    	return iteration;
    }
    
    public void degrade() {}

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {}


	@Override
	public double getThreshold() {
		return -1;
	}

	@Override
	public int getNumState() {
		return 1;
	}
	
	@Override
	public boolean isTime() {
		return true;
	}
	
	@Override
	public void diffuse() {}
	
	@Override
	public void turnOver(int x, int y, int z) {}


}
