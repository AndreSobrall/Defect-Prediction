import os
import numpy as np

# Path to Mapping output
DIR_PATH = "../../output"

# output:
# (train_src, train_labels), (test_src, test_labels)
def load_data():
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
		issues_path = DIR_PATH + "/" + dataset_name
		# print(issues_path)

		# For each issue in the dataset
		for issue in list_directory(issues_path):
			# print(issue)
			issue_path = issues_path + "/" + issue_name
			
			# For each (correct, buggy) pair inside each issue folder
			for file_name in list_directory(issue_path):
				# print(file_name)
				file_path = issue_path + "/" + file_name
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
				elif("master" in file_name):
					labels[idx] = 1

				idx = idx + 1;

	if(idx == labels.shape[0]):
		print("[CORRECT] Saw as many issues as expected.["+idx+"\\"+labels.shape[0]+"]")
	else:
		print("[WARNING!!!]: Some issues are being ignored. ["+idx+"\\"+labels.shape[0]+"]")

	# -------------- # 
	#  split dataset #
	# -------------- # 
	# percentage: 70/30 or 80/20
	split_idx = int(labels.shape[0]*0.7)
	
	# Trainning set
	train_src 	 = dataset[0:split_idx-1]
	train_labels = labels[0:split_idx-1]

	# Test set
	test_src	 = dataset[split_idx:]
	test_labels  = labels[split_idx:]

	return (train_src, train_labels), (test_src, test_labels)


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

	# TODO: Make Parsing and Mapping Side save this info on "output/max_size.txt"
	with open(DIR_PATH+"/max_size.txt") as f:
		file_content = f.read()
		issue_max_size = int(file_content)

	print("Bugs.jar shape is: ("+ 2*nr_datasets*nr_issues+", "+ issue_max_size+")")
	
	# times two(2*) == because each issue has a (correct, buggy) pair
	return (2*nr_datasets*nr_issues, issue_max_size)


# Retorna listagem do diretorio em 'path'
def list_directory(path):
	directory = os.listdir(path)

	# Caso especial.
	if(path == DIR_PATH):
		directory.remove("max_size.txt")

	if ".DS_Store" in directory:
		directory.remove(".DS_Store")

	return directory

def main():
	print("Hello!")
	#load_data()

if __name__ == "__main__":
    main()
