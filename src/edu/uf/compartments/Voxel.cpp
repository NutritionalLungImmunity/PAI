/*
 * Voxel.cpp
 *
 *  Created on: Jul 19, 2024
 *      Author: henriquedeassis
 */

#include "Voxel.h"
//#include "../utils/GridFactory.h"
#include <chrono>
#include "../utils/ToArray.h"
#include <algorithm>
#include <random>


namespace edu {
namespace uf {
namespace compartments {

using namespace edu::uf::interactable;
using namespace edu::uf::utils;
using namespace std;
using namespace chrono;

map<std::string, Molecule*> Voxel::molecules;
map<std::string, Molecule*> Voxel::infectiousAgentMolecules;
map<std::string, Molecule*> Voxel::moleculeInteractable;

int Voxel::_xbin = 0;
int Voxel::_ybin = 0;
int Voxel::_zbin = 0;

Voxel::Voxel(int x, int y, int z, int numSamples) : x(x), y(y), z(z), p(0.0), tissueType(EPITHELIUM), numSamples(numSamples), externalState(0) {generator = Rand::getRand().getGenerator();}

int Voxel::getX() const {
	return x;
}

void Voxel::setX(int x) {
	this->x = x;
}

int Voxel::getY() const {
	return y;
}

void Voxel::setY(int y) {
	this->y = y;
}

int Voxel::getZ() const {
	return z;
}

void Voxel::setZ(int z) {
	this->z = z;
}

double Voxel::getP() const {
	return p;
}

void Voxel::setP(double p) {
	this->p = p;
}

int Voxel::getTissueType() const {
	return tissueType;
}

void Voxel::setTissueType(int tissueType) {
	this->tissueType = tissueType;
}

void Voxel::setXbin(int xbin){
	_xbin = xbin;
}

int Voxel::getXbin(){
	return _xbin;
}

void Voxel::setYbin(int xbin){
	_ybin = xbin;
}

int Voxel::getYbin(){
	return _ybin;
}

void Voxel::setZbin(int xbin){
	_zbin = xbin;
}

int Voxel::getZbin(){
	return _zbin;
}

void Voxel::setExternalState(int state) {
	externalState = state;
}

map<int, InfectiousAgent*>& Voxel::getInfectiousAgents() {
	return infectiousAgents;
}

map<int, Interactable*>& Voxel::getInteractables() {
	return interactables;
}

map<int, Cell*>& Voxel::getCells() {
	return cells;
}

map<std::string, Molecule*>& Voxel::getMolecules() {
	return molecules;
}

vector<Voxel*>& Voxel::getNeighbors() {
	return neighbors;
}

void Voxel::setNeighbors(const vector<Voxel*>& neighbors) {
	this->neighbors = neighbors;
}

void Voxel::setCell(Cell* cell) {
    if (dynamic_cast<InfectiousAgent*>(cell)) {
        infectiousAgents[cell->getId()] = dynamic_cast<InfectiousAgent*>(cell);
    } else {
        cells[cell->getId()] = cell;
    }
    interactables[cell->getId()] = cell;
}

Cell* Voxel::removeCell(int id) {
    auto it = interactables.find(id);
    Interactable* agent = it->second;
    if (it != interactables.end()) {
    	if (Cell* cell = dynamic_cast<InfectiousAgent*>(agent)) {
    		infectiousAgents.erase(id);
    		interactables.erase(it);
    		return cell;
    	}
    	if (Cell* cell = dynamic_cast<Cell*>(agent)) {
    		cells.erase(id);
            interactables.erase(it);
            return cell;
    	}
    }
    return nullptr;
}

void Voxel::setMolecule(const string& molName, Molecule* molecule) {
    setMolecule(molName, molecule, false, false);
}

void Voxel::setMolecule(const string& molName, Molecule* molecule, bool infectiousAgentMolecule, bool moleculeInteractable) {
    molecules[molName] = molecule;
    if (infectiousAgentMolecule) {
        Voxel::infectiousAgentMolecules[molName] = molecule;
    }
    if (moleculeInteractable) {
        Voxel::moleculeInteractable[molName] = molecule;
    }
}

Molecule* Voxel::getMolecule(const string& molName) {
    auto it = molecules.find(molName);
    if (it != molecules.end()) {
        return it->second;
    }
    return nullptr;
}

void Voxel::setNeighbor(Voxel* neighbor) {
    neighbors.push_back(neighbor);
}

void Voxel::next(int x, int y, int z) {
    auto listCell = cells;
    Cell* agent = nullptr;
    for (auto& entry : listCell) {
        agent = entry.second;
        update(agent, x, y, z);
        agent->move(this->getX(), this->getY(), this->getZ(), 0);
        if (Leukocyte* leukocyte = dynamic_cast<Leukocyte*>(agent)) {
            if (!leukocyte->getPhagosome().empty()) {
                grow(x, y, z, _xbin, _ybin, _zbin, leukocyte);
            }
        }
    }
    auto listInf = infectiousAgents;
    InfectiousAgent* infAgent = nullptr;
    for (auto& entry : listInf) {
        infAgent = entry.second;
        update(infAgent, x, y, z);
        infAgent->move(this->getX(), this->getY(), this->getZ(), 0);
        infAgent->grow(x, y, z, _xbin, _ybin, _zbin);
    }
}

void Voxel::update(Cell* agent, int x, int y, int z) {
    agent->updateStatus(x, y, z);
    if (Leukocyte* phag = dynamic_cast<Leukocyte*>(agent)) {
        updatePhagosome(phag->getPhagosome(), x, y, z);
        phag->kill();
    }
}

void Voxel::updatePhagosome(vector<Cell*>& agents, int x, int y, int z) {
    for (Cell* agent : agents) {
        agent->updateStatus(x, y, z);
    }
}

void Voxel::grow(int x, int y, int z, int xbin, int ybin, int zbin, Leukocyte* agent) {
    for (Cell* phAgent : agent->getPhagosome()) {
    	InfectiousAgent* inf = static_cast<InfectiousAgent*>(phAgent);
    	inf->grow(x, y, z, xbin, ybin, zbin, agent);
    }
}

void Voxel::degrade() {
    for (auto& entry : molecules) {
        entry.second->turnOver(x, y, z);
        entry.second->computeTotalMolecule(x, y, z);
    }
}

void Voxel::interact() {

	int molSize = molecules.size();
	int cellSize = cells.size();
	int infAgSize = infectiousAgents.size();
	int infMolSize = infectiousAgentMolecules.size();
	int molMolSize = moleculeInteractable.size();

	Cell *cells[cellSize];
	Molecule *mols[molSize];
	InfectiousAgent *infectiousAgents[infAgSize];
	Molecule *infectiousMolecules[infMolSize];
	Molecule *moleculeInteractable[molMolSize];

	ToArray<int, Cell> cellConverter(cells);
	for_each(this->cells.begin(), this->cells.end(), cellConverter);

	ToArray<int, InfectiousAgent> infectiousAgentsConverter(infectiousAgents);
	for_each(this->infectiousAgents.begin(), this->infectiousAgents.end(), infectiousAgentsConverter);

	ToArray<string, Molecule> moleculesConverter(mols);
	for_each(this->molecules.begin(), this->molecules.end(), moleculesConverter);

	ToArray<string, Molecule> infectiousAgentMoleculesConverter(infectiousMolecules);
	for_each(this->infectiousAgentMolecules.begin(), this->infectiousAgentMolecules.end(), infectiousAgentMoleculesConverter);

	ToArray<string, Molecule> moleculeInteractableConverter(moleculeInteractable);
	for_each(this->moleculeInteractable.begin(), this->moleculeInteractable.end(), moleculeInteractableConverter);

    int *infIndices = new int[infAgSize];
    for(int i = 0; i < infAgSize; i++)
    	infIndices[i] = i;

    int size = numSamples == -1 ? cellSize : numSamples;
    int *cellsIndices = Rand::getRand().sample(cellSize, size);

    if (numSamples != -1) {
    	infIndices = Rand::getRand().sample(infMolSize, numSamples);
    }

    shuffle(&mols[0], &mols[molSize-1], generator);
    shuffle(&infectiousMolecules[0], &infectiousMolecules[infMolSize-1], generator);

    int l[5] = {0, 1, 2, 3, 4};
    shuffle(&l[0], &l[4], generator);
    for (int k : l) {
        switch (k) {
            case 0:
                for (int i = 0; i < molMolSize; ++i) {
                    for (int j = 0; j < molMolSize; ++j) {
                        moleculeInteractable[i]->interact(moleculeInteractable[j], x, y, z);
                    }
                }
                break;
            case 1:
                for (int i = 0; i < size; i++) {
                    if (!cells[cellsIndices[i]]->isTime()){continue;}
                    cells[cellsIndices[i]]->setExternalState(externalState);
                    for (int j = 0; j < size; j++) {
                        cells[cellsIndices[j]]->setExternalState(externalState);
                        cells[cellsIndices[i]]->interact(cells[cellsIndices[j]], x, y, z);
                    }
                }
                break;
            case 2:
            	for (int i = 0; i < size; i++) {
                    if (!cells[cellsIndices[i]]->isTime()){continue;}
                    for (int j = 0; j < molSize; ++j) {
                        cells[cellsIndices[i]]->interact(mols[j], x, y, z);
                    }
                }
                break;
            case 3:
            	for (int i = 0; i < size; i++) {
                    if (!cells[cellsIndices[i]]->isTime()) {continue;}
                    for (int j = 0; j < infAgSize; j++) {
                        cells[cellsIndices[i]]->interact(infectiousAgents[infIndices[j]], x, y, z);
                    }
                }
                break;
            case 4:
                for (int i = 0; i < infMolSize; ++i) {
                	for (int j = 0; j < infAgSize; j++) {
                        infectiousMolecules[i]->interact(infectiousAgents[infIndices[j]], x, y, z);
                    }
                }
                break;
            default:
            	printf("No such interaction:%d!\n", k);
                //cerr << "No such interaction: " << k << "!" << endl;
                exit(1);
        }
    }
    delete[] infIndices;
    delete cellsIndices;
}

string Voxel::toString() const {
    return to_string(x) + " " + to_string(y) + " " + to_string(z);
}



} // namespace compartments
} // namespace uf
} // namespace edu
