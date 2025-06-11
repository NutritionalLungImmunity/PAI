/*
 * LinAlg.h
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */

#pragma once

#ifndef EDU_UF_UTILS_LINALG_H_
#define EDU_UF_UTILS_LINALG_H_

#include <cmath>
#include <vector>
#include <iostream>
#include <stdexcept>

namespace edu {
namespace uf {
namespace utils {

class LinAlg {
private:
	static constexpr double cosTheta = 0.7071067811865475; //cos(M_PI / 4.0);
	static constexpr double sinTheta = 0.7071067811865475; //sin(M_PI / 4.0);

public:
    static double getdistance2d(double x1, double y1, double x2, double y2);
    static double euclidianDistance(const int* x, const int* y, int length);
    static double euclidianDistance(const double* x, const double* y, int length);
    static double getdistance3d(double x1, double y1, double z1, double x2, double y2, double z2);
    static double** rotation(double phi);
    static double** gramSchimidt(double x, double y, double z);
    static double** getBase(double i, double j, double k);
    static double** transpose(double** m, int lines, int cols);
    static double** dotProduct(double** m1, double** m2, int lines, int cols);
    static double* dotProduct(double** m, double* vt, int lines, int cols);
    static double dotProduct(const double* v1, const double* v2, int length);
    static double norm2(const double* v, int length);
    static void multiply(const double* v, double a, double* result, int length);
    static void multiply(double** matrix, double val, double** result, int lines, int cols);
    static void sum(const double* v1, const double* v2, double* result, int length);
    static void unitVector(float x1, float y1, float z1, float x2, float y2, float z2, float* result);
    static void identity(int N, double** result);
    static void add(double** matrix1, double** matrix2, double** result, int lines, int cols);
    static double determinant(double** matrix, int size);
    static void inverse(double** matrix, double** result, int size);
    static void minor(double** matrix, int row, int column, double** result, int size);
    static void solveTridiagonalLinearSystem(double d0, double d1, double sd0, double sd1, int xbin, int ybin, int zbin, double*** array, int transposition, double*** next);
    static void solveTridiagonalLinearSystem(double diagonal0, double diagonal1, double subdiagonal0, double subdiagonal1, int size, const double* array, double* result);

};

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */

#endif /* EDU_UF_UTILS_LINALG_H_ */
