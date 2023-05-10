import os
file = open("/Users/henriquedeassis/Documents/Projects/Afumigatus/data/bayesianLearning/testExp.csv", "r")
for line in file:
	line = line.replace(",", " ")
	os.system("java -jar jISS.jar " + line)
file.close() 
