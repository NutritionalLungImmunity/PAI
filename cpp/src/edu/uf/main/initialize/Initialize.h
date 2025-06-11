/*
 * Initialize.h
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#ifndef EDU_UF_MAIN_INITIALIZE_INITIALIZE_H_
#define EDU_UF_MAIN_INITIALIZE_INITIALIZE_H_

#include <vector>
#include <random>
#include "../../diffusion/Diffuse.h"
#include "../../compartments/Voxel.h"
#include "../../interactable/Macrophage.h"
#include "../../interactable/Neutrophil.h"
#include "../../interactable/PneumocyteII.h"
#include "../../interactable/afumigatus/Afumigatus.h"

namespace edu {
namespace uf {
namespace main {
namespace initialize {

using namespace std;
using namespace edu::uf::compartments;
using namespace edu::uf::interactable;
using namespace afumigatus;

class Initialize {
public:
    Initialize() : numSamples(-1) {}

    virtual ~Initialize() = default;

    static double random();
    static int randint(int min, int max);

    virtual void setNumSamples(int numSamples);
    virtual int getNumSamples() const;

    Voxel**** createPeriodicGrid(int xbin, int ybin, int zbin);

    virtual void initializeMolecules(Diffuse* diffuse, bool verbose) = 0;
    virtual void initializeLiver() = 0;
    virtual void initializeTypeIPneumocytes(int numCells) = 0;
    virtual void initializeBlood() = 0;
    virtual vector<PneumocyteII*> initializePneumocytes(int numCells) = 0;
    virtual vector<Macrophage*> initializeMacrophage(int numMacrophages) = 0;
    virtual vector<Neutrophil*> initializeNeutrophils(int numNeut) = 0;
    virtual vector<Afumigatus*> infect(int numAspergillus, int status, double initIron, double sigma, bool verbose) = 0;

private:
    int numSamples;
    static random_device rd;
    static mt19937 gen;
};

} // namespace initialize
} // namespace main
} // namespace uf
} // namespace edu

#endif /* EDU_UF_MAIN_INITIALIZE_INITIALIZE_H_ */
