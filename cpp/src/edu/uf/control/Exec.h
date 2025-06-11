/*
 * Exec.h
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */
#pragma once

#ifndef EDU_UF_CONTROL_EXEC_H_
#define EDU_UF_CONTROL_EXEC_H_


#include <list>
#include <map>
#include <iterator>
#include "../compartments/GridFactory.h"
#include "../compartments/Recruiter.h"
#include "../compartments/Voxel.h"
#include "../interactable/Molecule.h"
#include "../intracellularState/IntracellularModel.h"
#include "../interactable/Cell.h"
#include "../interactable/InfectiousAgent.h"
#include "../interactable/Interactable.h"
#include "../interactable/Internalizable.h"
#include "../interactable/Leukocyte.h"
#include "../interactable/afumigatus/Afumigatus.h"
#include "../utils/Constants.h"
#include "../utils/Rand.h"

#include <chrono>


namespace edu {
namespace uf {
namespace control {

using namespace std;
using namespace edu::uf::compartments;
using namespace edu::uf::interactable;
using namespace afumigatus;
using namespace chrono;

class Exec {
public:

    static void next(int xbin, int ybin, int zbin){
    	Voxel**** grid = GridFactory::getGrid();
    	for (int x = 0; x < xbin; x++) {
    		for (int y = 0; y < ybin; y++) {
    			for (int z = 0; z < zbin; z++) {
    				grid[x][y][z]->interact();
    	            Exec::gc(grid[x][y][z]);
    	            grid[x][y][z]->next(x, y, z);
    	            grid[x][y][z]->degrade();
    			}
    		}
    	}
    }

    static void recruit(std::vector<Recruiter*> recruiters, int recruiterCount){
    	for (int i = 0; i < recruiterCount; i++) {
    		recruiters[i]->recruit();
    	}
    }

    static void diffusion(){
    	for (Molecule* mol : Exec::molecules) {
    		mol->diffuse();
    	}
    }

    static void setMolecule(Molecule* mol){
    	molecules.push_back(mol);
    }

    static void resetCount(){
    	//Liver::getLiver()->reset();
    	for (Molecule* mol : molecules) {
    		mol->resetCount();
    		/*if (Setter* setter = dynamic_cast<Setter*>(mol)) {
    			setter->update();
    		}*/
    	}
    }

protected:
    static void gc(Voxel* voxel){
        map<int, Interactable*> tmpAgents(voxel->getInteractables().begin(), voxel->getInteractables().end());
        for (auto& entry : tmpAgents) {
            if (Cell* v = dynamic_cast<Cell*>(entry.second)) {
                //if (dynamic_cast<Liver*>(v)) continue; // Dirty hack!
                if (v->getBooleanNetwork()->getState(IntracellularModel::LIFE_STATUS) == IntracellularModel::DEAD && v->removeUponDeath()) {
                	if (Leukocyte* leukocyte = dynamic_cast<Leukocyte*>(v)) {
                        vector<Cell*>& phagosome = leukocyte->getPhagosome();
                        Exec::releasePhagosome(phagosome, voxel);
                    }
                    voxel->removeCell(entry.first);
                    Cell::remove(v->getId());
                    delete v; // Deleting the object after removal
                } else if (Leukocyte* leukocyte = dynamic_cast<Leukocyte*>(v)) {
                    vector<Cell*>& phagosome = leukocyte->getPhagosome();
                    for (auto it = phagosome.begin(); it != phagosome.end(); ) {
                        Cell* c = *it;
                        if (c->getBooleanNetwork()->getState(IntracellularModel::LIFE_STATUS) == IntracellularModel::DEAD) {
                        	it = phagosome.erase(it);
                            Cell::remove(c->getId());
                            delete c; // Deleting the object after removal
                        } else {
                            ++it;
                        }
                    }
                } else if (Internalizable* internalizable = dynamic_cast<Internalizable*>(v)) {
                    if (internalizable->isInternalizing()) {
                        voxel->removeCell(entry.first);
                    }
                }
            }
        }
    }

    static void releasePhagosome(vector<Cell*>& phagosome, Voxel* voxel){
    	for (auto it = phagosome.begin(); it != phagosome.end(); ) {
    		Cell* entry = *it;
    		if (entry->getBooleanNetwork()->getState(IntracellularModel::LIFE_STATUS) == IntracellularModel::DEAD) {
    			it = phagosome.erase(it);
    			Cell::remove(entry->getId());
    			//if(Afumigatus* a = dynamic_cast<Afumigatus*>(entry))Afumigatus::rm(a); // Dirty hack!
    			delete entry; // Deleting the object after removal
    		} else {
    			voxel->setCell(entry);
    			++it;
    	    }
    	}
    }

private:
    static vector<Molecule*> molecules;
};

} // namespace control
} // namespace uf
} // namespace edu


#endif /* EDU_UF_CONTROL_EXEC_H_ */
