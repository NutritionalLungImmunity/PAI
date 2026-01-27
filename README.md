# PAI

java Pulmonary Agent-based Infection simulator

This project is a C++ and Java cross-platform project. Buidable on Windows, Linux, and macOS via terminal and has a built docker image.

## Using this project with Docker

Without installing any dependencies, this project has a ready-to-run Docker image.

- Docker version 29.1.3 (https://www.docker.com/)

### Pull the Image 

```bash
docker pull ghcr.io/nutritionallungimmunity/pai:latest
```
### Run the Container

**For C++**: 
```bash
docker run --rm ghcr.io/nutritionallungimmunity/pai:latest cpp <num_neutrophils> <num_aspergillus> <num_macrophages> <num_pneumocytes>
```
Example
```bash
docker run --rm ghcr.io/nutritionallungimmunity/pai:latest cpp 15 1920 15 640
```

**For Java**: 
```bash
docker run --rm ghcr.io/nutritionallungimmunity/pai:latest java <num_neutrophils> <num_aspergillus> <num_macrophages> <num_pneumocytes>
```
Example
```bash
docker run --rm ghcr.io/nutritionallungimmunity/pai:latest java 15 1920 15 640
```

**For C++ test**: 
```bash
docker run --rm ghcr.io/nutritionallungimmunity/pai:latest ctest
```

**For Java test**: 
```bash
docker run --rm ghcr.io/nutritionallungimmunity/pai:latest jtest
```

## To create a local image
```bash
docker build -t <myimage> .
```
Example
```bash
docker build -t paiimage .
```

Without a Docker, this project can be build and run separately.

# PAIpp

This is the C++ cross-platform project built with CMake. Buidable on Windows, Linux, and macOS via terminal.

## Build Instructions

See [INSTALL.md](INSTALL.md) in **cpp/** directory for platform-specific instructions.

### Quick Start (Unix/macOS/Linux)

```bash
git clone https://github.com/NutritionalLungImmunity/PAI.git
cd PAI/cpp
cmake -S . -B build
cmake --build build
./build/PAIpp <num_neutrophils> <num_aspergillus> <num_macrophages> <num_pneumocytes>
```

### macOS

#### Requirements

- Xcode CLI Tools (for clang++ and make)

```bash
xcode-select --install
brew install cmake
```
#### Build

```bash
cmake -S . -B build
cmake --build build
./build/PAIpp <num_neutrophils> <num_aspergillus> <num_macrophages> <num_pneumocytes>
```

### Windows (MinGW)

#### Requirements

- MinGW
- Add mingw32-make to PATH
- CMake

#### Build

```bash
cmake -G "MinGW Makefiles" -S . -B build
cmake --build build
./build/PAIpp.exe <num_neutrophils> <num_aspergillus> <num_macrophages> <num_pneumocytes>
```
Example
```bash
./build/PAIpp.exe 15 1920 15 640
```

From Command prompt or PowerShell:

```batch
cmake -G "MinGW Makefiles" -S . -B build
cmake --build build
.\build\PAIpp.exe <num_neutrophils> <num_aspergillus> <num_macrophages> <num_pneumocytes>
```
Example
```batch
.\build\PAIpp.exe 15 1920 15 640
```
### Windows (Cygwin)

#### Requirements

- Cygwin
- Add cygwin to PATH
- CMake

#### Build

```bash
cmake -G "Unix Makefiles" -S . -B build
cmake --build build
./build/PAIpp.exe <num_neutrophils> <num_aspergillus> <num_macrophages> <num_pneumocytes>
```
Example
```bash
./build/PAIpp.exe 15 1920 15 640
```
From Command prompt or PowerShell:

```batch
cmake -G "Unix Makefiles" -S . -B build
cmake --build build
.\build\PAIpp.exe <num_neutrophils> <num_aspergillus> <num_macrophages> <num_pneumocytes>
```
Example
```batch
.\build\PAIpp.exe 15 1920 15 640
```

### Linux/Unix/

#### Requirements

- g++
- cmake

```bash 
sudo apt get update
sudo apt install g++
sudo apt install cmake
```
#### Build

```bash
cmake -S . -B build
cmake --build build
./build/PAIpp <num_neutrophils> <num_aspergillus> <num_macrophages> <num_pneumocytes>
```
Example
```bash
./build/PAIpp 15 1920 15 640
```
## Clean build

```bash
rm -rf build
```

From Command prompt or PowerShell:
```batch
rmdir /s /q build
```

## Test

The script `test_paipp.py`:

- Runs the `PAIpp` simulation with two different sets of inputs
- Captures its tabular output
- Checks key quantities at specific iterations (0, 180, 360, 720, 1440, and. 2045):
  - **Aspergillus** (column 2)
  - **TNF** (column 19)
  - **Macrophages** (column 22)
- Confirms these outputs are within expected ranges.

### How to Run

Make sure both `PAIpp` and `test_paipp.py` are in the same directory. Then run:

```bash
python3 test_paipp.py
```

test_paipp.py was tested with Python version 3.7.2.

This test is designed to see if output values are within reasonable bounds. Because of the stochastic nature of the simulator, passing the test is probable but not guaranteed.


# jPAI
java Pulmonary Agent-based Infection simulator

This is the java cross-platform project build with javac. Buidable on Windows, Linux, and macOS via terminal.

- Java Runtime Environment (JRE) 8 or later  
  (Tested with Java 18.0.2)

  If you have `jPAI.jar`:

```bash
java -jar jPAI.jar <num_neutrophils> <num_aspergillus> <num_macrophages> <num_pneumocytes>
```

Example
```bash
java -jar jPAI.jar 15 1920 15 640
```

## Build Instructions

- java 1.8
- javac 1.8

### Unix/macOS/Linux

```bash
git clone https://github.com/NutritionalLungImmunity/PAI.git
cd PAI/java
javac -d bin $(find . -name "*.java")
jar cfe jPAI.jar edu.uf.main.Main -C bin .
```

### Windows

Command Prompt or PowerShell

```batch
javac -d bin $(Get-ChildItem -Path . -Filter *.java -Recurse | ForEach-Object { $_.FullName })
jar cfe jPAI.jar edu.uf.main.Main -C bin .
```

## Test

The script `test_jpai.py`:

- Runs the `jPAI.jar` simulation with two different sets of inputs
- Captures its tabular output
- Checks key quantities at specific iterations (0, 180, 360, 720, 1440, and. 2045):
  - **Aspergillus** (column 2)
  - **TNF** (column 19)
  - **Macrophages** (column 22)
- Confirms these outputs are within expected ranges.

### How to Run

Make sure both `jPAI.jar` and `test_jpai.py` are in the same directory. Then run:

```bash
python3 test_jpai.py
```

test_jpai.py was tested with Python version 3.7.2

This test is designed to see if output values are within reasonable bounds. Because of the stochastic nature of the simulator, passing the test is probable but not guaranteed.

