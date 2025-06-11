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
	 * Advances the internal iteration counter and resets it after {@code size} iterations.
	 *
	 * <p>This method should be called once per simulation iteration. When the counter reaches {@code size}, 
	 * it wraps back to zero and increments an internal {@code count} representing the number of full cycles completed.</p>
	 *
	 * <p><strong>Note:</strong> This method is currently called inside {@code Cell.updateStatus()}. 
	 * This behavior should be reviewed to ensure correct timing and separation of concerns.</p>
	 */
	public void tic() {
		this.iteration = (this.iteration + 1) % size;
		if(this.iteration == 0)this.count++;
	}
	
	/**
	 * Returns {@code true} if a full cycle of {@code size} iterations has completed.
	 *
	 * <p>The value of {@code size} is set during construction of the {@code Clock} instance. 
	 * This method does not advance the iteration count; it simply checks whether the current 
	 * iteration is at the start of a new cycle (i.e., divisible by {@code size}). 
	 * Use the {@code tic()} method to update the iteration counter.</p>
	 *
	 * @return {@code true} if {@code iteration % size == 0}; {@code false} otherwise
	 */
	public boolean toc() {
		return iteration % size == 0;
	}
	
	/**
	 * Returns the number of times the iteration counter has completed a full cycle and been reset.
	 *
	 * <p>This value is incremented each time the {@code tic()} method wraps the internal iteration 
	 * counter back to zero.</p>
	 *
	 * @return the number of completed iteration cycles
	 */
	public long getCount() {
		return this.count;
	}
	
}
