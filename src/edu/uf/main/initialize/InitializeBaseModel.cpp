/*
 * InitializeBaseModel.cpp
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#include "InitializeBaseModel.h"
#include <iostream>
#include <vector>
#include <list>
#include "../../diffusion/Diffuse.h"
#include "../../compartments/GridFactory.h"
#include "../../compartments/Voxel.h"
#include "../../control/Exec.h"
//#include "../../interactable/Blood.h"
#include "../../interactable/IL10.h"
//#include "../../interactable/IL6.h"
#include "../../interactable/Iron.h"
#include "../../interactable/Lactoferrin.h"
//#include "../../interactable/Liver.h"
#include "../../interactable/MIP1B.h"
#include "../../interactable/MIP2.h"
#include "../../interactable/Macrophage.h"
#include "../../interactable/Neutrophil.h"
#include "../../interactable/PneumocyteII.h"
//#include "../../interactable/PneumocyteI.h"
#include "../../interactable/TGFb.h"
#include "../../interactable/TNFa.h"
#include "../../interactable/Transferrin.h"
#include "../../interactable/afumigatus/Afumigatus.h"
#include "../../interactable/afumigatus/TAFC.h"
#include "../../intracellularState/PneumocyteStateModel.h"
#include "../../intracellularState/AspergillusMacrophage.h"
#include "../../intracellularState/NeutrophilStateModel.h"
#include "../../utils/Constants.h"

namespace edu {
namespace uf {
namespace main {
namespace initialize {

using namespace edu::uf::control;
using namespace afumigatus;

void InitializeBaseModel::initializeMolecules(Diffuse* diffuse, bool verbose) {
    /*if (verbose) {
        std::cout << "Initializing Iron, TAFC, Lactoferrin, Transferrin, Hepcidin, IL6, TNF-a, IL10, TGF-b, MIP2, MIP1-b" << std::endl;
    }*/

    int xbin = GridFactory::getXbin();
    int ybin = GridFactory::getYbin();
    int zbin = GridFactory::getZbin();

    Iron* iron = Iron::getMolecule(nullptr, xbin, ybin, zbin, 1);
    TAFC* tafc = TAFC::getMolecule(diffuse, xbin, ybin, zbin, 2);
    Lactoferrin* lactoferrin = Lactoferrin::getMolecule(diffuse, xbin, ybin, zbin, 3);
    Transferrin* transferrin = Transferrin::getMolecule(diffuse, xbin, ybin, zbin, 3);
    //IL6* il6 = IL6::getMolecule(diffuse, xbin, ybin, zbin, 1);
    TNFa* tnfa = TNFa::getMolecule(diffuse, xbin, ybin, zbin, 1);
    IL10* il10 = IL10::getMolecule(diffuse, xbin, ybin, zbin, 1);
    TGFb* tgfb = TGFb::getMolecule(diffuse, xbin, ybin, zbin, 1);
    MIP2* mip2 = MIP2::getMolecule(diffuse, xbin, ybin, zbin, 1);
    MIP1B* mip1b = MIP1B::getMolecule(diffuse, xbin, ybin, zbin, 1);

    Exec::setMolecule(iron);
    Exec::setMolecule(tafc);
    Exec::setMolecule(lactoferrin);
    Exec::setMolecule(transferrin);
    //Exec::setMolecule(il6);
    Exec::setMolecule(tnfa);
    Exec::setMolecule(il10);
    Exec::setMolecule(tgfb);
    Exec::setMolecule(mip2);
    Exec::setMolecule(mip1b);

    Voxel::setMolecule(Iron::NAME, iron, true, true);
    Voxel::setMolecule(TAFC::NAME, tafc, true, true);
    Voxel::setMolecule(Lactoferrin::NAME, lactoferrin, false, true);

    for (int x = 0; x < xbin; ++x) {
        for (int y = 0; y < ybin; ++y) {
            for (int z = 0; z < zbin; ++z) {
                transferrin->set(utils::constexprants::DEFAULT_APOTF_CONCENTRATION, 0, x, y, z);
                transferrin->set(utils::constexprants::DEFAULT_TFFE_CONCENTRATION, 1, x, y, z);
                transferrin->set(utils::constexprants::DEFAULT_TFFE2_CONCENTRATION, 2, x, y, z);
            }
        }
    }

    Voxel::setMolecule(Transferrin::NAME, transferrin, false, true);
    //Voxel::setMolecule(IL6::NAME, il6);
    Voxel::setMolecule(TNFa::NAME, tnfa);
    Voxel::setMolecule(IL10::NAME, il10);
    Voxel::setMolecule(TGFb::NAME, tgfb);
    Voxel::setMolecule(MIP2::NAME, mip2);
    Voxel::setMolecule(MIP1B::NAME, mip1b);
}

void InitializeBaseModel::initializeBlood() {
    /*int xbin = GridFactory::getXbin();
    int ybin = GridFactory::getYbin();
    int zbin = GridFactory::getZbin();
    Voxel*** grid = GridFactory::getGrid();
    Blood* blood = Blood::getBlood(xbin, ybin, zbin);
    for (int x = 0; x < xbin; ++x) {
        for (int y = 0; y < ybin; ++y) {
            for (int z = 0; z < zbin; ++z) {
                grid[x][y][z].setCell(blood);
            }
        }
    }*/
}

void InitializeBaseModel::initializeLiver() {
    /*int xbin = GridFactory::getXbin();
    int ybin = GridFactory::getYbin();
    int zbin = GridFactory::getZbin();
    Voxel*** grid = GridFactory::getGrid();
    for (int x = 0; x < xbin; ++x) {
        for (int y = 0; y < ybin; ++y) {
            for (int z = 0; z < zbin; ++z) {
                grid[x][y][z].setCell(Liver::getLiver());
            }
        }
    }*/
}

std::vector<PneumocyteII*> InitializeBaseModel::initializePneumocytes(int numCells) {
    int xbin = GridFactory::getXbin();
    int ybin = GridFactory::getYbin();
    int zbin = GridFactory::getZbin();
    Voxel**** grid = GridFactory::getGrid();
    int k = 0;
    std::vector<PneumocyteII*> list;
    while (k < numCells) {
        int x = randint(0, xbin);
        int y = randint(0, ybin);
        int z = randint(0, zbin);
        if (grid[x][y][z]->getCells().empty()) {
            PneumocyteII* p = new PneumocyteII(new PneumocyteStateModel());
            grid[x][y][z]->setCell(p);
            list.push_back(p);
            k++;
        }
        if (k % 100000 == 0) {
            std::cout << k << " pneumocytes initialized ..." << std::endl;
        }
    }
    return list;
}

void InitializeBaseModel::initializeTypeIPneumocytes(int numCells) {
    /*int xbin = GridFactory::getXbin();
    int ybin = GridFactory::getYbin();
    int zbin = GridFactory::getZbin();

    int x0 = 0;
    int y0 = 0;
    int z0 = 0;

    double p = numCells / static_cast<double>(xbin * ybin * zbin);
    Voxel*** grid = GridFactory::getGrid();
    PneumocyteI* cell = new PneumocyteI(IntracellularModelFactory::createBooleanNetwork(IntracellularModelFactory::PNEUMOCYTE_I_KLEBSIELLA_MODEL));

    for (int x = 0; x < xbin; ++x) {
        for (int y = 0; y < ybin; ++y) {
            for (int z = 0; z < zbin; ++z) {
                if (rand() / static_cast<double>(RAND_MAX) < p || !grid[x][y][z].getNeighbors().contains(grid[x0][y0][z0])) {
                    cell = new PneumocyteI(IntracellularModelFactory::createBooleanNetwork(IntracellularModelFactory::PNEUMOCYTE_I_KLEBSIELLA_MODEL));
                }
                grid[x][y][z].setCell(cell);
                x0 = x;
                y0 = y;
                z0 = z;
            }
        }
    }*/
}

std::vector<Macrophage*> InitializeBaseModel::initializeMacrophage(int numMacrophages) {
    int xbin = GridFactory::getXbin();
    int ybin = GridFactory::getYbin();
    int zbin = GridFactory::getZbin();
    Voxel**** grid = GridFactory::getGrid();
    std::vector<Macrophage*> list;
    for (int i = 0; i < numMacrophages; ++i) {
        int x = randint(0, xbin - 1);
        int y = randint(0, ybin - 1);
        int z = randint(0, zbin - 1);
        Macrophage* m = new Macrophage(utils::constexprants::MA_INTERNAL_IRON, new AspergillusMacrophage());
        list.push_back(m);
        grid[x][y][z]->setCell(m);
    }
    return list;
}

std::vector<Neutrophil*> InitializeBaseModel::initializeNeutrophils(int numNeut) {
    int xbin = GridFactory::getXbin();
    int ybin = GridFactory::getYbin();
    int zbin = GridFactory::getZbin();
    Voxel**** grid = GridFactory::getGrid();
    std::vector<Neutrophil*> list;
    for (int i = 0; i < numNeut; ++i) {
        int x = randint(0, xbin - 1);
        int y = randint(0, ybin - 1);
        int z = randint(0, zbin - 1);
        Neutrophil* n = new Neutrophil(0.0, new NeutrophilStateModel());
        list.push_back(n);
        grid[x][y][z]->setCell(n);
    }
    return list;
}

std::vector<Afumigatus*> InitializeBaseModel::infect(int numAspergillus, int status, double initIron, double sigma, bool verbose) {
    int xbin = GridFactory::getXbin();
    int ybin = GridFactory::getYbin();
    int zbin = GridFactory::getZbin();
    Voxel**** grid = GridFactory::getGrid();
    if (verbose) {
        std::cout << "Infecting with " << numAspergillus << " conidia!" << std::endl;
    }
    std::vector<Afumigatus*> list;
    for (int i = 0; i < numAspergillus; ++i) {
        int x = randint(0, xbin - 1);
        int y = randint(0, ybin - 1);
        int z = randint(0, zbin - 1);
        Afumigatus* a = new Afumigatus(x, y, z, x, y, z, random(), random(), random(), 0, initIron, status, true);
        list.push_back(a);
        grid[x][y][z]->setCell(a);
    }
    return list;
}

} // namespace initialize
} // namespace main
} // namespace uf
} // namespace edu
