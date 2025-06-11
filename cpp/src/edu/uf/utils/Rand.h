/*
 * Rand.h
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */

#pragma once

#ifndef EDU_UF_UTILS_RAND_H_
#define EDU_UF_UTILS_RAND_H_

#include <random>
#include <vector>

namespace edu {
namespace uf {
namespace utils {

using namespace std;

class Rand {
public:
	virtual ~Rand() = default;
    static Rand& getRand();
    virtual double randnorm();
    int randpois(double lambda);
    //int* sample(int max, int size);
    //virtual vector<int> sample(int max, int size);
    virtual int* sample(int max, int size);
    virtual double randunif();
    virtual int randunif(int min, int max);
    virtual mt19937_64 getGenerator();

private:
    Rand();
    Rand(const Rand&) = delete;
    Rand& operator=(const Rand&) = delete;

    static Rand* rand;
    mt19937_64 generator;
    uniform_real_distribution<> uniform_dist;
    normal_distribution<> normal_dist;
};

} /* namespace utils */
} /* namespace uf */
} /* namespace edu */

#endif /* EDU_UF_UTILS_RAND_H_ */
