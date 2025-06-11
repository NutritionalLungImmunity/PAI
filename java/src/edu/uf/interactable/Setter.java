package edu.uf.interactable;


public abstract class Setter extends Molecule{
	 
	private int iteration;
    
    protected Setter(String name) {
		super(new double[][][][] {}, null, name);
	}
	
    /**
     * Disabled.
     */
	public void incTotalMolecule(int index, double inc) {}
	
	/**
     * Disabled. Return 0
     */
	public double inc(double qtty, String index, int x, int y, int z) {
		return 0;
	}
	
	/**
     * Disabled. Return 0
     */
	public double inc(double qtty, int index, int x) {
        return 0;
	}
	
	/**
     * Disabled. Return 0
     */
    public double inc(double qtty, int index, int x, int y, int z) {
        return 0;
    }
    
    /**
     * Disabled. Return 0
     */
    public double dec(double qtty, int index, int x) {
        return 0;
	}
	
    /**
     * Disabled. Return 0
     */
	public double dec(double qtty, String index, int x, int y, int z) {
		return -1;
	}

	/**
     * Disabled. Return 0
     */
    public double dec(double qtty, int index, int x, int y, int z) {
        return 0;
    }
	
    /**
     * Disabled. Return 0
     */
	public double pdec(double p, String index, int x, int y, int z) {
		return 0;
	}
	
	/**
     * Disabled. Return 0
     */
	public double pdec(double p, int index, int x) {
        return 0;
	}

	/**
     * Disabled. Return 0
     */
    public double pdec(double p, int index, int x, int y, int z) {
        return 0;
    }
	
    /**
     * Disabled. Return 0
     */
	public double pinc(double p, String index, int x, int y, int z) {
		return 0;
	}
	
	/**
     * Disabled. Return 0
     */
	public double pinc(double p, int index, int x) {
        return 0;
	}

	/**
     * Disabled. Return 0
     */
    public double pinc(double p, int index, int x, int y, int z) {
        return 0;
    }
    
    /**
     * Disabled.
     */
	public void set(double qtty, String index, int x, int y, int z) {}
	
	/**
     * Disabled.
     */
	public void set(double qtty, int index, int x) {}

	/**
     * Disabled.
     */
    public void set(double qtty, int index, int x, int y, int z) {}
	
    /**
     * Disabled. Return 0
     */
	public double get(String index, int x, int y, int z) {
		return 0;
	}
	
	/**
     * Disabled. Return 0
     */
	public double get(int index, int x) {
		return 0;
	}

	/**
     * Disabled. Return 0
     */
    public double get(int index, int x, int y, int z) {
        return 0;
    }
    
    
    /**
     * Increments the internal counter used to track the number of simulation iterations.
     *
     * <p>This counter is typically used to trigger state changes after a defined 
     * number of iterations. This method is called by {@code Exec.resetCount()}.</p>
     */
    public void update() {
    	this.iteration++;
    }
    
    /**
     * Returns the number of iterations the simulation has executed so far.
     *
     * <p>This value is typically used to determine when a state change 
     * should occur after a specified number of iterations.</p>
     *
     * @return the number of completed simulation iterations
     */
    public int getIteration() {
    	return iteration;
    }
    
    /**
     * Disabled.
     */
    public void degrade() {}

    /**
     * Disabled. Return 0
     */
    public int getIndex(String str) {
        return 0;
    }

    /**
     * Disabled.
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
