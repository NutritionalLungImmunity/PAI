/*
 * Afumigatus.cpp
 *
 *  Created on: Jul 18, 2024
 *      Author: henriquedeassis
 */

#include "../afumigatus/Afumigatus.h"

#include "../Macrophage.h"
#include "../PneumocyteII.h"
#include "../../compartments/GridFactory.h"
#include "../../compartments/Voxel.h"
#include "../../intracellularState/AspergillusIntracellularModel.h"
#include "../../primitives/Interactions.h"
#include "../../utils/Constants.h"
#include "../../utils/Id.h"
#include "../../utils/LinAlg.h"
#include "../../utils/Rand.h"
#include "../../utils/Util.h"
#include "../../time/Clock.h"
#include "../afumigatus/TAFC.h"

namespace edu {
namespace uf {
namespace interactable {
namespace afumigatus{

using namespace compartments;
using namespace primitives;
using namespace utils;

const int Afumigatus::classId = Id::getId();
const string Afumigatus::NAME = "Afumigatus";
double Afumigatus::totalIron = 0;
int Afumigatus::totalCells[5] = {0};
set<Afumigatus*> Afumigatus::treeSepta;
int Afumigatus::interactionId = Id::getId();
const Afumigatus* Afumigatus::DEF_OBJ = new Afumigatus();

Afumigatus::Afumigatus()
    : PositionalInfectiousAgent(0, 0, 0, new AspergillusIntracellularModel(AspergillusIntracellularModel::RESTING_CONIDIA)),
      nextSepta(nullptr), nextBranch(nullptr), root(true), growable(true), branchable(false), treeSize(0),
      xTip(0), yTip(0), zTip(0), dx(0), dy(0), dz(0), percentGrow(1e-4), iterationsToGrow(utils::constexprants::ITER_TO_GROW),
      epithelialInhibition(2.0), aspEpiInt(true), engaged(false), nitrogenPool(0), heme(utils::constexprants::HEME_INIT_QTTY) {
    /*totalIron += 0;
    totalCells[0]++;

    //int i = AspergillusIntracellularModel::hasLifeStage(AspergillusIntracellularModel::RESTING_CONIDIA);

    //if(i >= 0)Afumigatus::totalCells[i]++;
    clock = new Clock(static_cast<int>(utils::constexprants::INV_UNIT_T), 0);*/
}

/*Afumigatus::Afumigatus(double xRoot, double yRoot, double zRoot, double xTip, double yTip, double zTip,
                       double dx, double dy, double dz, int growthIteration, double ironPool, int status, bool isroot)
    : PositionalInfectiousAgent(xRoot, yRoot, zRoot, new AspergillusIntracellularModel(status)),
      nextSepta(nullptr), nextBranch(nullptr), root(isroot), growable(true), branchable(false), treeSize(0),
      xTip(xTip), yTip(yTip), zTip(zTip), percentGrow(1e-4), iterationsToGrow(utils::constexprants::ITER_TO_GROW),
      epithelialInhibition(2.0), aspEpiInt(true), engaged(false), nitrogenPool(0), heme(utils::constexprants::HEME_INIT_QTTY) {
    double ds[] = {dx, dy, dz};
    //LinAlg::multiply(ds, 1.0 / LinAlg::norm2(ds));
*/
Afumigatus::Afumigatus(double xRoot, double yRoot, double zRoot, double xTip, double yTip, double zTip,
                           double dx, double dy, double dz, int growthIteration, double ironPool, int status, bool isroot)
: PositionalInfectiousAgent(xRoot, yRoot, zRoot, new AspergillusIntracellularModel(status)){

	this->setIronPool(ironPool);
    this->root = isroot;
    this->xTip = xTip;
    this->yTip = yTip;
    this->zTip = zTip;

    double ds[] = {dx, dy, dz};

    this->setEngulfed(false);

    //this.cfu = None

    this->growable = true;
    this->branchable = false;
    //this.growthIteration = growthIteration;

    this->nextSepta = nullptr;
    this->nextBranch = nullptr;

    this->parentSepta = nullptr;
    this->parentBranch = nullptr;

    this->heme = utils::constexprants::HEME_INIT_QTTY;

    //this.Fe = false;

    //Afumigatus::totalIron = Afumigatus::totalIron + ironPool;
    //Afumigatus::totalCells[0]++;

    //this->clock = new Clock((int) Constants.INV_UNIT_T, 0);
    this->iterationsToGrow = utils::constexprants::ITER_TO_GROW;

    this->nitrogenPool = 0.0;
    this->treeSize = 0;
    this->percentGrow = 1e-4;

    this->epithelialInhibition = 2.0;
    //this.netGermBust = 1.0;
    this->aspEpiInt = true;
    this->engaged = false;



    this->dx = ds[0];
    this->dy = ds[1];
    this->dz = ds[2];
    setEngulfed(false);

    setIronPool(ironPool);
    totalIron += ironPool;
    totalCells[0]++;

    int i = AspergillusIntracellularModel::hasLifeStage(status);

    if(i >= 0)Afumigatus::totalCells[i]++;

    //clock = new Clock(static_cast<int>(utils::constexprants::INV_UNIT_T), 0);
}

Afumigatus::~Afumigatus(){
	if(parentSepta)
		parentSepta->nextSepta = nullptr;
	if(parentBranch)
		parentBranch->nextBranch = nullptr;
}

Afumigatus* Afumigatus::createAfumigatus(double xRoot, double yRoot, double zRoot, double xTip, double yTip, double zTip,
                                         double dx, double dy, double dz, int growthIteration, double ironPool, int status, bool isRoot) {
    return new Afumigatus(xRoot, yRoot, zRoot, xTip, yTip, zTip, dx, dy, dz, growthIteration, ironPool, status, isRoot);
}

int Afumigatus::getClassId() const {
	return classId;
}

//change!!
bool Afumigatus::hasSiderophore(Siderophore* siderophore) {
	//return true;
    return dynamic_cast<TAFC*>(siderophore) != nullptr;
}

void Afumigatus::decTotalCells(int idx) {
    totalCells[idx]--;
}

void Afumigatus::incTotalCells(int idx) {
    totalCells[idx]++;
}

bool Afumigatus::getAspEpiInt() const {
    return aspEpiInt;
}

void Afumigatus::setAspEpiInt(bool b) {
    aspEpiInt = b;
}

void Afumigatus::setEngaged(bool b) {
    engaged = b;
}

bool Afumigatus::isEngaged() const {
    return engaged;
}

int Afumigatus::getInteractionId() const {
    return interactionId;
}

bool Afumigatus::isTime() const {
    return this->getClock()->toc();
}

double Afumigatus::getIterationsToGrow() const {
    return iterationsToGrow;
}

void Afumigatus::setEpithelialInhibition(double inhibition) {
    epithelialInhibition = inhibition;
}

double Afumigatus::getEpithelialInhibition() const {
    return epithelialInhibition;
}

void Afumigatus::incHeme(double qtty) {
    heme += qtty;
}

void Afumigatus::setHeme(double qtty) {
    heme = qtty;
}

double Afumigatus::getHeme() const {
    return heme;
}

double Afumigatus::getNitrogen() const {
    return nitrogenPool;
}

void Afumigatus::setNitrogen(double nitrogen) {
    nitrogenPool = nitrogen;
}

void Afumigatus::incNitrogen(double qtty) {
    nitrogenPool += qtty;
}

int Afumigatus::getTotalCells0() {
    return totalCells[0];
}

int Afumigatus::getTotalRestingConidia() {
    return totalCells[1];
}

int Afumigatus::getTotalSwellingConidia() {
    return totalCells[2];
}

int Afumigatus::getTotalGerminatingConidia() {
    return totalCells[3];
}

int Afumigatus::getTotalHyphae() {
    return totalCells[4];
}

double Afumigatus::getTotalIron() {
    return totalIron;
}

Afumigatus* Afumigatus::getNextSepta() const {
    return nextSepta;
}

Afumigatus* Afumigatus::getNextBranch() const {
    return nextBranch;
}

/*bool Afumigatus::isRoot() const {
    return root;
}*/

void Afumigatus::setRoot(bool isRoot) {
    this->root = isRoot;
}

double Afumigatus::getxTip() const {
    return xTip;
}

void Afumigatus::setxTip(double xTip) {
    this->xTip = xTip;
}

double Afumigatus::getyTip() const {
    return yTip;
}

void Afumigatus::setyTip(double yTip) {
    this->yTip = yTip;
}

double Afumigatus::getzTip() const {
    return zTip;
}

void Afumigatus::setzTip(double zTip) {
    this->zTip = zTip;
}

double Afumigatus::getDx() const {
    return dx;
}

double Afumigatus::getDy() const {
    return dy;
}

double Afumigatus::getDz() const {
    return dz;
}

bool Afumigatus::isInternalizing() {
    return getBooleanNetwork()->getState(AspergillusIntracellularModel::LOCATION) == AspergillusIntracellularModel::INTERNALIZING;
}

bool Afumigatus::isGrowable() const {
    return growable;
}

void Afumigatus::setGrowable(bool growable) {
    this->growable = growable;
}

void Afumigatus::grow(int x, int y, int z, int xbin, int ybin, int zbin, Leukocyte* phagocyte) {
    if (getBooleanNetwork()->getState(IntracellularModel::LIFE_STATUS) == IntracellularModel::DEAD)
        return;

    Voxel**** grid = GridFactory::getGrid();
    computeGrowthRate();

    if (!this->getClock()->toc())
        return;

    if (getBooleanNetwork()->getState(AspergillusIntracellularModel::LOCATION) == AspergillusIntracellularModel::FREE) {
    	Voxel* voxel = Util::findVoxel(xTip, yTip, zTip, xbin, ybin, zbin, grid);
        if (voxel != nullptr && voxel->getTissueType() != Voxel::AIR) {
            Afumigatus* nextSepta = elongate();
            if (nextSepta != nullptr)
                voxel->setCell(nextSepta);
            nextSepta = branch();
            if (nextSepta != nullptr)
                voxel->setCell(nextSepta);
        }
    }
}

bool Afumigatus::growing() {
    //percentGrow += clock->getSize() / iterationsToGrow;
	percentGrow += utils::constexprants::INV_UNIT_T / iterationsToGrow;
    //cout << clock->getSize()  << "\t" << iterationsToGrow << "\n";
    if (percentGrow >= 1.0) {
        percentGrow -= 1.0;
        return true;
    }
    return false;
}

Afumigatus* Afumigatus::elongate() {
    Afumigatus* septa = nullptr;
    if (growable) {
        if (getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) == AspergillusIntracellularModel::HYPHAE) {
            if (growing()) {
                growable = false;
                branchable = true;
                setIronPool(getIronPool() / 2.0);
                nextSepta = createAfumigatus(xTip, yTip, zTip,
                                             xTip + dx, yTip + dy, zTip + dz,
                                             dx, dy, dz, 0,
                                             0, AspergillusIntracellularModel::HYPHAE, false);
                nextSepta->parentSepta = this;
                nextSepta->setIronPool(getIronPool());
                nextSepta->setNitrogen((nitrogenPool * treeSize) / (treeSize + 1.0));
                septa = nextSepta;
            }
        } else if (getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) == AspergillusIntracellularModel::GERM_TUBE) {
            if (growing()) {
                getBooleanNetwork()->setState(AspergillusIntracellularModel::STATUS, AspergillusIntracellularModel::HYPHAE);
                incTotalCells(4);
                decTotalCells(3);
                xTip = getX() + dx;
                yTip = getY() + dy;
                zTip = getZ() + dz;
            }
        }
    }
    iterationsToGrow = utils::constexprants::ITER_TO_GROW;
    //cout << "-> " << iterationsToGrow  << "\t" << utils::constexprants::ITER_TO_GROW << "\n";
    return septa;
}

Afumigatus* Afumigatus::branch() {
    return branch(utils::constexprants::PR_BRANCH);
}

Afumigatus* Afumigatus::branch(double prBranch) {
    return branch(nullptr, prBranch);
}

Afumigatus* Afumigatus::branch(double* phi, double prBranch) {
    Afumigatus* branch = nullptr;
    if (branchable && getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) == AspergillusIntracellularModel::HYPHAE) {
        if (Rand::getRand().randunif() < prBranch) {
            if (phi == nullptr) {
                phi = new double(2 * Rand::getRand().randunif() * M_PI);
            }
            setIronPool(getIronPool() / 2.0);
            double* growthVector = new double[]{dx, dy, dz};
            int lines = 3;
            int cols = 3;
            double** base =LinAlg::gramSchimidt(getX(), getY(), getZ());
            double** baseInv = LinAlg::transpose(base, lines, cols);
            double** R = LinAlg::rotation(2 * Rand::getRand().randunif() * M_PI);
            R = LinAlg::dotProduct(base, LinAlg::dotProduct(R, baseInv, lines, cols), lines, cols);
            growthVector = LinAlg::dotProduct(R, growthVector, lines , cols);
            nextBranch = createAfumigatus(xTip, yTip, zTip,
                                          xTip + growthVector[0], yTip + growthVector[1],
                                          zTip + growthVector[2],
                                          growthVector[0], growthVector[1], growthVector[2], -1,
                                          0, AspergillusIntracellularModel::HYPHAE, false);
            nextBranch->parentBranch = this;
            nextBranch->setIronPool(getIronPool());
            branch = nextBranch;
        }
        branchable = false;
    }
    return branch;
}

void Afumigatus::computeGrowthRate() {
    double eps = 1e-16;
    double iron = getIronPool() / utils::constexprants::HYPHAE_VOL + eps;
    double heme = this->heme / utils::constexprants::HYPHAE_VOL + eps;
    iterationsToGrow = (utils::constexprants::ITER_TO_GROW * epithelialInhibition * (utils::constexprants::INTERNAL_HEME_KM * iron + utils::constexprants::INTERNAL_IRON_KM * heme + iron * heme)) / (heme * iron);
}

/*void Afumigatus::diffuseIron() {
    diffuseIron(nullptr);
}*/

void Afumigatus::rm(Afumigatus* afumigatus){
	treeSepta.erase(afumigatus);
}

void Afumigatus::diffuseIron(Afumigatus* afumigatus) {
    if (afumigatus == nullptr) {
        if (root) {
            treeSepta.clear();
            treeSepta.insert(this);
            diffuseIron(this);
            double totalIron = 0;
            double totalHeme = 0;
            double totalN = 0;
            for (Afumigatus* a : treeSepta) {
                totalIron += a->getIronPool();
                totalHeme += a->getHeme();
                totalN += a->getNitrogen();
            }
            totalIron /= treeSepta.size();
            totalHeme /= treeSepta.size();
            totalN /= treeSepta.size();
            for (Afumigatus* a : treeSepta) {
                a->setIronPool(totalIron);
                a->setHeme(totalHeme);
                a->setNitrogen(totalN);
            }
        }
    } else {
        if (afumigatus->nextSepta != nullptr && afumigatus->nextBranch == nullptr) {
            treeSepta.insert(afumigatus->nextSepta);
            diffuseIron(afumigatus->nextSepta);
        } else if (afumigatus->nextSepta != nullptr && afumigatus->nextBranch != nullptr) {
            treeSepta.insert(afumigatus->nextSepta);
            treeSepta.insert(afumigatus->nextBranch);
            diffuseIron(afumigatus->nextBranch);
            diffuseIron(afumigatus->nextSepta);
        }
    }
}

bool Afumigatus::hasNitrogen() const {
    int size = treeSize + 1;
    double nitrogen = nitrogenPool * treeSize;
    return nitrogen >= utils::constexprants::NITROGEN_THRESHOLD * size;
}

bool Afumigatus::templateInteract(Interactable* interactable, int x, int y, int z) {
    /*if (dynamic_cast<Iron*>(interactable)) {
        if (Interactions::releaseIron(this, static_cast<Molecule*>(interactable), IntracellularModel::DYING, x, y, z))
            return true;
        return Interactions::releaseIron(this, static_cast<Molecule*>(interactable), IntracellularModel::DEAD, x, y, z);
    }*/
    if (dynamic_cast<Macrophage*>(interactable)){
        return Interactions::macrophageAspergillus(static_cast<Leukocyte*>(interactable), this, x, y, z);
    }
    if (dynamic_cast<PneumocyteII*>(interactable)){
    	return Interactions::typeIIPneumocyteAspergillus(static_cast<Cell*>(interactable), this);
    }

    return interactable->interact(this, x, y, z);
}

void Afumigatus::incIronPool(double qtty) {
    setIronPool(getIronPool() + qtty);
    totalIron += qtty;
}

void Afumigatus::die() {
    if (getBooleanNetwork()->getState(IntracellularModel::LIFE_STATUS) != IntracellularModel::DEAD) {
        getBooleanNetwork()->setState(IntracellularModel::LIFE_STATUS, IntracellularModel::DEAD);
        totalCells[AspergillusIntracellularModel::hasLifeStage(getBooleanNetwork()->getState(IntracellularModel::STATUS))]--;
        totalCells[0]--;
    }
}

string Afumigatus::getName() const{
    return NAME;
}

void Afumigatus::move(int x, int y, int z, int steps) {}

int Afumigatus::getMaxMoveSteps() {
    return -1;
}

void Afumigatus::updateStatus(int x, int y, int z) {
    PositionalInfectiousAgent::updateStatus(x, y, z);
    diffuseIron();
}

bool Afumigatus::isSecretingSiderophore(Siderophore* mol) {
    if (getBooleanNetwork()->getState(AspergillusIntracellularModel::LOCATION) == AspergillusIntracellularModel::FREE &&
        getBooleanNetwork()->getState(IntracellularModel::LIFE_STATUS) != IntracellularModel::DYING &&
        getBooleanNetwork()->getState(AspergillusIntracellularModel::LIFE_STATUS) != IntracellularModel::DEAD) {
        if (getBooleanNetwork()->hasPhenotype(AspergillusIntracellularModel::SECRETING_TAFC) &&
            (getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) == AspergillusIntracellularModel::SWELLING_CONIDIA ||
             getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) == AspergillusIntracellularModel::HYPHAE ||
             getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) == AspergillusIntracellularModel::GERM_TUBE)) {
            return true;
        }
    }
    return false;
}

bool Afumigatus::isUptakingSiderophore(Siderophore* mol) {
    if (getBooleanNetwork()->getState(IntracellularModel::LOCATION) == AspergillusIntracellularModel::FREE &&
        getBooleanNetwork()->getState(IntracellularModel::LIFE_STATUS) != IntracellularModel::DYING &&
        getBooleanNetwork()->getState(AspergillusIntracellularModel::LIFE_STATUS) != IntracellularModel::DEAD) {
        if (getBooleanNetwork()->hasPhenotype(AspergillusIntracellularModel::UPTAKING_TAFC) &&
            (getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) == AspergillusIntracellularModel::SWELLING_CONIDIA ||
             getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) == AspergillusIntracellularModel::HYPHAE ||
             getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) == AspergillusIntracellularModel::GERM_TUBE)) {
            return true;
        }
    }
    return false;
}

}
} // namespace interactable
} // namespace uf
} // namespace edu
