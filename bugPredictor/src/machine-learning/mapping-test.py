import os
from bugs_dot_jar import list_directory
import filecmp

DIR_PATH = "/Users/andre/Desktop/Tese/bugPredictor/output"

def find_broken_mappings(doPrintNames=False):
	
	total = 0
	broken = 0
	print("Looking for Fixed and Buggy that have the same mapping (i.e. are broken):")
	# For each issue in the dataset in the output folder
	for dataset_name in list_directory(DIR_PATH):
		local_total = 0
		local_broken = 0;
		if(dataset_name == "max_size.txt"):
			continue
		
		issues_path = DIR_PATH + "/" + dataset_name
		# For each issue in the dataset
		for issue in list_directory(issues_path):
			
			issue_path = issues_path + "/" + issue

			files = []
			for file_name in list_directory(issue_path):
				files.append(issue_path + "/" + file_name)
			
			if(isNotDiff(files[0], files[1])):
				local_broken = local_broken + 1
				if(doPrintNames):
					print(issue_path)

			local_total = local_total + 1;

		print("------------------------------------")
		print(dataset_name," :[",local_broken,"/",local_total,"]")
		print("------------------------------------")

		broken = broken + local_broken
		total  = total + local_total

	print("Total of broken mappings: [",broken,"/",total,"]")

def isNotDiff(fixed, buggy):
	return filecmp.cmp(fixed, buggy, shallow=False)

def main():
	find_broken_mappings(True)

if __name__ == "__main__":
    main()
