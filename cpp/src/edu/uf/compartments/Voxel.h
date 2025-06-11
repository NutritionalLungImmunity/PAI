/*
 * Voxel.h
 *
 *  Created on: Jul 19, 2024
 *      Author: henriquedeassis
 */

#pragma once

#ifndef EDU_UF_COMPARTMENTS_VOXEL_H_
#define EDU_UF_COMPARTMENTS_VOXEL_H_


#include <vector>
#include <string>
#include "../interactable/Cell.h"
#include "../interactable/InfectiousAgent.h"
#include "../interactable/Interactable.h"
#include "../interactable/Leukocyte.h"
#include "../interactable/Molecule.h"
#include "../utils/Rand.h"
#include <map>

namespace edu {
namespace uf {
namespace compartments {

using namespace edu::uf::interactable;
using namespace edu::uf::utils;

class Voxel {
public:
    static const int AIR = 0;
    static const int EPITHELIUM = 1;
    static const int REGULAR_TISSUE = 2;
    static const int BLOOD = 3;


    Voxel() = default; // @suppress("Class members should be properly initialized")
    Voxel(int x, int y, int z, int numSamples);

    virtual ~Voxel() = default;


    int getX() const;
    void setX(int x);

    int getY() const;
    void setY(int y);

    int getZ() const;
    void setZ(int z);

    virtual double getP() const;
    virtual void setP(double p);

    virtual int getTissueType() const;
    virtual void setTissueType(int tissueType);

    virtual void setExternalState(int state);

    virtual std::map<int, InfectiousAgent*>& getInfectiousAgents();
    virtual std::map<int, Interactable*>& getInteractables();
    virtual std::map<int, Cell*>& getCells();
    static std::map<std::string, Molecule*>& getMolecules();
    virtual std::vector<Voxel*>& getNeighbors();
    virtual void setNeighbors(const std::vector<Voxel*>& neighbors);

    virtual void setCell(Cell* cell);
    virtual Cell* removeCell(int id);

    static void setMolecule(const std::string& molName, Molecule* molecule);
    static void setMolecule(const std::string& molName, Molecule* molecule, bool infectiousAgentMolecule, bool moleculeInteractable);
    static Molecule* getMolecule(const std::string& molName);

    virtual void setNeighbor(Voxel* neighbor);
    virtual void next(int x, int y, int z);
    virtual void degrade();
    virtual void interact();

    static void setXbin(int xbin);
    static int getXbin();

    static void setYbin(int xbin);
    static int getYbin();

    static void setZbin(int xbin);
    static int getZbin();

    virtual std::string toString() const;

private:
    void update(Cell* agent, int x, int y, int z);
    void updatePhagosome(std::vector<Cell*>& agents, int x, int y, int z);
    void grow(int x, int y, int z, int xbin, int ybin, int zbin, Leukocyte* agent);

    int x;
    int y;
    int z;
    double p;
    int tissueType;
    std::map<int, Interactable*> interactables;
    std::map<int, Cell*> cells;
    std::map<int, InfectiousAgent*> infectiousAgents;
    static std::map<std::string, Molecule*> molecules;
    static std::map<std::string, Molecule*> infectiousAgentMolecules;
    static std::map<std::string, Molecule*> moleculeInteractable;
    std::vector<Voxel*> neighbors;
    int numSamples;
    int externalState;
    // std::mt19937_64 generator;

    static int _xbin;
    static int _ybin;
    static int _zbin;
};

} // namespace compartments
} // namespace uf
} // namespace edu

#endif /* EDU_UF_COMPARTMENTS_VOXEL_H_ */
