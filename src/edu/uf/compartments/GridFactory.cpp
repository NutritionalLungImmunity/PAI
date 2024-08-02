/*
 * GridFactory.cpp
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#include "GridFactory.h"
#include "Voxel.h" // Assuming Voxel class is defined in Voxel.h

namespace edu {
namespace uf {
namespace compartments {

const std::string GridFactory::PERIODIC_GRID = "PERIODIC_GRID";
Voxel**** GridFactory::grid = nullptr;
int GridFactory::xbin = 0;
int GridFactory::ybin = 0;
int GridFactory::zbin = 0;
int GridFactory::numSamples = 0;
std::string GridFactory::kind = "";

void GridFactory::set(int xbin, int ybin, int zbin, int numSamples, const std::string& kind) {
    GridFactory::xbin = xbin;
    GridFactory::ybin = ybin;
    GridFactory::zbin = zbin;
    GridFactory::numSamples = numSamples;
    GridFactory::kind = kind;
}

int GridFactory::getXbin() {
    return GridFactory::xbin;
}

int GridFactory::getYbin() {
    return GridFactory::ybin;
}

int GridFactory::getZbin() {
    return GridFactory::zbin;
}

Voxel**** GridFactory::getGrid() {
    if (grid == nullptr) {
        grid = new Voxel***[xbin];
        for (int i = 0; i < xbin; ++i) {
            grid[i] = new Voxel**[ybin];
            for (int j = 0; j < ybin; ++j) {
                grid[i][j] = new Voxel*[zbin];
            }
        }
        if (kind == PERIODIC_GRID) {
            for (int x = 0; x < xbin; ++x) {
                for (int y = 0; y < ybin; ++y) {
                    for (int z = 0; z < zbin; ++z) {
                        grid[x][y][z] = new Voxel(x, y, z, numSamples);
                    }
                }
            }

            Voxel::setXbin(xbin);
            Voxel::setYbin(ybin);
            Voxel::setZbin(zbin);

            for (int x = 0; x < xbin; ++x) {
                for (int y = 0; y < ybin; ++y) {
                    for (int z = 0; z < zbin; ++z) {
                        if (x + 1 < xbin)
                            grid[x][y][z]->getNeighbors().push_back(grid[x + 1][y][z]);
                        else
                            grid[x][y][z]->getNeighbors().push_back(grid[0][y][z]);
                        if (y + 1 < ybin)
                            grid[x][y][z]->getNeighbors().push_back(grid[x][y + 1][z]);
                        else
                            grid[x][y][z]->getNeighbors().push_back(grid[x][0][z]);
                        if (x - 1 >= 0)
                            grid[x][y][z]->getNeighbors().push_back(grid[x - 1][y][z]);
                        else
                            grid[x][y][z]->getNeighbors().push_back(grid[xbin - 1][y][z]);
                        if (y - 1 >= 0)
                            grid[x][y][z]->getNeighbors().push_back(grid[x][y - 1][z]);
                        else
                            grid[x][y][z]->getNeighbors().push_back(grid[x][ybin - 1][z]);
                        if (z + 1 < zbin)
                            grid[x][y][z]->getNeighbors().push_back(grid[x][y][z + 1]);
                        else
                            grid[x][y][z]->getNeighbors().push_back(grid[x][y][0]);
                        if (z - 1 >= 0)
                            grid[x][y][z]->getNeighbors().push_back(grid[x][y][z - 1]);
                        else
                            grid[x][y][z]->getNeighbors().push_back(grid[x][y][zbin - 1]);
                    }
                }
            }
        }
    }
    return grid;
}

} // namespace compartments
} // namespace uf
} // namespace edu

