package edu.uf.compartments;

public class GridFactory {
	
	public static final String PERIODIC_GRID = "PERIODIC_GRID";
	
	private static Voxel[][][] grid;
	
	private static int xbin;
	private static int ybin;
	private static int zbin;
	private static int numSamples;
	private static String kind;
	
	/**
	 * Sets the parameters of the voxel grid.
	 *
	 * @param xbin the size of the grid along the x-axis
	 * @param ybin the size of the grid along the y-axis
	 * @param zbin the size of the grid along the z-axis
	 * @param numSamples the number of Interactable objects to sample for interactions (usually -1 for all)
	 * @param kind the type of grid; either "PERIODIC_GRID" or another value
	 */
	public static void set(int xbin, int ybin, int zbin, int numSamples, String kind) {
		GridFactory.xbin = xbin;
		GridFactory.ybin = ybin;
		GridFactory.zbin = zbin;
		GridFactory.numSamples = numSamples;
		GridFactory.kind = kind;
	}
	
	public static int getXbin() {
		return GridFactory.xbin;
	}
	
	public static int getYbin() {
		return GridFactory.ybin;
	}
	
	public static int getZbin() {
		return GridFactory.zbin;
	}

	/**
	 * Creates a 3D voxel array if one does not already exist. Returns the 3D voxel array 
	 * if it has been previously created. Before calling this method, ensure that the 
	 * parameters have been set using the {@code set} method.
	 *
	 * @return the 3D voxel array
	 */
	public static Voxel[][][] getGrid(){
		if(grid == null) {
			grid = new Voxel[xbin][ybin][zbin];
			if(kind.contentEquals(PERIODIC_GRID)) {
		        for(int x = 0; x < xbin; x++)
		        	for(int y = 0; y < ybin; y++)
		        		for(int z = 0; z < zbin; z++) {
		        			grid[x][y][z] = new Voxel(x,y,z, numSamples);
		        		}
		        
		        
		        for(int x = 0; x < xbin; x++)
		        	for(int y = 0; y < ybin; y++)
		        		for(int z = 0; z < zbin; z++) {
		                    if (x + 1 < xbin)
		                        grid[x][y][z].getNeighbors().add(grid[x + 1][y][z]);
		                    else
		                        grid[x][y][z].getNeighbors().add(grid[0][y][z]);
		                    if (y + 1 < ybin)
		                        grid[x][y][z].getNeighbors().add(grid[x][y + 1][z]);
		                    else
		                        grid[x][y][z].getNeighbors().add(grid[x][0][z]);
		                    if (x - 1 >= 0)
		                        grid[x][y][z].getNeighbors().add(grid[x - 1][y][z]);
		                    else
		                        grid[x][y][z].getNeighbors().add(grid[xbin-1][y][z]);
		                    if (y - 1 >= 0)
		                        grid[x][y][z].getNeighbors().add(grid[x][y - 1][z]);
		                    else
		                        grid[x][y][z].getNeighbors().add(grid[x][ybin-1][z]);
		                    if (z + 1 < zbin)
		                        grid[x][y][z].getNeighbors().add(grid[x][y][z + 1]);
		                    else
		                        grid[x][y][z].getNeighbors().add(grid[x][y][0]);
		                    if (z - 1 >= 0)
		                        grid[x][y][z].getNeighbors().add(grid[x][y][z - 1]);
		                    else
		                        grid[x][y][z].getNeighbors().add(grid[x][y][zbin-1]);
		        		}
			}
		}
		return grid;
	}
	
}
