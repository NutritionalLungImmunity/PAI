package edu.uf.utils;

import edu.uf.interactable.Afumigatus;

public class LinAlg {
	
	private static final double cosTheta = Math.cos(Math.PI/4.0);
	private static final double sinTheta = Math.sin(Math.PI/4.0);
	
	
	/**
	 * compute the geometric distance in 2d between point(x1, y1) and point(x2, y2) 
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @return
	 */
	public static double getdistance2d(double x1, double y1, double x2, double y2) {
		double t1=x1-x2, t2=y1-y2;
		return (Math.sqrt(t1*t1+t2*t2)); 
	}
	
	
	public static double euclidianDistance(int[] x, int[] y) {
		if(x.length != y.length) {
			System.err.println("error non-conformable arguments");
			System.exit(1);
		}
		double d = 0;
		for(int i = 0; i < x.length; i++) {
			d += (x[i] - y[i])*(x[i] - y[i]);
		}
		return Math.sqrt(d); 
	}
	
	public static double euclidianDistance(double[] x, double[] y) {
		if(x.length != y.length) {
			System.err.println("error non-conformable arguments");
			System.exit(1);
		}
		double d = 0;
		for(int i = 0; i < x.length; i++) {
			d += (x[i] - y[i])*(x[i] - y[i]);
		}
		return Math.sqrt(d); 
	}
	
	/**
	 * compute the geometric distance in 3d between point(x1, y1, z1) and point(x2, y2, z2) 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return
	 */
	public static double getdistance3d(double x1, double y1, double z1, double x2, double y2, double z2)
	{
	  double t1=x1-x2, t2=y1-y2, t3=z1-z2;
	  return (Math.sqrt(t1*t1+t2*t2+t3*t3));
	}
	
	public static double[][] rotation(double phi){
		double[][] r = new double[3][3];
		
		r[0][0] =   Math.cos(phi)*cosTheta;
		r[0][1] =  -Math.sin(phi);
		r[0][2] =   Math.cos(phi)*sinTheta;
		
		r[1][0] =  Math.sin(phi)*cosTheta;
		r[1][1] =  Math.cos(phi);
		r[1][2] =  Math.sin(phi)*sinTheta;
		
		r[2][0] =  -sinTheta;
		r[2][1] =   0;
		r[2][2] =   cosTheta;
		/*double phi1 = 2.0*Rand.getRand().randunif()*Math.PI;
		double[][] Rx = new double[][] {
			new double[] {1.0, 0.0, 0.0}, 
			new double[] {0.0, Math.cos(phi1), -Math.sin(phi1)},
			new double[] {0.0, Math.sin(phi1),  Math.cos(phi1)}
		};
		
		double phi2 = Math.PI/4;//2.0*Rand.getRand().randunif()*Math.PI;
		double[][] Ry = new double[][] {
			new double[] { Math.cos(phi2), 0.0, Math.sin(phi2)},
			new double[] {0.0, 1.0, 0.0},
			new double[] {-Math.sin(phi2), 0.0, Math.cos(phi2)}
		};*/
		
		/*System.out.println();
		for(double[] v : Ry) {
			for(double d : v) {
				System.out.print(d + " ");
			}
			System.out.println();
		}
		System.out.println("######");*/
		
		/*double[][] Rz = new double[][] {
			new double[] {cosTheta, -sinTheta, 0.0},
			new double[] {sinTheta,  cosTheta, 0.0},
			new double[] {0.0, 0.0, 1.0}
		};*/
		
		/*double phi3 = 2.0*Rand.getRand().randunif()*Math.PI;
		double[][] Rz = new double[][] {
			new double[] {Math.cos(phi3), -Math.sin(phi3), 0.0},
			new double[] {Math.sin(phi3),  Math.cos(phi3), 0.0},
			new double[] {0.0, 0.0, 1.0}
		};
		
		r= dotProduct(Rz, Ry);*
		
		/*r[0][0] =  Math.cos(phi);
		r[0][1] = -Math.sin(phi)*cosTheta;
		r[0][2] =  Math.sin(phi)*sinTheta;
		
		r[1][0] =  Math.sin(phi);
		r[1][1] =  Math.cos(phi)*cosTheta;
		r[1][2] =  -Math.cos(phi)*sinTheta;
		
		r[2][0] =  0;
		r[2][1] =  sinTheta;
		r[2][2] =  cosTheta;*/
		
		
		/*r[0][0] =  cosTheta;
		r[0][1] = -sinTheta*Math.cos(phi);
		r[0][2] =  sinTheta*Math.sin(phi);
		
		r[1][0] =  sinTheta;
		r[1][1] =  cosTheta*Math.cos(phi);
		r[1][2] =  -cosTheta*Math.sin(phi);
		
		r[2][0] =  0;
		r[2][1] =  Math.sin(phi);
		r[2][2] =  Math.cos(phi);*/
		
		
		 /* r[0][0] =  cosTheta;
		r[0][1] = -sinTheta;
		r[0][2] =  0;
		
		r[1][0] =  Math.cos(phi)*sinTheta;
		r[1][1] =  Math.cos(phi)*cosTheta;
		r[1][2] =  -Math.sin(phi);
		
		r[2][0] =  Math.sin(phi)*sinTheta;
		r[2][1] =  Math.sin(phi)*cosTheta;
		r[2][2] =  Math.cos(phi);
		
		
		/*
		 * 
		r[0][0] =  cosTheta*Math.cos(phi);
		r[0][1] = -cosTheta*Math.sin(phi);
		r[0][2] =  sinTheta;
		
		r[1][0] =  Math.sin(phi);
		r[1][1] =  Math.cos(phi);
		r[1][2] =  0.0;
		
		r[2][0] = -sinTheta*Math.cos(phi);
		r[2][1] =  sinTheta*Math.sin(phi);
		r[2][2] =  cosTheta;
		 * */
		
		return r;
	}
	
	public static double[][] gramSchimidt(Afumigatus afumigatus){
		double[][] base = getBase(afumigatus);
		double[] e1 = multiply(base[0], 1.0/norm(base[0]));
		double normE1 = norm(e1);
		double[] e2 = sum(base[1], multiply(e1, -dotProduct(base[1], e1)/(normE1*normE1)));
		e2 = multiply(e2, 1.0/norm(e2));
		double normE2 = norm(e2);
		double[] e3 = sum(sum(base[2], multiply(e1, -dotProduct(base[2], e1)/(normE1*normE1))), multiply(e2, -dotProduct(base[2], e2)/(normE2*normE2)));
		e3 = multiply(e3, 1.0/norm(e3));
		return transpose(new double[][] {
			e2, e3, e1
		});
	}
	
	public static double[][] getBase(Afumigatus afumigatus){
		double[] x = new double[]{1.0, 0.0, 0.0};
		double[] y = new double[]{0.0, 1.0, 0.0};
		//double[] z = new double[]{0.0, 0.0, afumigatus.getZ()};
		double[] v = new double[]{afumigatus.getDx(), afumigatus.getDy(), afumigatus.getDz()};
		return new double[][] {
			v,x,y
		};
	}
	
	public static double[][] transpose(double[][] m){
		int lines = m.length;
		int cols  = m[0].length;
		double[][] m1 = new double[lines][cols];
		for(int i = 0; i < lines; i++) {
			for(int j = 0; j < cols; j++) {
				m1[j][i] = m[i][j];
			}
		}
		return m1;
	}
	
	public static double[][] dotProduct(double[][] m1, double[][] m2) {
		int lines = m1.length;
		int cols  = m1[0].length;
		double[][] m = new double[lines][cols];
		
		for(int i = 0; i < lines; i++) {
			for(int j = 0; j < cols; j++) {
				for(int k = 0; k < cols; k++) {
					m[i][j] += m1[i][k]*m2[k][j];
				}
			}
		}
		return m;
	}
	
	public static double[] dotProduct(double[][] m, double[] v) {
		int lines = m.length;
		int cols  = m[0].length;
		double[] v1 = new double[v.length];
		for(int i = 0; i < lines; i++) {
			for(int j = 0; j < cols; j++) {
				v1[i] += m[i][j]*v[j];
			}
		}
		return v1;
	}
	
	public static double dotProduct(double[] v1, double[] v2) {
		double product = 0;
		for(int i = 0; i < v1.length; i++) { 
			product += v1[i]*v2[i];
		}
		
		return product;
	}
	
	public static double norm(double[] v) {
		double norm = 0.0;
		for(int i = 0; i < v.length; i++) { 
			norm += v[i]*v[i];
		}
		return Math.sqrt(norm);
	}
	
	public static double[] multiply(double[] v, double a) {
		double[] copy = v.clone();
		for(int i = 0; i < v.length; i++) {
			copy[i] = v[i]*a;
		}
		return copy;
	}
	
	public static double[] sum(double[] v1, double[] v2) {
		double[] v = new double[v1.length];
		for(int i = 0; i < v1.length; i++) {
			v[i] = v1[i] + v2[i];
		}
		
		return v;
	}
	
	/***
	 * unit vector from (x1, y1, z1) to (x2, y2, z2)
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return
	 */
	public static float[] unitVector(float x1, float y1, float z1, float x2, float y2, float z2){
		float[] normal = new float[3];
		normal[0] = x2 - x1;
		normal[1] = y2 - y1;
		normal[2] = z2 - z1;
		float magnitude = (float)Math.sqrt(normal[0]*normal[0] + normal[1] * normal[1] + normal[2] * normal[2]);
		float[] unit = new float[3];
		unit[0] = normal[0] / magnitude;
		unit[1] = normal[1] / magnitude;
		unit[2] = normal[2] / magnitude;
		return unit;
	}
	
	public static void dotProduct(double[][] matrix, double[][][] vector, int dim1, int dim2, int dim3) {

    	int size = matrix.length;
    	
    	//dim1 == -1 ? vector.length : (dim2 == -1 ? vector[0].length : vector[0][0].length)
		double[] v = new double[(dim1 == -1 ? vector.length : (dim2 == -1 ? vector[0].length : vector[0][0].length))];
		for(int i = 0; i < size; i++) 
			for(int j = 0; j < size; j++) 
				v[i] += matrix[i][j]*(dim1 == -1 ? vector[j][dim2][dim3] : (dim2 == -1 ? vector[dim1][j][dim3] : vector[dim1][dim2][j]));
		
		for(int i = 0; i < size; i++) {
			if(dim1 == -1)
				vector[i][dim2][dim3] = v[i];
			else if(dim2 == -1)
				vector[dim1][i][dim3] = v[i];
			else
				vector[dim1][dim2][i] = v[i];
		}
    }
	
	
	public static double[][] identity(int N){
    	double[][] eye = new double[N][N];
    	for(int i = 0; i < N; i++)
    		eye[i][i] = 1;
    	return eye;
    }
    
    public static double[][] add(double[][] matrix1, double[][] matrix2){
    	int lines = matrix1.length;
    	int cols = matrix1[0].length;
    	
    	double[][] result = new double[lines][cols];
    	for(int i = 0; i < lines; i++)
    		for(int j = 0; j < cols; j++)
    			result[i][j] = matrix1[i][j] + matrix2[i][j];
    	
    	return result;
    }
    
    public static double determinant(double[][] matrix) {
        if (matrix.length != matrix[0].length)
            throw new IllegalStateException("invalid dimensions");

        if (matrix.length == 2)
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];

        double det = 0;
        for (int i = 0; i < matrix[0].length; i++)
            det += Math.pow(-1, i) * matrix[0][i]
                    * determinant(minor(matrix, 0, i));
        return det;
    }

    public static double[][] inverse(double[][] matrix) {
        double[][] inverse = new double[matrix.length][matrix.length];

        // minors and cofactors
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                inverse[i][j] = Math.pow(-1, i + j)
                        * determinant(minor(matrix, i, j));

        // adjugate and determinant
        double det = 1.0 / determinant(matrix);
        for (int i = 0; i < inverse.length; i++) {
            for (int j = 0; j <= i; j++) {
                double temp = inverse[i][j];
                inverse[i][j] = inverse[j][i] * det;
                inverse[j][i] = temp * det;
            }
        }

        return inverse;
    }

    public static double[][] minor(double[][] matrix, int row, int column) {
        double[][] minor = new double[matrix.length - 1][matrix.length - 1];

        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; i != row && j < matrix[i].length; j++)
                if (j != column)
                    minor[i < row ? i : i - 1][j < column ? j : j - 1] = matrix[i][j];
        return minor;
    }
    
    public static double[][] multiply(double[][] matrix, double val){
    	int lines = matrix.length;
    	int cols = matrix[0].length;
    	
    	double[][] result = new double[lines][cols];
    	for(int i = 0; i < lines; i++)
    		for(int j = 0; j < cols; j++)
    			result[i][j] = matrix[i][j]*val;
    	
    	return result;
    }
    
    
    /*
     * solve the system
     *| d1   sd1   0    0    0  ...               | 
     *| sd1  d0   sd0   0    0  ...               | 
     *|  0   sd0  d0   sd0   0  ...               |
     *|  0    0   sd0  d0   sd0 ...               | 
     *|  0    0    0   sd0  d0  ...               | 
     *|  .    .    .    .    .  .                 | * Y[xbin X ybin X zbin] = array[xbin X ybin X zbin]
     *|  .    .    .    .    .    .               |
     *|  .    .    .    .    .      .             |
     *|  0    0    0    0    0  ... sd0  d0   sd1 |
     *|  0    0    0    0    0  ...  0   sd1  d1  |
     * 
     * This method solves the linear system vector by vector of three-D array "Y". 
     */
    /**
     * 
     * @param d0
     * @param d1
     * @param sd0
     * @param sd1
     * @param xbin
     * @param ybin
     * @param zbin
     * @param array
     * @param transposition
     * @return Y
     * This method solves the tridiagonal linear system
     * 
     */
    public static double[][][] solveTridiagonalLinearSystem(
    		double d0, 
    		double d1, 
    		double sd0, 
    		double sd1,
    		int xbin,
    		int ybin,
    		int zbin,
    		double[][][] array,
    		int transposition
    ){
    	
    	int ibin = transposition == 0 ? xbin : (transposition == 1 ? ybin : zbin);
    	int jbin = transposition == 0 ? ybin : (transposition == 1 ? xbin : ybin);
    	int kbin = transposition == 0 ? zbin : (transposition == 1 ? zbin : xbin);
    	
    	
    	double[] c = new double[ibin];
    	double[] d = new double[ibin];
    	
    	double[][][] next = new double[xbin][ybin][zbin];
    	
    	
    	for (int i = 0; i < ibin; i++) {
            if (i == 0)
                c[i] = d1 / sd0;
            else if (i == (ibin - 1))
            	c[i] = d1 / (sd0 - d0 * c[i - 1]);
            else
                c[i] = d1 / (sd1 - d0 * c[i - 1]);
		}
    	for (int j = 0; j < jbin; j++) {
        	for (int k = 0;k < kbin; k++) {
        		for (int i = 0; i < ibin; i++) {
                    if (i == 0)
                        d[i] = array[i][j][k] / sd0;
                    else if (i == xbin - 1)
                        d[i] = (array[i][j][k] - d0 * d[i - 1]) / (sd0 - d0 * c[i - 1]);
                    else
                        d[i] = (array[i][j][k] - d0 * d[i - 1]) / (sd1 - d0 * c[i - 1]);
        		}
        		for (int i = ibin-1; i >= 0; i--) {
                    if (i == ibin - 1)
                        next[i][j][k] = d[i];
                    else {
                    	
                    	int ii = transposition == 0 ? i + 1 : i;
                    	int jj = transposition == 1 ? j + 1 : j;
                    	int kk = transposition == 2 ? k + 1 : k;
                    	
                        next[i][j][k] = d[i] - c[i] * next[ii][jj][kk]; //x+1
                    }
        		}
        	}
        }
		return next;
    }
    
    /**
     * 
     * @param d0
     * @param d1
     * @param sd0
     * @param sd1
     * @param size
     * @param array
     * @return vector
     * solve the system
     *| d1   sd1   0    0    0  ...               | 
     *| sd1  d0   sd0   0    0  ...               | 
     *|  0   sd0  d0   sd0   0  ...               |
     *|  0    0   sd0  d0   sd0 ...               | 
     *|  0    0    0   sd0  d0  ...               | 
     *|  .    .    .    .    .  .                 | * vector = array
     *|  .    .    .    .    .    .               |
     *|  .    .    .    .    .      .             |
     *|  0    0    0    0    0  ... sd0  d0   sd1 |
     *|  0    0    0    0    0  ...  0   sd1  d1  |
     */
    public static double[] solveTridiagonalLinearSystem(
    		double diagonal0, 
    		double diagonal1, 
    		double subdiagonal0, 
    		double subdiagonal1,
    		int size,
    		double[] array		
    ) {
    	
    	double[] c = new double[size];
    	double[] d = new double[size];
    	double[] x = new double[size];
    	
    	for (int i = 0; i < size; i++) {
            if (i == 0)
                c[i] = diagonal1 / subdiagonal0;
            else if (i == (size - 1))
            	c[i] = diagonal1 / (subdiagonal0 - diagonal0 * c[i - 1]);
            else
                c[i] = diagonal1 / (subdiagonal1 - diagonal0 * c[i - 1]);
		}
    	for (int i = 0; i < size; i++) {
            if (i == 0)
                d[i] = array[i] / subdiagonal0;
            else if (i == size - 1)
                d[i] = (array[i] - diagonal0 * d[i - 1]) / (subdiagonal0 - diagonal0 * c[i - 1]);
            else
                d[i] = (array[i] - diagonal0 * d[i - 1]) / (subdiagonal1 - diagonal0 * c[i - 1]);
		}
        for(int i=(size-1); i>-1; i--){
        	if (i==(size-1)) x[i]=d[i];
            else x[i]=d[i]-c[i]*x[i+1];
        }
        
        return x;
    }
}
