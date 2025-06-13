# PAIpp

This project is a C++ cross-platform project built with CMake. Buidable on Windows, Linux, and macOS via terminal.

## Build Instructions

See [INSTALL.md](INSTALL.md) for platform-specific instructions.

### Quick Start (Unix/macOS/Linux)

```bash
git clone https://github.com/...
cd PAI++
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

- MinGW-w64
- Add mingw32-make to PATH
- CMake

#### Build

```bash
cmake -G "MinGW Makefiles" -S . -B build
cmake --build build
./build/PAIpp.exe <num_neutrophils> <num_aspergillus> <num_macrophages> <num_pneumocytes>
```

From Command prompt or PowerShell:

```batch
cmake -G "MinGW Makefiles" -S . -B build
cmake --build build
.\build\PAIpp.exe <num_neutrophils> <num_aspergillus> <num_macrophages> <num_pneumocytes>
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

From Command prompt or PowerShell:

```batch
cmake -G "Unix Makefiles" -S . -B build
cmake --build build
.\build\PAIpp.exe <num_neutrophils> <num_aspergillus> <num_macrophages> <num_pneumocytes>
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


