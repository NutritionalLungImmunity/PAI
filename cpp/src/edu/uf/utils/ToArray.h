/*
 * ToArray.h
 *
 *  Created on: Jul 30, 2024
 *      Author: henriquedeassis
 */

#ifndef EDU_UF_UTILS_TOARRAY_H_
#define EDU_UF_UTILS_TOARRAY_H_

namespace edu {
namespace uf {
namespace utils {

using namespace std;


template <typename T1, typename T2>
class ToArray{
public:
	ToArray(T2** array){
		myArray = array;
	}

	bool operator () (const pair<T1,T2*> mapVal) {
		myArray[k++] = mapVal.second;
		return true;
	}

	void reset(){
		k = 0;
	}

private:
	int k = 0;
	T2** myArray;
};

}
}
}

#endif /* EDU_UF_UTILS_TOARRAY_H_ */
