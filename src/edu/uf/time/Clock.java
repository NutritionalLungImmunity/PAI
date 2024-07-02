package edu.uf.time;

import edu.uf.utils.Rand;

public class Clock {
	
	/*private static int iteration = 0;
	private int i;
	private int[] j;
	private boolean[] b;*/
	
	public int iteration  = 0;
	public int size;
	public long count = 0L;
	
	//private static List<Clock> listClock = new ArrayList<>();
	
	public Clock(int size) {
		this.iteration = Rand.getRand().randunif(0, size);
		this.size = size;
	}
	
	public Clock(int size, int iteration) {
		this.iteration = iteration;
		this.size = size;
	}
	/*public static Clock createClock(int size) {
		Clock clock = new Clock(size);
		Clock.listClock.add(clock);
		return clock;
	}*/
	
	public int getSize() {
		return size;
	}
	
	/**
	 * Updates the iteration counter and resets it if "size" iterations have passed. 
	 * This method should only be called once every iteration. It also keeps track of 
	 * the number of times it reset the count. 
	 * <strong> Notice: right now, this method is being called in cell.updateStatus." 
	 * This must be reviewed. </strong>
	 */
	public void tic() {
		this.iteration = (this.iteration + 1) % size;
		if(this.iteration == 0)this.count++;
	}
	
	/**
	 * Returns true if N iterations have passed. N="size" received by the "Clock" constructor. 
	 * This method does not update the iteration count. The number of iterations is updated 
	 * by the "tic" method.
	 * @return
	 */
	public boolean toc() {
		return iteration % size == 0;
	}
	
	/**
	 * Returns the number of times the iteration count was reset.
	 * @return
	 */
	public long getCount() {
		return this.count;
	}
	
}
