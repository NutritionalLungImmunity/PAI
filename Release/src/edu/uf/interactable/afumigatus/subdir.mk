################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/edu/uf/interactable/afumigatus/Afumigatus.cpp \
../src/edu/uf/interactable/afumigatus/TAFC.cpp 

CPP_DEPS += \
./src/edu/uf/interactable/afumigatus/Afumigatus.d \
./src/edu/uf/interactable/afumigatus/TAFC.d 

OBJS += \
./src/edu/uf/interactable/afumigatus/Afumigatus.o \
./src/edu/uf/interactable/afumigatus/TAFC.o 


# Each subdirectory must supply rules for building sources it contributes
src/edu/uf/interactable/afumigatus/%.o: ../src/edu/uf/interactable/afumigatus/%.cpp src/edu/uf/interactable/afumigatus/subdir.mk
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	g++ -std=c++2a -O3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


clean: clean-src-2f-edu-2f-uf-2f-interactable-2f-afumigatus

clean-src-2f-edu-2f-uf-2f-interactable-2f-afumigatus:
	-$(RM) ./src/edu/uf/interactable/afumigatus/Afumigatus.d ./src/edu/uf/interactable/afumigatus/Afumigatus.o ./src/edu/uf/interactable/afumigatus/TAFC.d ./src/edu/uf/interactable/afumigatus/TAFC.o

.PHONY: clean-src-2f-edu-2f-uf-2f-interactable-2f-afumigatus

