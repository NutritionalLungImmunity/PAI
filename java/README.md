# jPAI
java Pulmonary Agent-based Infection simulator

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
