/*
 * Afumigatus.h
 *
 *  Created on: Jul 18, 2024
 *      Author: henriquedeassis
 */

#pragma once

#ifndef EDU_UF_INTERACTABLE_AFUMIGATUS_AFUMIGATUS_H_
#define EDU_UF_INTERACTABLE_AFUMIGATUS_AFUMIGATUS_H_

#include <set>
#include "../PositionalInfectiousAgent.h"
#include "../Internalizable.h"
#include "../Siderophore.h"






namespace edu {
namespace uf {
namespace interactable {
namespace afumigatus{

using namespace edu::uf::utils;

class Afumigatus : public PositionalInfectiousAgent, public Internalizable {
public:

	int getClassId() const override;
    static const std::string NAME;

    static const Afumigatus* DEF_OBJ;

    Afumigatus();
    Afumigatus(double xRoot, double yRoot, double zRoot, double xTip, double yTip, double zTip,
               double dx, double dy, double dz, int growthIteration, double ironPool, int status, bool root);

    virtual ~Afumigatus()override;

    virtual bool hasSiderophore(Siderophore* siderophore) override;
    virtual bool isSecretingSiderophore(Siderophore* mol) override;
    virtual bool isUptakingSiderophore(Siderophore* mol) override;

    virtual void grow(int x, int y, int z, int xbin, int ybin, int zbin, Leukocyte* phagocyte = nullptr) override;
    virtual void incIronPool(double qtty) override;
    virtual void die() override;
    virtual std::string getName() const override;
    virtual void move(int x, int y, int z, int steps) override;
    virtual int getMaxMoveSteps() override;

    virtual void updateStatus(int x, int y, int z) override;

    static void decTotalCells(int idx);
    static void incTotalCells(int idx);

    virtual bool getAspEpiInt() const;
    virtual void setAspEpiInt(bool b);

    virtual void setEngaged(bool b);
    virtual bool isEngaged() const;

    virtual int getInteractionId() const override;
    virtual bool isTime() const override;

    virtual double getIterationsToGrow() const;

    virtual void setEpithelialInhibition(double inhibition);
    virtual double getEpithelialInhibition() const;

    virtual void incHeme(double qtty);
    virtual void setHeme(double qtty);
    virtual double getHeme() const;

    virtual double getNitrogen() const;
    virtual void setNitrogen(double nitrogen);
    virtual void incNitrogen(double qtty);

    static int getTotalCells0();
    static int getTotalRestingConidia();
    static int getTotalSwellingConidia();
    static int getTotalGerminatingConidia();
    static int getTotalHyphae();
    static double getTotalIron();

    Afumigatus* getNextSepta() const;
    Afumigatus* getNextBranch() const;
    //bool isRoot() const;
    virtual void setRoot(bool isRoot);

    virtual double getxTip() const;
    virtual void setxTip(double xTip);
    virtual double getyTip() const;
    virtual void setyTip(double yTip);
    virtual double getzTip() const;
    virtual void setzTip(double zTip);

    virtual double getDx() const;
    virtual double getDy() const;
    virtual double getDz() const;

    virtual bool isInternalizing() override;

    virtual bool isGrowable() const;
    virtual void setGrowable(bool growable);

    //virtual void diffuseIron();
    void diffuseIron(Afumigatus* afumigatus = nullptr);

    virtual bool templateInteract(Interactable* interactable, int x, int y, int z) override;

    static void rm(Afumigatus* afumigatus);

private:
    Afumigatus* createAfumigatus(double xRoot, double yRoot, double zRoot, double xTip, double yTip, double zTip,
                                 double dx, double dy, double dz, int growthIteration, double ironPool, int status, bool isRoot);

    bool growing();
    Afumigatus* elongate();
    Afumigatus* branch();
    Afumigatus* branch(double prBranch);
    Afumigatus* branch(double* phi, double prBranch);

    void computeGrowthRate();


    bool hasNitrogen() const;

    static double totalIron;
    static int totalCells[5];
    static std::set<Afumigatus*> treeSepta;

    Afumigatus* nextSepta;
    Afumigatus* nextBranch;
    Afumigatus* parentSepta;
    Afumigatus* parentBranch;
    bool root;
    bool growable;
    bool branchable;
    int treeSize;

    double xTip;
    double yTip;
    double zTip;
    double dx;
    double dy;
    double dz;
    double percentGrow;
    double iterationsToGrow;
    double epithelialInhibition;
    bool aspEpiInt;
    bool engaged;

    double nitrogenPool;
    double heme;

    static int interactionId;
    static const int classId;
    //Clock* clock;
};

}
} // namespace interactable
} // namespace uf
} // namespace edu

#endif /* EDU_UF_INTERACTABLE_AFUMIGATUS_H_ */
