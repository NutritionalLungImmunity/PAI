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
./build/PAIpp
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
./build/PAIpp
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
./build/PAIpp.exe
```

From Command prompt or PowerShell:

```batch
cmake -G "MinGW Makefiles" -S . -B build
cmake --build build
.\build\PAIpp.exe
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
./build/PAIpp.exe
```

From Command prompt or PowerShell:

```batch
cmake -G "Unix Makefiles" -S . -B build
cmake --build build
.\build\PAIpp.exe
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
./build/PAIpp
```

## Clean build

```bash
rm -rf build
```

From Command prompt or PowerShell:
```batch
rmdir /s /q build
```

The script `test_pai.py`:

- Runs the `jPAI.jar` simulation with two different sets of inputs
- Captures its tabular output
- Checks key quantities at specific iterations (0, 180, 360, 720, 1440, and. 2045):
  - **Aspergillus** (column 2)
  - **TNF** (column 19)
  - **Macrophages** (column 22)
- Confirms these outputs are within expected ranges.

## How to Run

Make sure both `jPAI.jar` and `test_pai.py` are in the same directory. Then run:

```bash
python3 test_pai.py


