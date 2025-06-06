################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/edu/uf/main/Main.cpp 

CPP_DEPS += \
./src/edu/uf/main/Main.d 

OBJS += \
./src/edu/uf/main/Main.o 


# Each subdirectory must supply rules for building sources it contributes
src/edu/uf/main/%.o: ../src/edu/uf/main/%.cpp src/edu/uf/main/subdir.mk
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -O3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


clean: clean-src-2f-edu-2f-uf-2f-main

clean-src-2f-edu-2f-uf-2f-main:
	-$(RM) ./src/edu/uf/main/Main.d ./src/edu/uf/main/Main.o

.PHONY: clean-src-2f-edu-2f-uf-2f-main

