package edu.uf.Diffusion;

public class FADIPeriodic extends FADI{

	public FADIPeriodic(double f, double pdeFactor, double deltaT) {
		super(f, pdeFactor, deltaT);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void solver(double[][][][] space, int index){
    	int xbin = space[index].length;
        int ybin = space[index][0].length;
        int zbin = space[index][0][0].length; 
        int xbin1 = xbin-1;
        int ybin1 = ybin-1;
        int zbin1 = zbin-1;
        
        double[] C_x = new double[xbin];
        double[] C_y = new double[ybin];
        double[] C_z = new double[zbin];

        double[] D_x = new double[xbin];
        double[] D_y = new double[ybin];
        double[] D_z = new double[zbin];
        
        double[] x2 = new double[xbin1];
        double[] y2 = new double[ybin1];
        double[] z2 = new double[zbin1];
        
        double xn = 0;
        double yn = 0;
        double zn = 0;
        
        double[] deltaX = new double[xbin1];
        double[] deltaY = new double[ybin1];
        double[] deltaZ = new double[zbin1];
        
        double[][][] x1 = new double[xbin1][ybin][zbin];
        double[][][] y1 = new double[xbin][ybin1][zbin];
        double[][][] z1 = new double[xbin][ybin][zbin1];
        
        
	
        int x, y, z;
        double next[][][] = new double[xbin][ybin][zbin];
        double D[][][] = new double[xbin][ybin][zbin]; 
        double A, B, C, E;
        
        A=-(3-2*f);
        B=pdeFactor/deltaT + 2*(3-2*f);
        C=-(3-2*f);
        E=pdeFactor/deltaT - 4*f;
        //std::cout <<"\n A:"<<A<<" B:"<<B<<" C:"<<C<<" E:"<<E;

        for(x=0; x<xbin; x++){
        	for(y=0; y<ybin; y++){
        		for(z=0; z<zbin; z++){
        			D[x][y][z]=E*space[index][x][y][z];
                    if (y==0 && z==0)
                    	D[x][y][z]+=f*(space[index][x][y+1][z]+space[index][x][y][z+1] + space[index][x][ybin1][z] + space[index][x][y][zbin1]);
                    else if (y==0 && z==zbin1) 
                    	D[x][y][z]+=f*(space[index][x][y+1][z]+space[index][x][y][z-1] + space[index][x][ybin1][z] + space[index][x][y][0]);
                    else if (y==ybin1 && z==0) 
                    	D[x][y][z]+=f*(space[index][x][y-1][z]+space[index][x][y][z+1] + space[index][x][0][z] + space[index][x][y][zbin1]);
                    else if (y==ybin1 && z==zbin1) 
                    	D[x][y][z]+=f*(space[index][x][y-1][z]+space[index][x][y][z-1] + space[index][x][0][z] + space[index][x][y][0]);
                    else if (y==0) 
                    	D[x][y][z]+=f*(space[index][x][y+1][z]+space[index][x][ybin1][z]+space[index][x][y][z-1]+space[index][x][y][z+1]);
                    else if (y==ybin1) 
                    	D[x][y][z]+=f*(space[index][x][y-1][z]+space[index][x][0][z]+space[index][x][y][z-1]+space[index][x][y][z+1]);
                    else if (z==0) 
                    	D[x][y][z]+=f*(space[index][x][y-1][z]+space[index][x][y+1][z]+space[index][x][y][z+1]+space[index][x][y][zbin1]);
                    else if (z==zbin1) 
                    	D[x][y][z]+=f*(space[index][x][y-1][z]+space[index][x][y+1][z]+space[index][x][y][z-1]+space[index][x][y][0]);
                    else 
                    	D[x][y][z]+=f*(space[index][x][y-1][z]+space[index][x][y+1][z]+space[index][x][y][z-1]+space[index][x][y][z+1]);
                }
        	}
        }
        for(x=0; x<xbin1; x++){
			if (x==0) C_x[x]=C/B;
            else C_x[x]= C/(B-A*C_x[x-1]);
        }
        
        for(y=0; y<ybin; y++){
        	for(z=0; z<zbin; z++){
                for(x=0; x<xbin1; x++){
                	if (x==0) D_x[x] = D[x][y][z]/B;
                    else D_x[x] = (D[x][y][z]-A*D_x[x-1])/(B-A*C_x[x-1]);
                }
                for(x=(xbin1-1); x>-1; x--){ 
                	if (x==(xbin1-1)) x1[x][y][z]=D_x[x];
                    else x1[x][y][z]=D_x[x]-C_x[x]*x1[x+1][y][z];
                }
            }
        }
        deltaX[0] = A;
        deltaX[xbin1-1] = A;
        
        for(x=0; x<xbin1; x++){
        	if (x==0) D_x[x] = deltaX[x]/B;
            else D_x[x] = (deltaX[x]-A*D_x[x-1])/(B-A*C_x[x-1]);
        }
        for(x=(xbin1-1); x>-1; x--){
        	if (x==(xbin1-1)) x2[x]=D_x[x];
            else x2[x]=D_x[x]-C_x[x]*x2[x+1];
        }
        
        for(y=0; y<ybin; y++){
        	for(z=0; z<zbin; z++){
        		xn = (D[xbin1][y][z] - A*x1[0][y][z] - A*x1[xbin1-1][y][z])/(B - A*x2[0] - A*x2[xbin1-1]);
                for(x=0; x<xbin; x++){
                	if (x==xbin1) next[x][y][z] = xn;
                    else next[x][y][z] = x1[x][y][z] - x2[x]*xn;
                }
            }
        }
        
        
        
        for(x=0; x<xbin; x++){
        	for(y=0; y<ybin; y++){
        		for(z=0; z<zbin; z++){
        			D[x][y][z]=E*next[x][y][z];
                    if (x==0 && z==0) D[x][y][z]+=f*(next[x+1][y][z]+next[x][y][z+1] + next[xbin1][y][z] + next[x][y][zbin1]);
                    else if (x==0 && z==zbin1) D[x][y][z]+=f*(next[x+1][y][z]+next[x][y][z-1] + next[xbin1][y][z] + next[x][y][0]);
                    else if (x==xbin1 && z==0) D[x][y][z]+=f*(next[x-1][y][z]+next[x][y][z+1] + next[0][y][z] + next[x][y][zbin1]);
                    else if (x==xbin1 && z==zbin1) D[x][y][z]+=f*(next[x-1][y][z]+next[x][y][z-1]  + next[0][y][z] + next[x][y][0]);
                    else if (x==0) D[x][y][z]+=f*(next[x+1][y][z]+ next[xbin1][y][z] +next[x][y][z-1]+next[x][y][z+1]);
                    else if (x==xbin1) D[x][y][z]+=f*(next[x-1][y][z]+ next[0][y][z] +next[x][y][z-1]+next[x][y][z+1]);
                    else if (z==0) D[x][y][z]+=f*(next[x-1][y][z]+next[x+1][y][z]+next[x][y][z+1] + next[x][y][zbin1]);
                    else if (z==zbin1) D[x][y][z]+=f*(next[x-1][y][z]+next[x+1][y][z]+next[x][y][z-1] + next[x][y][0]);
                    else D[x][y][z]+=f*(next[x-1][y][z]+next[x+1][y][z]+next[x][y][z-1]+next[x][y][z+1]);
                }
            }
        }
        
        for(y=0; y<ybin1; y++){
			if (y==0) C_y[y]=C/B;
            else C_y[y]= C/(B-A*C_y[y-1]);
		}
        
        for(x=0; x<xbin; x++){
        	for(z=0; z<zbin; z++){
        		for(y=0; y<ybin1; y++){
        			if (y==0) D_y[y]=D[x][y][z]/B;
        			else D_y[y]= (D[x][y][z]-A*D_y[y-1])/(B-A*C_y[y-1]);
                }
        		for(y=(ybin1-1); y>-1; y--){
        			if (y==(ybin1-1)) y1[x][y][z]=D_y[y];
                    else y1[x][y][z]=D_y[y]-C_y[y]*y1[x][y+1][z];
                }
            }
        }
        
        deltaY[0] = A;
        deltaY[ybin1-1] = A;
        
        for(y=0; y<ybin1; y++){
			if (y==0) D_y[y]=deltaY[y]/B;
			else D_y[y]= (deltaY[y]-A*D_y[y-1])/(B-A*C_y[y-1]);
        }
		for(y=(ybin1-1); y>-1; y--){
			if (y==(ybin1-1)) y2[y]=D_y[y];
            else y2[y]=D_y[y]-C_y[y]*y2[y+1];
        }
		
		for(x=0; x<xbin; x++){
        	for(z=0; z<zbin; z++){
        		yn = (D[x][ybin1][z] - A*y1[x][0][z] - A*y1[x][ybin1-1][z])/(B - A*y2[0] -A*y2[ybin1-1]); //IS IT D[xbin][... !?
        		for(y=0; y<ybin; y++){
        			if (y==ybin1) next[x][y][z]=yn;
        			else next[x][y][z] = y1[x][y][z] - y2[y]*yn;
                }
            }
        }
        
        for(x=0; x<xbin; x++){
        	for(y=0; y<ybin; y++){
        		for(z=0; z<zbin; z++){
        			D[x][y][z]=E*next[x][y][z];
                    if (y==0 && x==0) D[x][y][z]+=f*(next[x][y+1][z]+next[x+1][y][z] + next[xbin1][y][z] + next[x][ybin1][z]);
                    else if (y==0 && x==xbin1) D[x][y][z]+=f*(next[x][y+1][z]+next[x-1][y][z] + next[0][y][z] + next[x][ybin1][z]);
                    else if (y==ybin1 && x==0) D[x][y][z]+=f*(next[x][y-1][z]+next[x+1][y][z] + next[xbin1][y][z] + next[x][0][z]);
                    else if (y==ybin1 && x==xbin1) D[x][y][z]+=f*(next[x][y-1][z]+next[x-1][y][z] + next[0][y][z] + next[x][0][z]);
                    else if (y==0) D[x][y][z]+=f*(next[x][y+1][z]+ next[x][ybin1][z] +next[x-1][y][z]+next[x+1][y][z]);
                    else if (y==ybin1) D[x][y][z]+=f*(next[x][y-1][z]+ next[x][0][z] +next[x-1][y][z]+next[x+1][y][z]);
                    else if (x==0) D[x][y][z]+=f*(next[x][y-1][z]+next[x][y+1][z]+next[x+1][y][z] + next[xbin1][y][z]);
                    else if (x==xbin1) D[x][y][z]+=f*(next[x][y-1][z]+next[x][y+1][z]+next[x-1][y][z] + next[0][y][z]);
                    else D[x][y][z]+=f*(next[x][y-1][z]+next[x][y+1][z]+next[x-1][y][z]+next[x+1][y][z]);
                }
            }
        }
        
        for(z=0; z<zbin1; z++){
			if (z==0) C_z[z]=C/B;
            else C_z[z]= C/(B-A*C_z[z-1]);
		}
        for(y=0; y<ybin; y++){
        	for(x=0; x<xbin; x++){
        		for(z=0; z<zbin1; z++){
        			if (z==0) D_z[z]=D[x][y][z]/B;
        			else D_z[z]= (D[x][y][z]-A*D_z[z-1])/(B-A*C_z[z-1]);
        		}
        		for(z=(zbin1-1); z>-1; z--){
        			if (z==(zbin1-1)) z1[x][y][z]=D_z[z];
        			else z1[x][y][z]=D_z[z]-C_z[z]*z1[x][y][z+1];
        		}
        	}
        }
        
        deltaZ[0] = A;
        deltaZ[zbin1-1] = A;
        
        for(z=0; z<zbin1; z++){
			if (z==0) D_z[z]=deltaZ[z]/B;
			else D_z[z]= (deltaZ[z]-A*D_z[z-1])/(B-A*C_z[z-1]);
		}
		for(z=(zbin1-1); z>-1; z--){
			if (z==(zbin1-1)) z2[z]=D_z[z];
			else z2[z]=D_z[z]-C_z[z]*z2[z+1];
		}
		
		for(y=0; y<ybin; y++){
        	for(x=0; x<xbin; x++){
        		zn = (D[x][y][zbin1] - A*z1[x][y][0] - A*z1[x][y][zbin1-1])/(B - A*z2[0] - A*z2[zbin1-1]);
        		for(z=0; z<zbin; z++){
        			if (z==zbin1) space[index][x][y][z] = zn;
        			else space[index][x][y][z] = z1[x][y][z]-z2[z]*zn;
        		}
        	}
        }
        
        
        /*for (x = 0; x < xbin; x++) //in range(xbin):
            for (y = 0; y < ybin; y++) //in range(ybin):
                for (z = 0;z < zbin; z++) {
                    //D[x][y][z] = E * next[x][y][z];
                	space[x][y][z] = next[x][y][z];
                }
       */
    }

}
