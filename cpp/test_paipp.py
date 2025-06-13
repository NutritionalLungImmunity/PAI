import sys
import subprocess
import os
import math
import platform


def out_of_range1(diff, k, time):
    pass_all_tests = True
    if k == 0:
        if diff != 0:
            print("Afumigatus at time-step " + str(time) + " is OUT of range!", file=sys.stderr)
            pass_all_tests = False
        else:
            print("Afumigatus at time-step " + str(time) + " is within range!")
    if k == 1:
        if diff != 0:
            print("TNF-a at time-step " + str(time) + " is OUT of range!", file=sys.stderr)
            pass_all_tests = False
        else:
            print("TNF-a at time-step " + str(time) + " is within range!")
    if k == 2:
        if diff != 0:
            print("Macrophage at time-step " + str(time) + " is OUT of range!", file=sys.stderr)
            pass_all_tests = False
        else:
            print("Macrophage at time-step " + str(time) + " is within range!")

    return pass_all_tests

def out_of_range(diff, std, k, time):
    pass_all_tests = True
    if k == 0:
        if (diff < 0 and (diff + std) < 0) or (diff > 0 and (diff - std) > 0):
            print("Afumigatus at time-step " + str(time) + " is OUT of range!", file=sys.stderr)
            pass_all_tests = False
        else:
            print("Afumigatus at time-step " + str(time) + " is within range!")
    if k == 1:
        if (diff < 0 and (diff + std) < 0) or (diff > 0 and (diff - std) > 0):
            print("TNF-a at time-step " + str(time) + " is OUT of range!", file=sys.stderr)
            pass_all_tests = False
        else:
            print("TNF-a at time-step " + str(time) + " is within range!")
    if k == 2:
        if (diff < 0 and (diff + std) < 0) or (diff > 0 and (diff - std) > 0):
            print("Macrophage at time-step " + str(time) + " is OUT of range!", file=sys.stderr)
            pass_all_tests = False
        else:
            print("Macrophage at time-step " + str(time) + " is within range!")
    return pass_all_tests

def test(f, ref_afumigatus, ref_tnf, ref_ma, ref_afumigatus_std, ref_tnf_std, ref_ma_std, times = [0, 180, 360, 720, 1440, 2145]):
    i = 0
    pass_all_tests = True
    for line in f:
        sp = line.split("\t")
        if len(sp) != 24:
            continue
        k = int(sp[0])
        if k == times[i]:
            afumigatus = float(sp[1]) + 1 if i == 0 else math.log(float(sp[1]) + 1)
            tnf = float(sp[18]) if i == 0 else math.log(float(sp[18]))
            ma = int(sp[21]) if i == 0 else math.log(float(sp[21]))
            afumigatus_diff = afumigatus - ref_afumigatus[i]
            tnf_diff = tnf - ref_tnf[i]
            ma_diff = ma - ref_ma[i]
            if i == 0:
                pass_all_tests &= out_of_range1(afumigatus_diff, 0, times[i])
                pass_all_tests &= out_of_range1(tnf_diff, 1, times[i])
                pass_all_tests &= out_of_range1(ma_diff, 2, times[i])
            else:
                pass_all_tests &= out_of_range(afumigatus_diff, afumigatus*ref_afumigatus_std[i], 0, times[i])
                pass_all_tests &= out_of_range(tnf_diff, -tnf*ref_tnf_std[i], 1, times[i])
                pass_all_tests &= out_of_range(ma_diff, ma*ref_ma_std[i], 2, times[i])
            i += 1
    f.close()
    return pass_all_tests


pass_all_tests = True

if platform.system() == "Windows":
    app_name = "./PAIpp.exe"
else:
    app_name = "./PAIpp"

with open("file.tsv", "w") as outfile:
    result=subprocess.run(
        [app_name, "15", "1920", "15", "640"],
        stdout=outfile,
        stderr=subprocess.STDOUT  # optional: combine stdout and stderr
    )

if result.returncode != 0:
    raise RuntimeError(f"Simulation failed with return code {result.returncode}")
else:
    print("Simulation completed successfully.")

f = open("file.tsv", "r")


ref_afumigatus = [1921, 7.545915, 7.155904, 5.574055, 3.244941, 2.244287]
ref_afumigatus_std = [0, 0.0025, 0.02, 0.08, 0.175, 10]

ref_tnf = [0, -40.88301, -39.95179, -40.58841, -42.87585, -44.40466]
ref_tnf_std = [0, 0.01, 0.0075, 0.01, 0.125, 0.075]

ref_ma = [15, 3.002241, 4.179416, 4.859203, 4.269298, 3.440448]
ref_ma_std = [0, 0.25, 0.125, 0.1, 0.2, 0.3]

pass_all_tests &= test(f, ref_afumigatus, ref_tnf, ref_ma, ref_afumigatus_std, ref_tnf_std, ref_ma_std)

file_path = "file.tsv"

if os.path.exists(file_path):
    os.remove(file_path)

with open("file.tsv", "w") as outfile:
    result = subprocess.run(
        [app_name, "15", "1000", "150", "640"],
        stdout=outfile,
        stderr=subprocess.STDOUT  # optional: combine stdout and stderr
    )

if result.returncode != 0:
    raise RuntimeError(f"Simulation failed with return code {result.returncode}")
else:
    print("Simulation completed successfully.")

f = open("file.tsv", "r")


ref_afumigatus = [1001, 6.8474725, 6.3017588, 4.8024016, 2.3340229, 0.7945135]
ref_afumigatus_std = [0, 0.01, 0.03, 0.1, 1, 10]

ref_tnf = [0, -41.51024, -40.32655, -41.07596, -43.61308, -45.86800]
ref_tnf_std = [0, 0.02, 0.01, 0.05, 0.075, 0.3]

ref_ma = [150, 4.821182, 4.830577, 4.815199, 4.026479, 3.162690]
ref_ma_std = [0.02, 0.075, 0.1, 0.15, 0.25, 0.25]

pass_all_tests &= test(f, ref_afumigatus, ref_tnf, ref_ma, ref_afumigatus_std, ref_tnf_std, ref_ma_std)

file_path = "file.tsv"

if os.path.exists(file_path):
    os.remove(file_path)

print("\n\n")
if pass_all_tests:
    print("PAIpp run and pass all tests!")
else:
    print("PAIpp run but failed to pass all tests!", file=sys.stderr)


