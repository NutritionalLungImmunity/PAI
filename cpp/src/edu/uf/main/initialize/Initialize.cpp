/*
 * Initialize.cpp
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#include "Initialize.h"
#include "../../compartments/GridFactory.h"

namespace edu {
namespace uf {
namespace main {
namespace initialize {


random_device Initialize::rd;
mt19937 Initialize::gen(rd());

double Initialize::random() {
    uniform_real_distribution<> dis(0.0, 1.0);
    return dis(gen);
}

int Initialize::randint(int min, int max) {
    uniform_int_distribution<> dis(min, max - 1);
    return dis(gen);
}

void Initialize::setNumSamples(int numSamples) {
    this->numSamples = numSamples;
}

int Initialize::getNumSamples() const {
    return numSamples;
}

Voxel**** Initialize::createPeriodicGrid(int xbin, int ybin, int zbin) {
    GridFactory::set(xbin, ybin, zbin, numSamples, GridFactory::PERIODIC_GRID);
    return GridFactory::getGrid();
}

} // namespace initialize
} // namespace main
} // namespace uf
} // namespace edu
