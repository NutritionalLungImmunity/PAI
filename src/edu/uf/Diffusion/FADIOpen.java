package edu.uf.Diffusion;

public class FADIOpen extends FADI{

	public FADIOpen(double f, double pdeFactor, double deltaT) {
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
                    if (y==0 && z==0) D[x][y][z]+=f*(space[index][x][y+1][z]+space[index][x][y][z+1]);
                    else if (y==0 && z==zbin1) D[x][y][z]+=f*(space[index][x][y+1][z]+space[index][x][y][z-1]);
                    else if (y==ybin1 && z==0) D[x][y][z]+=f*(space[index][x][y-1][z]+space[index][x][y][z+1]);
                    else if (y==ybin1 && z==zbin1) D[x][y][z]+=f*(space[index][x][y-1][z]+space[index][x][y][z-1]);
                    else if (y==0) D[x][y][z]+=f*(space[index][x][y+1][z]+space[index][x][y][z-1]+space[index][x][y][z+1]);
                    else if (y==ybin1) D[x][y][z]+=f*(space[index][x][y-1][z]+space[index][x][y][z-1]+space[index][x][y][z+1]);
                    else if (z==0) D[x][y][z]+=f*(space[index][x][y-1][z]+space[index][x][y+1][z]+space[index][x][y][z+1]);
                    else if (z==zbin1) D[x][y][z]+=f*(space[index][x][y-1][z]+space[index][x][y+1][z]+space[index][x][y][z-1]);
                    else D[x][y][z]+=f*(space[index][x][y-1][z]+space[index][x][y+1][z]+space[index][x][y][z-1]+space[index][x][y][z+1]);
                }
        	}
        }
        
        for(x=0; x<xbin1; x++){ 
			if (x==0) C_x[x]=C/B;
			else C_x[x]= C/(B-A*C_x[x-1]);
		}
        for(y=0; y<ybin; y++){
        	for(z=0; z<zbin; z++){
        		for(x=0; x<xbin; x++){
        			if (x==0) D_x[x]=D[x][y][z]/B;
        			else D_x[x]= (D[x][y][z]-A*D_x[x-1])/(B-A*C_x[x-1]);
        		}
        		for(x=xbin1; x>-1; x--){
        			if (x==xbin1) next[x][y][z]=D_x[x];
        			else next[x][y][z]=D_x[x]-C_x[x]*next[x+1][y][z];
        		}
        	}
        }
        
        for(x=0; x<xbin; x++){
        	for(y=0; y<ybin; y++){
        		for(z=0; z<zbin; z++){
        			D[x][y][z]=E*next[x][y][z];
        			if (x==0 && z==0) D[x][y][z]+=f*(next[x+1][y][z]+next[x][y][z+1]);
                    else if (x==0 && z==zbin1) D[x][y][z]+=f*(next[x+1][y][z]+next[x][y][z-1]);
                    else if (x==xbin1 && z==0) D[x][y][z]+=f*(next[x-1][y][z]+next[x][y][z+1]);
                    else if (x==xbin1 && z==zbin1) D[x][y][z]+=f*(next[x-1][y][z]+next[x][y][z-1]);
                    else if (x==0) D[x][y][z]+=f*(next[x+1][y][z]+next[x][y][z-1]+next[x][y][z+1]);
                    else if (x==xbin1) D[x][y][z]+=f*(next[x-1][y][z]+next[x][y][z-1]+next[x][y][z+1]);
                    else if (z==0) D[x][y][z]+=f*(next[x-1][y][z]+next[x+1][y][z]+next[x][y][z+1]);
                    else if (z==zbin1) D[x][y][z]+=f*(next[x-1][y][z]+next[x+1][y][z]+next[x][y][z-1]);
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
        		for(y=0; y<ybin; y++){
        			if (y==0) D_y[y]=D[x][y][z]/B;
        			else D_y[y]= (D[x][y][z]-A*D_y[y-1])/(B-A*C_y[y-1]);
        		}
        		for(y=ybin1; y>-1; y--){
        			if (y==ybin1) next[x][y][z]=D_y[y];
        			else next[x][y][z]=D_y[y]-C_y[y]*next[x][y+1][z];
        		}
        	}
        }
        
        for(x=0; x<xbin; x++){
        	for(y=0; y<ybin; y++){
        		for(z=0; z<zbin; z++){
        			D[x][y][z]=E*next[x][y][z];
        			if (y==0 && x==0) D[x][y][z]+=f*(next[x][y+1][z]+next[x+1][y][z]);
        			else if (y==0 && x==xbin1) D[x][y][z]+=f*(next[x][y+1][z]+next[x-1][y][z]);
        			else if (y==ybin1 && x==0) D[x][y][z]+=f*(next[x][y-1][z]+next[x+1][y][z]);
        			else if (y==ybin1 && x==xbin1) D[x][y][z]+=f*(next[x][y-1][z]+next[x-1][y][z]);
                    else if (y==0) D[x][y][z]+=f*(next[x][y+1][z]+next[x-1][y][z]+next[x+1][y][z]);
                    else if (y==ybin1) D[x][y][z]+=f*(next[x][y-1][z]+next[x-1][y][z]+next[x+1][y][z]);
                    else if (x==0) D[x][y][z]+=f*(next[x][y-1][z]+next[x][y+1][z]+next[x+1][y][z]);
                    else if (x==xbin1) D[x][y][z]+=f*(next[x][y-1][z]+next[x][y+1][z]+next[x-1][y][z]);
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
        		for(z=0; z<zbin; z++){
        			if (z==0) D_z[z]=D[x][y][z]/B;
        			else D_z[z]= (D[x][y][z]-A*D_z[z-1])/(B-A*C_z[z-1]);
        		}
        		for(z=zbin1; z>-1; z--){
        			if (z==zbin1) space[index][x][y][z]=D_z[z];
        			else space[index][x][y][z]=D_z[z]-C_z[z]*space[index][x][y][z+1];
        		}
        	}
        }
        
        /*for (x = 0; x < xbin; x++) //in range(xbin):
            for (y = 0; y < ybin; y++) //in range(ybin):
                for (z = 0;z < zbin; z++) {
                    //D[x][y][z] = E * next[x][y][z];
                	space[x][y][z] = next[x][y][z];
                }*/
    }

}
