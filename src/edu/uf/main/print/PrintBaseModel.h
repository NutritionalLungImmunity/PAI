/*
 * PrintBaseModel.h
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#ifndef EDU_UF_MAIN_PRINT_PRINTBASEMODEL_H_
#define EDU_UF_MAIN_PRINT_PRINTBASEMODEL_H_


#include "PrintStat.h"
//#include "../../compartments/Voxel.h"
#include "../../interactable/Interactable.h"
#include "../../interactable/MIP2.h"
#include "../../interactable/Macrophage.h"
#include "../../interactable/Neutrophil.h"
#include "../../interactable/TNFa.h"
#include "../../interactable/Transferrin.h"
#include "../../interactable/afumigatus/Afumigatus.h"
#include "../../interactable/afumigatus/TAFC.h"
#include "../../interactable/Lactoferrin.h"
#include "../../interactable/TGFb.h"
#include "../../interactable/IL10.h"
#include "../../interactable/MIP1B.h"
#include "../../interactable/MIP2.h"
#include "../../intracellularState/PneumocyteStateModel.h"
#include <sstream>
//#include <memory>
//#include <iostream>

namespace edu {
namespace uf {
namespace main {
namespace print {

using namespace edu::uf::interactable;
using namespace afumigatus;
//using namespace edu::uf::compartiments;

class PrintBaseModel : public PrintStat {
public:
    //Voxel**** grid;

	virtual void printStatistics(int k, const string& file) {
        if (k % 15 != 0) return;

        ostringstream str;
        str << k << "\t"
            << Afumigatus::getTotalCells0() << "\t"
            << Afumigatus::getTotalRestingConidia() << "\t"
            << Afumigatus::getTotalSwellingConidia() << "\t"
            << Afumigatus::getTotalGerminatingConidia() << "\t"
            << Afumigatus::getTotalHyphae() << "\t"
            << (TAFC::getMolecule()->getTotalMolecule(0) + TAFC::getMolecule()->getTotalMolecule(1)) << "\t"
            << TAFC::getMolecule()->getTotalMolecule(0) << "\t"
            << TAFC::getMolecule()->getTotalMolecule(1) << "\t"
            << Lactoferrin::getMolecule()->getTotalMolecule(0) << "\t"
            << Lactoferrin::getMolecule()->getTotalMolecule(1) << "\t"
            << Lactoferrin::getMolecule()->getTotalMolecule(2) << "\t"
            << (Transferrin::getMolecule()->getTotalMolecule(0) + Transferrin::getMolecule()->getTotalMolecule(1) + Transferrin::getMolecule()->getTotalMolecule(2)) << "\t"
            << Transferrin::getMolecule()->getTotalMolecule(0) << "\t"
            << Transferrin::getMolecule()->getTotalMolecule(1) << "\t"
            << Transferrin::getMolecule()->getTotalMolecule(2) << "\t"
            << TGFb::getMolecule()->getTotalMolecule(0) << "\t"
            << IL10::getMolecule()->getTotalMolecule(0) << "\t"
            << TNFa::getMolecule()->getTotalMolecule(0) << "\t"
            << MIP1B::getMolecule()->getTotalMolecule(0) << "\t"
            << MIP2::getMolecule()->getTotalMolecule(0) << "\t"
            << Macrophage::getTotalCells() << "\t"
			<< PneumocyteStateModel::activeCount << "\t"
            << Neutrophil::getTotalCells();

        string output = str.str();

        if (file.empty()) {
            cout << output << endl;
        } else {
            try {
                if (getPrintWriter() == nullptr) {
                    setPrintWriter(new ofstream(file));
                }
                if (getPrintWriter() && getPrintWriter()->is_open()) {
                    *getPrintWriter() << output << endl;
                }
            } catch (const ofstream::failure& e) {
                cerr << "Exception writing to file: " << e.what() << endl;
            }
        }
		//System.out.println(">>> " + resting  + " " + swelling + " " + germ + " " + hyphae);
	}


};

} // namespace print
} // namespace main
} // namespace uf
} // namespace edu


#endif /* EDU_UF_MAIN_PRINT_PRINTBASEMODEL_H_ */
