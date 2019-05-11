import os

DIR_PATH = "../../output"

def load_data():
	datasets = {}
	# For each issue in the dataset in the output folder
	for dataset_name in list_directory(DIR_PATH):
		datasets[dataset_name] = []
		issues_path = getPathToIssues(dataset_name)
		print(issues_path)
		# For each issue in the dataset
		for issue in list_directory(issues_path):
			print(issue)
			issue_path = getPathToIssue(issues_path, issue)
			# Create a tuple(correct, buggy)
			issue_content = []
			for file_name in list_directory(issue_path):
				print(file_name)
				file_path = getFilePath(issue_path, file_name)
				with open(file_path) as f:
					issue_content.append(getIntegerMapping(f.read()))
			# Store issue content in datasets container
			datasets[dataset_name].append(issue_content)
	# print(datasets)


def getIntegerMapping(file_content):
	mapping = []
	i = 0
	tokens = file_content.split(" ")
	tokens_size = len(tokens)
	for token in tokens: 
		if(i < tokens_size-1):
			mapping.append(int(token))
		i = i + 1
	return mapping

def list_directory(path):
	directory = os.listdir(path)
	if ".DS_Store" in directory:
		directory.remove(".DS_Store")
	return directory

def getPathToIssues(dataset_name):
	return DIR_PATH + "/" + dataset_name

def getPathToIssue(issues_path, issue_name):
	return issues_path + "/" + issue_name

def getFilePath(issue_path, file_name):
	return issue_path + "/" + file_name	


def main():
	load_data()

if __name__ == "__main__":
    main()
