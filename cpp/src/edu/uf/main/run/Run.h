/*
 * Run.h
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#ifndef EDU_UF_MAIN_RUN_RUN_H_
#define EDU_UF_MAIN_RUN_RUN_H_

#include <vector>
#include <stdexcept>
#include <fstream>
#include "../../compartments/Recruiter.h"
#include "../print/PrintStat.h"

namespace edu {
namespace uf {
namespace main {
namespace run{

using namespace std;
using namespace edu::uf;
using namespace main;
using namespace compartments;
using namespace print;

class Run {
public:

	virtual ~Run() = default;

    static const std::vector<int> L;

    virtual void run(
        int iterations,
        int xbin,
        int ybin,
        int zbin,
		std::vector<Recruiter*> recruiters,
        int recruiterCount,
        bool printLattice,
        const string& outputFile,
        int nthreads,
		PrintStat* printStat
    ) = 0;

};

}
} // namespace
} // namespace uf
} // namespace edu


#endif /* EDU_UF_MAIN_RUN_RUN_H_ */
