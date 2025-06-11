/*
 * InitializeBaseModel.h
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#ifndef EDU_UF_MAIN_INITIALIZE_INITIALIZEBASEMODEL_H_
#define EDU_UF_MAIN_INITIALIZE_INITIALIZEBASEMODEL_H_

#include "Initialize.h"


namespace edu {
namespace uf {
namespace main {
namespace initialize {

using namespace std;
using namespace edu::uf::interactable;
using namespace edu::uf::intracellularState;

class InitializeBaseModel : public Initialize {
public:
	virtual void initializeMolecules(Diffuse* diffuse, bool verbose) override;
	virtual void initializeLiver() override;
	virtual vector<PneumocyteII*> initializePneumocytes(int numCells) override;
	virtual void initializeTypeIPneumocytes(int numCells) override;
	virtual void initializeBlood() override;
	virtual vector<Macrophage*> initializeMacrophage(int numMacrophages) override;
	virtual vector<Neutrophil*> initializeNeutrophils(int numNeut) override;
	virtual vector<Afumigatus*> infect(int numAspergillus, int status, double initIron, double sigma, bool verbose) override;
};

} // namespace initialize
} // namespace main
} // namespace uf
} // namespace edu

#endif /* EDU_UF_MAIN_INITIALIZE_INITIALIZEBASEMODEL_H_ */
