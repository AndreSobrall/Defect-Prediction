# --------------------- #
#	machine learning	#
# --------------------- #

import numpy as np
from sklearn.model_selection import cross_val_score

# --------------------- #
#	data loading func	#
# --------------------- #

from aux_func import list_directory
from aux_func import list_only_buggies_in_directory
from mapping_test import find_broken_mappings

# colors
ANSI_RESET  = "\u001B[0m"
ANSI_RED    = "\u001B[31m"
ANSI_GREEN  = "\u001B[32m"
ANSI_YELLOW = "\u001B[33m"
ANSI_CYAN   = "\u001B[36m"

# Path to Mapping output
DIR_PATH = "C:\\Users\\Andre\\Desktop\\ist\\Defect-Prediction\\bugPredictor\\output"

# output:
# [[name1, dataset1], [name2, dataset2], ......, [name_n, datasetn]]
def load_data(trainning_set_size):
	datasets = []
	# For each dataset in the bugs.jar mapped to the output folder
	for dataset_name in list_directory(DIR_PATH):
		datasets.append([dataset_name, load_dataset(dataset_name, trainning_set_size)])
	return datasets

# output:
# (train_src, train_labels), (test_src, test_labels)
def load_dataset(dataset_name, trainning_set_size):
	# Dataset Path	
	dataset_path = DIR_PATH + "/" + dataset_name

	# Collect the info on how the dataset is organized (nr_rows, nr_collumns)
	# And the files that contain erroneous mappings
	files_to_ignore, dataset_shape = getDatasetMetadata(dataset_path, dataset_name)

	# Array 2D
	# Tem (2*nr_issues) linhas.
	# Cada entrada e um array de comprimento "issue_max_size"
	dataset = np.zeros(shape=dataset_shape, dtype=int)

	# Array 1D
	# Tem (2*nr_datasets*nr_issues) entradas.
	# labels  tem que ser stored as numerical data.
	# Cada entrada tem o valor de uma das 2 labels:
	# "fixed" -> 1
	# "buggy"  -> 2
	labels  = np.zeros(shape=(dataset_shape[0]), dtype=int)

	# Counts number of relevant issues loaded
	# Goes from 0 -> (labels.shape[0]-1)
	idx = 0;

	# Counts number of issues that should be ignored
	# Goes from 0 -> len(files_to_ignore)
	ignore = 0;

	# For each issue in the dataset
	for issue in list_directory(dataset_path):
		issue_path = dataset_path + "/" + issue
		
		# for each buggy file present
		for buggy_name in list_only_buggies_in_directory(issue_path):

			# They must preserve the path until now.
			buggy = issue_path + "/" + buggy_name
			
			# files to ignore
			if buggy in files_to_ignore:
				ignore = ignore + 1 
				continue

			# get file name after "buggy-"
			simpleFileName = buggy_name[6:]

			# Get the correspondet fixed file path
			fixed_name = "fixed-" + simpleFileName
			fixed = issue_path + "/" + fixed_name
			
			dataset, labels, idx = storeParsingData(buggy, buggy_name, dataset, labels, idx)
			dataset, labels, idx = storeParsingData(fixed, fixed_name, dataset, labels, idx)
	
	# Just some asserts, to make sure everything was read
	# Verifies total number of issues read
	total_nr_issues = (labels.shape[0])
	if(idx == total_nr_issues):
		print(ANSI_GREEN + "[CORRECT]:"+ ANSI_RESET +" Saw as many issues as expected.[",idx,"/",total_nr_issues,"]")
	else:
		print(ANSI_YELLOW + "[WARNING]:"+ ANSI_RESET + " Some relevant issues are being ignored. [",idx,"/",total_nr_issues,"]")
    
    # Verifies total number of issues ignored
	if(ignore != len(files_to_ignore)):
		print(ANSI_YELLOW + "[WARNING]:"+ ANSI_RESET + " Some issues are being ignored. [",ignore,"/",len(files_to_ignore),"]")

	# # Uncomment to see longest row.
	# w = 0
	# while(dataset[w,dataset.shape[1]-1] == 0): 
	# 	w = w + 1;
	# print(w)
	# print(dataset[w])

	return split_dataset(dataset, labels, trainning_set_size)
	
def readFile(file_path):
	with open(file_path) as f:
		file_content = f.read()
		return file_content.split(" ")

def storeParsingData(file_path, file_name, dataset, labels, idx):
	i = 0
	tokens = readFile(file_path)
	tokens_size = len(tokens)
	for token in tokens: 
		if(i < tokens_size-1):
			# stores parsing data
			dataset[idx, i] = int(token)
		i = i + 1

	# store fixed label data
	if "buggy-" in file_name:
		labels[idx]	= 1
	
	# inc index
	idx = idx + 1
	return dataset, labels, idx


# output:
# (files_to_ignore, dataset_shape)
def getDatasetMetadata(dataset_path, dataset_name):
	(files_to_ignore, nr_issues) = find_broken_mappings(dataset_path, dataset_name)
	
	# Read issue_max_size from	 "output/max_size.txt"
	with open(dataset_path+"/max_size.txt") as f:
		line = f.readline() # reads only first line
		file_content = line.split(" ")
		issue_max_size = int(file_content[0])

	dataset_shape = (nr_issues+1, issue_max_size)
	print(dataset_name," shape is: ",dataset_shape)
	
	return (files_to_ignore, dataset_shape)

def getNumberOfFeatures(dataset_name):
	# Read issue_max_size from	 "output/max_size.txt"
	dataset_path = DIR_PATH + "/" + dataset_name
	with open(dataset_path+"/max_size.txt") as f:
		line = f.readline() # reads only first line
		file_content = line.split(" ")
		nr_features = int(file_content[1])
	return nr_features

# -------------- # 
#  split dataset #
# -------------- # 
#
# Splits the data into trainning and test sets
# according to "trainning_set_size" parameter

def split_dataset(dataset, labels, trainning_set_size):
	# Check train_set_size, atleast 50%
	if(trainning_set_size <= 0.5 or trainning_set_size >= 1):
		old = train_set_size
		trainning_set_size = 0.7
		print("Trainning set size is not a value between 0 < x < 1.")
		print("Setting to default. ",old," -> ", trainning_set_size)

	# percentage according to trainning_set_size
	split_idx = int(labels.shape[0]*trainning_set_size)
	
	# Trainning set
	train_src 	 = dataset[0:split_idx-1]
	train_labels = labels[0:split_idx-1]

	# Test set
	test_src	 = dataset[split_idx:]
	test_labels  = labels[split_idx:]

	return (train_src, train_labels), (test_src, test_labels)

def main():
	(train_src, train_labels), (test_src, test_labels) = load_data(0.7)

if __name__ == "__main__":
    main()
