package edu.uf.time;

import java.util.ArrayList;
import java.util.List;

public class Clock {
	
	private static int iteration = 0;
	private int i;
	private int[] j;
	private boolean[] b;
	
	private static List<Clock> listClock = new ArrayList<>();
	
	private Clock(int size) {
		this.j = new int[size];
		this.b = new boolean[size];
		for(int i = 0; i < size; i++)
			this.b[i] = true;
	}
	
	public static Clock createClock(int size) {
		Clock clock = new Clock(size);
		Clock.listClock.add(clock);
		return clock;
	}
	
	public static void updade() {
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
	}
	
}
