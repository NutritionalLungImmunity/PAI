package edu.uf.Diffusion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import edu.uf.utils.LinAlg;

public class ADI implements Diffuse{
	
	protected double lbd;
	protected int iter;
	protected double[][] X;
	protected double[][] Y;
	protected double[][] Z;

    public ADI(double lbd, int iter) {
        this.iter = iter;
        this.lbd = lbd;
        this.X = null;
        this.Y = null;
        this.Z = null;
    }
        
/*
    def create_open_matrixes(this, xbin, ybin, zbin):
        this.X = np.zeros([xbin, xbin])
        this.Y = np.zeros([ybin, ybin])
        this.Z = np.zeros([zbin, zbin])

        for i in range(xbin):
            for j in range(xbin):
                if i == j:
                    this.X[i,j] = - 2
                elif i == j + 1 or i == j - 1:
                    this.X[i, j] = 1
        this.X = np.linalg.inv((-this.lbd*this.X + np.identity(xbin)))

        for i in range(ybin):
            for j in range(ybin):
                if i == j:
                    this.Y[i, j] = - 2
                elif i == j + 1 or i == j - 1:
                    this.Y[i, j] = 1
        this.Y = np.linalg.inv((-this.lbd*this.Y + np.identity(ybin)))

        for i in range(zbin):
            for j in range(zbin):
                if i == j:
                    this.Z[i, j] = - 2
                elif i == j + 1 or i == j - 1:
                    this.Z[i, j] = 1
        this.Z = np.linalg.inv((-this.lbd*this.Z + np.identity(zbin)))

    def create_closed_matrixes(this, xbin, ybin, zbin):
        this.X = np.zeros([xbin, xbin])
        this.Y = np.zeros([ybin, ybin])
        this.Z = np.zeros([zbin, zbin])

        for i in range(xbin):
            for j in range(xbin):
                if i == j:
                    this.X[i,j] = - 2
                elif i == j + 1 or i == j - 1:
                    this.X[i, j] = 1
        this.X[0, 0] = -1
        this.X[0, 1] = 1
        this.X[xbin-1, xbin-1] = -1
        this.X[xbin-1, xbin - 2] = 1
        this.X = np.linalg.inv((-this.lbd*this.X + np.identity(xbin)))

        for i in range(ybin):
            for j in range(ybin):
                if i == j:
                    this.Y[i, j] = - 2
                elif i == j + 1 or i == j - 1:
                    this.Y[i, j] = 1
        this.Y[0, 0] = -1
        this.Y[0, 1] = 1
        this.Y[ybin - 1, ybin - 1] = -1
        this.Y[ybin - 1, ybin - 2] = 1
        this.Y = np.linalg.inv((-this.lbd*this.Y + np.identity(ybin)))

        for i in range(zbin):
            for j in range(zbin):
                if i == j:
                    this.Z[i, j] = - 2
                elif i == j + 1 or i == j - 1:
                    this.Z[i, j] = 1
        this.Z[0, 0] = -1
        this.Z[0, 1] = 1
        this.Z[zbin - 1, zbin - 1] = -1
        this.Z[zbin - 1, zbin - 2] = 1
        this.Z = np.linalg.inv((-this.lbd*this.Z + np.identity(zbin)))
*/
    
    private boolean readMatrix(File file, double[][] matrix) {
    	BufferedReader br = null;
    	try {
			br = new BufferedReader(new FileReader(file));
			
			String line = null;
			String[] sp = null;
			int i = 0;
			int j = 0;
			while((line = br.readLine())!=null) {
				sp = line.split(",");
				for(String str : sp) {
					matrix[i][j++] = Double.parseDouble(str);
				}
				i++;
				j = 0;
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
    	return true;
    }
    
    
    private boolean readMatrixes(String kind, int xbin, int ybin, int zbin) {
    	File fileX = new File(kind + "_X_" + xbin + ".csv");
    	File fileY = new File(kind + "_Y_" + ybin + ".csv");
    	File fileZ = new File(kind + "_Z_" + zbin + ".csv");
    	
    	if(fileX.exists() && fileY.exists() && fileZ.exists()) {
    		
    		this.X = new double[xbin][xbin];
    		this.Y = new double[ybin][ybin];
    		this.Z = new double[zbin][zbin];
    		
    		if(!readMatrix(fileX, this.X)) {
    			return false;
    		}
    		if(!readMatrix(fileY, this.Y)) {
    			return false;
    		}
    		if(!readMatrix(fileZ, this.Z)) {
    			return false;
    		}
    		return true;
    		
    	}
    	
    	return false;
    }
    
    private void writeMatrix(String kind, double[][] matrix, int nbin) {
    	try {
			PrintWriter pw = new PrintWriter(kind + nbin + ".csv");
			String line = null;
			for(double[] array : matrix) {
				line = "";
				for(double d : array) {
					if(line.length() > 0)
						line += ",";
					line += d;
				}
				pw.println(line);
			}
			pw.close(); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }

    public void createPeriodicMatrixes(int xbin, int ybin, int zbin) {
    	
    	if(!readMatrixes("periodic", xbin, ybin, zbin)) {
    		this.X = new double[xbin][xbin];
    		this.Y = new double[ybin][ybin];
    		this.Z = new double[zbin][zbin];
        
    		for(int i = 0; i < xbin; i++)
    			for(int j = 0; j < xbin; j++) {
    				if (i == j)
    					this.X[i][j] = - 2;
    				else if (i == j + 1 || i == j - 1)
    					this.X[i][j] = 1;
    			}
    		this.X[0][xbin-1] = 1;
    		this.X[xbin-1][0] = 1;
    		System.out.println("inverting X");
    		this.X = LinAlg.inverse(LinAlg.add(LinAlg.multiply(X, -this.lbd), LinAlg.identity(xbin)));

    		for(int i = 0; i < ybin; i++)
    			for(int j = 0; j < ybin; j++) {
    				if (i == j)
    					this.Y[i][j] = - 2;
    				else if (i == j + 1 || i == j - 1)
    					this.Y[i][j] = 1;
    			}
    		this.Y[0][ybin - 1] = 1;
    		this.Y[ybin - 1][0] = 1;
    		System.out.println("inverting Y");
    		this.Y = LinAlg.inverse(LinAlg.add(LinAlg.multiply(Y, -this.lbd), LinAlg.identity(ybin)));

    		for(int i = 0; i < zbin; i++)
    			for(int j = 0; j < zbin; j++) {
    				if (i == j)
    					this.Z[i][j] = - 2;
    				else if (i == j + 1 || i == j - 1)
    					this.Z[i][j] = 1;
    			}
    		this.Z[0][zbin - 1] = 1;
    		this.Z[zbin - 1][0] = 1;
    		System.out.println("inverting Z");
    		this.Z = LinAlg.inverse(LinAlg.add(LinAlg.multiply(Z, -this.lbd), LinAlg.identity(zbin)));
    		
    		writeMatrix("periodic_X_", this.X, xbin);
    		writeMatrix("periodic_Y_", this.Y, ybin);
    		writeMatrix("periodic_Z_", this.Z, zbin);
    	}
    	
    }
    
    @Override
    public void solver(double[][][][] grid, int index) {
    	int xbin = grid[0].length;
        int ybin = grid[0][0].length;
        int zbin = grid[0][0][0].length;
        
        double[] vector = null;
        for(int i = 0; i < this.iter; i++) {
        	vector = new double[xbin];
        	for (int y = 0; y < ybin; y++) {  //in range(ybin):
                for (int z = 0; z < zbin; z++) { // in range(zbin):
                	for(int x = 0; x < xbin; x++) {
                		vector[x] = grid[index][x][y][z];
                	}
                	vector = LinAlg.dotProduct(X, vector);
                	for (int x = 0; x < xbin; x++) {
                		grid[index][x][y][z] = vector[x];
                	}
                }
        	}
        	vector = new double[ybin];
        	for (int x = 0; x < xbin; x++) { //in range(ybin):
                for (int z = 0; z < zbin; z++) {// in range(zbin):
                	for(int y = 0; y < ybin; y++) {
                		vector[y] = grid[index][x][y][z];
                	}
                	vector = LinAlg.dotProduct(Y, vector);
                	for (int y = 0; y < ybin; y++) {
                		grid[index][x][y][z] = vector[y];
                	}
                }
        	}
                    //M[x, :, z] = this.Y.dot(M[x, :, z]);
        	vector = new double[zbin];
        	for (int y = 0; y < ybin; y++) { //in range(ybin):
                for (int x = 0; x < xbin; x++) { 
                	for(int z = 0; z < zbin; z++) {
                		vector[z] = grid[index][x][y][z];
                	}
                	vector = LinAlg.dotProduct(Z, vector);
                	for (int z = 0; z < zbin; z++) {
                		grid[index][x][y][z] = vector[z];
                	}
                }
        	}
                    //M[x,y,:] = this.Z.dot(M[x,y,:]);
        }
        

    }


    /*public void solver(double[][][][] grid, int index) {
    	int xbin = grid[0].length;
        int ybin = grid[0][0].length;
        int zbin = grid[0][0][0].length;
        
        for(int k = 0; k < this.iter; k++) {
        	for (int y = 0; y < ybin; y++) //in range(ybin):
                for (int z = 0; z < zbin; z++) // in range(zbin):
                	LinAlg.dotProduct(X, grid[index], -1, y, z);
                    //M[:][y][z] = this.X.dot(M[:,y,z]);
        	for (int x = 0; x < xbin; x++) //in range(ybin):
                for (int z = 0; z < zbin; z++) // in range(zbin):
                	LinAlg.dotProduct(X, grid[index], x, -1, z);
                    //M[x, :, z] = this.Y.dot(M[x, :, z]);
        	for (int y = 0; y < ybin; y++) //in range(ybin):
                for (int x = 0; x < xbin; x++) // in range(zbin):
                	LinAlg.dotProduct(X, grid[index], x, y, -1);
                    //M[x,y,:] = this.Z.dot(M[x,y,:]);
        }
        

    }*/

}
