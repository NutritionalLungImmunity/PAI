/*
 * GridFactory.h
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#pragma once

#ifndef EDU_UF_COMPARTMENTS_GRIDFACTORY_H_
#define EDU_UF_COMPARTMENTS_GRIDFACTORY_H_


#include <vector>
#include <string>
#include "Voxel.h"

namespace edu {
namespace uf {
namespace compartments {

class GridFactory {
public:
    static void set(int xbin, int ybin, int zbin, int numSamples, const std::string& kind);
    static int getXbin();
    static int getYbin();
    static int getZbin();
    static Voxel**** getGrid();

    static const std::string PERIODIC_GRID;

private:
    static Voxel**** grid;
    static int xbin;
    static int ybin;
    static int zbin;
    static int numSamples;
    static std::string kind;

};

} // namespace compartments
} // namespace uf
} // namespace edu/

#endif /* EDU_UF_COMPARTMENTS_GRIDFACTORY_H_ */
