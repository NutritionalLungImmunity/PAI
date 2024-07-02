package edu.uf.control;

import edu.uf.compartments.Voxel;

public class MultiThreadExec extends Exec implements Runnable{
	
	private String threadName;
	public static int batchSize = 1; //NOT USED!!!

	//private static Voxel[][][] grid;
	private static Voxel[][][] grid3d;
	private static Voxel[] grid;
	
	private static int bin;
	
	private static int x = 0;
	private static int y = 0;
	private static int z = 0;
	private static int k = 0;
	
	
	private static int xbin;
	private static int ybin;
	private static int zbin;
	
	
	
	public MultiThreadExec(String threadName) {
		this.threadName = threadName;
	}
	
	/*public static void setGrid(Voxel[][][] grid) {
		MultiThreadExec.grid = grid;
		MultiThreadExec.xbin = grid.length;
		MultiThreadExec.ybin = grid[0].length;
		MultiThreadExec.zbin = grid[0][0].length;
	}*/
	
	public static void setGrid(Voxel[][][] grid) {
		
		grid3d = grid;
		xbin = grid.length;
		ybin = grid[0].length;
		zbin = grid[0][0].length;
		MultiThreadExec.bin = xbin*ybin*zbin;
		
		MultiThreadExec.grid = new Voxel[bin];
		
		int w = 0;
		for(int i = 0; i < xbin; i++)
			for(int j = 0; j < ybin; j++)
				for(int k = 0; k < zbin; k++)
					MultiThreadExec.grid[w++] = grid[i][j][k];
				
	}
	
	private static synchronized Voxel getVoxel() {
		if (k >= bin) {
			return null;
		}
		//if(k%10000==0)System.out.println(k);
		return grid[k++];
	}
	
	/*private static synchronized Voxel getVoxel() {
		if (z >= zbin) {
			return null;
		}
		Voxel voxel = grid[x][y][z];
		x = (x+1)%xbin;
		y = x == 0 ? (y+1)%ybin : y;
		z = x == 0 && y == 0 ? (z+1) : z;
		return voxel;
	}*/
	
	public static synchronized void reset() {
		x = 0;
		y = 0;
		z = 0;
		k = 0;
	}
	
	public static void singleThreadNext() {
		for(int x = 0; x < xbin; x++)
    		for(int y = 0; y < ybin; y++)
    			for(int z = 0; z < zbin; z++) {
                    grid3d[x][y][z].next(x, y, z);
    			}
	}
	
	public void run() {
		while(true) {
			
			Voxel voxel = getVoxel();
			//Voxel[] array = getVoxelArray();
			
			if (voxel == null) {
				return;
			}else {
				//for(Voxel voxel : array) {
				//voxel.threadNames.add(Thread.currentThread().getName() + "__" + threadName);
				voxel.interact();
				Exec.gc(voxel);
				voxel.degrade();
				//}
			}
		}
	}
	
}
