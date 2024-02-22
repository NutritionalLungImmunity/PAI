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
	
	public void tic() {
		this.iteration = (this.iteration + 1) % size;
		if(this.iteration == 0)this.count++;
	}
	
	public boolean toc() {
		return iteration % size == 0;
	}
	
	public long getCount() {
		return this.count;
	}
	
	/*public static void updade() {
		Clock.iteration++;
		for(Clock clock : listClock)
			clock.updateClock();
	}
	
	private void updateClock() {
		this.i++;
	}
	
	public void tic(int idx) {
		tic(idx, false);
	}
	
	public void tic(int idx, boolean b) {
		if(!b) {
			this.j[idx] = i;
			this.b[idx] = true;
		}else if (b && this.b[idx]){
			this.j[idx] = i;
			this.b[idx] = false;
		}
	}
	
	public int toc(int idx) {
		return i - j[idx];
	}
	
	public boolean toc(int idx, int interval) {
		return (i - j[idx]) % interval == 0;
	}
	
	public int get() {
		return i;
	}
	
	public static int getIteration() {
		return Clock.iteration;
	}*/
	
}
