import os
from bugs_dot_jar import list_directory
from bugs_dot_jar import list_only_buggies_in_directory
import filecmp

DIR_PATH = "/Users/andre/Desktop/Tese/bugPredictor/output"

# colors
ANSI_RESET  = "\u001B[0m"
ANSI_RED    = "\u001B[31m"
ANSI_GREEN  = "\u001B[32m"
ANSI_YELLOW = "\u001B[33m"
ANSI_CYAN   = "\u001B[36m"

def find_broken_mappings(doPrintNames=False):
	# ignore
	# mappings that are not considered erros
	# these depend on the context of the application
	# TODO: right now, it stores every non-diff files
	# slowly manually verify these diff's are worthing ignore or 
	# if the parser should be tweaked
	to_ignore = []
	total  = 0
	broken = 0
	
	print("Looking for Fixed and Buggy that have the same mapping (i.e. are broken):")
	# For each issue in the dataset in the output folder
	for dataset_name in list_directory(DIR_PATH):
		if(dataset_name != "accumulo"):
			continue
		local_total = 0
		local_broken = 0;
		if(dataset_name == "max_size.txt"):
			continue
		
		issues_path = DIR_PATH + "/" + dataset_name
		# For each issue in the dataset
		for issue in list_directory(issues_path):
			
			issue_path = issues_path + "/" + issue
			print(ANSI_CYAN + issue + ":" + ANSI_RESET)

			# for each buggy file present
			for file_name in list_only_buggies_in_directory(issue_path):
				# get file name after "buggy-"
				simpleFileName = file_name[6:]

				# They must preserve the path until now.
				buggy = issue_path + "/" + file_name
				# Get the correspondet fixed file path
				fixed = issue_path + "/" + "fixed-" + simpleFileName

				print(fixed)
				print(buggy)

				# The diff should be broken, or else, the mapping is broken.
				if(isNotDiff(buggy, fixed)):
					local_broken = local_broken + 1
					if(doPrintNames):
						print(ANSI_YELLOW + "WARNING: " + file_name + ANSI_RESET)
						# will not be loaded
						to_ignore.append(buggy)

				elif(doPrintNames):
					print(ANSI_GREEN + "CORRECT" + ANSI_RESET)

				local_total = local_total + 1;

		print("------------------------------------")
		print(dataset_name," :[",local_broken,"/",local_total,"]")
		print("------------------------------------")

		broken = broken + local_broken
		total  = total + local_total

	print("Total of broken mappings: [",broken,"/",total,"]")

	return to_ignore

def isNotDiff(fixed, buggy):
	return filecmp.cmp(fixed, buggy, shallow=False)

def main():
	find_broken_mappings(True)

if __name__ == "__main__":
    main()
