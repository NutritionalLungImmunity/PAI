package edu.uf.interactable;


public abstract class Setter extends Molecule{
	 
	private int iteration;
    
    protected Setter() {
		super(new double[][][][] {}, null);
	}
	
    /**
     * Disabled.
     * @param index
     * @param inc
     */
	public void incTotalMolecule(int index, double inc) {}
	
	/**
     * Disabled. Return 0
     * @param index
     * @param inc
     */
	public double inc(double qtty, String index, int x, int y, int z) {
		return 0;
	}
	
	/**
     * Disabled. Return 0
     * @param index
     * @param inc
     */
	public double inc(double qtty, int index, int x) {
        return 0;
	}
	
	/**
     * Disabled. Return 0
     * @param index
     * @param inc
     */
    public double inc(double qtty, int index, int x, int y, int z) {
        return 0;
    }
    
    /**
     * Disabled. Return 0
     * @param index
     * @param inc
     */
    public double dec(double qtty, int index, int x) {
        return 0;
	}
	
    /**
     * Disabled. Return 0
     * @param index
     * @param inc
     */
	public double dec(double qtty, String index, int x, int y, int z) {
		return -1;
	}

	/**
     * Disabled. Return 0
     * @param index
     * @param inc
     */
    public double dec(double qtty, int index, int x, int y, int z) {
        return 0;
    }
	
    /**
     * Disabled. Return 0
     * @param index
     * @param inc
     */
	public double pdec(double p, String index, int x, int y, int z) {
		return 0;
	}
	
	/**
     * Disabled. Return 0
     * @param index
     * @param inc
     */
	public double pdec(double p, int index, int x) {
        return 0;
	}

	/**
     * Disabled. Return 0
     * @param index
     * @param inc
     */
    public double pdec(double p, int index, int x, int y, int z) {
        return 0;
    }
	
    /**
     * Disabled. Return 0
     * @param index
     * @param inc
     */
	public double pinc(double p, String index, int x, int y, int z) {
		return 0;
	}
	
	/**
     * Disabled. Return 0
     * @param index
     * @param inc
     */
	public double pinc(double p, int index, int x) {
        return 0;
	}

	/**
     * Disabled. Return 0
     * @param index
     * @param inc
     */
    public double pinc(double p, int index, int x, int y, int z) {
        return 0;
    }
    
    /**
     * Disabled.
     * @param index
     * @param inc
     */
	public void set(double qtty, String index, int x, int y, int z) {}
	
	/**
     * Disabled.
     * @param index
     * @param inc
     */
	public void set(double qtty, int index, int x) {}

	/**
     * Disabled.
     * @param index
     * @param inc
     */
    public void set(double qtty, int index, int x, int y, int z) {}
	
    /**
     * Disabled. Return 0
     * @param index
     * @param inc
     */
	public double get(String index, int x, int y, int z) {
		return 0;
	}
	
	/**
     * Disabled. Return 0
     * @param index
     * @param inc
     */
	public double get(int index, int x) {
		return 0;
	}

	/**
     * Disabled. Return 0
     * @param index
     * @param inc
     */
    public double get(int index, int x, int y, int z) {
        return 0;
    }
    
    
    /**
     * This method counts the number of simulation-iterations. The purpose of a Setter 
     * is to change state after N-iterations. This method is called by "Exec.resetCount."
     */
    public void update() {
    	this.iteration++;
    }
    
    /**
     * This method returns how many iterations the simulation has run for. The purpose 
     * of a Setter is to change state after N-iterations.
     * @return
     */
    public int getIteration() {
    	return iteration;
    }
    
    /**
     * Disabled.
     * @param index
     * @param inc
     */
    public void degrade() {}

    /**
     * Disabled. Return 0
     * @param index
     * @param inc
     */
    public int getIndex(String str) {
        return 0;
    }

    /**
     * Disabled.
     * @param index
     * @param inc
     */
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
	/**
     * Disabled.
     * @param index
     * @param inc
     */
	public void diffuse() {}
	
	@Override
	/**
     * Disabled.
     * @param index
     * @param inc
     */
	public void turnOver(int x, int y, int z) {}


}
