################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/edu/uf/main/initialize/Initialize.cpp \
../src/edu/uf/main/initialize/InitializeBaseModel.cpp 

CPP_DEPS += \
./src/edu/uf/main/initialize/Initialize.d \
./src/edu/uf/main/initialize/InitializeBaseModel.d 

OBJS += \
./src/edu/uf/main/initialize/Initialize.o \
./src/edu/uf/main/initialize/InitializeBaseModel.o 


# Each subdirectory must supply rules for building sources it contributes
src/edu/uf/main/initialize/%.o: ../src/edu/uf/main/initialize/%.cpp src/edu/uf/main/initialize/subdir.mk
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -O3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


clean: clean-src-2f-edu-2f-uf-2f-main-2f-initialize

clean-src-2f-edu-2f-uf-2f-main-2f-initialize:
	-$(RM) ./src/edu/uf/main/initialize/Initialize.d ./src/edu/uf/main/initialize/Initialize.o ./src/edu/uf/main/initialize/InitializeBaseModel.d ./src/edu/uf/main/initialize/InitializeBaseModel.o

.PHONY: clean-src-2f-edu-2f-uf-2f-main-2f-initialize

