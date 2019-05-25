import os
import numpy as np
from sklearn.model_selection import cross_val_score

# colors
ANSI_RESET  = "\u001B[0m"
ANSI_RED    = "\u001B[31m"
ANSI_GREEN  = "\u001B[32m"
ANSI_YELLOW = "\u001B[33m"
ANSI_BLUE   = "\u001B[34m"

# Path to Mapping output
DIR_PATH = "../../output"

# output:
# (train_src, train_labels), (test_src, test_labels)
def load_data(trainning_set_size):
	# Collect the info on how the dataset is organized (nr_rows, nr_collumns)
	dataset_shape = getDatasetShape()
	# Array 2D
	# Tem (2*nr_datasets*nr_issues) linhas.
	# Cada entrada e um array de comprimento "issue_max_size"
	dataset = np.zeros(shape=dataset_shape, dtype=int)

	# Array 1D
	# Tem (2*nr_datasets*nr_issues) entradas.
	# labels  tem que ser stored as numerical data.
	# Cada entrada tem o valor de uma das 2 labels:
	# "master" -> 1
	# "buggy"  -> 2
	labels  = np.zeros(shape=(dataset_shape[0]), dtype=int)

	idx = 0;

	# For each issue in the dataset in the output folder
	for dataset_name in list_directory(DIR_PATH):
		issues = DIR_PATH + "/" + dataset_name
		# print(issues)

		# For each issue in the dataset
		for issue in list_directory(issues):
			issue_path = issues + "/" + issue
			# print("\t",issue_path)
			
			# For each (correct, buggy) pair inside each issue folder
			for file_name in list_directory(issue_path):
				file_path = issue_path + "/" + file_name
				# print("\t\t", file_path)

				with open(file_path) as f:
					i = 0
					file_content = f.read()
					tokens = file_content.split(" ")
					tokens_size = len(tokens)

					# store parsing data
					for token in tokens: 
						if(i < tokens_size-1):
							dataset[idx, i] = int(token)
						i = i + 1

				# store label data
				if("buggy" in file_name):
					labels[idx]	= 2
				else:
					labels[idx] = 1

				idx = idx + 1;

	if(idx == labels.shape[0]):
		print(ANSI_GREEN + "[CORRECT]:"+ ANSI_RESET +" Saw as many issues as expected.[",idx,"/",labels.shape[0],"]")
	else:
		print(ANSI_YELLOW + "[WARNING]:"+ ANSI_RESET + " Some issues are being ignored. [",idx,"/",labels.shape[0],"]")

	## Uncomment to see longest row.
	# w = 0
	# while(dataset[w,dataset.shape[1]-1] == 0): 
	# 	w = w + 1;
	# print(w)
	# print(dataset[w])

	return split_dataset(dataset, labels, trainning_set_size)
	


# output:
# (nr_datasets*nr_issues, issue_max_size)
def getDatasetShape():
	root_dir    = list_directory(DIR_PATH)
	# number of folders in root dir
	nr_datasets = len(root_dir)
	nr_issues   = 0

	# for each dataset, get the number of issues
	for dataset_name in root_dir:
		nr_issues += len(list_directory(DIR_PATH + "/" + dataset_name)) # number of folders under the dataset dir

	# Read issue_max_size from	 "output/max_size.txt"
	with open(DIR_PATH+"/max_size.txt") as f:
		file_content = f.read()
		issue_max_size = int(file_content)

	dataset_shape = (2*nr_issues+1, issue_max_size)
	print("Bugs.jar shape is: ",dataset_shape)
	
	# times two(2*) == because each issue has a (correct, buggy) pair
	return dataset_shape


# Retorna listagem do diretorio em 'path'
def list_directory(path):
	directory = os.listdir(path)

	# Caso especial.
	if(path == DIR_PATH):
		directory.remove("max_size.txt")

	if ".DS_Store" in directory:
		directory.remove(".DS_Store")

	return directory

# Retorna listagem do diretorio em 'path'
def list_only_buggies_in_directory(path):
	directory = os.listdir(path)

	# Caso especial.
	if(path == DIR_PATH):
		directory.remove("max_size.txt")

	if ".DS_Store" in directory:
		directory.remove(".DS_Store")

	return [ x for x in directory if "fixed" not in x ]


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
