/*
 * Recruiter.cpp
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#include "Recruiter.h"
#include "../utils/Rand.h"
#include "GridFactory.h"

namespace edu {
namespace uf {
namespace compartments {

void Recruiter::recruit() {
    Voxel**** grid = GridFactory::getGrid();
    int qtty = getQtty();
    for (int i = 0; i < qtty; ++i) {
        int x = Rand::getRand().randunif(0, GridFactory::getXbin() - 1);
        int y = Rand::getRand().randunif(0, GridFactory::getYbin() - 1);
        int z = Rand::getRand().randunif(0, GridFactory::getZbin() - 1);

        grid[x][y][z]->setCell(createCell());
    }
}

} // namespace compartments
} // namespace uf
} // namespace edu
