/*
 * Rand.cpp
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */

#include "Rand.h"
#include <cmath>
#include <algorithm>

namespace edu {
namespace uf {
namespace utils {

Rand* Rand::rand = nullptr;

Rand::Rand()
    : generator(random_device{}()), uniform_dist(0.0, 1.0), normal_dist(0.0, 1.0) {}

Rand& Rand::getRand() {
    if (Rand::rand == nullptr) {
        Rand::rand = new Rand();
    }
    return *Rand::rand;
}

double Rand::randnorm() {
    double u1, u2, v1 = 0, v2 = 0;
    double s = 2;
    while (s >= 1) {
        u1 = uniform_dist(generator);
        u2 = uniform_dist(generator);
        v1 = 2.0 * u1 - 1.0;
        v2 = 2.0 * u2 - 1.0;
        s = v1 * v1 + v2 * v2;
    }
    double x = v1 * sqrt((-2.0 * log(s)) / s);
    return x;
}

int Rand::randpois(double lambda) {
    double L = exp(-lambda);
    double p = 1.0;
    int k = 0;
    do {
        k++;
        p *= uniform_dist(generator);
    } while (p > L);
    return k - 1;
}

/*int* Rand::sample(int max, int size) {
    if (max == 0 || size == 0) return nullptr;
    int* array = new int[size];
    vector<int> indices(max);
    iota(indices.begin(), indices.end(), 0);

    int n = max;
    for (int i = 0; i < size; ++i) {
        uniform_int_distribution<int> dist(0, n - 1);
        int k = dist(generator);
        array[i] = indices[k];
        indices.erase(indices.begin() + k);
        --n;
    }

    return array;
}*/

/*vector<int> Rand::sample(int max, int size) {
    if (max == 0 || size == 0) return vector<int>();

    vector<int> result(size);
    vector<int> indices(max);
    iota(indices.begin(), indices.end(), 0);

    int n = max;
    for (int i = 0; i < size; ++i) {
        uniform_int_distribution<int> dist(0, n - 1);
        int k = dist(generator);
        result[i] = indices[k];
        indices.erase(indices.begin() + k);
        --n;
    }

    return result;
}*/


int* Rand::sample(int max, int size) {
    if (max == 0 || size == 0) return new int[0];

    int *result = new int[size];
    vector<int> indices(max);
    iota(indices.begin(), indices.end(), 0);

    int n = max;
    for (int i = 0; i < size; ++i) {
        uniform_int_distribution<int> dist(0, n - 1);
        int k = dist(generator);
        result[i] = indices[k];
        indices.erase(indices.begin() + k);
        --n;
    }

    return result;
}

double Rand::randunif() {
    return uniform_dist(generator);
}

int Rand::randunif(int min, int max) {
    uniform_int_distribution<int> dist(min, max - 1);
    return dist(generator);
}

mt19937_64 Rand::getGenerator(){
	return generator;
}

} /* namespace utils */
} /* namespace uf */
} /* namespace edu */
