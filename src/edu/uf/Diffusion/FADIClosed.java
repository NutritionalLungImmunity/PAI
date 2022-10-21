package edu.uf.Diffusion;

public class FADIClosed extends FADI{

	public FADIClosed(double f, double pdeFactor, double deltaT) {
		super(f, pdeFactor, deltaT);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void solver(double[][][][] space, int index) {
		int xbin = space[index].length;
        int ybin = space[index][0].length;
        int zbin = space[index][0][0].length; 

        //import timeit
        
        double[][][] D = new double[xbin][ybin][zbin];
        double[][][] next = new double[xbin][ybin][zbin];

        //D = [[[0 for _ in range(zbin)] for _ in range(ybin)] for _ in range(xbin)]
        //next = [[[0 for _ in range(zbin)] for _ in range(ybin)] for _ in range(xbin)]
        
        double[] C_x = new double[xbin];
        double[] C_y = new double[ybin];
        double[] C_z = new double[zbin];

        double[] D_x = new double[xbin];
        double[] D_y = new double[ybin];
        double[] D_z = new double[zbin];

        double A  = -(3 - 2 * this.f);
        double B  = this.pdeFactor/this.deltaT + 2 * (3 - 2 * this.f);
        double Bc = this.pdeFactor/this.deltaT + (3 - 2 * this.f);
        double C  = -(3 - 2 * this.f);
        double E  = this.pdeFactor/this.deltaT - 4 * this.f;

        for (int x = 0; x < xbin; x++) {//in range(xbin):
        	for (int y = 0; y < ybin; y++) {//in range(ybin):
                for (int z = 0;z < zbin; z++) {// in range(zbin):
                	D[x][y][z] = E * space[index][x][y][z];//.molecules.get(molecule).values[0];
                    if (y == 0 && z == 0) {
                        D[x][y][z] += this.f * (space[index][x][y + 1][z] + space[index][x][y][z + 1] + 2 * space[index][x][y][z]);
                    }else if (y == 0 && z == zbin - 1) {
                        D[x][y][z] += this.f * (space[index][x][y + 1][z] + space[index][x][y][z - 1] + 2 * space[index][x][y][z]);
                    }else if (y == ybin - 1 && z == 0){
                        D[x][y][z] += this.f * (space[index][x][y - 1][z] + space[index][x][y][z + 1] + 2 * space[index][x][y][z]);
                    }else if (y == ybin - 1 && z == zbin - 1) {
                        D[x][y][z] += this.f * (space[index][x][y - 1][z] + space[index][x][y][z - 1] + 2 * space[index][x][y][z]);
                    }else if (y == 0) {
                        D[x][y][z] += this.f * (space[index][x][y + 1][z]  + space[index][x][y][z - 1] + space[index][x][y][z + 1] + space[index][x][y][z]);
                    }else if (y == ybin - 1) {
                        D[x][y][z] += this.f * (space[index][x][y - 1][z] + space[index][x][y][z - 1] + space[index][x][y][z + 1] + space[index][x][y][z]);
                    }else if (z == 0) {
                        D[x][y][z] += this.f * (space[index][x][y - 1][z] + space[index][x][y + 1][z] + space[index][x][y][z + 1] + space[index][x][y][z]);
                    }else if (z == zbin - 1) {
                        D[x][y][z] += this.f * (space[index][x][y + 1][z] + space[index][x][y - 1][z] + space[index][x][y][z - 1] + space[index][x][y][z]);
                    }else {
                        D[x][y][z] += this.f * (space[index][x][y - 1][z] + space[index][x][y + 1][z] + space[index][x][y][z - 1] + space[index][x][y][z + 1]);
                    }
                }
            }
        }

        for (int x = 0; x < xbin; x++) {
            if (x == 0)
                C_x[x] = C / Bc;
            else if (x == (xbin - 1))
            	C_x[x] = C / (Bc - A * C_x[x - 1]);
            else
                C_x[x] = C / (B - A * C_x[x - 1]);
		}
        for (int y = 0; y < ybin; y++) {
        	for (int z = 0;z < zbin; z++) {
        		
        		for (int x = 0; x < xbin; x++) {
                    if (x == 0)
                        D_x[x] = D[x][y][z] / Bc;
                    else if (x == xbin - 1)
                        D_x[x] = (D[x][y][z] - A * D_x[x - 1]) / (Bc - A * C_x[x - 1]);
                    else
                        D_x[x] = (D[x][y][z] - A * D_x[x - 1]) / (B - A * C_x[x - 1]);
        		}
        		for (int x = xbin-1; x >= 0; x--) {
                    if (x == xbin - 1)
                        next[x][y][z] = D_x[x];
                    else
                        next[x][y][z] = D_x[x] - C_x[x] * next[x + 1][y][z];
        		}
        	}
        }

        for (int x = 0; x < xbin; x++) {//in range(xbin):
            for (int y = 0; y < ybin; y++) {//in range(ybin):
                for (int z = 0;z < zbin; z++) {// in range(zbin):
                    D[x][y][z] = E * next[x][y][z];
                    if (x == 0 && z == 0)
                        D[x][y][z] = D[x][y][z] + this.f * (next[x+1][y][z] + next[x][y][z+1] + 2 * next[x][y][z]);
                    else if (x == 0 && z == zbin - 1)
                        D[x][y][z] = D[x][y][z] + this.f * (next[x+1][y][z] + next[x][y][z-1] + 2 * next[x][y][z]);
                    else if (x == xbin - 1 && z == 0)
                        D[x][y][z] = D[x][y][z] + this.f * (next[x-1][y][z] + next[x][y][z+1] + 2 * next[x][y][z]);
                    else if (x == xbin - 1 && z == zbin - 1)
                        D[x][y][z] = D[x][y][z] + this.f * (next[x-1][y][z] + next[x][y][z-1] + 2 * next[x][y][z]);
                    else if (x == 0)
                        D[x][y][z] = D[x][y][z] + this.f * (next[x+1][y][z] + next[x][y][z-1] + next[x][y][z+1] + next[x][y][z]);
                    else if (x == xbin - 1)
                        D[x][y][z] = D[x][y][z] + this.f * (next[x-1][y][z] + next[x][y][z-1] + next[x][y][z+1] + next[x][y][z]);
                    else if (z == 0)
                        D[x][y][z] = D[x][y][z] + this.f * (next[x-1][y][z] + next[x+1][y][z] + next[x][y][z+1] + next[x][y][z]);
                    else if (z == zbin - 1)
                        D[x][y][z] = D[x][y][z] + this.f * (next[x-1][y][z] + next[x+1][y][z] + next[x][y][z-1] + next[x][y][z]);
                    else
                        D[x][y][z] = D[x][y][z] + this.f * (next[x-1][y][z] + next[x+1][y][z] + next[x][y][z-1] + next[x][y][z+1]);
                }
            }
        }
        
        for (int y = 0; y < ybin; y++) {
            if (y == 0)
                C_y[y] = C / Bc;
            else if (y == (ybin - 1))
            	C_y[y] = C / (Bc - A * C_y[y - 1]);
            else
                C_y[y] = C / (B - A * C_y[y - 1]);
		}
        for (int x = 0; x < xbin; x++) {
        	for (int z = 0;z < zbin; z++) {
        		
        		for (int y = 0; y < ybin; y++) {
                    if (y == 0)
                        D_y[y] = D[x][y][z] / Bc;
                    else if (y == ybin - 1)
                        D_y[y] = (D[x][y][z] - A * D_y[y - 1]) / (Bc - A * C_y[y - 1]);
                    else
                        D_y[y] = (D[x][y][z] - A * D_y[y - 1]) / (B - A * C_y[y - 1]);
        		}

        		for (int y = ybin-1; y >= 0; y--) {
                    if (y == ybin - 1)
                        next[x][y][z] = D_y[y];
                    else
                        next[x][y][z] = D_y[y] - C_y[y] * next[x][y + 1][z];
        		}
        	}
        }

        for (int x = 0; x < xbin; x++) {//in range(xbin):
            for (int y = 0; y < ybin; y++) {//in range(ybin):
                for (int z = 0;z < zbin; z++) {
                    D[x][y][z] = E * next[x][y][z];
                    if (y == 0 && x == 0)
                        D[x][y][z] = D[x][y][z] + this.f * (next[x][y+1][z] + next[x+1][y][z] + 2 * next[x][y][z]);
                    else if (y == 0 && x == xbin - 1)
                        D[x][y][z] = D[x][y][z] + this.f * (next[x][y+1][z] + next[x-1][y][z] + 2 * next[x][y][z]);
                    else if (y == ybin - 1 && x == 0)
                        D[x][y][z] = D[x][y][z] + this.f * (next[x][y-1][z] + next[x+1][y][z] + 2 * next[x][y][z]);
                    else if (y == ybin - 1 && x == xbin - 1)
                        D[x][y][z] = D[x][y][z] + this.f * (next[x][y-1][z] + next[x-1][y][z] + 2 * next[x][y][z]);
                    else if (y == 0)
                        D[x][y][z] = D[x][y][z] + this.f * (next[x][y+1][z] + next[x-1][y][z] + next[x+1][y][z] + next[x][y][z]);
                    else if (y == ybin - 1)
                        D[x][y][z] = D[x][y][z] + this.f * (next[x][y-1][z] + next[x-1][y][z] + next[x+1][y][z] + next[x][y][z]);
                    else if (x == 0)
                        D[x][y][z] = D[x][y][z] + this.f * (next[x][y-1][z] + next[x][y+1][z] + next[x+1][y][z] + next[x][y][z]);
                    else if (x == xbin - 1)
                        D[x][y][z] = D[x][y][z] + this.f * (next[x][y-1][z] + next[x][y+1][z] + next[x-1][y][z] + next[x][y][z]);
                    else
                        D[x][y][z] = D[x][y][z] + this.f * (next[x][y-1][z] + next[x][y+1][z] + next[x-1][y][z] + next[x+1][y][z]);
                }
            }
        } 
        
        for (int z = 0;z < zbin; z++) {
            if (z == 0)
                C_z[z] = C / Bc;
            else if (z == (zbin - 1))
            	C_z[z] = C / (Bc - A * C_z[z - 1]);
            else
                C_z[z] = C / (B - A * C_z[z - 1]);
		}
        for (int y = 0; y < ybin; y++) {
        	for (int x = 0; x < xbin; x++) { 
        		
        		for (int z = 0;z < zbin; z++) {
                    if (z == 0)
                        D_z[z] = D[x][y][z] / Bc;
                    else if (z == zbin - 1)
                        D_z[z] = (D[x][y][z] - A * D_z[z - 1]) / (Bc - A * C_z[z - 1]);
                    else
                        D_z[z] = (D[x][y][z] - A * D_z[z - 1]) / (B - A * C_z[z - 1]);
        		}
        		for (int z = zbin-1;z >= 0; z--) {
                    if (z == zbin - 1)
                    	space[index][x][y][z] = D_z[z];
                    else
                    	space[index][x][y][z] = D_z[z] - C_z[z] * space[index][x][y][z + 1];
        		}
        	}
        }

        /*for (int x = 0; x < xbin; x++) //in range(xbin):
            for (int y = 0; y < ybin; y++) //in range(ybin):
                for (int z = 0;z < zbin; z++) {
                    //D[x][y][z] = E * next[x][y][z];
                    space[x][y][z] = next[x][y][z];
                }*/
    }
	
}
