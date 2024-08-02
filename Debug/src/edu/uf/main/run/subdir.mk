################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/edu/uf/main/run/RunSingleThread.cpp 

CPP_DEPS += \
./src/edu/uf/main/run/RunSingleThread.d 

OBJS += \
./src/edu/uf/main/run/RunSingleThread.o 


# Each subdirectory must supply rules for building sources it contributes
src/edu/uf/main/run/%.o: ../src/edu/uf/main/run/%.cpp src/edu/uf/main/run/subdir.mk
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	g++ -std=c++2a -O2 -g3 -Wall -c -fmessage-length=0 -v -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


clean: clean-src-2f-edu-2f-uf-2f-main-2f-run

clean-src-2f-edu-2f-uf-2f-main-2f-run:
	-$(RM) ./src/edu/uf/main/run/RunSingleThread.d ./src/edu/uf/main/run/RunSingleThread.o

.PHONY: clean-src-2f-edu-2f-uf-2f-main-2f-run

