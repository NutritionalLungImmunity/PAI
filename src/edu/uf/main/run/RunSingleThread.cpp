/*
 * RunSingleThread.cpp
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#include "RunSingleThread.h"
#include <iostream>
#include <chrono>
#include <vector>
#include <algorithm>
#include <random>
#include <string>
#include "../../control/Exec.h"
#include "../../interactable/afumigatus/Afumigatus.h"


namespace edu {
namespace uf {
namespace main {
namespace run {

using namespace chrono;
using namespace control;
using namespace afumigatus;

long RunSingleThread::eNextTime = 0;
long RunSingleThread::diffusionTime = 0;
long RunSingleThread::recruitTime = 0;

void RunSingleThread::run(
    int iterations,
    int xbin,
    int ybin,
    int zbin,
	Recruiter** recruiters,
    int recruiterCount,
    bool printLattice,
    const string& outputFile,
    int nthreads,
    PrintStat* printStat
) {
    int L[4] = {0, 1, 2, 3};
    random_device rd;
    mt19937 g(rd());

    for (int k = 0; k < iterations; k++) {
        if (k != 0) {
            shuffle(&L[0], &L[3], g);
        }
        if (Afumigatus::getTotalCells0() > 3e5) {
            return;
        }
        for (int ii : L) {
            if (ii == 0) {
            	//auto start  = high_resolution_clock::now();
                Exec::next(xbin, ybin, zbin);
                //auto end  = high_resolution_clock::now();
                //eNextTime += duration_cast<microseconds>(end - start).count();
            } else if (ii == 1) {
            	//auto start  = high_resolution_clock::now();
            	Exec::recruit(recruiters, recruiterCount);
            	//auto end  = high_resolution_clock::now();
            	//recruitTime += duration_cast<microseconds>(end - start).count();
            } else if (ii == 2) {
            	//auto start  = high_resolution_clock::now();
            	Exec::diffusion();
            	//auto end  = high_resolution_clock::now();
            	//diffusionTime += duration_cast<microseconds>(end - start).count();
            }
        }

        printStat->printStatistics(k, outputFile);
        Exec::resetCount();
    }
}

} // namespace run
} // namespace main
} // namespace uf
} // namespace edu
