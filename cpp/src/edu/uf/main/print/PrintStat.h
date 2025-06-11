/*
 * PrintStat.h
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#ifndef EDU_UF_MAIN_PRINT_PRINTSTAT_H_
#define EDU_UF_MAIN_PRINT_PRINTSTAT_H_


#include <fstream>
#include <string>

namespace edu {
namespace uf {
namespace main {
namespace print {

using namespace std;

class PrintStat {
public:
    virtual ~PrintStat() = default;

    /**
     * Print the simulation statistics (e.g., number of macrophages, amount of TNF, etc)
     * on each iteration (k == -1) or each "k" iteration. The exact statistics depend on the
     * implementation. Prints on the screen if file is empty or into a file if provided.
     * @param k
     * @param file
     */
    virtual void printStatistics(int k, const string& file) = 0;

    virtual void close(){
    	if (pw != nullptr) {
    		pw->close();
    	    delete pw;
    	    pw = nullptr;
    	}
    }

    virtual ofstream* getPrintWriter(){
    	return pw;
    }

    virtual void setPrintWriter(ofstream* pw){
        if (this->pw != nullptr) {
            close();
        }
        this->pw = pw;
    }

protected:
    ofstream* pw = nullptr;
};

} // namespace print
} // namespace main
} // namespace uf
} // namespace edu


#endif /* EDU_UF_MAIN_PRINT_PRINTSTAT_H_ */
