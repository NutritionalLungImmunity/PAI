/*
 * LinAlg.cpp
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */

#include "LinAlg.h"

namespace edu {
namespace uf {
namespace utils {

double LinAlg::getdistance2d(double x1, double y1, double x2, double y2) {
    double t1 = x1 - x2, t2 = y1 - y2;
    return std::sqrt(t1 * t1 + t2 * t2);
}

double LinAlg::euclidianDistance(const int* x, const int* y, int length) {
    double d = 0;
    for (int i = 0; i < length; ++i) {
        d += (x[i] - y[i]) * (x[i] - y[i]);
    }
    return std::sqrt(d);
}

double LinAlg::euclidianDistance(const double* x, const double* y, int length) {
    double d = 0;
    for (int i = 0; i < length; ++i) {
        d += (x[i] - y[i]) * (x[i] - y[i]);
    }
    return std::sqrt(d);
}

double LinAlg::getdistance3d(double x1, double y1, double z1, double x2, double y2, double z2) {
    double t1 = x1 - x2, t2 = y1 - y2, t3 = z1 - z2;
    return std::sqrt(t1 * t1 + t2 * t2 + t3 * t3);
}

double** LinAlg::rotation(double phi) {
	double** r = new double*[3];
	for(int i = 0; i < 3; i++)
		r[i] = new double[3];

    r[0][0] = std::cos(phi) * cosTheta;
    r[0][1] = -std::sin(phi);
    r[0][2] = std::cos(phi) * sinTheta;

    r[1][0] = std::sin(phi) * cosTheta;
    r[1][1] = std::cos(phi);
    r[1][2] = std::sin(phi) * sinTheta;

    r[2][0] = -sinTheta;
    r[2][1] = 0;
    r[2][2] = cosTheta;

    return r;
}

double** LinAlg::gramSchimidt(double x, double y, double z) {
    double** result = new double*[3];
    for (int i = 0; i < 3; ++i) {
        result[i] = new double[3];
    }

    double** base = getBase(x, y, z);
    double* e1 = new double[3];
    multiply(base[0], 1.0 / norm2(base[0], 3), e1, 3);
    double normE1 = norm2(e1, 3);
    double* e2 = new double[3];
    double* tmp = new double[3];
    multiply(e1, -dotProduct(base[1], e1, 3) / (normE1 * normE1), tmp, 3);
    sum(base[1], tmp, e2, 3);
    multiply(e2, 1.0 / norm2(e2, 3), e2, 3);
    double normE2 = norm2(e2, 3);
    double* e3 = new double[3];
    double* tmp1 = new double[3];
    multiply(e1, -dotProduct(base[2], e1, 3) / (normE1 * normE1), tmp, 3);
    multiply(e2, -dotProduct(base[2], e2, 3) / (normE2 * normE2), tmp1, 3);
    sum(base[2], tmp, e3, 3);
    sum(e3, tmp1, e3, 3);
    multiply(e3, 1.0 / norm2(e3, 3), e3, 3);

    for (int i = 0; i < 3; ++i) {
        result[0][i] = e2[i];
        result[1][i] = e3[i];
        result[2][i] = e1[i];
    }

    delete[] e1;
    delete[] e2;
    delete[] e3;
    delete[] tmp;
    delete[] tmp1;
    for (int i = 0; i < 3; ++i) {
        delete[] base[i];
    }

    return result;
}

double** LinAlg::getBase(double i, double j, double k) {
	double** result = new double*[3];
	for (int x = 0; x < 3; x++) {
		result[x] = new double[3];
	}

    result[0][0] = i;
    result[0][1] = j;
    result[0][2] = k;

    result[1][0] = 1.0;
    result[1][1] = 0.0;
    result[1][2] = 0.0;

    result[2][0] = 0.0;
    result[2][1] = 1.0;
    result[2][2] = 0.0;

    return result;
}

//modified
double** LinAlg::transpose(double** m, int lines, int cols) {
	double** result = new double*[lines];
	for (int i = 0; i < lines; ++i) {
		result[i] = new double[cols];
	}

    for (int i = 0; i < lines; ++i) {
        for (int j = 0; j < cols; ++j) {
        	result[j][i] = m[i][j];
        }
    }
    return result;
}

//modified
double** LinAlg::dotProduct(double** m1, double** m2, int lines, int cols) {
	double** result = new double*[lines];
	for (int i = 0; i < lines; ++i) {
		result[i] = new double[cols];
	}
    for (int i = 0; i < lines; ++i) {
        for (int j = 0; j < cols; ++j) {
            result[i][j] = 0;
            for (int k = 0; k < cols; ++k) {
                result[i][j] += m1[i][k] * m2[k][j];
            }
        }
    }
    return result;
}

//modified
double* LinAlg::dotProduct(double** m, double* v, int lines, int cols) {
	double* result = new double[lines];
    for (int i = 0; i < lines; ++i) {
        result[i] = 0;
        for (int j = 0; j < cols; ++j) {
            result[i] += m[i][j] * v[j];
        }
    }

    return result;
}

double LinAlg::dotProduct(const double* v1, const double* v2, int length) {
    double product = 0;
    for (int i = 0; i < length; ++i) {
        product += v1[i] * v2[i];
    }
    return product;
}

double LinAlg::norm2(const double* v, int length) {
    double norm = 0.0;
    for (int i = 0; i < length; ++i) {
        norm += v[i] * v[i];
    }
    return std::sqrt(norm);
}

void LinAlg::multiply(const double* v, double a, double* result, int length) {
    for (int i = 0; i < length; ++i) {
        result[i] = v[i] * a;
    }
}

void LinAlg::multiply(double** matrix, double val, double** result, int lines, int cols) {
    for (int i = 0; i < lines; ++i) {
        for (int j = 0; j < cols; ++j) {
            result[i][j] = matrix[i][j] * val;
        }
    }
}

void LinAlg::sum(const double* v1, const double* v2, double* result, int length) {
    for (int i = 0; i < length; ++i) {
        result[i] = v1[i] + v2[i];
    }
}

void LinAlg::unitVector(float x1, float y1, float z1, float x2, float y2, float z2, float* result) {
    float normal[3];
    normal[0] = x2 - x1;
    normal[1] = y2 - y1;
    normal[2] = z2 - z1;
    float magnitude = std::sqrt(normal[0] * normal[0] + normal[1] * normal[1] + normal[2] * normal[2]);
    result[0] = normal[0] / magnitude;
    result[1] = normal[1] / magnitude;
    result[2] = normal[2] / magnitude;
}

void LinAlg::identity(int N, double** result) {
    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < N; ++j) {
            result[i][j] = (i == j) ? 1.0 : 0.0;
        }
    }
}

void LinAlg::add(double** matrix1, double** matrix2, double** result, int lines, int cols) {
    for (int i = 0; i < lines; ++i) {
        for (int j = 0; j < cols; ++j) {
            result[i][j] = matrix1[i][j] + matrix2[i][j];
        }
    }
}

double LinAlg::determinant(double** matrix, int size) {
    if (size == 2) {
        return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
    }

    double det = 0;
    double** minorMatrix = new double*[size - 1];
    for (int i = 0; i < size - 1; ++i) {
        minorMatrix[i] = new double[size - 1];
    }

    for (int i = 0; i < size; ++i) {
        minor(matrix, 0, i, minorMatrix, size);
        det += std::pow(-1, i) * matrix[0][i] * determinant(minorMatrix, size - 1);
    }

    for (int i = 0; i < size - 1; ++i) {
        delete[] minorMatrix[i];
    }
    delete[] minorMatrix;

    return det;
}

void LinAlg::inverse(double** matrix, double** result, int size) {
    double** inverse = new double*[size];
    double** minors = new double*[size];
    for (int i = 0; i < size; ++i) {
    	inverse[i] = new double[size];
    	minors[i] = new double[size];
    }

    for (int i = 0; i < size; ++i) {
        for (int j = 0; j < size; ++j) {
        	minor(matrix, i, j, minors, size);
        	inverse[i][j] = std::pow(-1, i + j) * determinant(minors, size);
        }
    }

    double det = 1.0 / determinant(matrix, size);
    for (int i = 0; i < size; ++i) {
        for (int j = 0; j <= i; ++j) {
            double temp = inverse[i][j];
            inverse[i][j] = inverse[j][i] * det;
            inverse[j][i] = temp * det;
        }
    }

    for (int i = 0; i < size; ++i) {
        for (int j = 0; j < size; ++j) {
            result[i][j] = inverse[i][j];
        }
        delete[] inverse[i];
        delete[] minors[i];
    }
    delete[] inverse;
    delete[] minors;
}

void LinAlg::minor(double** matrix, int row, int column, double** result, int size) {
    int minorRow = 0, minorCol = 0;
    for (int i = 0; i < size; ++i) {
        if (i == row) continue;
        for (int j = 0; j < size; ++j) {
            if (j == column) continue;
            result[minorRow][minorCol++] = matrix[i][j];
        }
        ++minorRow;
        minorCol = 0;
    }
}

void LinAlg::solveTridiagonalLinearSystem(double d0, double d1, double sd0, double sd1, int xbin, int ybin, int zbin, double*** array, int transposition, double*** next) {
    int ibin = (transposition == 0) ? xbin : (transposition == 1) ? ybin : zbin;
    int jbin = (transposition == 0) ? ybin : (transposition == 1) ? xbin : ybin;
    int kbin = (transposition == 0) ? zbin : (transposition == 1) ? zbin : xbin;

    double* c = new double[ibin];
    double* d = new double[ibin];

    for (int i = 0; i < ibin; ++i) {
        if (i == 0) {
            c[i] = d1 / sd0;
        } else if (i == ibin - 1) {
            c[i] = d1 / (sd0 - d0 * c[i - 1]);
        } else {
            c[i] = d1 / (sd1 - d0 * c[i - 1]);
        }
    }

    for (int j = 0; j < jbin; ++j) {
        for (int k = 0; k < kbin; ++k) {
            for (int i = 0; i < ibin; ++i) {
                if (i == 0) {
                    d[i] = array[i][j][k] / sd0;
                } else if (i == xbin - 1) {
                    d[i] = (array[i][j][k] - d0 * d[i - 1]) / (sd0 - d0 * c[i - 1]);
                } else {
                    d[i] = (array[i][j][k] - d0 * d[i - 1]) / (sd1 - d0 * c[i - 1]);
                }
            }
            for (int i = ibin - 1; i >= 0; --i) {
                if (i == ibin - 1) {
                    next[i][j][k] = d[i];
                } else {
                    int ii = (transposition == 0) ? i + 1 : i;
                    int jj = (transposition == 1) ? j + 1 : j;
                    int kk = (transposition == 2) ? k + 1 : k;

                    next[i][j][k] = d[i] - c[i] * next[ii][jj][kk];
                }
            }
        }
    }

    delete[] c;
    delete[] d;
}

void LinAlg::solveTridiagonalLinearSystem(double diagonal0, double diagonal1, double subdiagonal0, double subdiagonal1, int size, const double* array, double* result) {
    double* c = new double[size];
    double* d = new double[size];

    for (int i = 0; i < size; ++i) {
        if (i == 0) {
            c[i] = diagonal1 / subdiagonal0;
        } else if (i == size - 1) {
            c[i] = diagonal1 / (subdiagonal0 - diagonal0 * c[i - 1]);
        } else {
            c[i] = diagonal1 / (subdiagonal1 - diagonal0 * c[i - 1]);
        }
    }

    for (int i = 0; i < size; ++i) {
        if (i == 0) {
            d[i] = array[i] / subdiagonal0;
        } else if (i == size - 1) {
            d[i] = (array[i] - diagonal0 * d[i - 1]) / (subdiagonal0 - diagonal0 * c[i - 1]);
        } else {
            d[i] = (array[i] - diagonal0 * d[i - 1]) / (subdiagonal1 - diagonal0 * c[i - 1]);
        }
    }

    for (int i = size - 1; i > -1; --i) {
        if (i == size - 1) {
            result[i] = d[i];
        } else {
            result[i] = d[i] - c[i] * result[i + 1];
        }
    }

    delete[] c;
    delete[] d;
}

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */
