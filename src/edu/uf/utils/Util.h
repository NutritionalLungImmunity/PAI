/*
 * Util.h
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */

#ifndef EDU_UF_UTILS_UTIL_H_
#define EDU_UF_UTILS_UTIL_H_

#include <cmath>
#include <iostream>
#include <iomanip>
#include <sstream>
#include <ctime>
#include <string>
#include "Constants.h"
#include "Rand.h"
#include "../compartments/Voxel.h"
#include "../interactable/Cell.h"
#include "../interactable/Chemokine.h"

namespace edu {
namespace uf {
namespace utils {

class Util {
public:

	/*static double activationFunction(double x, double kd, double v, double b, double h = 1) {
		x = x/v;
		return h * (1 - b*exp(-(x/kd)));
	}

	static int activationFunction5(double x, double kd) {
		double d = activationFunction(x, kd,  constexprants::VOXEL_VOL, 1.0);
		if(Rand::getRand().randunif() > d)return 0;
		if(d <= 0.35)
			return 1;
		if(d <= 0.69)
			return 2;
		if(d <= 0.90)
			return 3;
		else
			return 4;
	}*/

    static double michaelianKinetics(
        double substrate1,
        double substrate2,
        double km,
        double h) {
        return michaelianKinetics(substrate1, substrate2, km, h, 1, utils::constexprants::VOXEL_VOL);
    }

    static double michaelianKinetics(
        double substrate1,
        double substrate2,
        double km,
        double h,
        double Kcat,
        double v) {

        substrate1 = substrate1 / v;
        substrate2 = substrate2 / v;
        double substrate = 0;
        double enzime = 0;

        if (substrate1 > substrate2) {
            substrate = substrate1;
            enzime = substrate2;
        } else {
            substrate = substrate2;
            enzime = substrate1;
        }

        return h * Kcat * enzime * substrate * v  / (substrate + km);
    }

    /*static std::string printServerTime() {
        std::time_t now = std::time(nullptr);
        std::tm *ltm = std::localtime(&now);
        std::ostringstream oss;
        oss << std::put_time(ltm, "%Y-%m-%d %H:%M:%S");
        return oss.str();
    }*/

    static bool activationFunction(double x, double kd) {
        return activationFunction(x, kd, utils::constexprants::VOXEL_VOL);
    }

    static int activationFunction5(double x, double kd) {
        double d = activationFunction(x, kd, utils::constexprants::VOXEL_VOL, 1.0);
        if (Rand::getRand().randunif() > d) return 0;
        if (d <= 0.35) return 1;
        if (d <= 0.69) return 2;
        if (d <= 0.90) return 3;
        return 4;
    }

    static bool activationFunction(double x, double kd, double v) {
        return activationFunction(x, kd, v, 1.0) > Rand::getRand().randunif();
    }

    static double activationFunction(double x, double kd, double v, double b) {
        return activationFunction(x, kd, v, b, 1);
    }

    static double activationFunction(double x, double kd, double v, double b, double h) {
        x = x / v;
        return h * (1 - b * std::exp(-(x / kd)));
    }

    static bool ctActivationFunction(double x, double kd, double dt) {
        return (1 - std::exp(-(x * dt / kd))) > Rand::getRand().randunif();
    }

    static double inactivationFunction1(double x, double kd, double v, double b, double h) {
        x = x / v;
        return h * (1 - b + b * std::exp(-(x / kd)));
    }

    static double tranexamicActivation(double x, double kd, double v, double p0) {
        x = x / v;
        double b = (1.0 - p0) / p0;
        return p0 * (1 + b * (1 - std::exp(-(x / kd))));
    }

    static double turnoverRate(double x, double xSystem) {
        return Util::turnoverRate(x, xSystem, utils::constexprants::TURNOVER_RATE, utils::constexprants::REL_CYT_BIND_UNIT_T, utils::constexprants::VOXEL_VOL);
    }

    static double turnoverRate(double x, double xSystem, double k, double t, double v) {
        if (x == 0 && xSystem == 0)
            return 0;
        x = x / v;
        xSystem = xSystem / v;
        double y = (x - xSystem) * std::exp(-k * t) + xSystem;
        return y / x;
    }

    static double ironTfReaction(double iron, double Tf, double TfFe) {
        double totalBindingSite = 2 * (Tf + TfFe);
        double totalIron = iron + TfFe;
        if (totalIron <= 0 || totalBindingSite <= 0)
            return 0.0;
        double relTotalIron = totalIron / totalBindingSite;
        relTotalIron = relTotalIron <= 1.0 ? relTotalIron : 1.0;
        double relTfFe = utils::constexprants::P1 * relTotalIron * relTotalIron * relTotalIron +
        		utils::constexprants::P2 * relTotalIron * relTotalIron +
				utils::constexprants::P3 * relTotalIron;
        relTfFe = relTfFe > 0.0 ? relTfFe : 0.0;
        return relTfFe;
    }

    static compartments::Voxel* findVoxel(double x, double y, double z, int xbin, int ybin, int zbin, compartments::Voxel**** grid) {
        int xx = static_cast<int>(x);
        int yy = static_cast<int>(y);
        int zz = static_cast<int>(z);
        if (xx < 0 || yy < 0 || zz < 0)
            return nullptr;
        return grid[xx % xbin][yy % ybin][zz % zbin];
    }

    static double getChemoattraction(compartments::Voxel* voxel, interactable::Cell* agent) {
        if (agent->attractedBy() == "")
            return utils::constexprants::DRIFT_BIAS;

        compartments::Chemokine* chemokine = static_cast<compartments::Chemokine*>(voxel->getMolecules().at(agent->attractedBy()));
        return chemokine->chemoatract(voxel->getX(), voxel->getY(), voxel->getZ());
    }

    static void calcDriftProbability(compartments::Voxel* voxel, interactable::Cell* agent) {
        double chemoattraction = Util::getChemoattraction(voxel, agent);

        voxel->setP(chemoattraction);

        double cumP = voxel->getP();
        for (compartments::Voxel* v : voxel->getNeighbors()) {
            chemoattraction = Util::getChemoattraction(voxel, agent);
            v->setP(chemoattraction);
            cumP += v->getP();
        }
        voxel->setP(voxel->getP() / cumP);
        for (compartments::Voxel* v : voxel->getNeighbors())
            v->setP(v->getP() / cumP);
    }

    static compartments::Voxel* getVoxel(compartments::Voxel* voxel, double P) {
        double cumP = voxel->getP();
        if (P <= cumP)
            return voxel;
        for (compartments::Voxel* v : voxel->getNeighbors()) {
            cumP += v->getP();
            if (P <= cumP)
                return v;
        }
        return nullptr;
    }

    static void printIntArray(int* array, int size) {
        for (int i = 0; i < size; ++i)
            std::cout << array[i] << " ";
        std::cout << std::endl;
    }

};

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */

#endif /* EDU_UF_UTILS_UTIL_H_ */
